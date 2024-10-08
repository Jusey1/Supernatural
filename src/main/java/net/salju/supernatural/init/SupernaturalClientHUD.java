package net.salju.supernatural.init;

import net.salju.supernatural.events.SupernaturalManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.Minecraft;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class SupernaturalClientHUD {
	public static final ResourceLocation texture = new ResourceLocation("supernatural:textures/gui/generic_icons.png");

	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent(receiveCanceled = true)
	public void onOverlayRender(RenderGuiOverlayEvent.Pre event) {
		Minecraft mc = Minecraft.getInstance();
		GuiGraphics gui = event.getGuiGraphics();
		if (SupernaturalManager.isVampire(mc.player)) {
			if (event.getOverlay().equals(VanillaGuiOverlay.FOOD_LEVEL.type())) {
				event.setCanceled(true);
			}
		}
	}
}