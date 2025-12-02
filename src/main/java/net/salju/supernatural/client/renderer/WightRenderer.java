package net.salju.supernatural.client.renderer;

import net.salju.supernatural.Supernatural;
import net.salju.supernatural.init.SupernaturalClient;
import net.salju.supernatural.client.renderer.layers.WightClothingLayer;
import net.salju.supernatural.client.renderer.layers.WightEyesLayer;
import net.salju.supernatural.client.model.WightModel;
import net.salju.supernatural.entity.Wight;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.model.geom.ModelLayers;

public class WightRenderer extends HumanoidMobRenderer<Wight, WightModel<Wight>> {
	public WightRenderer(EntityRendererProvider.Context context) {
		super(context, new WightModel<>(context.bakeLayer(SupernaturalClient.WIGHT)), 0.5F);
		this.addLayer(new HumanoidArmorLayer<>(this, new WightModel<>(context.bakeLayer(ModelLayers.SKELETON_INNER_ARMOR)), new WightModel<>(context.bakeLayer(ModelLayers.SKELETON_OUTER_ARMOR)), context.getModelManager()));
        this.addLayer(new WightClothingLayer<>(this, context.getModelSet()));
        this.addLayer(new WightEyesLayer<>(this));
	}

	@Override
	public ResourceLocation getTextureLocation(Wight target) {
		return ResourceLocation.fromNamespaceAndPath(Supernatural.MODID, "textures/entity/wight/wight.png");
	}
}