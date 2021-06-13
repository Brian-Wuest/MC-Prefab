package com.wuest.prefab.Events;

import com.wuest.prefab.Config.ModConfiguration;
import com.wuest.prefab.ModRegistry;
import com.wuest.prefab.Prefab;
import com.wuest.prefab.Proxy.ClientProxy;
import com.wuest.prefab.Proxy.Messages.ConfigSyncMessage;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.util.ArrayList;

/**
 * This is the server side event hander.
 * 
 * @author WuestMan
 */
@EventBusSubscriber(value =
{ Side.SERVER, Side.CLIENT })
public class ModEventHandler
{
	static
	{
		ModEventHandler.RedstoneAffectedBlockPositions = new ArrayList<BlockPos>();
	}

	/**
	 * Determines the affected blocks by redstone power.
	 */
	public static ArrayList<BlockPos> RedstoneAffectedBlockPositions = new ArrayList<BlockPos>();

	/**
	 * This event occurs when a player logs in. This is used to send server configuration to the client.
	 * 
	 * @param event The event object.
	 */
	@SubscribeEvent
	public static void onPlayerLoginEvent(PlayerLoggedInEvent event)
	{
		if (!event.player.world.isRemote)
		{
			NBTTagCompound tag = Prefab.proxy.proxyConfiguration.ToNBTTagCompound();
			Prefab.network.sendTo(new ConfigSyncMessage(tag), (EntityPlayerMP) event.player);
			System.out.println("Sent config to '" + event.player.getDisplayNameString() + "'.");
		}
	}

	/**
	 * This event is used to clear out the server configuration for clients that log off the server.
	 * 
	 * @param event The event object.
	 */
	@SubscribeEvent
	public static void onPlayerLoggedOutEvent(PlayerLoggedOutEvent event)
	{
		// When the player logs out, make sure to re-set the server configuration.
		// This is so a new configuration can be successfully loaded when they switch servers or worlds (on single
		// player.
		if (event.player.world.isRemote)
		{
			// Make sure to null out the server configuration from the client.
			((ClientProxy) Prefab.proxy).serverConfiguration = null;
		}
	}

	/**
	 * This is used to sync up the configuration when it's change by the user.
	 * 
	 * @param onConfigChangedEvent The event object.
	 */
	@SubscribeEvent
	public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent onConfigChangedEvent)
	{
		if (onConfigChangedEvent.getModID().equals(Prefab.MODID))
		{
			ModConfiguration.syncConfig();
		}
	}

	@SubscribeEvent
	public static void AnvilUpdate(AnvilUpdateEvent event)
	{
		ItemStack rightItem = event.getRight();
		ItemStack leftItem = event.getLeft();

		if (rightItem.getItem() == ModRegistry.TripleCompressedStoneItem
			|| leftItem.getItem() == ModRegistry.TripleCompressedStoneItem)
		{
			if (rightItem.getItem() == ModRegistry.Bulldozer || leftItem.getItem() == ModRegistry.Bulldozer)
			{
				event.setCost(4);
				ItemStack bulldozer = rightItem.getItem() == ModRegistry.Bulldozer ? rightItem : leftItem;

				ItemStack outputStack = new ItemStack(ModRegistry.Bulldozer);
				ModRegistry.Bulldozer.setPoweredValue(outputStack, true);
				outputStack.setItemDamage(0);
				event.setOutput(outputStack);
			}
		}
	}

	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event)
	{
		event.getRegistry().registerAll(ModRegistry.ModBlocks.toArray(new Block[ModRegistry.ModBlocks.size()]));
	}

	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event)
	{
		event.getRegistry().registerAll(ModRegistry.ModItems.toArray(new Item[ModRegistry.ModItems.size()]));
	}

	@SubscribeEvent
	public static void registerRecipes(RegistryEvent.Register<IRecipe> event)
	{
		// Register the ore dictionary blocks.
		ModRegistry.RegisterOreDictionaryRecords();
	}
}
