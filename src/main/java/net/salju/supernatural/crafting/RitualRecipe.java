package net.salju.supernatural.crafting;

import net.salju.supernatural.init.SupernaturalData;
import net.salju.supernatural.init.SupernaturalItems;
import net.salju.supernatural.init.SupernaturalRecipes;
import net.salju.supernatural.init.SupernaturalTags;
import net.salju.supernatural.item.component.SoulgemData;
import net.minecraft.core.HolderLookup;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.TagValueOutput;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.Nullable;
import java.util.List;
import java.util.Optional;

public class RitualRecipe implements Recipe<RitualRecipeInput> {
    private final String group;
    private final String category;
    private final Ingredient input;
    private final ItemStack result;
    private final int power;
    private final int soul;
    private final String entity;
    private final Optional<ItemStack> offhand;
    private final Optional<ItemStack> block;
    private @Nullable PlacementInfo info;

    public RitualRecipe(String group, String category, Ingredient input, ItemStack result, int power, int soul, String entity, Optional<ItemStack> offhand, Optional<ItemStack> block) {
        this.group = group;
        this.category = category;
        this.input = input;
        this.result = result;
        this.power = power;
        this.soul = soul;
        this.entity = entity;
        this.offhand = offhand;
        this.block = block;
    }

    @Override
    public boolean matches(RitualRecipeInput input, Level world) {
        return this.input.test(input.getItem(0)) && this.shouldPerformRitual(input.getPower(), input.getSoul(), input.getBlockState(), input.getOffhandItem());
    }

    @Override
    public ItemStack assemble(RitualRecipeInput input, HolderLookup.Provider provider) {
        return this.result.copy();
    }

    @Override
    public PlacementInfo placementInfo() {
        if (this.info == null) {
            this.info = PlacementInfo.create(List.of(this.getInput()));
        }
        return this.info;
    }

    @Override
    public List<RecipeDisplay> display() {
        return List.of(new RitualRecipeDisplay(this.getInput().display(), new SlotDisplay.ItemStackSlotDisplay(this.getResult()), new SlotDisplay.ItemSlotDisplay(SupernaturalItems.RITUAL_BOOK)));
    }

    @Override
    public RecipeBookCategory recipeBookCategory() {
        if (this.category.contains("special")) {
            return SupernaturalRecipes.SPECIAL.get();
        } else if (this.category.contains("summon")) {
            return SupernaturalRecipes.SUMMONS.get();
        }
        return SupernaturalRecipes.RITUALS.get();
    }

    @Override
    public RecipeType<RitualRecipe> getType() {
        return SupernaturalRecipes.RITUAL.get();
    }

    @Override
    public RecipeSerializer<RitualRecipe> getSerializer() {
        return SupernaturalRecipes.RITUAL_SERIAL.get();
    }

    @Override
    public String group() {
        return this.group;
    }

    public String category() {
        return this.category;
    }

    public Ingredient getInput() {
        return this.input;
    }

    public ItemStack getResult(ItemStack stack, ItemStack offer) {
        if (this.getEntityTarget().contains("soulgem")) {
            return offer.copy();
        } else if (this.getResult().is(SupernaturalItems.SOULGEM)) {
            ItemStack copy = this.getResult().copy();
            TagValueOutput tag = TagValueOutput.createWithoutContext(ProblemReporter.DISCARDING);
            tag.putString("id", this.getEntityTarget());
            copy.set(SupernaturalData.SOULGEM, new SoulgemData(tag.buildResult(), "null"));
            return copy;
        } else if (stack.is(SupernaturalTags.SAFE)) {
            return stack.transmuteCopy(this.getResult().getItem());
        }
        return this.getResult();
    }

    public ItemStack getResult() {
        return this.result;
    }

    public ItemStack getOffhandItem() {
        return this.getOffhandTarget().orElse(ItemStack.EMPTY);
    }

    public Optional<ItemStack> getOffhandTarget() {
        return this.offhand;
    }

    public ItemStack getBlockItem() {
        return this.getBlockTarget().orElse(ItemStack.EMPTY);
    }

    public Optional<ItemStack> getBlockTarget() {
        return this.block;
    }

    public int getPower() {
        return this.power;
    }

    public int getSoul() {
        return this.soul;
    }

    public String getEntityTarget() {
        return this.entity;
    }

    public boolean checkBlockItem(Block blok) {
        if (this.getBlockItem().getItem() instanceof SpawnEggItem) {
            return true;
        }
        return this.getBlockItem().isEmpty() || this.getBlockItem().getItem() == blok.asItem();
    }

    public boolean checkOffhand(ItemStack stack) {
        return this.getOffhandItem().isEmpty() || this.getOffhandItem().getItem() == stack.getItem();
    }

    public boolean shouldPerformRitual(int i, int e, BlockState state, ItemStack stack) {
        return i == this.getPower() && e >= this.getSoul() && this.checkBlockItem(state.getBlock()) && this.checkOffhand(stack);
    }
}