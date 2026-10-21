package net.salju.supernatural.block.chess;

import net.minecraft.world.level.block.state.BlockBehaviour;
import com.mojang.serialization.MapCodec;

public class KingBlock extends AbstractChessBlock {
    public static final MapCodec<KingBlock> CODEC = simpleCodec(KingBlock::new);

	public KingBlock(BlockBehaviour.Properties props) {
		super(props);
	}

    @Override
    public MapCodec<KingBlock> codec() {
        return CODEC;
    }

    @Override
    public int getAnalogOutputSignal() {
        return 15;
    }

    @Override
    public int getChessDamage() {
        return 40;
    }

    @Override
    public int getChessHeight() {
        return 31;
    }
}