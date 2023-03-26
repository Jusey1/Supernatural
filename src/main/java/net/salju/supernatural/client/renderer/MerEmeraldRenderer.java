package net.salju.supernatural.client.renderer;

import net.salju.supernatural.entity.MerEmeraldEntity;
import net.salju.supernatural.client.model.MerModel;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.MultiBufferSource;

import com.mojang.blaze3d.vertex.PoseStack;

public class MerEmeraldRenderer extends MobRenderer<MerEmeraldEntity, MerModel<MerEmeraldEntity>> {
	public MerEmeraldRenderer(EntityRendererProvider.Context context) {
		super(context, new MerModel(context.bakeLayer(MerModel.MER_MODEL)), 0.5f);
		this.addLayer(new ItemInHandLayer<>(this, context.getItemInHandRenderer()));
		this.addLayer(new EyesLayer<MerEmeraldEntity, MerModel<MerEmeraldEntity>>(this) {
			@Override
			public RenderType renderType() {
				return RenderType.eyes(new ResourceLocation("supernatural:textures/entities/merfolk_emerald_glow.png"));
			}
		});
	}

	@Override
	public ResourceLocation getTextureLocation(MerEmeraldEntity entity) {
		return new ResourceLocation("supernatural:textures/entities/merfolk_emerald.png");
	}

	@Override
	public void render(MerEmeraldEntity mer, float f1, float f2, PoseStack stack, MultiBufferSource buffer, int inty) {
		stack.pushPose();
		if (mer.isOnGround() && !mer.isInWater()) {
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