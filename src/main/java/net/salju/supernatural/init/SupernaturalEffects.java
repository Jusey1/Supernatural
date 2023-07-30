package net.salju.supernatural.init;

import net.salju.supernatural.effect.*;
import net.salju.supernatural.SupernaturalMod;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;

import net.minecraft.world.effect.MobEffect;

public class SupernaturalEffects {
	public static final DeferredRegister<MobEffect> REGISTRY = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, SupernaturalMod.MODID);
	public static final RegistryObject<MobEffect> VAMPIRISM = REGISTRY.register("vampirism", () -> new Vampirism());
	public static final RegistryObject<MobEffect> BLEEDING = REGISTRY.register("bleeding", () -> new Bleeding());
	public static final RegistryObject<MobEffect> POSSESSION = REGISTRY.register("possession", () -> new Possession());
}