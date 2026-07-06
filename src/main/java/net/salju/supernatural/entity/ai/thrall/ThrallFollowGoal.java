package net.salju.supernatural.entity.ai.thrall;

import net.salju.supernatural.entity.Thrall;
import net.minecraft.world.entity.ai.goal.Goal;
import java.util.EnumSet;

public class ThrallFollowGoal extends Goal {
    private final Thrall bob;
    private final double speed;
    private final float startD;
    private final float stopD;
    private int cd;

    public ThrallFollowGoal(Thrall bob, double speed, float startD, float stopD) {
        this.bob = bob;
        this.speed = speed;
        this.startD = startD;
        this.stopD = stopD;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if (this.bob.getOwner() == null || this.bob.unableToMove()) {
            return false;
        } else {
            return !(this.bob.distanceToSqr(this.bob.getOwner()) < this.startD * this.startD);
        }
    }

    @Override
    public boolean canContinueToUse() {
        if (this.bob.getOwner() == null || this.bob.getNavigation().isDone()) {
            return false;
        } else {
            return this.bob.unableToMove() ? false : !(this.bob.distanceToSqr(this.bob.getOwner()) <= (this.stopD * this.stopD));
        }
    }

    @Override
    public void start() {
        this.cd = 0;
    }

    @Override
    public void stop() {
        this.bob.getNavigation().stop();;
    }

    @Override
    public void tick() {
        boolean check = this.bob.shouldAttemptTeleport();
        if (!check && this.bob.getOwner() != null) {
            this.bob.getLookControl().setLookAt(this.bob.getOwner(), 10.0F, this.bob.getMaxHeadXRot());
        }
        if (--this.cd <= 0) {
            this.cd = this.adjustedTickDelay(10);
            if (check) {
                this.bob.attemptTeleport();
            } else {
                if (this.bob.getOwner() != null) {
                    this.bob.getNavigation().moveTo(this.bob.getOwner(), this.speed);
                }
            }
        }
    }
}