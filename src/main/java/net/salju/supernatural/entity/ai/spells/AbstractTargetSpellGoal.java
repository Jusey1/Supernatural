package net.salju.supernatural.entity.ai.spells;

import net.minecraft.world.entity.Mob;

public abstract class AbstractTargetSpellGoal extends AbstractSpellGoal {
	public AbstractTargetSpellGoal(Mob target) {
		super(target);
	}

    @Override
    public boolean canUse() {
        return super.canUse() && this.hasTarget();
    }

    @Override
    public boolean canContinueToUse() {
        return super.canContinueToUse() && this.hasTarget();
    }

    public boolean hasTarget() {
        return this.getTarget() != null && this.getTarget().isAlive();
    }
}