package net.salju.supernatural.entity;

import net.salju.supernatural.init.SupernaturalEffects;
import net.salju.supernatural.init.SupernaturalSounds;
import net.salju.supernatural.init.SupernaturalDamageTypes;
import net.salju.supernatural.init.SupernaturalConfig;
import net.salju.supernatural.events.SupernaturalManager;
import net.salju.supernatural.entity.ai.abstractai.AbstractSpellcasterGoal;
import net.salju.supernatural.entity.ai.vampire.*;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.monster.illager.AbstractIllager;
import net.minecraft.world.entity.ai.util.GoalUtils;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.ServerLevelAccessor;
import javax.annotation.Nullable;

public class Vampire extends AbstractIllager implements Spellcaster {
    private static final EntityDataAccessor<Boolean> NECROMANCER = SynchedEntityData.defineId(Vampire.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Integer> SPELL_TICK = SynchedEntityData.defineId(Vampire.class, EntityDataSerializers.INT);

	public Vampire(EntityType<Vampire> type, Level world) {
		super(type, world);
        this.getNavigation().setCanOpenDoors(true);
		this.xpReward = 10;
	}

    @Override
    public void addAdditionalSaveData(ValueOutput tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("Necromancer", this.isNecromancer());
        tag.putInt("SpellTick", this.getSpellTick());
    }

    @Override
    public void readAdditionalSaveData(ValueInput tag) {
        super.readAdditionalSaveData(tag);
        this.getEntityData().set(NECROMANCER, tag.getBooleanOr("Necromancer", false));
        this.setSpellTick(tag.getIntOr("SpellTick", 0));
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(NECROMANCER, false);
        builder.define(SPELL_TICK, 0);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(0, new RestrictSunGoal(this));
        this.goalSelector.addGoal(0, new AbstractSpellcasterGoal(this));
        this.goalSelector.addGoal(2, new VampireBloodSpellGoal(this));
        this.goalSelector.addGoal(3, new AbstractIllager.RaiderOpenDoorGoal(this));
        this.goalSelector.addGoal(3, new Raider.HoldGroundAttackGoal(this, 10.0F));
        this.goalSelector.addGoal(4, new MeleeAttackGoal(this, 1.2, false));
        this.goalSelector.addGoal(4, new RandomStrollGoal(this, 1));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this, Raider.class).setAlertOthers());
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 12, true, false, new VampireAttackSelector(this)));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, LivingEntity.class, 8));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor world, DifficultyInstance difficulty, EntitySpawnReason reason, @Nullable SpawnGroupData data) {
        this.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.IRON_SWORD));
        this.setDropChance(EquipmentSlot.MAINHAND, 0.0F);
        if (Mth.nextInt(this.getRandom(), 1, 100) >= 85) {
            this.getEntityData().set(NECROMANCER, true);
            this.goalSelector.addGoal(1, new NecromancerBoltSpellGoal(this));
        }
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

    @Override
    protected void customServerAiStep(ServerLevel lvl) {
        if (!this.isNoAi() && GoalUtils.hasGroundPathNavigation(this)) {
            this.getNavigation().setCanOpenDoors(lvl.isRaided(this.blockPosition()));
        }
        if (this.isCastingSpell()) {
            this.navigation.stop();
        }
        super.customServerAiStep(lvl);
    }

    @Override
    public void aiStep() {
        if (this.isAlive() && this.level() instanceof ServerLevel lvl) {
            if (SupernaturalManager.shouldVampireBurn(this, lvl) && this.getItemBySlot(EquipmentSlot.HEAD).isEmpty()) {
                if (this.getRemainingFireTicks() <= 20) {
                    this.setRemainingFireTicks(120);
                    this.hurt(SupernaturalDamageTypes.causeSunDamage(this.level().registryAccess()), 3);
                }
            }
        }
        super.aiStep();
    }

    @Override
    public void baseTick() {
        super.baseTick();
        this.updateSpellTick();
        if (this.level().isClientSide()) {
            if (this.isCastingSpell()) {
                float f = this.yBodyRot * ((float) Math.PI / 180F) + Mth.cos((float) this.tickCount * 0.6662F) * 0.25F;
                this.applySpellEffects(this.level(), this.getX() - (double) Mth.cos(f) * 0.6 * (double) this.getScale(), this.getY() + 1.8 * (double) this.getScale(), this.getZ() - (double) Mth.sin(f) * 0.6 * (double) this.getScale());
                this.applySpellEffects(this.level(), this.getX() + (double) Mth.cos(f) * 0.6 * (double) this.getScale(), this.getY() + 1.8 * (double) this.getScale(), this.getZ() + (double) Mth.sin(f) * 0.6 * (double) this.getScale());
            }
        }
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

    @Override
    public void setSpellTick(int i) {
        this.getEntityData().set(SPELL_TICK, i);
    }

    @Override
    public int getSpellTick() {
        return this.getEntityData().get(SPELL_TICK);
    }

    public boolean isNecromancer() {
        return this.getEntityData().get(NECROMANCER);
    }
}