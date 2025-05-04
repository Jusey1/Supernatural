package net.salju.supernatural.entity.ai.targets;

import net.salju.supernatural.entity.AbstractMinionEntity;
import net.salju.supernatural.init.SupernaturalTags;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import javax.annotation.Nullable;

public class MinionAttackSelector implements TargetingConditions.Selector {
	private final AbstractMinionEntity armor;

	public MinionAttackSelector(AbstractMinionEntity source) {
		this.armor = source;
	}

	public boolean test(@Nullable LivingEntity target, ServerLevel lvl) {
		if (armor.isTamed() && armor.getOwner() != null && target != null) {
			Entity ent = armor.level().getEntity(armor.getOwner().getUUID());
			if (ent instanceof Player player) {
				if (player.getLastHurtByMob() != null && player.getLastHurtByMob().isAlive()) {
					return target.equals(player.getLastHurtByMob());
				} else if (player.getLastHurtMob() != null && player.getLastHurtMob().isAlive()) {
					return target.equals(player.getLastHurtMob());
				}
				return (target.getType().is(SupernaturalTags.ARMOR));
			} else if (ent instanceof Mob bob) {
				if (bob.getLastHurtByMob() != null && bob.getLastHurtByMob().isAlive()) {
					return target.equals(bob.getLastHurtByMob());
				} else if (bob.getTarget() != null && bob.getTarget().isAlive()) {
					return target.equals(bob.getTarget());
				}
			}
		}
		return (target instanceof Player);
	}
}