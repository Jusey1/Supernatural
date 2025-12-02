package net.salju.supernatural.init;

import net.salju.supernatural.Supernatural;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Items;
import java.util.EnumMap;
import java.util.List;

public class SupernaturalArmors {
	public static final DeferredRegister<ArmorMaterial> REGISTRY = DeferredRegister.create(Registries.ARMOR_MATERIAL, Supernatural.MODID);
	public static final DeferredHolder<ArmorMaterial, ArmorMaterial> COPPER_GOTHIC = REGISTRY.register("copper_gothic", () -> new ArmorMaterial(getGothicHelmetMap(2), 13, SoundEvents.ARMOR_EQUIP_IRON, () -> Ingredient.of(Items.COPPER_INGOT), getList("copper_gothic"), 0.0F, 0.0F));
	public static final DeferredHolder<ArmorMaterial, ArmorMaterial> EBONSTEEL = REGISTRY.register("ebonsteel", () -> new ArmorMaterial(getFullArmorMap(3, 8, 6, 3, 12), 21, SoundEvents.ARMOR_EQUIP_IRON, () -> Ingredient.of(Items.IRON_INGOT), getList("ebonsteel"), 1.0F, 0.0F));

    public static EnumMap<ArmorItem.Type, Integer> getFullArmorMap(int h, int c, int l, int b, int x) {
        EnumMap<ArmorItem.Type, Integer> map = new EnumMap(ArmorItem.Type.class);
        map.put(ArmorItem.Type.HELMET, h);
        map.put(ArmorItem.Type.CHESTPLATE, c);
        map.put(ArmorItem.Type.LEGGINGS, l);
        map.put(ArmorItem.Type.BOOTS, b);
        map.put(ArmorItem.Type.BODY, x);
        return map;
    }

    public static EnumMap<ArmorItem.Type, Integer> getGothicHelmetMap(int h) {
        EnumMap<ArmorItem.Type, Integer> map = new EnumMap(ArmorItem.Type.class);
        map.put(ArmorItem.Type.HELMET, h);
        return map;
    }

	public static List<ArmorMaterial.Layer> getList(String name) {
		return List.of(new ArmorMaterial.Layer(ResourceLocation.fromNamespaceAndPath(Supernatural.MODID, name)));
	}
}