package net.salju.supernatural.client.renderer;

import net.salju.supernatural.entity.Angel;
import net.salju.supernatural.client.model.AngelModel;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class AngelRenderer extends LivingEntityRenderer<Angel, AngelModel<Angel>> {
	public AngelRenderer(EntityRendererProvider.Context context) {
		super(context, new AngelModel(context.bakeLayer(AngelModel.ANGEL_MODEL)), 0.5f);
	}

	@Override
	public ResourceLocation getTextureLocation(Angel target) {
		return new ResourceLocation("supernatural:textures/entity/angel.png");
	}

	@Override
	protected boolean shouldShowName(Angel target) {
		return false;
	}
}