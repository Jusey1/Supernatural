package net.salju.supernatural.entity.ai.revenant;

import net.salju.supernatural.init.SupernaturalBlocks;
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
            this.getTarget().hurt(SupernaturalDamageTypes.causeRitualDamage(this.revenant.level().registryAccess(), this.revenant), this.getTarget().getMaxHealth() * 100.F);
            if (this.getTarget().level().isEmptyBlock(this.getTarget().blockPosition()) && this.getTarget().level().getBlockState(this.getTarget().blockPosition().below()).isSolid()) {
                this.getTarget().level().setBlock(this.getTarget().blockPosition(), SupernaturalBlocks.REVENANT_FLAME.get().defaultBlockState(), 3);
            }
        }
	}

	@Override
	protected int getCastingInterval() {
		return 250;
	}

    public boolean hasValidTarget() {
        if (this.getTarget() instanceof Player player && this.revenant.hasLineOfSight(player)) {
            return player.isSleeping() || player.getFoodData().getFoodLevel() <= 6;
        }
        return false;
    }
}