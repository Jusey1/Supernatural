package net.salju.supernatural.client.renderer;

import net.salju.supernatural.init.SupernaturalModels;
import net.salju.supernatural.entity.MerD;
import net.salju.supernatural.client.model.MerModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.PoseStack;

public class MerDiamondRenderer extends MobRenderer<MerD, MerModel<MerD>> {
	public MerDiamondRenderer(EntityRendererProvider.Context context) {
		super(context, new MerModel(context.bakeLayer(SupernaturalModels.FYSH)), 0.5f);
		this.addLayer(new ItemInHandLayer<>(this, context.getItemInHandRenderer()));
		this.addLayer(new EyesLayer<MerD, MerModel<MerD>>(this) {
			@Override
			public RenderType renderType() {
				return RenderType.eyes(new ResourceLocation("supernatural:textures/entity/merfolk_diamond_glow.png"));
			}
		});
	}

	@Override
	public ResourceLocation getTextureLocation(MerD mer) {
		return new ResourceLocation("supernatural:textures/entity/merfolk_diamond.png");
	}

	@Override
	public void render(MerD mer, float f1, float f2, PoseStack stack, MultiBufferSource buffer, int inty) {
		stack.pushPose();
		if (mer.onGround() && !mer.isInWater()) {
			stack.translate(0, -0.5, 0);
		} else {
			stack.translate(0, 0, 0);
		}
		float scale = 1.0F;
		stack.scale(scale, scale, scale);
		super.render(mer, f1, f2, stack, buffer, inty);
		stack.popPose();
	}
}