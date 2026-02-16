package net.salju.supernatural.entity;

import net.salju.supernatural.entity.ai.spells.vampire.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EntityType;

public class Necromancer extends AbstractVampireEntity {
	public Necromancer(EntityType<Necromancer> type, Level world) {
		super(type, world);
		this.xpReward = 15;
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.addGoal(1, new NecromancerVexSpellGoal(this));
		this.goalSelector.addGoal(2, new NecromancerBoltSpellGoal(this));
	}
}