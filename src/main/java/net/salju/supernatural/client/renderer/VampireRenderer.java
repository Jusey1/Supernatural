package net.salju.supernatural.client.renderer;

import net.salju.supernatural.entity.VampireEntity;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.client.renderer.entity.IllagerRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.IllagerModel;

import com.mojang.blaze3d.vertex.PoseStack;

public class VampireRenderer extends IllagerRenderer<VampireEntity> {
	public VampireRenderer(EntityRendererProvider.Context context) {
		super(context, new IllagerModel<>(context.bakeLayer(ModelLayers.VINDICATOR)), 0.5F);
		this.addLayer(new EyesLayer<VampireEntity, IllagerModel<VampireEntity>>(this) {
			@Override
			public RenderType renderType() {
				return RenderType.eyes(new ResourceLocation("supernatural:textures/entities/vampire_eyes.png"));
			}
		});
		this.addLayer(new ItemInHandLayer<VampireEntity, IllagerModel<VampireEntity>>(this, context.getItemInHandRenderer()) {
			public void render(PoseStack pose, MultiBufferSource buffer, int inty, VampireEntity vampy, float f1, float f2, float f3, float f4,
					float f5, float f6) {
				if (vampy.isAggressive()) {
					super.render(pose, buffer, inty, vampy, f1, f2, f3, f4, f5, f6);
				}
			}
		});
	}

	@Override
	public ResourceLocation getTextureLocation(VampireEntity entity) {
		if ((entity.getDisplayName().getString()).equals("Bob")) {
			return new ResourceLocation("supernatural:textures/entities/vampire_bob.png");
		} else if ((entity.getDisplayName().getString()).equals("Johnny")) {
			return new ResourceLocation("supernatural:textures/entities/vampire_johnny.png");
		} else {
			return new ResourceLocation("supernatural:textures/entities/vampire_normal.png");
		}
	}
}