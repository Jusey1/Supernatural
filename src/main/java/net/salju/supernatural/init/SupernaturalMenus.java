package net.salju.supernatural.init;

import net.salju.supernatural.gui.RitualBookMenu;
import net.salju.supernatural.SupernaturalMod;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraft.world.inventory.MenuType;

public class SupernaturalMenus {
	public static final DeferredRegister<MenuType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.MENU_TYPES, SupernaturalMod.MODID);
	public static final RegistryObject<MenuType<RitualBookMenu>> RITUALBOOK = REGISTRY.register("ritual_book_gui", () -> IForgeMenuType.create(RitualBookMenu::new));
}