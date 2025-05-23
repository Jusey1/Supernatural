package net.salju.supernatural.init;

import net.salju.supernatural.Supernatural;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.resources.ResourceLocation;

public class SupernaturalSounds {
	public static final DeferredRegister<SoundEvent> REGISTRY = DeferredRegister.create(Registries.SOUND_EVENT, Supernatural.MODID);
	public static final DeferredHolder<SoundEvent, SoundEvent> ARMOR_DEATH = REGISTRY.register("armor_death", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(Supernatural.MODID, "armor_death")));
	public static final DeferredHolder<SoundEvent, SoundEvent> ARMOR_HURT = REGISTRY.register("armor_hurt", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(Supernatural.MODID, "armor_hurt")));
	public static final DeferredHolder<SoundEvent, SoundEvent> SPOOK_POOF = REGISTRY.register("spook_poof", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(Supernatural.MODID, "spook_poof")));
	public static final DeferredHolder<SoundEvent, SoundEvent> VAMPIRE_CELEBRATE = REGISTRY.register("vampire_celebrate", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(Supernatural.MODID, "vampire_celebrate")));
	public static final DeferredHolder<SoundEvent, SoundEvent> VAMPIRE_DEATH = REGISTRY.register("vampire_death", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(Supernatural.MODID, "vampire_death")));
	public static final DeferredHolder<SoundEvent, SoundEvent> VAMPIRE_HURT = REGISTRY.register("vampire_hurt", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(Supernatural.MODID, "vampire_hurt")));
	public static final DeferredHolder<SoundEvent, SoundEvent> VAMPIRE_IDLE = REGISTRY.register("vampire_idle", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(Supernatural.MODID, "vampire_idle")));
}