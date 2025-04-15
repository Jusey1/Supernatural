package net.salju.supernatural.item;

import net.salju.supernatural.init.SupernaturalData;
import net.salju.supernatural.init.SupernaturalItems;
import net.salju.supernatural.init.SupernaturalTags;
import net.salju.supernatural.item.component.RitualCompassData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.util.Mth;
import net.minecraft.ChatFormatting;
import java.util.Optional;
import java.util.function.Consumer;

public class RitualCompassItem extends Item {
	public RitualCompassItem(Item.Properties props) {
		super(props);
	}

	@Override
	public void appendHoverText(ItemStack stack, Item.TooltipContext context, TooltipDisplay display, Consumer<Component> list, TooltipFlag flag) {
		super.appendHoverText(stack, context, display, list, flag);
		RitualCompassData data = stack.get(SupernaturalData.COMPASS);
		if (data != null) {
			if (context.level().dimensionType().natural()) {
				list.accept(Component.translatable(data.getPower()).withStyle(ChatFormatting.DARK_PURPLE));
			} else {
				list.accept(Component.translatable("desc.compass.broken").withStyle(ChatFormatting.DARK_PURPLE));
			}
		} else {
			list.accept(Component.translatable("desc.compass.creative").withStyle(ChatFormatting.DARK_PURPLE));
		}
	}

	@Override
	public InteractionResult use(Level world, Player player, InteractionHand hand) {
		if (player.isCreative() && world.dimensionType().natural()) {
			if (world instanceof ServerLevel lvl) {
				player.setItemInHand(hand, getRitualCompass(player.blockPosition(), lvl, Mth.nextInt(player.getRandom(), 0, 2)));
			}
			return InteractionResult.SUCCESS;
		}
		return InteractionResult.PASS;
	}

	public static ItemStack getRitualCompass(BlockPos pos, ServerLevel lvl, int i) {
		ItemStack stack = new ItemStack(SupernaturalItems.COMPASS.get());
		if (i <= 0) {
			BlockPos loc = lvl.findNearestMapStructure(SupernaturalTags.RUINS, pos, 100, false);
			if (loc != null) {
				stack.set(SupernaturalData.COMPASS, new RitualCompassData(Optional.of(GlobalPos.of(lvl.dimension(), loc)), "desc.compass.ruins"));
			}
		} else if (i >= 2) {
			BlockPos loc = lvl.findNearestMapStructure(SupernaturalTags.ANCIENT, pos, 100, false);
			if (loc != null) {
				stack.set(SupernaturalData.COMPASS, new RitualCompassData(Optional.of(GlobalPos.of(lvl.dimension(), loc)), "desc.compass.ancient"));
			}
		} else {
			BlockPos loc = lvl.findNearestMapStructure(SupernaturalTags.LIFE, pos, 100, false);
			if (loc != null) {
				stack.set(SupernaturalData.COMPASS, new RitualCompassData(Optional.of(GlobalPos.of(lvl.dimension(), loc)), "desc.compass.village"));
			}
		}
		return stack;
	}
}