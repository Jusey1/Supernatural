package net.salju.supernatural.compat.supplementaries;

import net.mehvahdjukaar.supplementaries.common.block.blocks.CandleHolderBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.salju.supernatural.init.SupernaturalCandle;

public class EbonsteelCandleHolderBlock extends CandleHolderBlock {
	public EbonsteelCandleHolderBlock(BlockBehaviour.Properties props) {
		super(null, props, SupernaturalCandle.FLAME::get, CandleHolderBlock::getDefaultParticleOffsets);
	}

    @Override
    public int getLightEmission(BlockState state, BlockGetter world, BlockPos pos) {
        if (state.getValue(LIT)) {
            return 2 + (2 * state.getValue(CANDLES));
        }
        return 0;
    }
}