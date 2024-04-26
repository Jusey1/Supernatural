package net.salju.supernatural.block;

import net.salju.supernatural.events.SupernaturalManager;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.entity.player.Player;
import net.minecraft.util.RandomSource;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;

public class PowerBlock extends Block {
	public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

	public PowerBlock(BlockBehaviour.Properties props) {
		super(props);
		this.registerDefaultState(this.stateDefinition.any().setValue(POWERED, Boolean.valueOf(false)));
	}

	@Override
	public void onPlace(BlockState state, Level world, BlockPos pos, BlockState oldstate, boolean check) {
		if (world instanceof ServerLevel lvl) {
			lvl.scheduleTick(pos, state.getBlock(), 10);
		}
	}

	@Override
	public void tick(BlockState state, ServerLevel lvl, BlockPos pos, RandomSource rng) {
		super.tick(state, lvl, pos, rng);
		lvl.scheduleTick(pos, state.getBlock(), 10);
		Player player = lvl.getNearestPlayer(pos.getX(), pos.getY(), pos.getZ(), 7, false);
		if (player != null && SupernaturalManager.isArtificer(player)) {
			lvl.setBlock(pos, state.setValue(POWERED, Boolean.valueOf(true)), 3);
		} else if (state.getValue(POWERED)) {
			lvl.setBlock(pos, state.setValue(POWERED, Boolean.valueOf(false)), 3);
		}
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> state) {
		state.add(POWERED);
	}

	@Override
	public int getSignal(BlockState state, BlockGetter blok, BlockPos pos, Direction dir) {
		return state.getValue(POWERED) ? 15 : 0;
	}

	@Override
	public int getDirectSignal(BlockState state, BlockGetter blok, BlockPos pos, Direction dir) {
		return state.getValue(POWERED) ? 15 : 0;
	}

	@Override
	public boolean isSignalSource(BlockState state) {
		return true;
	}
}