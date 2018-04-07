package com.wuest.prefab.Config;

import java.util.HashMap;

import com.wuest.prefab.Structures.Config.StructureConfiguration;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

/**
 * @author WuestMan This class serves as configuration data for the current player. It is expected that this lives on
 *         the client-side but saved on server side.
 */
public class EntityPlayerConfiguration
{
	private static final String GIVEN_HOUSEBUILDER_TAG = "givenHousebuilder";
	private static final String Built_Starter_house_Tag = "builtStarterHouse";

	public static final String PLAYER_ENTITY_TAG = "IsPlayerNew";

	public boolean givenHouseBuilder = false;
	public boolean builtStarterHouse = false;
	public HashMap<String, StructureConfiguration> clientConfigurations = new HashMap<String, StructureConfiguration>();

	public EntityPlayerConfiguration()
	{
	}

	/**
	 * Creates an EntityPlayerConfiguration from an entity player object.
	 * 
	 * @param player The player to create the instance from.
	 * @return A new instance of EntityPlayerConfiguration.
	 */
	public static EntityPlayerConfiguration loadFromEntityData(EntityPlayer player)
	{
		EntityPlayerConfiguration config = new EntityPlayerConfiguration();
		NBTTagCompound compoundTag = config.getModIsPlayerNewTag(player);

		config.loadFromNBTTagCompound(compoundTag);

		return config;
	}

	/**
	 * Loads specific properties from saved NBTTag data.
	 * 
	 * @param tag The tag to load the data from.
	 */
	public void loadFromNBTTagCompound(NBTTagCompound tag)
	{
		this.givenHouseBuilder = tag.getBoolean(EntityPlayerConfiguration.GIVEN_HOUSEBUILDER_TAG);
		this.builtStarterHouse = tag.getBoolean(EntityPlayerConfiguration.Built_Starter_house_Tag);
	}

	/**
	 * Gets and possibly creates the player tag used by this mod.
	 * 
	 * @param player The player to get the tag for.
	 * @return An NBTTagCompound to save data too.
	 */
	public NBTTagCompound getModIsPlayerNewTag(EntityPlayer player)
	{
		NBTTagCompound tag = player.getEntityData();

		// Get/create a tag used to determine if this is a new player.
		NBTTagCompound newPlayerTag = null;

		if (tag.hasKey(EntityPlayerConfiguration.PLAYER_ENTITY_TAG))
		{
			newPlayerTag = tag.getCompoundTag(EntityPlayerConfiguration.PLAYER_ENTITY_TAG);
		}
		else
		{
			newPlayerTag = new NBTTagCompound();
			tag.setTag(EntityPlayerConfiguration.PLAYER_ENTITY_TAG, newPlayerTag);
		}

		return newPlayerTag;
	}

	/**
	 * Saves this instance's data to the player tag.
	 * 
	 * @param player The player to save the data too.
	 */
	public void saveToPlayer(EntityPlayer player)
	{
		NBTTagCompound compoundTag = this.getModIsPlayerNewTag(player);

		compoundTag.setBoolean(EntityPlayerConfiguration.Built_Starter_house_Tag, this.builtStarterHouse);
		compoundTag.setBoolean(EntityPlayerConfiguration.GIVEN_HOUSEBUILDER_TAG, this.givenHouseBuilder);
	}

	/**
	 * Gets the client config for this gui screen.
	 * 
	 * @param guiName The name of the gui screen class.
	 * @param structureConfiguration The structure configuration class.
	 * @return A default instance of the structure configuration or the existing one found.
	 */
	public <T extends StructureConfiguration> T getClientConfig(String guiName, Class<T> structureConfiguration)
	{
		T config = (T) this.clientConfigurations.get(guiName);

		if (config == null)
		{
			try
			{
				config = structureConfiguration.newInstance();
				this.clientConfigurations.put(guiName, config);
			}
			catch (InstantiationException | IllegalAccessException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return config;
	}

	/**
	 * This is for clearing out non-persisted objects so when a player changes worlds that the client-side config is
	 * cleared.
	 */
	public void clearNonPersistedObjects()
	{
		this.clientConfigurations.clear();
	}
}