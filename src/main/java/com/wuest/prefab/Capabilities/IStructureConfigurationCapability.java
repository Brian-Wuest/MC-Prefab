package com.wuest.prefab.Capabilities;

import com.wuest.prefab.Config.BasicStructureConfiguration;

/**
 * This interface is used in the capability for the structure configuration for items.
 * @author WuestMan
 *
 */
public interface IStructureConfigurationCapability extends ITransferable<IStructureConfigurationCapability>
{
	BasicStructureConfiguration getConfiguration();
	boolean getDirty();
	void setDirty(boolean value);
	
	IStructureConfigurationCapability setConfiguration(BasicStructureConfiguration configuration);
}
