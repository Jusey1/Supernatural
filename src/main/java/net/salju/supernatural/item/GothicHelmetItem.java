package net.salju.supernatural.item;

import net.salju.supernatural.init.SupernaturalTags;
import net.salju.supernatural.client.model.GothicKoboldArmorModel;
import net.salju.supernatural.client.model.GothicArmorModel;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.DyeableArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Entity;
import net.minecraft.client.model.HumanoidModel;
import java.util.function.Consumer;

public class GothicHelmetItem extends DyeableArmorItem {
	private String texture;

	public GothicHelmetItem(ArmorMaterial material, ArmorItem.Type type, Item.Properties properties, String text) {
		super(material, type, properties);
		this.texture = text;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void initializeClient(Consumer<IClientItemExtensions> consumer) {
		consumer.accept(new IClientItemExtensions() {
			@Override
			public HumanoidModel<?> getHumanoidArmorModel(LivingEntity target, ItemStack stack, EquipmentSlot slot, HumanoidModel<?> basic) {
				if (isKobold(target)) {
					return new GothicKoboldArmorModel<>(GothicKoboldArmorModel.createBodyLayer().bakeRoot(), stack, slot);
				} else {
					return new GothicArmorModel<>(GothicArmorModel.createBodyLayer().bakeRoot(), stack, slot);
				}
			}
		});
	}

	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String layer) {
		return this.texture;
	}

	private static boolean isKobold(LivingEntity target) {
		return target.getType().is(SupernaturalTags.KOBOLD);
	}
}
