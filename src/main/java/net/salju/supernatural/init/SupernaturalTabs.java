package net.salju.supernatural.init;

import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.CreativeModeTab;

public class SupernaturalTabs {
	public static CreativeModeTab SUPERNATURAL_TAB;

	public static void load() {
		SUPERNATURAL_TAB = new CreativeModeTab("supernatural_tab") {
			@Override
			public ItemStack makeIcon() {
				return new ItemStack(Items.ENDER_EYE);
			}

			@Override
			public boolean hasSearchBar() {
				return false;
			}
		};
	}
}