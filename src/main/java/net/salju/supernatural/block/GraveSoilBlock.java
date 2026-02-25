package net.salju.supernatural.block;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LightLayer;
import net.salju.supernatural.init.SupernaturalMobs;
import net.salju.supernatural.init.SupernaturalConfig;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.monster.Vex;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.Level;
import net.minecraft.world.Difficulty;

public class GraveSoilBlock extends Block {
	public GraveSoilBlock(BlockBehaviour.Properties props) {
		super(props);
	}

    @Override
    public void randomTick(BlockState state, ServerLevel lvl, BlockPos pos, RandomSource rng) {
        super.randomTick(state, lvl, pos, rng);
        BlockPos poz = BlockPos.containing(pos.getX() + 0.5, pos.getY() + 2, pos.getZ() + 0.5);
        if (lvl.getBlockState(pos.above()).is(Blocks.SOUL_FIRE) && this.canSpawn(lvl, poz) && lvl.getDifficulty() != Difficulty.PEACEFUL) {
            if (lvl.isEmptyBlock(poz) && Math.random() <= 0.15) {
                if (Math.random() <= 0.65 || !SupernaturalConfig.VEX.get()) {
                    SupernaturalMobs.SPOOKY.get().spawn(lvl, poz, EntitySpawnReason.MOB_SUMMONED);
                } else {
                    Vex ghost = EntityType.VEX.spawn(lvl, poz, EntitySpawnReason.MOB_SUMMONED);
                    if (ghost != null) {
                        ghost.setLimitedLife(Mth.nextInt(ghost.getRandom(), 1200, 2400));
                        ghost.setBoundOrigin(poz);
                    }
                }
                lvl.sendParticles(ParticleTypes.SOUL, poz.getX() + 0.5, poz.getY() + 0.5, poz.getZ() + 0.5, 8, 0.75, 0.5, 0.75, 0);
                lvl.playSound(null, poz, SoundEvents.SOUL_ESCAPE.value(), SoundSource.BLOCKS, 1.0F, (float) (0.8F + (Math.random() * 0.2)));
            }
        }
    }

    private boolean canSpawn(ServerLevel lvl, BlockPos pos) {
        return (lvl.dimensionType().equals(Level.OVERWORLD) && lvl.getBrightness(LightLayer.BLOCK, pos) <= 10 && (lvl.getBrightness(LightLayer.SKY, pos) <= 10 || lvl.isDarkOutside()));
    }
}