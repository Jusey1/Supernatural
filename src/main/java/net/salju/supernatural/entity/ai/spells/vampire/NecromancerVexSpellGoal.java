package net.salju.supernatural.entity.ai.spells.vampire;

import net.salju.supernatural.entity.ai.spells.AbstractTargetSpellGoal;
import net.salju.supernatural.entity.Necromancer;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.monster.Vex;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import java.util.Random;

public class NecromancerVexSpellGoal extends AbstractTargetSpellGoal {
	private final TargetingConditions vex = TargetingConditions.forCombat().range(16.0D).ignoreLineOfSight().ignoreInvisibilityTesting();
	private final Random rng = new Random();

	public NecromancerVexSpellGoal(Necromancer target) {
		super(target);
	}

	@Override
	public boolean canUse() {
		if (!super.canUse()) {
			return false;
		} else {
			int i = 0;
			if (this.user.level() instanceof ServerLevel lvl) {
				i = lvl.getNearbyEntities(Vex.class, this.vex, this.user, this.user.getBoundingBox().inflate(32.76D)).size();
			}
			return this.rng.nextInt(5) + 1 > i;
		}
	}

	@Override
	protected void performSpellCasting() {
		if (this.user.level() instanceof ServerLevel lvl) {
			for (int i = 0; i < 3; ++i) {
				BlockPos pos = this.user.blockPosition().offset(-2 + this.rng.nextInt(5), 1, -2 + this.rng.nextInt(5));
				Vex ghost = EntityType.VEX.spawn(lvl, pos, EntitySpawnReason.MOB_SUMMONED);
				if (ghost != null) {
					ghost.setOwner(this.user);
					ghost.setBoundOrigin(pos);
					ghost.setLimitedLife(Mth.nextInt(ghost.getRandom(), 1200, 2400));
				}
			}
		}
	}

	@Override
	protected int getCastingTime() {
		return 60;
	}

	@Override
	protected int getCastingInterval() {
		return 460;
	}

	@Override
	protected int getSpell() {
		return 1;
	}

	@Override
	protected SoundEvent getSpellPrepareSound() {
		return SoundEvents.EVOKER_PREPARE_SUMMON;
	}
}