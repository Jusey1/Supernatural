package net.salju.supernatural.entity;

import net.salju.supernatural.init.SupernaturalModSounds;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.TridentItem;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Drowned;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.RangedAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomSwimmingGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.GlowSquid;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.util.Mth;
import net.minecraft.tags.FluidTags;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.BlockPos;

import javax.annotation.Nullable;

import java.util.function.Predicate;

public class AbstractMerEntity extends Monster implements RangedAttackMob {
	public boolean merTick = true;

	public AbstractMerEntity(EntityType<? extends Monster> type, Level world) {
		super(type, world);
		xpReward = 5;
		this.moveControl = new AbstractMerEntity.MerMoveControl(this);
		this.setPathfindingMalus(BlockPathTypes.WATER, 0.0F);
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.addGoal(1, new AbstractMerEntity.MerTridentAttackGoal(this, 1.0D, 40, 10.0F));
		this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.6, false));
		this.goalSelector.addGoal(3, new AbstractMerEntity.MerSwimGoal(this));
		this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, LivingEntity.class, (float) 6));
		this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
		this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 12, true, true, new AbstractMerEntity.MerAttackSelector(this)));
	}

	@Override
	public double getMyRidingOffset() {
		return this.isBaby() ? 0.0D : -0.15D;
	}

	@Override
	protected PathNavigation createNavigation(Level lvl) {
		return new WaterBoundPathNavigation(this, lvl);
	}

	@Override
	public void performRangedAttack(LivingEntity target, float tri) {
		if (this.getMainHandItem().getItem() instanceof TridentItem) {
			ThrownTrident throwntrident = new ThrownTrident(this.level(), this, new ItemStack(Items.TRIDENT));
			double d0 = target.getX() - this.getX();
			double d1 = target.getY(0.3333333333333333D) - throwntrident.getY();
			double d2 = target.getZ() - this.getZ();
			double d3 = Math.sqrt(d0 * d0 + d2 * d2);
			throwntrident.shoot(d0, d1 + d3 * (double) 0.2F, d2, 1.6F, (float) (14 - this.level().getDifficulty().getId() * 4));
			this.playSound(SoundEvents.DROWNED_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
			this.level().addFreshEntity(throwntrident);
		}
	}

	@Override
	public MobType getMobType() {
		return MobType.WATER;
	}

	@Override
	public boolean removeWhenFarAway(double distanceToClosestPlayer) {
		return true;
	}

	@Override
	public void baseTick() {
		int i = this.getAirSupply();
		super.baseTick();
		this.handleAirSupply(i);
	}

	@Override
	public boolean canBreatheUnderwater() {
		return true;
	}

	@Override
	public boolean checkSpawnObstruction(LevelReader world) {
		return world.isUnobstructed(this);
	}

	@Override
	public boolean isPushedByFluid() {
		return false;
	}

	@Override
	public void travel(Vec3 vec) {
		if (this.isEffectiveAi() && this.isInWater()) {
			this.moveRelative(0.01F, vec);
			this.move(MoverType.SELF, this.getDeltaMovement());
			this.setDeltaMovement(this.getDeltaMovement().scale(0.9D));
			if (this.getTarget() == null) {
				this.setDeltaMovement(this.getDeltaMovement().add(0.0D, -0.005D, 0.0D));
			}
		} else {
			super.travel(vec);
		}
	}

	boolean wantsToSwim() {
		LivingEntity livingentity = this.getTarget();
		return livingentity != null && livingentity.isInWater();
	}

	@Override
	public void updateSwimming() {
		if (!this.level().isClientSide) {
			if (this.isEffectiveAi() && this.isInWater() && this.wantsToSwim()) {
				this.setSwimming(true);
			} else {
				this.setSwimming(false);
			}
		}
	}

	protected boolean closeToNextPos() {
		Path path = this.getNavigation().getPath();
		if (path != null) {
			BlockPos blockpos = path.getTarget();
			if (blockpos != null) {
				double d0 = this.distanceToSqr((double) blockpos.getX(), (double) blockpos.getY(), (double) blockpos.getZ());
				if (d0 < 4.0D) {
					return true;
				}
			}
		}
		return false;
	}

	protected boolean canRandomSwim() {
		return true;
	}

	protected void handleAirSupply(int air) {
		if (this.isAlive() && !this.isInWaterOrBubble()) {
			this.setAirSupply(air - 1);
			if (this.getAirSupply() == -20) {
				this.setAirSupply(0);
				this.hurt(this.damageSources().drown(), 2.0F);
			}
		} else {
			this.setAirSupply(300);
		}
	}

	@Override
	public SoundEvent getAmbientSound() {
		if (this.isInWater()) {
			return SupernaturalModSounds.MERWATER_IDLE.get();
		} else {
			return SupernaturalModSounds.MERLAND_IDLE.get();
		}
	}

	@Override
	public void playStepSound(BlockPos pos, BlockState blockIn) {
		if (this.isInWater()) {
			this.playSound(SoundEvents.DROWNED_SWIM, 0.15f, 1);
		}
	}

	@Override
	public SoundEvent getHurtSound(DamageSource ds) {
		if (this.isInWater()) {
			return SupernaturalModSounds.MERWATER_HURT.get();
		} else {
			return SupernaturalModSounds.MERLAND_HURT.get();
		}
	}

	@Override
	public SoundEvent getDeathSound() {
		if (this.isInWater()) {
			return SupernaturalModSounds.MERWATER_DEATH.get();
		} else {
			return SupernaturalModSounds.MERLAND_DEATH.get();
		}
	}

	@Override
	public boolean doHurtTarget(Entity target) {
		if (super.doHurtTarget(target)) {
			if (this.isLeftHanded()) {
				this.setLeftHanded(false);
			} else {
				this.setLeftHanded(true);
			}
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean hurt(DamageSource source, float amount) {
		if (source.getDirectEntity() instanceof Projectile) {
			Projectile proj = (Projectile) source.getDirectEntity();
			if (proj.getOwner() instanceof AbstractMerEntity) {
				return false;
			}
		} else if (source.getDirectEntity() instanceof Drowned) {
			return super.hurt(source, 1);
		}
		return super.hurt(source, amount);
	}

	@Override
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor world, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData livingdata, @Nullable CompoundTag tag) {
		SpawnGroupData retval = super.finalizeSpawn(world, difficulty, reason, livingdata, tag);
		if (!(this instanceof MerAmethystEntity)) {
			ItemStack trident = new ItemStack(Items.TRIDENT);
			trident.setCount(1);
			this.setItemInHand(InteractionHand.MAIN_HAND, trident);
		}
		this.merTick = false;
		return retval;
	}

	static class MerMoveControl extends MoveControl {
		private final AbstractMerEntity mer;

		public MerMoveControl(AbstractMerEntity ent) {
			super(ent);
			this.mer = ent;
		}

		public void tick() {
			if (this.mer.isEyeInFluid(FluidTags.WATER)) {
				this.mer.setDeltaMovement(this.mer.getDeltaMovement().add(0.0D, 0.005D, 0.0D));
			}
			if (this.operation == MoveControl.Operation.MOVE_TO && !this.mer.getNavigation().isDone()) {
				float f = (float) (this.speedModifier * this.mer.getAttributeValue(Attributes.MOVEMENT_SPEED));
				this.mer.setSpeed(Mth.lerp(0.125F, this.mer.getSpeed(), f));
				double d0 = this.wantedX - this.mer.getX();
				double d1 = this.wantedY - this.mer.getY();
				double d2 = this.wantedZ - this.mer.getZ();
				double d3 = Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
				if (d1 != 0.0D) {
					if (this.mer.isAggressive()) {
						this.mer.setDeltaMovement(this.mer.getDeltaMovement().add(0.0D, (double) this.mer.getSpeed() * (d1 / d3) * 0.06D, 0.0D));
					} else {
						this.mer.setDeltaMovement(this.mer.getDeltaMovement().add(0.0D, (double) this.mer.getSpeed() * (d1 / d3) * 0.035D, 0.0D));
					}
				}
				if (d0 != 0.0D || d2 != 0.0D) {
					float f1 = (float) (Mth.atan2(d2, d0) * (double) (180F / (float) Math.PI)) - 90.0F;
					this.mer.setYRot(this.rotlerp(this.mer.getYRot(), f1, 90.0F));
					this.mer.yBodyRot = this.mer.getYRot();
					if (this.mer.isAggressive()) {
						this.mer.setDeltaMovement(this.mer.getDeltaMovement().add((double) this.mer.getSpeed() * (d0 / d3) * 0.06D, 0.0D, (double) this.mer.getSpeed() * (d2 / d3) * 0.06D));
					} else {
						this.mer.setDeltaMovement(this.mer.getDeltaMovement().add((double) this.mer.getSpeed() * (d0 / d3) * 0.035D, 0.0D, (double) this.mer.getSpeed() * (d2 / d3) * 0.035D));
					}
				}
			} else {
				this.mer.setSpeed(0.0F);
			}
		}
	}

	static class MerSwimGoal extends RandomSwimmingGoal {
		private final AbstractMerEntity mer;

		public MerSwimGoal(AbstractMerEntity ent) {
			super(ent, 1.0D, 40);
			this.mer = ent;
		}

		public boolean canUse() {
			return this.mer.canRandomSwim() && super.canUse() && !this.mer.isAggressive();
		}
	}

	static class MerTridentAttackGoal extends RangedAttackGoal {
		private final AbstractMerEntity mer;

		public MerTridentAttackGoal(RangedAttackMob mob, double tri, int tru, float tre) {
			super(mob, tri, tru, tre);
			this.mer = (AbstractMerEntity) mob;
		}

		public boolean canUse() {
			return super.canUse() && this.mer.getMainHandItem().getItem() == Items.TRIDENT;
		}

		public void start() {
			super.start();
			this.mer.setAggressive(true);
			this.mer.startUsingItem(InteractionHand.MAIN_HAND);
		}

		public void stop() {
			super.stop();
			this.mer.stopUsingItem();
			this.mer.setAggressive(false);
		}
	}

	static class MerAttackSelector implements Predicate<LivingEntity> {
		private final AbstractMerEntity mer;

		public MerAttackSelector(AbstractMerEntity source) {
			this.mer = source;
		}

		public boolean test(@Nullable LivingEntity target) {
			return (target instanceof Player || target instanceof Drowned || target instanceof GlowSquid);
		}
	}
}
