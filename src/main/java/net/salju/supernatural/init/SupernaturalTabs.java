package net.salju.supernatural.init;

import net.salju.supernatural.SupernaturalMod;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;

import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.network.chat.Component;
import net.minecraft.core.registries.Registries;

public class SupernaturalTabs {
	public static final DeferredRegister<CreativeModeTab> REGISTRY = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, SupernaturalMod.MODID);
	public static final RegistryObject<CreativeModeTab> SUPERNATURAL = REGISTRY.register("supernatural",
			() -> CreativeModeTab.builder().title(Component.translatable("itemGroup.supernatural")).icon(() -> new ItemStack(Items.ENDER_EYE)).displayItems((parameters, tabData) -> {
				tabData.accept(SupernaturalItems.VAMPIRE_SPAWN_EGG.get());
				tabData.accept(SupernaturalItems.NECROMANCER_SPAWN_EGG.get());
				tabData.accept(SupernaturalItems.POSSESSED_ARMOR_SPAWN_EGG.get());
				tabData.accept(SupernaturalItems.SPOOKY_SPAWN_EGG.get());
				tabData.accept(SupernaturalItems.MER_AMETHYST_SPAWN_EGG.get());
				tabData.accept(SupernaturalItems.GOTHIC_IRON_HELMET.get());
				tabData.accept(SupernaturalItems.GOTHIC_DIAMOND_HELMET.get());
				tabData.accept(SupernaturalItems.GOTHIC_GOLDEN_HELMET.get());
				tabData.accept(SupernaturalItems.GOTHIC_NETHERITE_HELMET.get());
				tabData.accept(SupernaturalBlocks.GRAVE_SOIL.get().asItem());
				tabData.accept(SupernaturalItems.GOTHIC_TEMPLATE.get());
				tabData.accept(SupernaturalItems.NECRO_TEMPLATE.get());
				tabData.accept(SupernaturalItems.ANIMAL_BLOOD.get());
				tabData.accept(SupernaturalItems.VILLAGER_BLOOD.get());
				tabData.accept(SupernaturalItems.PLAYER_BLOOD.get());
				tabData.accept(SupernaturalItems.ECTOPLASM.get());
				tabData.accept(SupernaturalItems.VAMPIRE_DUST.get());
				tabData.accept(SupernaturalItems.ANGEL_STATUE.get());
			}).build());
}