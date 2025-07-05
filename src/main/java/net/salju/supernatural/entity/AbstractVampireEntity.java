package net.salju.supernatural.entity;

import net.salju.supernatural.init.SupernaturalEffects;
import net.salju.supernatural.init.SupernaturalSounds;
import net.salju.supernatural.init.SupernaturalDamageTypes;
import net.salju.supernatural.init.SupernaturalConfig;
import net.salju.supernatural.events.SupernaturalManager;
import net.salju.supernatural.entity.ai.targets.VampireAttackSelector;
import net.salju.supernatural.entity.ai.spells.*;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.monster.SpellcasterIllager;
import net.minecraft.world.entity.monster.AbstractIllager;
import net.minecraft.world.entity.ai.util.GoalUtils;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.RestrictSunGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.ServerLevelAccessor;
import javax.annotation.Nullable;

public class AbstractVampireEntity extends SpellcasterIllager {
	public AbstractVampireEntity(EntityType<? extends AbstractVampireEntity> type, Level world) {
		super(type, world);
		this.setPersistenceRequired();
		this.getNavigation().setCanOpenDoors(true);
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
		this.targetSelector.addGoal(1, new HurtByTargetGoal(this, Raider.class).setAlertOthers());
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 12, true, false, new VampireAttackSelector(this)));
		this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, LivingEntity.class, 8));
		this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
	}

	@Override
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor world, DifficultyInstance difficulty, EntitySpawnReason reason, @Nullable SpawnGroupData data) {
		this.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.IRON_SWORD));
		this.setDropChance(EquipmentSlot.MAINHAND, 0.0F);
		return super.finalizeSpawn(world, difficulty, reason, data);
	}

	@Override
	public boolean doHurtTarget(ServerLevel lvl, Entity entity) {
		if (entity instanceof Player player && !SupernaturalManager.isVampire(player)) {
			if (Math.random() <= SupernaturalConfig.ATTACKED.get()) {
				player.addEffect(new MobEffectInstance(SupernaturalEffects.VAMPIRISM, 24000, 0));
			}
		}
		return super.doHurtTarget(lvl, entity);
	}

	protected void customServerAiStep(ServerLevel lvl) {
		if (!this.isNoAi() && GoalUtils.hasGroundPathNavigation(this)) {
			this.getNavigation().setCanOpenDoors(lvl.isRaided(this.blockPosition()));
		}
		if (this.isCastingSpell()) {
			this.navigation.stop();
		}
		super.customServerAiStep(lvl);
	}

	public void aiStep() {
		if (this.isAlive()) {
			if (!SupernaturalConfig.SUN.get() && this.isSunBurnTick() && !this.hasEffect(MobEffects.FIRE_RESISTANCE)) {
				if (this.getRemainingFireTicks() <= 20) {
					this.setRemainingFireTicks(120);
					this.hurt(SupernaturalDamageTypes.causeSunDamage(this.level().registryAccess()), 3);
				}
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
	public boolean removeWhenFarAway(double d) {
		return false;
	}

	@Override
	public void applyRaidBuffs(ServerLevel lvl, int i, boolean check) {
		//
	}

	@Override
	public SoundEvent getAmbientSound() {
		return SupernaturalSounds.VAMPIRE_IDLE.get();
	}

	@Override
	public SoundEvent getHurtSound(DamageSource source) {
		return SupernaturalSounds.VAMPIRE_HURT.get();
	}

	@Override
	public SoundEvent getDeathSound() {
		return SupernaturalSounds.VAMPIRE_DEATH.get();
	}

	@Override
	public SoundEvent getCelebrateSound() {
		return SupernaturalSounds.VAMPIRE_CELEBRATE.get();
	}

	@Override
	public SoundEvent getCastingSoundEvent() {
		return SoundEvents.EVOKER_CAST_SPELL;
	}
}