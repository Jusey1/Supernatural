package net.salju.supernatural.effect;

import net.salju.supernatural.init.SupernaturalMobs;
import net.salju.supernatural.init.SupernaturalEffects;
import net.salju.supernatural.entity.Spooky;
import net.salju.supernatural.entity.PossessedArmor;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.BlockPos;

public class Possession extends MobEffect {
	private boolean check;

	public Possession(MobEffectCategory cate, int i) {
		super(cate, i);
	}

	@Override
	public String getDescriptionId() {
		return "effect.supernatural.possession";
	}

	@Override
	public void applyEffectTick(LivingEntity target, int str) {
		int dur = target.getEffect(SupernaturalEffects.POSSESSION.get()).getDuration();
		if (target.hasEffect(MobEffects.GLOWING)) {
			target.removeEffect(SupernaturalEffects.POSSESSION.get());
		}
		if (dur <= 1) {
			check = true;
		} else {
			check = false;
		}
		if (target instanceof Animal) {
			if (!target.isOnFire() && !target.isInWater()) {
				target.setSecondsOnFire(5);
			}
			target.addEffect(new MobEffectInstance(MobEffects.LEVITATION, dur, str));
		} else if (target instanceof Player) {
			//
		} else {
			target.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, dur, str));
			if (target instanceof PossessedArmor armor && armor.isTamed()) {
				target.addEffect(new MobEffectInstance(MobEffects.REGENERATION, dur, str));
			}
		}
	}

	@Override
	public void removeAttributeModifiers(LivingEntity target, AttributeMap map, int str) {
		super.removeAttributeModifiers(target, map, str);
		if (target.level() instanceof ServerLevel lvl) {
			Spooky ghost = SupernaturalMobs.SPOOKY.get().spawn(lvl, target.blockPosition(), MobSpawnType.MOB_SUMMONED);
			if (check && target instanceof Player player) {
				ghost.setOwner(player.getUUID());
			}
		}
	}

	@Override
	public boolean isDurationEffectTick(int dur, int str) {
		return true;
	}
}