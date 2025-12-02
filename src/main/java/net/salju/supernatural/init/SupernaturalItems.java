package net.salju.supernatural.init;

import net.minecraft.sounds.SoundEvents;
import net.salju.supernatural.Supernatural;
import net.salju.supernatural.item.*;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.component.*;
import net.minecraft.world.item.equipment.*;
import net.minecraft.world.item.*;

public class SupernaturalItems {
	public static final Consumable BLOODY = Consumables.defaultDrink().consumeSeconds(2.0F).sound(SoundEvents.HONEY_DRINK).build();

	public static final DeferredRegister<Item> REGISTRY = DeferredRegister.createItems(Supernatural.MODID);
	public static final DeferredHolder<Item, Item> VAMPIRE_SPAWN_EGG = REGISTRY.register("vampire_spawn_egg", () -> new SpawnEggItem(createBaseProps("vampire_spawn_egg").spawnEgg(SupernaturalMobs.VAMPIRE.get())));
	public static final DeferredHolder<Item, Item> NECROMANCER_SPAWN_EGG = REGISTRY.register("necromancer_spawn_egg", () -> new SpawnEggItem(createBaseProps("necromancer_spawn_egg").spawnEgg(SupernaturalMobs.NECROMANCER.get())));
	public static final DeferredHolder<Item, Item> ARMOR_SPAWN_EGG = REGISTRY.register("armor_spawn_egg", () -> new SpawnEggItem(createBaseProps("armor_spawn_egg").spawnEgg(SupernaturalMobs.POSSESSED_ARMOR.get())));
    public static final DeferredHolder<Item, Item> SPOOKY_SPAWN_EGG = REGISTRY.register("spooky_spawn_egg", () -> new SpawnEggItem(createBaseProps("spooky_spawn_egg").spawnEgg(SupernaturalMobs.SPOOKY.get())));
    public static final DeferredHolder<Item, Item> MERFOLK_SPAWN_EGG = REGISTRY.register("merfolk_spawn_egg", () -> new SpawnEggItem(createBaseProps("merfolk_spawn_egg").spawnEgg(SupernaturalMobs.MERFOLK_AMETHYST.get())));
    public static final DeferredHolder<Item, Item> ANGEL_STATUE = REGISTRY.register("angel_statue", () -> new AngelItem(createBaseProps("angel_statue").stacksTo(1)));
    public static final DeferredHolder<Item, Item> RITUAL_ALTAR = block(SupernaturalBlocks.RITUAL_ALTAR, "ritual_altar");
    public static final DeferredHolder<Item, Item> GRAVE_SOIL = block(SupernaturalBlocks.GRAVE_SOIL, "grave_soil");
    public static final DeferredHolder<Item, Item> EBONSTEEL_HELMET = REGISTRY.register("ebonsteel_helmet", () -> new Item(createBaseProps("ebonsteel_helmet").durability(ArmorType.HELMET.getDurability(21)).humanoidArmor(SupernaturalArmors.EBONSTEEL, ArmorType.HELMET).rarity(Rarity.RARE)));
    public static final DeferredHolder<Item, Item> EBONSTEEL_CHESTPLATE = REGISTRY.register("ebonsteel_chestplate", () -> new Item(createBaseProps("ebonsteel_chestplate").durability(ArmorType.CHESTPLATE.getDurability(21)).humanoidArmor(SupernaturalArmors.EBONSTEEL, ArmorType.CHESTPLATE).rarity(Rarity.RARE)));
    public static final DeferredHolder<Item, Item> EBONSTEEL_LEGGINGS = REGISTRY.register("ebonsteel_leggings", () -> new Item(createBaseProps("ebonsteel_leggings").durability(ArmorType.LEGGINGS.getDurability(21)).humanoidArmor(SupernaturalArmors.EBONSTEEL, ArmorType.LEGGINGS).rarity(Rarity.RARE)));
    public static final DeferredHolder<Item, Item> EBONSTEEL_BOOTS = REGISTRY.register("ebonsteel_boots", () -> new Item(createBaseProps("ebonsteel_boots").durability(ArmorType.BOOTS.getDurability(21)).humanoidArmor(SupernaturalArmors.EBONSTEEL, ArmorType.BOOTS).rarity(Rarity.RARE)));
    public static final DeferredHolder<Item, Item> GOTHIC_COPPER_HELMET = REGISTRY.register("gothic_copper_helmet", () -> new Item(createBaseProps("gothic_copper_helmet").durability(ArmorType.HELMET.getDurability(11)).humanoidArmor(SupernaturalArmors.COPPER, ArmorType.HELMET)));
    public static final DeferredHolder<Item, Item> GOTHIC_IRON_HELMET = REGISTRY.register("gothic_iron_helmet", () -> new Item(createBaseProps("gothic_iron_helmet").durability(ArmorType.HELMET.getDurability(15)).humanoidArmor(SupernaturalArmors.IRON, ArmorType.HELMET)));
	public static final DeferredHolder<Item, Item> GOTHIC_DIAMOND_HELMET = REGISTRY.register("gothic_diamond_helmet", () -> new Item(createBaseProps("gothic_diamond_helmet").durability(ArmorType.HELMET.getDurability(33)).humanoidArmor(SupernaturalArmors.DIAMOND, ArmorType.HELMET)));
	public static final DeferredHolder<Item, Item> GOTHIC_GOLDEN_HELMET = REGISTRY.register("gothic_golden_helmet", () -> new Item(createBaseProps("gothic_golden_helmet").durability(ArmorType.HELMET.getDurability(7)).humanoidArmor(SupernaturalArmors.GOLDEN, ArmorType.HELMET)));
    public static final DeferredHolder<Item, Item> GOTHIC_NETHERITE_HELMET = REGISTRY.register("gothic_netherite_helmet", () -> new Item(createBaseProps("gothic_netherite_helmet").fireResistant().durability(ArmorType.HELMET.getDurability(37)).humanoidArmor(SupernaturalArmors.NETHERITE, ArmorType.HELMET)));
    public static final DeferredHolder<Item, Item> GOTHIC_EBONSTEEL_HELMET = REGISTRY.register("gothic_ebonsteel_helmet", () -> new Item(createBaseProps("gothic_ebonsteel_helmet").durability(ArmorType.HELMET.getDurability(21)).humanoidArmor(SupernaturalArmors.EBONSTEEL_GOTHIC, ArmorType.HELMET).rarity(Rarity.RARE)));
	public static final DeferredHolder<Item, Item> RITUAL_BOOK = REGISTRY.register("ritual_book", () -> new RitualBookItem(createBaseProps("ritual_book").stacksTo(1).rarity(Rarity.UNCOMMON)));
    public static final DeferredHolder<Item, Item> CONTRACT = REGISTRY.register("contract", () -> new ContractItem(createBaseProps("contract").stacksTo(1).rarity(Rarity.UNCOMMON)));
	public static final DeferredHolder<Item, Item> SOULGEM = REGISTRY.register("soulgem", () -> new SoulgemItem(createBaseProps("soulgem").stacksTo(1).rarity(Rarity.UNCOMMON)));
	public static final DeferredHolder<Item, Item> COMPASS = REGISTRY.register("ritual_compass", () -> new RitualCompassItem(createBaseProps("ritual_compass").stacksTo(1).rarity(Rarity.UNCOMMON)));
	public static final DeferredHolder<Item, Item> CORE_DARKNESS = REGISTRY.register("core_darkness", () -> new CoreDarknessItem(createBaseProps("core_darkness").stacksTo(1).rarity(Rarity.UNCOMMON)));
    public static final DeferredHolder<Item, Item> VAMPIRE_DUST = REGISTRY.register("vampire_dust", () -> new Item(createBaseProps("vampire_dust")));
	public static final DeferredHolder<Item, Item> BLOOD = REGISTRY.register("blood_bottle", () -> new BloodItem(createBaseProps("blood_bottle").stacksTo(16).component(DataComponents.CONSUMABLE, BLOODY).craftRemainder(Items.GLASS_BOTTLE).usingConvertsTo(Items.GLASS_BOTTLE)));

	private static DeferredHolder<Item, Item> block(DeferredHolder<Block, Block> block, String name) {
		return REGISTRY.register(block.getId().getPath(), () -> new BlockItem(block.get(), createBaseProps(name)));
	}

	public static Item.Properties createBaseProps(String name) {
		return new Item.Properties().setId(ResourceKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(Supernatural.MODID, name)));
	}
}