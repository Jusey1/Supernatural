package net.salju.supernatural.block;

import net.salju.supernatural.init.SupernaturalBlocks;
import net.salju.supernatural.block.entity.RevenantSpawnerBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.BaseEntityBlock;
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