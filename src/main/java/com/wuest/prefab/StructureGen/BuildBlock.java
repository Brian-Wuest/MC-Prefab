package com.wuest.prefab.StructureGen;

import java.util.ArrayList;

/**
 * This class defines a single block and where it will be in the structure.
 * @author WuestMan
 */
public class BuildBlock extends BuildFloor
{
	private String tileEntityDomain;
	private String tileEntityName;
	private ArrayList<BuildProperty> properties;
	
	public BuildBlock()
	{
		this.Initialize();
	}
	
	public String getTileEntityDomain()
	{
		return this.tileEntityDomain;
	}
	
	public void setTileEntityDomain(String value)
	{
		this.tileEntityDomain = value;
	}
	
	public String getTileEntityName()
	{
		return this.tileEntityName;
	}
	
	public void setTileEntityName(String value)
	{
		this.tileEntityName = value;
	}
	
	public ArrayList<BuildProperty> getProperties()
	{
		return this.properties;
	}
	
	public void setProperties(ArrayList<BuildProperty> value)
	{
		this.properties = value;
	}
	
	public void Initialize()
	{
		super.Initialize();
		this.tileEntityDomain = "";
		this.tileEntityName = "";
		this.properties = new ArrayList<BuildProperty>();
	}
}