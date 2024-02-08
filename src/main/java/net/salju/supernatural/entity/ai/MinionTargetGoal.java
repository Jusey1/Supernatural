package net.salju.supernatural.entity.ai;

import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.PathfinderMob;

public class MinionTargetGoal extends TargetGoal {
	private final TargetingConditions copyOwnerTargeting = TargetingConditions.forNonCombat().ignoreLineOfSight().ignoreInvisibilityTesting();
	private final PathfinderMob bob;
	private final Monster user;

	public MinionTargetGoal(PathfinderMob bob, Monster owner) {
		super(bob, false);
		this.bob = bob;
		this.user = owner;
	}

	public boolean canUse() {
		if (this.user != null && this.user.isAlive()) {
			return this.user.getTarget() != null && this.canAttack(this.user.getTarget(), this.copyOwnerTargeting);
		}
		return false;
	}

	public void start() {
		if (this.user != null && this.user.isAlive()) {
			bob.setTarget(this.user.getTarget());
		}
		super.start();
	}
}