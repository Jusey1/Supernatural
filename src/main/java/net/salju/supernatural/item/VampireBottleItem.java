package net.salju.supernatural.item;

import net.salju.supernatural.init.SupernaturalEffects;
import net.salju.supernatural.events.SupernaturalManager;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.level.Level;

public class VampireBottleItem extends Item {
	public VampireBottleItem(Item.Properties props) {
		super(props);
	}

	@Override
    public InteractionResult use(Level world, Player player, InteractionHand hand) {
        return ItemUtils.startUsingInstantly(world, player, hand);
	}

	@Override
	public ItemStack finishUsingItem(ItemStack stack, Level world, LivingEntity target) {
		if (!SupernaturalManager.isVampire(target)) {
            target.addEffect(new MobEffectInstance(SupernaturalEffects.VAMPIRISM, 12000, 0));
		}
        return super.finishUsingItem(stack, world, target);
	}
}