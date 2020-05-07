package com.abneyonline.platter.tile;

import com.abneyonline.platter.Config;
import com.abneyonline.platter.Registration;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
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

public class PlatterTile extends TileEntity implements ITickableTileEntity {

    private LazyOptional<IItemHandler> handler = LazyOptional.of(this::createHandler);
    private long tickCount = 0;

    public PlatterTile() {
        super(Registration.oak_platter_tile.get());
    }

    public PlatterTile(TileEntityType tet) {
        super(tet);
    }

    @Override
    public void tick() {
        if (!world.isRemote()) {
            tickCount += 1;
            if (tickCount >= Config.PLATTER_PERIOD.get() * 20) {
                tickCount = 0;

                int radii = Config.PLATTER_RADIUS.get();

                List<LivingEntity> pl = world.getEntitiesWithinAABB(LivingEntity.class, new AxisAlignedBB(getPos().add(-radii, -radii, -radii), getPos().add(radii, radii, radii)));
                ArrayList<PlayerEntity> lpe = new ArrayList<PlayerEntity>();
                ArrayList<AnimalEntity> lae = new ArrayList<AnimalEntity>();
                for (LivingEntity p : pl) {
                    if (p instanceof PlayerEntity) {
                        PlayerEntity pe = (PlayerEntity) p;

                        if (pe.canEat(false) && !pe.isCreative()) {
                            lpe.add(pe);
                        }
                    } else if (p instanceof AnimalEntity) {
                        AnimalEntity ae = (AnimalEntity) p;
                        if (ae.canBreed() && !ae.isChild()) {
                            lae.add(ae);
                        }
                    }
                }
                getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
                    for (int a = h.getSlots() - 1; a >= 0; a--) {
                        ItemStack retrievedItem = h.getStackInSlot(a);

                        if (retrievedItem.isFood() && !lpe.isEmpty()) {
                            Iterator<PlayerEntity> ipe = lpe.iterator();
                            while (ipe.hasNext() && (retrievedItem != ItemStack.EMPTY)) {
                                PlayerEntity toFeed = ipe.next();
                                ItemStack toEat = h.extractItem(a, 1, false);
                                toFeed.onFoodEaten(world, toEat);
                                ipe.remove();
                            }
                        }
                        if (!lae.isEmpty()) {

                            Iterator<AnimalEntity> iae = lae.iterator();

                            while (iae.hasNext() && (retrievedItem != ItemStack.EMPTY)) {
                                AnimalEntity ae = iae.next();
                                if (ae.isBreedingItem(retrievedItem) && ae.canBreed() && ae.getGrowingAge() == 0) {
                                    ItemStack toEat = h.extractItem(a, 1, false);
                                    ae.onFoodEaten(world, toEat);
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
    public void read(CompoundNBT tag) {
        CompoundNBT invTag = tag.getCompound("inv");
        handler.ifPresent(h -> ((INBTSerializable<CompoundNBT>) h).deserializeNBT(invTag));
        super.read(tag);
    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {
        handler.ifPresent(h -> {
            CompoundNBT compound = ((INBTSerializable<CompoundNBT>) h).serializeNBT();
            tag.put("inv", compound);
        });
        return super.write(tag);
    }

    private IItemHandler createHandler() {
        return new ItemStackHandler(Config.PLATTER_SLOTS.get()) {
            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                return super.isItemValid(slot, stack);
            }

            @Nonnull
            @Override
            public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
                if (simulate == false) {
                    world.notifyBlockUpdate(pos, getBlockState(), getBlockState(), 2);
                    markDirty();
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
                    world.notifyBlockUpdate(pos, getBlockState(), getBlockState(), 2);
                    markDirty();
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
    public SUpdateTileEntityPacket getUpdatePacket() {
        CompoundNBT nbtTag = new CompoundNBT();
        nbtTag = write(nbtTag);
        return new SUpdateTileEntityPacket(getPos(), -1, nbtTag);
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        CompoundNBT tag = pkt.getNbtCompound();
        read(tag);
    }

    @Override
    public CompoundNBT getUpdateTag() {
        CompoundNBT nbt = new CompoundNBT();
        write(nbt);
        return nbt;
    }

    @Override
    public void handleUpdateTag(CompoundNBT tag) {
        read(tag);
    }
}
