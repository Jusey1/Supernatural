package net.salju.supernatural.events;

import net.salju.supernatural.events.potion.*;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class SupernaturalPotions {
	@SubscribeEvent
	public static void init(FMLCommonSetupEvent event) {
		event.enqueueWork(() -> BrewingRecipeRegistry.addRecipe(new VampireDustPotion()));
		event.enqueueWork(() -> BrewingRecipeRegistry.addRecipe(new EctoPotion()));
	}
}