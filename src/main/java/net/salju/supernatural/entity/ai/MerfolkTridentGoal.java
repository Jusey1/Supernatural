package net.salju.supernatural.entity.ai;

import net.salju.supernatural.entity.AbstractMerfolkEntity;
import net.minecraft.world.entity.ai.goal.RangedAttackGoal;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.TridentItem;

public class MerfolkTridentGoal extends RangedAttackGoal {
	public final AbstractMerfolkEntity merfolk;

	public MerfolkTridentGoal(AbstractMerfolkEntity target, double d, int i, float f) {
		super(target, d, i, f);
		this.merfolk = target;
	}

	@Override
	public boolean canUse() {
		return super.canUse() && this.isHoldingTrident();
	}

	@Override
	public void start() {
		super.start();
		merfolk.setAggressive(true);
		merfolk.startUsingItem(ProjectileUtil.getWeaponHoldingHand(merfolk, item -> item instanceof TridentItem));
	}

	@Override
	public void stop() {
		super.stop();
		merfolk.setAggressive(false);
		merfolk.stopUsingItem();
	}

	private boolean isHoldingTrident() {
		return merfolk.isHolding(stack -> stack.getItem() instanceof TridentItem);
	}
}