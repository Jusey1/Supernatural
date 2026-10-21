package net.salju.supernatural.block.chess;

import net.salju.supernatural.init.SupernaturalBlocks;
import net.salju.supernatural.init.SupernaturalData;
import net.salju.supernatural.init.SupernaturalItems;
import net.salju.supernatural.item.component.CommanderWandData;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import com.mojang.serialization.MapCodec;

public class PawnBlock extends AbstractChessBlock {
    public static final MapCodec<PawnBlock> CODEC = simpleCodec(PawnBlock::new);

	public PawnBlock(BlockBehaviour.Properties props) {
		super(props);
	}

    @Override
    public MapCodec<PawnBlock> codec() {
        return CODEC;
    }

    @Override
    public InteractionResult useItemOn(ItemStack stack, BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult rez) {
        if (stack.is(SupernaturalItems.COMMANDER_WAND)) {
            CommanderWandData data = stack.get(SupernaturalData.COMMANDER);
            if (data != null && data.getCommandingPiecePosition() != null && data.getCommandingPiecePosition().equals(pos)) {
                world.setBlock(pos, this.getPromotionPiece(state, state.is(SupernaturalBlocks.EBONSTEEL_PAWN.get()), data.getPromotionType()), 3);
            }
        }
        return super.useItemOn(stack, state, world, pos, player, hand, rez);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return super.getStateForPlacement(context).setValue(PROMOTED, true);
    }

    @Override
    public int getAnalogOutputSignal() {
        return 2;
    }

    @Override
    public int getChessDamage() {
        return 10;
    }

    @Override
    public int getChessHeight() {
        return 22;
    }

    public BlockState getPromotionPiece(BlockState pawn, boolean check, int i) {
        if (pawn.is(SupernaturalBlocks.EBONSTEEL_PAWN.get())) {
            if (i <= 0) {
                return check ? SupernaturalBlocks.EBONSTEEL_QUEEN.get().withPropertiesOf(pawn) : SupernaturalBlocks.QUARTZ_QUEEN.get().withPropertiesOf(pawn);
            } else if (i == 1) {
                return check ? SupernaturalBlocks.EBONSTEEL_ROOK.get().withPropertiesOf(pawn) : SupernaturalBlocks.QUARTZ_ROOK.get().withPropertiesOf(pawn);
            } else if (i == 2) {
                return check ? SupernaturalBlocks.EBONSTEEL_BISHOP.get().withPropertiesOf(pawn) : SupernaturalBlocks.QUARTZ_BISHOP.get().withPropertiesOf(pawn);
            } else {
                return check ? SupernaturalBlocks.EBONSTEEL_KNIGHT.get().withPropertiesOf(pawn) : SupernaturalBlocks.QUARTZ_KNIGHT.get().withPropertiesOf(pawn);
            }
        }
        return pawn;
    }
}