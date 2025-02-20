package net.salju.supernatural.entity.ai;

import net.salju.supernatural.entity.AbstractVampireEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.ai.goal.Goal;
import java.util.EnumSet;

public class SupernaturalSpellcasterGoal extends Goal {
	private final Monster target;

	public SupernaturalSpellcasterGoal(Monster source) {
		this.target = source;
		this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
	}

	@Override
	public boolean canUse() {
		if (this.target instanceof AbstractVampireEntity vampire) {
			return vampire.isCastingSpell();
		}
		return false;
	}

	@Override
	public void stop() {
		super.stop();
		if (this.target instanceof AbstractVampireEntity vampire) {
			vampire.setIsCastingSpell(0);
		}
	}

	@Override
	public void tick() {
		if (this.target.getTarget() != null) {
			this.target.getLookControl().setLookAt(this.target.getTarget(), (float) this.target.getMaxHeadYRot(), (float) this.target.getMaxHeadXRot());
		}
	}
}