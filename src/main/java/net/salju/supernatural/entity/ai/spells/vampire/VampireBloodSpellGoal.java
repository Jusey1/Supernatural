package net.salju.supernatural.entity.ai.spells.vampire;

import net.salju.supernatural.entity.AbstractVampireEntity;
import net.salju.supernatural.init.SupernaturalTags;
import net.salju.supernatural.events.SupernaturalManager;
import net.salju.supernatural.entity.ai.spells.AbstractSpellGoal;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.effect.MobEffectInstance;

public class VampireBloodSpellGoal extends AbstractSpellGoal {
	public VampireBloodSpellGoal(AbstractVampireEntity target) {
		super(target);
	}

	@Override
	public boolean canUse() {
		if (this.user.getTarget() != null && !(this.user.getTarget().getType().is(EntityTypeTags.UNDEAD)) && !SupernaturalManager.isVampire(this.user.getTarget()) && !this.user.getTarget().getType().is(SupernaturalTags.IMMUNITY)) {
			if (this.user.getHealth() > (this.user.getMaxHealth() * 0.45)) {
				return false;
			} else if (super.canUse()) {
				this.user.getTarget().addEffect(new MobEffectInstance(MobEffects.LEVITATION, 30, 0, false, false));
				return true;
			}
		}
		return false;
	}

	@Override
	protected void performSpellCasting() {
		this.user.getTarget().hurt(this.user.getTarget().damageSources().magic(), (this.user.getTarget().getMaxHealth() * 0.15F));
		this.user.heal(this.user.getMaxHealth() * 0.25F);
	}

	@Override
	protected int getCastingTime() {
		return 40;
	}

	@Override
	protected int getCastingInterval() {
		return 480;
	}

	@Override
	protected int getSpell() {
		return 2;
	}

	@Override
	protected SoundEvent getSpellPrepareSound() {
		return SoundEvents.EVOKER_PREPARE_ATTACK;
	}
}