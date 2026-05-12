package zone.bears.platter.network;

import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import zone.bears.platter.Platter;

import java.util.List;
import java.util.Optional;

public record PlatterRenderPacket(Optional<List<ItemStack>> itemStacks,
                                  BlockPos renderedPlatter) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<PlatterRenderPacket> TYPE = new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(Platter.MODID, "render_packet"));

    public static final StreamCodec<RegistryFriendlyByteBuf, PlatterRenderPacket> STREAM_CODEC = StreamCodec.composite(ItemStack.STREAM_CODEC.apply(ByteBufCodecs.list()).apply(ByteBufCodecs::optional), PlatterRenderPacket::itemStacks, BlockPos.STREAM_CODEC, PlatterRenderPacket::renderedPlatter, PlatterRenderPacket::new);


    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
