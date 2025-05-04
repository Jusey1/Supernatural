package net.salju.supernatural.item;

import net.salju.supernatural.events.SupernaturalManager;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
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
}