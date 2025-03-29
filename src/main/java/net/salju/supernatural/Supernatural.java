package net.salju.supernatural;

import net.salju.supernatural.init.*;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.ModContainer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.fml.util.thread.SidedThreadGroups;
import net.minecraft.util.Tuple;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.List;
import java.util.Collection;
import java.util.ArrayList;

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
	}

	private static final Collection<Tuple<Runnable, Integer>> workQueue = new ConcurrentLinkedQueue<>();

	public static void queueServerWork(int tick, Runnable action) {
		if (Thread.currentThread().getThreadGroup() == SidedThreadGroups.SERVER) {
			workQueue.add(new Tuple<>(action, tick));
		}
	}

	@SubscribeEvent
	public void tick(ServerTickEvent.Post event) {
		List<Tuple<Runnable, Integer>> actions = new ArrayList<>();
		workQueue.forEach(work -> {
			work.setB(work.getB() - 1);
			if (work.getB() == 0) {
				actions.add(work);
			}
		});
		actions.forEach(e -> e.getA().run());
		workQueue.removeAll(actions);
	}
}