package net.salju.supernatural.init;

import net.salju.supernatural.Supernatural;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.resources.Identifier;

public class SupernaturalSounds {
	public static final DeferredRegister<SoundEvent> REGISTRY = DeferredRegister.create(Registries.SOUND_EVENT, Supernatural.MODID);
    public static final DeferredHolder<SoundEvent, SoundEvent> MERFOLK_DEATH = REGISTRY.register("merfolk_death", () -> SoundEvent.createVariableRangeEvent(Identifier.fromNamespaceAndPath(Supernatural.MODID, "merfolk_death")));
    public static final DeferredHolder<SoundEvent, SoundEvent> MERFOLK_HURT = REGISTRY.register("merfolk_hurt", () -> SoundEvent.createVariableRangeEvent(Identifier.fromNamespaceAndPath(Supernatural.MODID, "merfolk_hurt")));
    public static final DeferredHolder<SoundEvent, SoundEvent> MERFOLK_IDLE = REGISTRY.register("merfolk_idle", () -> SoundEvent.createVariableRangeEvent(Identifier.fromNamespaceAndPath(Supernatural.MODID, "merfolk_idle")));
    public static final DeferredHolder<SoundEvent, SoundEvent> MERLAND_DEATH = REGISTRY.register("merland_death", () -> SoundEvent.createVariableRangeEvent(Identifier.fromNamespaceAndPath(Supernatural.MODID, "merland_death")));
    public static final DeferredHolder<SoundEvent, SoundEvent> MERLAND_HURT = REGISTRY.register("merland_hurt", () -> SoundEvent.createVariableRangeEvent(Identifier.fromNamespaceAndPath(Supernatural.MODID, "merland_hurt")));
    public static final DeferredHolder<SoundEvent, SoundEvent> MERLAND_IDLE = REGISTRY.register("merland_idle", () -> SoundEvent.createVariableRangeEvent(Identifier.fromNamespaceAndPath(Supernatural.MODID, "merland_idle")));
    public static final DeferredHolder<SoundEvent, SoundEvent> THRALL_DEATH = REGISTRY.register("thrall_death", () -> SoundEvent.createVariableRangeEvent(Identifier.fromNamespaceAndPath(Supernatural.MODID, "thrall_death")));
    public static final DeferredHolder<SoundEvent, SoundEvent> THRALL_HURT = REGISTRY.register("thrall_hurt", () -> SoundEvent.createVariableRangeEvent(Identifier.fromNamespaceAndPath(Supernatural.MODID, "thrall_hurt")));
    public static final DeferredHolder<SoundEvent, SoundEvent> THRALL_IDLE = REGISTRY.register("thrall_idle", () -> SoundEvent.createVariableRangeEvent(Identifier.fromNamespaceAndPath(Supernatural.MODID, "thrall_idle")));
    public static final DeferredHolder<SoundEvent, SoundEvent> VAMPYRE_CELEBRATE = REGISTRY.register("vampyre_celebrate", () -> SoundEvent.createVariableRangeEvent(Identifier.fromNamespaceAndPath(Supernatural.MODID, "vampyre_celebrate")));
    public static final DeferredHolder<SoundEvent, SoundEvent> VAMPYRE_DEATH = REGISTRY.register("vampyre_death", () -> SoundEvent.createVariableRangeEvent(Identifier.fromNamespaceAndPath(Supernatural.MODID, "vampyre_death")));
    public static final DeferredHolder<SoundEvent, SoundEvent> VAMPYRE_HURT = REGISTRY.register("vampyre_hurt", () -> SoundEvent.createVariableRangeEvent(Identifier.fromNamespaceAndPath(Supernatural.MODID, "vampyre_hurt")));
    public static final DeferredHolder<SoundEvent, SoundEvent> VAMPYRE_IDLE = REGISTRY.register("vampyre_idle", () -> SoundEvent.createVariableRangeEvent(Identifier.fromNamespaceAndPath(Supernatural.MODID, "vampyre_idle")));
    public static final DeferredHolder<SoundEvent, SoundEvent> WIGHT_DEATH = REGISTRY.register("wight_death", () -> SoundEvent.createVariableRangeEvent(Identifier.fromNamespaceAndPath(Supernatural.MODID, "wight_death")));
    public static final DeferredHolder<SoundEvent, SoundEvent> WIGHT_HURT = REGISTRY.register("wight_hurt", () -> SoundEvent.createVariableRangeEvent(Identifier.fromNamespaceAndPath(Supernatural.MODID, "wight_hurt")));
    public static final DeferredHolder<SoundEvent, SoundEvent> WIGHT_IDLE = REGISTRY.register("wight_idle", () -> SoundEvent.createVariableRangeEvent(Identifier.fromNamespaceAndPath(Supernatural.MODID, "wight_idle")));
}