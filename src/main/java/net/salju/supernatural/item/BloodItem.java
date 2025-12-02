package net.salju.supernatural.item;

import net.salju.supernatural.events.SupernaturalManager;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.level.Level;

public class BloodItem extends Item {
	public BloodItem(Item.Properties props) {
		super(props);
	}

	@Override
	public InteractionResult use(Level world, Player player, InteractionHand hand) {
		if (SupernaturalManager.isVampire(player)) {
			return ItemUtils.startUsingInstantly(world, player, hand);
		}
		return InteractionResult.PASS;
	}

	@Override
	public ItemStack finishUsingItem(ItemStack stack, Level world, LivingEntity target) {
		if (SupernaturalManager.isVampire(target)) {
			target.heal(6.0F);
		}
		return super.finishUsingItem(stack, world, target);
	}
}