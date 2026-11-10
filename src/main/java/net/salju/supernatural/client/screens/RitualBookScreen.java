package net.salju.supernatural.client.screens;

import net.salju.supernatural.Supernatural;
import net.salju.supernatural.crafting.RitualBookMenu;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.client.gui.screens.recipebook.RecipeUpdateListener;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;

public class RitualBookScreen extends AbstractContainerScreen<RitualBookMenu> implements RecipeUpdateListener {
    private final RitualRecipeBookComponent<?> recipeBookComponent;
    private boolean check;

    public RitualBookScreen(RitualBookMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        this.recipeBookComponent = new RitualRecipeBookComponent<>(menu);
    }

    @Override
    protected void init() {
        super.init();
        this.check = this.width < 379;
        this.recipeBookComponent.init(this.width, this.height, this.minecraft, this.check);
        this.leftPos = this.recipeBookComponent.updateScreenPosition(this.width, this.imageWidth);
        this.addRenderableWidget(new ImageButton(this.leftPos + 6, this.height / 2 - 76, 20, 18, RecipeBookComponent.RECIPE_BUTTON_SPRITES, (target) -> {
            this.recipeBookComponent.toggleVisibility();
            this.leftPos = this.recipeBookComponent.updateScreenPosition(this.width, this.imageWidth);
            target.setPosition(this.leftPos + 6, this.height / 2 - 76);
        }));
        this.addWidget(this.recipeBookComponent);
        this.titleLabelX = 53;
    }

    @Override
    public void render(GuiGraphics gui, int i, int e, float f) {
        if (this.recipeBookComponent.isVisible() && this.check) {
            this.renderBackground(gui, i, e, f);
            this.recipeBookComponent.render(gui, i, e, f);
        } else {
            super.render(gui, i, e, f);
            this.recipeBookComponent.render(gui, i, e, f);
        }
        this.renderTooltip(gui, i, e);
        this.recipeBookComponent.renderTooltip(gui, i, e, null);
    }

    @Override
    protected void renderBg(GuiGraphics gui, float f, int a, int e) {
        int i = this.leftPos;
        int j = (this.height - this.imageHeight) / 2;
        gui.blit(RenderPipelines.GUI_TEXTURED, Identifier.fromNamespaceAndPath(Supernatural.MODID, "textures/gui/ritual_book.png"), i, j, 0.0F, 0.0F, this.imageWidth, this.imageHeight, 256, 256);
    }

    @Override
    protected void renderLabels(GuiGraphics gui, int mouseX, int mouseY) {
        gui.drawString(this.font, this.title, this.titleLabelX, this.titleLabelY, 4210752, false);
    }

    @Override
    public boolean charTyped(CharacterEvent event) {
        return this.recipeBookComponent.charTyped(event) ? true : super.charTyped(event);
    }

    @Override
    public boolean keyPressed(KeyEvent event) {
        return this.recipeBookComponent.keyPressed(event) ? true : super.keyPressed(event);
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean bool) {
        if (this.recipeBookComponent.mouseClicked(event, bool)) {
            this.setFocused(this.recipeBookComponent);
            return true;
        } else {
            return this.check && this.recipeBookComponent.isVisible() ? true : super.mouseClicked(event, bool);
        }
    }

    @Override
    public boolean mouseDragged(MouseButtonEvent event, double d, double db) {
        return this.recipeBookComponent.mouseDragged(event, d, db) ? true : super.mouseDragged(event, d, db);
    }

    @Override
    protected boolean isHovering(int a, int i, int e, int u, double d, double db) {
        return (!this.check || !this.recipeBookComponent.isVisible()) && super.isHovering(a, i, e, u, d, db);
    }

    @Override
    protected boolean hasClickedOutside(double d, double db, int i, int e) {
        boolean flag = d < i || db < e || d >= i + this.imageWidth || db >= e + this.imageHeight;
        return this.recipeBookComponent.hasClickedOutside(d, db, this.leftPos, this.topPos, this.imageWidth, this.imageHeight) && flag;
    }

    @Override
    protected void slotClicked(Slot slot, int i, int e, ClickType type) {
        super.slotClicked(slot, i, e, type);
        this.recipeBookComponent.slotClicked(slot);
    }

    @Override
    public void containerTick() {
        super.containerTick();
        this.recipeBookComponent.tick();
    }

    @Override
    public void recipesUpdated() {
        this.recipeBookComponent.recipesUpdated();
    }

    @Override
    public void fillGhostRecipe(RecipeDisplay display) {
        //
    }
}