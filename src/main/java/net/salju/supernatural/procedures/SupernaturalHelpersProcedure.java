package net.salju.supernatural.procedures;

import net.salju.supernatural.network.SupernaturalModVariables;
import net.salju.supernatural.init.SupernaturalModMobEffects;

import net.minecraft.world.level.Level;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.effect.MobEffectInstance;

public class SupernaturalHelpersProcedure {
	public static boolean isVampire(LivingEntity target) {
		return ((target.getCapability(SupernaturalModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new SupernaturalModVariables.PlayerVariables())).isVampire);
	}

	public static void setVampire(Player target, boolean vampire) {
		target.getCapability(SupernaturalModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
			capability.isVampire = vampire;
			capability.syncPlayerVariables(target);
		});
	}

	public static boolean isEvil(Level world) {
		return SupernaturalModVariables.WorldVariables.get(world).isEvil;
	}

	public static void setEvil(Level world, boolean evil) {
		SupernaturalModVariables.WorldVariables.get(world).isEvil = evil;
		SupernaturalModVariables.WorldVariables.get(world).syncData(world);
	}

	public static <T extends Mob> T convertArmor(ArmorStand target, EntityType<T> type, boolean equip) {
		T armor = type.create(target.level);
		armor.copyPosition(target);
		if (target.hasCustomName()) {
			armor.setCustomName(target.getCustomName());
			armor.setCustomNameVisible(target.isCustomNameVisible());
		}
		armor.setPersistenceRequired();
		armor.addEffect(new MobEffectInstance(SupernaturalModMobEffects.POSSESSION.get(), 999999, 0));
		armor.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SWORD));
		armor.setDropChance(EquipmentSlot.MAINHAND, 0.0F);
		if (equip) {
			for (EquipmentSlot slot : EquipmentSlot.values()) {
				ItemStack stack = target.getItemBySlot(slot);
				if (!stack.isEmpty()) {
					armor.setItemSlot(slot, stack.copy());
					armor.setDropChance(slot, 1.0F);
					stack.setCount(0);
				}
			}
		}
		target.level.addFreshEntity(armor);
		target.discard();
		return armor;
	}
}