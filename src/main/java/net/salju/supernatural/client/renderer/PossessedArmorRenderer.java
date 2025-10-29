package net.salju.supernatural.client.renderer;

import net.salju.supernatural.Supernatural;
import net.salju.supernatural.entity.PossessedArmor;
import net.salju.supernatural.client.model.PossessedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.ArmorModelSet;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.model.geom.ModelLayers;

public class PossessedArmorRenderer extends HumanoidMobRenderer<PossessedArmor, SupernaturalRenderState, PossessedModel<SupernaturalRenderState>> {
	public PossessedArmorRenderer(EntityRendererProvider.Context context) {
		super(context, new PossessedModel(context.bakeLayer(ModelLayers.PLAYER)), 0.5F);
		this.addLayer(new HumanoidArmorLayer(this, ArmorModelSet.bake(ModelLayers.PLAYER_ARMOR, context.getModelSet(), PossessedModel::new), ArmorModelSet.bake(ModelLayers.PLAYER_ARMOR, context.getModelSet(), PossessedModel::new), context.getEquipmentRenderer()));
	}

	@Override
	public ResourceLocation getTextureLocation(SupernaturalRenderState state) {
		return ResourceLocation.fromNamespaceAndPath(Supernatural.MODID, "textures/entity/empty_texture.png");
	}

    @Override
    public SupernaturalRenderState createRenderState() {
        return new SupernaturalRenderState();
    }

	@Override
	public void extractRenderState(PossessedArmor target, SupernaturalRenderState state, float f1) {
		super.extractRenderState(target, state, f1);
		HumanoidMobRenderer.extractHumanoidRenderState(target, state, f1, this.itemModelResolver);
		state.isAggressive = target.isAggressive();
		state.isLeftHanded = target.isLeftHanded();
	}
}