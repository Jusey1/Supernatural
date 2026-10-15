package net.salju.supernatural.block.chess;

import net.minecraft.world.level.block.state.BlockBehaviour;
import com.mojang.serialization.MapCodec;

public class PawnBlock extends AbstractChessBlock {
    public static final MapCodec<PawnBlock> CODEC = simpleCodec(PawnBlock::new);

	public PawnBlock(BlockBehaviour.Properties props) {
		super(props);
	}

    @Override
    public MapCodec<PawnBlock> codec() {
        return CODEC;
    }

    @Override
    public int getAnalogOutputSignal() {
        return 2;
    }

    @Override
    public int getChessDamage() {
        return 10;
    }

    @Override
    public int getChessHeight() {
        return 22;
    }
}