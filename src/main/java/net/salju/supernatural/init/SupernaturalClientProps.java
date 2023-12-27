package net.salju.supernatural.init;

import net.salju.supernatural.events.SupernaturalManager;
import net.salju.supernatural.SupernaturalMod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class SupernaturalClientProps {
	@SubscribeEvent
	public static void onClientSetup(FMLClientSetupEvent event) {
		ItemProperties.registerGeneric(new ResourceLocation(SupernaturalMod.MODID, "signed"), SIGNED);
	}

	private static final ClampedItemPropertyFunction SIGNED = (stack, world, target, i) -> {
		return SupernaturalManager.getUUID(stack) != null ? 0.1F : 0.0F;
	};
}