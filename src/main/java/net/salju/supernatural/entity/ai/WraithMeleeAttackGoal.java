package net.salju.supernatural.entity.ai;

import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.salju.supernatural.entity.Wraith;

public class WraithMeleeAttackGoal extends MeleeAttackGoal {
    private final Wraith ghost;

	public WraithMeleeAttackGoal(Wraith target, double speed, boolean check) {
		super(target, speed, check);
		this.ghost = target;
	}

    @Override
    public boolean canUse() {
        if (!ghost.isHealer() && ghost.getTarget() != null) {
            return super.canUse() && ghost.distanceTo(ghost.getTarget()) <= 1.2;
        }
        return false;
    }
}