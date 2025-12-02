package net.salju.supernatural.client.renderer;

import net.salju.supernatural.Supernatural;
import net.salju.supernatural.init.SupernaturalClient;
import net.salju.supernatural.entity.Angel;
import net.salju.supernatural.client.model.AngelModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class AngelRenderer extends LivingEntityRenderer<Angel, AngelModel<Angel>> {
	public AngelRenderer(EntityRendererProvider.Context context) {
		super(context, new AngelModel<>(context.bakeLayer(SupernaturalClient.ANGEL)), 0.5F);
	}

	@Override
	public ResourceLocation getTextureLocation(Angel target) {
		return ResourceLocation.fromNamespaceAndPath(Supernatural.MODID, "textures/entity/angel.png");
	}

	@Override
	protected boolean shouldShowName(Angel target) {
		return false;
	}
}