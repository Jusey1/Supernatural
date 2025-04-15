package net.salju.supernatural.block;

import net.salju.supernatural.init.SupernaturalItems;
import net.salju.supernatural.init.SupernaturalBlocks;
import net.salju.supernatural.events.SupernaturalManager;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import com.mojang.serialization.MapCodec;
import javax.annotation.Nullable;
import java.util.List;

public class RitualBlock extends BaseEntityBlock {
	public static final MapCodec<RitualBlock> CODEC = simpleCodec(RitualBlock::new);

	public RitualBlock(BlockBehaviour.Properties props) {
		super(props);
	}

	@Override
	public MapCodec<RitualBlock> codec() {
		return CODEC;
	}

	@Override
	public InteractionResult useItemOn(ItemStack stack, BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult rez) {
		BlockEntity entity = world.getBlockEntity(pos);
		if (entity instanceof RitualBlockEntity target) {
			if (stack.is(ItemTags.CANDLES) && stack.getItem() instanceof BlockItem blok) {
				List<BlockPos> list = SupernaturalManager.getCircle(pos);
				for (BlockPos poz : list) {
					if (world.isEmptyBlock(poz)) {
						blok.place(new BlockPlaceContext(world, player, hand, stack, rez.withPosition(poz)));
						return InteractionResult.SUCCESS;
					}
				}
			}
			if (target.isEmpty() && !stack.isEmpty()) {
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
					if (stack.is(SupernaturalItems.SOULGEM.get())) {
						Rituals.doRitual(target.getItem(0), stack, lvl, player, pos);
					} else {
						target.dropItem(0);
					}
				}
				return InteractionResult.SUCCESS;
			}
		}
		return InteractionResult.PASS;
	}

	@Override
	public boolean onDestroyedByPlayer(BlockState state, Level world, BlockPos pos, Player player, boolean check, FluidState fluid) {
		BlockEntity entity = world.getBlockEntity(pos);
		if (entity instanceof RitualBlockEntity target && !target.isEmpty()) {
			target.dropItem(0);
		}
		return super.onDestroyedByPlayer(state, world, pos, player, check, fluid);
	}

	@Override
	public boolean triggerEvent(BlockState state, Level world, BlockPos pos, int i, int e) {
		return world.getBlockEntity(pos) == null ? super.triggerEvent(state, world, pos, i, e) : world.getBlockEntity(pos).triggerEvent(i, e);
	}

	@Override
	public boolean hasAnalogOutputSignal(BlockState state) {
		return true;
	}

	@Override
	public int getAnalogOutputSignal(BlockState state, Level world, BlockPos pos) {
		BlockEntity entity = world.getBlockEntity(pos);
		if (entity instanceof RitualBlockEntity target && !target.isEmpty()) {
			return AbstractContainerMenu.getRedstoneSignalFromContainer(target);
		}
		return 0;
	}

	@Override
	public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new RitualBlockEntity(pos, state);
	}

	@Override
	public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
		return createTickerHelper(type, SupernaturalBlocks.RITUAL.get(), RitualBlockEntity::tick);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		return Shapes.or(box(2, 0, 2, 14, 8, 14), box(0, 0, 0, 4, 16, 4), box(12, 0, 12, 16, 16, 16), box(0, 0, 12, 4, 16, 16), box(12, 0, 0, 16, 16, 4));
	}

	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.MODEL;
	}
}