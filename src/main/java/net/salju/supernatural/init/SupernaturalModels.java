package net.salju.supernatural.init;

import net.salju.supernatural.Supernatural;
import net.salju.supernatural.client.model.*;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class SupernaturalModels {
	public static final ModelLayerLocation ANGEL = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(Supernatural.MODID, "angel"), "main");
	public static final ModelLayerLocation SPIRIT = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(Supernatural.MODID, "spirit"), "main");
	public static final ModelLayerLocation POSSESSED = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(Supernatural.MODID, "possessed"), "main");
	public static final ModelLayerLocation GOTHIC = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(Supernatural.MODID, "gothic"), "main");

	@SubscribeEvent
	public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
		event.registerLayerDefinition(SPIRIT, SpiritModel::createBodyLayer);
		event.registerLayerDefinition(POSSESSED, PossessedModel::createBodyLayer);
		event.registerLayerDefinition(ANGEL, AngelModel::createBodyLayer);
		event.registerLayerDefinition(GOTHIC, GothicArmorModel::createBodyLayer);
	}
}