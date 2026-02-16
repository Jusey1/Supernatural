package net.salju.supernatural.entity.ai;

import net.minecraft.world.entity.ai.util.AirAndWaterRandomPos;
import net.minecraft.world.entity.ai.util.HoverRandomPos;
import net.minecraft.world.phys.Vec3;
import net.salju.supernatural.entity.Revenant;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;

public class RevenantRandomFlyingGoal extends RandomStrollGoal {
    private final Revenant revenant;

    public RevenantRandomFlyingGoal(Revenant target, double d) {
        super(target, d);
        this.revenant = target;
    }

    @Override
    protected Vec3 getPosition() {
        Vec3 v = (this.revenant.getTarget() != null && this.revenant.getTarget().isAlive()) ? this.revenant.getTarget().getPosition(0.0F) : this.revenant.getViewVector(0.0F);
        Vec3 pos = HoverRandomPos.getPos(this.revenant, 8, 7, v.x, v.z, ((float)Math.PI / 2.0F), 3, 1);
        return pos != null ? pos : AirAndWaterRandomPos.getPos(this.mob, 8, 4, -2, v.x, v.z, ((float) Math.PI / 2.0F));
    }
}