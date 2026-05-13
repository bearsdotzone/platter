package zone.bears.platter.tile;

import com.mojang.authlib.GameProfile;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.util.FakePlayer;
import net.neoforged.neoforge.common.util.FakePlayerFactory;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.transfer.ResourceHandlerUtil;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;
import zone.bears.platter.Config;
import zone.bears.platter.Registration;
import zone.bears.platter.network.PlatterRenderPacket;

import java.util.*;
import java.util.stream.Collectors;

public class PlatterTile extends BlockEntity implements ItemOwner {
    public final PlatterItemStackHandler itemStackHandler = new PlatterItemStackHandler(Config.PLATTER_SLOTS.getAsInt());
    public int tickCount = 0;
    protected boolean tickForAnimals = true;

    public PlatterTile(BlockPos blockPos, BlockState blockState) {
        super(zone.bears.platter.Registration.platter_tile.get(), blockPos, blockState);
    }

    public PlatterTile(BlockEntityType blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }

    @Override
    protected void saveAdditional(ValueOutput valueOutput) {
        super.saveAdditional(valueOutput);
        itemStackHandler.serialize(valueOutput);

    }

    @Override
    protected void loadAdditional(ValueInput valueInput) {
        super.loadAdditional(valueInput);
        itemStackHandler.deserialize(valueInput);
    }

    @Override
    public void setChanged() {
        super.setChanged();
        if (level != null && !level.isClientSide()) {
            if (ResourceHandlerUtil.isEmpty(itemStackHandler)) {
                PacketDistributor.sendToPlayersTrackingChunk((ServerLevel) level, ChunkPos.containing(getBlockPos()), new PlatterRenderPacket(Optional.empty(), getBlockPos()));
            } else {
                PacketDistributor.sendToPlayersTrackingChunk((ServerLevel) level, ChunkPos.containing(getBlockPos()), new PlatterRenderPacket(Optional.of(itemStackHandler.copyToList().stream().filter((i) -> i != ItemStack.EMPTY).toList()), getBlockPos()));
            }
        }
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return this.saveWithoutMetadata(registries);
    }

    @Override
    public void handleUpdateTag(ValueInput input) {
        super.handleUpdateTag(input);
    }

