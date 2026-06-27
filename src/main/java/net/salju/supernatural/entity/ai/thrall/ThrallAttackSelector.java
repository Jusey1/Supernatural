package net.salju.supernatural.entity.ai.thrall;

import net.salju.supernatural.init.SupernaturalTags;
import net.salju.supernatural.entity.ai.MinionAttackSelector;
import net.salju.supernatural.entity.Thrall;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import javax.annotation.Nullable;

public class ThrallAttackSelector extends MinionAttackSelector {
	public ThrallAttackSelector(Thrall target) {
		super(target);
	}

    @Override
	public boolean test(@Nullable LivingEntity target, ServerLevel lvl) {
		return (super.test(target, lvl) || target.getType().is(SupernaturalTags.WIGHT));
	}
}