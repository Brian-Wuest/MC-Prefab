package com.wuest.prefab.Events;

import java.util.ArrayList;

import com.google.common.collect.ImmutableList;
import com.wuest.prefab.ModRegistry;
import com.wuest.prefab.Prefab;
import com.wuest.prefab.Blocks.BlockCompressedStone;
import com.wuest.prefab.Config.ModConfiguration;
import com.wuest.prefab.Proxy.ClientProxy;
import com.wuest.prefab.Proxy.Messages.ConfigSyncMessage;
import com.wuest.prefab.Structures.Capabilities.StructureConfigurationCapability;
import com.wuest.prefab.Structures.Capabilities.StructureConfigurationProvider;
import com.wuest.prefab.Structures.Items.ItemBasicStructure;

import net.minecraft.block.Block;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.RegistryEvent.MissingMappings;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;
import net.minecraftforge.fml.network.NetworkDirection;

/**
 * This is the server side event hander.
 * 
 * @author WuestMan
 */
@Mod.EventBusSubscriber({ Dist.DEDICATED_SERVER })
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
	 * Attaches the structure configuration capability to itemstacks.
	 * 
	 * @param event The event object.
	 */
	@SubscribeEvent
	public static void AttachItemStackCapabilities(AttachCapabilitiesEvent<ItemStack> event)
	{
		if (event.getObject().getItem() instanceof ItemBasicStructure)
		{
			event.addCapability(new ResourceLocation(Prefab.MODID, "structuresconfiguration"),
				new StructureConfigurationProvider(new StructureConfigurationCapability()));
		}
	}

	/**
	 * This event occurs when a player logs in. This is used to send server configuration to the client.
	 * 
	 * @param event The event object.
	 */
	@SubscribeEvent
	public static void onPlayerLoginEvent(PlayerLoggedInEvent event)
	{
		if (!event.getPlayer().world.isRemote)
		{
			CompoundNBT tag = Prefab.proxy.proxyConfiguration.serverConfiguration.ToNBTTagCompound();
			Prefab.network.sendTo(new ConfigSyncMessage(tag), ((ServerPlayerEntity) event.getPlayer()).connection.netManager, NetworkDirection.PLAY_TO_CLIENT);
			System.out.println("Sent config to '" + event.getPlayer().getDisplayName().getString() + "'.");
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
		Item tripleCompressedStone = ModRegistry.GetCompressedStoneType(BlockCompressedStone.EnumType.TRIPLE_COMPRESSED_STONE).getItem();

		if (rightItem.getItem() == tripleCompressedStone || leftItem.getItem() == tripleCompressedStone)
		{
			if (rightItem.getItem() == ModRegistry.Bulldozer() || leftItem.getItem() == ModRegistry.Bulldozer())
			{
				event.setCost(4);
				ItemStack bulldozer = rightItem.getItem() == ModRegistry.Bulldozer() ? rightItem : leftItem;

				ItemStack outputStack = new ItemStack(ModRegistry.Bulldozer());
				ModRegistry.Bulldozer().setPoweredValue(outputStack, true);
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
