package net.salju.supernatural.entity.ai;

import net.salju.supernatural.entity.AbstractVampireEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.sounds.SoundEvent;
import javax.annotation.Nullable;

public abstract class AbstractSupernaturalSpellGoal extends Goal {
	private final Monster user;
	protected int attackWarmupDelay;
	protected int nextAttackTickCount;

	public AbstractSupernaturalSpellGoal(Monster source) {
		this.user = source;
	}

	@Override
	public boolean canUse() {
		LivingEntity target = this.user.getTarget();
		if (target != null && target.isAlive()) {
			if (this.user instanceof AbstractVampireEntity vampire) {
				if (vampire.isCastingSpell()) {
					return false;
				} else {
					return vampire.tickCount >= this.nextAttackTickCount;
				}
			}
		}
		return false;
	}

	@Override
	public boolean canContinueToUse() {
		LivingEntity target = this.user.getTarget();
		return target != null && target.isAlive() && this.attackWarmupDelay > 0;
	}

	@Override
	public void start() {
		this.attackWarmupDelay = this.adjustedTickDelay(this.getCastWarmupTime());
		this.nextAttackTickCount = this.user.tickCount + this.getCastingInterval();
		if (this.user instanceof AbstractVampireEntity vampire) {
			vampire.setSpellCastingTime(this.getCastingTime());
			vampire.setIsCastingSpell(this.getSpell());
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
			if (this.user instanceof AbstractVampireEntity vampire) {
				vampire.playSound(vampire.getCastingSoundEvent(), 1.0F, 1.0F);
			}
		}
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