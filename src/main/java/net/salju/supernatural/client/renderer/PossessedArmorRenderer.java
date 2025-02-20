package net.salju.supernatural.client.renderer;

import net.salju.supernatural.Supernatural;
import net.salju.supernatural.entity.PossessedArmor;
import net.salju.supernatural.client.model.PossessedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.model.geom.ModelLayers;

public class PossessedArmorRenderer extends HumanoidMobRenderer<PossessedArmor, PossessedArmorState, PossessedModel<PossessedArmorState>> {
	public PossessedArmorRenderer(EntityRendererProvider.Context context) {
		super(context, new PossessedModel(context.bakeLayer(ModelLayers.PLAYER)), 0.5f);
		this.addLayer(new HumanoidArmorLayer(this, new PossessedModel(context.bakeLayer(ModelLayers.PLAYER_INNER_ARMOR)), new PossessedModel(context.bakeLayer(ModelLayers.PLAYER_OUTER_ARMOR)), context.getEquipmentRenderer()));
	}

	@Override
	public ResourceLocation getTextureLocation(PossessedArmorState target) {
		return ResourceLocation.fromNamespaceAndPath(Supernatural.MODID, "textures/entity/empty_texture.png");
	}

	@Override
	public PossessedArmorState createRenderState() {
		return new PossessedArmorState();
	}

	@Override
	public void extractRenderState(PossessedArmor target, PossessedArmorState state, float f1) {
		super.extractRenderState(target, state, f1);
		HumanoidMobRenderer.extractHumanoidRenderState(target, state, f1);
		state.isAggressive = target.isAggressive();
		state.isLeftHanded = target.isLeftHanded();
	}
}