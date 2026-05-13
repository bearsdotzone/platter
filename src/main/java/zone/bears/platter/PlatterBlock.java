package zone.bears.platter;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Mth;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import org.jspecify.annotations.NonNull;
import zone.bears.platter.tile.PersonalPlatterTile;
import zone.bears.platter.tile.PlatterTile;

import javax.annotation.Nullable;
import java.util.Arrays;


public class PlatterBlock extends Block implements EntityBlock {

    private static final BlockBehaviour.Properties normalProperties = Properties.of().strength(1.0f);
    private final static VoxelShape RENDER_SHAPE = Shapes.box(0.0, 0.0, 0.0, 1.0, 2.0 / 16.0, 1.0);

    public PlatterBlock(MapColor mapColor, String blockName) {
        super(normalProperties.mapColor(mapColor)
                .setId(ResourceKey.create(Registries.BLOCK,
                        Identifier.fromNamespaceAndPath(Platter.MODID, blockName))));
    }

    @Override
    protected @NonNull InteractionResult useItemOn(@NonNull ItemStack itemStack,
                                                   @NonNull BlockState state,
                                                   Level level,
                                                   @NonNull BlockPos pos,
                                                   @NonNull Player player,
                                                   @NonNull InteractionHand hand,
                                                   @NonNull BlockHitResult hitResult) {
        if (!level.isClientSide() && !player.isCrouching() && level.getBlockEntity(pos) instanceof PlatterTile platterTile) {
            try (Transaction rootTransaction = Transaction.openRoot()) {
                if (platterTile.itemStackHandler.hasFreeSpace()) {
                    int inserted = platterTile.itemStackHandler.pushStack(ItemResource.of(itemStack),
                            itemStack.count());
                    if (!player.isCreative()) player.getMainHandItem().shrink(inserted);
                    level.sendBlockUpdated(pos, state, state, 2);
                    rootTransaction.commit();
                    return InteractionResult.SUCCESS;
                }
            }
        }
        return InteractionResult.TRY_WITH_EMPTY_HAND;
    }

    @Override
    protected @NonNull InteractionResult useWithoutItem(@NonNull BlockState state,
                                                        Level level,
                                                        @NonNull BlockPos pos,
                                                        @NonNull Player player,
                                                        @NonNull BlockHitResult blockHitResult) {
        if (!level.isClientSide() && player.isCrouching() && level.getBlockEntity(pos) instanceof PlatterTile platterTile) {
            try (Transaction rootTransaction = Transaction.openRoot()) {
                if (platterTile.itemStackHandler.topItemResource() != ItemResource.EMPTY) {
                    ItemStack extracted = platterTile.itemStackHandler.popStack();
                    player.addItem(extracted);
                    level.sendBlockUpdated(pos, state, state, 2);
                    rootTransaction.commit();
                }
            }
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    public @NonNull BlockState playerWillDestroy(Level worldIn,
                                                 @NonNull BlockPos pos,
                                                 @NonNull BlockState state,
                                                 @NonNull Player player) {
        if (!worldIn.isClientSide() && worldIn.getBlockEntity(pos) instanceof PlatterTile platterTile) {
            Containers.dropContents(worldIn, pos, platterTile.itemStackHandler.copyToList());
        }

        return super.playerWillDestroy(worldIn, pos, state, player);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NonNull Level pLevel,
                                                                  @NonNull BlockState pState,
                                                                  @NonNull BlockEntityType<T> pBlockEntityType) {
        return (iLevel, iBlockPos, iBlockState, iBlockEntity) -> {
            if (iBlockEntity instanceof PlatterTile tile) tile.tickServer();
        };
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NonNull BlockPos blockPos, @NonNull BlockState blockState) {
        if (Arrays.stream(Registration.personal_platter_blocks)
                .anyMatch((i) -> i.get().equals(blockState.getBlock()))) {
            return new PersonalPlatterTile(blockPos, blockState);
        }
        return new PlatterTile(blockPos, blockState);
    }

    @Override
    public boolean hasAnalogOutputSignal(@NonNull BlockState state) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(@NonNull BlockState state,
                                     Level level,
                                     @NonNull BlockPos pos,
                                     @NonNull Direction direction) {
        // The utility class wasn't working.
        if (level.getBlockEntity(pos) instanceof PlatterTile platterTile) {
            float slotsFilled = 0;
            for (ItemStack i : platterTile.itemStackHandler.copyToList()) {
                slotsFilled += Math.clamp(i.count() / (float) i.getMaxStackSize(), 0, 1);
            }
            return Mth.lerpDiscrete(slotsFilled / platterTile.itemStackHandler.size(), 0, 15);
        }
        return 0;
    }

    @Override
    public @NonNull VoxelShape getShape(@NonNull BlockState state,
                                        @NonNull BlockGetter worldIn,
                                        @NonNull BlockPos pos,
                                        @NonNull CollisionContext context) {
        return RENDER_SHAPE;
    }

    @Override
    public @NonNull VoxelShape getOcclusionShape(@NonNull BlockState state) {
        return RENDER_SHAPE;
    }

    @Override
    public @NonNull VoxelShape getCollisionShape(@NonNull BlockState state,
                                                 @NonNull BlockGetter worldIn,
                                                 @NonNull BlockPos pos,
                                                 @NonNull CollisionContext context) {
        return RENDER_SHAPE;
    }
}
