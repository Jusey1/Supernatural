package net.salju.supernatural.crafting;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.salju.supernatural.init.SupernaturalRecipes;

public record RitualRecipeDisplay(SlotDisplay input, SlotDisplay result, SlotDisplay craftingStation) implements RecipeDisplay {
    public static final MapCodec<RitualRecipeDisplay> CODEC = RecordCodecBuilder.mapCodec((stuffs) -> stuffs.group(SlotDisplay.CODEC.fieldOf("input").forGetter(RitualRecipeDisplay::input), SlotDisplay.CODEC.fieldOf("result").forGetter(RitualRecipeDisplay::result), SlotDisplay.CODEC.fieldOf("crafting_station").forGetter(RitualRecipeDisplay::craftingStation)).apply(stuffs, RitualRecipeDisplay::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, RitualRecipeDisplay> STREAM_CODEC = StreamCodec.composite(SlotDisplay.STREAM_CODEC, RitualRecipeDisplay::input, SlotDisplay.STREAM_CODEC, RitualRecipeDisplay::result, SlotDisplay.STREAM_CODEC, RitualRecipeDisplay::craftingStation, RitualRecipeDisplay::new);

    @Override
    public RecipeDisplay.Type<RitualRecipeDisplay> type() {
        return SupernaturalRecipes.RITUAL_DISPLAY.get();
    }
}