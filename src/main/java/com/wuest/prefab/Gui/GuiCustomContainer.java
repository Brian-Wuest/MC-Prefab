package com.wuest.prefab.Gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

/**
 * This class is used for the BlockDrafter as it needs to open the GUI on the server side and this placeholder class is needed to accomplish that.
 * @author WuestMan
 *
 */
public class GuiCustomContainer extends Container
{
	public GuiCustomContainer()
	{
		super();
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer playerIn)
	{
		// TODO Auto-generated method stub
		return true;
	}

}
