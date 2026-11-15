package net.salju.supernatural.entity.ai.vampyre;

import net.salju.supernatural.init.SupernaturalSounds;
import net.salju.supernatural.entity.ai.abstractai.AbstractHealSpellGoal;
import net.salju.supernatural.entity.Vampyre;
import net.minecraft.sounds.SoundEvent;

public class VampyreBloodSpellGoal extends AbstractHealSpellGoal {
	public VampyreBloodSpellGoal(Vampyre vyre) {
		super(vyre);
	}

    @Override
    protected SoundEvent getSpellPrepareSound() {
        return SupernaturalSounds.VAMPYRE_PREPARE_SPELL.get();
    }
}