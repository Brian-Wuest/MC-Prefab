package com.wuest.prefab.Gui;

import com.wuest.prefab.Prefab;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.DefaultGuiFactory;

/**
 * 
 * @author WuestMan
 *
 */
public class ConfigGuiFactory extends DefaultGuiFactory
{
	public ConfigGuiFactory()
	{
		super("", "");

		this.modid = Prefab.MODID;
		this.title = "Prefab";
	}

	@Override
	public GuiScreen createConfigGui(GuiScreen parentScreen)
	{
		return new GuiPrefab(parentScreen);
	}
}