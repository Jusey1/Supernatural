package net.salju.supernatural.entity.ai.spells;

import net.salju.supernatural.Supernatural;
import net.salju.supernatural.init.SupernaturalMobs;
import net.salju.supernatural.entity.ai.MinionTargetGoal;
import net.salju.supernatural.entity.Vampire;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.*;

public class SupernaturalNecroSpellGoal extends AbstractSupernaturalSpellGoal {
	private final Monster user;

	public SupernaturalNecroSpellGoal(Monster source) {
		super(source);
		this.user = source;
	}

	@Override
	protected void performSpellCasting() {
		LivingEntity target = this.user.getTarget();
		double x = target.getX();
		double y = target.getY();
		double z = target.getZ();
		BlockPos pos = BlockPos.containing((x + 0.5), y, (z + 0.5));
		if (target.level() instanceof ServerLevel lvl) {
			Supernatural.queueServerWork(25, () -> {
				LightningBolt bolt = EntityType.LIGHTNING_BOLT.create(lvl, EntitySpawnReason.EVENT);
				bolt.move(MoverType.SELF, Vec3.atBottomCenterOf(pos));
				bolt.setVisualOnly(true);
				lvl.addFreshEntity(bolt);
				if (target.getX() == x && target.getY() == y && target.getZ() == z) {
					target.hurt(target.damageSources().magic(), 12);
				} else {
					for (int bob = 0; bob < (int) (2); bob++) {
						if (Math.random() <= 0.99) {
							if (Math.random() <= 0.5) {
								Skeleton skele = EntityType.SKELETON.spawn(lvl, pos, EntitySpawnReason.MOB_SUMMONED);
								skele.targetSelector.addGoal(1, new MinionTargetGoal(skele, this.user));
							} else {
								Zombie billy = EntityType.ZOMBIE.spawn(lvl, pos, EntitySpawnReason.MOB_SUMMONED);
								billy.targetSelector.addGoal(1, new MinionTargetGoal(billy, this.user));
							}
						} else {
							Vampire vampire = SupernaturalMobs.VAMPIRE.get().spawn(lvl, pos, EntitySpawnReason.MOB_SUMMONED);
							vampire.setCustomName(Component.literal("Bob"));
						}
					}
				}
			});
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