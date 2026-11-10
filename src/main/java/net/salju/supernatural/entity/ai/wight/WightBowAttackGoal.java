package net.salju.supernatural.entity.ai.wight;

import net.salju.supernatural.entity.Wight;
import net.minecraft.world.entity.ai.goal.RangedBowAttackGoal;
import net.minecraft.world.item.Items;

public class WightBowAttackGoal<T extends Wight> extends RangedBowAttackGoal<T> {
	private final T target;

	public WightBowAttackGoal(T t, double d, int i, float f) {
		super(t, d, i, f);
		this.target = t;
	}

    @Override
    public boolean canUse() {
        return super.canUse() && this.target.isHolding(Items.BOW);
    }
}