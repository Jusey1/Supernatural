package net.salju.supernatural.entity.ai.abstractai;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Mob;

public abstract class AbstractTeleportSpellGoal extends AbstractSpellGoal {
	public AbstractTeleportSpellGoal(Mob target) {
		super(target);
	}

    @Override
    public boolean canUse() {
        return super.canUse() && this.wantsToTeleport();
    }

    @Override
    protected void performSpellCasting() {
        this.performTeleport(0);
    }

    @Override
    protected int getCastingTime() {
        return 20;
    }

    @Override
    protected int getCastingInterval() {
        return 360;
    }

    @Override
    protected int getSpell() {
        return 0;
    }

    @Override
    protected SoundEvent getSpellPrepareSound() {
        return SoundEvents.EVOKER_PREPARE_SUMMON;
    }

    protected void performTeleport(int i) {
        if (this.wantsToTeleport() && i <= 10) {
            double x = this.getX() + (this.user.getRandom().nextDouble() - 0.5) * this.getRadius();
            double y = this.getY() + (double) (this.user.getRandom().nextInt(this.getHeight()) - this.getHeight() / 2);
            double z = this.getZ() + (this.user.getRandom().nextDouble() - 0.5) * this.getRadius();
            this.teleport(x, y, z, i + 1);
        }
    }

    private void teleport(double x, double y, double z, int i) {
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos(x, y, z);
        while(pos.getY() > this.user.level().getMinY() && this.user.level().isEmptyBlock(pos.below())) {
            pos.move(Direction.DOWN);
        }
        while(pos.getY() > this.user.level().getMinY() && this.user.level().getBlockState(pos).isSolid()) {
            pos.move(Direction.UP);
        }
        if (this.canTeleport(pos)) {
            this.user.teleportTo(pos.getBottomCenter().x(), pos.getBottomCenter().y(), pos.getBottomCenter().z());
            this.user.playSound(this.getTeleportSound(), 1.0F, 1.0F);
            if (this.user.level() instanceof ServerLevel lvl) {
                lvl.sendParticles(ParticleTypes.SOUL_FIRE_FLAME, pos.getX(), pos.getY() + 0.75, pos.getZ(), 12, 0.5, 0.5, 0.5, 0.65);
            }
        } else {
            this.performTeleport(i);
        }
    }

    public abstract SoundEvent getTeleportSound();

    public abstract double getX();

    public abstract double getY();

    public abstract double getZ();

    public abstract double getRadius();

    public abstract int getHeight();

    public abstract boolean wantsToTeleport();

    public abstract boolean canTeleport(BlockPos.MutableBlockPos pos);
}