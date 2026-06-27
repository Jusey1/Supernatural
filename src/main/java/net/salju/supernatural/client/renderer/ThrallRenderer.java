package net.salju.supernatural.client.renderer;

import net.salju.supernatural.Supernatural;
import net.salju.supernatural.init.SupernaturalClient;
import net.salju.supernatural.client.renderer.layers.ThrallEyesLayer;
import net.salju.supernatural.client.model.ThrallModel;
import net.salju.supernatural.entity.Thrall;
import net.minecraft.resources.Identifier;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.ArmorModelSet;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.model.geom.ModelLayers;

public class ThrallRenderer extends HumanoidMobRenderer<Thrall, SupernaturalRenderState, ThrallModel<SupernaturalRenderState>> {
	public ThrallRenderer(EntityRendererProvider.Context context) {
		super(context, new ThrallModel<>(context.bakeLayer(SupernaturalClient.THRALL)), 0.5F);
        this.addLayer(new HumanoidArmorLayer<>(this, ArmorModelSet.bake(ModelLayers.ZOMBIE_ARMOR, context.getModelSet(), ThrallModel::new), ArmorModelSet.bake(ModelLayers.ZOMBIE_BABY_ARMOR, context.getModelSet(), ThrallModel::new), context.getEquipmentRenderer()));
        this.addLayer(new ThrallEyesLayer<>(this));
	}

	@Override
	public Identifier getTextureLocation(SupernaturalRenderState target) {
		return Identifier.fromNamespaceAndPath(Supernatural.MODID, "textures/entity/thrall/thrall.png");
	}

    @Override
    public SupernaturalRenderState createRenderState() {
        return new SupernaturalRenderState();
    }

    @Override
    public void extractRenderState(Thrall target, SupernaturalRenderState state, float f1) {
        super.extractRenderState(target, state, f1);
        state.isAggressive = target.isAggressive();
        state.isLeftHanded = target.isLeftHanded();
    }
}