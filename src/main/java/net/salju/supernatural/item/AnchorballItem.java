package net.salju.supernatural.item;

import net.salju.supernatural.init.SupernaturalBlocks;
import net.salju.supernatural.init.SupernaturalData;
import net.salju.supernatural.init.SupernaturalItems;
import net.salju.supernatural.item.component.AnchorballData;
import net.minecraft.core.GlobalPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.InteractionResult;
import net.minecraft.ChatFormatting;
import java.util.function.Consumer;

public class AnchorballItem extends Item {
	public AnchorballItem(Item.Properties props) {
		super(props);
	}

	@Override
	public void appendHoverText(ItemStack stack, Item.TooltipContext context, TooltipDisplay display, Consumer<Component> list, TooltipFlag flag) {
		super.appendHoverText(stack, context, display, list, flag);
		AnchorballData data = stack.get(SupernaturalData.ANCHOR);
		if (data != null) {
			list.accept(Component.literal(data.getPos().getX() + ", " + data.getPos().getY() + ", " + data.getPos().getZ()).withStyle(ChatFormatting.DARK_PURPLE));
		}
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		if (context.getLevel().getBlockState(context.getClickedPos()).is(SupernaturalBlocks.RITUAL_ALTAR) && context.getPlayer() != null && context.getPlayer().isCreative() && context.getItemInHand().is(SupernaturalItems.ANCHORBALL)) {
			context.getItemInHand().set(SupernaturalData.ANCHOR, new AnchorballData(GlobalPos.of(context.getLevel().dimension(), context.getClickedPos())));
			return InteractionResult.SUCCESS;
		}
		return super.useOn(context);
	}
}