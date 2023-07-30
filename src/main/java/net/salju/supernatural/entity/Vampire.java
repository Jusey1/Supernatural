package net.salju.supernatural.entity;

import net.salju.supernatural.init.SupernaturalModSounds;
import net.salju.supernatural.init.SupernaturalMobs;
import net.salju.supernatural.init.SupernaturalEnchantments;
import net.salju.supernatural.init.SupernaturalConfig;

import net.minecraftforge.network.PlayMessages;

import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.entity.raid.Raid;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.monster.AbstractIllager;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.ai.util.GoalUtils;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.RestrictSunGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.BreakDoorGoal;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.Difficulty;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.nbt.CompoundTag;

import javax.annotation.Nullable;

import java.util.function.Predicate;
import java.util.EnumSet;

public class Vampire extends AbstractIllager {
	public Vampire(PlayMessages.SpawnEntity packet, Level world) {
		this(SupernaturalMobs.VAMPIRE.get(), world);
	}

	static final Predicate<Difficulty> DOOR_BREAKING_PREDICATE = (pip) -> {
		return pip == Difficulty.NORMAL || pip == Difficulty.HARD;
	};

	public Vampire(EntityType<Vampire> type, Level world) {
		super(type, world);
		this.setPersistenceRequired();
		xpReward = 8;
		((GroundPathNavigation) this.getNavigation()).setCanOpenDoors(true);
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.addGoal(0, new FloatGoal(this));
		this.goalSelector.addGoal(0, new RestrictSunGoal(this));
		this.goalSelector.addGoal(1, new Vampire.VampireBreakDoorGoal(this));
		this.goalSelector.addGoal(2, new AbstractIllager.RaiderOpenDoorGoal(this));
		this.goalSelector.addGoal(3, new Raider.HoldGroundAttackGoal(this, 10.0F));
		this.goalSelector.addGoal(4, new MeleeAttackGoal(this, 1.2, false));
		this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, Raider.class)).setAlertOthers());
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
		this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, AbstractVillager.class, true));
		this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
		this.goalSelector.addGoal(5, new RandomStrollGoal(this, 1));
		this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, LivingEntity.class, (float) 8));
		this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
	}

	protected void customServerAiStep() {
		if (!this.isNoAi() && GoalUtils.hasGroundPathNavigation(this)) {
			boolean flag = ((ServerLevel) this.level()).isRaided(this.blockPosition());
			((GroundPathNavigation) this.getNavigation()).setCanOpenDoors(flag);
		}
		super.customServerAiStep();
	}

	public void aiStep() {
		if (SupernaturalConfig.SUN.get() == false) {
			boolean flag = this.isSunBurnTick();
			if (flag) {
				this.setSecondsOnFire(8);
				this.hurt(this.damageSources().inFire(), 4);
			}
		}
		super.aiStep();
	}

	public AbstractIllager.IllagerArmPose getArmPose() {
		if (this.isAggressive()) {
			return AbstractIllager.IllagerArmPose.ATTACKING;
		} else {
			return this.isCelebrating() ? AbstractIllager.IllagerArmPose.CELEBRATING : AbstractIllager.IllagerArmPose.NEUTRAL;
		}
	}

	@Override
	public MobType getMobType() {
		return MobType.ILLAGER;
	}

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

	public void applyRaidBuffs(int imp, boolean boop) {
		Raid raid = this.getCurrentRaid();
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
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor world, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData livingdata, @Nullable CompoundTag tag) {
		SpawnGroupData retval = super.finalizeSpawn(world, difficulty, reason, livingdata, tag);
		ItemStack sword = new ItemStack(Items.IRON_SWORD);
		sword.setCount(1);
		if (Math.random() >= 0.9) {
			sword.enchant(SupernaturalEnchantments.LEECHING.get(), 2);
		} else if (Math.random() >= 0.8) {
			sword.enchant(SupernaturalEnchantments.LEECHING.get(), 1);
		}
		this.setItemInHand(InteractionHand.MAIN_HAND, sword);
		return retval;
	}

	@Override
	public void baseTick() {
		super.baseTick();
		if (this.isAggressive()) {
			if (SupernaturalConfig.STRENGTH.get() == true)
				this.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 1200, 0, (false), (false)));
			if (SupernaturalConfig.SPEED.get() == true)
				this.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 1200, 0, (false), (false)));
		}
	}

	static class VampireBreakDoorGoal extends BreakDoorGoal {
		public VampireBreakDoorGoal(Mob vampy) {
			super(vampy, 6, Vampire.DOOR_BREAKING_PREDICATE);
			this.setFlags(EnumSet.of(Goal.Flag.MOVE));
		}

		public boolean canContinueToUse() {
			Vampire vampire = (Vampire) this.mob;
			return vampire.hasActiveRaid() && super.canContinueToUse();
		}

		public boolean canUse() {
			Vampire vampire = (Vampire) this.mob;
			return vampire.hasActiveRaid() && vampire.random.nextInt(reducedTickDelay(10)) == 0 && super.canUse();
		}

		public void start() {
			super.start();
			this.mob.setNoActionTime(0);
		}
	}

	public static AttributeSupplier.Builder createAttributes() {
		AttributeSupplier.Builder builder = Mob.createMobAttributes();
		builder = builder.add(Attributes.MOVEMENT_SPEED, 0.35);
		builder = builder.add(Attributes.FOLLOW_RANGE, 12.0D);
		builder = builder.add(Attributes.MAX_HEALTH, 24);
		builder = builder.add(Attributes.ARMOR, 0);
		builder = builder.add(Attributes.ATTACK_DAMAGE, 3);
		return builder;
	}
}