package net.salju.supernatural.block;

import net.salju.supernatural.init.SupernaturalMobs;
import net.salju.supernatural.init.SupernaturalConfig;
import net.salju.supernatural.entity.Spooky;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.entity.monster.Vex;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.Difficulty;
import net.minecraft.util.RandomSource;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.BlockPos;

public class GraveSoilBlock extends Block {
	public GraveSoilBlock(BlockBehaviour.Properties props) {
		super(props);
	}

	@Override
	public void tick(BlockState block, ServerLevel lvl, BlockPos pos, RandomSource random) {
		super.tick(block, lvl, pos, random);
		int x = pos.getX();
		int y = pos.getY();
		int z = pos.getZ();
		BlockPos poz = BlockPos.containing(x + 0.5, y + 2, z + 0.5);
		if (!lvl.isDay() && !(lvl.getDifficulty() == Difficulty.PEACEFUL)) {
			if (Math.random() <= 0.05 && Math.random() <= 0.15) {
				if (Math.random() <= 0.45 || (SupernaturalConfig.VEX.get() == false)) {
					Spooky ghost = SupernaturalMobs.SPOOKY.get().spawn(lvl, poz, MobSpawnType.MOB_SUMMONED);
				} else {
					Vex ghost = EntityType.VEX.spawn(lvl, poz, MobSpawnType.MOB_SUMMONED);
				}
			}
		}
	}
}