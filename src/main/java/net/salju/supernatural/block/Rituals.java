package net.salju.supernatural.block;

import net.salju.supernatural.init.SupernaturalTags;
import net.salju.supernatural.init.SupernaturalItems;
import net.salju.supernatural.init.SupernaturalEffects;
import net.salju.supernatural.init.SupernaturalDamageTypes;
import net.salju.supernatural.init.SupernaturalConfig;
import net.salju.supernatural.init.SupernaturalBlocks;
import net.salju.supernatural.events.SupernaturalManager;
import net.salju.supernatural.entity.Angel;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.monster.ZombieVillager;
import net.minecraft.world.entity.animal.goat.Goat;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.util.Mth;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.nbt.NbtOps;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.BlockPos;
import javax.annotation.Nullable;

public class Rituals {
	public static void doRitual(ItemStack stack, ItemStack offer, ServerLevel lvl, Player player, BlockPos pos) {
		if (lvl.getBlockEntity(pos) instanceof RitualBlockEntity target) {
			ItemStack off = player.getOffhandItem();
			if (lvl.getBrightness(LightLayer.BLOCK, pos) < 6 && (lvl.getBrightness(LightLayer.SKY, pos) < 6 || !lvl.isDay())) {
				int i = SupernaturalManager.getPower(lvl, pos);
				int e = SupernaturalManager.getSoulLevel(SupernaturalManager.getSoulgem(offer));
				if (i == 28 && e >= 0 && stack.is(SupernaturalItems.GRAVE_SOIL.get())) {
					defaultResult(target, offer, lvl, player, pos);
					if (SupernaturalConfig.SACRIFICE.get()) {
						Mob sacrifice = getSacrifice(lvl, offer, target.getRenderBoundingBox().inflate(12.85D));
						(sacrifice != null ? sacrifice : player).hurt(SupernaturalDamageTypes.causeRitualDamage(player.level().registryAccess(), player), Float.MAX_VALUE);
					}
					summonMob(lvl, pos.above(), offer);
					lvl.playSound(null, pos, SoundEvents.SOUL_ESCAPE, SoundSource.BLOCKS, 1.0F, (float) (0.8F + (Math.random() * 0.2)));
				} else if (i == 20 && e >= 5 && stack.is(SupernaturalItems.BLOOD.get()) && !SupernaturalManager.isVampire(player) && !player.isHurt()) {
					Player victim = lvl.getPlayerByUUID(SupernaturalManager.getUUID(stack));
					Goat goat = getGoat(lvl, target.getRenderBoundingBox().inflate(12.85D));
					if (victim != null && victim == player && (goat != null || !SupernaturalConfig.SACRIFICE.get())) {
						defaultResult(target, offer, lvl, player, pos);
						player.hurt(player.damageSources().magic(), 0.25F);
						player.setHealth(1.0F);
						SupernaturalManager.setVampire(player, true);
						target.setItem(0, new ItemStack(Items.GLASS_BOTTLE));
						if (SupernaturalConfig.SACRIFICE.get()) {
							goat.hurt(SupernaturalDamageTypes.causeRitualDamage(goat.level().registryAccess(), player), Float.MAX_VALUE);
						}
					}
				} else if (i == 12 && e >= 2 && (stack.is(Items.IRON_INGOT) || stack.is(Items.COPPER_INGOT))) {
					defaultResult(target, offer, lvl, player, pos);
					target.setItem(0, new ItemStack(stack.is(Items.IRON_INGOT) ? Items.GOLD_INGOT : Items.IRON_INGOT));
				} else if (i == 8 && e >= 1 && stack.is(SupernaturalItems.VAMPIRE_DUST.get())) {
					defaultResult(target, offer, lvl, player, pos);
					BlockPos top = BlockPos.containing((pos.getX() + 3), (pos.getY() - 1), (pos.getZ() + 3));
					BlockPos bot = BlockPos.containing((pos.getX() - 3), (pos.getY() - 1), (pos.getZ() - 3));
					for (BlockPos poz : BlockPos.betweenClosed(top, bot)) {
						if (lvl.getBlockState(poz).is(Blocks.SOUL_SAND) || lvl.getBlockState(poz).is(Blocks.SOUL_SOIL)) {
							lvl.setBlock(poz, SupernaturalBlocks.GRAVE_SOIL.get().defaultBlockState(), 3);
						}
					}
				} else if (SupernaturalManager.isVampire(player) && off.is(SupernaturalItems.BLOOD.get())) {
					Player victim = lvl.getPlayerByUUID(SupernaturalManager.getUUID(off));
					if (victim != null) {
						if (i == 12 && e >= 4 && stack.is(SupernaturalItems.GRAVE_SOIL.get())) {
							defaultResult(target, offer, lvl, player, pos);
							victim.addEffect(new MobEffectInstance(MobEffects.DARKNESS, 100, 0, false, false));
							victim.addEffect(new MobEffectInstance(SupernaturalEffects.POSSESSION.get(), 6000, 0, false, false));
							for (int r = 0; r < 3; ++r) {
								BlockPos poz = victim.blockPosition().offset(-2 + lvl.random.nextInt(5), 1, -2 + lvl.random.nextInt(5));
								EntityType.VEX.spawn(lvl, poz, MobSpawnType.MOB_SUMMONED);
							}
						} else if (i == 12 && e >= 4 && stack.is(Items.CARVED_PUMPKIN)) {
							defaultResult(target, offer, lvl, player, pos);
							ItemStack helmet = victim.getItemBySlot(EquipmentSlot.HEAD);
							if (!helmet.isEmpty()) {
								target.cloneItem(helmet);
							}
							victim.setItemSlot(EquipmentSlot.HEAD, stack);
						}
					}
				}
			}
		}
	}

