package net.salju.supernatural.effect;

import net.salju.supernatural.init.SupernaturalDamageTypes;
import net.salju.supernatural.events.SupernaturalManager;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;

public class Vampirism extends MobEffect {
	public Vampirism(MobEffectCategory cate, int i) {
		super(cate, i);
	}

	@Override
	public boolean applyEffectTick(LivingEntity target, int str) {
		if (target instanceof Player player && !SupernaturalManager.isVampire(player)) {
			player.hurt(SupernaturalDamageTypes.causeRitualDamage(player.level().registryAccess(), player), 0.25F);
			if (player.isAlive()) {
				player.setHealth(1.0F);
			}
			SupernaturalManager.setVampire(player, true);
		}
		return super.applyEffectTick(target, str);
	}

	@Override
	public boolean shouldApplyEffectTickThisTick(int dur, int str) {
		return (dur <= 1 && str <= 0);
	}
}