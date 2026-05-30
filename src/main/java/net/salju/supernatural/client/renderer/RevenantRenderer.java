package net.salju.supernatural.client.renderer;

import net.salju.supernatural.Supernatural;
import net.salju.supernatural.init.SupernaturalClient;
import net.salju.supernatural.client.model.RevenantModel;
import net.salju.supernatural.client.renderer.layers.RevenantEyesLayer;
import net.salju.supernatural.entity.Revenant;
import net.minecraft.resources.Identifier;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;

public class RevenantRenderer extends MobRenderer<Revenant, SupernaturalRenderState, RevenantModel<SupernaturalRenderState>> {
	public RevenantRenderer(EntityRendererProvider.Context context) {
		super(context, new RevenantModel<>(context.bakeLayer(SupernaturalClient.REVENANT)), 0.5F);
        this.addLayer(new RevenantEyesLayer<>(this));
	}

	@Override
	public Identifier getTextureLocation(SupernaturalRenderState target) {
		return Identifier.fromNamespaceAndPath(Supernatural.MODID, "textures/entity/revenant/revenant.png");
	}

    @Override
    public SupernaturalRenderState createRenderState() {
        return new SupernaturalRenderState();
    }

    @Override
    protected boolean shouldShowName(Revenant target, double d) {
        return false;
    }

    @Override
    public void extractRenderState(Revenant target, SupernaturalRenderState state, float f1) {
        super.extractRenderState(target, state, f1);
        state.isAggressive = target.isAggressive();
        state.isCastingSpell = target.isCastingSpell();
        state.isInvisible = target.isInvisible();
        state.isLeftHanded = target.isLeftHanded();
    }
}