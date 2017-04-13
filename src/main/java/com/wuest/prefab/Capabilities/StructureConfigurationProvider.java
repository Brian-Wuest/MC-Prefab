package com.wuest.prefab.Capabilities;

import com.wuest.prefab.ModRegistry;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

/**
 * This is the provider class for the structure configuration capability.
 * @author WuestMan
 *
 */
public class StructureConfigurationProvider implements ICapabilitySerializable<NBTTagCompound>
{
	private IStructureConfigurationCapability structureConfigurationCapability;
	
	/**
	 * Initializes a new instance of the StructureConfigurationProvider class.
	 * @param structureConfigurationCapability The {@link IStructureConfigurationCapability} to be used by this class.
	 */
	public StructureConfigurationProvider(IStructureConfigurationCapability structureConfigurationCapability)
	{
		this.structureConfigurationCapability = structureConfigurationCapability;
	}
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing)
	{
		return capability == ModRegistry.StructureConfiguration;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing)
	{
		return capability == ModRegistry.StructureConfiguration ? ModRegistry.StructureConfiguration.cast(this.structureConfigurationCapability) : null;
	}

	@Override
	public NBTTagCompound serializeNBT()
	{
		return (NBTTagCompound)ModRegistry.StructureConfiguration.getStorage().writeNBT(ModRegistry.StructureConfiguration, this.structureConfigurationCapability, null);
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt)
	{
		ModRegistry.StructureConfiguration.getStorage().readNBT(ModRegistry.StructureConfiguration, this.structureConfigurationCapability, null, nbt);
	}

}