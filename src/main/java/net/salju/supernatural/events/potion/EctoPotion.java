package net.salju.supernatural.events.potion;

import net.salju.supernatural.init.SupernaturalItems;
import net.minecraftforge.common.brewing.IBrewingRecipe;

import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;

public class EctoPotion implements IBrewingRecipe {
	@Override
	public boolean isInput(ItemStack input) {
		Item inputItem = input.getItem();
		return inputItem == Items.POTION && PotionUtils.getPotion(input) == Potions.AWKWARD;
	}

	@Override
	public boolean isIngredient(ItemStack ingredient) {
		return ingredient.getItem() == SupernaturalItems.ECTOPLASM.get();
	}

	@Override
	public ItemStack getOutput(ItemStack input, ItemStack ingredient) {
		if (isInput(input) && isIngredient(ingredient)) {
			return new ItemStack(Items.EXPERIENCE_BOTTLE);
		}
		return ItemStack.EMPTY;
	}
}