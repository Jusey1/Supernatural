package net.salju.supernatural.crafting;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.level.block.state.BlockState;

public record RitualRecipeInput(ItemStack stack, ItemStack offhand, int i, int e, BlockState state) implements RecipeInput {
    @Override
    public ItemStack getItem(int i) {
        return stack;
    }

    @Override
    public int size() {
        return 1;
    }

    public BlockState getBlockState() {
        return state;
    }

    public ItemStack getOffhandItem() {
        return offhand;
    }

    public int getPower() {
        return i;
    }

    public int getSoul() {
        return e;
    }
}