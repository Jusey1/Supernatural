package net.salju.supernatural.client.renderer;

import net.salju.supernatural.Supernatural;
import net.salju.supernatural.init.SupernaturalClient;
import net.salju.supernatural.entity.Angel;
import net.salju.supernatural.client.model.AngelModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class AngelRenderer extends LivingEntityRenderer<Angel, SupernaturalRenderState, AngelModel<SupernaturalRenderState>> {
	public AngelRenderer(EntityRendererProvider.Context context) {
		super(context, new AngelModel(context.bakeLayer(SupernaturalClient.ANGEL)), 0.5F);
	}

	@Override
	public ResourceLocation getTextureLocation(SupernaturalRenderState target) {
		return ResourceLocation.fromNamespaceAndPath(Supernatural.MODID, "textures/entity/angel.png");
	}

    @Override
    public SupernaturalRenderState createRenderState() {
        return new SupernaturalRenderState();
    }

	@Override
	protected boolean shouldShowName(Angel target, double d) {
		return false;
	}

	@Override
	public void extractRenderState(Angel target, SupernaturalRenderState state, float f1) {
		super.extractRenderState(target, state, f1);
		state.pose = target.getAngelPose();
	}
}