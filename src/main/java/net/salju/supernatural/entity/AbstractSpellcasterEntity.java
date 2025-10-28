package net.salju.supernatural.entity;

import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public abstract class AbstractSpellcasterEntity extends Monster {
    public static final EntityDataAccessor<Integer> SPELL_TICK = SynchedEntityData.defineId(AbstractSpellcasterEntity.class, EntityDataSerializers.INT);

	public AbstractSpellcasterEntity(EntityType<? extends AbstractSpellcasterEntity> type, Level world) {
		super(type, world);
	}

    @Override
    public void addAdditionalSaveData(ValueOutput tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("SpellTick", this.getSpellTick());
    }

    @Override
    public void readAdditionalSaveData(ValueInput tag) {
        super.readAdditionalSaveData(tag);
        if (tag.getInt("SpellTick").isPresent()) {
            this.setSpellTick(tag.getInt("SpellTick").get());
        }
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(SPELL_TICK, 0);
    }

    @Override
    public void baseTick() {
        super.baseTick();
        if (this.getSpellTick() > 0) {
            this.setSpellTick(this.getSpellTick() - 1);
        }
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

    public abstract SoundEvent getCastingSoundEvent();

    public abstract int getSpellColor();
}