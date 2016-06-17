package com.wuest.prefab.Proxy;

import com.wuest.prefab.ItemRenderRegister;
import com.wuest.prefab.ModRegistry;
import com.wuest.prefab.Prefab;
import com.wuest.prefab.Blocks.BlockCompressedStone;

import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy extends CommonProxy
{
	@Override
	public void preInit(FMLPreInitializationEvent event)
	{
		super.preInit(event);
	}

	@Override
	public void init(FMLInitializationEvent event)
	{
		super.init(event);

		// After all items have been registered and all recipes loaded, register any necessary renderer.
		Prefab.proxy.registerRenderers();
		
		ModelBakery.registerItemVariants(ModRegistry.CompressedStoneItem(), BlockCompressedStone.EnumType.GetNames());
		
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
	}
	
	private void RegisterEventListeners()
	{
	}
}