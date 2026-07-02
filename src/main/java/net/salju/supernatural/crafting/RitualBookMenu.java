package net.salju.supernatural.crafting;

import net.salju.supernatural.init.SupernaturalData;
import net.salju.supernatural.init.SupernaturalItems;
import net.salju.supernatural.init.SupernaturalRecipes;
import net.salju.supernatural.item.component.SoulgemData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedItemContents;
import net.minecraft.world.inventory.RecipeBookMenu;
import net.minecraft.world.inventory.RecipeBookType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.crafting.RecipeHolder;

public class RitualBookMenu extends RecipeBookMenu {
    private final RitualBookContainer book;

    public RitualBookMenu(int id, Inventory inventory, RegistryFriendlyByteBuf registry) {
        this(id, inventory);
    }

    public RitualBookMenu(int id, Inventory inventory) {
        super(SupernaturalRecipes.RITUAL_BOOK.get(), id);
        this.book = new RitualBookContainer(this, 7);
        this.addSlot(new RitualBookSlot(this.book, 0, 80, 35));
        this.addSlot(new RitualBookSlot(this.book, 1, 80, 112));
        this.addSlot(new RitualBookSlot(this.book, 2, 80, 94));
        this.addSlot(new RitualBookSlot(this.book, 3, 50, 97));
        this.addSlot(new RitualBookSlot(this.book, 4, 110, 97));
        this.addSlot(new RitualBookSlot(this.book, 5, 80, 141));
    }

    @Override
    public RecipeBookMenu.PostPlaceAction handlePlacement(boolean check, boolean bool, RecipeHolder<?> holder, ServerLevel lvl, Inventory inventory) {
        if (holder.value() instanceof RitualRecipe) {
            this.placeRecipe((RecipeHolder<RitualRecipe>) holder);
        }
        return RecipeBookMenu.PostPlaceAction.NOTHING;
    }

    @Override
    public void slotsChanged(Container inventory) {
        //
    }

    @Override
    public void fillCraftSlotsStackedContents(StackedItemContents stacks) {
        //
    }

    @Override
    public void clearContainer(Player player, Container target) {
        target.clearContent();
    }

    @Override
    public void removed(Player player) {
        this.clearContainer(player, this.book);
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int i) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canTakeItemForPickAll(ItemStack stack, Slot slot) {
        return false;
    }

    @Override
    public RecipeBookType getRecipeBookType() {
        return RecipeBookType.valueOf("SUPERNATURAL_RITUALS");
    }

    public void placeRecipe(RecipeHolder<RitualRecipe> holder) {
        RitualRecipe recipe = holder.value();
        this.book.setItem(0, recipe.getResult());
        this.book.setItem(1, new ItemStack(recipe.getInput().getValues().get(0).value()));
        this.book.setItem(2, recipe.getBlockItem().getItem() instanceof SpawnEggItem ? ItemStack.EMPTY : recipe.getBlockItem());
        this.book.setItem(3, recipe.getOffhandItem());
        ItemStack soulgem = new ItemStack(SupernaturalItems.SOULGEM);
        soulgem.set(SupernaturalData.SOULGEM, new SoulgemData(new CompoundTag(), this.getSoul(recipe.getSoul())));
        this.book.setItem(4, soulgem);
        ItemStack candles = new ItemStack(SupernaturalItems.RITUAL_CANDLE);
        candles.setCount(recipe.getPower());
        this.book.setItem(5, candles);
    }

    public String getSoul(int i) {
        if (i >= 5) {
            return "soulgem.supernatural.grand";
        } else if (i == 4) {
            return "soulgem.supernatural.greater";
        } else if (i == 3) {
            return "soulgem.supernatural.common";
        } else if (i == 2) {
            return "soulgem.supernatural.lesser";
        }
        return "soulgem.supernatural.petty";
    }
}