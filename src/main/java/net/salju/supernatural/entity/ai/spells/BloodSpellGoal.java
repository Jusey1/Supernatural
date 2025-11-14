package net.salju.supernatural.entity.ai.spells;

import net.salju.supernatural.events.SupernaturalManager;
import net.salju.supernatural.init.SupernaturalTags;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.Difficulty;

public class BloodSpellGoal extends AbstractSpellGoal {
	public BloodSpellGoal(Mob target) {
		super(target);
	}

	@Override
	public boolean canUse() {
		if (this.getTarget() != null && !this.getTarget().getType().is(EntityTypeTags.UNDEAD) && !this.getTarget().getType().is(SupernaturalTags.IMMUNITY) && !SupernaturalManager.isVampire(this.getTarget())) {
			if (super.canUse() && this.user.getHealth() <= this.user.getMaxHealth() * 0.45F) {
				this.getTarget().addEffect(new MobEffectInstance(MobEffects.LEVITATION, 30, 0, false, false));
				return true;
			}
		}
		return false;
	}

	@Override
	protected void performSpellCasting() {
        if (this.getTarget() != null && this.user.level() instanceof ServerLevel lvl) {
            this.getTarget().hurtServer(lvl, this.getTarget().damageSources().magic(), this.getDamage());
            this.user.heal(this.getDamage() * 1.5F);
        }
	}

	@Override
	protected int getCastingTime() {
		return 40;
	}

	@Override
	protected int getCastingInterval() {
		return 480;
	}

	@Override
	protected int getSpell() {
		return 2;
	}

	@Override
	protected SoundEvent getSpellPrepareSound() {
		return SoundEvents.EVOKER_PREPARE_ATTACK;
	}

    private float getDamage() {
        if (this.user.level().getDifficulty().equals(Difficulty.HARD)) {
            return 4.5F;
        }
        return 3.0F;
    }
}