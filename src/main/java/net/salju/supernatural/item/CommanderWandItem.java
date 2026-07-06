package net.salju.supernatural.item;

import net.salju.supernatural.init.SupernaturalData;
import net.salju.supernatural.block.chess.AbstractChessBlock;
import net.salju.supernatural.init.SupernaturalItems;
import net.salju.supernatural.item.component.CommanderWandData;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.Level;
import net.minecraft.ChatFormatting;
import com.google.common.collect.Lists;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class CommanderWandItem extends Item {
	public CommanderWandItem(Item.Properties props) {
		super(props);
	}

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, TooltipDisplay display, Consumer<Component> list, TooltipFlag flag) {
        super.appendHoverText(stack, context, display, list, flag);
        CommanderWandData data = stack.get(SupernaturalData.COMMANDER);
        if (data != null) {
            list.accept(Component.translatable("desc.commander." + data.getPromotionType()).withStyle(ChatFormatting.GOLD));
            if (data.getCommandingPiecePosition() != null) {
                list.accept(Component.empty());
                if (context.level() != null) {
                    BlockState state = context.level().getBlockState(data.getCommandingPiecePosition());
                    list.accept(Component.translatable(state.getBlock().getDescriptionId()).withStyle(ChatFormatting.DARK_GRAY));
                }
                list.accept(Component.literal(data.getCommandingPiecePosition().getX() + ", " + data.getCommandingPiecePosition().getY() + ", " + data.getCommandingPiecePosition().getZ()).withStyle(ChatFormatting.DARK_PURPLE));
            }
        }
    }

    @Override
    public ItemStack getDefaultInstance() {
        ItemStack stack = super.getDefaultInstance();
        stack.set(SupernaturalData.COMMANDER, CommanderWandData.EMPTY);
        return stack;
    }

    @Override
    public InteractionResult use(Level world, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (stack.is(SupernaturalItems.COMMANDER_WAND)) {
            if (player.isCrouching()) {
                CommanderWandData data = stack.get(SupernaturalData.COMMANDER);
                if (data != null) {
                    int i = data.getPromotionType() + 1;
                    if (i > 3) {
                        i = 0;
                    }
                    stack.set(SupernaturalData.COMMANDER, new CommanderWandData(i, data.target()));
                }
                return InteractionResult.SUCCESS;
            }
        }
        return super.use(world, player, hand);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        CommanderWandData data = context.getItemInHand().get(SupernaturalData.COMMANDER);
        if (data != null && data.getCommandingPiecePosition() != null) {
            List<BlockPos> list = this.getPlacements(context.getLevel(), data.getCommandingPiecePosition());
            if (list.contains(context.getClickedPos().above())) {
                int i = 0;
                BlockPos target = context.getClickedPos().above(3);
                BlockState state = context.getLevel().getBlockState(data.getCommandingPiecePosition());
                for (BlockPos pos : BlockPos.betweenClosed(context.getClickedPos().above(), target)) {
                    if (this.isValidBlock(context.getLevel(), pos)) {
                        i++;
                    } else {
                        break;
                    }
                }
                if (i >= 3) {
                    if (context.getLevel() instanceof ServerLevel lvl) {
                        lvl.setBlock(target, state, 3);
                        lvl.playSound(null, target, SoundEvents.SOUL_ESCAPE.value(), SoundSource.BLOCKS, 1.0F, (float) (0.8F + (Math.random() * 0.2)));
                        lvl.destroyBlock(data.getCommandingPiecePosition(), false);
                        lvl.sendParticles(ParticleTypes.SOUL_FIRE_FLAME, data.getCommandingPiecePosition().getX(), data.getCommandingPiecePosition().getY(), data.getCommandingPiecePosition().getZ(), 7, 0.75, 1.5, 0.75, 0.15);
                        context.getItemInHand().set(SupernaturalData.COMMANDER, new CommanderWandData(data.getPromotionType(), Optional.empty()));
                    }
                }
            } else {
                context.getItemInHand().set(SupernaturalData.COMMANDER, CommanderWandData.EMPTY);
            }
            return InteractionResult.SUCCESS;
        }
        return super.useOn(context);
    }

    public List<BlockPos> getPlacements(Level world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        if (state.getBlock() instanceof AbstractChessBlock target) {
            return target.getValidMoves(state, world, pos);
        }
        return Lists.newArrayList();
    }

    public boolean isValidBlock(Level world, BlockPos pos) {
        return world.getBlockState(pos).isEmpty() || world.getBlockState(pos).getBlock() instanceof AbstractChessBlock;
    }
}