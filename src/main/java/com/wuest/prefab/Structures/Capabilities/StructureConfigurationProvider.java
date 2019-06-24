package com.wuest.prefab.Structures.Capabilities;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.wuest.prefab.ModRegistry;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

/**
 * This is the provider class for the structure configuration capability.
 * 
 * @author WuestMan
 *
 */
public class StructureConfigurationProvider implements ICapabilitySerializable<CompoundNBT>
{
	private IStructureConfigurationCapability structureConfigurationCapability;

	/**
	 * Initializes a new instance of the StructureConfigurationProvider class.
	 * 
	 * @param structureConfigurationCapability The {@link IStructureConfigurationCapability} to be used by this class.
	 */
	public StructureConfigurationProvider(IStructureConfigurationCapability structureConfigurationCapability)
	{
		this.structureConfigurationCapability = structureConfigurationCapability;
	}

	@Override
	@Nonnull
	public <T> LazyOptional<T> getCapability(@Nonnull final Capability<T> cap, final @Nullable Direction side)
	{
		LazyOptional<IStructureConfigurationCapability> optionalOfCurrentCapability = LazyOptional.of(() -> structureConfigurationCapability);

		return cap == ModRegistry.StructureConfiguration ? optionalOfCurrentCapability.cast() : LazyOptional.empty();
	}

	@Override
	public CompoundNBT serializeNBT()
	{
		return (CompoundNBT) ModRegistry.StructureConfiguration.getStorage().writeNBT(ModRegistry.StructureConfiguration, this.structureConfigurationCapability, null);
	}

	@Override
	public void deserializeNBT(CompoundNBT nbt)
	{
		ModRegistry.StructureConfiguration.getStorage().readNBT(ModRegistry.StructureConfiguration, this.structureConfigurationCapability, null, nbt);
	}

}