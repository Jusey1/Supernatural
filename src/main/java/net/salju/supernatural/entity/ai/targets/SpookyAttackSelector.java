package net.salju.supernatural.entity.ai.targets;

import net.salju.supernatural.init.SupernaturalConfig;
import net.salju.supernatural.init.SupernaturalEffects;
import net.salju.supernatural.init.SupernaturalTags;
import net.salju.supernatural.entity.Spooky;
import net.salju.supernatural.events.SupernaturalManager;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.LivingEntity;
import javax.annotation.Nullable;

public class SpookyAttackSelector implements TargetingConditions.Selector {
	private final Spooky ghost;

	public SpookyAttackSelector(Spooky source) {
		this.ghost = source;
	}

	public boolean test(@Nullable LivingEntity target, ServerLevel lvl) {
		if (!target.hasEffect(SupernaturalEffects.POSSESSION) && !target.hasEffect(MobEffects.GLOWING)) {
			if (SupernaturalConfig.ARMOR.get() && target instanceof ArmorStand) {
				return SupernaturalManager.hasArmor(target);
			}
			return target.getType().is(SupernaturalTags.SPOOKY) && !this.ghost.isTamed();
		}
		return false;
	}
}