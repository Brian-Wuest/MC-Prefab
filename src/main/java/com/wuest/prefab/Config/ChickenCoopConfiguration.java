package com.wuest.prefab.Config;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;

public class ChickenCoopConfiguration extends StructureConfiguration
{
	public ChickenCoopConfiguration ReadFromNBTTagCompound(NBTTagCompound messageTag) 
	{
		ChickenCoopConfiguration config = new ChickenCoopConfiguration();
		
		return (ChickenCoopConfiguration)super.ReadFromNBTTagCompound(messageTag, config);
	}
}