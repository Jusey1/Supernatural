package net.salju.supernatural.entity;

import net.salju.supernatural.entity.ai.targets.SpookyAttackSelector;
import net.salju.supernatural.init.SupernaturalSounds;
import net.salju.supernatural.init.SupernaturalMobs;
import net.salju.supernatural.init.SupernaturalEffects;
import net.salju.supernatural.init.SupernaturalConfig;
import net.salju.supernatural.events.SupernaturalManager;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundEvent;
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
import net.minecraft.world.entity.*;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.damagesource.DamageSource;
import java.util.Optional;

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
		this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.8, false));
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
						armor.setOwnerDirectly(this.getOwner());
						armor.setItemSlot(EquipmentSlot.MAINHAND, sword);
					}
					double r = this.random.nextGaussian() * 0.02D;
					lvl.sendParticles(ParticleTypes.POOF, this.getRandomX(1.0D), this.getRandomY(), this.getRandomZ(1.0D), 10, r, r, r, 0.25);
					this.discard();
				}
			}
		}
		if (this.isAlive()) {
			if (this.getTarget() != null && this.getTarget().isAlive()) {
				if (this.getTarget().hasEffect(MobEffects.GLOWING) || this.getTarget().hasEffect(SupernaturalEffects.POSSESSION)) {
					this.setTarget(null);
				}
			}
			if (this.level() instanceof ServerLevel lvl && !this.isTamed() && this.level().isBrightOutside() && this.level().canSeeSkyFromBelowWater(BlockPos.containing(this.getX(), this.getY(), this.getZ()))) {
				this.playSound(SupernaturalSounds.SPOOK_POOF.get(), 1.0F, 1.0F);
				this.discard();
				double r = this.random.nextGaussian() * 0.02D;
				lvl.sendParticles(ParticleTypes.POOF, this.getRandomX(1.0D), this.getRandomY(), this.getRandomZ(1.0D), 10, r, r, r, 0.25);
			}
		}
	}

	@Override
	protected float getSoundVolume() {
		return 0.35F;
	}

	@Override
	public boolean causeFallDamage(double d, float f, DamageSource source) {
		return false;
	}

	@Override
	public void setNoGravity(boolean check) {
		super.setNoGravity(true);
	}
}