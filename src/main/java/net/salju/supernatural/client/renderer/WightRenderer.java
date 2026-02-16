package net.salju.supernatural.client.renderer;

import net.salju.supernatural.Supernatural;
import net.salju.supernatural.init.SupernaturalClient;
import net.salju.supernatural.client.renderer.layers.WightClothingLayer;
import net.salju.supernatural.client.renderer.layers.WightEyesLayer;
import net.salju.supernatural.client.model.WightModel;
import net.salju.supernatural.entity.Wight;
import net.minecraft.resources.Identifier;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.ArmorModelSet;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.model.geom.ModelLayers;

public class WightRenderer extends HumanoidMobRenderer<Wight, SupernaturalRenderState, WightModel<SupernaturalRenderState>> {
	public WightRenderer(EntityRendererProvider.Context context) {
		super(context, new WightModel<>(context.bakeLayer(SupernaturalClient.WIGHT)), 0.5F);
        this.addLayer(new HumanoidArmorLayer<>(this, ArmorModelSet.bake(ModelLayers.SKELETON_ARMOR, context.getModelSet(), WightModel::new), context.getEquipmentRenderer()));
        this.addLayer(new WightClothingLayer<>(this, context.getModelSet()));
        this.addLayer(new WightEyesLayer<>(this));
	}

	@Override
	public Identifier getTextureLocation(SupernaturalRenderState target) {
		return Identifier.fromNamespaceAndPath(Supernatural.MODID, "textures/entity/wight/wight.png");
	}

    @Override
    public SupernaturalRenderState createRenderState() {
        return new SupernaturalRenderState();
    }

    @Override
    public void extractRenderState(Wight target, SupernaturalRenderState state, float f1) {
        super.extractRenderState(target, state, f1);
        state.isAggressive = target.isAggressive();
        state.isCastingSpell = target.isCastingSpell();
        state.isCharging = target.isCharging();
        state.isLeftHanded = target.isLeftHanded();
    }
}