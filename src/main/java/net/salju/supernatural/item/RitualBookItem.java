package net.salju.supernatural.item;

import net.salju.supernatural.init.SupernaturalConfig;
import net.salju.supernatural.init.SupernaturalData;
import net.salju.supernatural.item.component.RitualBookData;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.ChatFormatting;
import java.util.List;

public class RitualBookItem extends Item {
	public RitualBookItem(Item.Properties props) {
		super(props);
	}

	@Override
	public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> list, TooltipFlag flag) {
		super.appendHoverText(stack, context, list, flag);
		if (flag.hasShiftDown()) {
			RitualBookData data = stack.get(SupernaturalData.BOOK);
			if (data != null) {
				list.add(Component.translatable("desc.book.page_" + data.getPage()).withStyle(ChatFormatting.BLUE));
				list.add(Component.translatable("desc.book.desc_" + data.getPage()).withStyle(ChatFormatting.GRAY));
				list.add(Component.empty());
				if (SupernaturalConfig.SACRIFICE.get() && data.requiresSacrifice()) {
					list.add(Component.translatable("desc.book.sacrifice").withStyle(ChatFormatting.DARK_RED));
				}
				list.add(Component.translatable(data.getRequiredItem()).withStyle(ChatFormatting.GRAY));
				list.add(Component.translatable(data.getSoulPower()).withStyle(ChatFormatting.DARK_PURPLE));
				list.add(Component.literal(data.getCandles()).withStyle(ChatFormatting.GOLD));
			}
			list.add(Component.empty());
			list.add(Component.translatable("desc.book.wheel").withStyle(ChatFormatting.GRAY));
		} else {
			list.add(Component.translatable("desc.book.shift").withStyle(ChatFormatting.GRAY));
		}
	}

	@Override
	public ItemStack getDefaultInstance() {
		ItemStack stack = super.getDefaultInstance();
		stack.set(SupernaturalData.BOOK, RitualBookData.EMPTY);
		return stack;
	}
}