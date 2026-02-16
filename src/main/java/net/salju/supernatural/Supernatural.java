package net.salju.supernatural;

import net.salju.supernatural.compat.*;
import net.salju.supernatural.init.*;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.ModContainer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLEnvironment;

@Mod("supernatural")
public class Supernatural {
	public static final String MODID = "supernatural";

	public Supernatural(ModContainer mod, IEventBus bus) {
		NeoForge.EVENT_BUS.register(this);
		SupernaturalData.REGISTRY.register(bus);
		SupernaturalSounds.REGISTRY.register(bus);
		SupernaturalBlocks.BE.register(bus);
		SupernaturalBlocks.REGISTRY.register(bus);
		SupernaturalBlocks.POI.register(bus);
		SupernaturalItems.REGISTRY.register(bus);
		SupernaturalTabs.REGISTRY.register(bus);
		SupernaturalMobs.REGISTRY.register(bus);
		SupernaturalEffects.REGISTRY.register(bus);
		mod.registerConfig(ModConfig.Type.COMMON, SupernaturalConfig.CONFIG, "supernatural-common.toml");
		if (FMLEnvironment.getDist().isClient()) {
			if (ModList.get().isLoaded("appleskin")) {
				NeoForge.EVENT_BUS.register(new Appleskin());
			}
		}
	}

	@SubscribeEvent
	public void tick(ServerTickEvent.Post event) {
		//
	}
}