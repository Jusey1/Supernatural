package net.salju.supernatural.crafting;

import net.minecraft.core.NonNullList;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class RitualBookContainer implements Container {
    private final NonNullList<ItemStack> items;
    private final RitualBookMenu menu;

    public RitualBookContainer(RitualBookMenu menu, int i) {
        this.items = NonNullList.withSize(i, ItemStack.EMPTY);
        this.menu = menu;
    }

    @Override
    public int getContainerSize() {
        return this.items.size();
    }

    @Override
    public boolean isEmpty() {
        for(ItemStack stack : this.items) {
            if (!stack.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack getItem(int i) {
        return i >= this.getContainerSize() ? ItemStack.EMPTY : this.items.get(i);
    }

    @Override
    public ItemStack removeItemNoUpdate(int i) {
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeItem(int i, int e) {
        return ItemStack.EMPTY;
    }

    @Override
    public void setItem(int i, ItemStack stack) {
        this.items.set(i, stack);
        this.setChanged();
    }

    @Override
    public void setChanged() {
        this.menu.slotsChanged(this);
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    @Override
    public void clearContent() {
        this.items.clear();
    }
}