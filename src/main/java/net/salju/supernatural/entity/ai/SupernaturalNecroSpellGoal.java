package net.salju.supernatural.entity.ai;

import net.salju.supernatural.Supernatural;
import net.salju.supernatural.init.SupernaturalMobs;
import net.salju.supernatural.entity.Vampire;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.EntityType;

public class SupernaturalNecroSpellGoal extends AbstractSupernaturalSpellGoal {
	private final Monster user;

	public SupernaturalNecroSpellGoal(Monster source) {
		super(source);
		this.user = source;
	}

	@Override
	protected void performSpellCasting() {
		LivingEntity target = this.user.getTarget();
		LevelAccessor world = target.level();
		double x = Math.floor(target.getX());
		double y = Math.floor(target.getY());
		double z = Math.floor(target.getZ());
		BlockPos pos = BlockPos.containing((x + 0.5), y, (z + 0.5));
		if (world instanceof ServerLevel lvl) {
			Supernatural.queueServerWork(26, () -> {
				LightningBolt bolt = EntityType.LIGHTNING_BOLT.create(lvl, EntitySpawnReason.EVENT);
				bolt.moveTo(Vec3.atBottomCenterOf(pos));
				bolt.setVisualOnly(true);
				lvl.addFreshEntity(bolt);
				if ((Math.floor(target.getX()) == x) && (Math.floor(target.getY()) == y) && (Math.floor(target.getZ()) == z)) {
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
		return 210;
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