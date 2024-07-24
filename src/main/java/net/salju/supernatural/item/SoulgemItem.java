package net.salju.supernatural.item;

import net.salju.supernatural.init.SupernaturalTags;
import net.salju.supernatural.init.SupernaturalItems;
import net.salju.supernatural.events.SupernaturalManager;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
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

	@Override
	public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity target, InteractionHand hand) {
		if (player.isCreative() && SupernaturalManager.getSoul(stack) == "" && target instanceof Mob && !target.getType().is(SupernaturalTags.IMMUNITY)) {
			player.setItemInHand(hand, SupernaturalManager.setSoul(new ItemStack(SupernaturalItems.SOULGEM.get()), target));
			return InteractionResult.SUCCESS;
		}
		return InteractionResult.PASS;
	}
}