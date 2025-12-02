package net.salju.supernatural.entity;

import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.level.Level;

public abstract class AbstractSpellcasterEntity extends PathfinderMob {
    public static final EntityDataAccessor<Integer> SPELL_TICK = SynchedEntityData.defineId(AbstractSpellcasterEntity.class, EntityDataSerializers.INT);

	public AbstractSpellcasterEntity(EntityType<? extends AbstractSpellcasterEntity> type, Level world) {
		super(type, world);
        this.xpReward = 5;
	}

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("SpellTick", this.getSpellTick());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.setSpellTick(tag.getInt("SpellTick"));
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(SPELL_TICK, 0);
    }

    @Override
    public void aiStep() {
        this.updateSwingTime();
        if (this.getLightLevelDependentMagicValue() > 0.5F) {
            this.noActionTime += 2;
        }
        super.aiStep();
    }

    @Override
    public void baseTick() {
        super.baseTick();
        if (this.getSpellTick() > 0) {
            this.setSpellTick(this.getSpellTick() - 1);
        }
    }

    @Override
    public boolean shouldDespawnInPeaceful() {
        return true;
    }

    @Override
    public boolean shouldDropExperience() {
        return true;
    }

    @Override
    protected boolean shouldDropLoot() {
        return true;
    }

    public SoundEvent getCastingSoundEvent() {
        return SoundEvents.ILLUSIONER_CAST_SPELL;
    }

    public void setSpellTick(int i) {
        this.getEntityData().set(SPELL_TICK, i);
    }

    public void applySpellEffects(double x, double y, double z) {
        if (this.level().isClientSide()) {
            this.level().addParticle(ColorParticleOption.create(ParticleTypes.ENTITY_EFFECT, this.getSpellColor()), x, y, z, 0.0, 0.0, 0.0);
        }
    }

    public boolean isCastingSpell() {
        return this.getSpellTick() > 0;
    }

    public int getSpellTick() {
        return this.getEntityData().get(SPELL_TICK);
    }

    public int getSpellColor() {
        return -6697729;
    }
}