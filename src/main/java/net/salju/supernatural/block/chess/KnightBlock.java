package net.salju.supernatural.block.chess;

import net.minecraft.world.level.block.state.BlockBehaviour;
import com.mojang.serialization.MapCodec;

public class KnightBlock extends AbstractChessBlock {
    public static final MapCodec<KnightBlock> CODEC = simpleCodec(KnightBlock::new);

	public KnightBlock(BlockBehaviour.Properties props) {
		super(props);
	}

    @Override
    public MapCodec<KnightBlock> codec() {
        return CODEC;
    }

    @Override
    public int getAnalogOutputSignal() {
        return 5;
    }

    @Override
    public int getChessDamage() {
        return 15;
    }

    @Override
    public int getChessHeight() {
        return 24;
    }
}