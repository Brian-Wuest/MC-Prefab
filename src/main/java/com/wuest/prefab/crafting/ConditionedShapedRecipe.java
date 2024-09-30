package com.wuest.prefab.crafting;

import com.google.common.base.Strings;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wuest.prefab.ModRegistry;
import com.wuest.prefab.Prefab;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

import java.util.Arrays;
import java.util.Map;

public class ConditionedShapedRecipe extends ShapedRecipe {

    final int width;
    final int height;
    final CraftingBookCategory craftingBookCategory;
    final NonNullList<Ingredient> inputs;
    final String group;
    final String configName;
    final boolean recipeHasTags;
    ShapedRecipePattern pattern;
    ItemStack output;
    boolean reloadedTags;
    boolean showNotification;

    public ConditionedShapedRecipe(
            String group,
            CraftingBookCategory craftingBookCategory,
            ShapedRecipePattern pattern,
            ItemStack output,
            String configName,
            boolean recipeHasTags,
            boolean showNotification
    ) {
        super(group, craftingBookCategory, pattern, output, showNotification);

        this.group = group;
        this.craftingBookCategory = craftingBookCategory;
        this.width = pattern.width();
        this.height = pattern.height();
        this.inputs = pattern.ingredients();
        this.pattern = pattern;
        this.output = output;
        this.configName = configName;
        this.recipeHasTags = recipeHasTags;
        this.reloadedTags = false;
        this.showNotification = showNotification;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRegistry.ConditionedShapedRecipeSeriaizer.get();
    }

    @Override
    public String getGroup() {
        return this.group;
    }

    @Override
    public CraftingBookCategory category() {
        return this.craftingBookCategory;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registryAccess) {
        return this.output;
    }

    @Override
    public  NonNullList<Ingredient> getIngredients() {
        return this.inputs;
    }

    @Override
    public boolean showNotification() {
        return this.showNotification;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width >= this.width && height >= this.height;
    }

