package net.salju.supernatural.item;

import net.salju.supernatural.init.SupernaturalTags;
import net.salju.supernatural.init.SupernaturalItems;
import net.salju.supernatural.init.SupernaturalDamageTypes;
import net.salju.supernatural.events.SupernaturalManager;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.sounds.SoundEvents;

public class TaglockItem extends Item {
	public TaglockItem(Item.Properties props) {
		super(props);
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
	public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity target, InteractionHand hand) {
		if (target.getMobType() == MobType.UNDEAD || SupernaturalManager.isVampire(target) || target.getType().is(SupernaturalTags.IMMUNITY)) {
			return InteractionResult.PASS;
		} else {
			ItemStack blood = SupernaturalManager.setBlood(new ItemStack(SupernaturalItems.BLOOD.get()), target);
			target.hurt(SupernaturalDamageTypes.causeBleedDamage(target.level().registryAccess(), player), SupernaturalManager.isVampire(player) ? 4 : 2);
			player.playSound(SoundEvents.INK_SAC_USE);
			if (!player.isCreative()) {
				player.setItemInHand(hand, blood);
			} else {
				if (!player.getInventory().add(blood)) {
					player.drop(stack, false);
				}
			}
		}
		return InteractionResult.SUCCESS;
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
		if (SupernaturalManager.isVampire(player) || !player.isCrouching()) {
			return super.use(world, player, hand);
		} else {
			ItemStack blood = SupernaturalManager.setBlood(new ItemStack(SupernaturalItems.BLOOD.get()), player);
			player.hurt(SupernaturalDamageTypes.causeBleedDamage(player.level().registryAccess(), player), 2);
			player.playSound(SoundEvents.INK_SAC_USE);
			if (!player.isCreative()) {
				player.setItemInHand(hand, blood);
			} else {
				if (!player.getInventory().add(blood)) {
					player.drop(blood, false);
				}
			}
		}
		return InteractionResultHolder.success(player.getItemInHand(hand));
	}
}