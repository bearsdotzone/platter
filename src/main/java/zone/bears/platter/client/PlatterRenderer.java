package zone.bears.platter.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.joml.Quaternionf;
import org.joml.Random;
import zone.bears.platter.Config;
import zone.bears.platter.tile.PlatterTile;

public class PlatterRenderer implements BlockEntityRenderer {

    public PlatterRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(BlockEntity tileEntityIn, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {
        matrixStackIn.pushPose();

        Random r = new Random(tileEntityIn.hashCode());

        Quaternionf pose = new Quaternionf();
        pose.rotateX((float) Math.toRadians(90f));

        matrixStackIn.mulPose(pose);
        matrixStackIn.translate(0.5f, 0.5f, -0.063f);
        Minecraft mc = Minecraft.getInstance();
        if (tileEntityIn instanceof PlatterTile platterTile) {
            for (int i = 0; i < platterTile.itemStackHandler.getSlots(); i++) {
                ItemStack itemIn = platterTile.itemStackHandler.getStackInSlot(i);
                if (Config.PLATTER_ECCENTRICITY_ENABLED.get()) {
                    boolean addEccentricity = r.nextInt(2) == 1;

                    if (addEccentricity) {
                        matrixStackIn.mulPose(new Quaternionf().rotateLocalZ((float) Math.toRadians(r.nextInt(Config.PLATTER_ECCENTRICITY_VALUE.get()))));
                    } else {
                        matrixStackIn.mulPose(new Quaternionf().rotateLocalZ((float) Math.toRadians(r.nextInt(Config.PLATTER_ECCENTRICITY_VALUE.get()) * -1)));
                    }
                }

                matrixStackIn.translate(0f, 0.0f, -0.063f);
                if (!itemIn.isEmpty()) {

                    mc.getItemRenderer()
                      .renderStatic(itemIn, ItemDisplayContext.FIXED, combinedLightIn, combinedOverlayIn, matrixStackIn, bufferIn, null, 0);
                }
            }
        }
        matrixStackIn.popPose();
    }
}
