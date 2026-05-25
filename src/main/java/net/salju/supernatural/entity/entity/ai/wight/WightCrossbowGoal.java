package net.salju.supernatural.entity.ai.wight;

import net.salju.supernatural.entity.Wight;
import net.minecraft.world.entity.ai.goal.RangedCrossbowAttackGoal;
import net.minecraft.world.entity.monster.CrossbowAttackMob;

public class WightCrossbowGoal<T extends Wight & CrossbowAttackMob> extends RangedCrossbowAttackGoal<T> {
	private final T target;

	public WightCrossbowGoal(T t, double d, float f) {
		super(t, d, f);
		this.target = t;
	}

	@Override
	public void start() {
		super.start();
		this.target.setAggressive(true);
	}
}