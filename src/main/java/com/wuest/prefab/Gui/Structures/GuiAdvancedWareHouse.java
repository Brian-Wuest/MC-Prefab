package com.wuest.prefab.Gui.Structures;

import com.wuest.prefab.Proxy.Messages.StructureTagMessage.EnumStructureConfiguration;

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
		this.structureConfiguration = EnumStructureConfiguration.AdvancedWareHouse;
	}

}
