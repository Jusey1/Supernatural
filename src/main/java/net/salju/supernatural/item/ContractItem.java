package net.salju.supernatural.item;

import net.salju.supernatural.init.SupernaturalConfig;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.ChatFormatting;
import java.util.function.Consumer;

public class ContractItem extends Item {
	public ContractItem(Item.Properties props) {
		super(props);
	}

	@Override
	public void appendHoverText(ItemStack stack, Item.TooltipContext context, TooltipDisplay display, Consumer<Component> list, TooltipFlag flag) {
		super.appendHoverText(stack, context, display, list, flag);
		if (flag.hasShiftDown()) {
			if (SupernaturalConfig.SACRIFICE.get()) {
				list.accept(Component.translatable("entity.minecraft.goat").withStyle(ChatFormatting.DARK_RED));
			}
			list.accept(Component.translatable("soulgem.supernatural.grand").withStyle(ChatFormatting.DARK_PURPLE));
			list.accept(Component.literal("XX").withStyle(ChatFormatting.GOLD));
		} else {
			list.accept(Component.translatable("desc.contract.shift").withStyle(ChatFormatting.GRAY));
		}
	}
}