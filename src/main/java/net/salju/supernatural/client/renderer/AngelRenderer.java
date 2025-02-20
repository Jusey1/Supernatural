package net.salju.supernatural.client.renderer;

import net.salju.supernatural.Supernatural;
import net.salju.supernatural.init.SupernaturalModels;
import net.salju.supernatural.entity.Angel;
import net.salju.supernatural.client.model.AngelModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class AngelRenderer extends LivingEntityRenderer<Angel, AngelState, AngelModel<AngelState>> {
	public AngelRenderer(EntityRendererProvider.Context context) {
		super(context, new AngelModel(context.bakeLayer(SupernaturalModels.ANGEL)), 0.5f);
	}

	@Override
	public ResourceLocation getTextureLocation(AngelState target) {
		return ResourceLocation.fromNamespaceAndPath(Supernatural.MODID, "textures/entity/angel.png");
	}

	@Override
	public AngelState createRenderState() {
		return new AngelState();
	}

	@Override
	protected boolean shouldShowName(Angel target, double d) {
		return false;
	}

	@Override
	public void extractRenderState(Angel target, AngelState state, float f1) {
		super.extractRenderState(target, state, f1);
		state.angelPose = target.getAngelPose();
	}
}