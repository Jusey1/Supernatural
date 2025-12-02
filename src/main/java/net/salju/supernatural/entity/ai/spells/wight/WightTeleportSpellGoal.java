package net.salju.supernatural.entity.ai.spells.wight;

import net.salju.supernatural.init.SupernaturalSounds;
import net.salju.supernatural.entity.Wight;
import net.salju.supernatural.entity.ai.spells.AbstractTeleportSpellGoal;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;

public class WightTeleportSpellGoal extends AbstractTeleportSpellGoal {
	public WightTeleportSpellGoal(Wight target) {
		super(target);
	}

    @Override
    public SoundEvent getTeleportSound() {
        return SupernaturalSounds.SPOOK_POOF.get();
    }

    @Override
    public double getX() {
        if (this.getTarget() != null && this.user.distanceTo(this.getTarget()) >= 10.25) {
            return this.getTarget().getX();
        }
        return this.user.getX();
    }

    @Override
    public double getY() {
        if (this.getTarget() != null && this.user.distanceTo(this.getTarget()) >= 10.25) {
            return this.getTarget().getY();
        }
        return this.user.getY();
    }

    @Override
    public double getZ() {
        if (this.getTarget() != null && this.user.distanceTo(this.getTarget()) >= 10.25) {
            return this.getTarget().getZ();
        }
        return this.user.getZ();
    }

    @Override
    public double getRadius() {
        if (this.getTarget() != null && this.user.distanceTo(this.getTarget()) >= 10.25) {
            return 4.0;
        }
        return this.user.isInWaterOrBubble() ? 16.0 : 8.0;
    }

    @Override
    public int getHeight() {
        if (this.getTarget() != null && this.user.distanceTo(this.getTarget()) >= 10.25) {
            return 4;
        }
        return 6;
    }

    @Override
    public boolean wantsToTeleport() {
        if (this.getTarget() != null && this.getTarget().isAlive()) {
            if (this.user.distanceTo(this.getTarget()) >= 10.25) {
                return !this.getTarget().isInWaterOrBubble() && this.user.distanceTo(this.getTarget()) <= 18.76;
            } else if (this.user.distanceTo(this.getTarget()) <= 2.76) {
                return this.user.getHealth() <= this.user.getMaxHealth() * 0.25F;
            }
        }
        return this.user.isInWaterOrBubble();
    }

    @Override
    public boolean canTeleport(BlockPos.MutableBlockPos pos) {
        return !this.user.level().isWaterAt(pos) && !this.user.level().isWaterAt(pos.below());
    }
}