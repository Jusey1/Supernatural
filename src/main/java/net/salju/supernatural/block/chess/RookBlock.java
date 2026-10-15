package net.salju.supernatural.block.chess;

import net.minecraft.world.level.block.state.BlockBehaviour;
import com.mojang.serialization.MapCodec;

public class RookBlock extends AbstractChessBlock {
    public static final MapCodec<RookBlock> CODEC = simpleCodec(RookBlock::new);

	public RookBlock(BlockBehaviour.Properties props) {
		super(props);
	}

    @Override
    public MapCodec<RookBlock> codec() {
        return CODEC;
    }

    @Override
    public int getAnalogOutputSignal() {
        return 10;
    }

    @Override
    public int getChessDamage() {
        return 25;
    }

    @Override
    public int getChessHeight() {
        return 26;
    }
}