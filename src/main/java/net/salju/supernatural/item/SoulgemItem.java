package net.salju.supernatural.item;

import net.salju.supernatural.init.SupernaturalData;
import net.salju.supernatural.init.SupernaturalItems;
import net.salju.supernatural.init.SupernaturalTags;
import net.salju.supernatural.item.component.SoulgemData;
import net.salju.supernatural.events.SupernaturalManager;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.*;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import java.util.function.Consumer;

public class SoulgemItem extends Item {
	public SoulgemItem(Item.Properties props) {
		super(props);
	}

	@Override
	public void appendHoverText(ItemStack stack, Item.TooltipContext context, TooltipDisplay display, Consumer<Component> list, TooltipFlag flag) {
		super.appendHoverText(stack, context, display, list, flag);
		if (context.level() != null) {
			Entity entity = EntityType.loadEntityRecursive(SupernaturalManager.getSoulTag(stack), context.level(), EntitySpawnReason.LOAD, o -> o);
			if (entity != null) {
				if (entity.hasCustomName()) {
					list.accept(Component.literal(entity.getName().getString()).withStyle(ChatFormatting.GOLD));
				} else {
					list.accept(Component.translatable(entity.getType().toString()).withStyle(ChatFormatting.GOLD));
				}
			}
			list.accept(Component.translatable(SupernaturalManager.getSoulgem(stack)).withStyle(ChatFormatting.DARK_PURPLE));
		}
	}

	@Override
	public ItemStack getDefaultInstance() {
		ItemStack stack = super.getDefaultInstance();
		stack.set(SupernaturalData.SOULGEM, SoulgemData.EMPTY);
		return stack;
	}

	@Override
	public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity target, InteractionHand hand) {
		if (player.isCreative() && target instanceof Mob && !target.getType().is(SupernaturalTags.IMMUNITY)) {
			player.setItemInHand(hand, SupernaturalManager.setSoul(new ItemStack(SupernaturalItems.SOULGEM.get()), target));
			return InteractionResult.SUCCESS;
		}
		return InteractionResult.PASS;
	}
}