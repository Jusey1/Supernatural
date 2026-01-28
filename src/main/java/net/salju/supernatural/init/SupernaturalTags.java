package net.salju.supernatural.init;

import net.salju.supernatural.Supernatural;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.structure.Structure;

public class SupernaturalTags {
    public static final TagKey<EntityType<?>> ARMOR = TagKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(Supernatural.MODID, "armor_targets"));
	public static final TagKey<EntityType<?>> SPOOKY = TagKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(Supernatural.MODID, "spook_targets"));
    public static final TagKey<EntityType<?>> MERFOLK = TagKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(Supernatural.MODID, "merfolk_targets"));
	public static final TagKey<EntityType<?>> SPAWNER = TagKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(Supernatural.MODID, "spawnerables"));
	public static final TagKey<EntityType<?>> VAMPIRE = TagKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(Supernatural.MODID, "is_vampire"));
	public static final TagKey<EntityType<?>> IMMUNITY = TagKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(Supernatural.MODID, "immunity"));
	public static final TagKey<EntityType<?>> BLOODY = TagKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(Supernatural.MODID, "can_get_blood_from"));
	public static final TagKey<EntityType<?>> GRAND = TagKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(Supernatural.MODID, "grand_soul"));
	public static final TagKey<EntityType<?>> GREATER = TagKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(Supernatural.MODID, "greater_soul"));
	public static final TagKey<EntityType<?>> COMMON = TagKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(Supernatural.MODID, "common_soul"));
	public static final TagKey<EntityType<?>> LESSER = TagKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(Supernatural.MODID, "lesser_soul"));
	public static final TagKey<EntityType<?>> PETTY = TagKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(Supernatural.MODID, "petty_soul"));
	public static final TagKey<Block> SOIL = BlockTags.create(Identifier.fromNamespaceAndPath(Supernatural.MODID, "transmutable_soil_blocks"));
	public static final TagKey<Item> INGOTS = ItemTags.create(Identifier.fromNamespaceAndPath(Supernatural.MODID, "transmutable_ingots"));
	public static final TagKey<Item> HELMS = ItemTags.create(Identifier.fromNamespaceAndPath(Supernatural.MODID, "transmutable_helmets"));
	public static final TagKey<Item> KEPT = ItemTags.create(Identifier.fromNamespaceAndPath(Supernatural.MODID, "altar_kept_items"));
	public static final TagKey<Item> BLOOD = ItemTags.create(Identifier.fromNamespaceAndPath(Supernatural.MODID, "blood"));
    public static final TagKey<Item> DARK_ARMOR = ItemTags.create(Identifier.fromNamespaceAndPath(Supernatural.MODID, "ebonsteel_armor"));
	public static final TagKey<Structure> RUINS = TagKey.create(Registries.STRUCTURE, Identifier.fromNamespaceAndPath(Supernatural.MODID, "ritual_compass_01"));
	public static final TagKey<Structure> LIFE = TagKey.create(Registries.STRUCTURE, Identifier.fromNamespaceAndPath(Supernatural.MODID, "ritual_compass_02"));
	public static final TagKey<Structure> ANCIENT = TagKey.create(Registries.STRUCTURE, Identifier.fromNamespaceAndPath(Supernatural.MODID, "ritual_compass_03"));
    public static final TagKey<DamageType> MAGIC = TagKey.create(Registries.DAMAGE_TYPE, Identifier.fromNamespaceAndPath(Supernatural.MODID, "ebonsteel_resists"));
}