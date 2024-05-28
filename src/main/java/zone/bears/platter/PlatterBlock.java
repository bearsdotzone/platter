package zone.bears.platter;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.world.Containers;
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
import net.neoforged.neoforge.items.ItemHandlerHelper;
import zone.bears.platter.tile.*;

import javax.annotation.Nullable;


public class PlatterBlock extends Block implements EntityBlock {

    private static final BlockBehaviour.Properties normalProperties = Properties.of()
                                                                                .strength(1.0f);

    public PlatterBlock(MapColor mapColor) {
        super(normalProperties.mapColor(mapColor));
    }


    public InteractionResult useWithoutItem(BlockState state, Level worldIn, BlockPos pos, Player player, BlockHitResult blockHitResult) {
        if (!worldIn.isClientSide()) {
            ItemStack playerHand = player.getMainHandItem();
            if (worldIn.getBlockEntity(pos) instanceof PlatterTile platterTile) {
                if (platterTile.itemStackHandler != null) {
                    if (player.isCrouching()) {
                        for (int i = platterTile.itemStackHandler.getSlots() - 1; i >= 0; i--) {
                            ItemStack found = platterTile.itemStackHandler.getStackInSlot(i);
                            if (!found.isEmpty()) {
                                ItemStack toRemove = platterTile.itemStackHandler.extractItem(i, found.getCount(), false);
                                ItemHandlerHelper.giveItemToPlayer(player, toRemove);
                                platterTile.setChanged();
                                worldIn.sendBlockUpdated(pos, state, state, 2);
                                return InteractionResult.SUCCESS;
                            }
                        }
                    } else {
                        ItemStack toInsert = ItemHandlerHelper.insertItemStacked(platterTile.itemStackHandler, playerHand.copy(), false);
                        if (!player.isCreative()) {
                            playerHand.shrink(playerHand.getCount() - toInsert.getCount());
                        }
                        platterTile.setChanged();
                        worldIn.sendBlockUpdated(pos, state, state, 2);
                    }
                }
            }
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    public BlockState playerWillDestroy(Level worldIn, BlockPos pos, BlockState state, Player player) {
        if (!worldIn.isClientSide()) {
            NonNullList<ItemStack> toDrop = NonNullList.createWithCapacity(Config.PLATTER_SLOTS.getAsInt());
            if (worldIn.getBlockEntity(pos) instanceof PlatterTile platterTile) {
                for (int i = 0; i < platterTile.itemStackHandler.getSlots(); i++) {
                    if (!platterTile.itemStackHandler.getStackInSlot(i)
                                                     .isEmpty()) {
                        toDrop.add(platterTile.itemStackHandler.getStackInSlot(i)
                                                               .copy());
                    }
                }
            }
            Containers.dropContents(worldIn, pos, toDrop);
        }

        return super.playerWillDestroy(worldIn, pos, state, player);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return (iLevel, iBlockPos, iBlockState, iBlockEntity) -> {
            if (iBlockEntity instanceof PlatterTile tile)
                tile.tickServer();
        };
    }


//    @Nullable
//    @Override
//    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
//        if (level.isClientSide()) {
//            return null;
//        }
//        return (level1, blockPos, blockState, t) -> {
//            if (t instanceof PlatterTile tile) {
//                tile.tickServer();
//            }
//        };
//    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        String toCheck = getName().getString();
        if (toCheck.contains("Oak") && !toCheck.contains("Dark")) {
            return new OakPlatterTile(blockPos, blockState);
        } else if (toCheck.contains("Spruce")) {
            return new SprucePlatterTile(blockPos, blockState);
        } else if (toCheck.contains("Birch")) {
            return new BirchPlatterTile(blockPos, blockState);
        } else if (toCheck.contains("Jungle")) {
            return new JunglePlatterTile(blockPos, blockState);
        } else if (toCheck.contains("Acacia")) {
            return new AcaciaPlatterTile(blockPos, blockState);
        } else if (toCheck.contains("Dark")) {
            return new DarkOakPlatterTile(blockPos, blockState);
        } else if (toCheck.contains("Stone")) {
            return new StonePlatterTile(blockPos, blockState);
        } else if (toCheck.contains("Iron")) {
            return new IronPlatterTile(blockPos, blockState);
        } else if (toCheck.contains("Gold")) {
            return new GoldPlatterTile(blockPos, blockState);
        } else if (toCheck.contains("Crimson")) {
            return new CrimsonPlatterTile(blockPos, blockState);
        } else if (toCheck.contains("Warped")) {
            return new WarpedPlatterTile(blockPos, blockState);
        } else if (toCheck.contains("Mangrove")) {
            return new MangrovePlatterTile(blockPos, blockState);
        } else if (toCheck.contains("Cherry")) {
            return new CherryPlatterTile(blockPos, blockState);
        } else if (toCheck.contains("Bamboo")) {
            return new BambooPlatterTile(blockPos, blockState);
        } else {
            return new PlatterTile(blockPos, blockState);
        }
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState blockState, Level worldIn, BlockPos pos) {
        if (worldIn.getBlockEntity(pos) instanceof PlatterTile platterTile) {
            return ItemHandlerHelper.calcRedstoneFromInventory(platterTile.itemStackHandler);
        }
        return 0;
    }

    private final static VoxelShape RENDER_SHAPE = Shapes.box(0.0, 0.0, 0.0, 1.0, 2.0 / 16.0, 1.0);

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return RENDER_SHAPE;
    }

    @Override
    public VoxelShape getOcclusionShape(BlockState state, BlockGetter worldIn, BlockPos pos) {
        return RENDER_SHAPE;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return RENDER_SHAPE;
    }
}
