package net.salju.supernatural.item;

import net.salju.supernatural.init.SupernaturalData;
import net.salju.supernatural.item.component.RitualBookData;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.ChatFormatting;
import java.util.function.Consumer;

public class RitualBookItem extends Item {
	public RitualBookItem(Item.Properties props) {
		super(props);
	}

	@Override
	public void appendHoverText(ItemStack stack, Item.TooltipContext context, TooltipDisplay display, Consumer<Component> list, TooltipFlag flag) {
		super.appendHoverText(stack, context, display, list, flag);
		if (flag.hasShiftDown()) {
			RitualBookData data = stack.get(SupernaturalData.BOOK);
			if (data != null) {
				list.accept(Component.translatable("desc.book.page_" + data.getPage()).withStyle(ChatFormatting.BLUE));
				list.accept(Component.translatable("desc.book.desc_" + data.getPage()).withStyle(ChatFormatting.GRAY));
				list.accept(Component.empty());
				if (data.requiresSacrifice()) {
                    list.accept(Component.translatable("desc.book.sacrifice" + data.getSacificeTooltip()).withStyle(ChatFormatting.DARK_RED));
                }
				list.accept(Component.translatable(data.getRequiredItem()).withStyle(ChatFormatting.GRAY));
				list.accept(Component.translatable(data.getSoulPower()).withStyle(ChatFormatting.DARK_PURPLE));
				list.accept(Component.literal(data.getCandles()).withStyle(ChatFormatting.GOLD));
            } else {
                list.accept(Component.translatable("desc.book.tutorial_0").withStyle(ChatFormatting.BLUE));
                list.accept(Component.translatable("desc.book.tutorial_1").withStyle(ChatFormatting.GRAY));
                list.accept(Component.translatable("desc.book.tutorial_2").withStyle(ChatFormatting.GRAY));
                list.accept(Component.empty());
                list.accept(Component.translatable("desc.book.tutorial_3").withStyle(ChatFormatting.DARK_RED));
                list.accept(Component.translatable("desc.book.tutorial_4").withStyle(ChatFormatting.GRAY));
                list.accept(Component.translatable("desc.book.tutorial_5").withStyle(ChatFormatting.DARK_PURPLE));
                list.accept(Component.translatable("desc.book.tutorial_6").withStyle(ChatFormatting.GOLD));
                list.accept(Component.empty());
                list.accept(Component.translatable("desc.book.tutorial_7").withStyle(ChatFormatting.GRAY));
                list.accept(Component.translatable("desc.book.tutorial_8").withStyle(ChatFormatting.GRAY));
            }
			list.accept(Component.empty());
			list.accept(Component.translatable("desc.book.wheel").withStyle(ChatFormatting.GRAY));
		} else {
			list.accept(Component.translatable("desc.book.shift").withStyle(ChatFormatting.GRAY));
		}
	}

    @Override
    public InteractionResult use(Level world, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (stack.get(SupernaturalData.BOOK) != null) {
            stack.remove(SupernaturalData.BOOK);
            return InteractionResult.SUCCESS;
        }
        return super.use(world, player, hand);
    }
}