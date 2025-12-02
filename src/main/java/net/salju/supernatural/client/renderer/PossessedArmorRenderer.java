package net.salju.supernatural.client.renderer;

import net.salju.supernatural.Supernatural;
import net.salju.supernatural.init.SupernaturalClient;
import net.salju.supernatural.client.model.PossessedModel;
import net.salju.supernatural.entity.PossessedArmor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.model.geom.ModelLayers;

public class PossessedArmorRenderer extends HumanoidMobRenderer<PossessedArmor, PossessedModel<PossessedArmor>> {
	public PossessedArmorRenderer(EntityRendererProvider.Context context) {
		super(context, new PossessedModel<>(context.bakeLayer(SupernaturalClient.POSSESSED)), 0.5F);
		this.addLayer(new HumanoidArmorLayer<>(this, new PossessedModel<>(context.bakeLayer(ModelLayers.PLAYER_INNER_ARMOR)), new PossessedModel<>(context.bakeLayer(ModelLayers.PLAYER_OUTER_ARMOR)), context.getModelManager()));
	}

	@Override
	public ResourceLocation getTextureLocation(PossessedArmor target) {
		return ResourceLocation.fromNamespaceAndPath(Supernatural.MODID, "textures/entity/possessed_armor.png");
	}
}