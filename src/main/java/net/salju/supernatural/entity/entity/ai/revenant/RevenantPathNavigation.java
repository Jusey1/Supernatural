package net.salju.supernatural.entity.ai.revenant;

import net.salju.supernatural.entity.Revenant;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class RevenantPathNavigation extends FlyingPathNavigation {
	private final Revenant target;

	public RevenantPathNavigation(Revenant target, Level world) {
        super(target, world);
		this.target = target;
	}

    @Override
    protected boolean canMoveDirectly(Vec3 v, Vec3 t) {
        return this.target.isInvisible() || super.canMoveDirectly(v, t);
    }
}