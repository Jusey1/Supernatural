package net.salju.supernatural.block;

import net.salju.supernatural.item.Contracts;
import net.salju.supernatural.item.ContractItem;
import net.salju.supernatural.init.SupernaturalItems;
import net.salju.supernatural.init.SupernaturalEnchantments;
import net.salju.supernatural.init.SupernaturalConfig;
import net.salju.supernatural.init.SupernaturalBlockEntities;
import net.salju.supernatural.events.SupernaturalManager;
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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.tags.ItemTags;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.BlockPos;
import javax.annotation.Nullable;
import java.util.Map;
import java.util.List;
import net.minecraft.world.item.context.BlockPlaceContext;

public class RitualBlock extends BaseEntityBlock {
	public RitualBlock(BlockBehaviour.Properties props) {
		super(props);
	}

	@Override
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult rez) {
		BlockEntity entity = world.getBlockEntity(pos);
		ItemStack stack = player.getItemInHand(hand);
		if (entity instanceof RitualBlockEntity target) {
			if (stack.is(ItemTags.CANDLES) && stack.getItem() instanceof BlockItem blok) {
				List<BlockPos> list = SupernaturalManager.getCircle(pos);
				for (BlockPos poz : list) {
					if (world.isEmptyBlock(poz)) {
						return blok.place(new BlockPlaceContext(world, player, hand, stack, rez.withPosition(poz)));
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
						if (target.getItem(0).isEnchantable() && (player.experienceLevel >= 30 || player.isCreative())) {
							int i = SupernaturalManager.getSoulLevel(SupernaturalManager.getSoulgem(stack));
							target.setItem(0, EnchantmentHelper.enchantItem(lvl.getRandom(), target.getItem(0), (i * SupernaturalConfig.SOULPOWER.get()), true));
							if (SupernaturalManager.isVampire(player)) {
								int e = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.FIRE_ASPECT, target.getItem(0));
								if (e > 0) {
									Map<Enchantment, Integer> enchs = EnchantmentHelper.getEnchantments(target.getItem(0));
									enchs.put(SupernaturalEnchantments.LEECHING.get(), e);
									enchs.remove(Enchantments.FIRE_ASPECT);
									EnchantmentHelper.setEnchantments(enchs, target.getItem(0));
								}
							}
							lvl.playSound(null, pos, SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.BLOCKS, 1.0F, (float) (0.8F + (Math.random() * 0.2)));
							if (!player.isCreative()) {
								player.giveExperienceLevels(-i);
								stack.shrink(1);
							}
						} else {
							Rituals.doRitual(target.getItem(0), stack, lvl, player, pos);
						}
					} else if (stack.getItem() instanceof ContractItem item && SupernaturalManager.getUUID(stack) != null && target.getItem(0).is(item.getRitualItem())) {
						Contracts.doContract(item, stack, lvl, lvl.getPlayerByUUID(SupernaturalManager.getUUID(stack)), player, pos);
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
	public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean bool) {
		if (!state.is(newState.getBlock())) {
			BlockEntity entity = world.getBlockEntity(pos);
			if (entity instanceof RitualBlockEntity target && !target.isEmpty()) {
				target.dropItem(0);
			}
			super.onRemove(state, world, pos, newState, bool);
		}
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
		return createTickerHelper(type, SupernaturalBlockEntities.RITUAL.get(), RitualBlockEntity::tick);
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