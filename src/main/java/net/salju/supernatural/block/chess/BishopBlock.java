package net.salju.supernatural.block.chess;

import net.minecraft.world.level.block.state.BlockBehaviour;
import com.mojang.serialization.MapCodec;

public class BishopBlock extends AbstractChessBlock {
    public static final MapCodec<BishopBlock> CODEC = simpleCodec(BishopBlock::new);

	public BishopBlock(BlockBehaviour.Properties props) {
		super(props);
	}

    @Override
    public MapCodec<BishopBlock> codec() {
        return CODEC;
    }

    @Override
    public int getAnalogOutputSignal() {
        return 7;
    }

    @Override
    public int getChessDamage() {
        return 20;
    }

    @Override
    public int getChessHeight() {
        return 28;
    }
}