package net.salju.supernatural.entity.ai.wight;

import net.salju.supernatural.entity.Wight;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;

public class WightMeleeAttackGoal extends MeleeAttackGoal {
	private final Wight target;

	public WightMeleeAttackGoal(Wight t, double d, boolean check) {
		super(t, d, check);
		this.target = t;
	}

    @Override
    public boolean canUse() {
        return super.canUse() && !this.target.shouldUseCrossbow();
    }
}