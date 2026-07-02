package net.salju.supernatural.client.screens;

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
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import java.util.List;

public class RitualRecipeBookComponent<T extends RitualBookMenu> extends RecipeBookComponent {
    public RitualRecipeBookComponent(T menu, List<TabInfo> tabs) {
        super(menu, tabs);
    }

    @Override
    public boolean isCraftingSlot(Slot var1) {
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
    public void selectMatchingRecipes(RecipeCollection var1, StackedItemContents var2) {
        //
    }

    @Override
    public Component getRecipeFilterName() {
        return Component.empty();
    }

    @Override
    public WidgetSprites getFilterButtonTextures() {
        return new WidgetSprites(Identifier.withDefaultNamespace("recipe_book/filter_enabled"), Identifier.withDefaultNamespace("recipe_book/filter_disabled"), Identifier.withDefaultNamespace("recipe_book/filter_enabled_highlighted"), Identifier.withDefaultNamespace("recipe_book/filter_disabled_highlighted"));
    }
}