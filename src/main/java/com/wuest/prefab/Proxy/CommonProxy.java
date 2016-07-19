package com.wuest.prefab.Proxy;

import com.wuest.prefab.ModRegistry;
import com.wuest.prefab.Prefab;
import com.wuest.prefab.Config.ModConfiguration;
import com.wuest.prefab.Events.ModEventHandler;
import com.wuest.prefab.Gui.GuiHouseItem;
import com.wuest.prefab.Gui.GuiWareHosue;
import com.wuest.prefab.Proxy.Messages.ConfigSyncMessage;
import com.wuest.prefab.Proxy.Messages.HouseTagMessage;
import com.wuest.prefab.Proxy.Messages.WareHouseTagMessage;
import com.wuest.prefab.Proxy.Messages.Handlers.ConfigSyncHandler;
import com.wuest.prefab.Proxy.Messages.Handlers.HouseHandler;
import com.wuest.prefab.Proxy.Messages.Handlers.WareHouseHandler;

import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
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
		
		Prefab.network.registerMessage(HouseHandler.class, HouseTagMessage.class, 1, Side.SERVER);
		Prefab.network.registerMessage(WareHouseHandler.class, WareHouseTagMessage.class, 2, Side.SERVER);
		Prefab.network.registerMessage(ConfigSyncHandler.class, ConfigSyncMessage.class, 3, Side.CLIENT);
		
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
		else if (ID == GuiWareHosue.GUI_ID)
		{
			return new GuiWareHosue(x, y, z);
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
