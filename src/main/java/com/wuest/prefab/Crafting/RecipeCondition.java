package com.wuest.prefab.Crafting;

import java.util.function.BooleanSupplier;

import com.google.gson.JsonObject;
import com.wuest.prefab.Prefab;

import net.minecraftforge.common.crafting.IConditionSerializer;


/**
 * 
 * @author WuestMan
 *
 */
public class RecipeCondition implements IConditionSerializer
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
	public BooleanSupplier parse(JsonObject json)
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
