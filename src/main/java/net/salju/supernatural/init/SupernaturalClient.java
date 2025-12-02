package net.salju.supernatural.init;

import net.salju.supernatural.Supernatural;
import net.salju.supernatural.client.compass.RitualCompassAngle;
import net.salju.supernatural.client.model.*;
import net.salju.supernatural.client.renderer.*;
import net.salju.supernatural.compat.Kobolds;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterRangeSelectItemModelPropertyEvent;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.client.ClientHooks;
import net.neoforged.fml.ModList;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.ArmorModelSet;
import net.minecraft.client.resources.model.EquipmentClientInfo;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

@EventBusSubscriber(value = Dist.CLIENT)
public class SupernaturalClient {
    public static final ModelLayerLocation POSSESSED = registerModel("possessed", "main");
    public static final ModelLayerLocation SPIRIT = registerModel("spirit", "main");
    public static final ModelLayerLocation MERFOLK = registerModel("merfolk", "main");
    public static final ModelLayerLocation ANGEL = registerModel("angel", "main");
	public static final ArmorModelSet<ModelLayerLocation> GOTHIC = registerArmorSet("gothic");

	@SubscribeEvent
	public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
		event.registerLayerDefinition(POSSESSED, PossessedModel::createBodyLayer);
        event.registerLayerDefinition(SPIRIT, SpiritModel::createBodyLayer);
        event.registerLayerDefinition(MERFOLK, MerfolkModel::createBodyLayer);
		event.registerLayerDefinition(ANGEL, AngelModel::createBodyLayer);
        event.registerLayerDefinition(GOTHIC.head(), GothicArmorModel::createHeadLayer);
        event.registerLayerDefinition(GOTHIC.chest(), GothicArmorModel::createBodyLayer);
        event.registerLayerDefinition(GOTHIC.legs(), GothicArmorModel::createLegsLayer);
        event.registerLayerDefinition(GOTHIC.feet(), GothicArmorModel::createBootsLayer);
		if (ModList.get().isLoaded("kobolds")) {
			Kobolds.registerKoboldArmor(event);
		}
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
		event.registerEntityRenderer(SupernaturalMobs.ANGEL.get(), AngelRenderer::new);
		event.registerBlockEntityRenderer(SupernaturalBlocks.RITUAL.get(), RitualBlockRenderer::new);
	}

	@SubscribeEvent
	public static void registerClientExtensions(RegisterClientExtensionsEvent event) {
		IClientItemExtensions armor = new IClientItemExtensions() {
			@Override
			public Model getHumanoidArmorModel(ItemStack stack, EquipmentClientInfo.LayerType type, Model basic) {
				HumanoidModel<?> gothic = new GothicArmorModel(GothicArmorModel.createHeadLayer().bakeRoot());
				if (ModList.get().isLoaded("kobolds")) {
					gothic = Kobolds.getKoboldModel(basic, gothic);
				}
				if (basic instanceof HumanoidModel<?> target) {
					ClientHooks.copyModelProperties(target, gothic);
				}
				return gothic;
			}
		};
		event.registerItem(armor, SupernaturalItems.GOTHIC_DIAMOND_HELMET, SupernaturalItems.GOTHIC_IRON_HELMET, SupernaturalItems.GOTHIC_GOLDEN_HELMET, SupernaturalItems.GOTHIC_NETHERITE_HELMET, SupernaturalItems.GOTHIC_COPPER_HELMET);
	}

	@SubscribeEvent
	public static void registerNumProps(RegisterRangeSelectItemModelPropertyEvent event) {
		event.register(ResourceLocation.fromNamespaceAndPath(Supernatural.MODID, "angle"), RitualCompassAngle.MAP_CODEC);
	}

    public static ArmorModelSet<ModelLayerLocation> registerArmorSet(String path) {
        return new ArmorModelSet(registerModel(path, "helmet"), registerModel(path, "chestplate"), registerModel(path, "leggings"), registerModel(path, "boots"));
    }

    public static ModelLayerLocation registerModel(String path, String model) {
        return new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(Supernatural.MODID, path), model);
    }
}