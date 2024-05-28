package zone.bears.platter.network;

import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import zone.bears.platter.PlatterMod;

import java.util.List;

public record PlatterRenderPacket(List<ItemStack> itemStacks, BlockPos renderedPlatter) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<PlatterRenderPacket> TYPE = new CustomPacketPayload.Type<>(new ResourceLocation(PlatterMod.MODID, "render_packet"));


    public static final StreamCodec<RegistryFriendlyByteBuf, PlatterRenderPacket> STREAM_CODEC = StreamCodec.composite(ItemStack.OPTIONAL_LIST_STREAM_CODEC, PlatterRenderPacket::itemStacks, BlockPos.STREAM_CODEC, PlatterRenderPacket::renderedPlatter, PlatterRenderPacket::new);


    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