	private static void defaultResult(RitualBlockEntity target, ItemStack stack, ServerLevel lvl, Player player, BlockPos pos) {
		target.clearContent();
		lvl.playSound(null, pos, SoundEvents.SOUL_ESCAPE, SoundSource.BLOCKS, 1.0F, (float) (0.8F + (Math.random() * 0.2)));
		lvl.sendParticles(ParticleTypes.SOUL, (pos.getX() + 0.5), (pos.getY() + 0.5), (pos.getZ() + 0.5), 21, 3, 1, 3, 0);
		if (!player.isCreative()) {
			stack.shrink(1);
		}
		for (Angel statue : lvl.getEntitiesOfClass(Angel.class, target.getRenderBoundingBox().inflate(64.85D))) {
			if (Mth.nextInt(lvl.getRandom(), 0, 25) >= 24 && !statue.isCursed()) {
				statue.getEntityData().set(Angel.CURSED, true);
				lvl.sendParticles(ParticleTypes.SOUL, (statue.getX() + 0.5), (statue.getY() + 0.5), (statue.getZ() + 0.5), 8, 0.25, 0.35, 0.25, 0);
			}
		}
		if (lvl.canSeeSky(pos)) {
			LightningBolt bolt = EntityType.LIGHTNING_BOLT.create(lvl);
			bolt.moveTo(Vec3.atBottomCenterOf(pos));
			bolt.setVisualOnly(true);
			lvl.addFreshEntity(bolt);
		}
	}

	private static void summonMob(ServerLevel lvl, BlockPos pos, ItemStack stack) {
		Entity entity = EntityType.loadEntityRecursive(SupernaturalManager.getSoulTag(stack), lvl, o -> o);
		if (entity != null) {
			entity.moveTo(Vec3.atBottomCenterOf(pos));
			if (entity instanceof Villager bob) {
				ZombieVillager zomby = bob.convertTo(EntityType.ZOMBIE_VILLAGER, false);
				zomby.setVillagerData(bob.getVillagerData());
				zomby.setGossips(bob.getGossips().store(NbtOps.INSTANCE));
				zomby.setTradeOffers(bob.getOffers().createTag());
				zomby.setVillagerXp(bob.getVillagerXp());
			}
			lvl.addFreshEntity(entity);
		}
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