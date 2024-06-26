package net.salju.supernatural.entity;

import net.salju.supernatural.init.SupernaturalMobs;

import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.Difficulty;

public class MerA extends AbstractMerEntity {
	public MerA(EntityType<MerA> type, Level world) {
		super(type, world);
	}

	public static void init() {
		SpawnPlacements.register(SupernaturalMobs.MER_AMETHYST.get(), SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, (entityType, world, reason, pos, random) -> {
			int y = pos.getY();
			return (y < 52 && world.getDifficulty() != Difficulty.PEACEFUL && Mob.checkMobSpawnRules(entityType, world, reason, pos, random));
		});
	}

	public static AttributeSupplier.Builder createAttributes() {
		AttributeSupplier.Builder builder = Mob.createMobAttributes();
		builder = builder.add(Attributes.MOVEMENT_SPEED, 0.3);
		builder = builder.add(Attributes.MAX_HEALTH, 24);
		builder = builder.add(Attributes.ARMOR, 2);
		builder = builder.add(Attributes.ATTACK_DAMAGE, 4);
		return builder;
	}
}