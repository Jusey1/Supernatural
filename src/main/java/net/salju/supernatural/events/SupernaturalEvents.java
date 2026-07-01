package net.salju.supernatural.events;

import net.salju.supernatural.init.*;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.living.*;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.monster.illager.SpellcasterIllager;
import net.minecraft.world.entity.monster.illager.Vindicator;
import net.minecraft.world.entity.monster.Vex;
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
			player.getFoodData().setFoodLevel(18);
			if (player.level() instanceof ServerLevel lvl) {
				SupernaturalManager.addVampireEffects(player);
                ItemStack helmet = player.getItemBySlot(EquipmentSlot.HEAD);
                if (!helmet.is(SupernaturalTags.DARK_ARMOR)) {
                    if (SupernaturalManager.shouldVampireBurn(player, lvl) && !player.isCreative() && !player.isSpectator()) {
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

	@SubscribeEvent
	public static void onUseItemFinish(LivingEntityUseItemEvent.Finish event) {
		if (event.getEntity() instanceof Player player && SupernaturalManager.isVampire(player)) {
            if (event.getItem().has(DataComponents.FOOD)) {
                player.getFoodData().setFoodLevel(0);
            }
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
	public static void onAttacked(LivingIncomingDamageEvent event) {
		LivingEntity target = event.getEntity();
		if (event.getSource().getDirectEntity() instanceof LivingEntity source) {
			ItemStack weapon = source.getMainHandItem();
			if (SupernaturalManager.isVampire(target)) {
				int i = SupernaturalManager.getEnchantmentLevel(weapon, target.level(), "minecraft", "smite");
				if (weapon.is(Items.WOODEN_SWORD) && target.getHealth() <= target.getMaxHealth() * SupernaturalConfig.WOOD.get().floatValue()) {
					event.setAmount(target.getMaxHealth() * 10.0F);
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
            if (SupernaturalManager.isVampire(source) && !SupernaturalManager.isVampire(target)) {
                if (!target.getType().is(EntityTypeTags.UNDEAD) && !target.getType().is(SupernaturalTags.IMMUNITY)) {
                    source.heal(SupernaturalConfig.LEECH.get().floatValue() + 1.25F);
                }
            }
		}
        if (event.getSource().is(SupernaturalTags.MAGIC)) {
            int i = SupernaturalManager.getDarkArmor(target);
            if (i >= 1) {
                event.setAmount(event.getAmount() * (1.0F - (0.1F * i)));
            }
        }
        if (target instanceof SpellcasterIllager && target.level() instanceof ServerLevel lvl) {
            for (Vex ghost : target.level().getEntitiesOfClass(Vex.class, target.getBoundingBox().inflate(32.76))) {
                if (ghost.getOwner() != null && ghost.getOwner() == target) {
                    ghost.hurtServer(lvl, event.getSource(), event.getAmount() * 0.25F);
                    event.setAmount(event.getAmount() * 0.75F);
                    break;
                }
            }
        }
	}

	@SubscribeEvent
	public static void onDeath(LivingDeathEvent event) {
		LivingEntity target = event.getEntity();
		if (target.level() instanceof ServerLevel lvl) {
            if (target instanceof Mob && !target.getType().is(SupernaturalTags.IMMUNITY)) {
                if (event.getSource().getDirectEntity() instanceof LivingEntity bob && SupernaturalManager.canSoulbind(bob)) {
                    if (bob.getOffhandItem().is(Items.AMETHYST_SHARD)) {
                        lvl.sendParticles(ParticleTypes.SOUL, target.getX() + 0.5, target.getY() + 0.5, target.getZ() + 0.5, 8, 0.25, 0.35, 0.25, 0);
                        if (bob instanceof Player player) {
                            if (!player.isCreative()) {
                                player.getOffhandItem().shrink(1);
                            }
                            player.addItem(SupernaturalManager.setSoul(new ItemStack(SupernaturalItems.SOULGEM.get()), target));
                        } else {
                            bob.setItemSlot(EquipmentSlot.OFFHAND, SupernaturalManager.setSoul(new ItemStack(SupernaturalItems.SOULGEM.get()), target));
                        }
                    }
                }
            }
		}
	}

	@SubscribeEvent
	public static void onEntitySpawned(EntityJoinLevelEvent event) {
		if (!event.loadedFromDisk()) {
			if (event.getLevel() instanceof ServerLevel lvl && event.getEntity() instanceof Vindicator target) {
				if (SupernaturalConfig.RAIDERS.get() && lvl.isDarkOutside() && target.getCurrentRaid() != null && !(target.isPassenger() || target.isPatrolLeader())) {
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