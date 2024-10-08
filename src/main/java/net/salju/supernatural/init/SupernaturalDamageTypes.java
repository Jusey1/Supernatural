package net.salju.supernatural.init;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.resources.ResourceKey;
import net.minecraft.network.chat.Component;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.Holder;

public class SupernaturalDamageTypes {
	public static final ResourceKey<DamageType> SUN = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation("supernatural:sun"));
	public static final ResourceKey<DamageType> BLEED = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation("supernatural:bleed"));
	public static final ResourceKey<DamageType> RITUAL = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation("supernatural:ritual"));

	public static DamageSource causeSunDamage(RegistryAccess ra) {
		return new SupernaturalDamage(ra.registry(Registries.DAMAGE_TYPE).get().getHolderOrThrow(SUN));
	}

	public static DamageSource causeBleedDamage(RegistryAccess ra, LivingEntity target) {
		return new SupernaturalDamage(ra.registry(Registries.DAMAGE_TYPE).get().getHolderOrThrow(BLEED), target);
	}

	public static DamageSource causeRitualDamage(RegistryAccess ra, LivingEntity target) {
		return new SupernaturalDamage(ra.registry(Registries.DAMAGE_TYPE).get().getHolderOrThrow(RITUAL), target);
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