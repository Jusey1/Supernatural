package net.salju.supernatural.item;

import net.salju.supernatural.init.SupernaturalData;
import net.salju.supernatural.block.misc.RitualManager;
import net.salju.supernatural.item.component.RitualCompassData;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.util.Mth;

public class RitualCompassItem extends Item {
	public RitualCompassItem(Item.Properties props) {
		super(props);
	}

    @Override
    public Component getName(ItemStack stack) {
        return Component.translatable(this.getDescriptionId(stack));
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return stack.get(SupernaturalData.COMPASS) != null;
    }

	@Override
	public InteractionResult use(Level world, Player player, InteractionHand hand) {
		if (player.isCreative() && world.dimension().equals(Level.OVERWORLD)) {
			if (world instanceof ServerLevel lvl) {
				player.setItemInHand(hand, RitualManager.getRitualCompass(player.blockPosition(), lvl, Mth.nextInt(player.getRandom(), 1, 3)));
			}
			return InteractionResult.SUCCESS;
		}
		return InteractionResult.PASS;
	}

    public String getDescriptionId(ItemStack stack) {
        RitualCompassData data = stack.get(SupernaturalData.COMPASS);
        if (data != null) {
            return this.getDescriptionId() + "." + data.getPower();
        }
        return this.getDescriptionId();
    }
}