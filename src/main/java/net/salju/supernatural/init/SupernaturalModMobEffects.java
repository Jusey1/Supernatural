
/*
 *	MCreator note: This file will be REGENERATED on each build.
 */
package net.salju.supernatural.init;

import net.salju.supernatural.potion.VampirismMobEffect;
import net.salju.supernatural.potion.PossessionMobEffect;
import net.salju.supernatural.potion.BleedingMobEffect;
import net.salju.supernatural.SupernaturalMod;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;

import net.minecraft.world.effect.MobEffect;

public class SupernaturalModMobEffects {
	public static final DeferredRegister<MobEffect> REGISTRY = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, SupernaturalMod.MODID);
	public static final RegistryObject<MobEffect> VAMPIRISM = REGISTRY.register("vampirism", () -> new VampirismMobEffect());
	public static final RegistryObject<MobEffect> BLEEDING = REGISTRY.register("bleeding", () -> new BleedingMobEffect());
	public static final RegistryObject<MobEffect> POSSESSION = REGISTRY.register("possession", () -> new PossessionMobEffect());
}
