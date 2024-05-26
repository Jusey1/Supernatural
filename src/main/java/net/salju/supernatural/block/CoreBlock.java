package net.salju.supernatural.block;

import net.salju.supernatural.init.SupernaturalTags;
import net.salju.supernatural.init.SupernaturalItems;
import net.salju.supernatural.init.SupernaturalEffects;
import net.salju.supernatural.events.SupernaturalManager;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.util.Mth;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.BlockPos;

public class CoreBlock extends Block {
	public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

	public CoreBlock(BlockBehaviour.Properties props) {
		super(props);
		this.registerDefaultState(this.stateDefinition.any().setValue(POWERED, Boolean.valueOf(false)));
	}

	@Override
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult rez) {
		ItemStack stack = player.getItemInHand(hand);
		Inventory invy = player.getInventory();
		if (stack.is(SupernaturalTags.TOTEMS) && !state.getValue(POWERED)) {
			world.setBlock(pos, state.setValue(POWERED, Boolean.valueOf(true)), 3);
			if (!player.isCreative()) {
				stack.shrink(1);
			}
			if (world instanceof ServerLevel lvl) {
				lvl.playSound(null, pos, SoundEvents.RESPAWN_ANCHOR_CHARGE, SoundSource.BLOCKS, 0.6F, (float) (0.8F + (Math.random() * 0.2)));
			}
			return InteractionResult.SUCCESS;
		} else if (state.getValue(POWERED)) {
			if (!player.hasEffect(SupernaturalEffects.SUPERNATURAL.get()) && world.canSeeSky(pos.above(2)) && world.getLevelData().isThundering()) {
				if (world instanceof ServerLevel lvl && player instanceof ServerPlayer ply) {
					this.setArt(lvl, ply, pos);
				}
				return InteractionResult.SUCCESS;
			} else if (SupernaturalManager.isArtificer(player)) {
				if (isUnbreakable(stack) && invy.contains(new ItemStack(Items.IRON_INGOT)) && invy.getItem(invy.findSlotMatchingItem(new ItemStack(Items.IRON_INGOT))).getCount() >= 12) {
					int i = Mth.nextInt(player.getRandom(), 8, 12);
					invy.getItem(invy.findSlotMatchingItem(new ItemStack(Items.IRON_INGOT))).shrink(i);
					stack.getOrCreateTag().putBoolean("Unbreakable", true);
					stack.getOrCreateTag().putInt("HideFlags", 4);
					stack.setDamageValue(0);
					if (world instanceof ServerLevel lvl) {
						lvl.playSound(null, pos, SoundEvents.ANVIL_USE, SoundSource.BLOCKS, 0.85F, (float) (0.8F + (Math.random() * 0.2)));
					}
					return InteractionResult.SUCCESS;
				} else if (stack.is(Items.IRON_INGOT) && stack.getCount() >= 12) {
					if (world instanceof ServerLevel lvl) {
						int i = Mth.nextInt(player.getRandom(), 6, 12);
						return createItem(lvl, pos, stack, new ItemStack(SupernaturalItems.CANNONBALL.get()), i, Mth.nextInt(player.getRandom(), 4, i));
					} else {
						return InteractionResult.SUCCESS;
					}
				} else if (stack.is(Items.COPPER_INGOT) && stack.getCount() >= 12) {
					if (world instanceof ServerLevel lvl) {
						int i = Mth.nextInt(player.getRandom(), 6, 12);
						return createItem(lvl, pos, stack, new ItemStack(SupernaturalItems.COPPER_CANNONBALL.get()), i, Mth.nextInt(player.getRandom(), 4, i));
					} else {
						return InteractionResult.SUCCESS;
					}
				} else if (stack.is(Items.IRON_BLOCK) && stack.getCount() >= 12) {
					if (invy.contains(new ItemStack(Items.REDSTONE)) && invy.contains(new ItemStack(Items.OBSERVER)) && invy.getItem(invy.findSlotMatchingItem(new ItemStack(Items.REDSTONE))).getCount() >= 12) {
						if (world instanceof ServerLevel lvl) {
							int i = Mth.nextInt(player.getRandom(), 8, 12);
							invy.getItem(invy.findSlotMatchingItem(new ItemStack(Items.REDSTONE))).shrink(i);
							invy.getItem(invy.findSlotMatchingItem(new ItemStack(Items.OBSERVER))).shrink(1);
							world.setBlock(pos, state.setValue(POWERED, Boolean.valueOf(false)), 3);
							return createItem(lvl, pos, stack, new ItemStack(SupernaturalItems.CANNON.get()), i, 1);
						} else {
							return InteractionResult.SUCCESS;
						}
					}
				} else if (stack.is(Items.BUNDLE)) {
					if (invy.contains(new ItemStack(Items.ENDER_EYE)) && invy.contains(new ItemStack(Items.OBSIDIAN)) && invy.getItem(invy.findSlotMatchingItem(new ItemStack(Items.OBSIDIAN))).getCount() >= 12) {
						if (world instanceof ServerLevel lvl) {
							int i = Mth.nextInt(player.getRandom(), 8, 12);
							invy.getItem(invy.findSlotMatchingItem(new ItemStack(Items.OBSIDIAN))).shrink(i);
							invy.getItem(invy.findSlotMatchingItem(new ItemStack(Items.ENDER_EYE))).shrink(1);
							world.setBlock(pos, state.setValue(POWERED, Boolean.valueOf(false)), 3);
							return createItem(lvl, pos, stack, new ItemStack(SupernaturalItems.BUNDLE_HOLDING.get()), 1, 1);
						} else {
							return InteractionResult.SUCCESS;
						}
					}
				} else if (stack.is(Items.ENDER_PEARL) && stack.getCount() >= 4) {
					if (invy.contains(new ItemStack(Items.IRON_INGOT)) && invy.getItem(invy.findSlotMatchingItem(new ItemStack(Items.IRON_INGOT))).getCount() >= 12) {
						if (world instanceof ServerLevel lvl) {
							int i = Mth.nextInt(player.getRandom(), 8, 12);
							invy.getItem(invy.findSlotMatchingItem(new ItemStack(Items.IRON_INGOT))).shrink(i);
							world.setBlock(pos, state.setValue(POWERED, Boolean.valueOf(false)), 3);
							return createItem(lvl, pos, stack, new ItemStack(SupernaturalItems.MAGIC_MIRROR.get()), Mth.nextInt(player.getRandom(), 2, 4), 1);
						} else {
							return InteractionResult.SUCCESS;
						}
					}
				}
			}
		}
		return InteractionResult.PASS;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> state) {
		state.add(POWERED);
	}

	private boolean isUnbreakable(ItemStack stack) {
		return (stack.is(SupernaturalTags.ARTIFICER) && stack.isDamageableItem());
	}

	private InteractionResult createItem(ServerLevel lvl, BlockPos pos, ItemStack stack, ItemStack result, int i, int e) {
		result.setCount(e);
		ItemEntity drop = new ItemEntity(lvl, (pos.getX() + 0.5), (pos.getY() + 1.25), (pos.getZ() + 0.5), result);
		drop.setPickUpDelay(10);
		lvl.addFreshEntity(drop);
		lvl.playSound(null, pos, SoundEvents.FIRECHARGE_USE, SoundSource.BLOCKS, 0.85F, (float) (0.8F + (Math.random() * 0.2)));
		stack.shrink(i);
		return InteractionResult.SUCCESS;
	}

	private void setArt(ServerLevel lvl, ServerPlayer ply, BlockPos pos) {
		boolean west = lvl.getBlockState(pos.west()).getBlock() == Blocks.COPPER_BLOCK && lvl.getBlockState(pos.east()).getBlock() == Blocks.COPPER_BLOCK;
		boolean north = lvl.getBlockState(pos.north()).getBlock() == Blocks.COPPER_BLOCK && lvl.getBlockState(pos.south()).getBlock() == Blocks.COPPER_BLOCK;
		if (west || north) {
			if (lvl.getBlockState(pos.above()).getBlock() == Blocks.CARVED_PUMPKIN && lvl.getBlockState(pos.above(2)).getBlock() == Blocks.LIGHTNING_ROD && lvl.getBlockState(pos.below()).getBlock() == Blocks.COPPER_BLOCK) {
				lvl.broadcastEntityEvent(ply, (byte) 35);
				ply.hurt(ply.damageSources().magic(), 0.25F);
				ply.setHealth(1.0F);
				ply.getInventory().dropAll();
				SupernaturalManager.setArtificer(ply, true);
				lvl.destroyBlock(pos, false);
				lvl.destroyBlock(pos.below(), false);
				lvl.destroyBlock(pos.above(), false);
				lvl.destroyBlock(pos.above(2), false);
				if (west) {
					lvl.destroyBlock(pos.west(), false);
					lvl.destroyBlock(pos.east(), false);
				} else {
					lvl.destroyBlock(pos.north(), false);
					lvl.destroyBlock(pos.south(), false);
				}
				ply.teleportTo(pos.below().getX(), pos.below().getY(), pos.below().getZ());
				LightningBolt bolt = EntityType.LIGHTNING_BOLT.create(lvl);
				bolt.moveTo(Vec3.atBottomCenterOf(pos.below()));
				bolt.setVisualOnly(true);
				lvl.addFreshEntity(bolt);
			}
		}
	}
}