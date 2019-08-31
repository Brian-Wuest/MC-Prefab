package com.wuest.prefab.Crafting;

import com.google.gson.JsonObject;
import com.wuest.prefab.Prefab;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.IConditionSerializer;


/**
 * @author WuestMan
 */
public class RecipeCondition implements ICondition {
	public String recipeKey;
	public static final ResourceLocation NAME = new ResourceLocation(Prefab.MODID, "config_recipe");

	/**
	 * Initializes a new instance of the recipe condition class.
	 */
	public RecipeCondition(String recipeKey) {
		this.recipeKey = recipeKey;
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
	private boolean determineActiveRecipe() {
		boolean result = false;

		if (this.recipeKey != null) {
			if (Prefab.proxy.getServerConfiguration().recipeConfiguration.containsKey(this.recipeKey)) {
				result = Prefab.proxy.getServerConfiguration().recipeConfiguration.get(this.recipeKey);
			}
		}

		return result;
	}

	@SuppressWarnings("unused")
	public static class Serializer implements IConditionSerializer<RecipeCondition> {
		public static final RecipeCondition.Serializer INSTANCE = new RecipeCondition.Serializer();

		@Override
		public void write(JsonObject json, RecipeCondition value) {
			json.addProperty("recipeKey", value.recipeKey);
		}

		@Override
		public RecipeCondition read(JsonObject json) {
			String recipeKeyName = "recipeKey";

			return new RecipeCondition(json.get(recipeKeyName).getAsString());
		}

		@Override
		public ResourceLocation getID() {
			return RecipeCondition.NAME;
		}
	}
}
