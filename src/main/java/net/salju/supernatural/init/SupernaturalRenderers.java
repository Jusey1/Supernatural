package net.salju.supernatural.init;

import net.salju.supernatural.client.renderer.*;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.api.distmarker.Dist;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class SupernaturalRenderers {
	@SubscribeEvent
	public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
		event.registerEntityRenderer(SupernaturalMobs.VAMPIRE.get(), VampireRenderer::new);
		event.registerEntityRenderer(SupernaturalMobs.NECROMANCER.get(), NecromancerRenderer::new);
		event.registerEntityRenderer(SupernaturalMobs.POSSESSED_ARMOR.get(), PossessedArmorRenderer::new);
		event.registerEntityRenderer(SupernaturalMobs.SPOOKY.get(), SpookyRenderer::new);
		event.registerEntityRenderer(SupernaturalMobs.ANGEL.get(), AngelRenderer::new);
		event.registerBlockEntityRenderer(SupernaturalBlockEntities.RITUAL.get(), RitualBlockRenderer::new);
	}
}