package net.salju.supernatural.init;

import net.salju.supernatural.Supernatural;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.minecraft.world.item.equipment.EquipmentAssets;
import java.util.EnumMap;

public class SupernaturalArmors {
    public static ArmorMaterial COPPER = new ArmorMaterial(11, getGothicHelmetMap(2), 13, SoundEvents.ARMOR_EQUIP_COPPER, 0.0F, 0.0F, ItemTags.REPAIRS_COPPER_ARMOR, createId("copper_gothic"));
    public static ArmorMaterial IRON = new ArmorMaterial(15, getGothicHelmetMap(2), 9, SoundEvents.ARMOR_EQUIP_IRON, 0.0F, 0.0F, ItemTags.REPAIRS_IRON_ARMOR, createId("iron_gothic"));
	public static ArmorMaterial GOLDEN = new ArmorMaterial(7, getGothicHelmetMap(2), 25, SoundEvents.ARMOR_EQUIP_GOLD, 0.0F, 0.0F, ItemTags.REPAIRS_GOLD_ARMOR, createId("golden_gothic"));
	public static ArmorMaterial DIAMOND = new ArmorMaterial(33, getGothicHelmetMap(3), 10, SoundEvents.ARMOR_EQUIP_DIAMOND, 2.0F, 0.0F, ItemTags.REPAIRS_DIAMOND_ARMOR, createId("diamond_gothic"));
	public static ArmorMaterial NETHERITE = new ArmorMaterial(37, getGothicHelmetMap(3), 15, SoundEvents.ARMOR_EQUIP_NETHERITE, 3.0F, 0.1F, ItemTags.REPAIRS_NETHERITE_ARMOR, createId("netherite_gothic"));
    public static ArmorMaterial EBONSTEEL = new ArmorMaterial(21, getFullArmorMap(3, 8, 6, 3, 12), 21, SoundEvents.ARMOR_EQUIP_IRON, 1.0F, 0.0F, ItemTags.REPAIRS_IRON_ARMOR, createId("ebonsteel"));
    public static ArmorMaterial EBONSTEEL_GOTHIC = new ArmorMaterial(21, getGothicHelmetMap(3), 21, SoundEvents.ARMOR_EQUIP_IRON, 1.0F, 0.0F, ItemTags.REPAIRS_IRON_ARMOR, createId("ebonsteel_gothic"));

    public static EnumMap<ArmorType, Integer> getFullArmorMap(int h, int c, int l, int b, int x) {
        EnumMap<ArmorType, Integer> map = new EnumMap<>(ArmorType.class);
        map.put(ArmorType.HELMET, h);
        map.put(ArmorType.CHESTPLATE, c);
        map.put(ArmorType.LEGGINGS, l);
        map.put(ArmorType.BOOTS, b);
        map.put(ArmorType.BODY, x);
        return map;
    }

    public static EnumMap<ArmorType, Integer> getGothicHelmetMap(int h) {
        EnumMap<ArmorType, Integer> map = new EnumMap<>(ArmorType.class);
        map.put(ArmorType.HELMET, h);
        return map;
    }

	public static ResourceKey<EquipmentAsset> createId(String name) {
		return ResourceKey.create(EquipmentAssets.ROOT_ID, ResourceLocation.fromNamespaceAndPath(Supernatural.MODID, name));
	}
}