package net.salju.supernatural.item;

import net.salju.supernatural.init.SupernaturalBlocks;
import net.salju.supernatural.init.SupernaturalData;
import net.salju.supernatural.block.misc.RitualManager;
import net.salju.supernatural.item.component.AnchorballData;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.GlobalPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.ChatFormatting;
import java.util.function.Consumer;

public class SpectralMirrorItem extends Item {
	public SpectralMirrorItem(Item.Properties props) {
		super(props);
	}

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, TooltipDisplay display, Consumer<Component> list, TooltipFlag flag) {
        super.appendHoverText(stack, context, display, list, flag);
        AnchorballData data = stack.get(SupernaturalData.ANCHOR);
        if (data != null) {
            list.accept(Component.literal(data.getPos().getX() + ", " + data.getPos().getY() + ", " + data.getPos().getZ()).withStyle(ChatFormatting.DARK_PURPLE));
            String str = "desc.mirror." + data.getDimension().registry().getPath();
            list.accept(Component.translatable(str).withStyle(this.getColor(str)));
        }
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return true;
    }

	@Override
	public int getUseDuration(ItemStack stack, LivingEntity target) {
		return 64;
	}

    @Override
    public ItemUseAnimation getUseAnimation(ItemStack stack) {
        return ItemUseAnimation.BLOCK;
    }

	@Override
	public InteractionResult use(Level world, Player player, InteractionHand hand) {
        AnchorballData data = player.getItemInHand(hand).get(SupernaturalData.ANCHOR.get());
        if (data != null) {
            player.startUsingItem(hand);
            return InteractionResult.CONSUME;
        }
		return super.use(world, player, hand);
	}

    @Override
    public InteractionResult useOn(UseOnContext context) {
        if (context.getLevel().getBlockState(context.getClickedPos()).is(SupernaturalBlocks.RITUAL_ALTAR) && context.getPlayer() != null && context.getPlayer().isCreative()) {
            context.getItemInHand().set(SupernaturalData.ANCHOR, new AnchorballData(GlobalPos.of(context.getLevel().dimension(), context.getClickedPos())));
            return InteractionResult.SUCCESS;
        }
        return super.useOn(context);
    }

	@Override
	public boolean releaseUsing(ItemStack stack, Level world, LivingEntity target, int i) {
        AnchorballData data = stack.get(SupernaturalData.ANCHOR.get());
        if (i <= 10 && target instanceof ServerPlayer ply && world instanceof ServerLevel lvl && RitualManager.canTeleportTo(data, lvl)) {
			RitualManager.teleportUser(data, lvl, ply);
			ply.getCooldowns().addCooldown(stack, 200);
		}
        return super.releaseUsing(stack, world, target, i);
	}

	@Override
	public void onUseTick(Level world, LivingEntity target, ItemStack stack, int i) {
		super.onUseTick(world, target, stack, i);
        if (world.isClientSide()) {
            world.addParticle(ParticleTypes.SOUL_FIRE_FLAME, target.getRandomX(0.5), target.getRandomY(), target.getRandomZ(0.5), 0.0, 0.0, 0.0);
        } else {
            if (i <= 1) {
                this.releaseUsing(stack, world, target, i);
            }
        }
	}

    public ChatFormatting getColor(String str) {
        if (str.contains("the_nether")) {
            return ChatFormatting.RED;
        } else if (str.contains("the_end")) {
            return ChatFormatting.LIGHT_PURPLE;
        }
        return ChatFormatting.GREEN;
    }
}