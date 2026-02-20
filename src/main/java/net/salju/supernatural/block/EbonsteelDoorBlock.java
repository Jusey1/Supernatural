package net.salju.supernatural.block;

import net.salju.supernatural.init.SupernaturalItems;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.redstone.Orientation;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;

public class EbonsteelDoorBlock extends DoorBlock {
	public EbonsteelDoorBlock(BlockBehaviour.Properties props) {
		super(EbonsteelManager.EBONSTEEL, props);
        this.registerDefaultState(this.getStateDefinition().any().setValue(EbonsteelManager.LOCKED, false));
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
                if (world instanceof ServerLevel) {
                    world.setBlock(pos, this.getState(state, false), 2);
                    world.playSound(null, pos, SoundEvents.AMETHYST_BLOCK_RESONATE, SoundSource.BLOCKS);
                    if (!player.isCreative()) {
                        stack.shrink(1);
                    }
                }
            } else {
                world.playSound(null, pos, SoundEvents.VAULT_INSERT_ITEM_FAIL, SoundSource.BLOCKS);
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

    public BlockState getState(BlockState state, boolean check) {
        return state.setValue(EbonsteelManager.LOCKED, check);
    }
}