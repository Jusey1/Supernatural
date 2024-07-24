
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
	public static final RegistryObject<SoundEvent> ARMOR_DEATH = REGISTRY.register("armor_death", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation("supernatural", "armor_death")));
	public static final RegistryObject<SoundEvent> ARMOR_HURT = REGISTRY.register("armor_hurt", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation("supernatural", "armor_hurt")));
	public static final RegistryObject<SoundEvent> MERFOLK_WATER_DEATH = REGISTRY.register("merfolk_water_death", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation("supernatural", "merfolk_water_death")));
	public static final RegistryObject<SoundEvent> MERFOLK_WATER_HURT = REGISTRY.register("merfolk_water_hurt", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation("supernatural", "merfolk_water_hurt")));
	public static final RegistryObject<SoundEvent> MERFOLK_WATER_IDLE = REGISTRY.register("merfolk_water_idle", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation("supernatural", "merfolk_water_idle")));
	public static final RegistryObject<SoundEvent> MERLAND_DEATH = REGISTRY.register("merland_death", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation("supernatural", "merland_death")));
	public static final RegistryObject<SoundEvent> MERLAND_HURT = REGISTRY.register("merland_hurt", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation("supernatural", "merland_hurt")));
	public static final RegistryObject<SoundEvent> MERLAND_IDLE = REGISTRY.register("merland_idle", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation("supernatural", "merland_idle")));
	public static final RegistryObject<SoundEvent> SPOOK_POOF = REGISTRY.register("spook_poof", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation("supernatural", "spook_poof")));
	public static final RegistryObject<SoundEvent> VAMPIRE_CELEBRATE = REGISTRY.register("vampire_celebrate", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation("supernatural", "vampire_celebrate")));
	public static final RegistryObject<SoundEvent> VAMPIRE_DEATH = REGISTRY.register("vampire_death", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation("supernatural", "vampire_death")));
	public static final RegistryObject<SoundEvent> VAMPIRE_HURT = REGISTRY.register("vampire_hurt", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation("supernatural", "vampire_hurt")));
	public static final RegistryObject<SoundEvent> VAMPIRE_IDLE = REGISTRY.register("vampire_idle", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation("supernatural", "vampire_idle")));
}
