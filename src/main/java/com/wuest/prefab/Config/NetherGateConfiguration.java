package com.wuest.prefab.Config;

import net.minecraft.nbt.NBTTagCompound;

/**
 * @author WuestMan
 * This class is the configuration class for the nether gate.
 */
public class NetherGateConfiguration extends StructureConfiguration
{
	public NetherGateConfiguration ReadFromNBTTagCompound(NBTTagCompound messageTag) 
	{
		NetherGateConfiguration config = new NetherGateConfiguration();
		
		return (NetherGateConfiguration)super.ReadFromNBTTagCompound(messageTag, config);
	}
}