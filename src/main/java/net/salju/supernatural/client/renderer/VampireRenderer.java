package net.salju.supernatural.client.renderer;

import net.salju.supernatural.Supernatural;
import net.salju.supernatural.entity.Vampire;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.entity.state.IllagerRenderState;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.client.renderer.entity.IllagerRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.IllagerModel;
import com.mojang.blaze3d.vertex.PoseStack;

public class VampireRenderer extends IllagerRenderer<Vampire, IllagerRenderState> {
	public VampireRenderer(EntityRendererProvider.Context context) {
		super(context, new IllagerModel<>(context.bakeLayer(ModelLayers.VINDICATOR)), 0.5F);
		this.addLayer(new EyesLayer<>(this) {
			@Override
			public RenderType renderType() {
				return RenderType.eyes(ResourceLocation.fromNamespaceAndPath(Supernatural.MODID, "textures/entity/vampires/vampire_eyes.png"));
			}
		});
		this.addLayer(new ItemInHandLayer<>(this, context.getItemRenderer()) {
			public void render(PoseStack pose, MultiBufferSource buffer, int i, IllagerRenderState target, float f1, float f2) {
				if (target.isAggressive) {
					super.render(pose, buffer, i, target, f1, f2);
				}
			}
		});
	}

	@Override
	public ResourceLocation getTextureLocation(IllagerRenderState target) {
		return ResourceLocation.fromNamespaceAndPath(Supernatural.MODID, "textures/entity/vampires/vampire.png");
	}

	@Override
	public IllagerRenderState createRenderState() {
		return new IllagerRenderState();
	}
}