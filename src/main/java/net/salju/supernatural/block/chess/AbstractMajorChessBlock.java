package net.salju.supernatural.block.chess;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import java.util.List;

public abstract class AbstractMajorChessBlock extends AbstractChessBlock {
	public AbstractMajorChessBlock(BlockBehaviour.Properties props) {
		super(props);
	}

    public List<BlockPos> getRookMoves(BlockState state, Level world, BlockPos pos) {
        List<BlockPos> moves = super.getValidMoves(state, world, pos);
        for (int i = 0; i < 8; i++) {
            BlockPos check = pos.north(i);
            if (isValidBoard(world, check) && (isFree(world.getBlockState(check)) || canTake(state, world.getBlockState(check)))) {
                moves.add(check);
            } else {
                break;
            }
        }
        for (int i = 0; i < 8; i++) {
            BlockPos check = pos.west(i);
            if (isValidBoard(world, check) && (isFree(world.getBlockState(check)) || canTake(state, world.getBlockState(check)))) {
                moves.add(check);
            } else {
                break;
            }
        }
        for (int i = 0; i < 8; i++) {
            BlockPos check = pos.east(i);
            if (isValidBoard(world, check) && (isFree(world.getBlockState(check)) || canTake(state, world.getBlockState(check)))) {
                moves.add(check);
            } else {
                break;
            }
        }
        for (int i = 0; i < 8; i++) {
            BlockPos check = pos.south(i);
            if (isValidBoard(world, check) && (isFree(world.getBlockState(check)) || canTake(state, world.getBlockState(check)))) {
                moves.add(check);
            } else {
                break;
            }
        }
        return moves;
    }

    public List<BlockPos> getBishopMoves(BlockState state, Level world, BlockPos pos) {
        List<BlockPos> moves = super.getValidMoves(state, world, pos);
        for (int i = 0; i < 8; i++) {
            BlockPos check = BlockPos.containing(pos.getX() + i, pos.getY(), pos.getZ() + i);
            if (isValidBoard(world, check) && (isFree(world.getBlockState(check)) || canTake(state, world.getBlockState(check)))) {
                moves.add(check);
            } else {
                break;
            }
        }
        for (int i = 0; i < 8; i++) {
            BlockPos check = BlockPos.containing(pos.getX() + i, pos.getY(), pos.getZ() - i);
            if (isValidBoard(world, check) && (isFree(world.getBlockState(check)) || canTake(state, world.getBlockState(check)))) {
                moves.add(check);
            } else {
                break;
            }
        }
        for (int i = 0; i < 8; i++) {
            BlockPos check = BlockPos.containing(pos.getX() - i, pos.getY(), pos.getZ() + i);
            if (isValidBoard(world, check) && (isFree(world.getBlockState(check)) || canTake(state, world.getBlockState(check)))) {
                moves.add(check);
            } else {
                break;
            }
        }
        for (int i = 0; i < 8; i++) {
            BlockPos check = BlockPos.containing(pos.getX() - i, pos.getY(), pos.getZ() - i);
            if (isValidBoard(world, check) && (isFree(world.getBlockState(check)) || canTake(state, world.getBlockState(check)))) {
                moves.add(check);
            } else {
                break;
            }
        }
        return moves;
    }
}