    public void tickServer() {
        if (level != null && !level.isClientSide() && !level.hasNeighborSignal(getBlockPos())) {
            tickCount += 1;
            if (tickCount >= Config.PLATTER_PERIOD.get() * 20) {
                tickCount = 0;

                int radius = Config.PLATTER_RADIUS.get();
                AABB bb = new AABB(getBlockPos()).inflate(radius);
                List<Entity> entities = level.getEntities((Entity) null, bb, (i) -> i instanceof Animal || i instanceof Player);
                ArrayList<Player> players = new ArrayList<>();
                ArrayList<Animal> animals = new ArrayList<>();
                for (Entity i : entities) {
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

                Map<Class, List<Animal>> animalGroups = animals.stream().collect(Collectors.groupingBy(Animal::getClass));

                try (Transaction rootTransaction = Transaction.openRoot()) {
                    for (int i = itemStackHandler.size() - 1; i >= 0; i--) {
                        ItemResource retrievedItem;
                        while (!players.isEmpty() && (retrievedItem = itemStackHandler.getResource(i)).getComponents().has(DataComponents.FOOD)) {
                            Player playerToFeed = players.removeFirst();
                            itemStackHandler.extract(i, retrievedItem, 1, rootTransaction);
                            level.playSound(null, playerToFeed.blockPosition(), SoundEvents.GENERIC_EAT.value(), SoundSource.PLAYERS);
                            playerToFeed.getFoodData().eat(retrievedItem.getComponents().get(DataComponents.FOOD));
                        }

                        if (!animalGroups.isEmpty()) {
                            FakePlayer fp = FakePlayerFactory.get((ServerLevel) level, new GameProfile(UUID.randomUUID(), "test"));
                            for (List<Animal> animalList : animalGroups.values()) {
                                while (!animalList.isEmpty() && animalList.getFirst().isFood((retrievedItem = itemStackHandler.getResource(i)).toStack())) {
                                    Animal animalToFeed = animalList.removeFirst();
                                    if (animalToFeed.canFallInLove() && animalToFeed.getAge() == 0) {
                                        itemStackHandler.extract(i, retrievedItem, 1, rootTransaction);
                                        fp.setItemInHand(InteractionHand.MAIN_HAND, retrievedItem.toStack(1));
                                        animalToFeed.mobInteract(fp, InteractionHand.MAIN_HAND);
                                    }
                                }
                            }
                            fp.setRemoved(Entity.RemovalReason.DISCARDED);
                        }

                        if (animals.isEmpty() && players.isEmpty()) {
                            break;
                        }
                    }
                    rootTransaction.commit();
                }
            }
        }
    }

    @Override
    public Level level() {
        return getLevel();
    }

    @Override
    public Vec3 position() {
        return getBlockPos().getCenter();
    }

    @Override
    public float getVisualRotationYInDegrees() {
        return 0;
    }

    public class PlatterItemStackHandler extends ItemStacksResourceHandler {

        protected PlatterItemStackHandler(int size) {
            super(size);
        }

        @Override
        protected void onContentsChanged(int index, ItemStack previousContents) {
            super.onContentsChanged(index, previousContents);
            setChanged();
        }

        @Override
        public int extract(int index, ItemResource resource, int amount, TransactionContext transaction) {
            int extracted = super.extract(index, resource, amount, transaction);
            if (getResource(index) == ItemResource.EMPTY) {
                for (int i = index; i < size() - 1; i++) {
                    set(i, getResource(i + 1), getAmountAsInt(i + 1));
                }
            }
            return extracted;
        }

        public ItemStack popStack() {
            if (topItemResource() != ItemResource.EMPTY) {
                ItemStack extracted = topItemResource().toStack(topItemAmount());
                set(topItemIndex(), ItemResource.EMPTY, 0);
                return extracted;
            }
            return ItemStack.EMPTY;
        }

        @Override
        public int insert(ItemResource resource, int amount, TransactionContext transaction) {
            return pushStack(resource, amount);
        }

        @Override
        public int insert(int index, ItemResource resource, int amount, TransactionContext transaction) {
            return pushStack(resource, amount);
        }

        public boolean hasFreeSpace() {
            return getResource(size() - 1).equals(ItemResource.EMPTY);
        }

        // Return the index of the first empty inventory slot or -1 if no such slot exists.
        private int firstEmpty() {
            for (int i = 0; i < size(); i++) {
                if (getResource(i) == ItemResource.EMPTY) {
                    return i;
                }
            }
            return -1;
        }

        // Index of the first non-empty slot, or -1 if the container is empty.
        private int topItemIndex() {
            for (int i = size() - 1; i >= 0; i--) {
                if (getResource(i) != ItemResource.EMPTY) {
                    return i;
                }
            }
            return -1;
        }

        public ItemResource topItemResource() {
            for (int i = size() - 1; i >= 0; i--) {
                if (getResource(i) != ItemResource.EMPTY) {
                    return getResource(i);
                }
            }
            return ItemResource.EMPTY;
        }

        private int topItemAmount() {
            for (int i = size() - 1; i >= 0; i--) {
                if (getResource(i) != ItemResource.EMPTY) {
                    return getAmountAsInt(i);
                }
            }
            return 0;
        }

        public int pushStack(ItemResource itemResource, int amount) {
            int inserted = 0;
            int remaining = amount;
            if (topItemResource() == itemResource && topItemAmount() < itemResource.getMaxStackSize()) {
                int toAdd = remaining - (topItemAmount() + remaining) % itemResource.getMaxStackSize();
                set(topItemIndex(), itemResource, topItemAmount() + toAdd);
                remaining -= toAdd;
                inserted += toAdd;
            }
            if (hasFreeSpace() && remaining > 0) {
                set(firstEmpty(), itemResource, remaining);
                inserted += remaining;
            }
            return inserted;
        }
    }
}
