package com.wuest.prefab.Proxy;

import com.wuest.prefab.ModRegistry;
import com.wuest.prefab.Prefab;
import com.wuest.prefab.Config.ModConfiguration;
import com.wuest.prefab.Events.ClientEventHandler;
import com.wuest.prefab.Structures.Events.StructureClientEventHandler;
import com.wuest.prefab.Structures.Render.ShaderHelper;

import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

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
	public void preInit(FMLCommonSetupEvent event)
	{
		super.preInit(event);

		ModRegistry.AddGuis();

		// After all items have been registered and all recipes loaded, register any necessary renderer.
		Prefab.proxy.registerRenderers();
	}

	@Override
	public void init(FMLCommonSetupEvent event)
	{
		super.init(event);
	}

	@Override
	public void postinit(FMLCommonSetupEvent event)
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