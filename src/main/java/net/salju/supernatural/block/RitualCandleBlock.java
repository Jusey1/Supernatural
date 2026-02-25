package net.salju.supernatural.block;

import net.salju.supernatural.init.SupernaturalCandle;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.CandleBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class RitualCandleBlock extends CandleBlock {
	public RitualCandleBlock(BlockBehaviour.Properties props) {
		super(props);
	}

    @Override
    public void animateTick(BlockState state, Level world, BlockPos pos, RandomSource rng) {
        if (state.getValue(LIT)) {
            this.getParticleOffsets(state).forEach((v) -> addParticlesAndSound(world, v.add(pos.getX(), pos.getY(), pos.getZ()), rng));
        }
    }

    private static void addParticlesAndSound(Level world, Vec3 v, RandomSource rng) {
        float f = rng.nextFloat();
        if (f < 0.3F) {
            world.addParticle(ParticleTypes.SMOKE, v.x, v.y, v.z, 0.0F, 0.0F, 0.0F);
            if (f < 0.17F) {
                world.playLocalSound(v.x + 0.5F, v.y + 0.5F, v.z + 0.5F, SoundEvents.CANDLE_AMBIENT, SoundSource.BLOCKS, 1.0F + rng.nextFloat(), rng.nextFloat() * 0.7F + 0.3F, false);
            }
        }
        world.addParticle(SupernaturalCandle.FLAME.get(), v.x, v.y, v.z, 0.0F, 0.0F, 0.0F);
    }
}