package net.salju.supernatural.block;

import net.salju.supernatural.block.entity.WeaponEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.mojang.serialization.MapCodec;
import net.salju.supernatural.init.SupernaturalTags;

import javax.annotation.Nullable;
import java.util.Map;

public class EbonsteelWeaponBlock extends BaseEntityBlock {
    public static final EnumProperty<Direction> FACING = HorizontalDirectionalBlock.FACING;
    private static final Map<Direction, VoxelShape> BOXES = Maps.newEnumMap(ImmutableMap.of(Direction.NORTH, Block.box(4.0, 7.0, 12.0, 12.0, 9.0, 16.0), Direction.SOUTH, Block.box(4.0, 7.0, 0.0, 12.0, 9.0, 4.0), Direction.WEST, Block.box(12.0, 7.0, 4.0, 16.0, 9.0, 12.0), Direction.EAST, Block.box(0.0, 7.0, 4.0, 4.0, 9.0, 12.0)));
	public static final MapCodec<EbonsteelWeaponBlock> CODEC = simpleCodec(EbonsteelWeaponBlock::new);

	public EbonsteelWeaponBlock(BlockBehaviour.Properties props) {
		super(props);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
	}

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING);
    }

	@Override
	public MapCodec<EbonsteelWeaponBlock> codec() {
		return CODEC;
	}

	@Override
	public InteractionResult useItemOn(ItemStack stack, BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult rez) {
		BlockEntity entity = world.getBlockEntity(pos);
		if (entity instanceof WeaponEntity target) {
			if (target.isEmpty() && stack.is(SupernaturalTags.DISPLAY_ITEMS)) {
				if (world instanceof ServerLevel lvl) {
					target.setItem(0, stack);
					lvl.playSound(null, target.getBlockPos(), SoundEvents.ITEM_FRAME_ADD_ITEM, SoundSource.BLOCKS, 1.0F, (float) (0.8F + (Math.random() * 0.2)));
					if (!player.isCreative()) {
						stack.shrink(1);
					}
				}
				return InteractionResult.SUCCESS;
			} else if (!target.isEmpty()) {
				if (world instanceof ServerLevel lvl) {
                    target.dropItem(0);
				}
				return InteractionResult.SUCCESS;
			}
		}
		return InteractionResult.PASS;
	}

	@Override
    public boolean onDestroyedByPlayer(BlockState state, Level world, BlockPos pos, Player player, ItemStack stack, boolean check, FluidState fluid) {
        BlockEntity entity = world.getBlockEntity(pos);
        if (entity instanceof WeaponEntity target && !target.isEmpty()) {
            target.dropItem(0);
        }
        return super.onDestroyedByPlayer(state, world, pos, player, stack, check, fluid);
	}

    @Override
    protected BlockState updateShape(BlockState state, LevelReader world, ScheduledTickAccess access, BlockPos pos, Direction dir, BlockPos poz, BlockState ztate, RandomSource randy) {
        return dir.getOpposite().equals(state.getValue(FACING)) && !state.canSurvive(world, pos) ? Blocks.AIR.defaultBlockState() : state;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
        return world.getBlockState(pos.relative(state.getValue(FACING).getOpposite())).isFaceSturdy(world, pos.relative(state.getValue(FACING).getOpposite()), state.getValue(FACING));
    }

	@Override
	public boolean triggerEvent(BlockState state, Level world, BlockPos pos, int i, int e) {
		return world.getBlockEntity(pos) == null ? super.triggerEvent(state, world, pos, i, e) : world.getBlockEntity(pos).triggerEvent(i, e);
	}

	@Override
	public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new WeaponEntity(pos, state);
	}

	@Override
	public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
		return null;
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		return BOXES.get(state.getValue(FACING));
	}

	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.MODEL;
	}
}