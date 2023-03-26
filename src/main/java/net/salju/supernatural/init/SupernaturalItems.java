package net.salju.supernatural.init;

import net.salju.supernatural.item.*;
import net.salju.supernatural.SupernaturalMod;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.common.ForgeSpawnEggItem;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.entity.EquipmentSlot;

public class SupernaturalItems {
	public static final DeferredRegister<Item> REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, SupernaturalMod.MODID);
	public static final RegistryObject<Item> VAMPIRE_SPAWN_EGG = REGISTRY.register("vampire_spawn_egg", () -> new ForgeSpawnEggItem(SupernaturalModEntities.VAMPIRE, -6710887, -3407872, new Item.Properties().tab(SupernaturalTabs.SUPERNATURAL_TAB)));
	public static final RegistryObject<Item> NECROMANCER_SPAWN_EGG = REGISTRY.register("necromancer_spawn_egg", () -> new ForgeSpawnEggItem(SupernaturalModEntities.NECROMANCER, -6710887, -13382401, new Item.Properties().tab(SupernaturalTabs.SUPERNATURAL_TAB)));
	public static final RegistryObject<Item> POSSESSED_ARMOR_SPAWN_EGG = REGISTRY.register("possessed_armor_spawn_egg", () -> new ForgeSpawnEggItem(SupernaturalModEntities.POSSESSED_ARMOR, -10066330, -3355444, new Item.Properties().tab(SupernaturalTabs.SUPERNATURAL_TAB)));
	public static final RegistryObject<Item> SPOOKY_SPAWN_EGG = REGISTRY.register("spooky_spawn_egg", () -> new ForgeSpawnEggItem(SupernaturalModEntities.SPOOKY, -6697729, -3342337, new Item.Properties().tab(SupernaturalTabs.SUPERNATURAL_TAB)));
	public static final RegistryObject<Item> MER_AMETHYST_SPAWN_EGG = REGISTRY.register("mer_amethyst_spawn_egg", () -> new ForgeSpawnEggItem(SupernaturalModEntities.MER_AMETHYST, -16737946, -13369396, new Item.Properties().tab(SupernaturalTabs.SUPERNATURAL_TAB)));
	public static final RegistryObject<Item> GOTHIC_IRON_HELMET = REGISTRY.register("gothic_iron_helmet", () -> new GothicHelmetItem(ArmorMaterials.IRON, EquipmentSlot.HEAD, new Item.Properties().tab(SupernaturalTabs.SUPERNATURAL_TAB)));
	public static final RegistryObject<Item> GOTHIC_DIAMOND_HELMET = REGISTRY.register("gothic_diamond_helmet", () -> new GothicHelmetItem(ArmorMaterials.DIAMOND, EquipmentSlot.HEAD, new Item.Properties().tab(SupernaturalTabs.SUPERNATURAL_TAB)));
	public static final RegistryObject<Item> GOTHIC_GOLDEN_HELMET = REGISTRY.register("gothic_golden_helmet", () -> new GothicHelmetItem(ArmorMaterials.GOLD, EquipmentSlot.HEAD, new Item.Properties().tab(SupernaturalTabs.SUPERNATURAL_TAB)));
	public static final RegistryObject<Item> GOTHIC_NETHERITE_HELMET = REGISTRY.register("gothic_netherite_helmet", () -> new GothicHelmetItem(ArmorMaterials.NETHERITE, EquipmentSlot.HEAD, new Item.Properties().tab(SupernaturalTabs.SUPERNATURAL_TAB)));
	public static final RegistryObject<Item> GRAVE_SOIL = block(SupernaturalBlocks.GRAVE_SOIL, SupernaturalTabs.SUPERNATURAL_TAB);
	public static final RegistryObject<Item> GOTHIC_TEMPLATE = REGISTRY.register("gothic_template", () -> new Item(new Item.Properties().tab(SupernaturalTabs.SUPERNATURAL_TAB).stacksTo(64).rarity(Rarity.COMMON)));
	public static final RegistryObject<Item> NECRO_TEMPLATE = REGISTRY.register("necro_template", () -> new Item(new Item.Properties().tab(SupernaturalTabs.SUPERNATURAL_TAB).stacksTo(64).rarity(Rarity.COMMON)));
	public static final RegistryObject<Item> ANIMAL_BLOOD = REGISTRY.register("animal_blood", () -> new BloodItem(new Item.Properties().tab(SupernaturalTabs.SUPERNATURAL_TAB).stacksTo(16).rarity(Rarity.COMMON).food((new FoodProperties.Builder()).nutrition(4).saturationMod(0.0f).build())));
	public static final RegistryObject<Item> VILLAGER_BLOOD = REGISTRY.register("villager_blood", () -> new BloodItem(new Item.Properties().tab(SupernaturalTabs.SUPERNATURAL_TAB).stacksTo(16).rarity(Rarity.COMMON).food((new FoodProperties.Builder()).nutrition(8).saturationMod(0.0f).build())));
	public static final RegistryObject<Item> PLAYER_BLOOD = REGISTRY.register("player_blood", () -> new BloodItem(new Item.Properties().tab(SupernaturalTabs.SUPERNATURAL_TAB).stacksTo(16).rarity(Rarity.COMMON).food((new FoodProperties.Builder()).nutrition(16).saturationMod(0.0f).build())));
	public static final RegistryObject<Item> ECTOPLASM = REGISTRY.register("ectoplasm", () -> new Item(new Item.Properties().tab(SupernaturalTabs.SUPERNATURAL_TAB).stacksTo(64).rarity(Rarity.COMMON)));
	public static final RegistryObject<Item> VAMPIRE_DUST = REGISTRY.register("vampire_dust", () -> new Item(new Item.Properties().tab(SupernaturalTabs.SUPERNATURAL_TAB).stacksTo(64).rarity(Rarity.COMMON)));

	private static RegistryObject<Item> block(RegistryObject<Block> block, CreativeModeTab tab) {
		return REGISTRY.register(block.getId().getPath(), () -> new BlockItem(block.get(), new Item.Properties().tab(tab)));
	}
}