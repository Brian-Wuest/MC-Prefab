package com.wuest.prefab.crafting;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.wuest.prefab.ModRegistry;
import com.wuest.prefab.Prefab;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;

public class ConditionedShapedRecipe extends ShapedRecipe {
    static int MAX_WIDTH = 3;
    static int MAX_HEIGHT = 3;

    final ResourceLocation resourceLocation;

    final int width;
    final int height;
    final CraftingBookCategory craftingBookCategory;
    final NonNullList<Ingredient> inputs;
    final String group;
    final String configName;
    final boolean recipeHasTags;
    ItemStack output;
    boolean reloadedTags;
    boolean showNotification;

    public ConditionedShapedRecipe(
            ResourceLocation resourceLocation,
            String group,
            CraftingBookCategory craftingBookCategory,
            int width,
            int height,
            NonNullList<Ingredient> ingredients,
            ItemStack output,
            String configName,
            boolean recipeHasTags,
            boolean showNotification
    ) {
        super(resourceLocation, group, craftingBookCategory, width, height, ingredients, output, showNotification);

        this.resourceLocation = resourceLocation;
        this.group = group;
        this.craftingBookCategory = craftingBookCategory;
        this.width = width;
        this.height = height;
        this.inputs = ingredients;
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
    public ItemStack getResultItem(RegistryAccess registryAccess) {
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
    public ItemStack assemble(CraftingContainer craftingContainer, RegistryAccess registryAccess) {
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
                ingredient.itemStacks = Arrays.stream(ingredient.values).flatMap((value) -> {
                    return value.getItems().stream();
                }).distinct().toArray(ItemStack[]::new);

                if (ingredient.itemStacks.length == 0) {
                    // There are no items associated with this tag; mark this recipe as invalid.
                    invalidRecipe = true;
                    break;
                }
            }
        }

        if (invalidRecipe) {
            this.output = ItemStack.EMPTY;
        } else {
            this.output = Serializer.validateRecipeOutput(this.output, this.configName);
        }
    }

    static NonNullList<Ingredient> dissolvePattern(String[] p_44203_, Map<String, Ingredient> p_44204_, int p_44205_, int p_44206_) {
        NonNullList<Ingredient> nonnulllist = NonNullList.withSize(p_44205_ * p_44206_, Ingredient.EMPTY);
        Set<String> set = Sets.newHashSet(p_44204_.keySet());
        set.remove(" ");

        for(int i = 0; i < p_44203_.length; ++i) {
            for(int j = 0; j < p_44203_[i].length(); ++j) {
                String s = p_44203_[i].substring(j, j + 1);
                Ingredient ingredient = p_44204_.get(s);
                if (ingredient == null) {
                    throw new JsonSyntaxException("Pattern references symbol '" + s + "' but it's not defined in the key");
                }

                set.remove(s);
                nonnulllist.set(j + p_44205_ * i, ingredient);
            }
        }

        if (!set.isEmpty()) {
            throw new JsonSyntaxException("Key defines symbols that aren't used in pattern: " + set);
        } else {
            return nonnulllist;
        }
    }

    @VisibleForTesting
    static String[] shrink(String... p_44187_) {
        int i = Integer.MAX_VALUE;
        int j = 0;
        int k = 0;
        int l = 0;

        for(int i1 = 0; i1 < p_44187_.length; ++i1) {
            String s = p_44187_[i1];
            i = Math.min(i, firstNonSpace(s));
            int j1 = lastNonSpace(s);
            j = Math.max(j, j1);
            if (j1 < 0) {
                if (k == i1) {
                    ++k;
                }

                ++l;
            } else {
                l = 0;
            }
        }

        if (p_44187_.length == l) {
            return new String[0];
        } else {
            String[] astring = new String[p_44187_.length - l - k];

            for(int k1 = 0; k1 < astring.length; ++k1) {
                astring[k1] = p_44187_[k1 + k].substring(i, j + 1);
            }

            return astring;
        }
    }

    private static int firstNonSpace(String p_44185_) {
        int i;

        for(i = 0; i < p_44185_.length() && p_44185_.charAt(i) == ' '; ++i) {
        }

        return i;
    }

    private static int lastNonSpace(String p_44201_) {
        int i;

        for(i = p_44201_.length() - 1; i >= 0 && p_44201_.charAt(i) == ' '; --i) {
        }

        return i;
    }

    static String[] patternFromJson(JsonArray p_44197_) {
        String[] astring = new String[p_44197_.size()];
        if (astring.length > MAX_HEIGHT) {
            throw new JsonSyntaxException("Invalid pattern: too many rows, " + MAX_HEIGHT + " is maximum");
        } else if (astring.length == 0) {
            throw new JsonSyntaxException("Invalid pattern: empty pattern not allowed");
        } else {
            for(int i = 0; i < astring.length; ++i) {
                String s = GsonHelper.convertToString(p_44197_.get(i), "pattern[" + i + "]");
                if (s.length() > MAX_WIDTH) {
                    throw new JsonSyntaxException("Invalid pattern: too many columns, " + MAX_WIDTH + " is maximum");
                }

                if (i > 0 && astring[0].length() != s.length()) {
                    throw new JsonSyntaxException("Invalid pattern: each row must be the same width");
                }

                astring[i] = s;
            }

            return astring;
        }
    }

