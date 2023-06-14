package com.abneyonline.platter;

import com.abneyonline.platter.tile.*;
import net.minecraft.client.model.geom.builders.MaterialDefinition;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nullable;
import java.util.ArrayList;


public class PlatterBlock extends Block implements EntityBlock {
    public PlatterBlock() {
        super(Properties.of().strength(1.0f));
    }

    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult p_225533_6_) {
        if (!worldIn.isClientSide()) {
            ItemStack playerHand = player.getMainHandItem();

            BlockEntity tileEntity = worldIn.getBlockEntity(pos);
            tileEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(h -> {

                if (player.isCrouching()) {
                    for (int a = h.getSlots() - 1; a >= 0; a--) {
                        ItemStack found = h.getStackInSlot(a);
                        if (!found.isEmpty()) {
                            ItemStack toRemove = h.extractItem(a, found.getCount(), false);
                            ItemHandlerHelper.giveItemToPlayer(player, toRemove);
                            tileEntity.setChanged();
                            worldIn.sendBlockUpdated(pos, state, state, 2);
                            return;
                        }
                    }
                } else {
                    ItemStack toInsert = ItemHandlerHelper.insertItemStacked(h, playerHand.copy(), false);
                    if (!player.isCreative()) {
                        playerHand.shrink(playerHand.getCount() - toInsert.getCount());
                    }
                    tileEntity.setChanged();
                    worldIn.sendBlockUpdated(pos, state, state, 2);
                }
            });
        }


        return InteractionResult.SUCCESS;
    }

    @Override
    public void playerWillDestroy(Level worldIn, BlockPos pos, BlockState state, Player player) {
        if (!worldIn.isClientSide()) {
            ArrayList<ItemStack> toDrop = new ArrayList<ItemStack>();
            BlockEntity tileEntity = worldIn.getBlockEntity(pos);
            tileEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(h -> {
                for (int a = 0; a < h.getSlots(); a++) {
                    if (!h.getStackInSlot(a).isEmpty()) {
                        toDrop.add(h.getStackInSlot(a).copy());
                    }
                }
            });

            for (int a = 0; a < toDrop.size(); a++) {
                Containers.dropItemStack(worldIn, pos.getX(), pos.getY(), pos.getZ(), toDrop.get(a));
            }
        }

        super.playerWillDestroy(worldIn, pos, state, player);
    }
//
//    @Override
//    public boolean hasTileEntity(BlockState state) {
//        return true;
//    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if (level.isClientSide()) {
            return null;
        }
        return (level1, blockPos, blockState, t) -> {
            if (t instanceof PlatterTile tile) {
                tile.tickServer();
            }
        };
    }

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

        BlockEntity tileEntity = worldIn.getBlockEntity(pos);
        int toReturn = 0;
        LazyOptional<IItemHandler> handler = tileEntity.getCapability(ForgeCapabilities.ITEM_HANDLER);
        if(handler.isPresent())
        {
            return ItemHandlerHelper.calcRedstoneFromInventory(handler.orElse(null));
        }
        else
        {
            return 0;
        }
    }

    private final static VoxelShape RENDER_SHAPE = Shapes.box(0.0, 0.0, 0.0, 1.0, 2.0 / 16.0, 1.0);

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
//        return super.getShape(state, worldIn, pos, context);
        return RENDER_SHAPE;
    }

    @Override
    public VoxelShape getOcclusionShape(BlockState state, BlockGetter worldIn, BlockPos pos) {
//        return super.getRenderShape(state, worldIn, pos);
        return RENDER_SHAPE;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
//        return super.getCollisionShape(state, worldIn, pos, context);
        return RENDER_SHAPE;
    }
}
