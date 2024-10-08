package net.salju.supernatural.gui;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.GuiGraphics;

public class RitualBookScreen extends AbstractContainerScreen<RitualBookMenu> {
	private final RitualBookMenu book;
	private Button prev;
	private Button next;

	public RitualBookScreen(RitualBookMenu menu, Inventory inv, Component text) {
		super(menu, inv, text);
		this.imageWidth = 176;
		this.imageHeight = 170;
		this.book = menu;
	}

	@Override
	public void render(GuiGraphics ms, int x, int y, float ticks) {
		super.render(ms, x, y, ticks);
	}

	@Override
	protected void renderBg(GuiGraphics ms, float ticks, int x, int y) {
		ms.blit(new ResourceLocation("supernatural:textures/gui/ritualbook.png"), this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight, this.imageWidth, this.imageHeight);
	}

	@Override
	public void init() {
		super.init();
		prev = Button.builder(CommonComponents.GUI_BACK, e -> {
			if (true) {
				if (this.getPage() > 0) {
					this.setPage(this.getPage() - 1);
				}
			}
		}).bounds(this.leftPos + 90, this.topPos + 166, 50, 20).build();
		this.addRenderableWidget(prev);
		next = Button.builder(CommonComponents.GUI_CONTINUE, e -> {
			if (true) {
				this.setPage(this.getPage() + 1);
			}
		}).bounds(this.leftPos + 140, this.topPos + 166, 50, 20).build();
		this.addRenderableWidget(next);
	}

	public void setPage(int i) {
		this.book.setPage(i);
	}

	public int getPage() {
		return this.book.getPage();
	}
}