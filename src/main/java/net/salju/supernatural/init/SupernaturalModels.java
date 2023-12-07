package net.salju.supernatural.init;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.api.distmarker.Dist;

import net.salju.supernatural.client.model.*;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class SupernaturalModels {
	@SubscribeEvent
	public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
		event.registerLayerDefinition(SpiritModel.SPIRIT_MODEL, SpiritModel::createBodyLayer);
		event.registerLayerDefinition(PossessedModel.POSSESSED_MODEL, PossessedModel::createBodyLayer);
		event.registerLayerDefinition(MerModel.MER_MODEL, MerModel::createBodyLayer);
		event.registerLayerDefinition(AngelModel.ANGEL_MODEL, AngelModel::createBodyLayer);
		event.registerLayerDefinition(GothicArmorModel.GOTHIC_ARMOR, GothicArmorModel::createBodyLayer);
		event.registerLayerDefinition(GothicKoboldArmorModel.KOBOLD_GOTHIC_ARMOR, GothicKoboldArmorModel::createBodyLayer);
	}
}