package net.salju.supernatural.entity.ai;

import net.salju.supernatural.entity.AbstractMinionEntity;
import net.salju.supernatural.init.SupernaturalTags;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
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
		if (mob.isTamed() && mob.getOwner() != null && target != null) {
			if (mob.getOwner() instanceof LivingEntity owner) {
                if (owner.getLastHurtByMob() != null && owner.getLastHurtByMob().isAlive()) {
                    return target.equals(owner.getLastHurtByMob());
                } else if (owner.getLastHurtMob() != null && owner.getLastHurtMob().isAlive()) {
                    return target.equals(owner.getLastHurtMob());
                }
                if (owner instanceof Player) {
                    return target.getType().is(SupernaturalTags.SUMMON);
                }
			}
		}
		return target instanceof Player;
	}
}