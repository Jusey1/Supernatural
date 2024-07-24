package net.salju.supernatural.init;

import net.salju.supernatural.client.model.*;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.model.geom.ModelLayerLocation;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class SupernaturalModels {
	public static final ModelLayerLocation ANGEL = new ModelLayerLocation(new ResourceLocation("supernatural", "angel"), "main");
	public static final ModelLayerLocation FYSH = new ModelLayerLocation(new ResourceLocation("supernatural", "fysh"), "main");
	public static final ModelLayerLocation SPIRIT = new ModelLayerLocation(new ResourceLocation("supernatural", "spirit"), "main");
	public static final ModelLayerLocation POSSESSED = new ModelLayerLocation(new ResourceLocation("supernatural", "possessed"), "main");
	public static final ModelLayerLocation GOTHIC = new ModelLayerLocation(new ResourceLocation("supernatural", "gothic"), "main");
	public static final ModelLayerLocation K_GOTHIC = new ModelLayerLocation(new ResourceLocation("supernatural", "gothic_kobold"), "main");

	@SubscribeEvent
	public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
		event.registerLayerDefinition(SPIRIT, SpiritModel::createBodyLayer);
		event.registerLayerDefinition(POSSESSED, PossessedModel::createBodyLayer);
		event.registerLayerDefinition(FYSH, MerModel::createBodyLayer);
		event.registerLayerDefinition(ANGEL, AngelModel::createBodyLayer);
		event.registerLayerDefinition(GOTHIC, GothicArmorModel::createBodyLayer);
		event.registerLayerDefinition(K_GOTHIC, GothicKoboldArmorModel::createBodyLayer);
	}
}