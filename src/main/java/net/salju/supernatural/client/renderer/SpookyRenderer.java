package net.salju.supernatural.client.renderer;

import net.salju.supernatural.Supernatural;
import net.salju.supernatural.init.SupernaturalModels;
import net.salju.supernatural.entity.Spooky;
import net.salju.supernatural.client.model.SpiritModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class SpookyRenderer extends MobRenderer<Spooky, SpookyState, SpiritModel<SpookyState>> {
	public SpookyRenderer(EntityRendererProvider.Context context) {
		super(context, new SpiritModel(context.bakeLayer(SupernaturalModels.SPIRIT)), 0.3F);
	}

	@Override
	public ResourceLocation getTextureLocation(SpookyState target) {
		return ResourceLocation.fromNamespaceAndPath(Supernatural.MODID, "textures/entity/spook.png");
	}

	@Override
	protected int getBlockLightLevel(Spooky ghost, BlockPos pos) {
		return 15;
	}

	@Override
	public SpookyState createRenderState() {
		return new SpookyState();
	}

	@Override
	public void extractRenderState(Spooky target, SpookyState state, float f1) {
		super.extractRenderState(target, state, f1);
		state.isAggressive = target.isAggressive();
	}
}