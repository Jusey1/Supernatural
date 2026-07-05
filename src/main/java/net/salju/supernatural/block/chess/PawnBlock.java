package net.salju.supernatural.block.chess;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import com.mojang.serialization.MapCodec;
import java.util.List;

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
    public List<BlockPos> getValidMoves(BlockState state, Level world, BlockPos pos) {
        List<BlockPos> moves = super.getValidMoves(state, world, pos);
        Direction pawn = state.getValue(AbstractChessBlock.FACING);
        BlockPos front = pos.relative(pawn);
        if (isFree(world.getBlockState(front)) && isValidBoard(world, front)) {
            moves.add(front);
        }
        if (canTake(state, world.getBlockState(front.relative(pawn.getClockWise()))) && isValidBoard(world, front.relative(pawn.getClockWise()))) {
            moves.add(front.relative(pawn.getClockWise()));
        }
        if (canTake(state, world.getBlockState(front.relative(pawn.getCounterClockWise()))) && isValidBoard(world, front.relative(pawn.getCounterClockWise()))) {
            moves.add(front.relative(pawn.getCounterClockWise()));
        }
        return moves;
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