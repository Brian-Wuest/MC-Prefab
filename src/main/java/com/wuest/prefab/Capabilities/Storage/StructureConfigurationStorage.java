package com.wuest.prefab.Capabilities.Storage;

import com.wuest.prefab.Capabilities.IStructureConfigurationCapability;
import com.wuest.prefab.Config.BasicStructureConfiguration;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

/**
 * This is the storage class for the structure configuration capability.
 * @author WuestMan
 *
 */
public class StructureConfigurationStorage implements Capability.IStorage<IStructureConfigurationCapability>
{

	@Override
	public NBTBase writeNBT(
			Capability<IStructureConfigurationCapability> capability,
			IStructureConfigurationCapability instance, EnumFacing side)
	{
		NBTTagCompound tag = new NBTTagCompound();
		
		tag.setTag("configuration", instance.getConfiguration().WriteToNBTTagCompound());
		
		return tag;
	}

	@Override
	public void readNBT(
			Capability<IStructureConfigurationCapability> capability,
			IStructureConfigurationCapability instance, EnumFacing side,
			NBTBase nbt)
	{
		NBTTagCompound tag = (NBTTagCompound) nbt;
		instance.setConfiguration(instance.getConfiguration().ReadFromNBTTagCompound(tag.getCompoundTag("configuration")));
	}

}
