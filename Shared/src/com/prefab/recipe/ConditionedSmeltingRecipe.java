package com.prefab.recipe;

import com.google.common.base.Strings;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.prefab.ModRegistryBase;
import com.prefab.PrefabBase;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import org.apache.commons.lang3.StringUtils;

public class ConditionedSmeltingRecipe extends SmeltingRecipe {
    private final String configName;

    public ConditionedSmeltingRecipe(
            String group,
            CookingBookCategory cookingBookCategory,
            Ingredient input,
            ItemStack output,
            float experience,
            int cookTime,
            String configName
    ) {
        super(group, cookingBookCategory, input, output, experience, cookTime);

        this.configName = configName;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRegistryBase.ConditionedSmeltingRecipeSeriaizer;
    }

    public static class Serializer implements RecipeSerializer<ConditionedSmeltingRecipe> {
        public static final MapCodec<ConditionedSmeltingRecipe> CODEC = RecordCodecBuilder.mapCodec((instance) ->
                instance.group(
                        Codec.STRING.optionalFieldOf("group", "").forGetter((o) -> o.group),
                        CookingBookCategory.CODEC.fieldOf("category").orElse(CookingBookCategory.MISC).forGetter((o) -> o.category),
                        Ingredient.CODEC_NONEMPTY.fieldOf("ingredient").forGetter((o) -> o.ingredient),
                        ItemStack.CODEC.fieldOf("result").forGetter((o) -> o.result),
                        Codec.FLOAT.optionalFieldOf("experience", 0.1f).forGetter((o) -> o.experience),
                        Codec.INT.optionalFieldOf("cookingtime", 200).forGetter((o) -> o.cookingTime),
                        Codec.STRING.optionalFieldOf("configName", "").forGetter((o) -> o.configName)

                ).apply(instance, ConditionedSmeltingRecipe::new)
        );

        public static final StreamCodec<RegistryFriendlyByteBuf, ConditionedSmeltingRecipe> STREAM_CODEC = StreamCodec.of(
                ConditionedSmeltingRecipe.Serializer::toNetwork, ConditionedSmeltingRecipe.Serializer::fromNetwork
        );

        @Override
        public MapCodec<ConditionedSmeltingRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, ConditionedSmeltingRecipe> streamCodec() {
            return STREAM_CODEC;
        }

        public static ConditionedSmeltingRecipe fromNetwork(RegistryFriendlyByteBuf friendlyByteBuf) {
            String group = friendlyByteBuf.readUtf();
            String configName = friendlyByteBuf.readUtf();
            CookingBookCategory cookingBookCategory = friendlyByteBuf.readEnum(CookingBookCategory.class);
            Ingredient ingredient = Ingredient.CONTENTS_STREAM_CODEC.decode(friendlyByteBuf);
            ItemStack itemStack = validateRecipeOutput(ItemStack.STREAM_CODEC.decode(friendlyByteBuf), configName);
            float experience = friendlyByteBuf.readFloat();
            int cookTime = friendlyByteBuf.readVarInt();
            return new ConditionedSmeltingRecipe(group, cookingBookCategory, ingredient, itemStack, experience, cookTime, configName);
        }

        public static void toNetwork(RegistryFriendlyByteBuf friendlyByteBuf, ConditionedSmeltingRecipe abstractCookingRecipe) {
            friendlyByteBuf.writeUtf(abstractCookingRecipe.group);
            friendlyByteBuf.writeUtf(abstractCookingRecipe.configName);
            friendlyByteBuf.writeEnum(abstractCookingRecipe.category());
            Ingredient.CONTENTS_STREAM_CODEC.encode(friendlyByteBuf, abstractCookingRecipe.ingredient);
            ItemStack.STREAM_CODEC.encode(friendlyByteBuf, abstractCookingRecipe.result);
            friendlyByteBuf.writeFloat(abstractCookingRecipe.experience);
            friendlyByteBuf.writeVarInt(abstractCookingRecipe.cookingTime);
        }
        public static ItemStack validateRecipeOutput(ItemStack originalOutput, String configName) {
            if (originalOutput == ItemStack.EMPTY) {
                return ItemStack.EMPTY;
            }

            if (!StringUtils.isBlank(configName)
                    && PrefabBase.serverConfiguration.recipes.containsKey(configName)
                    && !PrefabBase.serverConfiguration.recipes.get(configName)) {
                // The configuration option for this recipe was turned off.
                // Specify that the recipe has no output which basically makes it disabled.
                return ItemStack.EMPTY;
            }

            return originalOutput;
        }
    }

}
