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
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.RegistryEvent.MissingMappings;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;
import net.minecraftforge.fml.relauncher.Side;

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

		if ((rightItem.getItem() == Item.getItemFromBlock(ModRegistry.CompressedStoneBlock())
			&& rightItem.getMetadata() == BlockCompressedStone.EnumType.TRIPLE_COMPRESSED_STONE.getMetadata())
			|| (leftItem.getItem() == Item.getItemFromBlock(ModRegistry.CompressedStoneBlock())
				&& leftItem.getMetadata() == BlockCompressedStone.EnumType.TRIPLE_COMPRESSED_STONE.getMetadata()))
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

	@SubscribeEvent
	public static void OnMissingBlockMapping(MissingMappings<Block> event)
	{
		for (MissingMappings.Mapping<Block> entry : event.getMappings())
		{
			Block mappedBlock = null;

			switch (entry.key.getResourcePath())
			{
				case "blockcompressedstone":
				case "blockCompressedStone":
				{
					mappedBlock = ModRegistry.CompressedStoneBlock();
				}
			}

			if (mappedBlock != null)
			{
				entry.remap(mappedBlock);
			}
		}
	}

	@SubscribeEvent
	public static void OnMissingMapping(MissingMappings<Item> event)
	{
		ImmutableList missingMappings = event.getMappings();

		for (MissingMappings.Mapping<Item> mapping : event.getMappings())
		{
			Item mappedItem = null;

			switch (mapping.key.getResourcePath())
			{
				case "blockcompressedstone":
				case "blockCompressedStone":
				{
					mappedItem = ModRegistry.ModItems.stream().filter(item -> item.getRegistryName().getResourcePath().equals("block_compressed_stone"))
						.findFirst().get();
					break;
				}

				case "itemproducefarm":
				case "itemProduceFarm":
				{
					mappedItem = ModRegistry.ProduceFarm();
					break;
				}

				case "itempileofbricks":
				case "itemPileOfBricks":
				{
					mappedItem = ModRegistry.PileOfBricks();
					break;
				}

				case "itemhorsestable":
				case "itemHorseStable":
				{
					mappedItem = ModRegistry.HorseStable();
					break;
				}

				case "itemnethergate":
				case "itemNetherGate":
				{
					mappedItem = ModRegistry.NetherGate();
					break;
				}

				case "itemwarehouseupgrade":
				case "itemWareHouseUpgrade":
				{
					mappedItem = ModRegistry.WareHouseUpgrade();
					break;
				}

				case "itemchickencoop":
				case "itemChickenCoop":
				{
					mappedItem = ModRegistry.ChickenCoop();
					break;
				}

				case "itemtreefarm":
				case "itemTreeFarm":
				{
					mappedItem = ModRegistry.TreeFarm();
					break;
				}

				case "itemcompressedchest":
				case "itemCompressedChest":
				{
					mappedItem = ModRegistry.CompressedChestItem();
					break;
				}

				case "itembundleoftimber":
				case "itemBundleOfTimber":
				{
					mappedItem = ModRegistry.BundleOfTimber();
					break;
				}

				case "itemwarehouse":
				case "itemWareHouse":
				{
					mappedItem = ModRegistry.WareHouse();
					break;
				}

				case "itempalletofbricks":
				case "itemPalletOfBricks":
				{
					mappedItem = ModRegistry.PalletOfBricks();
					break;
				}

				case "itemfishpond":
				case "itemFishPond":
				{
					mappedItem = ModRegistry.FishPond();
					break;
				}

				case "itemmonstermasher":
				case "itemMonsterMasher":
				{
					mappedItem = ModRegistry.MonsterMasher();
					break;
				}

				case "itemstarthouse":
				case "itemStartHouse":
				{
					mappedItem = ModRegistry.StartHouse();
					break;
				}

				case "itemadvancedwarehouse":
				case "itemAdvancedWareHouse":
				{
					mappedItem = ModRegistry.AdvancedWareHouse();
					break;
				}
			}

			if (mappedItem != null)
			{
				mapping.remap(mappedItem);
			}
		}
	}
}
