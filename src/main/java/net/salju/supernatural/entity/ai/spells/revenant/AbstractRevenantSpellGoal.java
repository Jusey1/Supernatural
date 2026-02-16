package net.salju.supernatural.entity.ai.spells.revenant;

import net.salju.supernatural.init.SupernaturalSounds;
import net.salju.supernatural.entity.ai.spells.AbstractTargetSpellGoal;
import net.salju.supernatural.entity.Revenant;
import net.minecraft.sounds.SoundEvent;

public abstract class AbstractRevenantSpellGoal extends AbstractTargetSpellGoal {
    public final Revenant revenant;

	public AbstractRevenantSpellGoal(Revenant target) {
		super(target);
        this.revenant = target;
	}

    @Override
    public boolean canUse() {
        return super.canUse() && !this.revenant.isInWall() && this.revenant.distanceTo(this.getTarget()) <= 10.76F;
    }

    @Override
    public void start() {
        super.start();
        this.revenant.setWeaknessTick(this.getCastingTime() - 1);
    }

    @Override
    protected SoundEvent getSpellPrepareSound() {
        return SupernaturalSounds.WRAITH_TELEPORT.get();
    }

    @Override
    protected int getCastingTime() {
        return 40;
    }

    @Override
    public int getSpell() {
        return 0;
    }
}