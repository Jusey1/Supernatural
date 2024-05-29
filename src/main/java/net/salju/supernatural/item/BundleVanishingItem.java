package net.salju.supernatural.item;

import net.salju.supernatural.events.SupernaturalManager;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.Vanishable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.RelativeMovement;
import net.minecraft.world.entity.Entity;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.BlockPos;
import java.util.stream.Stream;

public class BundleVanishingItem extends Item {
	public BundleVanishingItem(Item.Properties props) {
		super(props);
	}

	@Override
	public boolean onEntityItemUpdate(ItemStack stack, ItemEntity target) {
		if (isBomb(stack)) {
			if (target.level() instanceof ServerLevel lvl) {
				lvl.sendParticles(ParticleTypes.PORTAL, target.getX(), target.getY(), target.getZ(), 2, 0.5, 0.5, 0.5, 0.65);
			}
		}
		return super.onEntityItemUpdate(stack, target);
	}

	@Override
	public void inventoryTick(ItemStack stack, Level world, Entity target, int e, boolean check) {
		super.inventoryTick(stack, world, target, e, check);
		if (isBomb(stack)) {
			stack.shrink(1);
			if (world instanceof ServerLevel lvl) {
				ServerLevel loc = lvl.getServer().getLevel(Level.END);
				lvl.playSound(null, target.blockPosition(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.AMBIENT, 1.0F, (float) (0.8F + (Math.random() * 0.2)));
				lvl.sendParticles(ParticleTypes.PORTAL, target.getX(), target.getY(), target.getZ(), 12, 0.5, 0.5, 0.5, 0.65);
				if (target instanceof ServerPlayer ply) {
					ply.teleportTo(loc, ply.getX(), 0, ply.getZ(), ply.getYRot(), ply.getXRot());
				} else {
					target.teleportTo(loc, target.getX(), 0, target.getZ(), RelativeMovement.ALL, target.getYRot(), target.getXRot());
				}
				loc.playSound(null, BlockPos.containing(target.getX(), 0, target.getZ()), SoundEvents.ENDERMAN_TELEPORT, SoundSource.AMBIENT, 1.0F, (float) (0.8F + (Math.random() * 0.2)));
				loc.sendParticles(ParticleTypes.PORTAL, target.getX(), 0, target.getZ(), 12, 0.5, 0.5, 0.5, 0.65);
			}
		}
	}

	@Override
	public boolean overrideStackedOnOther(ItemStack stack, Slot slot, ClickAction action, Player player) {
		if (action != ClickAction.SECONDARY) {
			return false;
		} else {
			ItemStack target = slot.getItem();
			if (target.getItem() instanceof BundleHoldingItem || target.getItem() instanceof BundleVanishingItem) {
				if (getContentWeight(target) <= 0) {
					this.setBomb(stack);
					this.playSound(player, SoundEvents.BUNDLE_INSERT);
					target.shrink(target.getCount());
				}
			} else {
				this.playSound(player, SoundEvents.BUNDLE_INSERT);
				target.shrink(target.getCount());
			}
			return true;
		}
	}

	@Override
	public boolean overrideOtherStackedOnMe(ItemStack stack, ItemStack target, Slot slot, ClickAction action, Player player, SlotAccess acc) {
		if (action != ClickAction.SECONDARY) {
			return false;
		} else if (slot.allowModification(player)) {
			if (target.getItem() instanceof BundleHoldingItem || target.getItem() instanceof BundleVanishingItem) {
				if (getContentWeight(target) <= 0) {
					this.setBomb(stack);
					this.playSound(player, SoundEvents.BUNDLE_INSERT);
					target.shrink(target.getCount());
				}
			} else {
				this.playSound(player, SoundEvents.BUNDLE_INSERT);
				target.shrink(target.getCount());
			}
			return true;
		} else {
			return false;
		}
	}

	private ItemStack setBomb(ItemStack stack) {
		CompoundTag tag = stack.getOrCreateTag();
		tag.putBoolean("BundleBomb", true);
		return stack;
	}

	private boolean isBomb(ItemStack stack) {
		CompoundTag tag = stack.getOrCreateTag();
		return tag.getBoolean("BundleBomb");
	}

	private Stream<ItemStack> getContents(ItemStack stack) {
		CompoundTag tag = stack.getTag();
		if (tag == null) {
			return Stream.empty();
		} else {
			ListTag list = tag.getList("Items", 10);
			return list.stream().map(CompoundTag.class::cast).map(ItemStack::of);
		}
	}

	private int getContentWeight(ItemStack stack) {
		if (SupernaturalManager.getSoul(stack) != "") {
			return 256;
		} else {
			return getContents(stack).mapToInt((item) -> {
				return 1 * item.getCount();
			}).sum();
		}
	}

	private void playSound(Entity target, SoundEvent sound) {
		if (target.level() instanceof ServerLevel lvl) {
			lvl.playSound(null, target.blockPosition(), sound, SoundSource.AMBIENT, 1.0F, 0.8F + lvl.getRandom().nextFloat() * 0.4F);
		} else {
			target.playSound(sound, 0.8F, 0.8F + target.level().getRandom().nextFloat() * 0.4F);
		}
	}
}