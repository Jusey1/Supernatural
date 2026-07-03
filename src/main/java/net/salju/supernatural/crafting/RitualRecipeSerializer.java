package net.salju.supernatural.crafting;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;

public class RitualRecipeSerializer implements RecipeSerializer<RitualRecipe> {
    public static final MapCodec<RitualRecipe> CODEC = RecordCodecBuilder.mapCodec(
            codec -> codec.group(Codec.STRING.optionalFieldOf("group", "").forGetter(RitualRecipe::group),
                            Ingredient.CODEC.fieldOf("ingredient").forGetter(RitualRecipe::getInput),
                            ItemStack.STRICT_CODEC.fieldOf("result").forGetter(RitualRecipe::getResult),
                            Codec.INT.fieldOf("candle_power").forGetter(RitualRecipe::getPower),
                            Codec.INT.fieldOf("soul_power").forGetter(RitualRecipe::getSoul),
                            Codec.STRING.optionalFieldOf("entity", "minecraft:pig").forGetter(RitualRecipe::getEntityTarget),
                            ItemStack.STRICT_CODEC.optionalFieldOf("offhand").forGetter(RitualRecipe::getOffhandTarget),
                            ItemStack.STRICT_CODEC.optionalFieldOf("block").forGetter(RitualRecipe::getBlockTarget))
                    .apply(codec, RitualRecipe::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, RitualRecipe> STREAM_CODEC = StreamCodec.of(RitualRecipeSerializer::toNetwork, RitualRecipeSerializer::fromNetwork);

    public RitualRecipeSerializer() {
        //
    }

    @Override
    public MapCodec<RitualRecipe> codec() {
        return CODEC;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, RitualRecipe> streamCodec() {
        return STREAM_CODEC;
    }

    public static void toNetwork(RegistryFriendlyByteBuf buffer, RitualRecipe recipe) {
        buffer.writeUtf(recipe.group());
        Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, recipe.getInput());
        ItemStack.STREAM_CODEC.encode(buffer, recipe.getResult());
        buffer.writeInt(recipe.getPower());
        buffer.writeInt(recipe.getSoul());
        buffer.writeUtf(recipe.getEntityTarget());
    }

    public static RitualRecipe fromNetwork(RegistryFriendlyByteBuf buffer) {
        return new RitualRecipe(buffer.readUtf(32767), Ingredient.CONTENTS_STREAM_CODEC.decode(buffer), ItemStack.STREAM_CODEC.decode(buffer), buffer.readInt(), buffer.readInt(), buffer.readUtf(32767), Optional.empty(), Optional.empty());
    }
}