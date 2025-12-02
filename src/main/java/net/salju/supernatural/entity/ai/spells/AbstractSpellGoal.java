package net.salju.supernatural.entity.ai.spells;

import net.salju.supernatural.entity.AbstractSpellcasterEntity;
import net.salju.supernatural.entity.AbstractVampireEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.sounds.SoundEvent;
import javax.annotation.Nullable;

public abstract class AbstractSpellGoal extends Goal {
	public final Mob user;
	protected int attackWarmupDelay;
	protected int nextAttackTickCount;

	public AbstractSpellGoal(Mob target) {
		this.user = target;
	}

	@Override
	public boolean canUse() {
		if (this.user instanceof AbstractVampireEntity target && target.isCastingSpell()) {
			return false;
		} else if (this.user instanceof AbstractSpellcasterEntity target && target.isCastingSpell()) {
        	return false;
        }
        return this.user.tickCount >= this.nextAttackTickCount;
	}

	@Override
	public boolean canContinueToUse() {
		return this.attackWarmupDelay > 0;
	}

	@Override
	public void start() {
		this.attackWarmupDelay = this.adjustedTickDelay(this.getCastWarmupTime());
		this.nextAttackTickCount = this.user.tickCount + this.getCastingInterval();
		if (this.user instanceof AbstractVampireEntity target) {
			target.setSpellCastingTime(this.getCastingTime());
			target.setIsCastingSpell(this.getSpell());
		} else if (this.user instanceof AbstractSpellcasterEntity target) {
            target.setSpellTick(this.getCastingTime());
        }
		if (this.getSpellPrepareSound() != null) {
			this.user.playSound(this.getSpellPrepareSound(), 1.0F, 1.0F);
		}
	}

	@Override
	public void tick() {
		--this.attackWarmupDelay;
		if (this.attackWarmupDelay == 0) {
			this.performSpellCasting();
			if (this.user instanceof AbstractVampireEntity target) {
				this.user.playSound(target.getCastingSoundEvent(), 1.0F, 1.0F);
			} else if (this.user instanceof AbstractSpellcasterEntity target) {
                this.user.playSound(target.getCastingSoundEvent(), 1.0F, 1.0F);
            }
		}
	}

    @Nullable
    public LivingEntity getTarget() {
        if (this.user.getTarget() != null) {
            return this.user.getTarget();
        }
        return this.user.getLastHurtMob();
    }

	protected abstract void performSpellCasting();

	protected int getCastWarmupTime() {
		return 20;
	}

	protected abstract int getCastingTime();

	protected abstract int getCastingInterval();

	@Nullable
	protected abstract SoundEvent getSpellPrepareSound();

	protected abstract int getSpell();
}