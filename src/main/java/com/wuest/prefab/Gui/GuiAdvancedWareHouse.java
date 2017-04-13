package com.wuest.prefab.Gui;

/**
 * 
 * @author WuestMan
 *
 */
public class GuiAdvancedWareHouse extends GuiWareHouse
{

	public GuiAdvancedWareHouse(int x, int y, int z)
	{
		super(x, y, z);
	}
	
	@Override
	public void Initialize()
	{
		super.Initialize();
		this.configuration.advanced = true;
	}

}
