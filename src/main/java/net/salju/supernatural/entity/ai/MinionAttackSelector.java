package net.salju.supernatural.entity.ai;

import net.salju.supernatural.entity.AbstractMinionEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import javax.annotation.Nullable;

public class MinionAttackSelector implements TargetingConditions.Selector {
	public final AbstractMinionEntity mob;

	public MinionAttackSelector(AbstractMinionEntity target) {
		this.mob = target;
	}

    @Override
	public boolean test(@Nullable LivingEntity target, ServerLevel lvl) {
		if (mob.getOwner() != null && target != null) {
            if (mob.getOwner().getLastHurtByMob() != null && mob.getOwner().getLastHurtByMob().isAlive()) {
                return target.equals(mob.getOwner().getLastHurtByMob());
            } else if (mob.getOwner().getLastHurtMob() != null && mob.getOwner().getLastHurtMob().isAlive()) {
                return target.equals(mob.getOwner().getLastHurtMob());
            } else if (mob.getOwner() instanceof Player && !(target instanceof AbstractMinionEntity)) {
                return target instanceof Enemy;
            }
            return false;
		}
		return target instanceof Player;
	}
}