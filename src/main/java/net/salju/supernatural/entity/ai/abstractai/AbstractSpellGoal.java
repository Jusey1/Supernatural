package net.salju.supernatural.entity.ai.abstractai;

import net.salju.supernatural.entity.Spellcaster;
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
		if (this.user instanceof Spellcaster target) {
        	return !target.isCastingSpell();
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
		if (this.user instanceof Spellcaster target) {
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
			if (this.user instanceof Spellcaster target) {
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
		return this.getCastingTime() - 20;
	}

	protected abstract int getCastingTime();

	protected abstract int getCastingInterval();

	@Nullable
	protected abstract SoundEvent getSpellPrepareSound();
}