    static Map<String, Ingredient> keyFromJson(JsonObject p_44211_) {
        Map<String, Ingredient> map = Maps.newHashMap();

        for(Map.Entry<String, JsonElement> entry : p_44211_.entrySet()) {
            if (entry.getKey().length() != 1) {
                throw new JsonSyntaxException("Invalid key entry: '" + (String)entry.getKey() + "' is an invalid symbol (must be 1 character only).");
            }

            if (" ".equals(entry.getKey())) {
                throw new JsonSyntaxException("Invalid key entry: ' ' is a reserved symbol.");
            }

            map.put(entry.getKey(), Ingredient.fromJson(entry.getValue(), false));
        }

        map.put(" ", Ingredient.EMPTY);
        return map;
    }

    public static class Serializer implements RecipeSerializer<ConditionedShapedRecipe> {
        private static final ResourceLocation NAME = new ResourceLocation(Prefab.MODID, "crafting_shaped");

        public ConditionedShapedRecipe fromJson(ResourceLocation resourceLocation, JsonObject jsonObject) {
            String s = GsonHelper.getAsString(jsonObject, "group", "");
            CraftingBookCategory craftingbookcategory = CraftingBookCategory.CODEC.byName(GsonHelper.getAsString(jsonObject, "category", (String)null), CraftingBookCategory.MISC);
            Map<String, Ingredient> map = ConditionedShapedRecipe.keyFromJson(GsonHelper.getAsJsonObject(jsonObject, "key"));
            String[] astring = ConditionedShapedRecipe.shrink(ConditionedShapedRecipe.patternFromJson(GsonHelper.getAsJsonArray(jsonObject, "pattern")));
            int i = astring[0].length();
            int j = astring.length;
            NonNullList<Ingredient> nonnulllist = ConditionedShapedRecipe.dissolvePattern(astring, map, i, j);
            ItemStack itemstack = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(jsonObject, "result"));
            String configName = GsonHelper.getAsString(jsonObject, "configName");
            boolean recipeHasTags = GsonHelper.getAsBoolean(jsonObject, "recipe_has_tags", false);
            boolean flag = GsonHelper.getAsBoolean(jsonObject, "show_notification", true);
            return new ConditionedShapedRecipe(resourceLocation, s, craftingbookcategory, i, j, nonnulllist, itemstack, configName, recipeHasTags, flag);
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

        @Override
        public ConditionedShapedRecipe fromNetwork(ResourceLocation resourceLocation, FriendlyByteBuf friendlyByteBuf) {
            int width = friendlyByteBuf.readVarInt();
            int height = friendlyByteBuf.readVarInt();
            String groupName = friendlyByteBuf.readUtf();
            String configName = friendlyByteBuf.readUtf();
            CraftingBookCategory craftingBookCategory = friendlyByteBuf.readEnum(CraftingBookCategory.class);
            NonNullList<Ingredient> ingredients = NonNullList.withSize(width * height, Ingredient.EMPTY);

            ingredients.replaceAll(ignored -> Ingredient.fromNetwork(friendlyByteBuf));

            // Custom bit which validates the recipe output, if the validation fails then an empty itemstack is returned.
            ItemStack itemStack = Serializer.validateRecipeOutput(friendlyByteBuf.readItem(), configName);

            boolean recipeHasTags = friendlyByteBuf.readBoolean();
            boolean showNotification = friendlyByteBuf.readBoolean();
            return new ConditionedShapedRecipe(resourceLocation, groupName, craftingBookCategory, width, height, ingredients, itemStack, configName, recipeHasTags, showNotification);
        }

        @Override
        public void toNetwork(FriendlyByteBuf friendlyByteBuf, ConditionedShapedRecipe shapedRecipe) {
            friendlyByteBuf.writeVarInt(shapedRecipe.width);
            friendlyByteBuf.writeVarInt(shapedRecipe.height);
            friendlyByteBuf.writeUtf(shapedRecipe.group);
            friendlyByteBuf.writeUtf(shapedRecipe.configName);
            friendlyByteBuf.writeEnum(shapedRecipe.craftingBookCategory);

            for(Ingredient ingredient : shapedRecipe.inputs) {
                ingredient.toNetwork(friendlyByteBuf);
            }

            friendlyByteBuf.writeItem(shapedRecipe.output);
            friendlyByteBuf.writeBoolean(shapedRecipe.recipeHasTags);
            friendlyByteBuf.writeBoolean(shapedRecipe.showNotification);
        }
    }
}
