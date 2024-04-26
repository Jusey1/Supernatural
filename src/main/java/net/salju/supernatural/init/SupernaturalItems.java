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
import net.minecraft.world.item.Items;
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
	public static final RegistryObject<Item> CORE_BLOCK = block(SupernaturalBlocks.CORE_BLOCK);
	public static final RegistryObject<Item> POWER_BLOCK = block(SupernaturalBlocks.POWER_BLOCK);
	public static final RegistryObject<Item> GOTHIC_TEMPLATE = REGISTRY.register("gothic_template", () -> new SmithingTemplateItem(Component.translatable("desc.supernatural.helm.apply").withStyle(ChatFormatting.BLUE), Component.translatable("desc.supernatural.helm.ingredients").withStyle(ChatFormatting.BLUE), Component.translatable("desc.supernatural.helm").withStyle(ChatFormatting.GRAY), Component.translatable("desc.supernatural.helm.slot.base"), Component.translatable("desc.supernatural.helm.slot.add"), List.of((new ResourceLocation("item/empty_armor_slot_helmet"))), List.of((new ResourceLocation("supernatural:item/empty_slot_feather")))));
	public static final RegistryObject<Item> NECRO_TEMPLATE = REGISTRY.register("necro_template", () -> SmithingTemplateItem.createArmorTrimTemplate(new ResourceLocation("necro")));
	public static final RegistryObject<Item> ECTOPLASM = REGISTRY.register("ectoplasm", () -> new Item(new Item.Properties()));
	public static final RegistryObject<Item> VAMPIRE_DUST = REGISTRY.register("vampire_dust", () -> new Item(new Item.Properties()));
	public static final RegistryObject<Item> ANGEL_STATUE = REGISTRY.register("angel_statue", () -> new AngelItem(new Item.Properties()));
	public static final RegistryObject<Item> SOULGEM = REGISTRY.register("soulgem", () -> new SoulgemItem(new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> PLAYER_BLOOD = REGISTRY.register("player_blood", () -> new PlayerBloodItem(new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> CANNON = REGISTRY.register("artificer_cannon", () -> new CannonItem(new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> CANNONBALL = REGISTRY.register("cannonball", () -> new Item(new Item.Properties()));
	public static final RegistryObject<Item> COPPER_CANNONBALL = REGISTRY.register("copper_cannonball", () -> new Item(new Item.Properties()));
	public static final RegistryObject<Item> MAGIC_MIRROR = REGISTRY.register("magic_mirror", () -> new MagicMirrorItem(new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> BUNDLE_HOLDING = REGISTRY.register("bundle_of_holding", () -> new BundleHoldingItem(new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> VAMPIRISM_CONTRACT = REGISTRY.register("contract_vampirism", () -> new ContractItem(ContractItem.Types.VAMPIRISM, Items.GLASS_BOTTLE, "contract.supernatural.vampirism", "XX", new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> WEREWOLFISM_CONTRACT = REGISTRY.register("contract_werewolfism", () -> new ContractItem(ContractItem.Types.WEREWOLFISM, Items.BONE, "contract.supernatural.werewolfism", "XX", new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> REANIMATE_CONTRACT = REGISTRY.register("contract_reanimate", () -> new ContractItem(ContractItem.Types.REANIMATE, SupernaturalItems.SOULGEM.get(), "contract.supernatural.reanimate", "XXVIII", new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> VEXATION_CONTRACT = REGISTRY.register("contract_vexation", () -> new ContractItem(ContractItem.Types.VEXATION, Items.CAKE, "contract.supernatural.vexation", "XII", new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> MISFORTUNE_CONTRACT = REGISTRY.register("contract_misfortune", () -> new ContractItem(ContractItem.Types.MISFORTUNE, Items.RABBIT_FOOT, "contract.supernatural.misfortune", "XVI", new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> PUMPKIN_CONTRACT = REGISTRY.register("contract_pumpkin", () -> new ContractItem(ContractItem.Types.PUMPKIN, Items.CARVED_PUMPKIN, "contract.supernatural.pumpkin", "XII", new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> KNOWLEDGE_CONTRACT = REGISTRY.register("contract_knowledge", () -> new ContractItem(ContractItem.Types.KNOWLEDGE, Items.ENCHANTED_BOOK, "contract.supernatural.knowledge", "XVIII", new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> FORTUNE_CONTRACT = REGISTRY.register("contract_fortune", () -> new ContractItem(ContractItem.Types.FORTUNE, Items.ENCHANTED_GOLDEN_APPLE, "contract.supernatural.fortune", "XXIV", new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> DEVIL_CONTRACT = REGISTRY.register("contract_devil", () -> new ContractItem(ContractItem.Types.DEVIL, Items.NETHER_STAR, "contract.supernatural.devil", "XXVIII", new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON)));

	private static RegistryObject<Item> block(RegistryObject<Block> block) {
		return REGISTRY.register(block.getId().getPath(), () -> new BlockItem(block.get(), new Item.Properties()));
	}
}