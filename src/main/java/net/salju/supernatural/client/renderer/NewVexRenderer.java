package net.salju.supernatural.client.renderer;

import net.salju.supernatural.entity.NewVexEntity;
import net.salju.supernatural.client.model.SpiritModel;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class NewVexRenderer extends MobRenderer<NewVexEntity, SpiritModel<NewVexEntity>> {
	private static final ResourceLocation VEX_LOCATION = new ResourceLocation("supernatural:textures/entities/new_vex.png");
	private static final ResourceLocation VEX_CHARGING_LOCATION = new ResourceLocation("supernatural:textures/entities/new_vex_charging.png");

	public NewVexRenderer(EntityRendererProvider.Context context) {
		super(context, new SpiritModel(context.bakeLayer(SpiritModel.SPIRIT_MODEL)), 0.3F);
		this.addLayer(new ItemInHandLayer<>(this, context.getItemInHandRenderer()));
	}

	@Override
	public ResourceLocation getTextureLocation(NewVexEntity ghost) {
		return ghost.isCharging() ? VEX_CHARGING_LOCATION : VEX_LOCATION;
	}

	protected int getBlockLightLevel(NewVexEntity ghost, BlockPos pos) {
		return 15;
	}
}