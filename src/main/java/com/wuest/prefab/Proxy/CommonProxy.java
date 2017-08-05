package com.wuest.prefab.Proxy;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map.Entry;

import com.google.common.io.Files;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wuest.prefab.ModRegistry;
import com.wuest.prefab.Prefab;
import com.wuest.prefab.UpdateChecker;
import com.wuest.prefab.Blocks.BlockCompressedObsidian;
import com.wuest.prefab.Blocks.BlockCompressedStone;
import com.wuest.prefab.Config.ModConfiguration;
import com.wuest.prefab.Crafting.ShapedRecipeForFile;
import com.wuest.prefab.Events.ModEventHandler;
import com.wuest.prefab.Gui.GuiCustomContainer;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistry;
import net.minecraftforge.oredict.OreDictionary;

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
		
		// Register the capabilities.
		ModRegistry.RegisterCapabilities();
		
		// Make sure that the mod configuration is re-synced after loading all of the recipes.
		ModConfiguration.syncConfig();
	}
	
	public void init(FMLInitializationEvent event)
	{	
		NetworkRegistry.INSTANCE.registerGuiHandler(Prefab.instance, Prefab.proxy);
	}
	
	public void postinit(FMLPostInitializationEvent event)
	{
		if (this.proxyConfiguration.enableVersionCheckMessage)
		{
			UpdateChecker.checkVersion();
		}
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

	public ModConfiguration getServerConfiguration()
	{
		return CommonProxy.proxyConfiguration;
	}
	
	public class CustomExclusionStrategy implements ExclusionStrategy
	{

		private ArrayList<String> allowedNames = new ArrayList<String>(Arrays.asList("group", "recipeWidth", "recipeHeight", "recipeItems", "recipeOutput", "matchingStacks"));

		@Override
		public boolean shouldSkipField(FieldAttributes f)
		{
			if (this.allowedNames.contains(f.getName()))
			{
				return false;
			}
			
			return true;
		}

		@Override
		public boolean shouldSkipClass(Class<?> clazz)
		{
			if (clazz == EntityPlayer.class)
			{
				return true;
			}
			
			return false;
		}
		
	}
}
