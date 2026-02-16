package net.salju.supernatural.item;

import net.salju.supernatural.init.SupernaturalBlocks;
import net.salju.supernatural.init.SupernaturalData;
import net.salju.supernatural.init.SupernaturalItems;
import net.salju.supernatural.item.component.AnchorballData;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.GlobalPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.InteractionResult;
import net.minecraft.ChatFormatting;
import java.util.function.Consumer;

public class SpectralCoreItem extends Item {
	public SpectralCoreItem(Item.Properties props) {
		super(props);
	}

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, TooltipDisplay display, Consumer<Component> list, TooltipFlag flag) {
        super.appendHoverText(stack, context, display, list, flag);
        AnchorballData data = stack.get(SupernaturalData.ANCHOR);
        if (data != null) {
            list.accept(Component.literal(data.getPos().getX() + ", " + data.getPos().getY() + ", " + data.getPos().getZ()).withStyle(ChatFormatting.DARK_PURPLE));
        }
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return true;
    }

	@Override
	public InteractionResult useOn(UseOnContext context) {
		if (context.getLevel().getBlockState(context.getClickedPos()).is(SupernaturalBlocks.RITUAL_ALTAR) && context.getPlayer() != null && context.getPlayer().isCreative() && context.getItemInHand().is(SupernaturalItems.REVENANT_CORE)) {
			context.getItemInHand().set(SupernaturalData.ANCHOR, new AnchorballData(GlobalPos.of(context.getLevel().dimension(), context.getClickedPos())));
			return InteractionResult.SUCCESS;
		}
		return super.useOn(context);
	}

    @Override
    public boolean onEntityItemUpdate(ItemStack stack, ItemEntity target) {
        if (target.level().isClientSide()) {
            double d = Math.random();
            if (d <= 0.5) {
                target.level().addParticle(ParticleTypes.SOUL_FIRE_FLAME, target.getRandomX(0.75), target.getRandomY() + d, target.getRandomZ(0.75), 0.0, 0.0, 0.0);
            }
        }
        return super.onEntityItemUpdate(stack, target);
    }
}