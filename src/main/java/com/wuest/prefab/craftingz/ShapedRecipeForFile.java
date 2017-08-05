package com.wuest.prefab.craftingz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import com.google.gson.*;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.config.Config.Comment;

/**
 * 
 * @author WuestMan
 *
 */
public class ShapedRecipeForFile
{
	@Expose
	public String type;

	@Expose
	public String group;

	@Expose
	public NonNullList<String> pattern;

	@Expose
	public IngredientItem key;

	@Expose
	public RecipeResult result;
	public String registryName;

	public ShapedRecipeForFile()
	{
		this.type = "minecraft:crafting_shaped";
		this.group = "";
		this.pattern = NonNullList.create();
		this.key = new IngredientItem();
		this.result = new RecipeResult();
		this.registryName = "";
	}

	public static ShapedRecipeForFile createFromShapedRecipe(ShapedRecipes recipe)
	{
		ShapedRecipeForFile convertedRecipe = new ShapedRecipeForFile();
		convertedRecipe.group = recipe.getGroup();

		ItemStack resultStack = recipe.getRecipeOutput();

		convertedRecipe.result.count = resultStack.getCount();
		convertedRecipe.result.item.item = resultStack.getItem().getRegistryName().toString();

		if (resultStack.getItemDamage() != -1)
		{
			convertedRecipe.result.item.data = resultStack.getItemDamage();
		}

		convertedRecipe.registryName = recipe.getRegistryName().getResourcePath();

		NonNullList<Ingredient> ingredients = recipe.getIngredients();
		String patternLine = "";

		HashMap<String, ItemName> mappedItems = new HashMap();
		mappedItems.put("a", null);
		mappedItems.put("b", null);
		mappedItems.put("c", null);
		mappedItems.put("d", null);
		mappedItems.put("e", null);
		mappedItems.put("f", null);
		mappedItems.put("g", null);
		mappedItems.put("h", null);
		mappedItems.put("i", null);

		for (int i = 0; i < 9; i++)
		{
			if (i == 3 || i == 6)
			{
				convertedRecipe.pattern.add(patternLine);
				patternLine = "";
			}

			Ingredient ingredient = ingredients.get(i);
			ItemStack[] stacks = ingredient.getMatchingStacks();

			if (stacks.length > 0)
			{
				ItemStack stack = stacks[0];

				if (stack != ItemStack.EMPTY)
				{
					ItemName itemName = new ItemName();
					itemName.item = stack.getItem().getRegistryName().toString();

					if (stack.getItemDamage() != -1)
					{
						itemName.data = stack.getItemDamage();
					}

					String key = "";

					for (Entry<String, ItemName> entry : mappedItems.entrySet())
					{
						if (entry.getValue() != null && entry.getValue().item.equals(stack.getItem().getRegistryName().toString()))
						{
							key = "existingItem";
							patternLine = patternLine + entry.getKey();
							break;
						}
						else if (entry.getValue() == null)
						{
							// This entry is blank, set it.
							key = entry.getKey();
							break;
						}
					}

					if (!key.equals(""))
					{
						if (!key.equals("existingItem"))
						{
							mappedItems.put(key, itemName);
							patternLine = patternLine + key;

							switch (key)
							{
								case "a":
								{
									convertedRecipe.key.a = itemName;
									break;
								}

								case "b":
								{
									convertedRecipe.key.b = itemName;
									break;
								}

								case "c":
								{
									convertedRecipe.key.c = itemName;
									break;
								}

								case "d":
								{
									convertedRecipe.key.d = itemName;
									break;
								}

								case "e":
								{
									convertedRecipe.key.e = itemName;
									break;
								}

								case "f":
								{
									convertedRecipe.key.f = itemName;
									break;
								}

								case "g":
								{
									convertedRecipe.key.g = itemName;
									break;
								}

								case "h":
								{
									convertedRecipe.key.h = itemName;
									break;
								}

								case "i":
								{
									convertedRecipe.key.i = itemName;
									break;
								}
							}
						}
					}
					else
					{
						patternLine = patternLine + " ";
					}
				}
				else
				{
					patternLine = patternLine + " ";
				}
			}
			else
			{
				patternLine = patternLine + " ";
			}
		}

		convertedRecipe.pattern.add(patternLine);

		return convertedRecipe;
	}
}
