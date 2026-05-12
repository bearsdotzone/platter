package zone.bears.platter.network;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.transfer.item.ItemResource;
import zone.bears.platter.tile.PlatterTile;

import java.io.IOException;
import java.util.List;

public class PlatterClientPayloadHandler {

    public static void handleData(final PlatterRenderPacket data, final IPayloadContext context) {
        try (Level destination = context.player()
                .level()) {
            if (destination.getBlockEntity(data.renderedPlatter()) instanceof PlatterTile platterTile) {
                if (data.itemStacks().isPresent()) {
                    List<ItemStack> itemStacks = data.itemStacks().get();
                    for (int i = 0; i < platterTile.itemStackHandler.size(); i++) {
                        if (!itemStacks.isEmpty()) {
                            ItemStack entry = itemStacks.removeFirst();
                            platterTile.itemStackHandler.set(i, ItemResource.of(entry), entry.count());
                        } else
                            platterTile.itemStackHandler.set(i, ItemResource.EMPTY, 0);
                    }
                } else {
                    for (int i = 0; i < platterTile.itemStackHandler.size(); i++) {
                        platterTile.itemStackHandler.set(i, ItemResource.EMPTY, 0);
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
