package net.salju.supernatural.init;

import net.salju.supernatural.Supernatural;
import net.salju.supernatural.client.model.*;
import net.salju.supernatural.client.renderer.*;
import net.salju.supernatural.item.component.RitualCompassData;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.client.ClientHooks;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.item.CompassItemPropertyFunction;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.item.equipment.EquipmentModel;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class SupernaturalClient {
	public static final ModelLayerLocation ANGEL = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(Supernatural.MODID, "angel"), "main");
	public static final ModelLayerLocation SPIRIT = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(Supernatural.MODID, "spirit"), "main");
	public static final ModelLayerLocation POSSESSED = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(Supernatural.MODID, "possessed"), "main");
	public static final ModelLayerLocation GOTHIC = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(Supernatural.MODID, "gothic"), "main");

	@SubscribeEvent
	public static void onClientSetup(FMLClientSetupEvent event) {
		ItemProperties.register(SupernaturalItems.COMPASS.get(), ResourceLocation.withDefaultNamespace("angle"), new CompassItemPropertyFunction((lvl, stack, target) -> {
			RitualCompassData data = stack.get(SupernaturalData.COMPASS);
			return (data != null ? data.getGlobalPos() : null);
		}));
	}

	@SubscribeEvent
	public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
		event.registerLayerDefinition(SPIRIT, SpiritModel::createBodyLayer);
		event.registerLayerDefinition(POSSESSED, PossessedModel::createBodyLayer);
		event.registerLayerDefinition(ANGEL, AngelModel::createBodyLayer);
		event.registerLayerDefinition(GOTHIC, GothicArmorModel::createBodyLayer);
	}

	@SubscribeEvent
	public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
		event.registerEntityRenderer(SupernaturalMobs.VAMPIRE.get(), VampireRenderer::new);
		event.registerEntityRenderer(SupernaturalMobs.NECROMANCER.get(), NecromancerRenderer::new);
		event.registerEntityRenderer(SupernaturalMobs.POSSESSED_ARMOR.get(), PossessedArmorRenderer::new);
		event.registerEntityRenderer(SupernaturalMobs.SPOOKY.get(), SpookyRenderer::new);
		event.registerEntityRenderer(SupernaturalMobs.ANGEL.get(), AngelRenderer::new);
		event.registerBlockEntityRenderer(SupernaturalBlocks.RITUAL.get(), RitualBlockRenderer::new);
	}

	@SubscribeEvent
	public static void registerClientExtensions(RegisterClientExtensionsEvent event) {
		IClientItemExtensions armor = new IClientItemExtensions() {
			@Override
			public Model getHumanoidArmorModel(ItemStack stack, EquipmentModel.LayerType type, Model basic) {
				HumanoidModel<?> gothic = new GothicArmorModel(GothicArmorModel.createBodyLayer().bakeRoot());
				if (basic instanceof HumanoidModel<?> target) {
					ClientHooks.copyModelProperties(target, gothic);
				}
				return gothic;
			}

			@Override
			public int getDefaultDyeColor(ItemStack stack) {
				return DyedItemColor.getOrDefault(stack, DyeColor.WHITE.getTextColor());
			}
		};
		event.registerItem(armor, SupernaturalItems.GOTHIC_DIAMOND_HELMET, SupernaturalItems.GOTHIC_IRON_HELMET, SupernaturalItems.GOTHIC_GOLDEN_HELMET, SupernaturalItems.GOTHIC_NETHERITE_HELMET);
	}

	@SubscribeEvent
	public static void registerItemColors(RegisterColorHandlersEvent.Item event) {
		event.register((stack, layer) -> (layer > 0) ? -1 : DyedItemColor.getOrDefault(stack, DyeColor.WHITE.getTextColor()), new ItemLike[]{
				SupernaturalItems.GOTHIC_IRON_HELMET.get(), SupernaturalItems.GOTHIC_DIAMOND_HELMET.get(),
				SupernaturalItems.GOTHIC_NETHERITE_HELMET.get(), SupernaturalItems.GOTHIC_GOLDEN_HELMET.get()});
	}
}