    @Override
    public boolean matches(CraftingContainer craftingInventory, Level level) {
        // Make sure to re-load any ingredients associated with tags.
        // This is necessary due to changes in how tags are loaded and how we use configurable recipes.
        if (this.recipeHasTags && !this.reloadedTags) {
            this.validateTagIngredients();
            this.reloadedTags = true;
        }

        for (int i = 0; i <= craftingInventory.getWidth() - this.width; ++i) {
            for (int j = 0; j <= craftingInventory.getHeight() - this.height; ++j) {
                if (this.matches(craftingInventory, i, j, true)) {
                    return true;
                }

                if (this.matches(craftingInventory, i, j, false)) {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public ItemStack assemble(CraftingContainer craftingContainer, HolderLookup.Provider registryAccess) {
        return this.getResultItem(registryAccess).copy();
    }

    @Override
    public int getWidth() {
        return this.width;
    }

    @Override
    public int getHeight() {
        return this.height;
    }

    private boolean matches(CraftingContainer inv, int offsetX, int offsetY, boolean bl) {
        for (int i = 0; i < inv.getWidth(); ++i) {
            for (int j = 0; j < inv.getHeight(); ++j) {
                int k = i - offsetX;
                int l = j - offsetY;
                Ingredient ingredient = Ingredient.EMPTY;
                if (k >= 0 && l >= 0 && k < this.width && l < this.height) {
                    if (bl) {
                        ingredient = this.inputs.get(this.width - k - 1 + l * this.width);
                    } else {
                        ingredient = this.inputs.get(k + l * this.width);
                    }
                }

                if (!ingredient.test(inv.getItem(i + j * inv.getWidth()))) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Re-validates the tag ingredients for this recipe.
     * This is necessary because the tags are not loaded at the time when the recipe is initially loaded.
     */
    private void validateTagIngredients() {
        boolean invalidRecipe = false;

        for (Ingredient ingredient : this.getIngredients()) {
            if (ingredient.getItems().length == 0) {
                // Found a tag ingredient, loop through the pattern to determine if any of the keys are missing ingredients.
                if (this.pattern.data().isPresent()) {
                    for(Map.Entry<Character, Ingredient> keyMap : this.pattern.data().get().key().entrySet()) {
                        if (keyMap.getValue().itemStacks.length == 0) {
                            invalidRecipe = true;
                            break;
                        }
                    }
                }

                break;
            }
        }

        if (invalidRecipe) {
            this.output = ItemStack.EMPTY;
        } else {
            this.output = Serializer.validateRecipeOutput(this.output, this.configName);
        }
    }

    public static class Serializer implements RecipeSerializer<ConditionedShapedRecipe> {
        public static final MapCodec<ConditionedShapedRecipe> CODEC = RecordCodecBuilder.mapCodec((instance) -> {
            return instance.group(
                    Codec.STRING.optionalFieldOf("group", "").forGetter(shapedRecipe -> {
                return shapedRecipe.group;
            }), CraftingBookCategory.CODEC.fieldOf("category").orElse(CraftingBookCategory.MISC).forGetter((shapedRecipe) -> {
                return shapedRecipe.craftingBookCategory;
            }), ShapedRecipePattern.MAP_CODEC.forGetter((shapedRecipe) -> {
                return shapedRecipe.pattern;
            }), ItemStack.STRICT_CODEC.fieldOf("result").forGetter((shapedRecipe) -> {
                return shapedRecipe.output;
            }), Codec.STRING.optionalFieldOf("configName", "").forGetter((shapedRecipe) -> {
                        return shapedRecipe.configName;
            }), Codec.BOOL.optionalFieldOf("recipe_has_tags", true).forGetter((shapedRecipe) -> {
                return shapedRecipe.recipeHasTags;
            }), Codec.BOOL.optionalFieldOf("show_notification", true).forGetter((shapedRecipe) -> {
                return shapedRecipe.showNotification;
            })).apply(instance, ConditionedShapedRecipe::new);
        });

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

        public static final StreamCodec<RegistryFriendlyByteBuf, ConditionedShapedRecipe> STREAM_CODEC = StreamCodec.of(
                ConditionedShapedRecipe.Serializer::toNetwork, ConditionedShapedRecipe.Serializer::fromNetwork
        );

        @Override
        public MapCodec<ConditionedShapedRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, ConditionedShapedRecipe> streamCodec() {
            return STREAM_CODEC;
        }

        public static ConditionedShapedRecipe fromNetwork(RegistryFriendlyByteBuf friendlyByteBuf) {
            String groupName = friendlyByteBuf.readUtf();
            String configName = friendlyByteBuf.readUtf();
            CraftingBookCategory craftingBookCategory = friendlyByteBuf.readEnum(CraftingBookCategory.class);
            ShapedRecipePattern shapedRecipePattern = ShapedRecipePattern.STREAM_CODEC.decode(friendlyByteBuf);

            // Custom bit which validates the recipe output, if the validation fails then an empty itemstack is returned.
            ItemStack itemStack = Serializer.validateRecipeOutput(ItemStack.STREAM_CODEC.decode(friendlyByteBuf), configName);

            boolean recipeHasTags = friendlyByteBuf.readBoolean();
            boolean showNotification = friendlyByteBuf.readBoolean();
            return new ConditionedShapedRecipe(groupName, craftingBookCategory, shapedRecipePattern, itemStack, configName, recipeHasTags, showNotification);
        }

        public static void toNetwork(RegistryFriendlyByteBuf friendlyByteBuf, ConditionedShapedRecipe shapedRecipe) {
            friendlyByteBuf.writeUtf(shapedRecipe.group);
            friendlyByteBuf.writeUtf(shapedRecipe.configName);
            friendlyByteBuf.writeEnum(shapedRecipe.craftingBookCategory);
            ShapedRecipePattern.STREAM_CODEC.encode(friendlyByteBuf, shapedRecipe.pattern);
            ItemStack.STREAM_CODEC.encode(friendlyByteBuf, shapedRecipe.output);
            friendlyByteBuf.writeBoolean(shapedRecipe.recipeHasTags);
            friendlyByteBuf.writeBoolean(shapedRecipe.showNotification);
        }
    }
}
