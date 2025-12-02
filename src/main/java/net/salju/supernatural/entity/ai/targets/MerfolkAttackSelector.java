package net.salju.supernatural.entity.ai.targets;

import net.salju.supernatural.init.SupernaturalTags;
import net.salju.supernatural.entity.AbstractMerfolkEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import javax.annotation.Nullable;
import java.util.function.Predicate;

public class MerfolkAttackSelector implements Predicate<LivingEntity> {
	private final AbstractMerfolkEntity merfolk;

	public MerfolkAttackSelector(AbstractMerfolkEntity source) {
		this.merfolk = source;
	}

    @Override
	public boolean test(@Nullable LivingEntity target) {
		return (target instanceof Player || target.getType().is(SupernaturalTags.MERFOLK));
	}
}