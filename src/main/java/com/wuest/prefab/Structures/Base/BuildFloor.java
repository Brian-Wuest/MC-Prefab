package com.wuest.prefab.Structures.Base;

/**
 * 
 * @author WuestMan
 *
 */
@Deprecated
public class BuildFloor extends BuildClear
{
	private String blockDomain;
	private String blockName;

	public BuildFloor()
	{
		this.Initialize();
	}
	
	public String getBlockDomain()
	{
		return this.blockDomain;
	}
	
	public void setBlockDomain(String value)
	{
		this.blockDomain = value;
	}
	
	public String getBlockName()
	{
		return this.blockName;
	}
	
	public void setBlockName(String value)
	{
		this.blockName = value;
	}
	
	@Override
	public void Initialize()
	{
		super.Initialize();
		
		this.blockDomain = "";
		this.blockName = "";
	}
}