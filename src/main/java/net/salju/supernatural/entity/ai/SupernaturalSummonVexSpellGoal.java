package net.salju.supernatural.entity.ai;

import net.minecraft.world.entity.monster.Vex;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.AbstractIllager;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.BlockPos;
import java.util.Random;

public class SupernaturalSummonVexSpellGoal extends AbstractSupernaturalSpellGoal {
	private final TargetingConditions vexCountTargeting = TargetingConditions.forNonCombat().range(16.0D).ignoreLineOfSight().ignoreInvisibilityTesting();
	private final Random rng = new Random();
	private final Monster user;

	public SupernaturalSummonVexSpellGoal(AbstractIllager source) {
		super(source);
		this.user = source;
	}

	@Override
	public boolean canUse() {
		if (!super.canUse()) {
			return false;
		} else {
			int i = this.user.level().getNearbyEntities(Vex.class, this.vexCountTargeting, this.user, this.user.getBoundingBox().inflate(16.0D)).size();
			return this.rng.nextInt(8) + 1 > i;
		}
	}

	@Override
	protected void performSpellCasting() {
		ServerLevel lvl = (ServerLevel) this.user.level();
		for (int i = 0; i < 3; ++i) {
			BlockPos pos = this.user.blockPosition().offset(-2 + this.rng.nextInt(5), 1, -2 + this.rng.nextInt(5));
			Vex vex = EntityType.VEX.spawn(lvl, pos, MobSpawnType.MOB_SUMMONED);
			vex.setOwner(this.user);
			vex.setBoundOrigin(pos);
			vex.setLimitedLife(2000);
		}
	}

	@Override
	protected int getCastingTime() {
		return 60;
	}

	@Override
	protected int getCastingInterval() {
		return 320;
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