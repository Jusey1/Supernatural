package net.salju.supernatural.client.renderer;

import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.salju.supernatural.block.entity.TreasureVaultBlockEntity;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.phys.Vec3;
import com.mojang.math.Axis;
import com.mojang.blaze3d.vertex.PoseStack;
import org.joml.Quaternionf;

public class TreasureVaultRenderer implements BlockEntityRenderer<TreasureVaultBlockEntity, SupernaturalBlockState> {
	private final ItemModelResolver item;

	public TreasureVaultRenderer(BlockEntityRendererProvider.Context context) {
        this.item = context.itemModelResolver();
	}

    @Override
    public SupernaturalBlockState createRenderState() {
        return new SupernaturalBlockState();
    }

    @Override
    public void extractRenderState(TreasureVaultBlockEntity target, SupernaturalBlockState state, float f1, Vec3 v, ModelFeatureRenderer.CrumblingOverlay progress) {
        BlockEntityRenderState.extractBase(target, state, progress);
        state.main = f1;
        state.item = target.getRenderStack();
        state.time = target.getLevel().getGameTime();
        this.item.updateForTopItem(state.itemState, state.item, ItemDisplayContext.GROUND, target.getLevel(), null, 0);
    }

    @Override
    public void submit(SupernaturalBlockState state, PoseStack pose, SubmitNodeCollector buffer, CameraRenderState c) {
        if (!state.item.isEmpty()) {
            this.renderItem(state, pose, buffer);
        }
    }

    protected void renderItem(SupernaturalBlockState state, PoseStack pose, SubmitNodeCollector buffer) {
        pose.pushPose();
        pose.translate(0.5, 0.5, 0.5);
        pose.mulPose(Axis.YP.rotation(((Math.floorMod(state.time, (long) ((int) (2 * 360.0F))) + state.main) / (float) ((int) (2 * 360.0F))) * 3.5F * 10));
        state.itemState.submit(pose, buffer, state.lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor);
        pose.popPose();
    }
}