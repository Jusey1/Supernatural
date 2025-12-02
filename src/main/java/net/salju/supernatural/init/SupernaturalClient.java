package net.salju.supernatural.init;

import net.salju.supernatural.Supernatural;
import net.salju.supernatural.client.model.*;
import net.salju.supernatural.client.renderer.*;
import net.salju.supernatural.compat.Kobolds;
import net.salju.supernatural.item.component.RitualCompassData;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.item.CompassItemPropertyFunction;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class SupernaturalClient {
	public static final ModelLayerLocation ANGEL = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(Supernatural.MODID, "angel"), "main");
	public static final ModelLayerLocation SPIRIT = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(Supernatural.MODID, "spirit"), "main");
	public static final ModelLayerLocation POSSESSED = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(Supernatural.MODID, "possessed"), "main");
    public static final ModelLayerLocation MERFOLK = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(Supernatural.MODID, "merfolk"), "main");
    public static final ModelLayerLocation WIGHT = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(Supernatural.MODID, "wight"), "main");
    public static final ModelLayerLocation WIGHT_CLOTHING = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(Supernatural.MODID, "wight"), "clothing");
	public static final ModelLayerLocation GOTHIC = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(Supernatural.MODID, "gothic"), "main");

	@SubscribeEvent
	public static void onClientSetup(FMLClientSetupEvent event) {
		ItemProperties.register(SupernaturalItems.COMPASS.get(), ResourceLocation.withDefaultNamespace("angle"), new CompassItemPropertyFunction((lvl, stack, target) -> {
			RitualCompassData data = stack.get(SupernaturalData.COMPASS);
			return (data != null ? data.getGlobalPos() : null);
		}));
	}

	@SubscribeEvent
	public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
		event.registerEntityRenderer(SupernaturalMobs.VAMPIRE.get(), VampireRenderer::new);
		event.registerEntityRenderer(SupernaturalMobs.NECROMANCER.get(), NecromancerRenderer::new);
		event.registerEntityRenderer(SupernaturalMobs.POSSESSED_ARMOR.get(), PossessedArmorRenderer::new);
		event.registerEntityRenderer(SupernaturalMobs.SPOOKY.get(), SpookyRenderer::new);
		event.registerEntityRenderer(SupernaturalMobs.MERFOLK_AMETHYST.get(), MerfolkAmethystRenderer::new);
		event.registerEntityRenderer(SupernaturalMobs.MERFOLK_EMERALD.get(), MerfolkEmeraldRenderer::new);
		event.registerEntityRenderer(SupernaturalMobs.MERFOLK_DIAMOND.get(), MerfolkDiamondRenderer::new);
        event.registerEntityRenderer(SupernaturalMobs.WIGHT.get(), WightRenderer::new);
		event.registerEntityRenderer(SupernaturalMobs.ANGEL.get(), AngelRenderer::new);
		event.registerBlockEntityRenderer(SupernaturalBlocks.RITUAL.get(), RitualBlockRenderer::new);
	}

	@SubscribeEvent
	public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
		event.registerLayerDefinition(POSSESSED, PossessedModel::createBodyLayer);
		event.registerLayerDefinition(SPIRIT, SpiritModel::createBodyLayer);
		event.registerLayerDefinition(MERFOLK, MerfolkModel::createBodyLayer);
        event.registerLayerDefinition(WIGHT, WightModel::createBodyLayer);
        event.registerLayerDefinition(WIGHT_CLOTHING, WightModel::createClothingLayer);
		event.registerLayerDefinition(ANGEL, AngelModel::createBodyLayer);
		event.registerLayerDefinition(GOTHIC, GothicArmorModel::createBodyLayer);
		if (ModList.get().isLoaded("kobolds")) {
			Kobolds.registerKoboldArmor(event);
		}
	}

	@SubscribeEvent
	public static void registerClientExtensions(RegisterClientExtensionsEvent event) {
		IClientItemExtensions armor = new IClientItemExtensions() {
			@Override
			public HumanoidModel<?> getHumanoidArmorModel(LivingEntity target, ItemStack stack, EquipmentSlot slot, HumanoidModel<?> basic) {
				HumanoidModel<?> gothic = new GothicArmorModel<>(GothicArmorModel.createBodyLayer().bakeRoot(), stack, slot);
				if (ModList.get().isLoaded("kobolds")) {
					return Kobolds.getKoboldModel(target, stack, slot, gothic);
				}
				return gothic;
			}
		};
		event.registerItem(armor, SupernaturalItems.GOTHIC_DIAMOND_HELMET, SupernaturalItems.GOTHIC_IRON_HELMET, SupernaturalItems.GOTHIC_GOLDEN_HELMET, SupernaturalItems.GOTHIC_NETHERITE_HELMET, SupernaturalItems.GOTHIC_COPPER_HELMET, SupernaturalItems.GOTHIC_EBONSTEEL_HELMET);
	}

	@SubscribeEvent
	public static void registerItemColors(RegisterColorHandlersEvent.Item event) {
		event.register((stack, layer) -> (layer > 0) ? -1 : DyedItemColor.getOrDefault(stack, 0), new ItemLike[]{SupernaturalItems.GOTHIC_IRON_HELMET.get(), SupernaturalItems.GOTHIC_DIAMOND_HELMET.get(), SupernaturalItems.GOTHIC_NETHERITE_HELMET.get(), SupernaturalItems.GOTHIC_GOLDEN_HELMET.get(), SupernaturalItems.GOTHIC_COPPER_HELMET.get(), SupernaturalItems.GOTHIC_EBONSTEEL_HELMET.get()});
	}
}