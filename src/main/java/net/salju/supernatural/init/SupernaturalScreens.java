package net.salju.supernatural.init;

import net.salju.supernatural.gui.RitualBookScreen;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraft.client.gui.screens.MenuScreens;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class SupernaturalScreens {
	@SubscribeEvent
	public static void clientLoad(FMLClientSetupEvent event) {
		event.enqueueWork(() -> {
			MenuScreens.register(SupernaturalMenus.RITUALBOOK.get(), RitualBookScreen::new);
		});
	}
}