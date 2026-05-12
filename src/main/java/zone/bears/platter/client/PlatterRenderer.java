package zone.bears.platter.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import it.unimi.dsi.fastutil.HashCommon;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.joml.Random;
import org.jspecify.annotations.Nullable;
import zone.bears.platter.Config;
import zone.bears.platter.tile.PlatterTile;

import java.util.List;

public class PlatterRenderer<T extends PlatterTile> implements BlockEntityRenderer<T, PlatterBlockEntityRenderState> {
    private final ItemModelResolver itemModelResolver;

    public PlatterRenderer(BlockEntityRendererProvider.Context context) {
        this.itemModelResolver = context.itemModelResolver();
    }

    @Override
    public PlatterBlockEntityRenderState createRenderState() {
        return new PlatterBlockEntityRenderState();
    }

    @Override
    public void extractRenderState(T blockEntity, PlatterBlockEntityRenderState state, float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
        List<ItemStack> items = blockEntity.itemStackHandler.copyToList();
        int seed = HashCommon.long2int(blockEntity.getBlockPos().asLong());
        state.seed = seed;

        for (int i = 0; i < items.size(); i++) {
            if (items.get(i) != ItemStack.EMPTY) {
                ItemStackRenderState itemStackRenderState = new ItemStackRenderState();
                itemModelResolver.updateForTopItem(itemStackRenderState, items.get(i), ItemDisplayContext.NONE, blockEntity.getLevel(), blockEntity, seed++);
                state.items[i] = itemStackRenderState;
            }
        }
    }

    @Override
    public void submit(PlatterBlockEntityRenderState platterBlockEntityRenderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState) {

        Random r = new Random(platterBlockEntityRenderState.seed);

        for (int i = 0; i < platterBlockEntityRenderState.items.length; i++) {
            ItemStackRenderState itemStackRenderState = platterBlockEntityRenderState.items[i];
            if (itemStackRenderState != null) {
                poseStack.pushPose();
                poseStack.translate(0.5, i * 0.063f + 1.5 / 16.0, 0.5);
                poseStack.scale(14 / 16f, 1f, 14 / 16f);
                poseStack.mulPose(Axis.XP.rotationDegrees(90));

                if (Config.PLATTER_ECCENTRICITY_ENABLED.get()) {
                    boolean rotateClockwise = r.nextFloat() < 0.5f;
                    int rotationDegrees = r.nextInt(Config.PLATTER_ECCENTRICITY_VALUE.getAsInt()) * (rotateClockwise ? 1 : -1);

                    poseStack.mulPose(Axis.ZP.rotationDegrees(rotationDegrees));
                }

                itemStackRenderState.submit(poseStack, submitNodeCollector, platterBlockEntityRenderState.lightCoords, OverlayTexture.NO_OVERLAY, 0);
                poseStack.popPose();
            }
        }

    }
}

