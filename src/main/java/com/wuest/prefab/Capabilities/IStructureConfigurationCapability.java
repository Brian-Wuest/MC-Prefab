package com.wuest.prefab.Capabilities;

import com.wuest.prefab.Config.Structures.BasicStructureConfiguration;

/**
 * This interface is used in the capability for the structure configuration for items.
 * @author WuestMan
 *
 */
public interface IStructureConfigurationCapability extends ITransferable<IStructureConfigurationCapability>
{
	/**
	 * Gets the configuration for this capability.
	 * @return The BasicStructureConfiguration for this capability.
	 */
	BasicStructureConfiguration getConfiguration();
	
	/**
	 * Determines if this capability needs to be updated.
	 * @return A value indicating whether this capability needs to be updated.
	 */
	boolean getDirty();
	
	/**
	 * Sets a value determining if this capability needs updating.
	 * @param value A boolean to determine if this capability needs updating.
	 */
	void setDirty(boolean value);
	
	/**
	 * Sets the configuration for this capability.
	 * @param configuration The new configuration to be used by this capability.
	 * @return An instance of this class for property shortcuts.
	 */
	IStructureConfigurationCapability setConfiguration(BasicStructureConfiguration configuration);
}
