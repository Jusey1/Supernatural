package net.salju.supernatural.entity;

import net.salju.supernatural.entity.ai.WraithMeleeAttackGoal;
import net.salju.supernatural.entity.ai.spells.AbstractSpellcasterGoal;
import net.salju.supernatural.entity.ai.targets.WraithAttackSelector;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class Wraith extends AbstractMinionEntity implements Enemy {
    private static final EntityDataAccessor<Boolean> HEALER = SynchedEntityData.defineId(Wraith.class, EntityDataSerializers.BOOLEAN);

	public Wraith(EntityType<Wraith> type, Level world) {
		super(type, world);
	}

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("Healer", this.isHealer());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.getEntityData().set(HEALER, tag.getBoolean("Healer"));
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(HEALER, false);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new RestrictSunGoal(this));
        this.goalSelector.addGoal(0, new AbstractSpellcasterGoal(this));
        this.goalSelector.addGoal(2, new WraithMeleeAttackGoal(this, 0.25, false));
        this.goalSelector.addGoal(3, new RandomStrollGoal(this, 1));
        this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, LivingEntity.class, 8));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 12, true, false, new WraithAttackSelector(this)));
    }

    @Override
    public SoundEvent getCastingSoundEvent() {
        return SoundEvents.EVOKER_CAST_SPELL;
    }

    @Override
    public int getSpellColor() {
        return -6697729;
    }

    public boolean isHealer() {
        return this.getEntityData().get(HEALER);
    }
}