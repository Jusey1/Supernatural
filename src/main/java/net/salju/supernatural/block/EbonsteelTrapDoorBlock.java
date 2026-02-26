package net.salju.supernatural.block;

import net.salju.supernatural.init.SupernaturalItems;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.redstone.Orientation;
import net.minecraft.world.phys.BlockHitResult;
import javax.annotation.Nullable;

public class EbonsteelTrapDoorBlock extends TrapDoorBlock {
	public EbonsteelTrapDoorBlock(BlockBehaviour.Properties props) {
		super(EbonsteelManager.EBONSTEEL, props);
	}

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(EbonsteelManager.LOCKED);
    }

    @Override
    public InteractionResult useItemOn(ItemStack stack, BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult rez) {
        if (state.getValue(EbonsteelManager.LOCKED)) {
            if (stack.is(SupernaturalItems.EBONSTEEL_KEY.get())) {
                world.setBlock(pos, this.getState(state, false).cycle(OPEN), 10);
                world.playSound(player, pos, SoundEvents.IRON_TRAPDOOR_OPEN, SoundSource.BLOCKS);
                world.gameEvent(player, GameEvent.BLOCK_OPEN, pos);
                if (!player.isCreative()) {
                    stack.shrink(1);
                }
            } else {
                world.playSound(player, pos, SoundEvents.VAULT_INSERT_ITEM_FAIL, SoundSource.BLOCKS);
            }
            return InteractionResult.SUCCESS;
        }
        return super.useItemOn(stack, state, world, pos, player, hand, rez);
    }

    @Override
    protected void neighborChanged(BlockState state, Level world, BlockPos pos, Block blok, Orientation ori, boolean check) {
        if (!state.getValue(EbonsteelManager.LOCKED)) {
            super.neighborChanged(state, world, pos, blok, ori, check);
        }
    }

    @Override @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState state = super.getStateForPlacement(context);
        if (state != null && context.getPlayer() != null) {
            return this.getState(state, context.getPlayer().isCreative());
        }
        return state;
    }

    public BlockState getState(BlockState state, boolean check) {
        return state.setValue(EbonsteelManager.LOCKED, check);
    }
}