package com.wuest.prefab.Config;

import net.minecraft.nbt.NBTTagCompound;

/**
 * The horse stable configuration.
 * @author WuestMan
 */
public class HorseStableConfiguration extends StructureConfiguration
{
	public HorseStableConfiguration ReadFromNBTTagCompound(NBTTagCompound messageTag) 
	{
		HorseStableConfiguration config = new HorseStableConfiguration();
		
		return (HorseStableConfiguration)super.ReadFromNBTTagCompound(messageTag, config);
	}
}
