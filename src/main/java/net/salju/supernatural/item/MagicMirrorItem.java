package net.salju.supernatural.item;

import net.minecraft.world.level.Level;
import net.minecraft.world.item.Vanishable;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionHand;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.BlockPos;

public class MagicMirrorItem extends Item implements Vanishable {
	public MagicMirrorItem(Item.Properties props) {
		super(props);
	}

	@Override
	public int getUseDuration(ItemStack stack) {
		return 42;
	}

	@Override
	public UseAnim getUseAnimation(ItemStack stack) {
		return UseAnim.BLOCK;
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
		player.startUsingItem(hand);
		return InteractionResultHolder.consume(player.getItemInHand(hand));
	}

	@Override
	public void releaseUsing(ItemStack stack, Level world, LivingEntity target, int i) {
		super.releaseUsing(stack, world, target, i);
		if (i <= 10 && target instanceof ServerPlayer ply && world instanceof ServerLevel lvl) {
			ResourceKey<Level> dim = ply.getRespawnDimension();
			if (dim == null) {
				dim = Level.OVERWORLD;
			}
			double x;
			double y;
			double z;
			ServerLevel loc = ply.server.getLevel(dim);
			if (ply.getRespawnPosition() == null) {
				x = world.getLevelData().getXSpawn() + 0.5;
				y = world.getLevelData().getYSpawn();
				z = world.getLevelData().getZSpawn() + 0.5;
			} else {
				x = ply.getRespawnPosition().getX() + 0.5;
				y = ply.getRespawnPosition().getY() + 0.7;
				z = ply.getRespawnPosition().getZ() + 0.5;
			}
			if (loc != null) {
				lvl.playSound(null, ply.blockPosition(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1.0F, (float) (0.8F + (Math.random() * 0.2)));
				lvl.sendParticles(ParticleTypes.PORTAL, ply.getX(), ply.getY(), ply.getZ(), 12, 0.5, 0.5, 0.5, 0.65);
				ply.teleportTo(loc, x, y, z, ply.getYRot(), ply.getXRot());
				loc.playSound(null, BlockPos.containing(x, y, z), SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1.0F, (float) (0.8F + (Math.random() * 0.2)));
				loc.sendParticles(ParticleTypes.PORTAL, x, y, z, 12, 0.5, 0.5, 0.5, 0.65);
			}
			ply.getCooldowns().addCooldown(stack.getItem(), 200);
		}
	}

	@Override
	public void onUseTick(Level world, LivingEntity target, ItemStack stack, int i) {
		super.onUseTick(world, target, stack, i);
		if (world instanceof ServerLevel lvl) {
			if (i <= 1) {
				this.releaseUsing(stack, world, target, i);
			} else {
				lvl.sendParticles(ParticleTypes.PORTAL, target.getX(), target.getY(), target.getZ(), 1, 0.5, 0.5, 0.5, 0.65);
			}
		}
	}
}