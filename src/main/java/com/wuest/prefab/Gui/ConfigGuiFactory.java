package com.wuest.prefab.Gui;

import java.util.Set;

import com.wuest.prefab.Prefab;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.IModGuiFactory;
import net.minecraftforge.fml.client.config.GuiConfig;

/**
 * 
 * @author WuestMan
 *
 */
public class ConfigGuiFactory implements IModGuiFactory
{
	@Override
	public void initialize(Minecraft minecraftInstance) 
	{
	}
	
	@Override
	public Class<? extends GuiScreen> mainConfigGuiClass() 
	{
		return GuiPrefab.class;
	}
	

	@Override
	public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() 
	{
		return null;
	}

	@Override
	public RuntimeOptionGuiHandler getHandlerFor(RuntimeOptionCategoryElement element) 
	{
		return null;
	}
}
