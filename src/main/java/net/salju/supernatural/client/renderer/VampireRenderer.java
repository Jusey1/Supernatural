package net.salju.supernatural.client.renderer;

import net.salju.supernatural.init.SupernaturalConfig;
import net.salju.supernatural.entity.Vampire;
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

public class VampireRenderer extends IllagerRenderer<Vampire> {
	public VampireRenderer(EntityRendererProvider.Context context) {
		super(context, new IllagerModel<>(context.bakeLayer(ModelLayers.VINDICATOR)), 0.5F);
		this.addLayer(new EyesLayer<Vampire, IllagerModel<Vampire>>(this) {
			@Override
			public RenderType renderType() {
				return RenderType.eyes(new ResourceLocation("supernatural:textures/entity/vampire_eyes.png"));
			}
		});
		this.addLayer(new ItemInHandLayer<Vampire, IllagerModel<Vampire>>(this, context.getItemInHandRenderer()) {
			public void render(PoseStack pose, MultiBufferSource buffer, int i, Vampire vampy, float f1, float f2, float f3, float f4, float f5, float f6) {
				if (vampy.isAggressive()) {
					super.render(pose, buffer, i, vampy, f1, f2, f3, f4, f5, f6);
				}
			}
		});
	}

	@Override
	public ResourceLocation getTextureLocation(Vampire vampy) {
		if (SupernaturalConfig.ORIGINAL.get()) {
			return new ResourceLocation("supernatural:textures/entity/vampire_original.png");
		} else {
			return new ResourceLocation("supernatural:textures/entity/vampire_normal.png");
		}
	}
}