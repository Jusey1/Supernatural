package net.salju.supernatural.item;

import net.salju.supernatural.init.SupernaturalItems;
import net.salju.supernatural.events.SupernaturalManager;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionHand;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import java.util.UUID;
import java.util.List;

public class PlayerBloodItem extends Item {
	public PlayerBloodItem(Item.Properties props) {
		super(props);
	}

	@Override
	public void appendHoverText(ItemStack stack, Level world, List<Component> list, TooltipFlag flag) {
		super.appendHoverText(stack, world, list, flag);
		UUID target = SupernaturalManager.getUUID(stack);
		if (target != null && world != null) {
			list.add(Component.literal(world.getPlayerByUUID(target).getName().getString()).withStyle(ChatFormatting.DARK_RED));
		}
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		if (player.isCreative() && stack.is(SupernaturalItems.PLAYER_BLOOD.get()) && SupernaturalManager.getUUID(stack) == null) {
			SupernaturalManager.setUUID(stack, player);
			return InteractionResultHolder.consume(stack);
		}
		return super.use(world, player, hand);
	}

	@Override
	public boolean hasCraftingRemainingItem() {
		return true;
	}

	@Override
	public ItemStack getCraftingRemainingItem(ItemStack stack) {
		return new ItemStack(Items.GLASS_BOTTLE);
	}
}