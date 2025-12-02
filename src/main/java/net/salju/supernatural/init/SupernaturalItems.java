package net.salju.supernatural.init;

import net.salju.supernatural.Supernatural;
import net.salju.supernatural.item.*;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.*;

public class SupernaturalItems {
	public static final DeferredRegister<Item> REGISTRY = DeferredRegister.createItems(Supernatural.MODID);
	public static final DeferredHolder<Item, Item> VAMPIRE_SPAWN_EGG = REGISTRY.register("vampire_spawn_egg", () -> new SpawnEggItem(SupernaturalMobs.VAMPIRE.get(), -6710887, -3407872, new Item.Properties()));
	public static final DeferredHolder<Item, Item> NECROMANCER_SPAWN_EGG = REGISTRY.register("necromancer_spawn_egg", () -> new SpawnEggItem(SupernaturalMobs.NECROMANCER.get(), -6710887, -13382401, new Item.Properties()));
	public static final DeferredHolder<Item, Item> ARMOR_SPAWN_EGG = REGISTRY.register("armor_spawn_egg", () -> new SpawnEggItem(SupernaturalMobs.POSSESSED_ARMOR.get(), -10066330, -3355444, new Item.Properties()));
	public static final DeferredHolder<Item, Item> SPOOKY_SPAWN_EGG = REGISTRY.register("spooky_spawn_egg", () -> new SpawnEggItem(SupernaturalMobs.SPOOKY.get(), -6697729, -3342337, new Item.Properties()));
	public static final DeferredHolder<Item, Item> MERFOLK_SPAWN_EGG = REGISTRY.register("merfolk_spawn_egg", () -> new SpawnEggItem(SupernaturalMobs.MERFOLK_AMETHYST.get(), -16737946, -13369396, new Item.Properties()));
	public static final DeferredHolder<Item, Item> WIGHT_SPAWN_EGG = REGISTRY.register("wight_spawn_egg", () -> new SpawnEggItem(SupernaturalMobs.WIGHT.get(), 7967402, 11914464, new Item.Properties()));
	public static final DeferredHolder<Item, Item> ANGEL_STATUE = REGISTRY.register("angel_statue", () -> new AngelItem(new Item.Properties().stacksTo(1)));
	public static final DeferredHolder<Item, Item> RITUAL_ALTAR = block(SupernaturalBlocks.RITUAL_ALTAR);
	public static final DeferredHolder<Item, Item> GRAVE_SOIL = block(SupernaturalBlocks.GRAVE_SOIL);
	public static final DeferredHolder<Item, Item> EBONSTEEL_HELMET = REGISTRY.register("ebonsteel_helmet", () -> new ArmorItem(SupernaturalArmors.EBONSTEEL, ArmorItem.Type.HELMET, new Item.Properties().durability(ArmorItem.Type.HELMET.getDurability(21)).rarity(Rarity.RARE)));
	public static final DeferredHolder<Item, Item> EBONSTEEL_CHESTPLATE = REGISTRY.register("ebonsteel_chestplate", () -> new ArmorItem(SupernaturalArmors.EBONSTEEL, ArmorItem.Type.CHESTPLATE, new Item.Properties().durability(ArmorItem.Type.CHESTPLATE.getDurability(21)).rarity(Rarity.RARE)));
	public static final DeferredHolder<Item, Item> EBONSTEEL_LEGGINGS = REGISTRY.register("ebonsteel_leggings", () -> new ArmorItem(SupernaturalArmors.EBONSTEEL, ArmorItem.Type.LEGGINGS, new Item.Properties().durability(ArmorItem.Type.LEGGINGS.getDurability(21)).rarity(Rarity.RARE)));
	public static final DeferredHolder<Item, Item> EBONSTEEL_BOOTS = REGISTRY.register("ebonsteel_boots", () -> new ArmorItem(SupernaturalArmors.EBONSTEEL, ArmorItem.Type.BOOTS, new Item.Properties().durability(ArmorItem.Type.BOOTS.getDurability(21)).rarity(Rarity.RARE)));
	public static final DeferredHolder<Item, Item> GOTHIC_COPPER_HELMET = REGISTRY.register("gothic_copper_helmet", () -> new GothicHelmetItem(SupernaturalArmors.COPPER_GOTHIC, ArmorItem.Type.HELMET, new Item.Properties().durability(ArmorItem.Type.HELMET.getDurability(11)), "textures/models/armor/copper_gothic.png"));
	public static final DeferredHolder<Item, Item> GOTHIC_IRON_HELMET = REGISTRY.register("gothic_iron_helmet", () -> new GothicHelmetItem(ArmorMaterials.IRON, ArmorItem.Type.HELMET, new Item.Properties().durability(ArmorItem.Type.HELMET.getDurability(15)), "textures/models/armor/iron_gothic.png"));
	public static final DeferredHolder<Item, Item> GOTHIC_DIAMOND_HELMET = REGISTRY.register("gothic_diamond_helmet", () -> new GothicHelmetItem(ArmorMaterials.DIAMOND, ArmorItem.Type.HELMET, new Item.Properties().durability(ArmorItem.Type.HELMET.getDurability(33)), "textures/models/armor/diamond_gothic.png"));
	public static final DeferredHolder<Item, Item> GOTHIC_GOLDEN_HELMET = REGISTRY.register("gothic_golden_helmet", () -> new GothicHelmetItem(ArmorMaterials.GOLD, ArmorItem.Type.HELMET, new Item.Properties().durability(ArmorItem.Type.HELMET.getDurability(7)), "textures/models/armor/golden_gothic.png"));
	public static final DeferredHolder<Item, Item> GOTHIC_NETHERITE_HELMET = REGISTRY.register("gothic_netherite_helmet", () -> new GothicHelmetItem(ArmorMaterials.NETHERITE, ArmorItem.Type.HELMET, new Item.Properties().fireResistant().durability(ArmorItem.Type.HELMET.getDurability(37)), "textures/models/armor/netherite_gothic.png"));
	public static final DeferredHolder<Item, Item> GOTHIC_EBONSTEEL_HELMET = REGISTRY.register("gothic_ebonsteel_helmet", () -> new GothicHelmetItem(SupernaturalArmors.EBONSTEEL, ArmorItem.Type.HELMET, new Item.Properties().durability(ArmorItem.Type.HELMET.getDurability(21)).rarity(Rarity.RARE), "textures/models/armor/ebonsteel_gothic.png"));
	public static final DeferredHolder<Item, Item> RITUAL_BOOK = REGISTRY.register("ritual_book", () -> new RitualBookItem(new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON)));
	public static final DeferredHolder<Item, Item> CONTRACT = REGISTRY.register("contract", () -> new ContractItem(new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON)));
	public static final DeferredHolder<Item, Item> COMPASS = REGISTRY.register("ritual_compass", () -> new RitualCompassItem(new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON)));
	public static final DeferredHolder<Item, Item> SOULGEM = REGISTRY.register("soulgem", () -> new SoulgemItem(new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON)));
	public static final DeferredHolder<Item, Item> CORE_DARKNESS = REGISTRY.register("core_darkness", () -> new CoreDarknessItem(new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON)));
	public static final DeferredHolder<Item, Item> VAMPIRE_DUST = REGISTRY.register("vampire_dust", () -> new Item(new Item.Properties()));
	public static final DeferredHolder<Item, Item> BLOOD = REGISTRY.register("blood_bottle", () -> new BloodItem(new Item.Properties().stacksTo(16).craftRemainder(Items.GLASS_BOTTLE)));

	private static DeferredHolder<Item, Item> block(DeferredHolder<Block, Block> block) {
		return REGISTRY.register(block.getId().getPath(), () -> new BlockItem(block.get(), new Item.Properties()));
	}
}