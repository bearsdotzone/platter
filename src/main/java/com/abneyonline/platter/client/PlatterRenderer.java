package com.abneyonline.platter.client;

import com.abneyonline.platter.tile.PlatterTile;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;

import java.util.ArrayDeque;

public class PlatterRenderer extends TileEntityRenderer<PlatterTile> {

    public PlatterRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }

    @Override
    public void render(PlatterTile tileEntityIn, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        matrixStackIn.push();

        matrixStackIn.rotate(new Vector3f(1f, 0f, 0f).rotationDegrees(90f));
        matrixStackIn.translate(0.5f, 0.5f, -0.0625f);

        ArrayDeque<ItemStack> items = new ArrayDeque<ItemStack>();

        tileEntityIn.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
            for (int a = 0; a < h.getSlots(); a++) {
                if (!h.getStackInSlot(a).isEmpty()) {
                    items.add(h.getStackInSlot(a));
                }
            }
        });

        while (items.size() > 0) {
            ItemStack itemIn = items.pop();

            matrixStackIn.translate(0f, 0.0f, -0.0625f);

            Minecraft mc = Minecraft.getInstance();
            if (!itemIn.isEmpty()) {
                mc.getItemRenderer().renderItem(itemIn, ItemCameraTransforms.TransformType.GROUND, 0xFFFFFF, combinedOverlayIn, matrixStackIn, bufferIn);
            }
        }


        matrixStackIn.pop();
    }
}
