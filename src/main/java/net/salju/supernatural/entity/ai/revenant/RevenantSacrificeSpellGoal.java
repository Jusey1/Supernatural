package net.salju.supernatural.entity.ai.revenant;

import net.salju.supernatural.init.SupernaturalDamageTypes;
import net.salju.supernatural.entity.Revenant;
import net.minecraft.world.entity.player.Player;

public class RevenantSacrificeSpellGoal extends AbstractRevenantSpellGoal {
	public RevenantSacrificeSpellGoal(Revenant target) {
		super(target);
	}

    @Override
    public boolean canUse() {
        return super.canUse() && this.hasValidTarget();
    }

	@Override
	protected void performSpellCasting() {
        if (this.hasValidTarget()) {
            this.getTarget().hurt(SupernaturalDamageTypes.causeNightmareDamage(this.revenant.level().registryAccess()), this.getTarget().getMaxHealth() * 100.F);
        }
	}

    @Override
    protected int getCastingTime() {
        return 120;
    }

	@Override
	protected int getCastingInterval() {
		return 250;
	}

    public boolean hasValidTarget() {
        if (this.getTarget() instanceof Player player && this.revenant.hasLineOfSight(player)) {
            return player.isSleeping();
        }
        return false;
    }
}