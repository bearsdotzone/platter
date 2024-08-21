package com.abneyonline.platter.network;

import com.abneyonline.platter.PlatterMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class PlatterClientHandler {
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.ChannelBuilder
            .named(new ResourceLocation(PlatterMod.MODID, "render_packet"))
            .clientAcceptedVersions(s -> true)
            .serverAcceptedVersions(s -> true)
            .networkProtocolVersion(() -> PROTOCOL_VERSION)
            .simpleChannel();

    public static void init()
    {
        INSTANCE.registerMessage(1, MessagePlatterRender.class, MessagePlatterRender::encode, MessagePlatterRender::decode, MessagePlatterRender::handle);
    }
}
