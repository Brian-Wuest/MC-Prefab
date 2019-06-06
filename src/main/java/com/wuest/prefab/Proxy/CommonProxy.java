package com.wuest.prefab.Proxy;

import java.util.ArrayList;
import java.util.Arrays;

import org.apache.commons.lang3.tuple.Pair;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.wuest.prefab.ModRegistry;
import com.wuest.prefab.Prefab;
import com.wuest.prefab.Config.ModConfiguration;
import com.wuest.prefab.Events.ModEventHandler;
import com.wuest.prefab.Gui.GuiCustomContainer;
import com.wuest.prefab.Structures.Events.StructureEventHandler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.network.FMLPlayMessages;
import net.minecraftforge.fml.network.NetworkRegistry;

/**
 * This is the server side proxy.
 * 
 * @author WuestMan
 *
 */
public class CommonProxy
{
	public static ModEventHandler eventHandler = new ModEventHandler();
	public static StructureEventHandler structureEventHandler = new StructureEventHandler();
	public static ModConfiguration proxyConfiguration;
	public static ForgeConfigSpec COMMON_SPEC;
	
	/*
	 * Methods for ClientProxy to Override
	 */
	public void registerRenderers()
	{
	}

	public void preInit(FMLCommonSetupEvent event)
	{
		ModRegistry.RegisterModComponents();
		
		Prefab.network = NetworkRegistry.ChannelBuilder.named(new ResourceLocation(Prefab.MODID, "main_channel"))
            .clientAcceptedVersions(Prefab.PROTOCOL_VERSION::equals)
            .serverAcceptedVersions(Prefab.PROTOCOL_VERSION::equals)
            .networkProtocolVersion(() -> Prefab.PROTOCOL_VERSION)
            .simpleChannel();
		
        Pair<ModConfiguration, ForgeConfigSpec> commonPair = new ForgeConfigSpec.Builder().configure(ModConfiguration::new);
        COMMON_SPEC = commonPair.getRight();
        proxyConfiguration = commonPair.getLeft();
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, COMMON_SPEC);

		// Register messages.
		ModRegistry.RegisterMessages();

		// Register the capabilities.
		ModRegistry.RegisterCapabilities();
	}

	public void init(FMLCommonSetupEvent event)
	{
		ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.GUIFACTORY, () -> CommonProxy::openGui);
	}

	public void postinit(FMLCommonSetupEvent event)
	{
	}
	
    public static GuiScreen openGui(FMLPlayMessages.OpenContainer openContainer)
    {
        BlockPos pos = openContainer.getAdditionalData().readBlockPos();
        ModRegistry.GetModGuiByID(openContainer.getId().getPath(), pos.getX(), pos.getY(), pos.getZ());

        return null;
    }

	public ModConfiguration getServerConfiguration()
	{
		return CommonProxy.proxyConfiguration;
	}

	public class CustomExclusionStrategy implements ExclusionStrategy
	{
		private ArrayList<String> allowedNames = new ArrayList<String>(
			Arrays.asList("group", "recipeWidth", "recipeHeight", "recipeItems", "recipeOutput", "matchingStacks"));

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
