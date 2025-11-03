package net.salju.supernatural.events;

import net.salju.supernatural.init.SupernaturalTags;
import net.salju.supernatural.init.SupernaturalMobs;
import net.salju.supernatural.init.SupernaturalItems;
import net.salju.supernatural.init.SupernaturalEffects;
import net.salju.supernatural.init.SupernaturalDamageTypes;
import net.salju.supernatural.init.SupernaturalConfig;
import net.salju.supernatural.block.RitualBlockEntity;
import net.salju.supernatural.compat.Thirst;
import net.salju.supernatural.entity.Spooky;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.ModList;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.neoforge.event.entity.living.*;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Vindicator;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.util.Mth;

@EventBusSubscriber
public class SupernaturalEvents {
	@SubscribeEvent
	public static void onPlayerTick(PlayerTickEvent.Post event) {
		Player player = event.getEntity();
		if (SupernaturalManager.isVampire(player)) {
			player.getFoodData().setSaturation(1);
			player.getFoodData().setFoodLevel(20);
			if (player.level() instanceof ServerLevel lvl) {
				SupernaturalManager.addVampireEffects(player);
				boolean check = (player.isInWaterOrRain() || player.isInPowderSnow || player.wasInPowderSnow || player.isCreative() || SupernaturalConfig.SUN.get() || player.hasEffect(MobEffects.FIRE_RESISTANCE));
				if (lvl.isBrightOutside() && lvl.canSeeSky(BlockPos.containing(player.getX(), player.getEyeY(), player.getZ())) && !check) {
					ItemStack helmet = player.getItemBySlot(EquipmentSlot.HEAD);
					if (helmet.isEmpty()) {
						if (player.getRemainingFireTicks() <= 20) {
							player.setRemainingFireTicks(120);
								player.hurt(SupernaturalDamageTypes.causeSunDamage(player.level().registryAccess()), 3);
						}
					} else if (Mth.nextInt(player.getRandom(), 0, 25) <= 2) {
						helmet.hurtAndBreak(1, player, EquipmentSlot.HEAD);
					}
				}
			}
		}
	}

	@SubscribeEvent
	public static void onHeal(LivingHealEvent event) {
		if (event.getAmount() <= 1.0F && SupernaturalManager.isVampire(event.getEntity())) {
			event.setAmount(event.getAmount() * (event.getEntity().hasEffect(MobEffects.REGENERATION) ? 0.45F : 0.15F));
		}
	}

	@SubscribeEvent
	public static void onRightClickEntity(PlayerInteractEvent.EntityInteract event) {
		if (SupernaturalManager.isVampire(event.getEntity()) && event.getTarget() instanceof LivingEntity target && event.getEntity().isCrouching()) {
			if (!SupernaturalManager.isVampire(target) && target.isSleeping() && (target instanceof Player || target.getType().is(SupernaturalTags.BLOODY))) {
				event.getEntity().heal(6.0F);
				if (ModList.get().isLoaded("thirst")) {
					Thirst.vampireBite(event.getEntity());
				}
				if (Math.random() <= SupernaturalConfig.BITE.get()) {
					target.hurt(target.damageSources().source(DamageTypes.PLAYER_ATTACK, event.getEntity()), 3.0F);
					if (target instanceof Player) {
						target.addEffect(new MobEffectInstance(SupernaturalEffects.VAMPIRISM, 24000, 0));
					}
				}
				event.setCanceled(true);
			}
		}
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
		if (event.getEntity().hasEffect(SupernaturalEffects.POSSESSION)) {
			event.setCanceled(true);
		}
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
		if (event.getEntity().hasEffect(SupernaturalEffects.POSSESSION)) {
			event.setCanceled(true);
		}
	}

	@SubscribeEvent
	public static void onUseItemFinish(LivingEntityUseItemEvent.Finish event) {
		if (event.getEntity() instanceof Player player && SupernaturalManager.isVampire(player)) {
			if (event.getItem().is(Items.ENCHANTED_GOLDEN_APPLE) && player.getOffhandItem().is(Items.TOTEM_OF_UNDYING)) {
				player.level().broadcastEntityEvent(player, (byte) 35);
				SupernaturalManager.setVampire(player, false);
				if (!player.isCreative()) {
					player.getOffhandItem().shrink(1);
				}
			}
		}
	}

	@SubscribeEvent
	public static void onEffectAdded(MobEffectEvent.Applicable event) {
		if (SupernaturalManager.isVampire(event.getEntity())) {
			if (event.getEffectInstance().is(MobEffects.POISON) || event.getEffectInstance().is(MobEffects.HUNGER)) {
				event.setResult(MobEffectEvent.Applicable.Result.DO_NOT_APPLY);
			}
		}
	}

	@SubscribeEvent
	public static void onEffectRemoval(MobEffectEvent.Remove event) {
		if (event.getEffectInstance() != null) {
			if (event.getEffectInstance().is(SupernaturalEffects.POSSESSION) && event.getEntity().level() instanceof ServerLevel lvl) {
				SupernaturalMobs.SPOOKY.get().spawn(lvl, event.getEntity().blockPosition(), EntitySpawnReason.MOB_SUMMONED);
			}
		}
	}

