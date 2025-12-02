package net.salju.supernatural.entity.ai.targets;

import net.salju.supernatural.init.SupernaturalTags;
import net.salju.supernatural.entity.AbstractMerfolkEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import javax.annotation.Nullable;

public class MerfolkAttackSelector implements TargetingConditions.Selector {
	private final AbstractMerfolkEntity merfolk;

	public MerfolkAttackSelector(AbstractMerfolkEntity source) {
		this.merfolk = source;
	}

    @Override
	public boolean test(@Nullable LivingEntity target, ServerLevel lvl) {
		return (target instanceof Player || target.getType().is(SupernaturalTags.MERFOLK));
	}
}