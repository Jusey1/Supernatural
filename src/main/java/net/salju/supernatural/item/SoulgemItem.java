package net.salju.supernatural.item;

import net.salju.supernatural.events.SupernaturalManager;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import java.util.List;

public class SoulgemItem extends Item {
	public SoulgemItem(Item.Properties props) {
		super(props);
	}

	@Override
	public void appendHoverText(ItemStack stack, Level world, List<Component> list, TooltipFlag flag) {
		super.appendHoverText(stack, world, list, flag);
		if (SupernaturalManager.getSoul(stack) != "") {
			list.add(Component.translatable(SupernaturalManager.getSoul(stack)).withStyle(ChatFormatting.GOLD));
			list.add(Component.translatable(SupernaturalManager.getSoulgem(stack)).withStyle(ChatFormatting.DARK_PURPLE));
		}
	}
}