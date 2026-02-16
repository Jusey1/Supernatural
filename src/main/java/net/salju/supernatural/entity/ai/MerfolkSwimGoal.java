package net.salju.supernatural.entity.ai;

import net.salju.supernatural.entity.Merfolk;
import net.minecraft.world.entity.ai.goal.RandomSwimmingGoal;

public class MerfolkSwimGoal extends RandomSwimmingGoal {
	public final Merfolk merfolk;

	public MerfolkSwimGoal(Merfolk target) {
		super(target, 1.0F, 40);
		this.merfolk = target;
	}

	@Override
	public boolean canUse() {
		return super.canUse() && this.merfolk.isInWater() && !this.merfolk.isAggressive();
	}
}