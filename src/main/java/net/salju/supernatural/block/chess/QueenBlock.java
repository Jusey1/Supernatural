package net.salju.supernatural.block.chess;

import net.minecraft.world.level.block.state.BlockBehaviour;
import com.mojang.serialization.MapCodec;

public class QueenBlock extends AbstractChessBlock {
    public static final MapCodec<QueenBlock> CODEC = simpleCodec(QueenBlock::new);

	public QueenBlock(BlockBehaviour.Properties props) {
		super(props);
	}

    @Override
    public MapCodec<QueenBlock> codec() {
        return CODEC;
    }

    @Override
    public int getAnalogOutputSignal() {
        return 12;
    }

    @Override
    public int getChessDamage() {
        return 30;
    }

    @Override
    public int getChessHeight() {
        return 31;
    }
}