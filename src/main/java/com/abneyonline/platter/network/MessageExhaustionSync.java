package com.abneyonline.platter.network;

import com.abneyonline.platter.helpers.HungerHelper;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageExhaustionSync
{
	float exhaustionLevel;

	public MessageExhaustionSync(float exhaustionLevel)
	{
		this.exhaustionLevel = exhaustionLevel;
	}

	public static void encode(MessageExhaustionSync pkt, PacketBuffer buf)
	{
		buf.writeFloat(pkt.exhaustionLevel);
	}

	public static MessageExhaustionSync decode(PacketBuffer buf)
	{
		return new MessageExhaustionSync(buf.readFloat());
	}

	public static void handle(final MessageExhaustionSync message, Supplier<NetworkEvent.Context> ctx)
	{
		ctx.get().enqueueWork(() -> {
			HungerHelper.setExhaustion(NetworkHelper.getSidedPlayer(ctx.get()), message.exhaustionLevel);
		});
		ctx.get().setPacketHandled(true);
	}
}