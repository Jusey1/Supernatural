package net.salju.supernatural.item;

import net.salju.supernatural.init.SupernaturalItems;
import net.salju.supernatural.client.model.GothicKoboldArmorModel;
import net.salju.supernatural.client.model.GothicArmorModel;

import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.api.distmarker.Dist;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.DyeableArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Entity;
import net.minecraft.tags.TagKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Registry;
import net.minecraft.client.model.HumanoidModel;

import java.util.function.Consumer;

public class GothicHelmetItem extends DyeableArmorItem {
	public GothicHelmetItem(ArmorMaterial material, EquipmentSlot slot, Item.Properties properties) {
		super(material, slot, properties);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void initializeClient(Consumer<IClientItemExtensions> consumer) {
		consumer.accept(new IClientItemExtensions() {
			@Override
			public HumanoidModel<?> getHumanoidArmorModel(LivingEntity wearer, ItemStack stack, EquipmentSlot armorSlot, HumanoidModel<?> df) {
				if (wearer.getType().is(TagKey.create(Registry.ENTITY_TYPE_REGISTRY, new ResourceLocation("forge:kobolds")))) {
					return new GothicKoboldArmorModel<>(GothicKoboldArmorModel.createBodyLayer().bakeRoot(), stack, armorSlot);
				} else {
					return new GothicArmorModel<>(GothicArmorModel.createBodyLayer().bakeRoot(), stack, armorSlot);
				}
			}
		});
	}

	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String layer) {
		if (stack.getItem() == SupernaturalItems.GOTHIC_IRON_HELMET.get()) {
			return "supernatural:textures/models/armor/gothic_iron.png";
		} else if (stack.getItem() == SupernaturalItems.GOTHIC_DIAMOND_HELMET.get()) {
			return "supernatural:textures/models/armor/gothic_diamond.png";
		} else if (stack.getItem() == SupernaturalItems.GOTHIC_GOLDEN_HELMET.get()) {
			return "supernatural:textures/models/armor/gothic_golden.png";
		} else {
			return "supernatural:textures/models/armor/gothic_netherite.png";
		}
	}
}