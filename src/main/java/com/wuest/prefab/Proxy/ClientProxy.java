package com.wuest.prefab.Proxy;

import com.wuest.prefab.ClientModRegistry;
import com.wuest.prefab.ModRegistry;
import com.wuest.prefab.Prefab;
import com.wuest.prefab.Config.ModConfiguration;
import com.wuest.prefab.Events.ClientEventHandler;
import com.wuest.prefab.Structures.Events.StructureClientEventHandler;
import com.wuest.prefab.Structures.Render.ShaderHelper;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

/**
 * 
 * @author WuestMan
 *
 */
public class ClientProxy extends CommonProxy
{
	public ModConfiguration serverConfiguration = null;
	public static ClientEventHandler clientEventHandler = new ClientEventHandler();
	public static StructureClientEventHandler structureClientEventHandler = new StructureClientEventHandler();

	@Override
	public void preInit(FMLPreInitializationEvent event)
	{
		super.preInit(event);

		ModRegistry.AddGuis();

		// After all items have been registered and all recipes loaded, register any necessary renderer.
		Prefab.proxy.registerRenderers();
	}

	@Override
	public void init(FMLInitializationEvent event)
	{
		super.init(event);

		ClientModRegistry.RegisterModelMeshers();
	}

	@Override
	public void postinit(FMLPostInitializationEvent event)
	{
		super.postinit(event);
	}

	@Override
	public void registerRenderers()
	{
		ShaderHelper.Initialize();
	}

	@Override
	public ModConfiguration getServerConfiguration()
	{
		if (this.serverConfiguration == null)
		{
			// Get the server configuration.
			return CommonProxy.proxyConfiguration;
		}
		else
		{
			return this.serverConfiguration;
		}
	}
}