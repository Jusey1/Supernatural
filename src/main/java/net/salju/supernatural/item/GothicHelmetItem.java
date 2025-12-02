package net.salju.supernatural.item;

import net.salju.supernatural.Supernatural;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ArmorItem;

public class GothicHelmetItem extends ArmorItem {
	private final String texture;

	public GothicHelmetItem(Holder<ArmorMaterial> material, ArmorItem.Type type, Item.Properties properties, String text) {
		super(material, type, properties);
		this.texture = text;
	}

	@Override
	public ResourceLocation getArmorTexture(ItemStack stack, Entity target, EquipmentSlot slot, ArmorMaterial.Layer layer, boolean check) {
		return ResourceLocation.fromNamespaceAndPath(Supernatural.MODID, this.texture);
	}
}