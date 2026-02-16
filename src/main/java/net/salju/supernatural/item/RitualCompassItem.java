package net.salju.supernatural.item;

import net.salju.supernatural.init.SupernaturalData;
import net.salju.supernatural.init.SupernaturalItems;
import net.salju.supernatural.init.SupernaturalTags;
import net.salju.supernatural.item.component.RitualCompassData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.util.Mth;
import java.util.Optional;

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
				player.setItemInHand(hand, getRitualCompass(player.blockPosition(), lvl, Mth.nextInt(player.getRandom(), 0, 2)));
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

	public static ItemStack getRitualCompass(BlockPos pos, ServerLevel lvl, int i) {
		ItemStack stack = new ItemStack(SupernaturalItems.COMPASS.get());
		if (i <= 0) {
			BlockPos loc = lvl.findNearestMapStructure(SupernaturalTags.RUINS, pos, 100, false);
			if (loc != null) {
				stack.set(SupernaturalData.COMPASS, new RitualCompassData(Optional.of(GlobalPos.of(lvl.dimension(), loc)), "ruins"));
			}
		} else if (i >= 2) {
			BlockPos loc = lvl.findNearestMapStructure(SupernaturalTags.ANCIENT, pos, 100, false);
			if (loc != null) {
				stack.set(SupernaturalData.COMPASS, new RitualCompassData(Optional.of(GlobalPos.of(lvl.dimension(), loc)), "ancient"));
			}
		} else {
			BlockPos loc = lvl.findNearestMapStructure(SupernaturalTags.LIFE, pos, 100, false);
			if (loc != null) {
				stack.set(SupernaturalData.COMPASS, new RitualCompassData(Optional.of(GlobalPos.of(lvl.dimension(), loc)), "village"));
			}
		}
		return stack;
	}
}