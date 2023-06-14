package com.abneyonline.platter.client;

import com.abneyonline.platter.Config;
import com.abneyonline.platter.tile.PlatterTile;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import org.joml.Quaternionf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.item.ItemStack;
import org.joml.Random;

import java.util.ArrayDeque;

public class PlatterRenderer implements BlockEntityRenderer<PlatterTile> {

    public PlatterRenderer(BlockEntityRendererProvider.Context context) {

    }

    @Override
    public void render(PlatterTile tileEntityIn, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {
        matrixStackIn.pushPose();

        Random r = new Random(tileEntityIn.hashCode());

        Quaternionf pose = new Quaternionf();
        pose.rotateX((float) Math.toRadians(90f));

        matrixStackIn.mulPose(pose);
        matrixStackIn.translate(0.5f, 0.5f, -0.063f);
        Minecraft mc = Minecraft.getInstance();

        ArrayDeque<ItemStack> items = new ArrayDeque<ItemStack>();

        tileEntityIn.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(h -> {
            for (int a = 0; a < h.getSlots(); a++) {
                if (!h.getStackInSlot(a).isEmpty()) {
                    items.add(h.getStackInSlot(a));
                }
            }
        });

        while (items.size() > 0) {
            ItemStack itemIn = items.pop();
            if(Config.PLATTER_ECCENTRICITY_ENABLED.get()) {

                boolean addEccentricity = r.nextInt(2) == 1;

                if(addEccentricity)
                {
                    matrixStackIn.mulPose(new Quaternionf().rotateLocalZ((float) Math.toRadians(r.nextInt(Config.PLATTER_ECCENTRICITY_VALUE.get()))));
                }
                else
                {
                    matrixStackIn.mulPose(new Quaternionf().rotateLocalZ((float) Math.toRadians(r.nextInt(Config.PLATTER_ECCENTRICITY_VALUE.get() )* -1)));
                }
            }

            matrixStackIn.translate(0f, 0.0f, -0.063f);
            if (!itemIn.isEmpty()) {

                mc.getItemRenderer().renderStatic(itemIn, ItemDisplayContext.FIXED, combinedLightIn, combinedOverlayIn, matrixStackIn, bufferIn, null, 0);
            }
        }
        matrixStackIn.popPose();
    }
}
