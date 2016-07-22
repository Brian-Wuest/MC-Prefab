package com.wuest.prefab.Config;

import net.minecraft.nbt.NBTTagCompound;

public class TreeFarmConfiguration extends StructureConfiguration 
{
	public TreeFarmConfiguration ReadFromNBTTagCompound(NBTTagCompound messageTag) 
	{
		TreeFarmConfiguration config = new TreeFarmConfiguration();
		
		return (TreeFarmConfiguration)super.ReadFromNBTTagCompound(messageTag, config);
	}
}
