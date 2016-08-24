package com.wuest.prefab.Items;

import com.wuest.prefab.ModRegistry;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

/**
 * @author WuestMan
 */
public class ItemBundleOfTimber extends Item
{
	public ItemBundleOfTimber(String name)
	{
		super();

		this.setCreativeTab(CreativeTabs.MATERIALS);
		ModRegistry.setItemName(this, name);
	}
}