package net.salju.supernatural.entity;

import net.salju.supernatural.init.SupernaturalModSounds;
import net.salju.supernatural.init.SupernaturalConfig;
import net.salju.supernatural.entity.ai.VampireAttackSelector;
import net.salju.supernatural.entity.ai.SupernaturalSpellcasterGoal;
import net.salju.supernatural.entity.ai.SupernaturalBloodSpellGoal;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.entity.monster.SpellcasterIllager;
import net.minecraft.world.entity.monster.AbstractIllager;
import net.minecraft.world.entity.ai.util.GoalUtils;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.RestrictSunGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.server.level.ServerLevel;

public class AbstractVampireEntity extends SpellcasterIllager {
	public AbstractVampireEntity(EntityType<? extends AbstractVampireEntity> type, Level world) {
		super(type, world);
		this.setPersistenceRequired();
		((GroundPathNavigation) this.getNavigation()).setCanOpenDoors(true);
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.addGoal(0, new FloatGoal(this));
		this.goalSelector.addGoal(0, new RestrictSunGoal(this));
		this.goalSelector.addGoal(0, new SupernaturalSpellcasterGoal(this));
		this.goalSelector.addGoal(2, new SupernaturalBloodSpellGoal(this));
		this.goalSelector.addGoal(3, new AbstractIllager.RaiderOpenDoorGoal(this));
		this.goalSelector.addGoal(3, new Raider.HoldGroundAttackGoal(this, 10.0F));
		this.goalSelector.addGoal(4, new MeleeAttackGoal(this, 1.2, false));
		this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, Raider.class)).setAlertOthers());
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 12, true, false, new VampireAttackSelector(this)));
		this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, LivingEntity.class, (float) 8));
		this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
	}

	protected void customServerAiStep() {
		if (!this.isNoAi() && GoalUtils.hasGroundPathNavigation(this)) {
			boolean flag = ((ServerLevel) this.level()).isRaided(this.blockPosition());
			((GroundPathNavigation) this.getNavigation()).setCanOpenDoors(flag);
		}
		if (this.isCastingSpell()) {
			this.navigation.stop();
		}
		super.customServerAiStep();
	}

	public void aiStep() {
		if (!SupernaturalConfig.SUN.get()) {
			boolean flag = this.isSunBurnTick();
			if (flag) {
				this.setSecondsOnFire(8);
				this.hurt(this.damageSources().inFire(), 4);
			}
		}
		super.aiStep();
	}

	public void setIsCastingSpell(int i) {
		if (i == 0) {
			this.setIsCastingSpell(SpellcasterIllager.IllagerSpell.NONE);
		} else if (i == 1) {
			this.setIsCastingSpell(SpellcasterIllager.IllagerSpell.SUMMON_VEX);
		} else if (i == 2) {
			this.setIsCastingSpell(SpellcasterIllager.IllagerSpell.FANGS);
		}
	}

	public void setSpellCastingTime(int i) {
		this.spellCastingTickCount = i;
	}

	@Override
	public AbstractIllager.IllagerArmPose getArmPose() {
		if (this.isAggressive()) {
			return AbstractIllager.IllagerArmPose.ATTACKING;
		} else if (this.isCastingSpell()) {
			return AbstractIllager.IllagerArmPose.SPELLCASTING;
		} else {
			return this.isCelebrating() ? AbstractIllager.IllagerArmPose.CELEBRATING : AbstractIllager.IllagerArmPose.NEUTRAL;
		}
	}

	@Override
	public MobType getMobType() {
		return MobType.ILLAGER;
	}

	@Override
	public boolean isAlliedTo(Entity ally) {
		if (super.isAlliedTo(ally)) {
			return true;
		} else if (ally instanceof LivingEntity && ((LivingEntity) ally).getMobType() == MobType.ILLAGER) {
			return this.getTeam() == null && ally.getTeam() == null;
		} else {
			return false;
		}
	}

	@Override
	public boolean removeWhenFarAway(double distanceToClosestPlayer) {
		return false;
	}

	@Override
	public void applyRaidBuffs(int i, boolean check) {
		//
	}

	@Override
	public SoundEvent getAmbientSound() {
		return SupernaturalModSounds.VAMPIRE_IDLE.get();
	}

	@Override
	public SoundEvent getHurtSound(DamageSource ds) {
		return SupernaturalModSounds.VAMPIRE_HURT.get();
	}

	@Override
	public SoundEvent getDeathSound() {
		return SupernaturalModSounds.VAMPIRE_DEATH.get();
	}

	@Override
	public SoundEvent getCelebrateSound() {
		return SupernaturalModSounds.VAMPIRE_CELEBRATE.get();
	}

	@Override
	public SoundEvent getCastingSoundEvent() {
		return SoundEvents.EVOKER_CAST_SPELL;
	}
}
