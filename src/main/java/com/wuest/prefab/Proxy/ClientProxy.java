package com.wuest.prefab.Proxy;

import com.wuest.prefab.ItemRenderRegister;
import com.wuest.prefab.ModRegistry;
import com.wuest.prefab.Prefab;
import com.wuest.prefab.Blocks.BlockCompressedStone;
import com.wuest.prefab.Config.ModConfiguration;
import com.wuest.prefab.Events.ClientEventHandler;
import com.wuest.prefab.Render.ShaderHelper;
import com.wuest.prefab.Render.StructureRenderHandler;

import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy extends CommonProxy
{
	public ModConfiguration serverConfiguration = null;
	public static ClientEventHandler clientEventHandler = new ClientEventHandler();
	
	@Override
	public void preInit(FMLPreInitializationEvent event)
	{
		super.preInit(event);
		
		ModRegistry.AddGuis();
		
		// After all items have been registered and all recipes loaded, register any necessary renderer.
		Prefab.proxy.registerRenderers();
				
		ModelBakery.registerItemVariants(ModRegistry.CompressedStoneItem(), BlockCompressedStone.EnumType.GetNames());
	}

	@Override
	public void init(FMLInitializationEvent event)
	{
		super.init(event);

		this.RegisterEventListeners();
	}

	@Override
	public void postinit(FMLPostInitializationEvent event)
	{
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
		System.out.println("Registering event listeners");

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