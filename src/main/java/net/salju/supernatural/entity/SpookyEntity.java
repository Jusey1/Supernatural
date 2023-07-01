
package net.salju.supernatural.entity;

import net.salju.supernatural.procedures.SupernaturalHelpersProcedure;
import net.salju.supernatural.init.SupernaturalModSounds;
import net.salju.supernatural.init.SupernaturalModMobEffects;
import net.salju.supernatural.init.SupernaturalModEntities;
import net.salju.supernatural.init.SupernaturalConfig;

import net.minecraftforge.network.PlayMessages;
import net.minecraftforge.event.ForgeEventFactory;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomFlyingGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.tags.TagKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.BlockPos;

import javax.annotation.Nullable;

import java.util.function.Predicate;

public class SpookyEntity extends Monster {
	public SpookyEntity(PlayMessages.SpawnEntity packet, Level world) {
		this(SupernaturalModEntities.SPOOKY.get(), world);
	}

	public SpookyEntity(EntityType<SpookyEntity> type, Level world) {
		super(type, world);
		this.moveControl = new FlyingMoveControl(this, 20, true);
		xpReward = 2;
	}

	private static final Predicate<Entity> AVOID_PLAYERS = (player) -> {
		return !player.isDiscrete() && EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(player);
	};

	@Override
	protected PathNavigation createNavigation(Level lvl) {
		FlyingPathNavigation scary = new FlyingPathNavigation(this, lvl);
		scary.setCanOpenDoors(false);
		scary.setCanFloat(true);
		scary.setCanPassDoors(true);
		return scary;
	}

