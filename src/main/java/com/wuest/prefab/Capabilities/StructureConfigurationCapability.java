package com.wuest.prefab.Capabilities;

import com.wuest.prefab.Config.BasicStructureConfiguration;

/**
 * This is the initial class for the structure configuration capability.
 * @author WuestMan
 *
 */
public class StructureConfigurationCapability
		implements IStructureConfigurationCapability
{
	private BasicStructureConfiguration structureConfiguration;
	
	
	/**
	 * Initializes a new instance of the StructureConfigurationCapability class.
	 */
	public StructureConfigurationCapability()
	{
		this.structureConfiguration = new BasicStructureConfiguration();
	}
	
	@Override
	public void Transfer(IStructureConfigurationCapability transferable)
	{
		this.setConfiguration(transferable.getConfiguration());
	}

	@Override
	public BasicStructureConfiguration getConfiguration()
	{
		return this.structureConfiguration;
	}

	@Override
	public IStructureConfigurationCapability setConfiguration(
			BasicStructureConfiguration configuration)
	{
		this.structureConfiguration = configuration;
		return this;
	}
}