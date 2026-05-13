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
import zone.bears.platter.tile.*;

import javax.annotation.Nullable;


public class PlatterBlock extends Block implements EntityBlock {

    private static final BlockBehaviour.Properties normalProperties = Properties.of().strength(1.0f);
    private final static VoxelShape RENDER_SHAPE = Shapes.box(0.0, 0.0, 0.0, 1.0, 2.0 / 16.0, 1.0);

    public PlatterBlock(MapColor mapColor, String blockName) {
        super(normalProperties.mapColor(mapColor).setId(ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(Platter.MODID, blockName))));
    }

    @Override
    protected InteractionResult useItemOn(ItemStack itemStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (!level.isClientSide() && !player.isCrouching() && level.getBlockEntity(pos) instanceof PlatterTile platterTile) {
            try (Transaction rootTransaction = Transaction.openRoot()) {
                if (platterTile.itemStackHandler.hasFreeSpace()) {
                    int inserted = platterTile.itemStackHandler.pushStack(ItemResource.of(itemStack), itemStack.count());
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
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult blockHitResult) {
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
    public BlockState playerWillDestroy(Level worldIn, BlockPos pos, BlockState state, Player player) {
        if (!worldIn.isClientSide() && worldIn.getBlockEntity(pos) instanceof PlatterTile platterTile) {
            Containers.dropContents(worldIn, pos, platterTile.itemStackHandler.copyToList());
        }

        return super.playerWillDestroy(worldIn, pos, state, player);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return (iLevel, iBlockPos, iBlockState, iBlockEntity) -> {
            if (iBlockEntity instanceof PlatterTile tile) tile.tickServer();
        };
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        if ((blockState.getBlock().equals(Registration.stone_platter_block.get())) || (blockState.getBlock().equals(Registration.iron_platter_block.get())) || (blockState.getBlock().equals(Registration.gold_platter_block.get()))) {
            return new PersonalPlatterTile(blockPos, blockState);
        }
        return new PlatterTile(blockPos, blockState);
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos, Direction direction) {
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
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return RENDER_SHAPE;
    }

    @Override
    public VoxelShape getOcclusionShape(BlockState state) {
        return RENDER_SHAPE;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return RENDER_SHAPE;
    }
}
