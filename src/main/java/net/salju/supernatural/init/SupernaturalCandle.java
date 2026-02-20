package net.salju.supernatural.init;

import net.salju.supernatural.Supernatural;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.Registries;

public class SupernaturalCandle {
	public static final DeferredRegister<ParticleType<?>> REGISTRY = DeferredRegister.create(Registries.PARTICLE_TYPE, Supernatural.MODID);
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> FLAME = REGISTRY.register("ritual_candle_flame", () -> new SimpleParticleType(false));
}