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
	public static final DeferredHolder<SoundEvent, SoundEvent> MERFOLK_DEATH = REGISTRY.register("merfolk_death", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(Supernatural.MODID, "merfolk_death")));
	public static final DeferredHolder<SoundEvent, SoundEvent> MERFOLK_HURT = REGISTRY.register("merfolk_hurt", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(Supernatural.MODID, "merfolk_hurt")));
	public static final DeferredHolder<SoundEvent, SoundEvent> MERFOLK_IDLE = REGISTRY.register("merfolk_idle", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(Supernatural.MODID, "merfolk_idle")));
	public static final DeferredHolder<SoundEvent, SoundEvent> MERLAND_DEATH = REGISTRY.register("merland_death", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(Supernatural.MODID, "merland_death")));
	public static final DeferredHolder<SoundEvent, SoundEvent> MERLAND_HURT = REGISTRY.register("merland_hurt", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(Supernatural.MODID, "merland_hurt")));
	public static final DeferredHolder<SoundEvent, SoundEvent> MERLAND_IDLE = REGISTRY.register("merland_idle", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(Supernatural.MODID, "merland_idle")));
	public static final DeferredHolder<SoundEvent, SoundEvent> SPOOK_POOF = REGISTRY.register("spook_poof", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(Supernatural.MODID, "spook_poof")));
	public static final DeferredHolder<SoundEvent, SoundEvent> VAMPIRE_CELEBRATE = REGISTRY.register("vampire_celebrate", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(Supernatural.MODID, "vampire_celebrate")));
    public static final DeferredHolder<SoundEvent, SoundEvent> VAMPIRE_DEATH = REGISTRY.register("vampire_death", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(Supernatural.MODID, "vampire_death")));
    public static final DeferredHolder<SoundEvent, SoundEvent> VAMPIRE_HURT = REGISTRY.register("vampire_hurt", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(Supernatural.MODID, "vampire_hurt")));
    public static final DeferredHolder<SoundEvent, SoundEvent> VAMPIRE_IDLE = REGISTRY.register("vampire_idle", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(Supernatural.MODID, "vampire_idle")));
    public static final DeferredHolder<SoundEvent, SoundEvent> WIGHT_DEATH = REGISTRY.register("wight_death", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(Supernatural.MODID, "wight_death")));
    public static final DeferredHolder<SoundEvent, SoundEvent> WIGHT_HURT = REGISTRY.register("wight_hurt", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(Supernatural.MODID, "wight_hurt")));
    public static final DeferredHolder<SoundEvent, SoundEvent> WIGHT_IDLE = REGISTRY.register("wight_idle", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(Supernatural.MODID, "wight_idle")));
    public static final DeferredHolder<SoundEvent, SoundEvent> WRAITH_ATTACK = REGISTRY.register("wraith_attack", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(Supernatural.MODID, "wraith_attack")));
    public static final DeferredHolder<SoundEvent, SoundEvent> WRAITH_DEATH = REGISTRY.register("wraith_death", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(Supernatural.MODID, "wraith_death")));
    public static final DeferredHolder<SoundEvent, SoundEvent> WRAITH_FIRE = REGISTRY.register("wraith_firespell", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(Supernatural.MODID, "wraith_firespell")));
    public static final DeferredHolder<SoundEvent, SoundEvent> WRAITH_HURT = REGISTRY.register("wraith_hurt", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(Supernatural.MODID, "wraith_hurt")));
    public static final DeferredHolder<SoundEvent, SoundEvent> WRAITH_IDLE = REGISTRY.register("wraith_idle", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(Supernatural.MODID, "wraith_idle")));
    public static final DeferredHolder<SoundEvent, SoundEvent> WRAITH_TELEPORT = REGISTRY.register("wraith_teleport", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(Supernatural.MODID, "wraith_teleport")));
}