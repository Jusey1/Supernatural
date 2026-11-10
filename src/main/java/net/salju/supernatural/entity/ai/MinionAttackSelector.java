package net.salju.supernatural.entity.ai;

import net.salju.supernatural.init.SupernaturalTags;
import net.salju.supernatural.entity.AbstractMinionEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.OwnableEntity;
import javax.annotation.Nullable;

public class MinionAttackSelector implements TargetingConditions.Selector {
	public final AbstractMinionEntity mob;

	public MinionAttackSelector(AbstractMinionEntity target) {
		this.mob = target;
	}

    @Override
	public boolean test(@Nullable LivingEntity target, ServerLevel lvl) {
        if (mob.getOwner() != null && target != null) {
            if (this.isValidTargetByHurt(target, mob.getOwner().getLastHurtByMob()) || this.isValidTargetByHurt(target, mob.getOwner().getLastHurtMob())) {
                return this.isValidTarget(target);
            } else if (mob.getOwner() instanceof Player) {
                return this.isValidTarget(target) && target.getType().is(SupernaturalTags.MINION);
            }
        }
        return target instanceof Player;
	}

    public boolean isValidTargetByHurt(LivingEntity target, LivingEntity hurt) {
        if (hurt != null && hurt.isAlive()) {
            return target.equals(hurt);
        }
        return false;
    }

    public boolean isValidTarget(LivingEntity target) {
        if (target instanceof OwnableEntity ent) {
            return ent.getOwner() != mob.getOwner();
        }
        return !target.getType().is(SupernaturalTags.IGNORE);
    }
}