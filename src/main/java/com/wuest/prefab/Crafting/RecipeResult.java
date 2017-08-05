package com.wuest.prefab.Crafting;

import com.google.gson.annotations.Expose;

/**
 * 
 * @author WuestMan
 *
 */
public class RecipeResult
{
	@Expose
	public ItemName item;
	
	@Expose
	public int count;

	public RecipeResult()
	{
		this.item = new ItemName();
	}
}
