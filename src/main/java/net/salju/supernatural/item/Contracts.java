package net.salju.supernatural.item;

import net.salju.supernatural.network.UsedContract;
import net.salju.supernatural.init.SupernaturalTags;
import net.salju.supernatural.init.SupernaturalItems;
import net.salju.supernatural.init.SupernaturalEffects;
import net.salju.supernatural.init.SupernaturalConfig;
import net.salju.supernatural.events.SupernaturalManager;
import net.salju.supernatural.entity.Angel;
import net.salju.supernatural.block.RitualBlockEntity;
import net.salju.supernatural.SupernaturalMod;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.monster.ZombieVillager;
import net.minecraft.world.entity.animal.goat.Goat;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.util.Mth;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.nbt.NbtOps;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.BlockPos;
import javax.annotation.Nullable;

public class Contracts {
	public static void doContract(ContractItem type, ItemStack stack, ServerLevel lvl, Player player, Player user, BlockPos pos) {
		if (lvl.getBlockEntity(pos) instanceof RitualBlockEntity target && !target.isEmpty() && player != null && lvl.getBrightness(LightLayer.BLOCK, pos) <= 6 && (lvl.getBrightness(LightLayer.SKY, pos) <= 6 || !lvl.isDay())) {
			ItemStack offer = target.getItem(0).copy();
			int p = SupernaturalManager.getPower(lvl, pos);
			Goat goat = getGoat(lvl, target.getRenderBoundingBox().inflate(12.85D));
			if (type.is(ContractItem.Types.VAMPIRISM) && SupernaturalConfig.VAMPIRISM.get() && p == 20 && !player.hasEffect(SupernaturalEffects.SUPERNATURAL.get()) && !player.isHurt() && (goat != null || !SupernaturalConfig.SACRIFICE.get())) {
				defaultResult(target, stack, lvl, player, user, pos);
				target.setItem(0, SupernaturalManager.setUUID(new ItemStack(SupernaturalItems.PLAYER_BLOOD.get()), player));
				player.hurt(player.damageSources().magic(), 0.25F);
				player.setHealth(1.0F);
				SupernaturalManager.setVampire(player, true);
				if (SupernaturalConfig.SACRIFICE.get()) {
					goat.hurt(goat.damageSources().magic(), Float.MAX_VALUE);
				}
			} else if (type.is(ContractItem.Types.WEREWOLFISM) && SupernaturalConfig.WEREWOLFISM.get() && p == 20 && !player.hasEffect(SupernaturalEffects.SUPERNATURAL.get()) && lvl.getMoonPhase() == 0 && !lvl.isDay()
					&& (goat != null || !SupernaturalConfig.SACRIFICE.get())) {
				defaultResult(target, stack, lvl, player, user, pos);
				SupernaturalManager.setWerewolf(player, true);
				if (SupernaturalConfig.SACRIFICE.get()) {
					goat.hurt(goat.damageSources().magic(), Float.MAX_VALUE);
				}
			} else if (type.is(ContractItem.Types.REANIMATE) && SupernaturalConfig.REANIMATE.get() && p == 28) {
				defaultResult(target, stack, lvl, player, user, pos);
				Mob sacrifice = getSacrifice(lvl, offer, target.getRenderBoundingBox().inflate(12.85D));
				if (sacrifice != null) {
					summonMob(sacrifice, lvl, pos.above(), offer);
				} else {
					summonMob(player, lvl, pos.above(), offer);
				}
				lvl.playSound(null, pos, SoundEvents.SOUL_ESCAPE, SoundSource.BLOCKS, 1.0F, (float) (0.8F + (Math.random() * 0.2)));
			} else if (type.is(ContractItem.Types.VEXATION) && SupernaturalConfig.VEXATION.get() && p == 12) {
				defaultResult(target, stack, lvl, player, user, pos);
				player.addEffect(new MobEffectInstance(MobEffects.DARKNESS, 100, 0, false, false));
				player.addEffect(new MobEffectInstance(SupernaturalEffects.POSSESSION.get(), 6000, 0, false, false));
				for (int i = 0; i < 3; ++i) {
					BlockPos poz = player.blockPosition().offset(-2 + lvl.random.nextInt(5), 1, -2 + lvl.random.nextInt(5));
					EntityType.VEX.spawn(lvl, poz, MobSpawnType.MOB_SUMMONED);
				}
			} else if (type.is(ContractItem.Types.MISFORTUNE) && SupernaturalConfig.MISFORTUNE.get() && p == 16) {
				defaultResult(target, stack, lvl, player, user, pos);
				player.addEffect(new MobEffectInstance(MobEffects.BAD_OMEN, 48000, 2, false, false));
				player.addEffect(new MobEffectInstance(MobEffects.UNLUCK, 48000, 4, false, false));
			} else if (type.is(ContractItem.Types.KNOWLEDGE) && SupernaturalConfig.KNOWLEDGE.get() && p == 18) {
				defaultResult(target, stack, lvl, player, user, pos);
				int e = Mth.nextInt(lvl.getRandom(), 12, 24);
				for (int i = 0; i < e; ++i) {
					SupernaturalMod.queueServerWork((i * 10), () -> {
						lvl.sendParticles(ParticleTypes.SOUL, (pos.getX() + 0.5), (pos.getY() + 0.5), (pos.getZ() + 0.5), 4, 0.35, 1, 0.35, 0);
						lvl.addFreshEntity(new ExperienceOrb(lvl, (pos.getX() + 0.5), (pos.getY() + 0.5), (pos.getZ() + 0.5), 6));
					});
				}
				for (int i = 0; i < 1; ++i) {
					BlockPos poz = player.blockPosition().offset(-2 + lvl.random.nextInt(5), 1, -2 + lvl.random.nextInt(5));
					EntityType.VEX.spawn(lvl, poz, MobSpawnType.MOB_SUMMONED);
				}
			} else if (type.is(ContractItem.Types.FORTUNE) && SupernaturalConfig.FORTUNE.get() && p == 24) {
				defaultResult(target, stack, lvl, player, user, pos);
				int e = Mth.nextInt(lvl.getRandom(), 21, 42);
				for (int i = 0; i < e; ++i) {
					SupernaturalMod.queueServerWork((i * 10), () -> {
						ItemStack prize = new ItemStack(Items.EMERALD);
						if (Mth.nextInt(lvl.getRandom(), 0, 25) >= 21) {
							prize = new ItemStack(Items.DIAMOND);
						}
						lvl.sendParticles(ParticleTypes.SOUL, (pos.getX() + 0.5), (pos.getY() + 0.5), (pos.getZ() + 0.5), 4, 0.35, 1, 0.35, 0);
						target.cloneItem(prize);
					});
				}
				for (int i = 0; i < 1; ++i) {
					BlockPos poz = player.blockPosition().offset(-2 + lvl.random.nextInt(5), 1, -2 + lvl.random.nextInt(5));
					EntityType.VEX.spawn(lvl, poz, MobSpawnType.MOB_SUMMONED);
				}
			} else if (type.is(ContractItem.Types.PUMPKIN) && SupernaturalConfig.PUMPKIN.get() && p == 12) {
				defaultResult(target, stack, lvl, player, user, pos);
				ItemStack helmet = player.getItemBySlot(EquipmentSlot.HEAD);
				if (!helmet.isEmpty()) {
					target.cloneItem(helmet);
				}
				player.setItemSlot(EquipmentSlot.HEAD, offer);
			}
		}
	}

	private static void defaultResult(RitualBlockEntity target, ItemStack stack, ServerLevel lvl, Player player, Player user, BlockPos pos) {
		lvl.playSound(null, player.blockPosition(), SoundEvents.TOTEM_USE, SoundSource.PLAYERS, 1.0F, (float) (0.8F + (Math.random() * 0.2)));
		if (player instanceof ServerPlayer ply) {
			SupernaturalMod.sendToClientPlayer(new UsedContract(stack), ply);
		}
		target.clearContent();
		if (!user.isCreative()) {
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

	private static void summonMob(LivingEntity target, ServerLevel lvl, BlockPos pos, ItemStack stack) {
		if (SupernaturalConfig.SACRIFICE.get()) {
			target.hurt(target.damageSources().magic(), Float.MAX_VALUE);
		}
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
