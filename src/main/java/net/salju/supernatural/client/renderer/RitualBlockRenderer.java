package net.salju.supernatural.client.renderer;

import net.salju.supernatural.block.entity.RitualBlockEntity;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.phys.Vec3;
import com.mojang.math.Axis;
import com.mojang.blaze3d.vertex.PoseStack;

public class RitualBlockRenderer implements BlockEntityRenderer<RitualBlockEntity, RitualBlockState> {
	private final ItemModelResolver item;

	public RitualBlockRenderer(BlockEntityRendererProvider.Context context) {
		this.item = context.itemModelResolver();
	}

    @Override
    public RitualBlockState createRenderState() {
        return new RitualBlockState();
    }

    @Override
    public void extractRenderState(RitualBlockEntity target, RitualBlockState state, float f1, Vec3 v, ModelFeatureRenderer.CrumblingOverlay progress) {
        BlockEntityRenderState.extractBase(target, state, progress);
        state.main = f1;
        state.item = target.getItem(0);
        state.time = target.getLevel().getGameTime();
        this.item.updateForTopItem(state.itemState, state.item, ItemDisplayContext.GROUND, target.getLevel(), null, 0);
    }

	@Override
	public void submit(RitualBlockState state, PoseStack pose, SubmitNodeCollector buffer, CameraRenderState c) {
		if (!state.item.isEmpty()) {
			this.renderItem(state, pose, buffer);
		}
	}

	protected void renderItem(RitualBlockState state, PoseStack pose, SubmitNodeCollector buffer) {
		pose.pushPose();
		pose.translate(0.5, 0.75, 0.5);
		pose.mulPose(Axis.YP.rotation((float) (((Math.floorMod(state.time, (long) ((int) (2 * 360f))) + state.main) / (float) ((int) (2 * 360f))) * Math.PI * 10)));
        state.itemState.submit(pose, buffer, state.lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor);
		pose.popPose();
	}
}