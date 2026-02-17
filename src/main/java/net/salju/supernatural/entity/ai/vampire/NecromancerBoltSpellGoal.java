package net.salju.supernatural.entity.ai.vampire;

import net.salju.supernatural.entity.ai.abstractai.AbstractTargetSpellGoal;
import net.salju.supernatural.entity.ai.MinionTargetGoal;
import net.salju.supernatural.entity.Necromancer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.monster.zombie.Zombie;
import net.minecraft.world.entity.monster.skeleton.Skeleton;
import net.minecraft.world.entity.*;

public class NecromancerBoltSpellGoal extends AbstractTargetSpellGoal {
	public NecromancerBoltSpellGoal(Necromancer source) {
		super(source);
	}

	@Override
	protected void performSpellCasting() {
        if (this.getTarget() != null) {
            LivingEntity target = this.getTarget();
            double x = target.blockPosition().getBottomCenter().x();
            double y = target.blockPosition().getBottomCenter().y();
            double z = target.blockPosition().getBottomCenter().z();
            if (target.level() instanceof ServerLevel lvl) {
                LightningBolt bolt = EntityType.LIGHTNING_BOLT.create(lvl, EntitySpawnReason.MOB_SUMMONED);
                if (bolt != null) {
                    bolt.snapTo(x, y, z);
                    bolt.setVisualOnly(true);
                    lvl.addFreshEntity(bolt);
                    for (int bob = 0; bob < 2; bob++) {
                        BlockPos pos = this.getBlockPos(BlockPos.containing(x, y, z));
                        if (Math.random() <= 0.35) {
                            Skeleton skele = EntityType.SKELETON.spawn(lvl, pos, EntitySpawnReason.MOB_SUMMONED);
                            if (skele != null) {
                                skele.targetSelector.addGoal(1, new MinionTargetGoal(skele, this.user));
                            }
                        } else {
                            Zombie billy = EntityType.ZOMBIE.spawn(lvl, pos, EntitySpawnReason.MOB_SUMMONED);
                            if (billy != null) {
                                billy.targetSelector.addGoal(1, new MinionTargetGoal(billy, this.user));
                            }
                        }
                    }
                }
            }
        }
	}

	@Override
	protected int getCastingTime() {
		return 40;
	}

	@Override
	protected int getCastingInterval() {
		return 420;
	}

	@Override
	protected int getSpell() {
		return 2;
	}

	@Override
	protected SoundEvent getSpellPrepareSound() {
		return SoundEvents.EVOKER_PREPARE_ATTACK;
	}

    private BlockPos getBlockPos(BlockPos pos) {
        double x = pos.getX() + (this.user.getRandom().nextDouble() - 0.5) * 4.5;
        double y = pos.getY() + (double) (this.user.getRandom().nextInt(4) - 1);
        double z = pos.getZ() + (this.user.getRandom().nextDouble() - 0.5) * 4.5;
        BlockPos.MutableBlockPos poz = new BlockPos.MutableBlockPos(x, y, z);
        while(poz.getY() > this.user.level().getMinY() && this.user.level().isEmptyBlock(poz.below())) {
            poz.move(Direction.DOWN);
        }
        while(poz.getY() > this.user.level().getMinY() && this.user.level().getBlockState(poz).isSolid()) {
            poz.move(Direction.UP);
        }
        return poz.immutable();
    }
}