package net.salju.supernatural.entity.ai.spells;

import net.salju.supernatural.entity.AbstractVampireEntity;
import net.salju.supernatural.entity.Spellcaster;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.Mob;
import java.util.EnumSet;

public class AbstractSpellcasterGoal extends Goal {
	public final Mob user;

	public AbstractSpellcasterGoal(Mob target) {
		this.user = target;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
	}

	@Override
	public boolean canUse() {
		if (this.user instanceof AbstractVampireEntity target) {
			return target.isCastingSpell();
		} else if (this.user instanceof Spellcaster target) {
            return target.isCastingSpell();
        }
		return false;
	}

	@Override
	public void stop() {
		super.stop();
		if (this.user instanceof AbstractVampireEntity target) {
			target.setIsCastingSpell(0);
		} else if (this.user instanceof Spellcaster target) {
            target.setSpellTick(0);
        }
	}

	@Override
	public void tick() {
		if (this.user.getTarget() != null) {
			this.user.getLookControl().setLookAt(this.user.getTarget(), (float) this.user.getMaxHeadYRot(), (float) this.user.getMaxHeadXRot());
		}
	}
}