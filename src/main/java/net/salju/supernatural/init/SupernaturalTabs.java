package net.salju.supernatural.init;

import net.salju.supernatural.Supernatural;
import net.salju.supernatural.compat.Supplementaries;
import net.salju.supernatural.events.SupernaturalManager;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class SupernaturalTabs {
	public static final DeferredRegister<CreativeModeTab> REGISTRY = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Supernatural.MODID);
	public static final DeferredHolder<CreativeModeTab, CreativeModeTab> SUPERNATURAL = REGISTRY.register("supernatural",
			() -> CreativeModeTab.builder().title(Component.translatable("itemGroup.supernatural")).icon(() -> new ItemStack(SupernaturalItems.SOULGEM.get())).displayItems((parameters, tabData) -> {
                tabData.accept(SupernaturalItems.VAMPIRE_SPAWN_EGG.get());
                tabData.accept(SupernaturalItems.NECROMANCER_SPAWN_EGG.get());
                tabData.accept(SupernaturalItems.ARMOR_SPAWN_EGG.get());
                tabData.accept(SupernaturalItems.SPOOKY_SPAWN_EGG.get());
                tabData.accept(SupernaturalItems.ANGEL_STATUE.get());
                tabData.accept(SupernaturalItems.MERFOLK_SPAWN_EGG.get());
                tabData.accept(SupernaturalItems.WIGHT_SPAWN_EGG.get());
                tabData.accept(SupernaturalItems.REVENANT_SPAWN_EGG.get());
                tabData.accept(SupernaturalItems.SCOURGE_SPAWN_EGG.get());
                tabData.accept(SupernaturalItems.EBONSTEEL_HELMET.get());
                tabData.accept(SupernaturalItems.EBONSTEEL_CHESTPLATE.get());
                tabData.accept(SupernaturalItems.EBONSTEEL_LEGGINGS.get());
                tabData.accept(SupernaturalItems.EBONSTEEL_BOOTS.get());
                tabData.accept(SupernaturalManager.dyeHelmet(SupernaturalItems.GOTHIC_COPPER_HELMET.get()));
                tabData.accept(SupernaturalManager.dyeHelmet(SupernaturalItems.GOTHIC_IRON_HELMET.get()));
                tabData.accept(SupernaturalManager.dyeHelmet(SupernaturalItems.GOTHIC_GOLDEN_HELMET.get()));
                tabData.accept(SupernaturalManager.dyeHelmet(SupernaturalItems.GOTHIC_DIAMOND_HELMET.get()));
                tabData.accept(SupernaturalManager.dyeHelmet(SupernaturalItems.GOTHIC_NETHERITE_HELMET.get()));
                tabData.accept(SupernaturalManager.dyeHelmet(SupernaturalItems.GOTHIC_EBONSTEEL_HELMET.get()));
                tabData.accept(SupernaturalItems.EBONSTEEL_INGOT.get());
                tabData.accept(SupernaturalItems.EBONSTEEL_SCRAP.get());
                tabData.accept(SupernaturalItems.EBONSTEEL_KEY.get());
                tabData.accept(SupernaturalItems.COMPASS.get());
                tabData.accept(SupernaturalItems.SOULGEM.get());
                tabData.accept(SupernaturalItems.REVENANT_CORE.get());
                tabData.accept(SupernaturalItems.RITUAL_BOOK.get());
                tabData.accept(SupernaturalItems.CONTRACT.get());
                tabData.accept(SupernaturalItems.BLOOD.get());
                tabData.accept(SupernaturalItems.VAMPIRE_DUST.get());
                tabData.accept(SupernaturalItems.MERFOLK_SCALE.get());
                tabData.accept(SupernaturalItems.PLASMA.get());
                tabData.accept(SupernaturalItems.EBONSTEEL_BARS.get());
                tabData.accept(SupernaturalItems.EBONSTEEL_DOOR.get());
                tabData.accept(SupernaturalItems.EBONSTEEL_TRAPDOOR.get());
                tabData.accept(SupernaturalItems.EBONSTEEL_CHAIN.get());
                tabData.accept(SupernaturalItems.EBONSTEEL_LANTERN.get());
                if (ModList.get().isLoaded("supplementaries")) {
                    tabData.accept(Supplementaries.getCandleHolder());
                }
                tabData.accept(SupernaturalItems.RITUAL_CANDLE.get());
                tabData.accept(SupernaturalItems.RITUAL_ALTAR.get());
                tabData.accept(SupernaturalItems.TREASURE_SPAWNER.get());
                tabData.accept(SupernaturalItems.TREASURE_VAULT.get());
                tabData.accept(SupernaturalItems.EBONSTEEL_BLOCK.get());
                tabData.accept(SupernaturalItems.GRAVE_SOIL.get());
			}).build());
}
