package net.salju.supernatural.network;

import net.salju.supernatural.SupernaturalMod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class NetworkSetup {
	@SubscribeEvent
	public static void registerMessage(FMLCommonSetupEvent event) {
		SupernaturalMod.addNetworkMessage(UsedContract.class, UsedContract::buffer, UsedContract::reader, UsedContract::handler);
	}
}