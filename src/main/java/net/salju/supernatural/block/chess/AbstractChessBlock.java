package net.salju.supernatural.block.chess;

import net.salju.supernatural.init.*;
import net.salju.supernatural.block.misc.RevenantVault;
import net.salju.supernatural.block.BoardTileBlock;
import net.salju.supernatural.item.component.CommanderWandData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.GlobalPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.InteractionResult;
import com.google.common.collect.Lists;
import java.util.List;
import java.util.Optional;

public abstract class AbstractChessBlock extends FallingBlock {
    public static final EnumProperty<Direction> FACING = HorizontalDirectionalBlock.FACING;
    public static final BooleanProperty PROMOTED = BooleanProperty.create("promoted");

	public AbstractChessBlock(BlockBehaviour.Properties props) {
		super(props);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(PROMOTED, false));
	}

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING);
        builder.add(PROMOTED);
    }

    @Override
    public InteractionResult useItemOn(ItemStack stack, BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult rez) {
        if (stack.is(SupernaturalItems.COMMANDER_WAND)) {
            CommanderWandData data = stack.get(SupernaturalData.COMMANDER);
            if (data != null) {
                stack.set(SupernaturalData.COMMANDER, new CommanderWandData(data.getPromotionType(), Optional.of(GlobalPos.of(world.dimension(), pos))));
            } else {
                stack.set(SupernaturalData.COMMANDER, new CommanderWandData(0, Optional.of(GlobalPos.of(world.dimension(), pos))));
            }
            return InteractionResult.SUCCESS;
        }
        return super.useItemOn(stack, state, world, pos, player, hand, rez);
    }

    @Override
    public void onLand(Level world, BlockPos pos, BlockState state, BlockState air, FallingBlockEntity target) {
        if (world.getBlockState(pos.below()).getBlock() instanceof AbstractChessBlock) {
            world.destroyBlock(pos.below(), true);
        } else if (world.getBlockState(pos.below()).getBlock() instanceof BoardTileBlock) {
            if (BoardTileBlock.isCursed(world, pos.below())) {
                world.destroyBlock(pos, false);
                if (world instanceof ServerLevel lvl) {
                    RevenantVault.ejectItem(lvl, pos, new ItemStack(getPawn(state)));
                }
            }
        }
    }

    @Override
    protected void falling(FallingBlockEntity target) {
        target.setHurtsEntities(5.0F, this.getChessDamage());
    }

    @Override
    public DamageSource getFallDamageSource(Entity target) {
        return SupernaturalDamageTypes.getCheckmated(target.level().registryAccess(), null);
    }

    @Override
    protected BlockState updateShape(BlockState state, LevelReader world, ScheduledTickAccess access, BlockPos pos, Direction dir, BlockPos poz, BlockState ztate, RandomSource randy) {
        return !state.canSurvive(world, pos) ? Blocks.AIR.defaultBlockState() : state;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    protected BlockState rotate(BlockState state, Rotation rot) {
        return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
    }

    @Override
    protected BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return Shapes.or(box(3, 0, 3, 13, this.getChessHeight(), 13));
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
        return super.canSurvive(state, world, pos) && world.getBlockState(pos.above()).isAir();
    }

    @Override
    protected boolean isPathfindable(BlockState state, PathComputationType path) {
        return false;
    }

    @Override
    public int getDustColor(BlockState state, BlockGetter world, BlockPos pos) {
        return state.getMapColor(world, pos).col;
    }

    public List<BlockPos> getValidMoves(BlockState state, Level world, BlockPos pos) {
        return Lists.newArrayList();
    }

    public boolean isPromoted(BlockState state) {
        return state.getValue(PROMOTED);
    }

    public abstract int getAnalogOutputSignal();

    public abstract int getChessDamage();

    public abstract int getChessHeight();

    public static boolean canTake(BlockState state, BlockState target) {
        return state.is(SupernaturalTags.EBONSTEEL_PIECES) ? target.is(SupernaturalTags.QUARTZ_PIECES) : target.is(SupernaturalTags.EBONSTEEL_PIECES);
    }

    public static boolean isValidBoard(Level world, BlockPos pos) {
        return world.getBlockState(pos.below()).is(SupernaturalTags.CHESSBOARD);
    }

    public static Item getPawn(BlockState state) {
        return state.is(SupernaturalTags.EBONSTEEL_PIECES) ? SupernaturalItems.EBONSTEEL_PAWN.get() : SupernaturalItems.QUARTZ_PAWN.get();
    }
}