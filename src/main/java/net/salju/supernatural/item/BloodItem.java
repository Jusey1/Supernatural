package net.salju.supernatural.item;

import net.salju.supernatural.events.SupernaturalManager;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;

public class BloodItem extends Item {
	public BloodItem(Item.Properties props) {
		super(props);
	}

	@Override
	public int getUseDuration(ItemStack stack, LivingEntity target) {
		return 40;
	}

	@Override
	public UseAnim getUseAnimation(ItemStack stack) {
		return UseAnim.DRINK;
	}

	@Override
	public SoundEvent getDrinkingSound() {
		return SoundEvents.HONEY_DRINK;
	}

	@Override
	public SoundEvent getEatingSound() {
		return SoundEvents.HONEY_DRINK;
	}

	@Override
	public ItemStack finishUsingItem(ItemStack stack, Level world, LivingEntity target) {
		super.finishUsingItem(stack, world, target);
		if (SupernaturalManager.isVampire(target)) {
			target.heal(6.0F);
		}
		if (target instanceof Player player && !player.hasInfiniteMaterials()) {
			stack.shrink(1);
		}
		if (stack.isEmpty()) {
			return new ItemStack(Items.GLASS_BOTTLE);
		} else {
			if (target instanceof Player player && !player.hasInfiniteMaterials()) {
				ItemStack bottle = new ItemStack(Items.GLASS_BOTTLE);
				if (!player.getInventory().add(bottle)) {
					player.drop(bottle, false);
				}
			}
			return stack;
		}
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
		if (SupernaturalManager.isVampire(player)) {
			return ItemUtils.startUsingInstantly(world, player, hand);
		}
		return super.use(world, player, hand);
	}
}