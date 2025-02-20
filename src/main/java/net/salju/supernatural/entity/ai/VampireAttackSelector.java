package net.salju.supernatural.entity.ai;

import net.salju.supernatural.events.SupernaturalManager;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.monster.AbstractIllager;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.LivingEntity;
import javax.annotation.Nullable;

public class VampireAttackSelector implements TargetingConditions.Selector {
	private final AbstractIllager vampire;

	public VampireAttackSelector(AbstractIllager source) {
		this.vampire = source;
	}

	public boolean test(@Nullable LivingEntity target, ServerLevel lvl) {
		if (target instanceof Player) {
			if (this.vampire.getCurrentRaid() != null) {
				return true;
			}
			return !(SupernaturalManager.isVampire(target));
		}
		return (target instanceof AbstractVillager || target instanceof IronGolem);
	}
}