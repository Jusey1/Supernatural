package net.salju.supernatural.block;

import net.salju.supernatural.Supernatural;
import net.salju.supernatural.init.SupernaturalBlocks;
import net.salju.supernatural.init.SupernaturalItems;
import net.salju.supernatural.init.SupernaturalMobs;
import net.salju.supernatural.block.entity.TreasureVaultBlockEntity;
import net.salju.supernatural.block.misc.TreasureVault;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import com.mojang.serialization.MapCodec;
import javax.annotation.Nullable;
import java.util.List;

public class TreasureVaultBlock extends BaseEntityBlock {
	public static final MapCodec<TreasureVaultBlock> CODEC = simpleCodec(TreasureVaultBlock::new);
    public static final BooleanProperty TREASURE = BooleanProperty.create("treasure");

	public TreasureVaultBlock(BlockBehaviour.Properties props) {
		super(props);
        this.registerDefaultState(this.getStateDefinition().any().setValue(TREASURE, false));
	}

	@Override
	public MapCodec<TreasureVaultBlock> codec() {
		return CODEC;
	}

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(TREASURE);
    }

    @Override
    public InteractionResult useItemOn(ItemStack stack, BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult rez) {
        if (stack.is(SupernaturalItems.EBONSTEEL_KEY.get()) && this.canGiveTreasure(state, world, pos)) {
            if (world instanceof ServerLevel lvl) {
                world.setBlock(pos, this.getState(state, true), 2);
                TreasureVault.playSound(world, pos, SoundEvents.VAULT_OPEN_SHUTTER);
                List<ItemStack> vault = TreasureVault.getRewards(lvl, pos, player, "gameplay/treasure_vault_loot");
                if (!player.isCreative()) {
                    stack.shrink(1);
                }
                int i = 20;
                for (ItemStack reward : vault) {
                    i = i + 20;
                }
                this.setVault(vault, world, pos);
                this.setRenderStack(vault.getLast(), world, pos);
                this.setCD(world, pos, i);
            }
        } else {
            TreasureVault.playSound(world, pos, SoundEvents.VAULT_INSERT_ITEM_FAIL);
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public boolean onDestroyedByPlayer(BlockState state, Level world, BlockPos pos, Player player, ItemStack stack, boolean check, FluidState fluid) {
        if (world instanceof ServerLevel lvl && !player.isCreative()) {
            SupernaturalMobs.REVENANT.get().spawn(lvl, pos, EntitySpawnReason.MOB_SUMMONED);
        }
        return super.onDestroyedByPlayer(state, world, pos, player, stack, check, fluid);
    }

    @Override
    public int getExpDrop(BlockState state, LevelAccessor world, BlockPos pos, @Nullable BlockEntity target, @Nullable Entity mob, ItemStack stack) {
        return 15 + world.getRandom().nextInt(15) + world.getRandom().nextInt(15);
    }

	@Override
	public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new TreasureVaultBlockEntity(pos, state);
	}

	@Override
	public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, SupernaturalBlocks.TV.get(), world.isClientSide() ? TreasureVaultBlockEntity::clientTick : TreasureVaultBlockEntity::serverTick);
	}

	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.MODEL;
	}

    public BlockState getState(BlockState state, boolean check) {
        return state.setValue(TREASURE, check);
    }

    public boolean canGiveTreasure(BlockState state, Level world, BlockPos pos) {
        if (world.getBlockEntity(pos) instanceof TreasureVaultBlockEntity target) {
            return !state.getValue(TREASURE) && target.canGiveTreasure();
        }
        return false;
    }

    public void setCD(Level world, BlockPos pos, int i) {
        if (world.getBlockEntity(pos) instanceof TreasureVaultBlockEntity target) {
            target.setCD(i);
        }
    }

    public void setVault(List<ItemStack> vault, Level world, BlockPos pos) {
        if (world.getBlockEntity(pos) instanceof TreasureVaultBlockEntity target) {
            target.setVault(vault);
        }
    }

    public void setRenderStack(ItemStack stack, Level world, BlockPos pos) {
        if (world.getBlockEntity(pos) instanceof TreasureVaultBlockEntity target) {
            target.setRenderItem(stack);
        }
    }
}