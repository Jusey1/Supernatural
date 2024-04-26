package net.salju.supernatural.events;

import net.salju.supernatural.init.SupernaturalItems;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraft.world.InteractionHand;
import net.minecraft.client.Minecraft;
import com.mojang.math.Axis;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class SupernaturalClientEvents {
	@SubscribeEvent
	public static void onRenderHand(RenderHandEvent event) {
		if (event.getItemStack().is(SupernaturalItems.MAGIC_MIRROR.get()) && Minecraft.getInstance().player.isUsingItem() && event.getItemStack() == Minecraft.getInstance().player.getUseItem()) {
			float r = (event.getHand() == InteractionHand.MAIN_HAND ? 21.0F : -21.0F);
			float t = (event.getHand() == InteractionHand.MAIN_HAND ? -0.185F : 0.185F);
			event.getPoseStack().translate(t, 0.015F, 0.02F);
			event.getPoseStack().mulPose(Axis.XP.rotationDegrees(-3.0F));
			event.getPoseStack().mulPose(Axis.ZP.rotationDegrees(r));
		}
	}
}