package net.salju.supernatural.events;

import net.salju.supernatural.network.UsedContract;
import net.salju.supernatural.item.DrinkableBloodItem;
import net.salju.supernatural.init.SupernaturalTags;
import net.salju.supernatural.init.SupernaturalMobs;
import net.salju.supernatural.init.SupernaturalItems;
import net.salju.supernatural.init.SupernaturalEnchantments;
import net.salju.supernatural.init.SupernaturalEffects;
import net.salju.supernatural.init.SupernaturalConfig;
import net.salju.supernatural.entity.MerA;
import net.salju.supernatural.block.RitualBlockEntity;
import net.salju.supernatural.SupernaturalMod;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.living.MobEffectEvent;
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
import net.minecraft.world.entity.monster.SpellcasterIllager;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.monster.Evoker;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.damagesource.CombatRules;
import net.minecraft.util.Mth;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.BlockPos;

@Mod.EventBusSubscriber
public class SupernaturalEvents {
	@SubscribeEvent
	public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
		if (event.phase == TickEvent.Phase.END) {
			Player player = event.player;
			if (player.level() instanceof ServerLevel lvl) {
				if (SupernaturalManager.isVampire(player)) {
					SupernaturalManager.addVampireEffects(player);
					if ((player.getHealth() < player.getMaxHealth() && player.getFoodData().getFoodLevel() >= 16) || player.isSprinting()) {
						player.getFoodData().setSaturation(1);
					} else {
						player.getFoodData().setSaturation(0);
					}
					boolean check = (player.isInWaterRainOrBubble() || player.isInPowderSnow || player.wasInPowderSnow || player.isCreative());
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
				} else if (SupernaturalManager.isWerewolf(player)) {
					SupernaturalManager.addWerewolfEffects(player);
					if (player.hasEffect(MobEffects.POISON)) {
						player.getFoodData().setFoodLevel(1);
					} else if (player.hasEffect(MobEffects.HUNGER)) {
						player.getFoodData().setFoodLevel(8);
					} else {
						player.getFoodData().setFoodLevel(18);
					}
					player.getFoodData().setSaturation(1);
					if (lvl.getMoonPhase() == 0 && !lvl.isDay()) {
						if (player.isSleeping() && player.getSleepTimer() >= 15) {
							player.stopSleeping();
							player.displayClientMessage(Component.translatable("gui.supernatural.werewolf_sleep"), true);
						}
					}
				}
			}
		}
	}

	@SubscribeEvent
	public static void onPlayerClone(PlayerEvent.Clone event) {
		if (SupernaturalManager.isVampire(event.getOriginal())) {
			SupernaturalManager.setVampire(event.getEntity(), true);
		} else if (SupernaturalManager.isWerewolf(event.getOriginal())) {
			SupernaturalManager.setWerewolf(event.getEntity(), true);
		}
	}

	@SubscribeEvent
	public static void onHeal(LivingHealEvent event) {
		if (event.getAmount() <= 1.0F) {
			if (event.getEntity().hasEffect(SupernaturalEffects.SUPERNATURAL.get())) {
				event.setAmount(event.getAmount() * (event.getEntity().hasEffect(MobEffects.REGENERATION) ? 0.5F : 0.1F));
			}
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
	public static void onRightClickItem(PlayerInteractEvent.RightClickItem event) {
		Player player = event.getEntity();
		ItemStack stack = event.getItemStack();
		if (!player.hasEffect(SupernaturalEffects.SUPERNATURAL.get())) {
			if (player.level() instanceof ServerLevel lvl) {
				if (lvl.getMoonPhase() == 0 && !lvl.isDay() && SupernaturalManager.getPack(player, 8) && stack.is(Items.TOTEM_OF_UNDYING)) {
					lvl.playSound(null, player.blockPosition(), SoundEvents.TOTEM_USE, SoundSource.PLAYERS, 1.0F, (float) (0.8F + (Math.random() * 0.2)));
					if (player instanceof ServerPlayer ply) {
						SupernaturalMod.sendToClientPlayer(new UsedContract(stack), ply);
					}
					if (!player.isCreative()) {
						stack.shrink(1);
					}
					SupernaturalManager.setWerewolf(player, true);
				}
			}
		}
	}

	@SubscribeEvent
	public static void onUseItemFinish(LivingEntityUseItemEvent.Finish event) {
		ItemStack stack = event.getItem();
		if (event.getEntity() instanceof Player player && stack.isEdible() && player.level() instanceof ServerLevel lvl) {
			if (stack.is(Items.ENCHANTED_GOLDEN_APPLE) && player.getOffhandItem().is(Items.TOTEM_OF_UNDYING) && player.hasEffect(SupernaturalEffects.SUPERNATURAL.get())) {
				lvl.playSound(null, player.blockPosition(), SoundEvents.TOTEM_USE, SoundSource.PLAYERS, 1.0F, (float) (0.8F + (Math.random() * 0.2)));
				if (player instanceof ServerPlayer ply) {
					SupernaturalMod.sendToClientPlayer(new UsedContract(player.getOffhandItem()), ply);
				}
				if (!player.isCreative()) {
					player.getOffhandItem().shrink(1);
				}
				if (SupernaturalManager.isVampire(player)) {
					SupernaturalManager.setVampire(player, false);
				} else if (SupernaturalManager.isWerewolf(player)) {
					SupernaturalManager.setWerewolf(player, false);
				}
			} else if (SupernaturalManager.isVampire(player) && !(stack.getItem() instanceof DrinkableBloodItem)) {
				player.getFoodData().setFoodLevel(player.getFoodData().getFoodLevel() - (stack.getFoodProperties(player).getNutrition() * 2));
			} else if (SupernaturalManager.isWerewolf(player) && stack.getFoodProperties(player).isMeat()) {
				player.setHealth(player.getHealth() + (stack.getFoodProperties(player).getNutrition() / 2.0F));
			}
		}
	}

	@SubscribeEvent
	public static void onEffect(MobEffectEvent.Applicable event) {
		if (SupernaturalManager.isWerewolf(event.getEntity()) && event.getEffectInstance().getEffect().getCategory() == MobEffectCategory.BENEFICIAL) {
			for (TamableAnimal animal : event.getEntity().level().getEntitiesOfClass(TamableAnimal.class, event.getEntity().getBoundingBox().inflate(24.85D))) {
				if (animal.isOwnedBy(event.getEntity())) {
					animal.addEffect(event.getEffectInstance());
				}
			}
		}
	}

	@SubscribeEvent
	public static void onEntityAttacked(LivingHurtEvent event) {
		if (event != null && event.getEntity() != null) {
			LivingEntity target = event.getEntity();
			if (target instanceof SpellcasterIllager) {
				for (Vex bobs : target.level().getEntitiesOfClass(Vex.class, target.getBoundingBox().inflate(18.76D))) {
					if (bobs.getOwner() == target) {
						bobs.hurt(event.getSource(), event.getAmount() * 0.45F);
						event.setAmount(event.getAmount() * 0.3F);
					}
				}
			} else if (target instanceof TamableAnimal animal && animal.getOwner() != null && SupernaturalManager.isWerewolf(animal.getOwner())) {
				if (target.blockPosition().closerThan(animal.getOwner().blockPosition(), 28)) {
					float d = CombatRules.getDamageAfterAbsorb(event.getAmount(), animal.getOwner().getArmorValue(), (float) animal.getOwner().getAttributeValue(Attributes.ARMOR_TOUGHNESS));
					int k = EnchantmentHelper.getDamageProtection(animal.getOwner().getArmorSlots(), event.getSource());
					if (k > 0) {
						d = CombatRules.getDamageAfterMagicAbsorb(d, (float) k);
					}
					event.setAmount(d);
					if (target.level() instanceof ServerLevel lvl) {
						lvl.sendParticles(ParticleTypes.ENCHANTED_HIT, target.getX(), (target.getY() + 0.5), target.getZ(), 8, 2, 1, 2, 0);
					}
				}
			}
			if (event.getSource().getDirectEntity() != null && event.getSource().getDirectEntity() instanceof LivingEntity source) {
				ItemStack weapon = source.getMainHandItem();
				if (SupernaturalManager.isVampire(target) && weapon.getItem() == Items.WOODEN_SWORD) {
					if (target.getHealth() <= (target.getMaxHealth() * SupernaturalConfig.WOOD.get())) {
						event.setAmount(Float.MAX_VALUE);
						if (target.level() instanceof ServerLevel lvl) {
							EntityType.BAT.spawn(lvl, target.blockPosition(), MobSpawnType.MOB_SUMMONED);
						}
					}
				} else if (SupernaturalManager.isWerewolf(target) && weapon.getDescriptionId().contains("iron")) {
					target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 120, 0));
					event.setAmount(event.getAmount() * (1.0F + ((float) SupernaturalConfig.IRON.get() / 100)));
				}
				if (!(target.getMobType() == MobType.UNDEAD) && !SupernaturalManager.isVampire(target) && !target.getType().is(SupernaturalTags.IMMUNITY)) {
					int i = EnchantmentHelper.getItemEnchantmentLevel(SupernaturalEnchantments.LEECHING.get(), weapon);
					source.setHealth(source.getHealth() + ((float) i * SupernaturalConfig.LEECH.get()));
					if (source instanceof Player vampyre && SupernaturalManager.isVampire(vampyre)) {
						if (vampyre.getFoodData().getFoodLevel() < 20) {
							vampyre.getFoodData().setFoodLevel(vampyre.getFoodData().getFoodLevel() + 1 + (i * 1));
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
					for (int i = 0; i < Mth.nextInt(target.getRandom(), 1, (1 + event.getLootingLevel())); i++) {
						target.spawnAtLocation(new ItemStack(SupernaturalItems.ECTOPLASM.get()));
					}
				}
			} else {
				RitualBlockEntity block = SupernaturalManager.getAltar(target.blockPosition(), lvl, 12, Items.AMETHYST_SHARD);
				if (block != null && target instanceof Mob && !target.getType().is(SupernaturalTags.IMMUNITY)) {
					block.setItem(0, SupernaturalManager.setSoul(new ItemStack(SupernaturalItems.SOULGEM.get()), target));
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
						if (raidyr instanceof Vindicator && Math.random() <= 0.15) {
							SupernaturalMobs.VAMPIRE.get().spawn(lvl, pos, MobSpawnType.EVENT);
							raid.removeFromRaid(raidyr, true);
							event.setCanceled(true);
						} else if (raidyr instanceof Evoker && Math.random() <= 0.05) {
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