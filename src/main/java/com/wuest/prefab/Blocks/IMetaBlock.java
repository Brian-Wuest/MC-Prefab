package com.wuest.prefab.Blocks;

import net.minecraft.item.ItemStack;

/**
 * 
 * @author WuestMan
 *
 */
public interface IMetaBlock
{
	/**
	 * Gets the special name for this meta block.
	 * @param stack The item stack used to assist with getting the name.
	 * @return The special name.
	 */
	String getSpecialName(ItemStack stack);
	
	/**
	 * Gets the unlocalized name using meta data.
	 * @param metaData The meta data used to get the unlocalized name.
	 * @return The unlocalized name.
	 */
	String getMetaDataUnLocalizedName(int metaData);
}
