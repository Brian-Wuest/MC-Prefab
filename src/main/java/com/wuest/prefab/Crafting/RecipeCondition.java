package com.wuest.prefab.Crafting;

import java.util.function.BooleanSupplier;

import com.google.gson.JsonObject;
import com.wuest.prefab.Prefab;

import net.minecraftforge.common.crafting.IConditionFactory;
import net.minecraftforge.common.crafting.JsonContext;

/**
 * 
 * @author WuestMan
 *
 */
public class RecipeCondition implements IConditionFactory
{
	private String recipeKeyName = "recipeKey";
	public String recipeKey;

	/**
	 * Initializes a new instance of the recipe condition class.
	 */
	public RecipeCondition()
	{
	}

	@Override
	public BooleanSupplier parse(JsonContext context, JsonObject json)
	{
		this.recipeKey = json.get(recipeKeyName).getAsString();

		return () -> this.determineActiveRecipe();
	}

	/**
	 * Determines if the recipe is active.
	 * 
	 * @return True if the recipe is active, otherwise false.
	 */
	public boolean determineActiveRecipe()
	{
		boolean result = false;

		if (this.recipeKey != null)
		{
			if (Prefab.proxy.getServerConfiguration().recipeConfiguration.containsKey(this.recipeKey))
			{
				result = Prefab.proxy.getServerConfiguration().recipeConfiguration.get(this.recipeKey);
			}
		}

		return result;
	}

}
