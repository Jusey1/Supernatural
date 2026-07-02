package net.salju.supernatural.events;

import net.salju.supernatural.item.SpectralMirrorItem;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent;
import net.neoforged.neoforge.client.event.RenderHandEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.InteractionHand;
import com.mojang.math.Axis;

@EventBusSubscriber(value = Dist.CLIENT)
public class SupernaturalClientEvents {
    @SubscribeEvent
    public static void onRenderHand(RenderHandEvent event) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) {
            if (player.isUsingItem() && event.getItemStack().is(player.getUseItem().getItem())) {
                boolean check = event.getHand().equals(InteractionHand.MAIN_HAND);
                if (event.getItemStack().getItem() instanceof SpectralMirrorItem) {
                    event.getPoseStack().translate(check ? 0.245F : -0.245F, 0.035F, 0.14F);
                    event.getPoseStack().mulPose(Axis.YP.rotationDegrees(check ? 32.0F : -32.0F));
                    event.getPoseStack().mulPose(Axis.ZP.rotationDegrees(check ? 4.0F : -4.0F));
                }
            }
        }
    }

	@SubscribeEvent(receiveCanceled = true)
	public static void onGui(RenderGuiLayerEvent.Pre event) {
		Player player = Minecraft.getInstance().player;
		if (player != null && SupernaturalManager.hasVampirism(player, 1)) {
			if (event.getName().equals(VanillaGuiLayers.FOOD_LEVEL)) {
				event.setCanceled(true);
			}
		}
	}
}