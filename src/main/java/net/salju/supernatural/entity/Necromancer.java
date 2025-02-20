package net.salju.supernatural.entity;

import net.salju.supernatural.entity.ai.SupernaturalSummonVexSpellGoal;
import net.salju.supernatural.entity.ai.SupernaturalNecroSpellGoal;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.EntityType;

public class Necromancer extends AbstractVampireEntity {
	public Necromancer(EntityType<Necromancer> type, Level world) {
		super(type, world);
		this.xpReward = 50;
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.addGoal(1, new SupernaturalSummonVexSpellGoal(this));
		this.goalSelector.addGoal(2, new SupernaturalNecroSpellGoal(this));
	}

	public static AttributeSupplier.Builder createAttributes() {
		return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 100.0).add(Attributes.ATTACK_DAMAGE, 3.0).add(Attributes.ARMOR, 2.0).add(Attributes.MOVEMENT_SPEED, 0.3);
	}
}