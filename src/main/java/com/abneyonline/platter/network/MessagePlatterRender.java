package com.abneyonline.platter.network;

import com.abneyonline.platter.tile.PlatterTile;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.network.NetworkEvent;
import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class MessagePlatterRender {

    private ArrayList<ItemStack> items;
    private BlockPos blockPos;

    public MessagePlatterRender(ArrayList<ItemStack> items, BlockPos blockPos)
    {
        this.items = items;
        this.blockPos = blockPos;
    }

    public static void encode(MessagePlatterRender packet, FriendlyByteBuf buffer) {
        buffer.writeCollection(packet.items, (buf, itemStack) -> {buf.writeItemStack(itemStack, true);});
        buffer.writeBlockPos(packet.blockPos);
    }

    public static MessagePlatterRender decode(FriendlyByteBuf buffer) {
        return new MessagePlatterRender(buffer.readCollection(ArrayList::new, FriendlyByteBuf::readItem), buffer.readBlockPos());
    }

    public static void handle(final MessagePlatterRender message, Supplier<NetworkEvent.Context> ctx)
    {
        ctx.get().enqueueWork(() -> {
            Player thePlayer = NetworkHelper.getSidedPlayer(ctx.get());
            if(thePlayer.level().hasChunkAt(message.blockPos))
            {
                if(thePlayer.level().getBlockEntity(message.blockPos) instanceof PlatterTile platterTile)
                {
                    for(int i = 0; i < message.items.size(); i++)
                        platterTile.inputItems.setStackInSlot(i, message.items.get(i));
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
