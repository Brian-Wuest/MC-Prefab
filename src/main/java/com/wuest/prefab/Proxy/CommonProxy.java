package com.wuest.prefab.Proxy;

import com.wuest.prefab.ModRegistry;
import com.wuest.prefab.Prefab;
import com.wuest.prefab.UpdateChecker;
import com.wuest.prefab.Config.ModConfiguration;
import com.wuest.prefab.Events.ModEventHandler;
import com.wuest.prefab.Gui.GuiCustomContainer;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;

/**
 * This is the server side proxy.
 * @author WuestMan
 *
 */
public class CommonProxy implements IGuiHandler
{
	public static ModEventHandler eventHandler = new ModEventHandler();
	public static ModConfiguration proxyConfiguration;

	/*
	 * Methods for ClientProxy to Override
	 */
	public void registerRenderers()
	{
	}
	
	public void preInit(FMLPreInitializationEvent event)
	{
		Prefab.network = NetworkRegistry.INSTANCE.newSimpleChannel("PrefabChannel");
		Prefab.config = new Configuration(event.getSuggestedConfigurationFile());
		Prefab.config.load();
		ModConfiguration.syncConfig();
		
		if (this.proxyConfiguration.enableVersionCheckMessage)
		{
			// Pull the repository information.
			UpdateChecker.checkVersion();
		}
		
		// Register messages.
		ModRegistry.RegisterMessages();
		
		// Register the capabilities.
		ModRegistry.RegisterCapabilities();
		
		// Register items here.
		ModRegistry.RegisterModComponents();
		
		this.RegisterEventListeners();
		
		// Register the recipes here.
		ModRegistry.RegisterRecipes();
	}
	
	public void init(FMLInitializationEvent event)
	{
		NetworkRegistry.INSTANCE.registerGuiHandler(Prefab.instance, Prefab.proxy);
	}
	
	public void postinit(FMLPostInitializationEvent event)
	{
	}
	
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		if (ID == ModRegistry.GuiDrafter)
		{
			return new GuiCustomContainer();
		}
		
		return ModRegistry.GetModGuiByID(ID, x, y, z);
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		return ModRegistry.GetModGuiByID(ID, x, y, z);
	}

	
	protected void RegisterEventListeners()
	{
		// DEBUG
		System.out.println("Registering server event listeners");
		MinecraftForge.EVENT_BUS.register(eventHandler);
	}
	
	public ModConfiguration getServerConfiguration()
	{
		return CommonProxy.proxyConfiguration;
	}
}
