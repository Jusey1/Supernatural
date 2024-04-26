package net.salju.supernatural.init;

import net.salju.supernatural.events.SupernaturalManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.Minecraft;
import java.util.Random;
import com.mojang.blaze3d.systems.RenderSystem;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class SupernaturalClientHUD {
	public static final ResourceLocation texture = new ResourceLocation("supernatural:textures/gui/generic_icons.png");
	public static final Random luck = new Random();

	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent(receiveCanceled = true)
	public void onOverlayRender(RenderGuiOverlayEvent.Pre event) {
		Minecraft mc = Minecraft.getInstance();
		GuiGraphics gui = event.getGuiGraphics();
		if (event.getOverlay().equals(VanillaGuiOverlay.FOOD_LEVEL.type())) {
			if (mc.player.isCreative() || mc.player.isSpectator() || mc.player.isPassenger()) {
				return;
			}
			if (SupernaturalManager.isVampire(mc.player)) {
				event.setCanceled(true);
				RenderSystem.setShaderTexture(0, texture);
				RenderSystem.enableBlend();
				int left = event.getWindow().getGuiScaledWidth() / 2 + 91;
				int top = event.getWindow().getGuiScaledHeight() - ((ForgeGui) (Minecraft.getInstance()).gui).rightHeight;
				((ForgeGui) (Minecraft.getInstance()).gui).rightHeight += 10;
				int level = mc.player.getFoodData().getFoodLevel();
				for (int i = 0; i < 10; i++) {
					int idx = i * 2 + 1;
					int x = left - i * 8 - 9;
					int y = top;
					int icon = 16;
					if (mc.player.hasEffect(MobEffects.HUNGER)) {
						icon += 18;
					}
					if (mc.player.getFoodData().getSaturationLevel() <= 0.0F && mc.gui.getGuiTicks() % (level * 3 + 1) == 0) {
						y = top + luck.nextInt(3) - 1;
					}
					gui.blit(texture, x, y, 0, 0, 9, 9);
					if (idx < level) {
						gui.blit(texture, x, y, icon + -7, 0, 9, 9);
					} else if (idx == level) {
						gui.blit(texture, x, y, icon + 2, 0, 9, 9);
					}
				}
				RenderSystem.disableBlend();
			} else if (SupernaturalManager.isArtificer(mc.player)) {
				event.setCanceled(true);
			}
		}
	}
}