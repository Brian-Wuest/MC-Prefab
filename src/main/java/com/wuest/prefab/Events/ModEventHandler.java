package com.wuest.prefab.Events;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import com.wuest.prefab.BuildingMethods;
import com.wuest.prefab.ModRegistry;
import com.wuest.prefab.Prefab;
import com.wuest.prefab.Capabilities.StructureConfigurationCapability;
import com.wuest.prefab.Capabilities.StructureConfigurationProvider;
import com.wuest.prefab.Config.ModConfiguration;
import com.wuest.prefab.Items.ItemBasicStructure;
import com.wuest.prefab.Proxy.ClientProxy;
import com.wuest.prefab.Proxy.Messages.ConfigSyncMessage;
import com.wuest.prefab.StructureGen.BuildBlock;
import com.wuest.prefab.StructureGen.Structure;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ServerTickEvent;

/**
 * This is the server side event hander.
 * @author WuestMan
 */
public class ModEventHandler
{
	public static final String GIVEN_HOUSEBUILDER_TAG = "givenHousebuilder";

	public static HashMap<EntityPlayer, ArrayList<Structure>> structuresToBuild = new HashMap<EntityPlayer, ArrayList<Structure>>();
	public static ArrayList<BlockPos> RedstoneAffectedBlockPositions = null;
	
	static
	{
		ModEventHandler.RedstoneAffectedBlockPositions = new ArrayList<BlockPos>();
	}
	
	@SubscribeEvent
	public void PlayerJoinedWorld(EntityJoinWorldEvent event)
	{
		if (!event.getWorld().isRemote && event.getEntity() instanceof EntityPlayer) 
		{
			System.out.println("Player joined world, checking to see if the house builder should be provided.");

			EntityPlayer player = (EntityPlayer)event.getEntity();
			NBTTagCompound persistTag = this.getModIsPlayerNewTag(player);

			// Get the opposite of the value, if the bool doesn't exist then we can add the house to the inventory, otherwise the player isn't new and shouldn't get the item.
			boolean shouldGiveHousebuilder = !persistTag.getBoolean(ModEventHandler.GIVEN_HOUSEBUILDER_TAG);

			if (shouldGiveHousebuilder && Prefab.proxy.proxyConfiguration.addHouseItem)
			{
				ItemStack stack = new ItemStack(ModRegistry.StartHouse());
				player.inventory.addItemStackToInventory(stack);
				player.inventoryContainer.detectAndSendChanges();

				// Make sure to set the tag for this player so they don't get the item again.
				persistTag.setBoolean(ModEventHandler.GIVEN_HOUSEBUILDER_TAG, true);
			}
		}
	}
	
	@SubscribeEvent
	public void AttachItemStackCapabilities(AttachCapabilitiesEvent.Item event)
	{
		if (event.getItem() instanceof ItemBasicStructure)
		{
			event.addCapability(new ResourceLocation(Prefab.MODID, "structuresconfiguration"), new StructureConfigurationProvider(new StructureConfigurationCapability()));
		}
	}
	
