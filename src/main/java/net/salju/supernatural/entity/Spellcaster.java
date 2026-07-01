package net.salju.supernatural.entity;

import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.Level;

public interface Spellcaster  {
    default boolean isCastingSpell() {
        return this.getSpellTick() > 0;
    }

    default int getSpellColor() {
        return -6697729;
    }

    default void applySpellEffects(Level world, double x, double y, double z) {
        if (world.isClientSide()) {
            world.addParticle(ColorParticleOption.create(ParticleTypes.ENTITY_EFFECT, this.getSpellColor()), x, y, z, 0.0, 0.0, 0.0);
        }
    }

	default void updateSpellTick() {
		if (this.getSpellTick() > 0) {
			this.setSpellTick(this.getSpellTick() - 1);
		}
	}

	int getSpellTick();

    void setSpellTick(int i);

    SoundEvent getCastingSoundEvent();
}