package net.salju.supernatural.entity;

import net.salju.supernatural.init.SupernaturalSounds;
import net.salju.supernatural.entity.ai.abstractai.AbstractSpellcasterGoal;
import net.salju.supernatural.entity.ai.revenant.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.DifficultyInstance;
import javax.annotation.Nullable;

public class Revenant extends AbstractSpellcasterEntity implements Enemy {
    public static final EntityDataAccessor<Integer> WEAK_TICK = SynchedEntityData.defineId(Revenant.class, EntityDataSerializers.INT);

	public Revenant(EntityType<Revenant> type, Level world) {
		super(type, world);
        this.moveControl = new FlyingMoveControl(this, 45, true);
        this.setPersistenceRequired();
	}

    @Override
    public void addAdditionalSaveData(ValueOutput tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("WeaknessTick", this.getWeaknessTick());
    }

    @Override
    public void readAdditionalSaveData(ValueInput tag) {
        super.readAdditionalSaveData(tag);
        this.setWeaknessTick(tag.getIntOr("WeaknessTick", 0));
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(WEAK_TICK, 0);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new RevenantStareGoal(this));
        this.goalSelector.addGoal(0, new AbstractSpellcasterGoal(this));
        this.goalSelector.addGoal(0, new RevenantCorruptSpellGoal(this));
        this.goalSelector.addGoal(1, new RevenantSacrificeSpellGoal(this));
        this.goalSelector.addGoal(2, new RevenantFireSpellGoal(this));
        this.goalSelector.addGoal(4, new RevenantRandomFlyingGoal(this, 1.2));
        this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, LivingEntity.class, 8));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, false));
    }

    @Override
    protected PathNavigation createNavigation(Level lvl) {
        return new RevenantPathNavigation(this, lvl);
    }

    @Override
    protected void onInsideBlock(BlockState state) {
        super.onInsideBlock(state);
        if (!state.isAir()) {
            if (this.getTarget() != null && this.getTarget().isAlive()) {
                this.getMoveControl().setWantedPosition(this.getTarget().getRandomX(8.2), this.getTarget().getRandomY(), this.getTarget().getRandomZ(8.2), 1.2);
            } else {
                this.getMoveControl().setWantedPosition(this.getX(), this.getY() + 1.2, this.getZ(), 1.2);
            }
        }
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor lvl, DifficultyInstance difficulty, EntitySpawnReason reason, @Nullable SpawnGroupData data) {
        this.setWeaknessTick(5);
        return super.finalizeSpawn(lvl, difficulty, reason, data);
    }

    @Override
    public void tick() {
        this.setNoGravity(true);
        if (this.isInvisible()) {
            this.noPhysics = true;
            super.tick();
            this.noPhysics = false;
        } else {
            super.tick();
        }
    }

    @Override
    public void baseTick() {
        super.baseTick();
        if (this.isAlive()) {
            if (this.getTarget() != null && this.getTarget().isAlive() && this.distanceTo(this.getTarget()) >= 12.76F) {
                this.getMoveControl().setWantedPosition(this.getTarget().getRandomX(8.2), this.getTarget().getRandomY(), this.getTarget().getRandomZ(8.2), 1.2);
            }
            if (this.level().isClientSide()) {
                if (this.isCastingSpell()) {
                    float f = this.yBodyRot * ((float) Math.PI / 180F) + Mth.cos((float) this.tickCount * 0.6662F) * 0.25F;
                    this.applySpellEffects(this.getX() - (double) Mth.cos(f) * 0.6 * (double) this.getScale(), this.getY() + 1.65 * (double) this.getScale(), this.getZ() - (double) Mth.sin(f) * 0.6 * (double) this.getScale());
                    this.applySpellEffects(this.getX() + (double) Mth.cos(f) * 0.6 * (double) this.getScale(), this.getY() + 1.65 * (double) this.getScale(), this.getZ() + (double) Mth.sin(f) * 0.6 * (double) this.getScale());
                }
                this.level().addParticle(ParticleTypes.SOUL_FIRE_FLAME, this.getRandomX(0.5), this.getRandomY(), this.getRandomZ(0.5), 0.0, 0.0, 0.0);
            }
        }
        if (this.getWeaknessTick() > 0) {
            this.setWeaknessTick(this.getWeaknessTick() - 1);
        }
    }

    @Override
    public boolean hurtServer(ServerLevel lvl, DamageSource source, float amount) {
        if (source.is(DamageTypes.FALL) || source.is(DamageTypes.IN_WALL)) {
            return false;
        }
        if (!this.isInvisible() || source.is(DamageTypes.FELL_OUT_OF_WORLD) || source.isCreativePlayer() || amount >= 10000.0F) {
            return super.hurtServer(lvl, source, amount);
        }
        return false;
    }

    @Override
    public SoundEvent getAmbientSound() {
        return SupernaturalSounds.WRAITH_IDLE.get();
    }

    @Override
    public SoundEvent getHurtSound(DamageSource source) {
        return SupernaturalSounds.WRAITH_HURT.get();
    }

    @Override
    public SoundEvent getDeathSound() {
        return SupernaturalSounds.WRAITH_DEATH.get();
    }

    @Override
    public SoundEvent getCastingSoundEvent() {
        return SupernaturalSounds.WRAITH_TELEPORT.get();
    }

    @Override
    public boolean isInvisible() {
        return this.getWeaknessTick() <= 0;
    }

    @Override
    public boolean isAffectedByPotions() {
        return false;
    }

    public void setWeaknessTick(int i) {
        this.getEntityData().set(WEAK_TICK, i);
    }

    public int getWeaknessTick() {
        return this.getEntityData().get(WEAK_TICK);
    }
}