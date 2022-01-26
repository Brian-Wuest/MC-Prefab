package com.wuest.prefab.crafting;

import com.google.gson.JsonObject;
import com.wuest.prefab.Prefab;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.IConditionSerializer;

/**
 * @author WuestMan
 */
public class SmeltingCondition implements ICondition {
    public static final ResourceLocation NAME = new ResourceLocation(Prefab.MODID, "smelting_recipe");
    public String recipeKeyName;

    /**
     * Initializes a new instance of the smelting condition class.
     */
    public SmeltingCondition(String identifier) {
        this.recipeKeyName = identifier;
    }

    @Override
    public ResourceLocation getID() {
        return NAME;
    }

    @Override
    public boolean test() {
        return this.determineActiveRecipe();
    }

    /**
     * Determines if the recipe is active.
     *
     * @return True if the recipe is active, otherwise false.
     */
    public boolean determineActiveRecipe() {
        boolean result = false;

        if (this.recipeKeyName != null) {
            if (Prefab.proxy.getServerConfiguration().recipeConfiguration.containsKey(this.recipeKeyName)) {
                result = Prefab.proxy.getServerConfiguration().recipeConfiguration.get(this.recipeKeyName);
            }
        }

        return result;
    }

    @SuppressWarnings("unused")
    public static class Serializer implements IConditionSerializer<SmeltingCondition> {
        public static final SmeltingCondition.Serializer INSTANCE = new SmeltingCondition.Serializer();

        @Override
        public void write(JsonObject json, SmeltingCondition value) {
            json.addProperty("recipeKey", value.recipeKeyName);
        }

        @Override
        public SmeltingCondition read(JsonObject json) {
            String recipeKeyName = "recipeKey";

            return new SmeltingCondition(json.get(recipeKeyName).getAsString());
        }

        @Override
        public ResourceLocation getID() {
            return SmeltingCondition.NAME;
        }
    }
}