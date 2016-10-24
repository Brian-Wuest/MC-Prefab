package com.wuest.prefab.Config;

import net.minecraft.nbt.NBTTagCompound;

/**
 * The configuration class for the modular house.
 * @author WuestMan
 *
 */
public class ModularHouseConfiguration extends StructureConfiguration
{
	public ModularHouseConfiguration ReadFromNBTTagCompound(NBTTagCompound messageTag) 
	{
		ModularHouseConfiguration config = new ModularHouseConfiguration();
		
		return (ModularHouseConfiguration)super.ReadFromNBTTagCompound(messageTag, config);
	}
}
