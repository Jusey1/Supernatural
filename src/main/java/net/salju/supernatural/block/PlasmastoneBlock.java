package net.salju.supernatural.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.redstone.Orientation;
import net.salju.supernatural.init.SupernaturalTags;

import javax.annotation.Nullable;

public class PlasmastoneBlock extends Block {
    public static final IntegerProperty TYPE = IntegerProperty.create("type", 1, 4);

	public PlasmastoneBlock(BlockBehaviour.Properties props) {
		super(props);
        this.registerDefaultState(this.getStateDefinition().any().setValue(TYPE, 4));
	}

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(TYPE);
    }

    @Override
    protected void neighborChanged(BlockState state, Level world, BlockPos pos, Block blok, Orientation ori, boolean check) {
        super.neighborChanged(state, world, pos, blok, ori, check);
        int i = this.getPlasmaType(world, pos);
        if (state.getValue(TYPE) != i) {
            world.setBlock(pos, state.setValue(TYPE, i), 2);
        }
    }

    @Override @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState state = super.getStateForPlacement(context);
        if (state != null) {
            int i = this.getPlasmaType(context.getLevel(), context.getClickedPos());
            if (state.getValue(TYPE) != i) {
                return state.setValue(TYPE, i);
            }
        }
        return state;
    }

    public boolean isCorrectBlock(BlockState state) {
        return state.is(SupernaturalTags.PLASMASTONE);
    }

    public int getPlasmaType(Level world, BlockPos pos) {
        if (this.isCorrectBlock(world.getBlockState(pos.above()))) {
            if (this.isCorrectBlock(world.getBlockState(pos.below()))) {
                return 2;
            }
            return 3;
        } else if (this.isCorrectBlock(world.getBlockState(pos.below()))) {
            return 1;
        }
        return 4;
    }
}