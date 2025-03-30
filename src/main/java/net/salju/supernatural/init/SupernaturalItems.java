package net.salju.supernatural.init;

import net.salju.supernatural.Supernatural;
import net.salju.supernatural.item.*;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.equipment.*;
import net.minecraft.world.item.*;

public class SupernaturalItems {
	public static final DeferredRegister<Item> REGISTRY = DeferredRegister.createItems(Supernatural.MODID);
	public static final DeferredHolder<Item, Item> VAMPIRE_SPAWN_EGG = REGISTRY.register("vampire_spawn_egg", () -> new SpawnEggItem(SupernaturalMobs.VAMPIRE.get(), -6710887, -3407872, createBaseProps("vampire_spawn_egg")));
	public static final DeferredHolder<Item, Item> NECROMANCER_SPAWN_EGG = REGISTRY.register("necromancer_spawn_egg", () -> new SpawnEggItem(SupernaturalMobs.NECROMANCER.get(), -6710887, -13382401, createBaseProps("necromancer_spawn_egg")));
	public static final DeferredHolder<Item, Item> ARMOR_SPAWN_EGG = REGISTRY.register("armor_spawn_egg", () -> new SpawnEggItem(SupernaturalMobs.POSSESSED_ARMOR.get(), -10066330, -3355444, createBaseProps("armor_spawn_egg")));
	public static final DeferredHolder<Item, Item> SPOOKY_SPAWN_EGG = REGISTRY.register("spooky_spawn_egg", () -> new SpawnEggItem(SupernaturalMobs.SPOOKY.get(), -6697729, -3342337, createBaseProps("spooky_spawn_egg")));
	public static final DeferredHolder<Item, Item> GOTHIC_IRON_HELMET = REGISTRY.register("gothic_iron_helmet", () -> new GothicHelmetItem(SupernaturalArmors.IRON, createBaseProps("gothic_iron_helmet").durability(ArmorType.HELMET.getDurability(15))));
	public static final DeferredHolder<Item, Item> GOTHIC_DIAMOND_HELMET = REGISTRY.register("gothic_diamond_helmet", () -> new GothicHelmetItem(SupernaturalArmors.DIAMOND, createBaseProps("gothic_diamond_helmet").durability(ArmorType.HELMET.getDurability(33))));
	public static final DeferredHolder<Item, Item> GOTHIC_GOLDEN_HELMET = REGISTRY.register("gothic_golden_helmet", () -> new GothicHelmetItem(SupernaturalArmors.GOLDEN, createBaseProps("gothic_golden_helmet").durability(ArmorType.HELMET.getDurability(7))));
	public static final DeferredHolder<Item, Item> GOTHIC_NETHERITE_HELMET = REGISTRY.register("gothic_netherite_helmet", () -> new GothicHelmetItem(SupernaturalArmors.NETHERITE, createBaseProps("gothic_netherite_helmet").fireResistant().durability(ArmorType.HELMET.getDurability(37))));
	public static final DeferredHolder<Item, Item> GRAVE_SOIL = block(SupernaturalBlocks.GRAVE_SOIL, "grave_soil");
	public static final DeferredHolder<Item, Item> RITUAL_ALTAR = block(SupernaturalBlocks.RITUAL_ALTAR, "ritual_altar");
	public static final DeferredHolder<Item, Item> CONTRACT = REGISTRY.register("contract", () -> new ContractItem(createBaseProps("contract").stacksTo(1).rarity(Rarity.UNCOMMON).fireResistant()));
	public static final DeferredHolder<Item, Item> RITUAL_BOOK = REGISTRY.register("ritual_book", () -> new RitualBookItem(createBaseProps("ritual_book").stacksTo(1).rarity(Rarity.UNCOMMON).fireResistant()));
	public static final DeferredHolder<Item, Item> VAMPIRE_DUST = REGISTRY.register("vampire_dust", () -> new Item(createBaseProps("vampire_dust")));
	public static final DeferredHolder<Item, Item> ANGEL_STATUE = REGISTRY.register("angel_statue", () -> new AngelItem(createBaseProps("angel_statue").stacksTo(1).rarity(Rarity.UNCOMMON)));
	public static final DeferredHolder<Item, Item> SOULGEM = REGISTRY.register("soulgem", () -> new SoulgemItem(createBaseProps("soulgem").stacksTo(1).rarity(Rarity.UNCOMMON).fireResistant()));
	public static final DeferredHolder<Item, Item> COMPASS = REGISTRY.register("ritual_compass", () -> new RitualCompassItem(createBaseProps("ritual_compass").stacksTo(1).rarity(Rarity.UNCOMMON).fireResistant()));
	public static final DeferredHolder<Item, Item> ANCHORBALL = REGISTRY.register("anchorball", () -> new AnchorballItem(new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON).fireResistant()));

	private static DeferredHolder<Item, Item> block(DeferredHolder<Block, Block> block, String name) {
		return REGISTRY.register(block.getId().getPath(), () -> new BlockItem(block.get(), createBaseProps(name)));
	}

	public static Item.Properties createBaseProps(String name) {
		return new Item.Properties().setId(ResourceKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(Supernatural.MODID, name)));
	}
}