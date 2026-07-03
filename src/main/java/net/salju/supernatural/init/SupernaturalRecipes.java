package net.salju.supernatural.init;

import net.salju.supernatural.Supernatural;
import net.salju.supernatural.crafting.*;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.crafting.*;
import java.util.function.Supplier;

public class SupernaturalRecipes {
    public static final DeferredRegister<RecipeType<?>> TYPES = DeferredRegister.create(Registries.RECIPE_TYPE, Supernatural.MODID);
    public static final DeferredRegister<RecipeSerializer<?>> SERIALS = DeferredRegister.create(Registries.RECIPE_SERIALIZER, Supernatural.MODID);
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(Registries.MENU, Supernatural.MODID);
    public static final DeferredRegister<RecipeBookCategory> CATES = DeferredRegister.create(Registries.RECIPE_BOOK_CATEGORY, Supernatural.MODID);

    public static final Supplier<RecipeType<RitualRecipe>> RITUAL = TYPES.register("ritual", () -> registerRecipeType("ritual"));
    public static final Supplier<RecipeSerializer<RitualRecipe>> RITUAL_SERIAL = SERIALS.register("ritual", RitualRecipeSerializer::new);
    public static final Supplier<MenuType<RitualBookMenu>> RITUAL_BOOK = MENUS.register("ritual_book", () -> IMenuTypeExtension.create(RitualBookMenu::new));
    public static final Supplier<RecipeBookCategory> RITUALS = CATES.register("rituals", RecipeBookCategory::new);

    public static <T extends Recipe<?>> RecipeType<T> registerRecipeType(String str) {
        return new RecipeType<>() {
            public String toString() {
                return Supernatural.MODID + ":" + str;
            }
        };
    }
}