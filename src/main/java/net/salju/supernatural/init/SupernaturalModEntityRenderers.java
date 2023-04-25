
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.salju.supernatural.init;

import net.salju.supernatural.client.renderer.VampireRenderer;
import net.salju.supernatural.client.renderer.SpookyRenderer;
import net.salju.supernatural.client.renderer.PossessedArmorRenderer;
import net.salju.supernatural.client.renderer.NecromancerRenderer;
import net.salju.supernatural.client.renderer.MerEmeraldRenderer;
import net.salju.supernatural.client.renderer.MerDiamondRenderer;
import net.salju.supernatural.client.renderer.MerAmethystRenderer;
import net.salju.supernatural.client.renderer.AngelRenderer;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.api.distmarker.Dist;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class SupernaturalModEntityRenderers {
	@SubscribeEvent
	public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
		event.registerEntityRenderer(SupernaturalModEntities.VAMPIRE.get(), VampireRenderer::new);
		event.registerEntityRenderer(SupernaturalModEntities.NECROMANCER.get(), NecromancerRenderer::new);
		event.registerEntityRenderer(SupernaturalModEntities.POSSESSED_ARMOR.get(), PossessedArmorRenderer::new);
		event.registerEntityRenderer(SupernaturalModEntities.SPOOKY.get(), SpookyRenderer::new);
		event.registerEntityRenderer(SupernaturalModEntities.MER_AMETHYST.get(), MerAmethystRenderer::new);
		event.registerEntityRenderer(SupernaturalModEntities.MER_EMERALD.get(), MerEmeraldRenderer::new);
		event.registerEntityRenderer(SupernaturalModEntities.MER_DIAMOND.get(), MerDiamondRenderer::new);
		event.registerEntityRenderer(SupernaturalModEntities.ANGEL.get(), AngelRenderer::new);
	}
}
