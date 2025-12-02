package net.salju.supernatural.entity.ai.targets;

import net.salju.supernatural.entity.Wraith;
import net.minecraft.world.entity.LivingEntity;
import javax.annotation.Nullable;

public class WraithAttackSelector extends MinionAttackSelector {
	private final Wraith ghost;

	public WraithAttackSelector(Wraith target) {
        super(target);
		this.ghost = target;
	}

    @Override
	public boolean test(@Nullable LivingEntity target) {
        if (target != null && ghost.getOwner() != null && ghost.isHealer()) {
            return target.getUUID() == ghost.getOwner();
        }
        return super.test(target);
	}
}