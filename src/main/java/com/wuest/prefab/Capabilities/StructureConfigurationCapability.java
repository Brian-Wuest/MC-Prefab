package com.wuest.prefab.Capabilities;

import com.wuest.prefab.Config.Structures.BasicStructureConfiguration;

/**
 * This is the initial class for the structure configuration capability.
 * @author WuestMan
 *
 */
public class StructureConfigurationCapability
		implements IStructureConfigurationCapability
{
	private BasicStructureConfiguration structureConfiguration;
	private boolean dirty;
	
	/**
	 * Initializes a new instance of the StructureConfigurationCapability class.
	 */
	public StructureConfigurationCapability()
	{
		this.structureConfiguration = new BasicStructureConfiguration();
		this.dirty = true;
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
	
	@Override
	public boolean getDirty()
	{
		return this.dirty;
	}
	
	@Override
	public void setDirty(boolean value)
	{
		this.dirty = value;
	}
}