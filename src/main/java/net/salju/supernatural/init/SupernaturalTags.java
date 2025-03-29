package net.salju.supernatural.init;

import net.salju.supernatural.Supernatural;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.structure.Structure;

public class SupernaturalTags {
	public static final TagKey<EntityType<?>> SPOOKY = TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(Supernatural.MODID, "spook_targets"));
	public static final TagKey<EntityType<?>> ARMOR = TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(Supernatural.MODID, "armor_targets"));
	public static final TagKey<EntityType<?>> SPAWNER = TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(Supernatural.MODID, "spawnerables"));
	public static final TagKey<EntityType<?>> VAMPIRE = TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(Supernatural.MODID, "is_vampire"));
	public static final TagKey<EntityType<?>> IMMUNITY = TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(Supernatural.MODID, "immunity"));
	public static final TagKey<EntityType<?>> GRAND = TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(Supernatural.MODID, "grand_soul"));
	public static final TagKey<EntityType<?>> GREATER = TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(Supernatural.MODID, "greater_soul"));
	public static final TagKey<EntityType<?>> COMMON = TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(Supernatural.MODID, "common_soul"));
	public static final TagKey<EntityType<?>> LESSER = TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(Supernatural.MODID, "lesser_soul"));
	public static final TagKey<EntityType<?>> PETTY = TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(Supernatural.MODID, "petty_soul"));
	public static final TagKey<Block> SOIL = BlockTags.create(ResourceLocation.fromNamespaceAndPath(Supernatural.MODID, "transmutable_soil_blocks"));
	public static final TagKey<Item> INGOTS = ItemTags.create(ResourceLocation.fromNamespaceAndPath(Supernatural.MODID, "transmutable_ingots"));
	public static final TagKey<Item> HELMS = ItemTags.create(ResourceLocation.fromNamespaceAndPath(Supernatural.MODID, "transmutable_helmets"));
	public static final TagKey<Item> KEPT = ItemTags.create(ResourceLocation.fromNamespaceAndPath(Supernatural.MODID, "altar_kept_items"));
	public static final TagKey<Item> MONEY = ItemTags.create(ResourceLocation.fromNamespaceAndPath(Supernatural.MODID, "valuable_items"));
	public static final TagKey<Structure> RUINS = TagKey.create(Registries.STRUCTURE, ResourceLocation.fromNamespaceAndPath(Supernatural.MODID, "ritual_compass_01"));
	public static final TagKey<Structure> LIFE = TagKey.create(Registries.STRUCTURE, ResourceLocation.fromNamespaceAndPath(Supernatural.MODID, "ritual_compass_02"));
	public static final TagKey<Structure> ANCIENT = TagKey.create(Registries.STRUCTURE, ResourceLocation.fromNamespaceAndPath(Supernatural.MODID, "ritual_compass_03"));
}