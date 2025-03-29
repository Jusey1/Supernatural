package net.salju.supernatural.init;

import net.salju.supernatural.Supernatural;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.resources.ResourceKey;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageSource;

public class SupernaturalDamageTypes {
	public static final ResourceKey<DamageType> SUN = ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath(Supernatural.MODID, "sun"));
	public static final ResourceKey<DamageType> RITUAL = ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath(Supernatural.MODID, "ritual"));

	public static DamageSource causeSunDamage(RegistryAccess ra) {
		return new SupernaturalDamage(ra.lookupOrThrow(Registries.DAMAGE_TYPE).getOrThrow(SUN));
	}

	public static DamageSource causeRitualDamage(RegistryAccess ra, LivingEntity target) {
		return new SupernaturalDamage(ra.lookupOrThrow(Registries.DAMAGE_TYPE).getOrThrow(RITUAL), target);
	}

	private static class SupernaturalDamage extends DamageSource {
		public SupernaturalDamage(Holder.Reference<DamageType> message) {
			super(message);
		}

		public SupernaturalDamage(Holder.Reference<DamageType> message, Entity src) {
			super(message, src);
		}

		@Override
		public Component getLocalizedDeathMessage(LivingEntity target) {
			String s = "death.attack." + this.getMsgId();
			Entity entity = this.getDirectEntity() != null ? this.getDirectEntity() : target.getLastHurtByMob();
			if (entity != null) {
				return Component.translatable(s + ".entity", target.getDisplayName(), entity.getDisplayName());
			} else {
				return Component.translatable(s, target.getDisplayName());
			}
		}
	}
}