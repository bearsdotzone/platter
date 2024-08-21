package com.abneyonline.platter.tile;

import com.abneyonline.platter.Config;
import com.abneyonline.platter.Registration;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
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

                List<LivingEntity> livingEntities = level.getEntitiesOfClass(LivingEntity.class, new AABB(getBlockPos().offset(-radii, -radii, -radii), getBlockPos().offset(radii, radii, radii)));
                ArrayList<Player> players = new ArrayList<Player>();
                ArrayList<Animal> animals = new ArrayList<Animal>();
                for (LivingEntity livingEntity : livingEntities) {
                    if (livingEntity instanceof Player playerEntity) {

                        if (playerEntity.canEat(false) && !playerEntity.isCreative()) {
                            players.add(playerEntity);
                        }
                    } else if (livingEntity instanceof Animal animalEntity && tickForAnimals) {
                        if (animalEntity.canFallInLove() && !animalEntity.isBaby()) {
                            animals.add(animalEntity);
                        }
                    }
                }
                getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(itemHandler -> {
                    for (int i = itemHandler.getSlots() - 1; i >= 0; i--) {
                        ItemStack itemToEat = itemHandler.getStackInSlot(i);

                        if (itemToEat.isEdible() && !players.isEmpty()) {
                            Iterator<Player> playerIterator = players.iterator();
                            while (playerIterator.hasNext() && (itemToEat != ItemStack.EMPTY)) {
                                Player player = playerIterator.next();
                                ItemStack toEat = itemHandler.extractItem(i, 1, false);
                                player.eat(level, toEat);
                                playerIterator.remove();
                                itemToEat = itemHandler.getStackInSlot(i);
                            }
                        }
                        if (!animals.isEmpty()) {

                            Iterator<Animal> animalIterator = animals.iterator();

                            while (animalIterator.hasNext() && (itemToEat != ItemStack.EMPTY)) {
                                Animal animal = animalIterator.next();
                                if (animal.isFood(itemToEat) && animal.canFallInLove() && animal.getAge() == 0) {
                                    ItemStack toEat = itemHandler.extractItem(i, 1, false);
                                    animal.eat(level, toEat);
                                    animal.setInLove(null);
                                    animalIterator.remove();
                                    itemToEat = itemHandler.getStackInSlot(i);
                                }
                            }
                        }

                        if (animals.isEmpty() && players.isEmpty()) {
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

                if(toReturn.isEmpty())
                {
                    return toReturn;
                }

                if (!simulate) {
                    if (getStackInSlot(slot).getCount() == 0) {
                        // The slot we are extracting from is now empty, shift item stacks from above this in the stack, downwards
                        for (int i = slot; i < stacks.size() - 1; i++) {
                            setStackInSlot(i, getStackInSlot(i + 1));
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
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
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
