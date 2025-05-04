package net.salju.supernatural.effect;

import net.salju.supernatural.init.SupernaturalMobs;
import net.salju.supernatural.init.SupernaturalEffects;
import net.salju.supernatural.entity.Spooky;
import net.salju.supernatural.entity.PossessedArmor;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.server.level.ServerLevel;

public class Possession extends MobEffect {
	public Possession(MobEffectCategory cate, int i) {
		super(cate, i);
	}

	@Override
	public String getDescriptionId() {
		return "effect.supernatural.possession";
	}

	@Override
	public boolean applyEffectTick(ServerLevel lvl, LivingEntity target, int str) {
		int dur = target.getEffect(SupernaturalEffects.POSSESSION).getDuration();
		if (target.hasEffect(MobEffects.GLOWING)) {
			target.removeEffect(SupernaturalEffects.POSSESSION);
		}
		if (dur <= 1) {
			Spooky ghost = SupernaturalMobs.SPOOKY.get().spawn(lvl, target.blockPosition(), EntitySpawnReason.MOB_SUMMONED);
			if (target instanceof Player player && ghost != null) {
				ghost.setPersistenceRequired();
				ghost.setOwner(player);
			}
		}
		if (target instanceof Animal) {
			if (!target.isOnFire() && !target.isInWater()) {
				target.setRemainingFireTicks(5);
			}
			target.addEffect(new MobEffectInstance(MobEffects.LEVITATION, dur, str));
		} else if (target instanceof PossessedArmor armor) {
			target.addEffect(new MobEffectInstance(MobEffects.STRENGTH, dur, str));
			if (armor.isTamed()) {
				target.addEffect(new MobEffectInstance(MobEffects.REGENERATION, dur, str));
			}
		}
		return super.applyEffectTick(lvl, target, str);
	}

	@Override
	public boolean shouldApplyEffectTickThisTick(int dur, int str) {
		return true;
	}
}