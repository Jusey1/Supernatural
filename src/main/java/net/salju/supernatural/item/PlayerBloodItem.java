package net.salju.supernatural.item;

import net.salju.supernatural.events.SupernaturalHelpers;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
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
		UUID target = SupernaturalHelpers.getUUID(stack);
		if (target != null) {
			list.add(Component.literal(world.getPlayerByUUID(target).getName().getString()).withStyle(ChatFormatting.DARK_RED));
		}
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