package net.salju.supernatural.effect;

import net.salju.supernatural.init.SupernaturalMobs;
import net.salju.supernatural.init.SupernaturalEffects;
import net.salju.supernatural.entity.Spooky;
import net.salju.supernatural.entity.PossessedArmor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffect;

public class Possession extends MobEffect {
	public Possession(MobEffectCategory cate, int i) {
		super(cate, i);
	}

	@Override
	public boolean applyEffectTick(LivingEntity target, int str) {
		int dur = target.getEffect(SupernaturalEffects.POSSESSION).getDuration();
		if (target.hasEffect(MobEffects.GLOWING)) {
			target.removeEffect(SupernaturalEffects.POSSESSION);
		}
		if (dur <= 1) {
			if (target.level() instanceof ServerLevel lvl) {
				Spooky ghost = SupernaturalMobs.SPOOKY.get().spawn(lvl, target.blockPosition(), MobSpawnType.MOB_SUMMONED);
				if (target instanceof Player player && ghost != null) {
					ghost.setPersistenceRequired();
					ghost.setOwner(player.getUUID());
				}
			}
		}
		if (target instanceof Animal) {
			if (!target.isOnFire() && !target.isInWater()) {
				target.setRemainingFireTicks(100);
			}
			target.addEffect(new MobEffectInstance(MobEffects.LEVITATION, dur, str));
		} else if (target instanceof PossessedArmor armor) {
			target.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, dur, str));
			if (armor.isTamed()) {
				target.addEffect(new MobEffectInstance(MobEffects.REGENERATION, dur, str));
			}
		}
		return super.applyEffectTick(target, str);
	}

	@Override
	public boolean shouldApplyEffectTickThisTick(int dur, int str) {
		return true;
	}
}