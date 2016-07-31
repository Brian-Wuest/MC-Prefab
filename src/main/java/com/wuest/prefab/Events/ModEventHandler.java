package com.wuest.prefab.Events;

import com.wuest.prefab.ModRegistry;
import com.wuest.prefab.Prefab;
import com.wuest.prefab.UpdateChecker;
import com.wuest.prefab.Config.ModConfiguration;
import com.wuest.prefab.Proxy.ClientProxy;
import com.wuest.prefab.Proxy.Messages.ConfigSyncMessage;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;

/**
 * This is the server side event hander.
 * @author WuestMan
 */
public class ModEventHandler
{
	public static final String GIVEN_HOUSEBUILDER_TAG = "givenHousebuilder";

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
		else if (event.getWorld().isRemote && event.getEntity() instanceof EntityPlayer)
		{
			// Show a message to this player if their version is old.
			if (UpdateChecker.showMessage)
			{
				((EntityPlayer)event.getEntity()).addChatMessage(new TextComponentString(UpdateChecker.messageToShow));
			}
		}
	}
	
	@SubscribeEvent
	public void onPlayerLoginEvent(PlayerLoggedInEvent event)
	{
		if(!event.player.worldObj.isRemote)
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
		if (event.player.worldObj.isRemote)
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