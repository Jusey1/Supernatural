package net.salju.supernatural.block;

import net.salju.supernatural.block.chess.AbstractChessBlock;
import net.salju.supernatural.init.SupernaturalBlocks;
import net.salju.supernatural.init.SupernaturalTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
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
        if (isCursed(world, pos) && world.getBlockState(pos.above()).isAir()) {
            world.setBlock(pos.above(), SupernaturalBlocks.REVENANT_FLAME.get().defaultBlockState(), 3);
        }
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState state = super.getStateForPlacement(context);
        if (state != null && isCursed(context.getLevel(), context.getClickedPos()) && context.getLevel().getBlockState(context.getClickedPos().above()).isAir()) {
            context.getLevel().setBlock(context.getClickedPos().above(), SupernaturalBlocks.REVENANT_FLAME.get().defaultBlockState(), 3);
        }
        return state;
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

    public static boolean isCursed(Level world, BlockPos pos) {
        return world.getBlockState(pos.below()).is(SupernaturalTags.CURSED);
    }
}