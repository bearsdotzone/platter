package com.abneyonline.platter.tile;

import com.abneyonline.platter.Config;
import com.abneyonline.platter.Registration;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PlatterTile extends BlockEntity {

    private final ItemStackHandler inputItems = createHandler();
    private final LazyOptional<IItemHandler> handler = LazyOptional.of(() -> inputItems);
    private long tickCount = 0;
    protected boolean tickForAnimals = true;

    public PlatterTile(BlockPos blockPos, BlockState blockState) {
        super(Registration.oak_platter_tile.get(), blockPos, blockState);
    }

    public PlatterTile(BlockEntityType tet, BlockPos blockPos, BlockState blockState) {
        super(tet, blockPos, blockState);
    }

    public void tickServer() {
        if (!level.isClientSide() && !level.hasNeighborSignal(getBlockPos())) {
            tickCount += 1;
            if (tickCount >= Config.PLATTER_PERIOD.get() * 20) {
                tickCount = 0;

                int radii = Config.PLATTER_RADIUS.get();

                List<LivingEntity> pl = level.getEntitiesOfClass(LivingEntity.class, new AABB(getBlockPos().offset(-radii, -radii, -radii), getBlockPos().offset(radii, radii, radii)));
                ArrayList<Player> lpe = new ArrayList<Player>();
                ArrayList<Animal> lae = new ArrayList<Animal>();
                for (LivingEntity p : pl) {
                    if (p instanceof Player) {
                        Player pe = (Player) p;

                        if (pe.canEat(false) && !pe.isCreative()) {
                            lpe.add(pe);
                        }
                    } else if (p instanceof Animal && tickForAnimals) {
                        Animal ae = (Animal) p;
                        if (ae.canFallInLove() && !ae.isBaby()) {
                            lae.add(ae);
                        }
                    }
                }
                getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
                    for (int a = h.getSlots() - 1; a >= 0; a--) {
                        ItemStack retrievedItem = h.getStackInSlot(a);

                        if (retrievedItem.isEdible() && !lpe.isEmpty()) {
                            Iterator<Player> ipe = lpe.iterator();
                            while (ipe.hasNext() && (retrievedItem != ItemStack.EMPTY)) {
                                Player toFeed = ipe.next();
                                ItemStack toEat = h.extractItem(a, 1, false);
                                toFeed.eat(level, toEat);
                                ipe.remove();
                            }
                        }
                        if (!lae.isEmpty()) {

                            Iterator<Animal> iae = lae.iterator();

                            while (iae.hasNext() && (retrievedItem != ItemStack.EMPTY)) {
                                Animal ae = iae.next();
                                if (ae.isFood(retrievedItem) && ae.canFallInLove() && ae.getAge() == 0) {
                                    ItemStack toEat = h.extractItem(a, 1, false);
                                    ae.eat(level, toEat);
                                    ae.setInLove(null);
                                    iae.remove();
                                }
                            }
                        }

                        if (lae.isEmpty() && lpe.isEmpty()) {
                            break;
                        }
                    }
                });

            }
        }
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        inputItems.deserializeNBT(tag.getCompound("inv"));
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        tag.put("inv", inputItems.serializeNBT());
    }

    private ItemStackHandler createHandler() {
        return new ItemStackHandler(Config.PLATTER_SLOTS.get()) {
            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                return super.isItemValid(slot, stack);
            }

            @Nonnull
            @Override
            public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
                if (simulate == false) {
                    level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 2);
                    setChanged();
                }
                return super.insertItem(slot, stack, simulate);
            }

            @Nonnull
            @Override
            public ItemStack extractItem(int slot, int amount, boolean simulate) {
                ItemStack toReturn = super.extractItem(slot, amount, simulate);

                if (getStackInSlot(slot).isEmpty()) {
                    return toReturn;
                }

                if (simulate == false) {
                    if (getStackInSlot(slot).getCount() - amount <= 0) {
                        for (int a = slot; a < stacks.size() - 1; a++) {
                            setStackInSlot(a, getStackInSlot(a + 1));
                        }
                    }
                    level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 2);
                    setChanged();
                }

                return toReturn;
            }
        };
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return handler.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        CompoundTag tag = pkt.getTag();
        load(tag);
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag nbt = new CompoundTag();
        saveAdditional(nbt);
        return nbt;
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        if (tag != null) { load(tag); }
    }
}
