package net.salju.supernatural.client.renderer;

import net.salju.supernatural.Supernatural;
import net.salju.supernatural.entity.Necromancer;
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

public class NecromancerRenderer extends IllagerRenderer<Necromancer> {
	public NecromancerRenderer(EntityRendererProvider.Context context) {
		super(context, new IllagerModel<>(context.bakeLayer(ModelLayers.EVOKER)), 0.5F);
		this.addLayer(new EyesLayer<>(this) {
			@Override
			public RenderType renderType() {
				return RenderType.eyes(ResourceLocation.fromNamespaceAndPath(Supernatural.MODID, "textures/entity/vampires/vampire_eyes.png"));
			}
		});
		this.addLayer(new ItemInHandLayer<>(this, context.getItemInHandRenderer()) {
			public void render(PoseStack pose, MultiBufferSource buffer, int i, Necromancer target, float f1, float f2, float f3, float f4, float f5, float f6) {
				if (target.isAggressive()) {
					super.render(pose, buffer, i, target, f1, f2, f3, f4, f5, f6);
				}
			}
		});
		this.model.getHat().visible = true;
	}

	@Override
	public ResourceLocation getTextureLocation(Necromancer target) {
		return ResourceLocation.fromNamespaceAndPath(Supernatural.MODID, "textures/entity/vampires/vampire_necromancer.png");
	}
}