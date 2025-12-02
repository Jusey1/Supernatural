package net.salju.supernatural.item;

import net.salju.supernatural.init.SupernaturalConfig;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.ChatFormatting;
import java.util.List;

public class ContractItem extends Item {
	public ContractItem(Item.Properties props) {
		super(props);
	}

	@Override
	public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> list, TooltipFlag flag) {
		super.appendHoverText(stack, context, list, flag);
		if (flag.hasShiftDown()) {
			if (SupernaturalConfig.SACRIFICE.get()) {
				list.add(Component.translatable("entity.minecraft.goat").withStyle(ChatFormatting.DARK_RED));
			}
			list.add(Component.translatable("soulgem.supernatural.grand").withStyle(ChatFormatting.DARK_PURPLE));
			list.add(Component.literal("XX").withStyle(ChatFormatting.GOLD));
		} else {
			list.add(Component.translatable("desc.contract.shift").withStyle(ChatFormatting.GRAY));
		}
	}
}