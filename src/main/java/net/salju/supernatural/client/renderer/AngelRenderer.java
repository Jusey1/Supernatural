package net.salju.supernatural.client.renderer;

import net.salju.supernatural.entity.Angel;
import net.salju.supernatural.client.model.AngelModel;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class AngelRenderer extends MobRenderer<Angel, AngelModel<Angel>> {
	public AngelRenderer(EntityRendererProvider.Context context) {
		super(context, new AngelModel(context.bakeLayer(AngelModel.ANGEL_MODEL)), 0.5f);
	}

	@Override
	public ResourceLocation getTextureLocation(Angel angel) {
		return new ResourceLocation("supernatural:textures/entities/angel.png");
	}
}