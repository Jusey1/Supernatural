package net.salju.supernatural.gui;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.Container;

public class RitualBookSlot extends Slot {
	public RitualBookSlot(Container con, int a, int i, int e) {
		super(con, a, i, e);
	}

	@Override
	public boolean mayPlace(ItemStack stack) {
		return false;
	}

	@Override
	public boolean mayPickup(Player player) {
		return false;
	}

	@Override
	public ItemStack remove(int i) {
		return ItemStack.EMPTY;
	}
}