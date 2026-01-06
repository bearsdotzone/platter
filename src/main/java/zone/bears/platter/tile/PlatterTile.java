package zone.bears.platter.tile;

import com.mojang.authlib.GameProfile;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.common.util.FakePlayer;
import net.neoforged.neoforge.common.util.FakePlayerFactory;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.network.PacketDistributor;
import zone.bears.platter.Config;
import zone.bears.platter.Registration;
import zone.bears.platter.network.PlatterRenderPacket;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class PlatterTile extends BlockEntity {

    public final ItemStackHandler itemStackHandler = new PlatterItemStackHandler(Config.PLATTER_SLOTS.getAsInt());
    public int tickCount = 0;
    protected boolean tickForAnimals = true;

    public PlatterTile(BlockPos blockPos, BlockState blockState) {
        super(Registration.oak_platter_tile.get(), blockPos, blockState);
    }

    public PlatterTile(BlockEntityType blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }

    @Override
    protected void saveAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        super.saveAdditional(pTag, pRegistries);
        pTag.put("inv", itemStackHandler.serializeNBT(pRegistries));

    }

    @Override
    protected void loadAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        super.loadAdditional(pTag, pRegistries);
        itemStackHandler.deserializeNBT(pRegistries, pTag.getCompound("inv").get());
    }

    @Override
    public void setChanged() {
        super.setChanged();
        if (level != null && !level.isClientSide()) {
            List<ItemStack> clientItems = new ArrayList<>(Config.PLATTER_SLOTS.getAsInt());
            for (int i = 0; i < itemStackHandler.getSlots(); i++) {
                clientItems.add(itemStackHandler.getStackInSlot(i));
            }
            PacketDistributor.sendToPlayersTrackingChunk((ServerLevel) level, new ChunkPos(getBlockPos()), new PlatterRenderPacket(clientItems, getBlockPos()));
        }
    }

    public void tickServer() {
        if (level != null && !level.isClientSide() && !level.hasNeighborSignal(getBlockPos())) {
            tickCount += 1;
            if (tickCount >= Config.PLATTER_PERIOD.get() * 20) {
                tickCount = 0;

                int radii = Config.PLATTER_RADIUS.get();

                List<LivingEntity> livingEntities = level.getEntitiesOfClass(LivingEntity.class, new AABB(getBlockPos().offset(-radii, -radii, -radii)
                        .getCenter(), getBlockPos().offset(radii, radii, radii)
                        .getCenter()));
                ArrayList<Player> players = new ArrayList<>();
                ArrayList<Animal> animals = new ArrayList<>();
                for (LivingEntity i : livingEntities) {
                    if (i instanceof Player player) {
                        if (player.canEat(false) && !player.isCreative()) {
                            players.add(player);
                        }
                    } else if (i instanceof Animal animal && tickForAnimals) {
                        if (animal.canFallInLove() && !animal.isBaby()) {
                            animals.add(animal);
                        }
                    }
                }

                for (int i = itemStackHandler.getSlots() - 1; i >= 0; i--) {
                    ItemStack retrievedItem = itemStackHandler.getStackInSlot(i);
                    if (!players.isEmpty()) {
                        Iterator<Player> playerIterator = players.iterator();
                        while (playerIterator.hasNext() && (retrievedItem != ItemStack.EMPTY)) {
                            Player playerToFeed = playerIterator.next();
                            if (retrievedItem.getComponents().has(DataComponents.FOOD)) {
                                ItemStack toEat = itemStackHandler.extractItem(i, 1, false);
                                playerToFeed.getFoodData().eat(retrievedItem.getComponents().get(DataComponents.FOOD));
//                                playerToFeed.eat(level, toEat);
                                playerIterator.remove();
                                retrievedItem = itemStackHandler.getStackInSlot(i);
                            }
                        }
                    }


                    if (!animals.isEmpty()) {

                        Iterator<Animal> animalIterator = animals.iterator();

                        while (animalIterator.hasNext() && (retrievedItem != ItemStack.EMPTY)) {
                            Animal animalToFeed = animalIterator.next();
                            if (animalToFeed.isFood(retrievedItem) && animalToFeed.canFallInLove() && animalToFeed.getAge() == 0) {
                                ItemStack toEat = itemStackHandler.extractItem(i, 1, false);
                                FakePlayer fp = FakePlayerFactory.get((ServerLevel) level, new GameProfile(UUID.randomUUID(), "test"));
                                fp.setItemInHand(InteractionHand.MAIN_HAND, toEat);
                                animalToFeed.mobInteract(fp, InteractionHand.MAIN_HAND);
//                                animalToFeed.eat(level, toEat);
//                                animalToFeed.setInLove(null);
                                animalIterator.remove();
                                retrievedItem = itemStackHandler.getStackInSlot(i);
                            }
                        }
                    }

                    if (animals.isEmpty() && players.isEmpty()) {
                        break;
                    }
                }

            }
        }
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider pRegistries) {
        CompoundTag toReturn = super.getUpdateTag(pRegistries);
        toReturn.put("Items", itemStackHandler.serializeNBT(pRegistries));
        return toReturn;
    }

    @Override
    public void handleUpdateTag(CompoundTag tag, HolderLookup.Provider lookupProvider) {
        super.handleUpdateTag(tag, lookupProvider);
        itemStackHandler.deserializeNBT(lookupProvider, tag.getCompound("Items").get());
    }


//    private ItemStackHandler createHandler() {
//        return new ItemStackHandler(Config.PLATTER_SLOTS.get()) {
//            @Override
//            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
//                return super.isItemValid(slot, stack);
//            }
//
//            @Nonnull
//            @Override
//            public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
//                if (simulate == false) {
//                    level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 2);
//                    setChanged();
//                }
//                return super.insertItem(slot, stack, simulate);
//            }
//
//            @Nonnull
//            @Override
//            public ItemStack extractItem(int slot, int amount, boolean simulate) {
//                ItemStack toReturn = super.extractItem(slot, amount, simulate);
//
//                if (getStackInSlot(slot).isEmpty()) {
//                    return toReturn;
//                }
//
//                if (simulate == false) {
//                    if (getStackInSlot(slot).getCount() - amount <= 0) {
//                        for (int a = slot; a < stacks.size() - 1; a++) {
//                            setStackInSlot(a, getStackInSlot(a + 1));
//                        }
//                    }
//                    level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 2);
//                    setChanged();
//                }
//
//                return toReturn;
//            }
//        };
//    }

//    @Override
//    public ClientboundBlockEntityDataPacket getUpdatePacket() {
//        return ClientboundBlockEntityDataPacket.create(this);
//    }
//
//    @Override
//    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt, HolderLookup.Provider registries) {
//        CompoundTag tag = pkt.getTag();
//        loadAdditional(tag, registries);
//    }
//

    public class PlatterItemStackHandler extends ItemStackHandler {

        protected PlatterItemStackHandler(int size) {
            super(size);
        }

        @Override
        protected void onContentsChanged(int slot) {
            super.onContentsChanged(slot);
            setChanged();
        }

        @Override
        public ItemStack extractItem(int slot, int amount, boolean simulate) {
            ItemStack toReturn = super.extractItem(slot, amount, simulate);

            if (toReturn.isEmpty()) {
                return toReturn;
            }

            if (!simulate) {
                if (getStackInSlot(slot).getCount() == 0) {
                    // The slot we are extracting from is now empty, shift item stacks from above this in the stack, downwards
                    for (int i = slot; i < stacks.size() - 1; i++) {
                        setStackInSlot(i, getStackInSlot(i + 1));
                    }
                }
            }

            return toReturn;
        }
    }
}
