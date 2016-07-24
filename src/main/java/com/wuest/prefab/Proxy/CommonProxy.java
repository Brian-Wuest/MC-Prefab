package com.wuest.prefab.Proxy;

import com.wuest.prefab.ModRegistry;
import com.wuest.prefab.Prefab;
import com.wuest.prefab.Config.ModConfiguration;
import com.wuest.prefab.Events.ModEventHandler;
import com.wuest.prefab.Gui.*;
import com.wuest.prefab.Proxy.Messages.*;
import com.wuest.prefab.Proxy.Messages.Handlers.*;

import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;

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
		
		// Register messages.
		ModRegistry.RegisterMessages();
		
		// Register items here.
		ModRegistry.RegisterModComponents();
		
		// Register the recipes here.
		ModRegistry.RegisterRecipes();
	}
	
	public void init(FMLInitializationEvent event)
	{
		NetworkRegistry.INSTANCE.registerGuiHandler(Prefab.instance, Prefab.proxy);
		this.RegisterEventListeners();
	}
	
	public void postinit(FMLPostInitializationEvent event)
	{
	}
	
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		if (ID == GuiHouseItem.GUI_ID)
		{
			return new GuiHouseItem(x, y, z);
		}
		else if (ID == GuiWareHouse.GUI_ID)
		{
			return new GuiWareHouse(x, y, z);
		}
		else if (ID == GuiChickenCoop.GUI_ID)
		{
			return new GuiChickenCoop(x, y, z);
		}
		else if (ID == GuiProduceFarm.GUI_ID)
		{
			return new GuiProduceFarm(x, y, z);
		}
		else if (ID == GuiTreeFarm.GUI_ID)
		{
			return new GuiTreeFarm(x, y, z);
		}

		return null;
	}

	
	protected void RegisterEventListeners()
	{
		// DEBUG
		System.out.println("Registering event listeners");
		MinecraftForge.EVENT_BUS.register(eventHandler);
	}
	
	public ModConfiguration getServerConfiguration()
	{
		return CommonProxy.proxyConfiguration;
	}
}
