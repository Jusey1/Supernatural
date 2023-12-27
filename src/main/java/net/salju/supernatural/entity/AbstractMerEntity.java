package net.salju.supernatural.entity;

import net.salju.supernatural.init.SupernaturalTags;
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
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.item.ItemEntity;
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
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.util.Mth;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.BlockPos;

import javax.annotation.Nullable;
import java.util.function.Predicate;
import java.util.UUID;

public class AbstractMerEntity extends Monster implements RangedAttackMob {
	private ItemStack trident = new ItemStack(Items.TRIDENT);
	private UUID thrownTrident;
	private int cooldown;

	public AbstractMerEntity(EntityType<? extends Monster> type, Level world) {
		super(type, world);
		this.xpReward = 5;
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
	public void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);
		tag.put("Trident", this.trident.save(new CompoundTag()));
		if (this.thrownTrident != null) {
			tag.putUUID("TridentUUID", this.thrownTrident);
		}
		tag.putInt("CD", this.cooldown);
	}

	@Override
	public void readAdditionalSaveData(CompoundTag tag) {
		super.readAdditionalSaveData(tag);
		if (tag.contains("Trident")) {
			ItemStack stack = ItemStack.of(tag.getCompound("Trident"));
			this.trident = stack;
		}
		if (tag.contains("TridentUUID")) {
			UUID saved = tag.getUUID("TridentUUID");
			this.thrownTrident = saved;
		}
		if (tag.contains("CD")) {
			int saved = tag.getInt("CD");
			this.cooldown = saved;
		}
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
			ThrownTrident proj = new ThrownTrident(this.level(), this, new ItemStack(Items.TRIDENT));
			double d0 = target.getX() - this.getX();
			double d1 = target.getY(0.3333333333333333D) - proj.getY();
			double d2 = target.getZ() - this.getZ();
			double d3 = Math.sqrt(d0 * d0 + d2 * d2);
			proj.shoot(d0, d1 + d3 * (double) 0.2F, d2, 1.6F, (float) (14 - this.level().getDifficulty().getId() * 4));
			this.playSound(SoundEvents.DROWNED_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
			this.level().addFreshEntity(proj);
			this.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
			this.thrownTrident = proj.getUUID();
			this.setCD(1200);
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
		super.baseTick();
		this.handleAirSupply(this.getAirSupply());
		if (!this.level().isClientSide() && this.isAlive() && this.isEffectiveAi()) {
			this.checkTrident();
			if (this.cooldown > 0) {
				--this.cooldown;
			}
		}
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

	protected void checkTrident() {
		if (this.thrownTrident != null && this.level() instanceof ServerLevel lvl && this.cooldown <= 1180) {
			if (lvl.getEntity(this.thrownTrident) instanceof ThrownTrident proj && proj.getOwner().is(this)) {
				if (this.distanceTo(proj) < 2.0D || this.cooldown == 1) {
					this.giveTrident(proj);
				} else if (this.distanceTo(proj) < 32.0D && this.getTarget() == null) {
					this.getNavigation().moveTo(proj, 1.0F);
				}
			} else if (lvl.getEntity(this.thrownTrident) == null && this.cooldown <= 1) {
				this.giveTrident(null);
			}
		}
	}

	protected void giveTrident(@Nullable ThrownTrident proj) {
		this.setItemInHand(InteractionHand.MAIN_HAND, this.trident);
		this.thrownTrident = null;
		if (this.cooldown > 1 && proj != null) {
			this.setCD(0);
			proj.discard();
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

	public void setCD(int i) {
		this.cooldown = i;
	}

	public int getCD() {
		return this.cooldown;
	}

	protected boolean canRandomSwim() {
		return true;
	}

	protected boolean wantsToSwim() {
		return this.getTarget() != null && this.getTarget().isInWater();
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
	public void playStepSound(BlockPos pos, BlockState state) {
		if (this.isInWater()) {
			this.playSound(SoundEvents.DROWNED_SWIM, 0.15f, 1);
		}
	}

	@Override
	public SoundEvent getAmbientSound() {
		return (this.isInWater() ? SupernaturalModSounds.MERWATER_IDLE.get() : SupernaturalModSounds.MERLAND_IDLE.get());
	}

	@Override
	public SoundEvent getHurtSound(DamageSource ds) {
		return (this.isInWater() ? SupernaturalModSounds.MERWATER_HURT.get() : SupernaturalModSounds.MERLAND_HURT.get());
	}

	@Override
	public SoundEvent getDeathSound() {
		return (this.isInWater() ? SupernaturalModSounds.MERWATER_DEATH.get() : SupernaturalModSounds.MERLAND_DEATH.get());
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
		}
		return false;
	}

	@Override
	public void die(DamageSource source) {
		if (this.level() instanceof ServerLevel lvl && this.getMainHandItem().isEmpty() && Math.random() >= 0.95) {
			ItemEntity item = new ItemEntity(lvl, this.getX(), this.getY(), this.getZ(), this.trident);
			item.setPickUpDelay(10);
			lvl.addFreshEntity(item);
		}
		super.die(source);
	}

	@Override
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor world, DifficultyInstance souls, MobSpawnType reason, @Nullable SpawnGroupData data, @Nullable CompoundTag tag) {
		this.setItemInHand(InteractionHand.MAIN_HAND, this.trident);
		return super.finalizeSpawn(world, souls, reason, data, tag);
	}

	static class MerMoveControl extends MoveControl {
		private final AbstractMerEntity mer;

		public MerMoveControl(AbstractMerEntity ent) {
			super(ent);
			this.mer = ent;
		}

		public void tick() {
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

		public MerTridentAttackGoal(RangedAttackMob mob, double d, int i, float f) {
			super(mob, d, i, f);
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
			return (target instanceof Player || target.getType().is(SupernaturalTags.MERRY));
		}
	}
}