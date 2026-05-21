package net.salju.supernatural.init;

import net.salju.supernatural.Supernatural;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageSource;

public class SupernaturalDamageTypes {
	public static final ResourceKey<DamageType> SUN = ResourceKey.create(Registries.DAMAGE_TYPE, Identifier.fromNamespaceAndPath(Supernatural.MODID, "sun"));
	public static final ResourceKey<DamageType> RITUAL = ResourceKey.create(Registries.DAMAGE_TYPE, Identifier.fromNamespaceAndPath(Supernatural.MODID, "ritual"));
    public static final ResourceKey<DamageType> NIGHTMARE = ResourceKey.create(Registries.DAMAGE_TYPE, Identifier.fromNamespaceAndPath(Supernatural.MODID, "nightmare"));
    public static final ResourceKey<DamageType> CHECKMATE = ResourceKey.create(Registries.DAMAGE_TYPE, Identifier.fromNamespaceAndPath(Supernatural.MODID, "checkmate"));

	public static DamageSource causeSunDamage(RegistryAccess ra) {
		return new DamageSource(ra.lookupOrThrow(Registries.DAMAGE_TYPE).getOrThrow(SUN));
	}

	public static DamageSource causeRitualDamage(RegistryAccess ra) {
		return new DamageSource(ra.lookupOrThrow(Registries.DAMAGE_TYPE).getOrThrow(RITUAL));
	}

    public static DamageSource causeNightmareDamage(RegistryAccess ra) {
        return new DamageSource(ra.lookupOrThrow(Registries.DAMAGE_TYPE).getOrThrow(NIGHTMARE));
    }

    public static DamageSource getCheckmated(RegistryAccess ra) {
        return new DamageSource(ra.lookupOrThrow(Registries.DAMAGE_TYPE).getOrThrow(CHECKMATE));
    }
}