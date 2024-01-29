package net.salju.supernatural.entity.ai;

import net.salju.supernatural.entity.Necromancer;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.PathfinderMob;

public class MinionTargetGoal extends TargetGoal {
	private final TargetingConditions copyOwnerTargeting = TargetingConditions.forNonCombat().ignoreLineOfSight().ignoreInvisibilityTesting();
	private final PathfinderMob bob;

	public MinionTargetGoal(PathfinderMob bob) {
			super(bob, false);
			this.bob = bob;
		}

	public boolean canUse() {
		Necromancer target = bob.level().getNearestEntity(Necromancer.class, TargetingConditions.DEFAULT, bob, bob.getX(), bob.getY(), bob.getZ(), bob.getBoundingBox().inflate(32.0D));
		if (target != null) {
			return target.getTarget() != null && this.canAttack(target.getTarget(), this.copyOwnerTargeting);
		}
		return false;
	}

	public void start() {
		Necromancer target = bob.level().getNearestEntity(Necromancer.class, TargetingConditions.DEFAULT, bob, bob.getX(), bob.getY(), bob.getZ(), bob.getBoundingBox().inflate(32.0D));
		if (target != null) {
			bob.setTarget(target.getTarget());
		}
		super.start();
	}
}