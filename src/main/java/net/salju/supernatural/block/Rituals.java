package net.salju.supernatural.block;

import net.salju.supernatural.Supernatural;
import net.salju.supernatural.init.*;
import net.salju.supernatural.entity.Angel;
import net.salju.supernatural.events.SupernaturalManager;
import net.salju.supernatural.item.component.AnchorballData;
import net.salju.supernatural.item.RitualCompassItem;
import net.neoforged.neoforge.event.EventHooks;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.animal.goat.Goat;
import net.minecraft.world.entity.monster.ZombieVillager;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.portal.TeleportTransition;
import net.minecraft.world.level.Spawner;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Rituals {
	public static void doRitual(ItemStack stack, ItemStack offer, ServerLevel lvl, Player player, BlockPos pos) {
		if (lvl.getBlockEntity(pos) instanceof RitualBlockEntity target) {
			if (SupernaturalManager.canRitualsWork(lvl, pos, target)) {
				int i = SupernaturalManager.getPower(lvl, pos);
				int e = SupernaturalManager.getSoulLevel(SupernaturalManager.getSoulgem(offer));
				if (stack.is(SupernaturalItems.CONTRACT.get()) && i == 20 && e >= 5 && !SupernaturalManager.isVampire(player)) {
					Goat goat = getGoat(lvl, new AABB(pos).inflate(SupernaturalConfig.ALTARRANGE.get()));
					if (goat != null || !SupernaturalConfig.SACRIFICE.get()) {
						defaultResult(target, offer, lvl, player, pos);
						player.hurt(SupernaturalDamageTypes.causeRitualDamage(player.level().registryAccess(), player), 0.25F);
						if (player.isAlive()) {
							player.setHealth(1.0F);
						}
						SupernaturalManager.setVampire(player, true);
						if (SupernaturalConfig.SACRIFICE.get()) {
							goat.hurt(SupernaturalDamageTypes.causeRitualDamage(goat.level().registryAccess(), player), Float.MAX_VALUE);
						}
					}
				} else if (stack.is(SupernaturalItems.GRAVE_SOIL.get()) && i == 28 && e >= 0) {
					defaultResult(target, offer, lvl, player, pos);
					if (SupernaturalConfig.SACRIFICE.get()) {
						Mob sacrifice = getSacrifice(lvl, offer, new AABB(pos).inflate(SupernaturalConfig.ALTARRANGE.get()));
						(sacrifice != null ? sacrifice : player).hurt(SupernaturalDamageTypes.causeRitualDamage(player.level().registryAccess(), player), Float.MAX_VALUE);
					}
					summonMob(lvl, pos.above(), offer);
					lvl.playSound(null, pos, SoundEvents.SOUL_ESCAPE.value(), SoundSource.BLOCKS, 1.0F, (float) (0.8F + (Math.random() * 0.2)));
				} else if (stack.is(SupernaturalItems.VAMPIRE_DUST.get())) {
					if (i == 8 && e >= 1) {
						defaultResult(target, offer, lvl, player, pos);
						BlockPos top = BlockPos.containing((pos.getX() + 3), (pos.getY() - 1), (pos.getZ() + 3));
						BlockPos bot = BlockPos.containing((pos.getX() - 3), (pos.getY() - 1), (pos.getZ() - 3));
						for (BlockPos poz : BlockPos.betweenClosed(top, bot)) {
							if (lvl.getBlockState(poz).is(SupernaturalTags.SOIL)) {
								lvl.setBlock(poz, SupernaturalBlocks.GRAVE_SOIL.get().defaultBlockState(), 3);
							}
						}
					} else if (i == 16 && e >= 4) {
						defaultResult(target, offer, lvl, player, pos);
						for (Angel statue : lvl.getEntitiesOfClass(Angel.class, new AABB(pos).inflate(SupernaturalConfig.ALTARRANGE.get()))) {
							if (!statue.isCursed()) {
								statue.getEntityData().set(Angel.CURSED, true);
								lvl.sendParticles(ParticleTypes.SOUL, (statue.getX() + 0.5), (statue.getY() + 0.5), (statue.getZ() + 0.5), 8, 0.25, 0.35, 0.25, 0);
							}
						}
					}
				} else if (stack.is(SupernaturalTags.INGOTS) && i == 12 && e >= 2) {
					defaultResult(target, offer, lvl, player, pos);
					target.setItem(0, new ItemStack(stack.is(Items.IRON_INGOT) ? Items.GOLD_INGOT : Items.IRON_INGOT));
				} else if (stack.is(SupernaturalTags.HELMS) && i == 16 && e >= 3) {
					ItemStack copy = stack.transmuteCopy(getHelmet(stack.getItem()));
					defaultResult(target, offer, lvl, player, pos);
					target.setItem(0, copy);
				} else if (stack.is(SupernaturalTags.MONEY) && i == 16 && e >= 2) {
					ItemStack copy = stack.copy();
					defaultResult(target, offer, lvl, player, pos);
					target.setGreedy(true);
					for (int r = 0; r < 11; ++r) {
						int t = 10 * r;
						Supernatural.queueServerWork(t, () -> {
							if (target.stillValid(player)) {
								if (t >= 100) {
									if (target.getItem(0).is(Items.NETHER_STAR)) {
										for (int p = 0; p < 10; ++p) {
											Supernatural.queueServerWork(10 * p, () -> {
												if (target.stillValid(player)) {
													target.cloneItem(copy.copy());
												}
											});
										}
									}
									target.clearContent();
									target.setGreedy(false);
								} else {
									target.setItem(0, new ItemStack(getGreedyItem(Mth.nextInt(player.getRandom(), 0, 12))));
								}
							}
						});
					}
				} else if (stack.is(SupernaturalItems.ANCHORBALL.get()) && i == 12 && e >= 3) {
					ItemStack copy = stack.copy();
					AnchorballData data = copy.get(SupernaturalData.ANCHOR.get());
					defaultResult(target, offer, lvl, player, pos);
					if (data != null) {
						if (lvl.isInWorldBounds(data.getPos()) && lvl.getPoiManager().existsAtPosition(SupernaturalBlocks.RITUAL_POI.getKey(), data.getPos())) {
							target.setItem(0, copy);
							ServerLevel loc = lvl.getServer().getLevel(data.target().dimension());
							double x = data.getPos().getX() + 0.5;
							double y = data.getPos().getY() + 0.7;
							double z = data.getPos().getZ() + 0.5;
							if (loc != null && player instanceof ServerPlayer ply) {
								lvl.playSound(null, pos, SoundEvents.ENDERMAN_TELEPORT, SoundSource.BLOCKS, 1.0F, (float) (0.8F + (Math.random() * 0.2)));
								lvl.sendParticles(ParticleTypes.PORTAL, pos.getX(), pos.getY() + 0.75, pos.getZ(), 12, 0.5, 0.5, 0.5, 0.65);
								ply.teleport(new TeleportTransition(loc, new Vec3(x, y, z), ply.getDeltaMovement(), ply.getYRot(), ply.getXRot(), TeleportTransition.DO_NOTHING));
								loc.playSound(null, data.getPos(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.BLOCKS, 1.0F, (float) (0.8F + (Math.random() * 0.2)));
								loc.sendParticles(ParticleTypes.PORTAL, x, y, z, 12, 0.5, 0.5, 0.5, 0.65);
							}
						}
					}
				} else if (stack.is(Items.WRITABLE_BOOK) && i == 12 && e >= 3) {
					defaultResult(target, offer, lvl, player, pos);
					target.setItem(0, new ItemStack(SupernaturalItems.RITUAL_BOOK.get()));
				} else if (stack.is(Items.TOTEM_OF_UNDYING) && i == 28 && e >= 0) {
					Entity entity = EntityType.loadEntityRecursive(SupernaturalManager.getSoulTag(offer), lvl, EntitySpawnReason.LOAD, o -> o);
					if (lvl.getBlockEntity(pos.above()) instanceof Spawner blok && entity != null && entity.getType().is(SupernaturalTags.SPAWNER)) {
						blok.setEntityId(entity.getType(), lvl.getRandom());
						defaultResult(target, offer, lvl, player, pos);
						lvl.sendBlockUpdated(pos.above(), lvl.getBlockState(pos.above()), lvl.getBlockState(pos.above()), 3);
						lvl.gameEvent(player, GameEvent.BLOCK_CHANGE, pos.above());
					}
				} else if (stack.is(Items.COMPASS)) {
					if (i == 12 && e >= 3) {
						defaultResult(target, offer, lvl, player, pos);
						target.setItem(0, RitualCompassItem.getRitualCompass(pos, lvl, 0));
					} else if (i == 20 && e >= 4) {
						defaultResult(target, offer, lvl, player, pos);
						target.setItem(0, RitualCompassItem.getRitualCompass(pos, lvl, 1));
					} else if (i == 28 && e >= 5) {
						defaultResult(target, offer, lvl, player, pos);
						target.setItem(0, RitualCompassItem.getRitualCompass(pos, lvl, 2));
					}
				} else if (stack.is(Items.ENDER_PEARL) && i == 16 && e >= 4) {
					ItemStack copy = new ItemStack(SupernaturalItems.ANCHORBALL.get());
					defaultResult(target, offer, lvl, player, pos);
					copy.set(SupernaturalData.ANCHOR, new AnchorballData(GlobalPos.of(lvl.dimension(), pos)));
					target.setItem(0, copy);
				} else if (stack.isEnchantable() && (player.experienceLevel >= 30 || player.isCreative())) {
					ItemStack copy = stack.copy();
					defaultResult(target, offer, lvl, player, pos);
					int c = e * SupernaturalConfig.SOULPOWER.get() + i;
					target.setItem(0, EnchantmentHelper.enchantItem(lvl.getRandom(), copy, c, lvl.registryAccess(), Optional.empty()));
					lvl.playSound(null, pos, SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.BLOCKS, 1.0F, (float) (0.8F + (Math.random() * 0.2)));
					if (!player.isCreative()) {
						player.giveExperiencePoints(c * -2);
					}
				} else {
					target.dropItem(0);
				}
			} else {
				target.dropItem(0);
			}
		}
	}

	private static void defaultResult(RitualBlockEntity target, ItemStack stack, ServerLevel lvl, Player player, BlockPos pos) {
		target.clearContent();
		lvl.playSound(null, pos, SoundEvents.SOUL_ESCAPE.value(), SoundSource.BLOCKS, 1.0F, (float) (0.8F + (Math.random() * 0.2)));
		lvl.sendParticles(ParticleTypes.SOUL, (pos.getX() + 0.5), (pos.getY() + 0.5), (pos.getZ() + 0.5), 21, 3, 1, 3, 0);
		if (!player.isCreative()) {
			stack.shrink(1);
		}
	}

	private static void summonMob(ServerLevel lvl, BlockPos pos, ItemStack stack) {
		Entity entity = EntityType.loadEntityRecursive(SupernaturalManager.getSoulTag(stack), lvl, EntitySpawnReason.MOB_SUMMONED, o -> o);
		if (entity != null) {
			entity.teleportTo(Vec3.atBottomCenterOf(pos).x, Vec3.atBottomCenterOf(pos).y, Vec3.atBottomCenterOf(pos).z);
			if (entity instanceof Villager bob) {
				ZombieVillager zomby = bob.convertTo(EntityType.ZOMBIE_VILLAGER, ConversionParams.single(bob, true, true), newbie -> { EventHooks.onLivingConvert(bob, newbie); });
				if (zomby != null) {
					zomby.setVillagerData(bob.getVillagerData());
					zomby.setGossips(bob.getGossips());
					zomby.setTradeOffers(bob.getOffers());
					zomby.setVillagerXp(bob.getVillagerXp());
				}
			}
			if (lvl.canSeeSky(pos)) {
				LightningBolt bolt = EntityType.LIGHTNING_BOLT.create(lvl, EntitySpawnReason.MOB_SUMMONED);
				if (bolt != null) {
					bolt.move(MoverType.SELF, Vec3.atBottomCenterOf(pos));
					bolt.setVisualOnly(true);
					lvl.addFreshEntity(bolt);
				}
			}
			lvl.addFreshEntity(entity);
		}
	}

	private static Item getHelmet(Item target) {
		Map<Item, Item> map = new HashMap<>();
        map.put(Items.COPPER_HELMET, SupernaturalItems.GOTHIC_COPPER_HELMET.get());
        map.put(Items.IRON_HELMET, SupernaturalItems.GOTHIC_IRON_HELMET.get());
		map.put(Items.GOLDEN_HELMET, SupernaturalItems.GOTHIC_GOLDEN_HELMET.get());
		map.put(Items.DIAMOND_HELMET, SupernaturalItems.GOTHIC_DIAMOND_HELMET.get());
		map.put(Items.NETHERITE_HELMET, SupernaturalItems.GOTHIC_NETHERITE_HELMET.get());
		return map.getOrDefault(target, Items.AIR);
	}

	private static Item getGreedyItem(int i) {
		Map<Integer, Item> map = new HashMap<>();
		map.put(0, Items.NETHER_STAR);
		map.put(1, Items.ROTTEN_FLESH);
		map.put(2, Items.BONE);
		map.put(3, Items.SPIDER_EYE);
		map.put(4, Items.FERMENTED_SPIDER_EYE);
		map.put(5, Items.SLIME_BALL);
		map.put(6, Items.POISONOUS_POTATO);
		map.put(7, Items.ROTTEN_FLESH);
		map.put(8, Items.BONE);
		map.put(9, Items.SPIDER_EYE);
		map.put(10, Items.FERMENTED_SPIDER_EYE);
		map.put(11, Items.SLIME_BALL);
		map.put(12, Items.POISONOUS_POTATO);
		return map.getOrDefault(i, Items.AIR);
	}

	@Nullable
	private static Goat getGoat(ServerLevel lvl, AABB box) {
		for (Goat target : lvl.getEntitiesOfClass(Goat.class, box)) {
			return target;
		}
		return null;
	}

	@Nullable
	private static Mob getSacrifice(ServerLevel lvl, ItemStack stack, AABB box) {
		for (Mob target : lvl.getEntitiesOfClass(Mob.class, box)) {
			if (SupernaturalManager.getSoulLevel(SupernaturalManager.getSoulLevel(target)) >= SupernaturalManager.getSoulLevel(SupernaturalManager.getSoulgem(stack)) && !target.getType().is(SupernaturalTags.IMMUNITY)) {
				return target;
			}
		}
		return null;
	}
}