package net.salju.supernatural.init;

import net.salju.supernatural.Supernatural;
import net.salju.supernatural.effect.*;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffect;

public class SupernaturalEffects {
	public static final DeferredRegister<MobEffect> REGISTRY = DeferredRegister.create(Registries.MOB_EFFECT, Supernatural.MODID);
	public static final DeferredHolder<MobEffect, MobEffect> VAMPIRISM = REGISTRY.register("vampirism", () -> new Vampirism(MobEffectCategory.NEUTRAL, -6750208));
	public static final DeferredHolder<MobEffect, MobEffect> POSSESSION = REGISTRY.register("possession", () -> new Possession(MobEffectCategory.HARMFUL, -6697729));
}