	@SubscribeEvent
	public static void onAttacked(LivingIncomingDamageEvent event) {
		LivingEntity target = event.getEntity();
		if (event.getSource().getDirectEntity() instanceof LivingEntity source) {
			ItemStack weapon = source.getMainHandItem();
			if (SupernaturalManager.isVampire(target)) {
				int i = SupernaturalManager.getEnchantmentLevel(weapon, target.level(), "minecraft", "smite");
				if (weapon.is(Items.WOODEN_SWORD) && target.getHealth() <= target.getMaxHealth() * SupernaturalConfig.WOOD.get().floatValue()) {
					event.setAmount(Float.MAX_VALUE);
					if (target.level() instanceof ServerLevel lvl) {
						EntityType.BAT.spawn(lvl, target.blockPosition(), EntitySpawnReason.MOB_SUMMONED);
					}
				} else if (i > 0) {
					if (target instanceof Player) {
						event.setAmount(event.getAmount() + ((float) i * 2.5F));
					}
				} else if (SupernaturalConfig.DR.get() < 1.0) {
					event.setAmount(event.getAmount() * SupernaturalConfig.DR.get().floatValue());
				}
			}
			if (SupernaturalManager.isVampire(source)&& !SupernaturalManager.isVampire(target) && !target.getType().is(EntityTypeTags.UNDEAD) && !target.getType().is(SupernaturalTags.IMMUNITY)) {
				source.heal(SupernaturalConfig.LEECH.get().floatValue() + 1.25F);
				if (source instanceof Player player && SupernaturalConfig.BLOOD.get() && (target instanceof Player || target.getType().is(SupernaturalTags.BLOODY))) {
					ItemStack stack = player.getOffhandItem();
					if (stack.is(Items.GLASS_BOTTLE)) {
						if (!player.isCreative()) {
							stack.shrink(1);
						}
						ItemStack blood = new ItemStack(SupernaturalItems.BLOOD.get());
						if (!player.getInventory().add(blood)) {
							player.drop(blood, false);
						}
					}
				}
			}
		}
	}

	@SubscribeEvent
	public static void onDeath(LivingDeathEvent event) {
		LivingEntity target = event.getEntity();
		if (target.level() instanceof ServerLevel lvl) {
			if (target.hasEffect(SupernaturalEffects.POSSESSION)) {
				Spooky ghost = SupernaturalMobs.SPOOKY.get().spawn(lvl, target.blockPosition(), EntitySpawnReason.MOB_SUMMONED);
				if (ghost != null) {
					ghost.setPersistenceRequired();
				}
			} else if (lvl.dimensionType().natural() && target instanceof Mob && !target.getType().is(SupernaturalTags.IMMUNITY)) {
				RitualBlockEntity block = SupernaturalManager.getAltar(target.blockPosition(), lvl, 12, Items.AMETHYST_SHARD);
				if (block != null) {
					block.setItem(0, SupernaturalManager.setSoul(new ItemStack(SupernaturalItems.SOULGEM.get()), target));
					lvl.sendParticles(ParticleTypes.SOUL, (block.getBlockPos().getX() + 0.5), (block.getBlockPos().getY() + 0.5), (block.getBlockPos().getZ() + 0.5), 6, 0.1, 0.15, 0.1, 0);
					lvl.sendParticles(ParticleTypes.SOUL, (target.getX() + 0.5), (target.getY() + 0.5), (target.getZ() + 0.5), 8, 0.25, 0.35, 0.25, 0);
					target.getPersistentData().putBoolean("SoulTrapped", true);
				}
			}
		}
	}

	@SubscribeEvent
	public static void onDrops(LivingDropsEvent event) {
		LivingEntity target = event.getEntity();
		if (target.level() instanceof ServerLevel) {
			if (target.getPersistentData().getBoolean("SoulTrapped").isPresent()) {
				event.setCanceled(target.getPersistentData().getBoolean("SoulTrapped").get());
			}
		}
	}

	@SubscribeEvent
	public static void onXpDrops(LivingExperienceDropEvent event) {
		LivingEntity target = event.getEntity();
		if (target.level() instanceof ServerLevel) {
			if (target.getPersistentData().getBoolean("SoulTrapped").isPresent()) {
				event.setCanceled(target.getPersistentData().getBoolean("SoulTrapped").get());
			}
		}
	}

    @SubscribeEvent
    public static void onAttackTarget(LivingChangeTargetEvent event) {
        if (event.getEntity() instanceof IronGolem && event.getOriginalAboutToBeSetTarget() != null) {
            if (event.getOriginalAboutToBeSetTarget().getType().is(SupernaturalTags.IGNORES)) {
                event.setCanceled(true);
            }
        }
    }

	@SubscribeEvent
	public static void onEntitySpawned(EntityJoinLevelEvent event) {
		if (!event.loadedFromDisk()) {
			if (event.getLevel() instanceof ServerLevel lvl && event.getEntity() instanceof Vindicator target) {
				if (SupernaturalConfig.RAIDERS.get() && lvl.isMoonVisible() && target.getCurrentRaid() != null && !(target.isPassenger() || target.isPatrolLeader())) {
					if (Math.random() <= SupernaturalConfig.VAMPIRER.get()) {
						SupernaturalMobs.VAMPIRE.get().spawn(lvl, target.blockPosition(), EntitySpawnReason.EVENT);
						target.getCurrentRaid().removeFromRaid(lvl, target, true);
						event.setCanceled(true);
					}
				}
			}
		}
	}
}