package com.wuest.prefab.Config;

import net.minecraft.nbt.NBTTagCompound;

public class FishPondConfiguration extends StructureConfiguration
{
	public FishPondConfiguration ReadFromNBTTagCompound(NBTTagCompound messageTag) 
	{
		FishPondConfiguration config = new FishPondConfiguration();
		
		return (FishPondConfiguration)super.ReadFromNBTTagCompound(messageTag, config);
	}
}
