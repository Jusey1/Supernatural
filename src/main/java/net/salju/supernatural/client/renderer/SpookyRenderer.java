package net.salju.supernatural.client.renderer;

import net.salju.supernatural.Supernatural;
import net.salju.supernatural.init.SupernaturalClient;
import net.salju.supernatural.entity.Spooky;
import net.salju.supernatural.client.model.SpiritModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class SpookyRenderer extends MobRenderer<Spooky, SpiritModel<Spooky>> {
	public SpookyRenderer(EntityRendererProvider.Context context) {
		super(context, new SpiritModel<>(context.bakeLayer(SupernaturalClient.SPIRIT)), 0.3F);
	}

	@Override
	public ResourceLocation getTextureLocation(Spooky target) {
		return ResourceLocation.fromNamespaceAndPath(Supernatural.MODID, "textures/entity/spook.png");
	}

    @Override
	protected int getBlockLightLevel(Spooky target, BlockPos pos) {
		return 15;
	}
}