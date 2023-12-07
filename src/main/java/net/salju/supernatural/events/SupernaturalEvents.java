package net.salju.supernatural.events;

import net.salju.supernatural.item.DrinkableBloodItem;
import net.salju.supernatural.init.SupernaturalTags;
import net.salju.supernatural.init.SupernaturalMobs;
import net.salju.supernatural.init.SupernaturalItems;
import net.salju.supernatural.init.SupernaturalEnchantments;
import net.salju.supernatural.init.SupernaturalEffects;
import net.salju.supernatural.init.SupernaturalConfig;
import net.salju.supernatural.entity.MerA;
import net.salju.supernatural.block.RitualBlockEntity;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.TickEvent;

import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.entity.raid.Raid;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.monster.Vindicator;
import net.minecraft.world.entity.monster.Vex;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.monster.Evoker;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.util.Mth;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.BlockPos;

@Mod.EventBusSubscriber
public class SupernaturalEvents {
	@SubscribeEvent
	public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
		if (event.phase == TickEvent.Phase.END) {
			Player player = event.player;
			if (SupernaturalHelpers.isVampire(player) && player.level() instanceof ServerLevel lvl) {
				SupernaturalHelpers.addVampireEffects(player);
				if (!player.isCreative()) {
					if ((player.getHealth() < player.getMaxHealth() && player.getFoodData().getFoodLevel() >= 16) || player.isSprinting()) {
						player.getFoodData().setSaturation(1);
					} else {
						player.getFoodData().setSaturation(0);
					}
					boolean check = (player.isInWaterRainOrBubble() || player.isInPowderSnow || player.wasInPowderSnow);
					if (lvl.isDay() && lvl.canSeeSky(BlockPos.containing(player.getX(), player.getEyeY(), player.getZ())) && !check && !SupernaturalConfig.SUN.get()) {
						ItemStack helmet = player.getItemBySlot(EquipmentSlot.HEAD);
						if (helmet.isEmpty()) {
							if (player.getRemainingFireTicks() <= 10) {
								player.setSecondsOnFire(3);
								player.hurt(player.damageSources().inFire(), 4);
							}
						} else if (Mth.nextInt(player.getRandom(), 0, 10 * EnchantmentHelper.getItemEnchantmentLevel(Enchantments.UNBREAKING, helmet) + 20) <= 2) {
							if (helmet.hurt(1, player.getRandom(), null)) {
								helmet.shrink(1);
							}
						}
					}
				}
			}
		}
	}

	@SubscribeEvent
	public static void onPlayerClone(PlayerEvent.Clone event) {
		if (SupernaturalHelpers.isVampire(event.getOriginal())) {
			SupernaturalHelpers.setVampire(event.getEntity(), true);
		}
	}

	@SubscribeEvent
	public static void onHeal(LivingHealEvent event) {
		if (SupernaturalHelpers.isVampire(event.getEntity()) && event.getAmount() <= 1.0F) {
			event.setAmount(event.getAmount() * (event.getEntity().hasEffect(MobEffects.REGENERATION) ? 0.5F : 0.1F));
		}
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
		if (event.getEntity().hasEffect(SupernaturalEffects.POSSESSION.get())) {
			event.setCanceled(true);
		}
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
		if (event.getEntity().hasEffect(SupernaturalEffects.POSSESSION.get())) {
			event.setCanceled(true);
		}
	}

	@SubscribeEvent
	public static void onUseItemFinish(LivingEntityUseItemEvent.Finish event) {
		if (event != null && event.getEntity() != null) {
			if (event.getEntity() instanceof Player player && SupernaturalHelpers.isVampire(player)) {
				if (event.getItem().getItem().isEdible() && !(event.getItem().getItem() instanceof DrinkableBloodItem)) {
					player.getFoodData().setFoodLevel(player.getFoodData().getFoodLevel() - (event.getItem().getItem().getFoodProperties().getNutrition() * 2));
				}
			}
		}
	}

	@SubscribeEvent
	public static void onEntityAttacked(LivingHurtEvent event) {
		if (event != null && event.getEntity() != null) {
			LivingEntity target = event.getEntity();
			if (event.getSource().getDirectEntity() != null) {
				Entity damage = event.getSource().getDirectEntity();
				if (damage instanceof LivingEntity source) {
					ItemStack weapon = source.getMainHandItem();
					if (SupernaturalHelpers.isVampire(target) && weapon.getItem() == Items.WOODEN_SWORD) {
						if (target.getHealth() <= (target.getMaxHealth() * SupernaturalConfig.WOOD.get())) {
							event.setAmount(Float.MAX_VALUE);
							if (target.level() instanceof ServerLevel lvl) {
								EntityType.BAT.spawn(lvl, target.blockPosition(), MobSpawnType.MOB_SUMMONED);
							}
						}
					} else if (SupernaturalHelpers.isVampire(source)) {
						if (!(target.getMobType() == MobType.UNDEAD) && !SupernaturalHelpers.isVampire(target) && !target.getType().is(SupernaturalTags.IMMUNITY)) {
							int i = EnchantmentHelper.getItemEnchantmentLevel(SupernaturalEnchantments.LEECHING.get(), weapon);
							source.setHealth(source.getHealth() + ((float) i * SupernaturalConfig.LEECH.get()));
							if (source instanceof Player vampyre) {
								if (vampyre.getFoodData().getFoodLevel() < 20) {
									vampyre.getFoodData().setFoodLevel(vampyre.getFoodData().getFoodLevel() + 1 + (i * 1));
								}
							}
						}
					}
				}
			}
		}
	}

	@SubscribeEvent
	public static void onEntityDrops(LivingDropsEvent event) {
		LivingEntity target = event.getEntity();
		if (target.level() instanceof ServerLevel lvl) {
			if (target.hasEffect(SupernaturalEffects.POSSESSION.get())) {
				SupernaturalMobs.SPOOKY.get().spawn(lvl, target.blockPosition(), MobSpawnType.MOB_SUMMONED);
				if (target instanceof Slime slm && slm.isTiny() && Math.random() <= 0.35) {
					event.setCanceled(true);
					for (int slym = 0; slym < Mth.nextInt(target.getRandom(), 1, (1 + event.getLootingLevel())); slym++) {
						target.spawnAtLocation(new ItemStack(SupernaturalItems.ECTOPLASM.get()));
					}
				}
			} else {
				RitualBlockEntity block = SupernaturalHelpers.getAltar(target.blockPosition(), lvl, 12, Items.AMETHYST_SHARD);
				if (block != null && target instanceof Mob && !target.getType().is(SupernaturalTags.IMMUNITY)) {
					block.setItem(0, SupernaturalHelpers.setSoul(new ItemStack(SupernaturalItems.SOULGEM.get()), target));
					lvl.sendParticles(ParticleTypes.SOUL, (block.getBlockPos().getX() + 0.5), (block.getBlockPos().getY() + 0.5), (block.getBlockPos().getZ() + 0.5), 6, 0.1, 0.15, 0.1, 0);
					lvl.sendParticles(ParticleTypes.SOUL, (target.getX() + 0.5), (target.getY() + 0.5), (target.getZ() + 0.5), 8, 0.25, 0.35, 0.25, 0);
					event.setCanceled(true);
				}
			}
		}
	}

	@SubscribeEvent
	public static void onEntitySpawned(EntityJoinLevelEvent event) {
		Entity target = event.getEntity();
		BlockPos pos = target.blockPosition();
		if (event.getLevel() instanceof ServerLevel lvl) {
			if (target instanceof Raider raidyr && !(lvl.isDay()) && !(raidyr.isPassenger()) && !(raidyr.isPatrolLeader())) {
				if (SupernaturalConfig.RAIDERS.get()) {
					Raid raid = raidyr.getCurrentRaid();
					if (raid != null) {
						if (raidyr instanceof Vindicator && (Math.random() <= 0.15)) {
							SupernaturalMobs.VAMPIRE.get().spawn(lvl, pos, MobSpawnType.EVENT);
							raid.removeFromRaid(raidyr, true);
							event.setCanceled(true);
						} else if (raidyr instanceof Evoker && (Math.random() <= 0.05)) {
							SupernaturalMobs.NECROMANCER.get().spawn(lvl, pos, MobSpawnType.EVENT);
							raid.removeFromRaid(raidyr, true);
							event.setCanceled(true);
						}
					}
				}
			} else if (target instanceof Vex ghost) {
				if (ghost.getOwner() == null) {
					ghost.setBoundOrigin(pos);
				}
				ghost.setLimitedLife(2000);
			} else if (target instanceof MerA && !event.loadedFromDisk()) {
				if (Math.random() >= 0.85) {
					SupernaturalMobs.MER_DIAMOND.get().spawn(lvl, pos, MobSpawnType.NATURAL);
					event.setCanceled(true);
				} else if (Math.random() >= 0.65) {
					SupernaturalMobs.MER_EMERALD.get().spawn(lvl, pos, MobSpawnType.NATURAL);
					event.setCanceled(true);
				}
			}
		}
	}
}