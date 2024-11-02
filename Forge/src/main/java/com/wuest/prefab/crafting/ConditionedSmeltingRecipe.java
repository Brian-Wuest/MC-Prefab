package com.wuest.prefab.crafting;

import com.google.common.base.Strings;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.wuest.prefab.Prefab;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CookingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SmeltingRecipe;

public class ConditionedSmeltingRecipe extends SmeltingRecipe {
    private  final ResourceLocation resourceLocation;

    private final String configName;

    public ConditionedSmeltingRecipe(
            ResourceLocation resourceLocation,
            String group,
            CookingBookCategory cookingBookCategory,
            Ingredient input,
            ItemStack output,
            float experience,
            int cookTime,
            String configName
    ) {
        super(resourceLocation, group, cookingBookCategory, input, output, experience, cookTime);

        this.resourceLocation = resourceLocation;
        this.configName = configName;
    }

    public static class Serializer implements RecipeSerializer<ConditionedSmeltingRecipe> {
        public ConditionedSmeltingRecipe fromJson(ResourceLocation identifier, JsonObject jsonObject) {
            String string = GsonHelper.getAsString(jsonObject, "group", "");
            String configName = GsonHelper.getAsString(jsonObject, "configName", "");
            JsonElement jsonElement = GsonHelper.isArrayNode(jsonObject, "ingredient") ?  GsonHelper.getAsJsonArray(jsonObject, "ingredient") : GsonHelper.getAsJsonObject(jsonObject, "ingredient");
            Ingredient ingredient = Ingredient.fromJson((JsonElement)jsonElement);
            String string2 = GsonHelper.getAsString(jsonObject, "result");
            ResourceLocation identifier2 = new ResourceLocation(string2);
            ItemStack itemStack = new ItemStack(BuiltInRegistries.ITEM.getOptional(identifier2).orElseThrow(() -> {
                return new IllegalStateException("Item: " + string2 + " does not exist");
            }));

            itemStack = validateRecipeOutput(itemStack, configName);

            float experience = GsonHelper.getAsFloat(jsonObject, "experience", 0.0F);
            int cookingtime = GsonHelper.getAsInt(jsonObject, "cookingtime", 200);
            return new ConditionedSmeltingRecipe(identifier, string, CookingBookCategory.MISC, ingredient, itemStack, experience, cookingtime, configName);
        }

        public ConditionedSmeltingRecipe fromNetwork(ResourceLocation identifier, FriendlyByteBuf packetByteBuf) {
            String group = packetByteBuf.readUtf();
            String configName = packetByteBuf.readUtf() ;
            Ingredient ingredient = Ingredient.fromNetwork(packetByteBuf);
            ItemStack itemStack = validateRecipeOutput(packetByteBuf.readItem(), configName);
            float experience = packetByteBuf.readFloat();
            int cookTime = packetByteBuf.readVarInt();
            return new ConditionedSmeltingRecipe(identifier, group, CookingBookCategory.MISC, ingredient, itemStack, experience, cookTime, configName);
        }

        public void toNetwork(FriendlyByteBuf packetByteBuf, ConditionedSmeltingRecipe abstractCookingRecipe) {
            packetByteBuf.writeUtf(abstractCookingRecipe.group);
            packetByteBuf.writeUtf(abstractCookingRecipe.configName);
            abstractCookingRecipe.ingredient.toNetwork(packetByteBuf);
            packetByteBuf.writeItem(abstractCookingRecipe.result);
            packetByteBuf.writeFloat(abstractCookingRecipe.experience);
            packetByteBuf.writeVarInt(abstractCookingRecipe.cookingTime);
        }

        public static ItemStack validateRecipeOutput(ItemStack originalOutput, String configName) {
            if (originalOutput == ItemStack.EMPTY) {
                return ItemStack.EMPTY;
            }

            if (!Strings.isNullOrEmpty(configName)
                    && Prefab.proxy.getServerConfiguration().recipeConfiguration.containsKey(configName)
                    && !Prefab.proxy.getServerConfiguration().recipeConfiguration.get(configName)) {
                // The configuration option for this recipe was turned off.
                // Specify that the recipe has no output which basically makes it disabled.
                return ItemStack.EMPTY;
            }

            return originalOutput;
        }
    }

}
