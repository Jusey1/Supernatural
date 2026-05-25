package net.salju.supernatural.entity.ai.revenant;

import net.salju.supernatural.entity.Angel;
import net.salju.supernatural.entity.Revenant;
import javax.annotation.Nullable;

public class RevenantCorruptSpellGoal extends AbstractRevenantSpellGoal {
    @Nullable
    private Angel angel;

	public RevenantCorruptSpellGoal(Revenant target) {
		super(target);
	}

    @Override
    public boolean canUse() {
        if (this.angel == null) {
            for (Angel statue : this.revenant.level().getEntitiesOfClass(Angel.class, this.revenant.getBoundingBox().inflate(12.5))) {
                if (!statue.isCursed()) {
                    this.angel = statue;
                    break;
                }
            }
        }
        return super.canUse() && this.angel != null;
    }

	@Override
	protected void performSpellCasting() {
        if (this.angel != null) {
            this.angel.getEntityData().set(Angel.CURSED, true);
            this.angel = null;
        }
	}

	@Override
	protected int getCastingInterval() {
		return 120;
	}
}