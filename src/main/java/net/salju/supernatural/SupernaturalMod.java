package net.salju.supernatural;

import net.salju.supernatural.init.*;
import net.minecraftforge.network.simple.SimpleChannel;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.common.MinecraftForge;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.FriendlyByteBuf;
import java.util.function.Supplier;
import java.util.function.Function;
import java.util.function.BiConsumer;
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
		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, SupernaturalConfig.CONFIG, "supernatural-common.toml");
	}

	private static final String V = "1";
	public static final SimpleChannel PACKET = NetworkRegistry.newSimpleChannel(new ResourceLocation(MODID, MODID), () -> V, V::equals, V::equals);
	private static int id = 0;

	public static <T> void addNetworkMessage(Class<T> type, BiConsumer<T, FriendlyByteBuf> encoder, Function<FriendlyByteBuf, T> decoder, BiConsumer<T, Supplier<NetworkEvent.Context>> supply) {
		PACKET.registerMessage(id, type, encoder, decoder, supply);
		id++;
	}

	public static <MSG> void sendToClientPlayer(MSG msg, ServerPlayer ply) {
		PACKET.sendTo(msg, ply.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
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