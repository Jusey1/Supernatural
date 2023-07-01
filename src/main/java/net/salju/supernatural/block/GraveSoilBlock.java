package net.salju.supernatural.block;

import net.salju.supernatural.init.SupernaturalModEntities;
import net.salju.supernatural.init.SupernaturalConfig;
import net.salju.supernatural.entity.SpookyEntity;

import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.entity.monster.Vex;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.Difficulty;
import net.minecraft.util.RandomSource;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.BlockPos;

public class GraveSoilBlock extends Block {
	public GraveSoilBlock() {
		super(BlockBehaviour.Properties.of().mapColor(MapColor.PODZOL).sound(SoundType.SOUL_SOIL).strength(1.2f, 8f).requiresCorrectToolForDrops().randomTicks());
	}

	@Override
	public void tick(BlockState block, ServerLevel world, BlockPos pos, RandomSource random) {
		super.tick(block, world, pos, random);
		int x = pos.getX();
		int y = pos.getY();
		int z = pos.getZ();
		if (!world.isDay() && !(world.getDifficulty() == Difficulty.PEACEFUL)) {
			if (Math.random() <= 0.04 && Math.random() <= 0.45) {
				if (Math.random() <= 0.65 || (SupernaturalConfig.VEX.get() == false)) {
					Mob ghost = new SpookyEntity(SupernaturalModEntities.SPOOKY.get(), world);
					ghost.moveTo(x + 0.5, y + 2, z + 0.5, world.getRandom().nextFloat() * 360F, 0);
					ghost.finalizeSpawn(world, world.getCurrentDifficultyAt(ghost.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
					world.addFreshEntity(ghost);
				} else {
					Mob ghost = new Vex(EntityType.VEX, world);
					ghost.moveTo(x + 0.5, y + 2, z + 0.5, world.getRandom().nextFloat() * 360F, 0);
					ghost.finalizeSpawn(world, world.getCurrentDifficultyAt(ghost.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
					world.addFreshEntity(ghost);
				}
			}
		}
	}
}