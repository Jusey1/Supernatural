package net.salju.supernatural.init;

import net.salju.supernatural.Supernatural;
import net.salju.supernatural.client.compass.RitualCompassAngle;
import net.salju.supernatural.client.model.*;
import net.salju.supernatural.client.renderer.*;
import net.salju.supernatural.client.screens.*;
import net.salju.supernatural.compat.Kobolds;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.client.ClientHooks;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.fml.ModList;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.ArmorModelSet;
import net.minecraft.client.resources.model.EquipmentClientInfo;
import net.minecraft.client.particle.FlameParticle;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;

@EventBusSubscriber(value = Dist.CLIENT)
public class SupernaturalClient {
    public static final ModelLayerLocation ANGEL = registerModel("angel", "main");
    public static final ModelLayerLocation MERFOLK = registerModel("merfolk", "main");
    public static final ModelLayerLocation THRALL = registerModel("thrall", "main");
    public static final ModelLayerLocation WIGHT = registerModel("wight", "main");
    public static final ModelLayerLocation SCOURGE = registerModel("scourge", "main");
    public static final ModelLayerLocation SCOURGE_ARMOR = registerModel("scourge", "armor");
	public static final ArmorModelSet<ModelLayerLocation> GOTHIC = registerArmorSet("gothic");

	@SubscribeEvent
	public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
		event.registerEntityRenderer(SupernaturalMobs.VAMPIRE.get(), VampireRenderer::new);
        event.registerEntityRenderer(SupernaturalMobs.ANGEL.get(), AngelRenderer::new);
        event.registerEntityRenderer(SupernaturalMobs.MERFOLK.get(), MerfolkRenderer::new);
        event.registerEntityRenderer(SupernaturalMobs.THRALL.get(), ThrallRenderer::new);
        event.registerEntityRenderer(SupernaturalMobs.WIGHT.get(), WightRenderer::new);
        event.registerEntityRenderer(SupernaturalMobs.SCOURGE.get(), ScourgeRenderer::new);
        event.registerBlockEntityRenderer(SupernaturalBlocks.RITUAL.get(), RitualBlockRenderer::new);
        event.registerBlockEntityRenderer(SupernaturalBlocks.WEAPON.get(), WeaponDisplayRenderer::new);
        event.registerBlockEntityRenderer(SupernaturalBlocks.RP.get(), TreasureSpawnerRenderer::new);
        event.registerBlockEntityRenderer(SupernaturalBlocks.RV.get(), TreasureVaultRenderer::new);
	}

    @SubscribeEvent
    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(ANGEL, AngelModel::createBodyLayer);
        event.registerLayerDefinition(MERFOLK, MerfolkModel::createBodyLayer);
        event.registerLayerDefinition(THRALL, ThrallModel::createBodyLayer);
        event.registerLayerDefinition(WIGHT, WightModel::createBodyLayer);
        event.registerLayerDefinition(SCOURGE, ScourgeModel::createBodyLayer);
        event.registerLayerDefinition(SCOURGE_ARMOR, ScourgeModel::createBodyLayer);
        event.registerLayerDefinition(GOTHIC.head(), GothicArmorModel::createHeadLayer);
        event.registerLayerDefinition(GOTHIC.chest(), GothicArmorModel::createBodyLayer);
        event.registerLayerDefinition(GOTHIC.legs(), GothicArmorModel::createLegsLayer);
        event.registerLayerDefinition(GOTHIC.feet(), GothicArmorModel::createBootsLayer);
        if (ModList.get().isLoaded("kobolds")) {
            Kobolds.registerKoboldArmor(event);
        }
    }

    @SubscribeEvent
    public static void registerMenuScreens(RegisterMenuScreensEvent event) {
        event.register(SupernaturalRecipes.RITUAL_BOOK.get(), RitualBookScreen::new);
    }

    @SubscribeEvent
    public static void registerParticles(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(SupernaturalCandle.FLAME.get(), FlameParticle.SmallFlameProvider::new);
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
		event.registerItem(armor, SupernaturalItems.GOTHIC_DIAMOND_HELMET, SupernaturalItems.GOTHIC_IRON_HELMET, SupernaturalItems.GOTHIC_GOLDEN_HELMET, SupernaturalItems.GOTHIC_NETHERITE_HELMET, SupernaturalItems.GOTHIC_COPPER_HELMET, SupernaturalItems.GOTHIC_EBONSTEEL_HELMET);
	}

	@SubscribeEvent
	public static void registerNumProps(RegisterRangeSelectItemModelPropertyEvent event) {
		event.register(Identifier.fromNamespaceAndPath(Supernatural.MODID, "angle"), RitualCompassAngle.MAP_CODEC);
	}

    public static ArmorModelSet<ModelLayerLocation> registerArmorSet(String path) {
        return new ArmorModelSet<>(registerModel(path, "helmet"), registerModel(path, "chestplate"), registerModel(path, "leggings"), registerModel(path, "boots"));
    }

    public static ModelLayerLocation registerModel(String path, String model) {
        return new ModelLayerLocation(Identifier.fromNamespaceAndPath(Supernatural.MODID, path), model);
    }
}