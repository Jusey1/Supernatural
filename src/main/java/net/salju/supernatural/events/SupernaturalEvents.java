package net.salju.supernatural.events;

import net.salju.supernatural.init.*;
import net.salju.supernatural.entity.*;

import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.EntityStruckByLightningEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.TickEvent;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.entity.raid.Raid;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.monster.Vindicator;
import net.minecraft.world.entity.monster.Vex;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.monster.Evoker;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.ambient.Bat;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Mth;
import net.minecraft.tags.TagKey;
import net.minecraft.tags.ItemTags;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.BlockPos;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.advancements.Advancement;

import java.util.Iterator;

@Mod.EventBusSubscriber
public class SupernaturalEvents {
	@SubscribeEvent
	public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
		if (event.phase == TickEvent.Phase.END) {
			Player player = event.player;
			ItemStack helmet = player.getItemBySlot(EquipmentSlot.HEAD);
			LevelAccessor world = player.level();
			double x = player.getX();
			double y = player.getY();
			double z = player.getZ();
			if (SupernaturalHelpers.isVampire(player)) {
				if (SupernaturalConfig.SPEED.get() == true)
					player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 1200, 0, false, false));
				if (SupernaturalConfig.STRENGTH.get() == true)
					player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 1200, 0, false, false));
				if (SupernaturalConfig.HASTE.get() == true)
					player.addEffect(new MobEffectInstance(MobEffects.DIG_SPEED, 1200, 0, false, false));
				if (!player.getAbilities().instabuild) {
					if ((player.getHealth() < player.getMaxHealth()) && (player.getFoodData().getFoodLevel() >= 16)) {
						player.getFoodData().setSaturation(1);
					} else if (player.isSprinting()) {
						player.getFoodData().setSaturation(1);
					} else {
						player.getFoodData().setSaturation(0);
					}
					if (!world.isClientSide() && world instanceof ServerLevel lvl && lvl.isDay() && world.canSeeSkyFromBelowWater(BlockPos.containing(x, y, z)) && !world.getLevelData().isThundering() && !world.getLevelData().isRaining()
							&& (SupernaturalConfig.SUN.get() == false)) {
						if (helmet.isEmpty()) {
							if (player.getRemainingFireTicks() <= 10 && !(player.isInWater())) {
								player.setSecondsOnFire(3);
								player.hurt(player.damageSources().inFire(), 4);
							}
						} else if (Mth.nextDouble(RandomSource.create(), 0, 10 * EnchantmentHelper.getItemEnchantmentLevel(Enchantments.UNBREAKING, helmet) + 20) <= 2) {
							if (helmet.hurt(1, RandomSource.create(), null)) {
								helmet.shrink(1);
								helmet.setDamageValue(0);
							}
						}
					} else if (player.isShiftKeyDown()) {
						player.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, 10, 0, false, false));
					}
				}
			}
		}
	}

	@SubscribeEvent
	public static void onUseItemFinish(LivingEntityUseItemEvent.Finish event) {
		if (event != null && event.getEntity() != null) {
			LivingEntity entity = event.getEntity();
			ItemStack food = event.getItem();
			if (entity instanceof Player player) {
				if (SupernaturalHelpers.isVampire(player)) {
					if (food.getItem().isEdible() && !food.is(ItemTags.create(new ResourceLocation("supernatural:blood")))) {
						player.getFoodData().setFoodLevel((int) ((player.getFoodData().getFoodLevel()) - (food.getItem().getFoodProperties().getNutrition()) * 2));
					}
				}
			}
		}
	}

	@SubscribeEvent
	public static void onEntityAttacked(LivingHurtEvent event) {
		if (event != null && event.getEntity() != null) {
			LivingEntity target = event.getEntity();
			LevelAccessor world = target.level();
			double x = target.getX();
			double y = target.getY();
			double z = target.getZ();
			if (event.getSource().getDirectEntity() != null) {
				Entity damage = event.getSource().getDirectEntity();
				if (damage instanceof LivingEntity source) {
					ItemStack weapon = source.getMainHandItem();
					if (SupernaturalHelpers.isVampire(target) || target.getType().is(TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation("supernatural:is_vampire")))) {
						if (weapon.getItem() == Items.WOODEN_SWORD) {
							if (target.getHealth() <= (target.getMaxHealth() * SupernaturalConfig.WOOD.get())) {
								event.setAmount(Float.MAX_VALUE);
								if (world instanceof ServerLevel lvl) {
									Mob bat = new Bat(EntityType.BAT, lvl);
									bat.copyPosition(target);
									bat.finalizeSpawn(lvl, world.getCurrentDifficultyAt(bat.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
									world.addFreshEntity(bat);
								}
								if (source instanceof ServerPlayer player) {
									Advancement vampire = player.server.getAdvancements().getAdvancement(new ResourceLocation("supernatural:vampire_slayer"));
									AdvancementProgress slayer = player.getAdvancements().getOrStartProgress(vampire);
									if (!slayer.isDone()) {
										Iterator muffins = slayer.getRemainingCriteria().iterator();
										while (muffins.hasNext())
											player.getAdvancements().award(vampire, (String) muffins.next());
									}
								}
							}
						}
					} else if (!target.isBlocking()) {
						if (SupernaturalHelpers.isVampire(source) || source.getType().is(TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation("supernatural:is_vampire")))) {
							ItemStack bottle = new ItemStack(Items.GLASS_BOTTLE);
							if (target instanceof Player player && !(SupernaturalHelpers.isVampire(player))) {
								if (!player.hasEffect(SupernaturalEffects.VAMPIRISM.get()) && (player.getArmorValue() < SupernaturalConfig.VAMPIRE.get())) {
									player.addEffect(new MobEffectInstance(SupernaturalEffects.VAMPIRISM.get(), 24000, 0, false, false));
									player.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 120, 0, false, false));
								}
								if (source.getOffhandItem().getItem() == Items.GLASS_BOTTLE && (!player.hasEffect(SupernaturalEffects.BLEEDING.get()))) {
									player.addEffect(new MobEffectInstance(SupernaturalEffects.BLEEDING.get(), 1200, 0));
									if (source instanceof Player vampyre) {
										vampyre.getInventory().clearOrCountMatchingItems(p -> bottle.getItem() == p.getItem(), 1, vampyre.inventoryMenu.getCraftSlots());
										ItemStack blood = new ItemStack(SupernaturalItems.PLAYER_BLOOD.get());
										blood.setCount(1);
										ItemHandlerHelper.giveItemToPlayer(vampyre, blood);
									}
								}
							} else if (source instanceof Player vampyre) {
								if (vampyre.getOffhandItem().getItem() == Items.GLASS_BOTTLE && (!target.hasEffect(SupernaturalEffects.BLEEDING.get()))) {
									if (target instanceof Villager) {
										target.addEffect(new MobEffectInstance(SupernaturalEffects.BLEEDING.get(), 800, 0));
										vampyre.getInventory().clearOrCountMatchingItems(p -> bottle.getItem() == p.getItem(), 1, vampyre.inventoryMenu.getCraftSlots());
										ItemStack blood = new ItemStack(SupernaturalItems.VILLAGER_BLOOD.get());
										blood.setCount(1);
										ItemHandlerHelper.giveItemToPlayer(vampyre, blood);
									} else if (target instanceof Animal) {
										target.addEffect(new MobEffectInstance(SupernaturalEffects.BLEEDING.get(), 400, 0));
										vampyre.getInventory().clearOrCountMatchingItems(p -> bottle.getItem() == p.getItem(), 1, vampyre.inventoryMenu.getCraftSlots());
										ItemStack blood = new ItemStack(SupernaturalItems.ANIMAL_BLOOD.get());
										blood.setCount(1);
										ItemHandlerHelper.giveItemToPlayer(vampyre, blood);
									}
								}
							}
							int bleed = EnchantmentHelper.getItemEnchantmentLevel(SupernaturalEnchantments.LEECHING.get(), (weapon));
							if (!(target.getMobType() == MobType.UNDEAD) && (bleed > 0)) {
								if (!target.hasEffect(SupernaturalEffects.BLEEDING.get())) {
									source.setHealth((float) (source.getHealth() + (bleed * 0.5)));
									if (source instanceof Player vampyre) {
										if (vampyre.getFoodData().getFoodLevel() < 20) {
											vampyre.getFoodData().setFoodLevel((int) ((vampyre.getFoodData().getFoodLevel()) + bleed * 1));
										}
									}
									target.addEffect(new MobEffectInstance(SupernaturalEffects.BLEEDING.get(), 160, 0));
								}
							}
						}
					}
				}
			}
		}
	}

	@SubscribeEvent
	public static void onEntityStruckByLightning(EntityStruckByLightningEvent event) {
		if (event != null && event.getEntity() != null) {
			Entity target = event.getEntity();
			LevelAccessor world = target.level();
			double x = target.getX();
			double y = target.getY();
			double z = target.getZ();
			if (target instanceof Player player && SupernaturalHelpers.isVampire(player)) {
				if (player.getInventory().contains(new ItemStack(Items.TOTEM_OF_UNDYING))) {
					ItemStack totem = new ItemStack(Items.TOTEM_OF_UNDYING);
					SupernaturalHelpers.setVampire(player, false);
					player.getInventory().clearOrCountMatchingItems(p -> totem.getItem() == p.getItem(), 1, player.inventoryMenu.getCraftSlots());
					player.level().broadcastEntityEvent(player, (byte) 35);
				}
			}
		}
	}

	@SubscribeEvent
	public static void onEntityDrops(LivingDropsEvent event) {
		LivingEntity target = event.getEntity();
		LevelAccessor world = target.level();
		double x = target.getX();
		double y = target.getY();
		double z = target.getZ();
		int loot = event.getLootingLevel();
		BlockPos pos = BlockPos.containing(x, y, z);
		if (target.hasEffect(SupernaturalEffects.POSSESSION.get())) {
			if (!world.isClientSide() && world instanceof ServerLevel lvl) {
				Spooky ghost = SupernaturalMobs.SPOOKY.get().spawn(lvl, pos, MobSpawnType.MOB_SUMMONED);
				if (target instanceof Slime slm && slm.isTiny() && (Math.random() <= 0.35)) {
					event.setCanceled(true);
					for (int slym = 0; slym < (int) (Mth.nextInt(RandomSource.create(), 1, (1 + loot))); slym++) {
						target.spawnAtLocation(new ItemStack(SupernaturalItems.ECTOPLASM.get()));
					}
				}
			}
		}
	}

	@SubscribeEvent
	public static void onEntitySpawned(EntityJoinLevelEvent event) {
		Entity target = event.getEntity();
		LevelAccessor world = event.getLevel();
		double x = target.getX();
		double y = target.getY();
		double z = target.getZ();
		BlockPos pos = BlockPos.containing(x, y, z);
		if (!world.isClientSide() && world instanceof ServerLevel lvl) {
			if (target instanceof Raider raidyr && !(lvl.isDay()) && !(raidyr.isPassenger()) && !(raidyr.isPatrolLeader())) {
				if (SupernaturalConfig.RAIDERS.get() == false) {
					Raid raid = raidyr.getCurrentRaid();
					if (raid != null) {
						if (raidyr instanceof Vindicator && (Math.random() <= 0.15)) {
							Vampire vampire = SupernaturalMobs.VAMPIRE.get().spawn(lvl, pos, MobSpawnType.EVENT);
							raid.removeFromRaid(raidyr, true);
							event.setCanceled(true);
						} else if (raidyr instanceof Evoker && (Math.random() <= 0.05)) {
							Necromancer vampire = SupernaturalMobs.NECROMANCER.get().spawn(lvl, pos, MobSpawnType.EVENT);
							raid.removeFromRaid(raidyr, true);
							event.setCanceled(true);
						}
					}
				}
			} else if (target instanceof Vex ghost) {
				if (ghost.getOwner() == null) {
					ghost.setBoundOrigin(BlockPos.containing(x, y, z));
				}
				ghost.setLimitedLife(2000);
			} else if (target instanceof MerA merry && (merry.merTick = true)) {
				if (Math.random() >= 0.85) {
					MerD mer = SupernaturalMobs.MER_DIAMOND.get().spawn(lvl, pos, MobSpawnType.NATURAL);
					event.setCanceled(true);
				} else if (Math.random() >= 0.65) {
					MerE mer = SupernaturalMobs.MER_EMERALD.get().spawn(lvl, pos, MobSpawnType.NATURAL);
					event.setCanceled(true);
				}
			}
		}
	}
}