package net.salju.supernatural.init;

import net.salju.supernatural.procedures.SupernaturalHelpersProcedure;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.api.distmarker.Dist;

import net.minecraft.world.food.FoodData;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.Minecraft;

import java.util.Random;

import com.mojang.blaze3d.systems.RenderSystem;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class SupernaturalBloodBar {
	public static final ResourceLocation ICONS_LOCATION = new ResourceLocation("supernatural:textures/gui/generic_icons.png");
	public static final Random luck = new Random();

	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent(receiveCanceled = true)
	public void onOverlayRender(RenderGuiOverlayEvent.Pre event) {
		Minecraft mc = Minecraft.getInstance();

		if (event.getOverlay().equals(VanillaGuiOverlay.FOOD_LEVEL.type()) && SupernaturalHelpersProcedure.isVampire(mc.player)) {
			if (mc.player.isCreative() || mc.player.isSpectator()) {
				return;
			}
			event.setCanceled(true);
			RenderSystem.setShaderTexture(0, ICONS_LOCATION);
			int width = event.getWindow().getGuiScaledWidth();
			int height = event.getWindow().getGuiScaledHeight();
			LocalPlayer localPlayer = mc.player;
			RenderSystem.enableBlend();
			int left = width / 2 + 91;
			int top = height - ((ForgeGui) (Minecraft.getInstance()).gui).rightHeight;
			((ForgeGui) (Minecraft.getInstance()).gui).rightHeight += 10;
			boolean unused = false;
			FoodData stats = mc.player.getFoodData();
			int level = stats.getFoodLevel();
			int barPx = 8;
			int barNum = 10;
			for (int i = 0; i < barNum; i++) {
				int idx = i * 2 + 1;
				int x = left - i * barPx - 9;
				int y = top;
				int icon = 16;
				byte background = 0;
				if (mc.player.hasEffect(MobEffects.HUNGER)) {
					icon += 18;
					background = 0;
				}
				if (unused) {
					background = 1;
				}
				if (localPlayer.getFoodData().getSaturationLevel() <= 0.0F && mc.gui.getGuiTicks() % (level * 3 + 1) == 0) {
					y = top + luck.nextInt(3) - 1;
				}
				mc.gui.blit(event.getPoseStack(), x, y, 0, 0, 9, 9);
				if (idx < level) {
					mc.gui.blit(event.getPoseStack(), x, y, icon + -7, 0, 9, 9);
				} else if (idx == level) {
					mc.gui.blit(event.getPoseStack(), x, y, icon + 2, 0, 9, 9);
				}
			}
			RenderSystem.disableBlend();
			RenderSystem.setShaderTexture(0, GuiComponent.GUI_ICONS_LOCATION);
		}
	}
}