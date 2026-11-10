package net.salju.supernatural.entity.ai;

import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;

public class MinionTargetGoal extends TargetGoal {
	private final TargetingConditions copyOwnerTargeting = TargetingConditions.forNonCombat().ignoreLineOfSight().ignoreInvisibilityTesting();
	private final PathfinderMob bob;
	private final LivingEntity user;

	public MinionTargetGoal(PathfinderMob bob, LivingEntity owner) {
		super(bob, false);
		this.bob = bob;
		this.user = owner;
	}

	@Override
	public boolean canUse() {
		if (this.user != null && this.user.isAlive()) {
			return ((this.user.getLastHurtMob() != null && this.canAttack(this.user.getLastHurtMob(), this.copyOwnerTargeting)) || (this.user.getLastHurtByMob() != null) && this.canAttack(this.user.getLastHurtByMob(), this.copyOwnerTargeting));
		}
		return false;
	}

	@Override
	public void start() {
		if (this.user != null && this.user.isAlive()) {
			if (this.user.getLastHurtMob() != null && this.user.getLastHurtMob().isAlive()) {
				this.bob.setTarget(this.user.getLastHurtMob());
			} else if (this.user.getLastHurtByMob() != null && this.user.getLastHurtByMob().isAlive()) {
				this.bob.setTarget(this.user.getLastHurtByMob());
			}
		}
		super.start();
	}
}