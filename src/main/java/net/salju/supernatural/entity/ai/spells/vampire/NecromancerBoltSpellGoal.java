package net.salju.supernatural.entity.ai.spells.vampire;

import net.salju.supernatural.Supernatural;
import net.salju.supernatural.init.SupernaturalMobs;
import net.salju.supernatural.entity.ai.spells.AbstractTargetSpellGoal;
import net.salju.supernatural.entity.ai.MinionTargetGoal;
import net.salju.supernatural.entity.Necromancer;
import net.salju.supernatural.entity.Vampire;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.*;

public class NecromancerBoltSpellGoal extends AbstractTargetSpellGoal {
	public NecromancerBoltSpellGoal(Necromancer source) {
		super(source);
	}

	@Override
	protected void performSpellCasting() {
        if (this.getTarget() != null) {
            LivingEntity target = this.getTarget();
            double x = target.getX();
            double y = target.getY();
            double z = target.getZ();
            BlockPos pos = BlockPos.containing((x + 0.5), y, (z + 0.5));
            if (target.level() instanceof ServerLevel lvl) {
                Supernatural.queueServerWork(25, () -> {
                    LightningBolt bolt = EntityType.LIGHTNING_BOLT.create(lvl, EntitySpawnReason.MOB_SUMMONED);
                    if (bolt != null) {
                        bolt.snapTo(pos.getX(), pos.getY(), pos.getZ());
                        if (target.getX() == x && target.getY() == y && target.getZ() == z) {
                            bolt.setDamage(12.0F);
                            lvl.addFreshEntity(bolt);
                        } else {
                            bolt.setVisualOnly(true);
                            lvl.addFreshEntity(bolt);
                            for (int bob = 0; bob < 2; bob++) {
                                if (Math.random() <= 0.99) {
                                    if (Math.random() <= 0.5) {
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
                                } else {
                                    Vampire vampire = SupernaturalMobs.VAMPIRE.get().spawn(lvl, pos, EntitySpawnReason.MOB_SUMMONED);
                                    if (vampire != null) {
                                        vampire.setCustomName(Component.literal("Bob"));
                                    }
                                }
                            }
                        }
                    }
                });
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
}