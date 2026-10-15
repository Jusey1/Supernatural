package net.salju.supernatural.block;

import net.salju.supernatural.block.chess.AbstractChessBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.redstone.Orientation;

public class BoardTileBlock extends Block {
	public BoardTileBlock(BlockBehaviour.Properties props) {
		super(props);
	}

    @Override
    protected void neighborChanged(BlockState state, Level world, BlockPos pos, Block blok, Orientation ori, boolean check) {
        super.neighborChanged(state, world, pos, blok, ori, check);
        world.updateNeighbourForOutputSignal(pos, blok);
    }

    @Override
    protected boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    protected int getAnalogOutputSignal(BlockState state, Level world, BlockPos pos, Direction dir) {
        if (world.getBlockState(pos.above()).getBlock() instanceof AbstractChessBlock target) {
            return target.getAnalogOutputSignal();
        }
        return super.getAnalogOutputSignal(state, world, pos, dir);
    }
}