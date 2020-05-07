package com.abneyonline.platter;

import com.abneyonline.platter.tile.*;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nullable;
import java.util.ArrayList;

public class PlatterBlock extends Block {
    public PlatterBlock() {
        super(Properties.create(Material.WOOD).hardnessAndResistance(1.0f).harvestLevel(0).harvestTool(ToolType.AXE));
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult p_225533_6_) {
        if (!worldIn.isRemote()) {
            ItemStack playerHand = player.getHeldItemMainhand();

            TileEntity tileEntity = worldIn.getTileEntity(pos);
            tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {

                if (player.isCrouching()) {
                    for (int a = h.getSlots() - 1; a >= 0; a--) {
                        ItemStack found = h.getStackInSlot(a);
                        if (!found.isEmpty()) {
                            ItemStack toRemove = h.extractItem(a, found.getCount(), false);
                            ItemHandlerHelper.giveItemToPlayer(player, toRemove);
                            tileEntity.markDirty();
                            worldIn.notifyBlockUpdate(pos, state, state, 2);
                            return;
                        }
                    }
                } else {
                    ItemStack toInsert = ItemHandlerHelper.insertItemStacked(h, playerHand.copy(), false);
                    if (!player.isCreative()) {
                        playerHand.shrink(playerHand.getCount() - toInsert.getCount());
                    }
                    tileEntity.markDirty();
                    worldIn.notifyBlockUpdate(pos, state, state, 2);
                }
            });
        }


        return ActionResultType.SUCCESS;
    }

    @Override
    public void onBlockHarvested(World worldIn, BlockPos pos, BlockState state, PlayerEntity player) {
        if (!worldIn.isRemote()) {
            ArrayList<ItemStack> toDrop = new ArrayList<ItemStack>();
            TileEntity tileEntity = worldIn.getTileEntity(pos);
            tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
                for (int a = 0; a < h.getSlots(); a++) {
                    if (!h.getStackInSlot(a).isEmpty()) {
                        toDrop.add(h.getStackInSlot(a).copy());
                    }
                }
            });

            for (int a = 0; a < toDrop.size(); a++) {
                InventoryHelper.spawnItemStack(worldIn, pos.getX(), pos.getY(), pos.getZ(), toDrop.get(a));
            }
        }

        super.onBlockHarvested(worldIn, pos, state, player);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        String toCheck = getRegistryName().getPath();
        if (toCheck.contains("oak") && !toCheck.contains("dark")) {
            return new OakPlatterTile();
        } else if (toCheck.contains("spruce")) {
            return new SprucePlatterTile();
        } else if (toCheck.contains("birch")) {
            return new BirchPlatterTile();
        } else if (toCheck.contains("jungle")) {
            return new JunglePlatterTile();
        } else if (toCheck.contains("acacia")) {
            return new AcaciaPlatterTile();
        } else if (toCheck.contains("dark")) {
            return new DarkOakPlatterTile();
        } else {
            return new PlatterTile();
        }
    }

    private final static VoxelShape RENDER_SHAPE = VoxelShapes.create(0.0, 0.0, 0.0, 1.0, 2.0 / 16.0, 1.0);

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
//        return super.getShape(state, worldIn, pos, context);
        return RENDER_SHAPE;
    }

    @Override
    public VoxelShape getRenderShape(BlockState state, IBlockReader worldIn, BlockPos pos) {
//        return super.getRenderShape(state, worldIn, pos);
        return RENDER_SHAPE;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
//        return super.getCollisionShape(state, worldIn, pos, context);
        return RENDER_SHAPE;
    }
}
