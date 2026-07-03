package net.salju.supernatural.item;

import net.salju.supernatural.crafting.RitualBookMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;

public class RitualBookItem extends Item {
	public RitualBookItem(Item.Properties props) {
		super(props);
	}

    @Override
    public InteractionResult use(Level world, Player player, InteractionHand hand) {
        if (!world.isClientSide()) {
            player.openMenu(this.getMenuProvider());
        }
        return super.use(world, player, hand);
    }

    public MenuProvider getMenuProvider() {
        return new SimpleMenuProvider((id, inventory, player) -> new RitualBookMenu(id, inventory), Component.translatable("item.supernatural.ritual_book"));
    }
}