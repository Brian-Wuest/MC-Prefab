package com.wuest.prefab.Blocks;

import net.minecraft.item.ItemStack;

public interface IMetaBlock
{
	String getSpecialName(ItemStack stack);
	
	String getMetaDataUnLocalizedName(int metaData);
}
