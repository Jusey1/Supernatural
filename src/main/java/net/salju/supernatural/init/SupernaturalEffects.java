package net.salju.supernatural.init;

import net.salju.supernatural.effect.*;
import net.salju.supernatural.SupernaturalMod;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;

import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffect;

public class SupernaturalEffects {
	public static final DeferredRegister<MobEffect> REGISTRY = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, SupernaturalMod.MODID);
	public static final RegistryObject<MobEffect> SUPERNATURAL = REGISTRY.register("supernatural", () -> new Supernatural(MobEffectCategory.HARMFUL, -6750208));
	public static final RegistryObject<MobEffect> POSSESSION = REGISTRY.register("possession", () -> new Possession(MobEffectCategory.HARMFUL, -3342337));
}