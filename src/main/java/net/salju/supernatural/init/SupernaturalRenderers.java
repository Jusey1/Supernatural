package net.salju.supernatural.init;

import net.salju.supernatural.client.renderer.*;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.api.distmarker.Dist;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class SupernaturalRenderers {
	@SubscribeEvent
	public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
		event.registerEntityRenderer(SupernaturalMobs.VAMPIRE.get(), VampireRenderer::new);
		event.registerEntityRenderer(SupernaturalMobs.NECROMANCER.get(), NecromancerRenderer::new);
		event.registerEntityRenderer(SupernaturalMobs.POSSESSED_ARMOR.get(), PossessedArmorRenderer::new);
		event.registerEntityRenderer(SupernaturalMobs.SPOOKY.get(), SpookyRenderer::new);
		event.registerEntityRenderer(SupernaturalMobs.MER_AMETHYST.get(), MerAmethystRenderer::new);
		event.registerEntityRenderer(SupernaturalMobs.MER_EMERALD.get(), MerEmeraldRenderer::new);
		event.registerEntityRenderer(SupernaturalMobs.MER_DIAMOND.get(), MerDiamondRenderer::new);
		event.registerEntityRenderer(SupernaturalMobs.ANGEL.get(), AngelRenderer::new);
		event.registerBlockEntityRenderer(SupernaturalBlockEntities.RITUAL.get(), RitualBlockRenderer::new);
	}
}