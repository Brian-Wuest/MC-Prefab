package com.wuest.prefab;

import com.wuest.prefab.Proxy.CommonProxy;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;

/**
 * The starting point to load all of the blocks, items and other objects associated with this mod. 
 * @author WuestMan
 *
 */
@Mod(modid = Prefab.MODID, version = Prefab.VERSION, acceptedMinecraftVersions="[1.10.2]", guiFactory = "com.wuest.prefab.Gui.ConfigGuiFactory")
public class Prefab
{
    public static final String MODID = "prefab";
    public static final String VERSION = "1.1.0.3";
    
    @Instance(value = Prefab.MODID)
	public static Prefab instance;
    
	// Says where the client and server 'proxy' code is loaded.
	@SidedProxy(clientSide = "com.wuest.prefab.Proxy.ClientProxy", serverSide = "com.wuest.prefab.Proxy.CommonProxy")
	public static CommonProxy proxy;

	public static SimpleNetworkWrapper network;
	public static Configuration config;
    
	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		Prefab.proxy.preInit(event);
	}

	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		Prefab.proxy.init(event);
	}

	@EventHandler
	public void postinit(FMLPostInitializationEvent event)
	{
		Prefab.proxy.postinit(event);
	}
}
