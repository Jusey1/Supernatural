package net.salju.supernatural;

import net.salju.supernatural.init.*;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.common.MinecraftForge;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.List;
import java.util.Collection;
import java.util.ArrayList;
import java.util.AbstractMap;

@Mod("supernatural")
public class SupernaturalMod {
	public static final String MODID = "supernatural";

	public SupernaturalMod() {
		MinecraftForge.EVENT_BUS.register(this);
		MinecraftForge.EVENT_BUS.register(new SupernaturalClientHUD());
		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
		SupernaturalModSounds.REGISTRY.register(bus);
		SupernaturalBlockEntities.REGISTRY.register(bus);
		SupernaturalBlocks.REGISTRY.register(bus);
		SupernaturalItems.REGISTRY.register(bus);
		SupernaturalTabs.REGISTRY.register(bus);
		SupernaturalMobs.REGISTRY.register(bus);
		SupernaturalEffects.REGISTRY.register(bus);
		SupernaturalEnchantments.REGISTRY.register(bus);
		SupernaturalMenus.REGISTRY.register(bus);
		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, SupernaturalConfig.CONFIG, "supernatural-common.toml");
	}

	private static final Collection<AbstractMap.SimpleEntry<Runnable, Integer>> workQueue = new ConcurrentLinkedQueue<>();

	public static void queueServerWork(int tick, Runnable action) {
		workQueue.add(new AbstractMap.SimpleEntry(action, tick));
	}

	@SubscribeEvent
	public void tick(TickEvent.ServerTickEvent event) {
		if (event.phase == TickEvent.Phase.END) {
			List<AbstractMap.SimpleEntry<Runnable, Integer>> actions = new ArrayList<>();
			workQueue.forEach(work -> {
				work.setValue(work.getValue() - 1);
				if (work.getValue() == 0)
					actions.add(work);
			});
			actions.forEach(e -> e.getKey().run());
			workQueue.removeAll(actions);
		}
	}
}