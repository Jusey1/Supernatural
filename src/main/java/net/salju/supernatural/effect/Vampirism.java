package net.salju.supernatural.effect;

import net.salju.supernatural.events.SupernaturalHelpers;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.advancements.Advancement;

import java.util.Iterator;

public class Vampirism extends MobEffect {
	public Vampirism() {
		super(MobEffectCategory.HARMFUL, -3407872);
	}

	@Override
	public String getDescriptionId() {
		return "effect.supernatural.vampirism";
	}

	@Override
	public void applyEffectTick(LivingEntity target, int str) {
		if (target instanceof Player player) {
			player.getPersistentData().putDouble("willVampire", (player.getPersistentData().getDouble("willVampire") + 1));
		}
	}

	@Override
	public void removeAttributeModifiers(LivingEntity target, AttributeMap map, int str) {
		super.removeAttributeModifiers(target, map, str);
		if (target instanceof Player player) {
			if (player.getPersistentData().getDouble("willVampire") >= 24000) {
				SupernaturalHelpers.setVampire(player, true);
				if (player instanceof ServerPlayer ply) {
					Advancement adv = ply.server.getAdvancements().getAdvancement(new ResourceLocation("supernatural:become_vampire"));
					AdvancementProgress ap = ply.getAdvancements().getOrStartProgress(adv);
					if (!ap.isDone()) {
						Iterator gator = ap.getRemainingCriteria().iterator();
						while (gator.hasNext())
							ply.getAdvancements().award(adv, (String) gator.next());
					}
				}
			}
			player.getPersistentData().putDouble("willVampire", 0);
		}
	}

	@Override
	public boolean isDurationEffectTick(int dur, int str) {
		return true;
	}
}