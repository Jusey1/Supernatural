
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.salju.supernatural.init;

import net.salju.supernatural.SupernaturalMod;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.resources.ResourceLocation;

public class SupernaturalModSounds {
	public static final DeferredRegister<SoundEvent> REGISTRY = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, SupernaturalMod.MODID);
	public static final RegistryObject<SoundEvent> VAMPIRE_IDLE = REGISTRY.register("vampire_idle", () -> new SoundEvent(new ResourceLocation("supernatural", "vampire_idle")));
	public static final RegistryObject<SoundEvent> VAMPIRE_CELEBRATE = REGISTRY.register("vampire_celebrate", () -> new SoundEvent(new ResourceLocation("supernatural", "vampire_celebrate")));
	public static final RegistryObject<SoundEvent> VAMPIRE_HURT = REGISTRY.register("vampire_hurt", () -> new SoundEvent(new ResourceLocation("supernatural", "vampire_hurt")));
	public static final RegistryObject<SoundEvent> VAMPIRE_DEATH = REGISTRY.register("vampire_death", () -> new SoundEvent(new ResourceLocation("supernatural", "vampire_death")));
	public static final RegistryObject<SoundEvent> ARMOR_DEATH = REGISTRY.register("armor_death", () -> new SoundEvent(new ResourceLocation("supernatural", "armor_death")));
	public static final RegistryObject<SoundEvent> ARMOR_HURT = REGISTRY.register("armor_hurt", () -> new SoundEvent(new ResourceLocation("supernatural", "armor_hurt")));
	public static final RegistryObject<SoundEvent> MERLAND_DEATH = REGISTRY.register("merland_death", () -> new SoundEvent(new ResourceLocation("supernatural", "merland_death")));
	public static final RegistryObject<SoundEvent> MERLAND_HURT = REGISTRY.register("merland_hurt", () -> new SoundEvent(new ResourceLocation("supernatural", "merland_hurt")));
	public static final RegistryObject<SoundEvent> MERLAND_IDLE = REGISTRY.register("merland_idle", () -> new SoundEvent(new ResourceLocation("supernatural", "merland_idle")));
	public static final RegistryObject<SoundEvent> MERWATER_DEATH = REGISTRY.register("merwater_death", () -> new SoundEvent(new ResourceLocation("supernatural", "merwater_death")));
	public static final RegistryObject<SoundEvent> MERWATER_HURT = REGISTRY.register("merwater_hurt", () -> new SoundEvent(new ResourceLocation("supernatural", "merwater_hurt")));
	public static final RegistryObject<SoundEvent> MERWATER_IDLE = REGISTRY.register("merwater_idle", () -> new SoundEvent(new ResourceLocation("supernatural", "merwater_idle")));
	public static final RegistryObject<SoundEvent> SPOOK_POOF = REGISTRY.register("spook_poof", () -> new SoundEvent(new ResourceLocation("supernatural", "spook_poof")));
}
