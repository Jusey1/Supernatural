package net.salju.supernatural.init;

import net.salju.supernatural.item.*;
import net.salju.supernatural.SupernaturalMod;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.SmithingTemplateItem;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import java.util.List;

public class SupernaturalItems {
	public static final DeferredRegister<Item> REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, SupernaturalMod.MODID);
	public static final RegistryObject<Item> VAMPIRE_SPAWN_EGG = REGISTRY.register("vampire_spawn_egg", () -> new ForgeSpawnEggItem(SupernaturalMobs.VAMPIRE, -6710887, -3407872, new Item.Properties()));
	public static final RegistryObject<Item> NECROMANCER_SPAWN_EGG = REGISTRY.register("necromancer_spawn_egg", () -> new ForgeSpawnEggItem(SupernaturalMobs.NECROMANCER, -6710887, -13382401, new Item.Properties()));
	public static final RegistryObject<Item> POSSESSED_ARMOR_SPAWN_EGG = REGISTRY.register("possessed_armor_spawn_egg", () -> new ForgeSpawnEggItem(SupernaturalMobs.POSSESSED_ARMOR, -10066330, -3355444, new Item.Properties()));
	public static final RegistryObject<Item> SPOOKY_SPAWN_EGG = REGISTRY.register("spooky_spawn_egg", () -> new ForgeSpawnEggItem(SupernaturalMobs.SPOOKY, -6697729, -3342337, new Item.Properties()));
	public static final RegistryObject<Item> MER_AMETHYST_SPAWN_EGG = REGISTRY.register("mer_amethyst_spawn_egg", () -> new ForgeSpawnEggItem(SupernaturalMobs.MER_AMETHYST, -16737946, -13369396, new Item.Properties()));
	public static final RegistryObject<Item> GOTHIC_IRON_HELMET = REGISTRY.register("gothic_iron_helmet", () -> new GothicHelmetItem(ArmorMaterials.IRON, ArmorItem.Type.HELMET, new Item.Properties(), "supernatural:textures/models/armor/gothic_iron.png"));
	public static final RegistryObject<Item> GOTHIC_DIAMOND_HELMET = REGISTRY.register("gothic_diamond_helmet", () -> new GothicHelmetItem(ArmorMaterials.DIAMOND, ArmorItem.Type.HELMET, new Item.Properties(), "supernatural:textures/models/armor/gothic_diamond.png"));
	public static final RegistryObject<Item> GOTHIC_GOLDEN_HELMET = REGISTRY.register("gothic_golden_helmet", () -> new GothicHelmetItem(ArmorMaterials.GOLD, ArmorItem.Type.HELMET, new Item.Properties(), "supernatural:textures/models/armor/gothic_golden.png"));
	public static final RegistryObject<Item> GOTHIC_NETHERITE_HELMET = REGISTRY.register("gothic_netherite_helmet", () -> new GothicHelmetItem(ArmorMaterials.NETHERITE, ArmorItem.Type.HELMET, new Item.Properties(), "supernatural:textures/models/armor/gothic_netherite.png"));
	public static final RegistryObject<Item> GRAVE_SOIL = block(SupernaturalBlocks.GRAVE_SOIL);
	public static final RegistryObject<Item> RITUAL_ALTAR = block(SupernaturalBlocks.RITUAL_ALTAR);
	public static final RegistryObject<Item> GOTHIC_TEMPLATE = REGISTRY.register("gothic_template", () -> new SmithingTemplateItem(Component.translatable("desc.supernatural.helm.apply").withStyle(ChatFormatting.BLUE), Component.translatable("desc.supernatural.helm.ingredients").withStyle(ChatFormatting.BLUE), Component.translatable("desc.supernatural.helm").withStyle(ChatFormatting.GRAY), Component.translatable("desc.supernatural.helm.slot.base"), Component.translatable("desc.supernatural.helm.slot.add"), List.of((new ResourceLocation("item/empty_armor_slot_helmet"))), List.of((new ResourceLocation("supernatural:item/empty_slot_feather")))));
	public static final RegistryObject<Item> NECRO_TEMPLATE = REGISTRY.register("necro_template", () -> SmithingTemplateItem.createArmorTrimTemplate(new ResourceLocation("necro")));
	public static final RegistryObject<Item> ECTOPLASM = REGISTRY.register("ectoplasm", () -> new Item(new Item.Properties()));
	public static final RegistryObject<Item> VAMPIRE_DUST = REGISTRY.register("vampire_dust", () -> new Item(new Item.Properties()));
	public static final RegistryObject<Item> ANGEL_STATUE = REGISTRY.register("angel_statue", () -> new AngelItem(new Item.Properties()));
	public static final RegistryObject<Item> SOULGEM = REGISTRY.register("soulgem", () -> new SoulgemItem(new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON).fireResistant()));
	public static final RegistryObject<Item> TAGLOCK = REGISTRY.register("taglock", () -> new TaglockItem(new Item.Properties().stacksTo(1)));
	public static final RegistryObject<Item> BLOOD = REGISTRY.register("blood_bottle", () -> new BloodItem(new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON)));

	private static RegistryObject<Item> block(RegistryObject<Block> block) {
		return REGISTRY.register(block.getId().getPath(), () -> new BlockItem(block.get(), new Item.Properties()));
	}
}