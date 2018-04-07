package com.wuest.prefab.Structures.Gui;

import com.wuest.prefab.Structures.Messages.StructureTagMessage.EnumStructureConfiguration;

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
		this.clientGUIIdentifier = "Advanced Warehouse";
	}
	
	@Override
	public void Initialize()
	{
		super.Initialize();
		this.configuration.advanced = true;
		this.structureConfiguration = EnumStructureConfiguration.AdvancedWareHouse;
	}

}
