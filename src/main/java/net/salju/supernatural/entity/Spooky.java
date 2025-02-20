package net.salju.supernatural.entity;

import net.salju.supernatural.init.SupernaturalTags;
import net.salju.supernatural.init.SupernaturalSounds;
import net.salju.supernatural.init.SupernaturalMobs;
import net.salju.supernatural.init.SupernaturalEffects;
import net.salju.supernatural.init.SupernaturalConfig;
import net.salju.supernatural.events.SupernaturalManager;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomFlyingGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.BlockPos;
import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;

public class Spooky extends PathfinderMob {
	private UUID friend;

	public Spooky(EntityType<Spooky> type, Level world) {
		super(type, world);
		this.moveControl = new FlyingMoveControl(this, 20, true);
	}

	@Override
	protected PathNavigation createNavigation(Level lvl) {
		FlyingPathNavigation scary = new FlyingPathNavigation(this, lvl);
		scary.setCanOpenDoors(false);
		scary.setCanFloat(true);
		scary.setCanPassDoors(true);
		return scary;
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.addGoal(0, new FloatGoal(this));
		this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.8, false));
		this.goalSelector.addGoal(3, new RandomLookAroundGoal(this));
		this.goalSelector.addGoal(4, new WaterAvoidingRandomFlyingGoal(this, 1.2));
		this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, LivingEntity.class, 8.0F));
		this.targetSelector.addGoal(0, new HurtByTargetGoal(this));
		this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 12, true, true, new Spooky.SpookAttackSelector()));
	}

	@Override
	public void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);
		tag.putUUID("Player", this.friend);
	}

	@Override
	public void readAdditionalSaveData(CompoundTag tag) {
		super.readAdditionalSaveData(tag);
		if (tag.contains("Player")) {
			this.friend = tag.getUUID("Player");
		}
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
	public SoundEvent getHurtSound(DamageSource source) {
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
	public boolean doHurtTarget(ServerLevel lvl, Entity entity) {
		if (entity instanceof LivingEntity target) {
			target.addEffect(new MobEffectInstance(SupernaturalEffects.POSSESSION, 6000, 0));
			target.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 32, 0, false, false));
			this.playSound(SupernaturalSounds.SPOOK_POOF.get(), 1.0F, 1.0F);
			this.discard();
			double r = this.random.nextGaussian() * 0.02D;
			lvl.sendParticles(ParticleTypes.POOF, this.getRandomX(1.0D), this.getRandomY(), this.getRandomZ(1.0D), 10, r, r, r, 0.25);
		}
		return super.doHurtTarget(lvl, entity);
	}

	@Override
	public void baseTick() {
		super.baseTick();
		if (SupernaturalConfig.ARMOR.get() && this.level() instanceof ServerLevel lvl) {
			ArmorStand target = lvl.getNearestEntity(ArmorStand.class, TargetingConditions.DEFAULT, this, this.getX(), this.getY(), this.getZ(), this.getBoundingBox().inflate(0.85D));
			if (target != null) {
				if (SupernaturalManager.hasArmor(target)) {
					this.playSound(SupernaturalSounds.SPOOK_POOF.get(), 1.0F, 1.0F);
					PossessedArmor armor = SupernaturalManager.convertArmor(target, SupernaturalMobs.POSSESSED_ARMOR.get(), true);
					if (this.getOwner() != null) {
						ItemStack sword = new ItemStack(Items.IRON_SWORD);
						EnchantmentHelper.enchantItem(this.getRandom(), sword, 32, lvl.registryAccess(), Optional.empty());
						armor.setOwner(this.getOwner());
						armor.setItemSlot(EquipmentSlot.MAINHAND, sword);
					}
					double r = this.random.nextGaussian() * 0.02D;
					lvl.sendParticles(ParticleTypes.POOF, this.getRandomX(1.0D), this.getRandomY(), this.getRandomZ(1.0D), 10, r, r, r, 0.25);
					this.discard();
				}
			}
		}
		if (this.isAlive()) {
			double x = this.getX();
			double y = this.getY();
			double z = this.getZ();
			if (this.level() instanceof ServerLevel lvl && this.level().isDay() && this.level().canSeeSkyFromBelowWater(BlockPos.containing(x, y, z))) {
				this.playSound(SupernaturalSounds.SPOOK_POOF.get(), 1.0F, 1.0F);
				this.discard();
				double r = this.random.nextGaussian() * 0.02D;
				lvl.sendParticles(ParticleTypes.POOF, this.getRandomX(1.0D), this.getRandomY(), this.getRandomZ(1.0D), 10, r, r, r, 0.25);
			}
		}
	}

	@Override
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor world, DifficultyInstance difficulty, EntitySpawnReason reason, @Nullable SpawnGroupData data) {
		if (this.level() instanceof ServerLevel lvl) {
			Mob target = lvl.getNearestEntity(Mob.class, TargetingConditions.DEFAULT, this, this.getX(), this.getY(), this.getZ(), this.getBoundingBox().inflate(0.76D));
			if (target != null && target.isPersistenceRequired()) {
				this.setPersistenceRequired();
			}
		}
		return super.finalizeSpawn(world, difficulty, reason, data);
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
	public void setNoGravity(boolean check) {
		super.setNoGravity(true);
	}

	public void setOwner(UUID player) {
		this.friend = player;
	}

	public UUID getOwner() {
		return this.friend;
	}

	public static AttributeSupplier.Builder createAttributes() {
		return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 12).add(Attributes.ATTACK_DAMAGE, 0.15).add(Attributes.MOVEMENT_SPEED, 0.2).add(Attributes.FLYING_SPEED, 0.35);
	}

	static class SpookAttackSelector implements TargetingConditions.Selector {
		public SpookAttackSelector() {
			//
		}

		public boolean test(@Nullable LivingEntity target, ServerLevel lvl) {
			if (!target.hasEffect(SupernaturalEffects.POSSESSION) && !target.hasEffect(MobEffects.GLOWING)) {
				if (target instanceof ArmorStand) {
					return SupernaturalManager.hasArmor(target) && SupernaturalConfig.ARMOR.get();
				}
				return target.getType().is(SupernaturalTags.SPOOKY);
			}
			return false;
		}
	}
}