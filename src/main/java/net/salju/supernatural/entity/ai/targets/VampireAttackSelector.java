package net.salju.supernatural.entity.ai.targets;

import net.salju.supernatural.events.SupernaturalManager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.monster.AbstractIllager;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.LivingEntity;
import javax.annotation.Nullable;
import java.util.function.Predicate;

public class VampireAttackSelector implements Predicate<LivingEntity> {
	private final AbstractIllager vampire;

	public VampireAttackSelector(AbstractIllager source) {
		this.vampire = source;
	}

    @Override
	public boolean test(@Nullable LivingEntity target) {
		if (target instanceof Player) {
			if (this.vampire.getCurrentRaid() != null) {
				return true;
			}
			return !(SupernaturalManager.isVampire(target));
		}
		return (target instanceof AbstractVillager || target instanceof IronGolem);
	}
}