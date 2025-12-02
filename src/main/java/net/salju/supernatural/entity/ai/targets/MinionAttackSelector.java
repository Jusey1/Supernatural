package net.salju.supernatural.entity.ai.targets;

import net.salju.supernatural.entity.AbstractMinionEntity;
import net.salju.supernatural.init.SupernaturalTags;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;

import javax.annotation.Nullable;
import java.util.function.Predicate;

public class MinionAttackSelector implements Predicate<LivingEntity> {
	private final AbstractMinionEntity summon;

	public MinionAttackSelector(AbstractMinionEntity source) {
		this.summon = source;
	}

    @Override
	public boolean test(@Nullable LivingEntity target) {
		if (summon.isTamed() && summon.getOwner() != null && target != null && summon.level() instanceof ServerLevel lvl) {
			if (lvl.getEntity(summon.getOwner()) instanceof LivingEntity owner) {
				if (owner.getLastHurtByMob() != null && owner.getLastHurtByMob().isAlive()) {
					return target.equals(owner.getLastHurtByMob());
				} else if (owner.getLastHurtMob() != null && owner.getLastHurtMob().isAlive()) {
					return target.equals(owner.getLastHurtMob());
				}
                if (owner instanceof Player) {
                    return target.getType().is(SupernaturalTags.ARMOR);
                }
			}
		}
		return target instanceof Player;
	}
}