package net.salju.supernatural.client.screens;

import net.salju.supernatural.init.SupernaturalItems;
import net.salju.supernatural.init.SupernaturalRecipes;
import net.salju.supernatural.crafting.RitualBookMenu;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.gui.screens.recipebook.GhostSlots;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.client.gui.screens.recipebook.RecipeCollection;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.context.ContextMap;
import net.minecraft.world.entity.player.StackedItemContents;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.inventory.Slot;
import java.util.List;

public class RitualRecipeBookComponent<T extends RitualBookMenu> extends RecipeBookComponent {
    public RitualRecipeBookComponent(T menu) {
        this(menu, List.of(new RecipeBookComponent.TabInfo(SupernaturalItems.RITUAL_BOOK.get(), SupernaturalRecipes.RITUALS.get()), new RecipeBookComponent.TabInfo(SupernaturalItems.REVENANT_CORE.get(), SupernaturalRecipes.SPECIAL.get()), new RecipeBookComponent.TabInfo(SupernaturalItems.SOULGEM.get(), SupernaturalRecipes.SUMMONS.get())));
    }

    public RitualRecipeBookComponent(T menu, List<TabInfo> tabs) {
        super(menu, tabs);
    }

    @Override
    public boolean isCraftingSlot(Slot slot) {
        return false;
    }

    @Override
    public void fillGhostRecipe(GhostSlots slots, RecipeDisplay display, ContextMap map) {
        //
    }

    @Override
    public void renderGhostRecipe(GuiGraphics gui, boolean check) {
        //
    }

    @Override
    public void selectMatchingRecipes(RecipeCollection collection, StackedItemContents contents) {
        collection.selectRecipes(contents, this::canDisplay);
    }

    @Override
    public Component getRecipeFilterName() {
        return Component.translatable("gui.recipebook.toggleRecipes.craftable");
    }

    @Override
    public WidgetSprites getFilterButtonTextures() {
        return new WidgetSprites(Identifier.withDefaultNamespace("recipe_book/filter_enabled"), Identifier.withDefaultNamespace("recipe_book/filter_disabled"), Identifier.withDefaultNamespace("recipe_book/filter_enabled_highlighted"), Identifier.withDefaultNamespace("recipe_book/filter_disabled_highlighted"));
    }

    private boolean canDisplay(RecipeDisplay recipeDisplay) {
        return true;
    }
}