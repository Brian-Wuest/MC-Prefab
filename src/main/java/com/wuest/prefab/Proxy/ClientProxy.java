package com.wuest.prefab.Proxy;

import com.wuest.prefab.*;
import com.wuest.prefab.Blocks.BlockCompressedStone;
import com.wuest.prefab.Config.ModConfiguration;
import com.wuest.prefab.Events.ClientEventHandler;
import com.wuest.prefab.Items.Structures.ItemBasicStructure;
import com.wuest.prefab.Render.*;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.*;

/**
 * 
 * @author WuestMan
 *
 */
public class ClientProxy extends CommonProxy
{
	public ModConfiguration serverConfiguration = null;
	public static ClientEventHandler clientEventHandler = new ClientEventHandler();
	
	@Override
	public void preInit(FMLPreInitializationEvent event)
	{
		super.preInit(event);
		
		this.RegisterEventListeners();

		ModRegistry.AddGuis();
		
		// After all items have been registered and all recipes loaded, register any necessary renderer.
		Prefab.proxy.registerRenderers();
		
		ModRegistry.RegisterItemVariants();
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
		ItemRenderRegister.registerItemRenderer();
		ShaderHelper.Initialize();
	}
	
	@Override
	protected void RegisterEventListeners()
	{
		super.RegisterEventListeners();
		
		System.out.println("Registering client event listeners");

		MinecraftForge.EVENT_BUS.register(clientEventHandler);
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