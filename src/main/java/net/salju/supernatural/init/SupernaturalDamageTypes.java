package net.salju.supernatural.init;

import net.salju.supernatural.Supernatural;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;

import javax.annotation.Nullable;

public class SupernaturalDamageTypes {
    public static final ResourceKey<DamageType> ANGEL = ResourceKey.create(Registries.DAMAGE_TYPE, Identifier.fromNamespaceAndPath(Supernatural.MODID, "angel"));
    public static final ResourceKey<DamageType> CHECKMATE = ResourceKey.create(Registries.DAMAGE_TYPE, Identifier.fromNamespaceAndPath(Supernatural.MODID, "checkmate"));
    public static final ResourceKey<DamageType> COLDFLAME = ResourceKey.create(Registries.DAMAGE_TYPE, Identifier.fromNamespaceAndPath(Supernatural.MODID, "coldflame"));
    public static final ResourceKey<DamageType> SUN = ResourceKey.create(Registries.DAMAGE_TYPE, Identifier.fromNamespaceAndPath(Supernatural.MODID, "sun"));

    public static DamageSource getAngel(RegistryAccess ra, @Nullable Entity target) {
        if (target != null) {
            return new DamageSource(ra.lookupOrThrow(Registries.DAMAGE_TYPE).getOrThrow(ANGEL), target);
        }
        return new DamageSource(ra.lookupOrThrow(Registries.DAMAGE_TYPE).getOrThrow(ANGEL));
    }

    public static DamageSource getCheckmated(RegistryAccess ra, @Nullable Entity target) {
        if (target != null) {
            return new DamageSource(ra.lookupOrThrow(Registries.DAMAGE_TYPE).getOrThrow(CHECKMATE), target);
        }
        return new DamageSource(ra.lookupOrThrow(Registries.DAMAGE_TYPE).getOrThrow(CHECKMATE));
    }

    public static DamageSource getColdflame(RegistryAccess ra, @Nullable Entity target) {
        if (target != null) {
            return new DamageSource(ra.lookupOrThrow(Registries.DAMAGE_TYPE).getOrThrow(COLDFLAME), target);
        }
        return new DamageSource(ra.lookupOrThrow(Registries.DAMAGE_TYPE).getOrThrow(COLDFLAME));
    }

    public static DamageSource getSun(RegistryAccess ra, @Nullable Entity target) {
        if (target != null) {
            return new DamageSource(ra.lookupOrThrow(Registries.DAMAGE_TYPE).getOrThrow(SUN), target);
        }
        return new DamageSource(ra.lookupOrThrow(Registries.DAMAGE_TYPE).getOrThrow(SUN));
    }
}