package net.salju.supernatural.entity;

import net.salju.supernatural.events.SupernaturalManager;
import net.salju.supernatural.init.SupernaturalSounds;
import net.salju.supernatural.init.SupernaturalEffects;
import net.salju.supernatural.entity.ai.spells.spooky.*;
import net.salju.supernatural.entity.ai.spells.*;
import net.salju.supernatural.entity.ai.targets.SpookyAttackSelector;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomFlyingGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.Level;

public class Spooky extends AbstractMinionEntity {
	public Spooky(EntityType<Spooky> type, Level world) {
		super(type, world);
		this.moveControl = new FlyingMoveControl(this, 20, true);
	}

	@Override
	protected PathNavigation createNavigation(Level lvl) {
		return new FlyingPathNavigation(this, lvl);
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.addGoal(0, new FloatGoal(this));
		this.goalSelector.addGoal(0, new AbstractSpellcasterGoal(this));
		this.goalSelector.addGoal(1, new SpookyPossessionSpellGoal(this));
		this.goalSelector.addGoal(3, new RandomLookAroundGoal(this));
		this.goalSelector.addGoal(4, new WaterAvoidingRandomFlyingGoal(this, 1.2));
		this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, LivingEntity.class, 8.0F));
		this.targetSelector.addGoal(0, new HurtByTargetGoal(this));
		this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 12, true, true, new SpookyAttackSelector(this)));
	}

	@Override
	public SoundEvent getAmbientSound() {
		return SoundEvents.VEX_AMBIENT;
	}

	@Override
	public SoundEvent getHurtSound(DamageSource source) {
		return SoundEvents.VEX_HURT;
	}

	@Override
	public SoundEvent getDeathSound() {
		return SoundEvents.VEX_DEATH;
	}

	@Override
	public void aiStep() {
		super.aiStep();
		this.setNoGravity(true);
	}

	@Override
	public void baseTick() {
		super.baseTick();
		if (this.isAlive()) {
			if (this.getTarget() != null && this.getTarget().isAlive()) {
				if (this.getTarget().hasEffect(MobEffects.GLOWING) || this.getTarget().hasEffect(SupernaturalEffects.POSSESSION)) {
					this.setTarget(null);
				} else if (this.getTarget() instanceof ArmorStand && !SupernaturalManager.hasArmor(this.getTarget())) {
					this.setTarget(null);
				}
			}
			if (this.isCastingSpell()) {
				this.applySpellEffects(this.getX(), this.getY() + 0.35, this.getZ());
			}
			if (this.level() instanceof ServerLevel lvl && !this.isTamed() && this.level().isDay() && this.level().canSeeSkyFromBelowWater(BlockPos.containing(this.getX(), this.getY(), this.getZ()))) {
				this.playSound(SupernaturalSounds.SPOOK_POOF.get(), 1.0F, 1.0F);
				this.discard();
				double r = this.random.nextGaussian() * 0.02D;
				lvl.sendParticles(ParticleTypes.POOF, this.getRandomX(1.0D), this.getRandomY(), this.getRandomZ(1.0D), 10, r, r, r, 0.25);
			}
		}
	}

	@Override
	public void setNoGravity(boolean check) {
		super.setNoGravity(true);
	}

	@Override
	public boolean causeFallDamage(float l, float d, DamageSource source) {
		return false;
	}

    @Override
    public float getWalkTargetValue(BlockPos pos, LevelReader world) {
        return -world.getPathfindingCostFromLightLevels(pos);
    }

	@Override
	protected float getSoundVolume() {
		return 0.35F;
	}
}