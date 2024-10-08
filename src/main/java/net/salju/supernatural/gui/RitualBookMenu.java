package net.salju.supernatural.gui;

import net.salju.supernatural.init.SupernaturalMenus;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.Container;
import net.minecraft.network.FriendlyByteBuf;
import javax.annotation.Nullable;

public class RitualBookMenu extends AbstractContainerMenu {
	private final Container book;
	private int page;

	public RitualBookMenu(int id, Inventory inv, @Nullable FriendlyByteBuf extraData) {
		this(id, inv, new SimpleContainer(16));
	}

	public RitualBookMenu(int id, Inventory inv, Container con) {
		super(SupernaturalMenus.RITUALBOOK.get(), id);
		checkContainerSize(con, 16);
		this.book = con;
	}

	@Override
	public boolean stillValid(Player player) {
		return true;
	}

	@Override
	public ItemStack quickMoveStack(Player player, int i) {
		return ItemStack.EMPTY;
	}

	public int getPage() {
		return this.page;
	}

	public void setPage(int i) {
		this.page = i;
	}

	public void updateSlots(int i) {
		//
	}
}