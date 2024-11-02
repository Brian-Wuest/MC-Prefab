package com.wuest.prefab.crafting;

import com.google.common.base.Strings;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.wuest.prefab.ModRegistry;
import com.wuest.prefab.Prefab;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.minecraft.world.level.Level;

import java.util.Iterator;

public class ConditionedShapelessRecipe extends ShapelessRecipe {
    private final ResourceLocation resourceLocation;
    private final String group;
    CraftingBookCategory category;
    private final ItemStack output;
    private final NonNullList<Ingredient> ingredients;
    private final String configName;

    public ConditionedShapelessRecipe(
            ResourceLocation resourceLocation,
            String group,
            CraftingBookCategory craftingBookCategory,
            ItemStack output,
            NonNullList<Ingredient> ingredients,
            String configName
    ) {
        super(resourceLocation, group, craftingBookCategory, output, ingredients);

        this.resourceLocation = resourceLocation;
        this.group = group;
        this.output = output;
        this.ingredients = ingredients;
        this.configName = configName;
        this.category = craftingBookCategory;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRegistry.ConditionedShapelessRecipeSeriaizer.get();
    }

    @Override
    public String getGroup() {
        return this.group;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess registryAccess) {
        return this.output;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return this.ingredients;
    }

    @Override
    public boolean matches(CraftingContainer craftingInventory, Level world) {
        StackedContents stackedContents = new StackedContents();
        int i = 0;

        for (int j = 0; j < craftingInventory.getContainerSize(); ++j) {
            ItemStack itemStack = craftingInventory.getItem(j);

            if (!itemStack.isEmpty()) {
                ++i;
                stackedContents.accountStack(itemStack, 1);
            }
        }

        return i == this.ingredients.size() && stackedContents.canCraft(this, null);
    }

    @Override
    public ItemStack assemble(CraftingContainer craftingContainer, RegistryAccess registryAccess) {
        return this.output.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= this.ingredients.size();
    }

    public static class Serializer implements RecipeSerializer<ConditionedShapelessRecipe> {
        public ConditionedShapelessRecipe fromJson(ResourceLocation identifier, JsonObject jsonObject) {
            String groupName = GsonHelper.getAsString(jsonObject, "group", "");
            String configName = GsonHelper.getAsString(jsonObject, "configName", "");
            NonNullList<Ingredient> defaultedList = itemsFromJson(GsonHelper.getAsJsonArray(jsonObject, "ingredients"));

            if (defaultedList.isEmpty()) {
                throw new JsonParseException("No ingredients for shapeless recipe");
            } else if (defaultedList.size() > 9) {
                throw new JsonParseException("Too many ingredients for shapeless recipe");
            } else {
                ItemStack itemStack = validateRecipeOutput(ConditionedShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(jsonObject, "result")), configName);
                return new ConditionedShapelessRecipe(identifier, groupName, CraftingBookCategory.MISC, itemStack, defaultedList, configName);
            }
        }

        private static NonNullList<Ingredient> itemsFromJson(JsonArray json) {
            NonNullList<Ingredient> defaultedList = NonNullList.create();

            for (int i = 0; i < json.size(); ++i) {
                Ingredient ingredient = Ingredient.fromJson(json.get(i));
                if (!ingredient.isEmpty()) {
                    defaultedList.add(ingredient);
                }
            }

            return defaultedList;
        }


        public ConditionedShapelessRecipe fromNetwork(ResourceLocation identifier, FriendlyByteBuf packetByteBuf) {
            String groupName = packetByteBuf.readUtf();
            String configName = packetByteBuf.readUtf();
            int i = packetByteBuf.readVarInt();
            NonNullList<Ingredient> defaultedList = NonNullList.withSize(i, Ingredient.EMPTY);

            defaultedList.replaceAll(ignored -> Ingredient.fromNetwork(packetByteBuf));

            ItemStack itemStack = validateRecipeOutput(packetByteBuf.readItem(), configName);
            return new ConditionedShapelessRecipe(identifier, groupName, CraftingBookCategory.MISC, itemStack, defaultedList, configName);
        }

        public void toNetwork(FriendlyByteBuf packetByteBuf, ConditionedShapelessRecipe shapelessRecipe) {
            packetByteBuf.writeUtf(shapelessRecipe.group);
            packetByteBuf.writeUtf(shapelessRecipe.configName);
            packetByteBuf.writeVarInt(shapelessRecipe.ingredients.size());

            for(Ingredient ingredient : shapelessRecipe.ingredients) {
                ingredient.toNetwork(packetByteBuf);
            }

            packetByteBuf.writeItem(shapelessRecipe.output);
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