	protected float getStandingEyeHeight(Pose pose, EntityDimensions ent) {
		return ent.height * 0.6F;
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.addGoal(0, new FloatGoal(this));
		this.goalSelector.addGoal(1, new AvoidEntityGoal<>(this, Player.class, (float) 4, 1.4, 1.2, (player) -> {
			return AVOID_PLAYERS.test(player);
		}));
		this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.8, false));
		this.goalSelector.addGoal(3, new RandomLookAroundGoal(this));
		this.goalSelector.addGoal(4, new WaterAvoidingRandomFlyingGoal(this, 1.2));
		this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, LivingEntity.class, 8.0F));
		this.targetSelector.addGoal(0, new HurtByTargetGoal(this));
		this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 12, true, true, new SpookyEntity.SpookAttackSelector(this)));
	}

	@Override
	public MobType getMobType() {
		return MobType.UNDEAD;
	}

	@Override
	public boolean removeWhenFarAway(double distanceToClosestPlayer) {
		return !(this.isPersistenceRequired());
	}

	@Override
	public SoundEvent getAmbientSound() {
		return SoundEvents.VEX_AMBIENT;
	}

	@Override
	public SoundEvent getHurtSound(DamageSource ds) {
		return SoundEvents.VEX_HURT;
	}

	@Override
	public SoundEvent getDeathSound() {
		return SoundEvents.VEX_DEATH;
	}

	public void aiStep() {
		super.aiStep();
		this.setNoGravity(true);
	}

	@Override
	public boolean doHurtTarget(Entity entity) {
		double x = this.getX();
		double y = this.getY();
		double z = this.getZ();
		if (super.doHurtTarget(entity) && entity instanceof LivingEntity target) {
			if (target instanceof Animal || target instanceof Enemy) {
				target.addEffect(new MobEffectInstance(SupernaturalModMobEffects.POSSESSION.get(), 6000, 0));
			} else if (target instanceof Player) {
				target.addEffect(new MobEffectInstance(SupernaturalModMobEffects.POSSESSION.get(), 3000, 0));
				target.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 32, 0, (false), (false)));
			}
			if (!this.level().isClientSide()) {
				this.playSound(SupernaturalModSounds.SPOOK_POOF.get(), 1.0F, 1.0F);
				this.discard();
				if (this.level() instanceof ServerLevel lvl) {
					double r = this.random.nextGaussian() * 0.02D;
					lvl.sendParticles(ParticleTypes.POOF, this.getRandomX(1.0D), this.getRandomY(), this.getRandomZ(1.0D), 10, r, r, r, 0.25);
				}
			}
		}
		return false;
	}

	@Override
	public void baseTick() {
		super.baseTick();
		if (SupernaturalConfig.ARMOR.get() == true) {
			for (ArmorStand target : this.level().getEntitiesOfClass(ArmorStand.class, this.getBoundingBox().inflate(0.85D))) {
				LevelAccessor world = target.level();
				double x = target.getX();
				double y = target.getY();
				double z = target.getZ();
				if (!(target.getItemBySlot(EquipmentSlot.HEAD) == ItemStack.EMPTY) && !(target.getItemBySlot(EquipmentSlot.CHEST) == ItemStack.EMPTY) && !(target.getItemBySlot(EquipmentSlot.LEGS) == ItemStack.EMPTY)
						&& !(target.getItemBySlot(EquipmentSlot.FEET) == ItemStack.EMPTY)) {
					this.playSound(SupernaturalModSounds.SPOOK_POOF.get(), 1.0F, 1.0F);
					if (world instanceof ServerLevel lvl) {
						PossessedArmorEntity armor = SupernaturalHelpersProcedure.convertArmor(target, SupernaturalModEntities.POSSESSED_ARMOR.get(), true);
						ForgeEventFactory.onLivingConvert(target, armor);
						double r = this.random.nextGaussian() * 0.02D;
						lvl.sendParticles(ParticleTypes.POOF, this.getRandomX(1.0D), this.getRandomY(), this.getRandomZ(1.0D), 10, r, r, r, 0.25);
						this.discard();
					}
				} else {
					if (!(target.getItemBySlot(EquipmentSlot.HEAD) == ItemStack.EMPTY)) {
						if (world instanceof Level lvl && !lvl.isClientSide()) {
							ItemEntity armor = new ItemEntity(lvl, x, y, z, (target.getItemBySlot(EquipmentSlot.HEAD)));
							armor.setPickUpDelay(10);
							lvl.addFreshEntity(armor);
						}
					}
					if (!(target.getItemBySlot(EquipmentSlot.CHEST) == ItemStack.EMPTY)) {
						if (world instanceof Level lvl && !lvl.isClientSide()) {
							ItemEntity armor = new ItemEntity(lvl, x, y, z, (target.getItemBySlot(EquipmentSlot.CHEST)));
							armor.setPickUpDelay(10);
							lvl.addFreshEntity(armor);
						}
					}
					if (!(target.getItemBySlot(EquipmentSlot.LEGS) == ItemStack.EMPTY)) {
						if (world instanceof Level lvl && !lvl.isClientSide()) {
							ItemEntity armor = new ItemEntity(lvl, x, y, z, (target.getItemBySlot(EquipmentSlot.LEGS)));
							armor.setPickUpDelay(10);
							lvl.addFreshEntity(armor);
						}
					}
					if (!(target.getItemBySlot(EquipmentSlot.FEET) == ItemStack.EMPTY)) {
						if (world instanceof Level lvl && !lvl.isClientSide()) {
							ItemEntity armor = new ItemEntity(lvl, x, y, z, (target.getItemBySlot(EquipmentSlot.FEET)));
							armor.setPickUpDelay(10);
							lvl.addFreshEntity(armor);
						}
					}
					target.playSound(SoundEvents.ARMOR_STAND_BREAK, 1.0F, 1.0F);
					target.discard();
					if (world instanceof Level lvl && !lvl.isClientSide()) {
						ItemEntity armor = new ItemEntity(lvl, x, y, z, new ItemStack(Items.ARMOR_STAND));
						armor.setPickUpDelay(10);
						lvl.addFreshEntity(armor);
					}
				}
			}
		}
		if (this.isAlive()) {
			double x = this.getX();
			double y = this.getY();
			double z = this.getZ();
			if (!this.level().isClientSide() && this.level().isDay() && this.level().canSeeSkyFromBelowWater(BlockPos.containing(x, y, z))) {
				this.playSound(SupernaturalModSounds.SPOOK_POOF.get(), 1.0F, 1.0F);
				this.discard();
				if (this.level() instanceof ServerLevel lvl) {
					double r = this.random.nextGaussian() * 0.02D;
					lvl.sendParticles(ParticleTypes.POOF, this.getRandomX(1.0D), this.getRandomY(), this.getRandomZ(1.0D), 10, r, r, r, 0.25);
				}
			}
		}
	}

	@Override
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor world, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData livingdata, @Nullable CompoundTag tag) {
		SpawnGroupData retval = super.finalizeSpawn(world, difficulty, reason, livingdata, tag);
		double x = this.getX();
		double y = this.getY();
		double z = this.getZ();
		for (LivingEntity target : this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(0.56D))) {
			if (target instanceof Player || target instanceof Animal || target instanceof Monster billy && billy.isPersistenceRequired()) {
				this.setPersistenceRequired();
			}
		}
		return retval;
	}

	@Override
	protected float getSoundVolume() {
		return 0.35F;
	}

	@Override
	public boolean causeFallDamage(float l, float d, DamageSource source) {
		return false;
	}

	@Override
	protected void checkFallDamage(double y, boolean onGroundIn, BlockState state, BlockPos pos) {
	}

	@Override
	public void setNoGravity(boolean ignored) {
		super.setNoGravity(true);
	}

	public static void init() {
	}

	public static AttributeSupplier.Builder createAttributes() {
		AttributeSupplier.Builder builder = Mob.createMobAttributes();
		builder = builder.add(Attributes.MOVEMENT_SPEED, 0.2);
		builder = builder.add(Attributes.MAX_HEALTH, 12);
		builder = builder.add(Attributes.ATTACK_DAMAGE, (1 * 0.15));
		builder = builder.add(Attributes.FLYING_SPEED, 0.35);
		return builder;
	}

	static class SpookAttackSelector implements Predicate<LivingEntity> {
		private final SpookyEntity ghost;

		public SpookAttackSelector(SpookyEntity source) {
			this.ghost = source;
		}

		public boolean test(@Nullable LivingEntity target) {
			if (!target.hasEffect(SupernaturalModMobEffects.POSSESSION.get()) && !target.hasEffect(MobEffects.GLOWING) && !target.getType().is(TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation("supernatural:spook_no_possess")))) {
				return (target instanceof Animal || target instanceof Enemy || target instanceof ArmorStand && (SupernaturalConfig.ARMOR.get() == true));
			}
			return false;
		}
	}
}