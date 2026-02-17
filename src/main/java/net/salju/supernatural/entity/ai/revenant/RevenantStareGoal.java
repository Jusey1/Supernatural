package net.salju.supernatural.entity.ai.revenant;

import net.salju.supernatural.entity.Revenant;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.goal.Goal;

public class RevenantStareGoal extends Goal {
	private final Revenant revenant;

	public RevenantStareGoal(Revenant target) {
		this.revenant = target;
	}

    @Override
    public boolean canUse() {
        return this.revenant.getTarget() != null && this.revenant.isAlive();
    }

    @Override
    public void tick() {
        if (this.revenant.getTarget() != null) {
            this.revenant.setYRot(-((float) Mth.atan2(this.revenant.getTarget().getX() - this.revenant.getX(), this.revenant.getTarget().getZ() - this.revenant.getZ())) * (180F / (float)Math.PI));
            this.revenant.yBodyRot = this.revenant.getYRot();
            this.revenant.getLookControl().setLookAt(this.revenant.getTarget(), (float) this.revenant.getMaxHeadYRot(), (float) this.revenant.getMaxHeadXRot());
        }
    }
}