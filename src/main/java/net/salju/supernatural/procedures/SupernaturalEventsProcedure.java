package net.salju.supernatural.procedures;

import net.salju.supernatural.init.SupernaturalModMobEffects;
import net.salju.supernatural.init.SupernaturalModEntities;
import net.salju.supernatural.init.SupernaturalItems;
import net.salju.supernatural.init.SupernaturalEnchantments;
import net.salju.supernatural.entity.VampireEntity;
import net.salju.supernatural.entity.SpookyEntity;
import net.salju.supernatural.entity.NewVexEntity;
import net.salju.supernatural.entity.NecromancerEntity;
import net.salju.supernatural.entity.MerEmeraldEntity;
import net.salju.supernatural.entity.MerDiamondEntity;
import net.salju.supernatural.entity.MerAmethystEntity;

import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.EntityStruckByLightningEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.TickEvent;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.raid.Raid;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.monster.Vindicator;
import net.minecraft.world.entity.monster.Vex;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.monster.Evoker;
import net.minecraft.world.entity.item.ItemEntity;
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
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Mth;
import net.minecraft.tags.TagKey;
import net.minecraft.tags.ItemTags;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Registry;
import net.minecraft.core.BlockPos;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.advancements.Advancement;

import java.util.Iterator;

@Mod.EventBusSubscriber
public class SupernaturalEventsProcedure {
	@SubscribeEvent
	public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
		if (event.phase == TickEvent.Phase.END) {
			Player player = event.player;
			ItemStack helmet = player.getItemBySlot(EquipmentSlot.HEAD);
			LevelAccessor world = player.level;
			double x = player.getX();
			double y = player.getY();
			double z = player.getZ();
			if (SupernaturalHelpersProcedure.isVampire(player)) {
				player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 999999, 0, (false), (false)));
				player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 999999, 0, (false), (false)));
				player.addEffect(new MobEffectInstance(MobEffects.DIG_SPEED, 999999, 0, (false), (false)));
				if (!player.getAbilities().instabuild) {
					if ((player.getHealth() < player.getMaxHealth()) && (player.getFoodData().getFoodLevel() >= 16)) {
						player.getFoodData().setSaturation(1);
					} else if (player.isSprinting()) {
						player.getFoodData().setSaturation(1);
					} else {
						player.getFoodData().setSaturation(0);
					}
					if (!world.isClientSide() && world instanceof ServerLevel lvl && lvl.isDay() && world.canSeeSkyFromBelowWater(new BlockPos(x, y, z)) && !world.getLevelData().isThundering() && !world.getLevelData().isRaining()) {
						if (helmet == (ItemStack.EMPTY)) {
							if (player.getRemainingFireTicks() <= 10) {
								player.setSecondsOnFire(3);
								player.hurt(new DamageSource("vampire.sun").bypassArmor(), 4);
							}
						} else if (Mth.nextDouble(RandomSource.create(), 0, 10 * EnchantmentHelper.getItemEnchantmentLevel(Enchantments.UNBREAKING, helmet) + 20) <= 2) {
							if (helmet.hurt(1, RandomSource.create(), null)) {
								helmet.shrink(1);
								helmet.setDamageValue(0);
							}
						}
					} else if (player.isShiftKeyDown()) {
						player.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, 10, 0, (false), (false)));
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
				if (SupernaturalHelpersProcedure.isVampire(player)) {
					if (food.getItem().isEdible() && !food.is(ItemTags.create(new ResourceLocation("supernatural:blood")))) {
						player.getFoodData().setFoodLevel((int) ((player.getFoodData().getFoodLevel()) - (food.getItem().getFoodProperties().getNutrition()) * 2));
					}
				}
			}
		}
	}

	@SubscribeEvent
	public static void onEntityAttacked(LivingAttackEvent event) {
		if (event != null && event.getEntity() != null) {
			LivingEntity target = event.getEntity();
			LevelAccessor world = target.level;
			double x = target.getX();
			double y = target.getY();
			double z = target.getZ();
			if (event.getSource().getDirectEntity() != null) {
				Entity damage = event.getSource().getDirectEntity();
				if (damage instanceof LivingEntity source) {
					ItemStack weapon = source.getMainHandItem();
					if (SupernaturalHelpersProcedure.isVampire(target) || target.getType().is(TagKey.create(Registry.ENTITY_TYPE_REGISTRY, new ResourceLocation("supernatural:is_vampire")))) {
						if (weapon.getItem() == Items.WOODEN_SWORD) {
							if (target.getHealth() <= (target.getMaxHealth() * 0.6)) {
								target.hurt(new DamageSource("vampire.wood").bypassArmor(), 999);
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
						if (SupernaturalHelpersProcedure.isVampire(source) || source.getType().is(TagKey.create(Registry.ENTITY_TYPE_REGISTRY, new ResourceLocation("supernatural:is_vampire")))) {
							ItemStack bottle = new ItemStack(Items.GLASS_BOTTLE);
							if (target instanceof Player player && !(SupernaturalHelpersProcedure.isVampire(player))) {
								if (!player.hasEffect(SupernaturalModMobEffects.VAMPIRISM.get()) && (player.getArmorValue() < 12)) {
									player.addEffect(new MobEffectInstance(SupernaturalModMobEffects.VAMPIRISM.get(), 24000, 0, (false), (false)));
									player.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 120, 0, (false), (false)));
								}
								if (source.getOffhandItem().getItem() == Items.GLASS_BOTTLE && (!player.hasEffect(SupernaturalModMobEffects.BLEEDING.get()))) {
									player.addEffect(new MobEffectInstance(SupernaturalModMobEffects.BLEEDING.get(), 1200, 0));
									if (source instanceof Player vampyre) {
										vampyre.getInventory().clearOrCountMatchingItems(p -> bottle.getItem() == p.getItem(), 1, vampyre.inventoryMenu.getCraftSlots());
										ItemStack blood = new ItemStack(SupernaturalItems.PLAYER_BLOOD.get());
										blood.setCount(1);
										ItemHandlerHelper.giveItemToPlayer(vampyre, blood);
									}
								}
							} else if (source instanceof Player vampyre) {
								if (vampyre.getOffhandItem().getItem() == Items.GLASS_BOTTLE && (!target.hasEffect(SupernaturalModMobEffects.BLEEDING.get()))) {
									if (target instanceof Villager) {
										target.addEffect(new MobEffectInstance(SupernaturalModMobEffects.BLEEDING.get(), 800, 0));
										vampyre.getInventory().clearOrCountMatchingItems(p -> bottle.getItem() == p.getItem(), 1, vampyre.inventoryMenu.getCraftSlots());
										ItemStack blood = new ItemStack(SupernaturalItems.VILLAGER_BLOOD.get());
										blood.setCount(1);
										ItemHandlerHelper.giveItemToPlayer(vampyre, blood);
									} else if (target instanceof Animal) {
										target.addEffect(new MobEffectInstance(SupernaturalModMobEffects.BLEEDING.get(), 400, 0));
										vampyre.getInventory().clearOrCountMatchingItems(p -> bottle.getItem() == p.getItem(), 1, vampyre.inventoryMenu.getCraftSlots());
										ItemStack blood = new ItemStack(SupernaturalItems.ANIMAL_BLOOD.get());
										blood.setCount(1);
										ItemHandlerHelper.giveItemToPlayer(vampyre, blood);
									}
								}
							}
							int bleed = EnchantmentHelper.getItemEnchantmentLevel(SupernaturalEnchantments.LEECHING.get(), (weapon));
							if (!(target.getMobType() == MobType.UNDEAD) && (bleed > 0)) {
								if (!target.hasEffect(SupernaturalModMobEffects.BLEEDING.get())) {
									source.setHealth((float) (source.getHealth() + (bleed * 0.5)));
									if (source instanceof Player vampyre) {
										if (vampyre.getFoodData().getFoodLevel() < 20) {
											vampyre.getFoodData().setFoodLevel((int) ((vampyre.getFoodData().getFoodLevel()) + bleed * 1));
										}
									}
									target.addEffect(new MobEffectInstance(SupernaturalModMobEffects.BLEEDING.get(), 160, 0));
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
			LevelAccessor world = target.level;
			double x = target.getX();
			double y = target.getY();
			double z = target.getZ();
			if (target instanceof Player player && SupernaturalHelpersProcedure.isVampire(player)) {
				if (player.getInventory().contains(new ItemStack(Items.TOTEM_OF_UNDYING))) {
					ItemStack totem = new ItemStack(Items.TOTEM_OF_UNDYING);
					SupernaturalHelpersProcedure.setVampire(player, false);
					player.removeEffect(MobEffects.DAMAGE_BOOST);
					player.removeEffect(MobEffects.DIG_SPEED);
					player.removeEffect(MobEffects.MOVEMENT_SPEED);
					player.getInventory().clearOrCountMatchingItems(p -> totem.getItem() == p.getItem(), 1, player.inventoryMenu.getCraftSlots());
					player.level.broadcastEntityEvent(player, (byte) 35);
				}
			}
		}
	}

	@SubscribeEvent
	public static void onEntityDrops(LivingDropsEvent event) {
		LivingEntity target = event.getEntity();
		LevelAccessor world = target.level;
		double x = target.getX();
		double y = target.getY();
		double z = target.getZ();
		int loot = event.getLootingLevel();
		if (target.hasEffect(SupernaturalModMobEffects.POSSESSION.get())) {
			if (!world.isClientSide() && world instanceof ServerLevel lvl) {
				Mob ghost = new SpookyEntity(SupernaturalModEntities.SPOOKY.get(), lvl);
				ghost.copyPosition(target);
				ghost.finalizeSpawn(lvl, world.getCurrentDifficultyAt(ghost.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
				world.addFreshEntity(ghost);
				if (target instanceof Slime slm && slm.isTiny() && (Math.random() <= 0.35)) {
					event.setCanceled(true);
					for (int slym = 0; slym < (int) (Mth.nextInt(RandomSource.create(), 1, (1 + loot))); slym++) {
						ItemEntity slime = new ItemEntity(lvl, x, y, z, (new ItemStack(SupernaturalItems.ECTOPLASM.get())));
						slime.setPickUpDelay(10);
						lvl.addFreshEntity(slime);
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
		if (!world.isClientSide() && world instanceof ServerLevel lvl) {
			if (target instanceof Vindicator raidyr && !(lvl.isDay()) && !(raidyr.isPassenger()) && !(raidyr.isPatrolLeader())) {
				Raid raid = raidyr.getCurrentRaid();
				if (raid != null && (Math.random() <= 0.25)) {
					Mob vampire = new VampireEntity(SupernaturalModEntities.VAMPIRE.get(), lvl);
					vampire.copyPosition(raidyr);
					vampire.finalizeSpawn(lvl, world.getCurrentDifficultyAt(vampire.blockPosition()), MobSpawnType.EVENT, null, null);
					world.addFreshEntity(vampire);
					raid.removeFromRaid(raidyr, true);
					event.setCanceled(true);
				}
			} else if (target instanceof Evoker raidyr && !(lvl.isDay()) && !(raidyr.isPassenger()) && !(raidyr.isPatrolLeader())) {
				Raid raid = raidyr.getCurrentRaid();
				if (raid != null && (Math.random() <= 0.15)) {
					if (!(!world.getEntitiesOfClass(NecromancerEntity.class, AABB.ofSize(new Vec3(x, y, z), 64, 64, 64), e -> true).isEmpty())) {
						Mob vampire = new NecromancerEntity(SupernaturalModEntities.NECROMANCER.get(), lvl);
						vampire.copyPosition(raidyr);
						vampire.finalizeSpawn(lvl, world.getCurrentDifficultyAt(vampire.blockPosition()), MobSpawnType.EVENT, null, null);
						world.addFreshEntity(vampire);
						raid.removeFromRaid(raidyr, true);
						event.setCanceled(true);
					}
				}
			} else if (target instanceof Vex && !(target instanceof NewVexEntity)) {
				Mob ghost = new NewVexEntity(SupernaturalModEntities.NEW_VEX.get(), lvl);
				ghost.copyPosition(target);
				ghost.finalizeSpawn(lvl, world.getCurrentDifficultyAt(ghost.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
				world.addFreshEntity(ghost);
				event.setCanceled(true);
			} else if (target instanceof MerAmethystEntity merry && (merry.merTick = true)) {
				if (Math.random() >= 0.85) {
					Mob mer = new MerDiamondEntity(SupernaturalModEntities.MER_DIAMOND.get(), lvl);
					mer.copyPosition(target);
					mer.finalizeSpawn(lvl, world.getCurrentDifficultyAt(mer.blockPosition()), MobSpawnType.NATURAL, null, null);
					world.addFreshEntity(mer);
					event.setCanceled(true);
				} else if (Math.random() >= 0.65) {
					Mob mer = new MerEmeraldEntity(SupernaturalModEntities.MER_EMERALD.get(), lvl);
					mer.copyPosition(target);
					mer.finalizeSpawn(lvl, world.getCurrentDifficultyAt(mer.blockPosition()), MobSpawnType.NATURAL, null, null);
					world.addFreshEntity(mer);
					event.setCanceled(true);
				}
			}
		}
	}
}
