package net.salju.supernatural.block;

import net.salju.supernatural.init.SupernaturalMobs;
import net.salju.supernatural.init.SupernaturalConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Vex;
import net.minecraft.world.Difficulty;

public class GraveSoilBlock extends Block {
	public GraveSoilBlock(BlockBehaviour.Properties props) {
		super(props);
	}

	@Override
	public void tick(BlockState block, ServerLevel lvl, BlockPos pos, RandomSource random) {
		super.tick(block, lvl, pos, random);
		BlockPos poz = BlockPos.containing(pos.getX() + 0.5, pos.getY() + 2, pos.getZ() + 0.5);
		if ((lvl.getBrightness(LightLayer.SKY, pos.above()) < 3 || lvl.isDarkOutside()) && lvl.getBrightness(LightLayer.BLOCK, pos.above()) < 3 && lvl.isEmptyBlock(pos.above())  && lvl.getDifficulty() != Difficulty.PEACEFUL) {
			if (Math.random() <= 0.05 && Math.random() <= 0.15 && lvl.isEmptyBlock(poz)) {
				if (Math.random() <= 0.45 || !SupernaturalConfig.VEX.get()) {
					SupernaturalMobs.SPOOKY.get().spawn(lvl, poz, EntitySpawnReason.MOB_SUMMONED);
				} else {
					Vex ghost = EntityType.VEX.spawn(lvl, poz, EntitySpawnReason.MOB_SUMMONED);
					if (ghost != null) {
						ghost.setLimitedLife(Mth.nextInt(ghost.getRandom(), 1200, 2400));
						ghost.setBoundOrigin(poz);
					}
				}
			}
		}
	}
}