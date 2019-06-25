package com.wuest.prefab.Structures.Capabilities.Storage;

import com.wuest.prefab.Structures.Capabilities.IStructureConfigurationCapability;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

/**
 * This is the storage class for the structure configuration capability.
 * 
 * @author WuestMan
 *
 */
public class StructureConfigurationStorage implements Capability.IStorage<IStructureConfigurationCapability>
{

	@Override
	public INBT writeNBT(
		Capability<IStructureConfigurationCapability> capability,
		IStructureConfigurationCapability instance, Direction side)
	{
		CompoundNBT tag = new CompoundNBT();

		tag.put("configuration", instance.getConfiguration().WriteToCompoundNBT());

		return tag;
	}

	@Override
	public void readNBT(
		Capability<IStructureConfigurationCapability> capability,
		IStructureConfigurationCapability instance, Direction side,
		INBT nbt)
	{
		CompoundNBT tag = (CompoundNBT) nbt;
		instance.setConfiguration(instance.getConfiguration().ReadFromCompoundNBT(tag.getCompound("configuration")));
	}

}
