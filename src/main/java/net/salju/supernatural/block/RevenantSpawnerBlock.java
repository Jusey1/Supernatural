package net.salju.supernatural.block;

import net.salju.supernatural.init.SupernaturalBlocks;
import net.salju.supernatural.init.SupernaturalMobs;
import net.salju.supernatural.block.entity.RevenantSpawnerBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import com.mojang.serialization.MapCodec;
import javax.annotation.Nullable;

public class RevenantSpawnerBlock extends BaseEntityBlock {
	public static final MapCodec<RevenantSpawnerBlock> CODEC = simpleCodec(RevenantSpawnerBlock::new);

	public RevenantSpawnerBlock(BlockBehaviour.Properties props) {
		super(props);
	}

	@Override
	public MapCodec<RevenantSpawnerBlock> codec() {
		return CODEC;
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
		return new RevenantSpawnerBlockEntity(pos, state);
	}

	@Override
	public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, SupernaturalBlocks.RP.get(), world.isClientSide() ? RevenantSpawnerBlockEntity::clientTick : RevenantSpawnerBlockEntity::serverTick);
	}

	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.MODEL;
	}
}