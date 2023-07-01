package net.salju.supernatural.potion;

import net.salju.supernatural.init.SupernaturalModMobEffects;
import net.salju.supernatural.init.SupernaturalModEntities;
import net.salju.supernatural.entity.SpookyEntity;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.server.level.ServerLevel;

public class PossessionMobEffect extends MobEffect {
	public PossessionMobEffect() {
		super(MobEffectCategory.HARMFUL, -3342337);
	}

	@Override
	public String getDescriptionId() {
		return "effect.supernatural.possession";
	}

	@Override
	public void applyEffectTick(LivingEntity target, int str) {
		if (target.hasEffect(MobEffects.GLOWING)) {
			target.removeEffect(SupernaturalModMobEffects.POSSESSION.get());
		}
		if (target instanceof Animal) {
			if (!target.isOnFire() && !target.isInWater()) {
				target.setSecondsOnFire(5);
			}
			target.addEffect(new MobEffectInstance(MobEffects.LEVITATION, 999999, 0, (false), (false)));
		} else if (target instanceof Monster) {
			target.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 999999, 0, (false), (false)));
		} else if (target instanceof Player) {
			target.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 999999, 0, (false), (false)));
		}
	}

	@Override
	public void removeAttributeModifiers(LivingEntity target, AttributeMap map, int str) {
		super.removeAttributeModifiers(target, map, str);
		LevelAccessor world = target.level();
		double x = target.getX();
		double y = target.getY();
		double z = target.getZ();
		if (target instanceof Animal) {
			target.removeEffect(MobEffects.LEVITATION);
		} else if (target instanceof Monster) {
			target.removeEffect(MobEffects.DAMAGE_RESISTANCE);
		} else if (target instanceof Player) {
			target.removeEffect(MobEffects.WEAKNESS);
		}
		if (world instanceof ServerLevel lvl) {
			Mob ghost = new SpookyEntity(SupernaturalModEntities.SPOOKY.get(), lvl);
			ghost.copyPosition(target);
			ghost.finalizeSpawn(lvl, world.getCurrentDifficultyAt(ghost.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
			world.addFreshEntity(ghost);
		}
	}

	@Override
	public boolean isDurationEffectTick(int dur, int str) {
		return true;
	}
}