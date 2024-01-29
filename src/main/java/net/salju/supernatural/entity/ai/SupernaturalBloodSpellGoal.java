package net.salju.supernatural.entity.ai;

import net.salju.supernatural.init.SupernaturalTags;
import net.salju.supernatural.events.SupernaturalManager;

import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundEvent;

public class SupernaturalBloodSpellGoal extends AbstractSupernaturalSpellGoal {
	private final Monster user;

	public SupernaturalBloodSpellGoal(Monster source) {
		super(source);
		this.user = source;
	}

	@Override
	public boolean canUse() {
		if (this.user.getTarget() != null && !(this.user.getTarget().getMobType() == MobType.UNDEAD) && !SupernaturalManager.isVampire(this.user.getTarget()) && !this.user.getTarget().getType().is(SupernaturalTags.IMMUNITY)) {
			if (this.user.getHealth() > (this.user.getMaxHealth() * 0.45)) {
				return false;
			} else {
				this.user.getTarget().addEffect(new MobEffectInstance(MobEffects.LEVITATION, 30, 0, false, false));
				return true;
			}
		}
		return false;
	}

	@Override
	protected void performSpellCasting() {
		this.user.getTarget().hurt(this.user.getTarget().damageSources().magic(), (this.user.getTarget().getMaxHealth() * 0.25F));
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