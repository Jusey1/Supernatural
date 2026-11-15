package net.salju.supernatural.entity.ai.abstractai;

import net.salju.supernatural.entity.Spellcaster;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.sounds.SoundEvent;
import javax.annotation.Nullable;

public abstract class AbstractSpellGoal extends Goal {
	public final Mob caster;
	protected int attackWarmupDelay;
	protected int nextAttackTickCount;

	public AbstractSpellGoal(Mob target) {
		this.caster = target;
	}

	@Override
	public boolean canUse() {
        return this.caster.tickCount >= this.nextAttackTickCount && this.getSpellTick() <= 0;
	}

	@Override
	public boolean canContinueToUse() {
		return this.attackWarmupDelay > 0;
	}

	@Override
	public void start() {
		this.attackWarmupDelay = this.adjustedTickDelay(this.getCastWarmupTime());
		this.nextAttackTickCount = this.caster.tickCount + this.getCastingInterval();
		if (this.caster instanceof Spellcaster target) {
            target.setSpellTick(this.getCastingTime());
        }
		if (this.getSpellPrepareSound() != null) {
			this.caster.playSound(this.getSpellPrepareSound(), 1.0F, 1.0F);
		}
	}

	@Override
	public void tick() {
		--this.attackWarmupDelay;
		if (this.attackWarmupDelay == 0) {
			this.performSpellCasting();
			if (this.caster instanceof Spellcaster target) {
                this.caster.playSound(target.getCastingSoundEvent(), 1.0F, 1.0F);
            }
		}
	}

    @Nullable
    public LivingEntity getTarget() {
        if (this.caster.getTarget() != null) {
            return this.caster.getTarget();
        }
        return this.caster.getLastHurtMob();
    }

    private int getSpellTick() {
        if (this.caster instanceof Spellcaster target) {
            return target.getSpellTick();
        }
        return 0;
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