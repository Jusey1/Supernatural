package net.salju.supernatural.client.renderer;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.model.geom.ModelLayers;

import net.salju.supernatural.entity.PossessedArmorEntity;
import net.salju.supernatural.client.model.PossessedModel;

public class PossessedArmorRenderer extends HumanoidMobRenderer<PossessedArmorEntity, PossessedModel<PossessedArmorEntity>> {
	public PossessedArmorRenderer(EntityRendererProvider.Context context) {
		super(context, new PossessedModel(context.bakeLayer(ModelLayers.PLAYER)), 0.5f);
		this.addLayer(new HumanoidArmorLayer(this, new PossessedModel(context.bakeLayer(ModelLayers.PLAYER_INNER_ARMOR)),
				new PossessedModel(context.bakeLayer(ModelLayers.PLAYER_OUTER_ARMOR))));
	}

	@Override
	public ResourceLocation getTextureLocation(PossessedArmorEntity entity) {
		return new ResourceLocation("supernatural:textures/entities/empty_texture.png");
	}
}