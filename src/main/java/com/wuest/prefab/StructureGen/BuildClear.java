package com.wuest.prefab.StructureGen;

/**
 * Defines a shape which should have all of it's blocks replaced with air.
 * @author WuestMan
 *
 */
public class BuildClear
{
	private BuildShape shape;
	private PositionOffset startingPosition;
	
	public BuildClear()
	{
		this.Initialize();
	}
	
	public BuildShape getShape()
	{
		return this.shape;
	}
	
	public void setShape(BuildShape value)
	{
		this.shape = value;
	}
	
	public PositionOffset getStartingPosition()
	{
		return this.startingPosition;
	}
	
	public void setStartingPosition(PositionOffset value)
	{
		this.startingPosition = value;
	}
	
	public void Initialize()
	{
		this.shape = new BuildShape();
		this.startingPosition = new PositionOffset();
	}
}
