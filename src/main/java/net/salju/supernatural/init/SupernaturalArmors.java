package net.salju.supernatural.init;

import net.salju.supernatural.Supernatural;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.Util;
import java.util.EnumMap;

public class SupernaturalArmors {
	public static final ResourceLocation I = ResourceLocation.fromNamespaceAndPath(Supernatural.MODID, "iron_gothic");
	public static final ResourceLocation G = ResourceLocation.fromNamespaceAndPath(Supernatural.MODID, "golden_gothic");
	public static final ResourceLocation D = ResourceLocation.fromNamespaceAndPath(Supernatural.MODID, "diamond_gothic");
	public static final ResourceLocation N = ResourceLocation.fromNamespaceAndPath(Supernatural.MODID, "netherite_gothic");

	public static ArmorMaterial IRON = new ArmorMaterial(15, Util.make(new EnumMap<>(ArmorType.class), map -> map.put(ArmorType.HELMET, 2)), 9, SoundEvents.ARMOR_EQUIP_IRON, 0.0F, 0.0F, ItemTags.REPAIRS_IRON_ARMOR, I);
	public static ArmorMaterial GOLDEN = new ArmorMaterial(7, Util.make(new EnumMap<>(ArmorType.class), map -> map.put(ArmorType.HELMET, 2)), 25, SoundEvents.ARMOR_EQUIP_GOLD, 0.0F, 0.0F, ItemTags.REPAIRS_GOLD_ARMOR, G);
	public static ArmorMaterial DIAMOND = new ArmorMaterial(33, Util.make(new EnumMap<>(ArmorType.class), map -> map.put(ArmorType.HELMET, 3)), 10, SoundEvents.ARMOR_EQUIP_DIAMOND, 2.0F, 0.0F, ItemTags.REPAIRS_DIAMOND_ARMOR, D);
	public static ArmorMaterial NETHERITE = new ArmorMaterial(37, Util.make(new EnumMap<>(ArmorType.class), map -> map.put(ArmorType.HELMET, 3)), 15, SoundEvents.ARMOR_EQUIP_NETHERITE, 3.0F, 0.1F, ItemTags.REPAIRS_NETHERITE_ARMOR, N);
}