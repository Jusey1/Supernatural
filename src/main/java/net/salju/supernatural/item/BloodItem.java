package net.salju.supernatural.item;

import net.salju.supernatural.events.SupernaturalManager;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionHand;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import java.util.UUID;
import java.util.List;

public class BloodItem extends Item {
	public BloodItem(Item.Properties props) {
		super(props);
	}

	@Override
	public void appendHoverText(ItemStack stack, Level world, List<Component> list, TooltipFlag flag) {
		super.appendHoverText(stack, world, list, flag);
		UUID target = SupernaturalManager.getUUID(stack);
		if (target != null && world != null) {
			Player player = world.getPlayerByUUID(target);
			if (player != null) {
				list.add(Component.literal(world.getPlayerByUUID(target).getName().getString()).withStyle(ChatFormatting.DARK_RED));
			}
		}
	}

	@Override
	public int getUseDuration(ItemStack stack) {
		return 40;
	}

	@Override
	public boolean hasCraftingRemainingItem() {
		return true;
	}

	@Override
	public ItemStack getCraftingRemainingItem(ItemStack stack) {
		return new ItemStack(Items.GLASS_BOTTLE);
	}

	@Override
	public UseAnim getUseAnimation(ItemStack stack) {
		return UseAnim.DRINK;
	}

	@Override
	public SoundEvent getDrinkingSound() {
		return SoundEvents.HONEY_DRINK;
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
		if (SupernaturalManager.isVampire(player)) {
			return ItemUtils.startUsingInstantly(world, player, hand);
		}
		return super.use(world, player, hand);
	}

	@Override
	public ItemStack finishUsingItem(ItemStack stack, Level world, LivingEntity target) {
		super.finishUsingItem(stack, world, target);
		if (SupernaturalManager.isVampire(target)) {
			target.heal((float) SupernaturalManager.getBlood(stack));
		}
		if (target instanceof Player player && !player.isCreative()) {
			return new ItemStack(Items.GLASS_BOTTLE);
		}
		return stack;
	}
}