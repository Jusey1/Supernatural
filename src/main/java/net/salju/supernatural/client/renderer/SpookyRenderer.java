package net.salju.supernatural.client.renderer;

import net.salju.supernatural.entity.Spooky;
import net.salju.supernatural.client.model.SpiritModel;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class SpookyRenderer extends MobRenderer<Spooky, SpiritModel<Spooky>> {
	public SpookyRenderer(EntityRendererProvider.Context context) {
		super(context, new SpiritModel(context.bakeLayer(SpiritModel.SPIRIT_MODEL)), 0.3F);
	}

	@Override
	public ResourceLocation getTextureLocation(Spooky ghost) {
		return new ResourceLocation("supernatural:textures/entities/spook.png");
	}

	protected int getBlockLightLevel(Spooky ghost, BlockPos pos) {
		return 15;
	}
}