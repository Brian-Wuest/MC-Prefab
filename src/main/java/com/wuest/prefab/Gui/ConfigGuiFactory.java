package com.wuest.prefab.Gui;

import java.util.Set;

import com.wuest.prefab.Prefab;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.DefaultGuiFactory;
import net.minecraftforge.fml.client.IModGuiFactory;
import net.minecraftforge.fml.client.config.GuiConfig;

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
	public Class<? extends GuiScreen> mainConfigGuiClass() 
	{
		return GuiPrefab.class;
	}
	
    @Override
    public GuiScreen createConfigGui(GuiScreen parentScreen)
    {  
        return new GuiPrefab(parentScreen);
    }
}