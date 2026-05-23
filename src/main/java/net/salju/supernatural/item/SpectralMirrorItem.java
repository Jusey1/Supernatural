package net.salju.supernatural.item;

import net.salju.supernatural.init.SupernaturalData;
import net.salju.supernatural.block.misc.RitualManager;
import net.salju.supernatural.item.component.AnchorballData;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.level.Level;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;

public class SpectralMirrorItem extends SpectralCoreItem {
	public SpectralMirrorItem(Item.Properties props) {
		super(props);
	}

	@Override
	public int getUseDuration(ItemStack stack, LivingEntity target) {
		return 64;
	}

    @Override
    public ItemUseAnimation getUseAnimation(ItemStack stack) {
        return ItemUseAnimation.BLOCK;
    }

	@Override
	public InteractionResult use(Level world, Player player, InteractionHand hand) {
        AnchorballData data = player.getItemInHand(hand).get(SupernaturalData.ANCHOR.get());
        if (data != null) {
            player.startUsingItem(hand);
            return InteractionResult.CONSUME;
        }
		return super.use(world, player, hand);
	}

	@Override
	public boolean releaseUsing(ItemStack stack, Level world, LivingEntity target, int i) {
        AnchorballData data = stack.get(SupernaturalData.ANCHOR.get());
		if (i <= 10 && target instanceof ServerPlayer ply && world instanceof ServerLevel lvl && RitualManager.canTeleportTo(data, lvl)) {
			RitualManager.teleportUser(data, ply, lvl);
			ply.getCooldowns().addCooldown(stack, 200);
		}
        return super.releaseUsing(stack, world, target, i);
	}

	@Override
	public void onUseTick(Level world, LivingEntity target, ItemStack stack, int i) {
		super.onUseTick(world, target, stack, i);
        if (world.isClientSide()) {
            world.addParticle(ParticleTypes.SOUL_FIRE_FLAME, target.getRandomX(0.5), target.getRandomY(), target.getRandomZ(0.5), 0.0, 0.0, 0.0);
        } else {
            if (i <= 1) {
                this.releaseUsing(stack, world, target, i);
            }
        }
	}
}