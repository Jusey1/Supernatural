package net.salju.supernatural.entity.ai.vampyre;

import net.salju.supernatural.events.SupernaturalManager;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.golem.IronGolem;
import net.minecraft.world.entity.monster.illager.AbstractIllager;
import net.minecraft.world.entity.npc.villager.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import javax.annotation.Nullable;

public class VampyreAttackSelector implements TargetingConditions.Selector {
	private final AbstractIllager vyre;

	public VampyreAttackSelector(AbstractIllager source) {
		this.vyre = source;
	}

    @Override
	public boolean test(@Nullable LivingEntity target, ServerLevel lvl) {
		if (target instanceof Player) {
			if (this.vyre.getCurrentRaid() != null) {
				return true;
			}
			return !(SupernaturalManager.isVampire(target));
		}
		return (target instanceof AbstractVillager || target instanceof IronGolem);
	}
}