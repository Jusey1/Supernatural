package net.salju.supernatural.entity;

import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EntityType;

public class Vampire extends AbstractVampireEntity {
	public Vampire(EntityType<Vampire> type, Level world) {
		super(type, world);
		this.xpReward = 6;
	}
}