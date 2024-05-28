package zone.bears.platter.network;

import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import zone.bears.platter.tile.PlatterTile;

import java.io.IOException;

public class PlatterClientPayloadHandler {

    public static void handleData(final PlatterRenderPacket data, final IPayloadContext context) {
        try (Level destination = context.player()
                                        .level()) {
            if (destination.getBlockEntity(data.renderedPlatter()) instanceof PlatterTile platterTile) {
                for (int i = 0; i < platterTile.itemStackHandler.getSlots(); i++) {
                    platterTile.itemStackHandler.setStackInSlot(i, data.itemStacks()
                                                                       .get(i));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
