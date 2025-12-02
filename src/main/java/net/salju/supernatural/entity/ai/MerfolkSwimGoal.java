package net.salju.supernatural.entity.ai;

import net.salju.supernatural.entity.AbstractMerfolkEntity;
import net.minecraft.world.entity.ai.goal.RandomSwimmingGoal;

public class MerfolkSwimGoal extends RandomSwimmingGoal {
	public final AbstractMerfolkEntity merfolk;

	public MerfolkSwimGoal(AbstractMerfolkEntity target) {
		super(target, 1.0F, 40);
		this.merfolk = target;
	}

	@Override
	public boolean canUse() {
		return super.canUse() && this.merfolk.isInWaterOrBubble() && !this.merfolk.isAggressive();
	}
}