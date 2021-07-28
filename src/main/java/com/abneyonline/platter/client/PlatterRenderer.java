package com.abneyonline.platter.client;

import com.abneyonline.platter.tile.PlatterTile;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.model.data.EmptyModelData;
import net.minecraftforge.items.CapabilityItemHandler;

import java.util.ArrayDeque;

import com.mojang.math.Vector3f;

public class PlatterRenderer implements BlockEntityRenderer<PlatterTile> {

    public PlatterRenderer(BlockEntityRendererProvider.Context context) {

    }

    @Override
    public void render(PlatterTile tileEntityIn, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {
        matrixStackIn.pushPose();

        matrixStackIn.mulPose(new Vector3f(1f, 0f, 0f).rotationDegrees(90f));
        matrixStackIn.translate(0.5f, 0.5f, -0.063f);
        Minecraft mc = Minecraft.getInstance();

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

            matrixStackIn.translate(0f, 0.0f, -0.063f);
            if (!itemIn.isEmpty()) {
                mc.getItemRenderer().renderStatic(itemIn, ItemTransforms.TransformType.FIXED, combinedLightIn, combinedOverlayIn, matrixStackIn, bufferIn, 0);
            }
        }
        matrixStackIn.popPose();
    }
}
