package net.salju.supernatural.entity;

import net.minecraft.sounds.SoundEvent;

public interface Spellcaster {
    SoundEvent getCastingSoundEvent();

    void applySpellEffects(double x, double y, double z);

    void setSpellTick(int i);

    boolean isCastingSpell();

    int getSpellTick();

    int getSpellColor();
}