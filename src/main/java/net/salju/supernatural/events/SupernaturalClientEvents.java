package net.salju.supernatural.events;

import net.salju.supernatural.init.SupernaturalData;
import net.salju.supernatural.init.SupernaturalEffects;
import net.salju.supernatural.init.SupernaturalItems;
import net.salju.supernatural.item.component.RitualBookData;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ScreenEvent;
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import net.minecraft.client.Minecraft;
import net.minecraft.client.ScrollWheelHandler;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.joml.Vector2i;

@EventBusSubscriber(value = Dist.CLIENT)
public class SupernaturalClientEvents {
	@SubscribeEvent
	public static void onWheel(ScreenEvent.MouseScrolled.Post event) {
		if (event.getScreen() instanceof AbstractContainerScreen<?> screen && screen.getSlotUnderMouse() != null) {
			ItemStack stack = screen.getSlotUnderMouse().getItem();
			if (screen.getSlotUnderMouse().hasItem()) {
				RitualBookData data = stack.get(SupernaturalData.BOOK);
				if (data != null) {
					Vector2i v = new ScrollWheelHandler().onMouseScroll(event.getScrollDeltaX(), event.getScrollDeltaY());
					int w = v.y == 0 ? -v.x : v.y;
					if (w != 0) {
						int c = data.getPage();
						int e = ScrollWheelHandler.getNextScrollWheelSelection(w, c, data.getMaxRituals());
						if (c != e) {
							stack.set(SupernaturalData.BOOK, new RitualBookData(e));
						}
					}
				} else if (stack.is(SupernaturalItems.RITUAL_BOOK)) {
					stack.set(SupernaturalData.BOOK, RitualBookData.EMPTY);
				}
			}
		}
	}

	@SubscribeEvent(receiveCanceled = true)
	public static void onGui(RenderGuiLayerEvent.Pre event) {
		Player player = Minecraft.getInstance().player;
		if (event.getName().equals(VanillaGuiLayers.FOOD_LEVEL) && player != null) {
			if (player.hasEffect(SupernaturalEffects.VAMPIRISM) && player.getEffect(SupernaturalEffects.VAMPIRISM).getAmplifier() >= 1) {
				event.setCanceled(true);
			}
		}
	}
}