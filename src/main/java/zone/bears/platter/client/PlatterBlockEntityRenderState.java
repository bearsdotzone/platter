package zone.bears.platter.client;

import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import zone.bears.platter.Config;

public class PlatterBlockEntityRenderState extends BlockEntityRenderState {
    public ItemStackRenderState[] items = new ItemStackRenderState[Config.PLATTER_SLOTS.getAsInt()];
    public int seed;
}
