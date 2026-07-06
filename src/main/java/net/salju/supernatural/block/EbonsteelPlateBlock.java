package net.salju.supernatural.block;

import net.salju.supernatural.init.SupernaturalTags;
import net.salju.supernatural.block.chess.AbstractChessBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BasePressurePlateBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.AABB;
import com.mojang.serialization.MapCodec;

public class EbonsteelPlateBlock extends BasePressurePlateBlock {
    public static final MapCodec<EbonsteelPlateBlock> CODEC = simpleCodec(EbonsteelPlateBlock::new);
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

	public EbonsteelPlateBlock(BlockBehaviour.Properties props) {
		super(props, EbonsteelManager.EBONSTEEL);
        this.registerDefaultState(this.stateDefinition.any().setValue(POWERED, false));
	}

    @Override
    public MapCodec<EbonsteelPlateBlock> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(POWERED);
    }

    @Override
    protected int getSignalForState(BlockState state) {
        return state.getValue(POWERED) ? 15 : 0;
    }

    @Override
    protected BlockState setSignalForState(BlockState state, int i) {
        return state.setValue(POWERED, i > 0);
    }

    @Override
    protected int getSignalStrength(Level level, BlockPos pos) {
        Class c;
        switch (this.type.pressurePlateSensitivity()) {
            case EVERYTHING -> c = Entity.class;
            case MOBS -> c = LivingEntity.class;
            default -> throw new MatchException(null, null);
        }
        Class<? extends Entity> target = c;
        return getEbonsteelEntityCount(level, TOUCH_AABB.move(pos), target) > 0 ? 15 : 0;
    }

    public static int getEbonsteelEntityCount(Level world, AABB box, Class<? extends Entity> target) {
        return world.getEntitiesOfClass(target, box, EntitySelector.NO_SPECTATORS.and((entity) -> (entity instanceof FallingBlockEntity block && block.getBlockState().getBlock() instanceof AbstractChessBlock) || entity.getType().is(SupernaturalTags.GAMBIT))).size();
    }
}