package net.salju.supernatural.init;

import net.salju.supernatural.events.SupernaturalManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.Minecraft;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class SupernaturalClientHUD {
	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent(receiveCanceled = true)
	public void onOverlayRender(RenderGuiOverlayEvent.Pre event) {
		Minecraft mc = Minecraft.getInstance();
		GuiGraphics gui = event.getGuiGraphics();
		if (event.getOverlay().equals(VanillaGuiOverlay.FOOD_LEVEL.type())) {
			if (SupernaturalManager.isVampire(mc.player)) {
				event.setCanceled(true);
			}
		}
	}
}