	@SubscribeEvent
	public void onServerTick(ServerTickEvent event)
	{
		ArrayList<EntityPlayer> playersToRemove = new ArrayList<EntityPlayer>();
		
		for (Entry<EntityPlayer, ArrayList<Structure>> entry : ModEventHandler.structuresToBuild.entrySet())
		{
			ArrayList<Structure> structuresToRemove = new ArrayList<Structure>();
			
			// Build the first 100 blocks of each structure for this player.
			for (Structure structure : entry.getValue())
			{
				for (int i = 0; i < 100; i++)
				{
					// Structure clearing happens before anything else.
					if (structure.clearedBlockPos.size() > 0)
					{
						BlockPos currentPos = structure.clearedBlockPos.get(0);
						structure.clearedBlockPos.remove(0);
						
						if (!structure.world.isAirBlock(currentPos))
						{
							structure.world.setBlockToAir(currentPos);
						}
						
						continue;
					}
					
					BuildBlock currentBlock = null;
					
					if (structure.priorityOneBlocks.size() > 0)
					{
						currentBlock = structure.priorityOneBlocks.get(0);
						structure.priorityOneBlocks.remove(0);
					}
					else if (structure.priorityTwoBlocks.size() > 0)
					{
						currentBlock = structure.priorityTwoBlocks.get(0);
						structure.priorityTwoBlocks.remove(0);
					}
					else if (structure.priorityThreeBlocks.size() > 0)
					{
						currentBlock = structure.priorityThreeBlocks.get(0);
						structure.priorityThreeBlocks.remove(0);
					}
					else
					{
						// There are no more blocks to set.
						structuresToRemove.add(structure);
						break;
					}
					
					IBlockState state = currentBlock.getBlockState();
					
					BuildingMethods.ReplaceBlock(structure.world, currentBlock.getStartingPosition().getRelativePosition(structure.originalPos, structure.configuration.houseFacing), state);
					
					// After placing the initial block, set the sub-block. This needs to happen as the list isn't always in the correct order.
					if (currentBlock.getSubBlock() != null)
					{
						BuildBlock subBlock = currentBlock.getSubBlock();
						
						BuildingMethods.ReplaceBlock(structure.world, subBlock.getStartingPosition().getRelativePosition(structure.originalPos, structure.configuration.houseFacing), subBlock.getBlockState());
					}
				}
			}
			
			for (Structure structure : structuresToRemove)
			{
				// This structure is done building. Do any post-building operations.
				structure.AfterBuilding(structure.configuration, structure.world, structure.originalPos, structure.assumedNorth, entry.getKey());
				entry.getValue().remove(structure);
			}
			
			if (entry.getValue().size() == 0)
			{
				playersToRemove.add(entry.getKey());
			}
		}
		
		// Remove each player that has their structure's built.
		for (EntityPlayer player : playersToRemove)
		{
			ModEventHandler.structuresToBuild.remove(player);
		}
	}
	
	@SubscribeEvent
	public void onPlayerLoginEvent(PlayerLoggedInEvent event)
	{
		if(!event.player.world.isRemote)
		{
			NBTTagCompound tag = Prefab.proxy.proxyConfiguration.ToNBTTagCompound();
			Prefab.network.sendTo(new ConfigSyncMessage(tag), (EntityPlayerMP)event.player);
			System.out.println("Sent config to '" + event.player.getDisplayNameString() + ".'");
		}
	}
	
	@SubscribeEvent
	public void onPlayerLoggedOutEvent(PlayerLoggedOutEvent event)
	{
		// When the player logs out, make sure to re-set the server configuration. 
		// This is so a new configuration can be successfully loaded when they switch servers or worlds (on single player.
		if (event.player.world.isRemote)
		{
			// Make sure to null out the server configuration from the client.
			((ClientProxy)Prefab.proxy).serverConfiguration = null;
		}
	}

	@SubscribeEvent
	public void onClone(PlayerEvent.Clone event) 
	{
		// Don't add the tag unless the house item was added. This way it can be added if the feature is turned on.
		// When the player is cloned, make sure to copy the tag. If this is not done the item can be given to the player again if they die before the log out and log back in.
		NBTTagCompound originalTag = event.getOriginal().getEntityData();

		// Use the server configuration to determine if the house should be added for this player.
		if (Prefab.proxy.proxyConfiguration.addHouseItem)
		{
			if (originalTag.hasKey("IsPlayerNew"))
			{
				NBTTagCompound newPlayerTag = event.getEntityPlayer().getEntityData();
				newPlayerTag.setTag("IsPlayerNew", originalTag.getTag("IsPlayerNew"));
			}
		}
	}

	@SubscribeEvent
	public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent onConfigChangedEvent)
	{
		if(onConfigChangedEvent.getModID().equals(Prefab.MODID))
		{
			ModConfiguration.syncConfig();
		}
	}
	
	private NBTTagCompound getModIsPlayerNewTag(EntityPlayer player)
	{
		NBTTagCompound tag = player.getEntityData();

		// Get/create a tag used to determine if this is a new player.
		NBTTagCompound newPlayerTag = null;

		if (tag.hasKey("IsPlayerNew"))
		{
			newPlayerTag = tag.getCompoundTag("IsPlayerNew");
		}
		else
		{
			newPlayerTag = new NBTTagCompound();
			tag.setTag("IsPlayerNew", newPlayerTag);
		}

		return newPlayerTag;
	}

}