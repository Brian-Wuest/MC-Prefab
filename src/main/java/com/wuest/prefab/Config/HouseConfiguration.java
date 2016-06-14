package com.wuest.prefab.Config;

import com.wuest.prefab.Config.ModConfiguration.CeilingFloorBlockType;
import com.wuest.prefab.Config.ModConfiguration.WallBlockType;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;

/**
 * This class is used to determine the configuration for a particular house.
 * @author WuestMan
 *
 */
public class HouseConfiguration 
{
	public static String tagKey = "WuestHouseConfig";

	public static String addTorchesName = "Add Torches";
	public static String addBedName = "Add Bed";
	public static String addCraftingTableName = "Add Crafting Table";
	public static String addChestName = "Add Chest";
	public static String addChestContentsName = "Add Chest Contents";
	public static String addFarmName = "Add Farm";
	public static String floorBlockName = "Floor Stone Type";
	public static String ceilingBlockName = "Ceiling Stone Type";
	public static String wallWoodTypeName = "Wall Wood Type";
	public static String isCeilingFlatName = "Is Ceiling Flat";
	public static String addMineShaftName = "Build Mineshaft";
	public static String houseWidthName = "House Width";
	public static String houseDepthName = "House Depth";
	public static String houseFacingName = "House Facing";
	
	private static String addTorchesTag = "addTorches";
	private static String addBedTag = "addBed";
	private static String addCraftingTableTag = "addCraftingTable";
	private static String addChestTag = "addChest";
	private static String addChestContentsTag = "addChestContents";
	private static String addFarmTag = "addFarm";
	private static String floorBlockTag = "floorBlock";
	private static String ceilingBlockTag = "ceilingBlock";
	private static String wallWoodTypeTag = "wallWoodType";
	private static String isCeilingFlatTag = "isCeilingFlat";
	private static String addMineShaftTag = "addMineShaft";
	private static String hitXTag = "hitX";
	private static String hitYTag = "hitY";
	private static String hitZTag = "hitZ";
	private static String houseWidthTag = "houseWidth";
	private static String houseDepthTag = "houseDepth";
	private static String houseFacingTag = "houseFacing";

	public boolean addTorches;
	public boolean addBed;
	public boolean addCraftingTable;
	public boolean addChest;
	public boolean addChestContents;
	public boolean addFarm;
	public CeilingFloorBlockType floorBlock;
	public CeilingFloorBlockType ceilingBlock;
	public WallBlockType wallWoodType;
	public boolean isCeilingFlat;
	public boolean addMineShaft;
	public int hitX;
	public int hitY;
	public int hitZ;
	public EnumFacing houseFacing; 
	
	/**
	 * When the house is facing north, this would be the east/west direction.
	 */
	public int houseWidth;
	
	/**
	 * When the house if facing north, this would be the north/south direction.
	 */
	public int houseDepth;

	public NBTTagCompound WriteToNBTTagCompound()
	{
		NBTTagCompound tag = new NBTTagCompound();

		// This tag should only be written for options which will NOT be overwritten by server options.
		// Server configuration settings will be used for all other options.
		// This is so the server admin can force a player to not use something.
		tag.setBoolean(HouseConfiguration.addTorchesTag, this.addTorches);
		tag.setBoolean(HouseConfiguration.addBedTag, this.addBed);
		tag.setBoolean(HouseConfiguration.addCraftingTableTag, this.addCraftingTable);
		tag.setBoolean(HouseConfiguration.addChestTag, this.addChest);
		tag.setBoolean(HouseConfiguration.addChestContentsTag, this.addChestContents);
		tag.setBoolean(HouseConfiguration.addFarmTag, this.addFarm);
		tag.setInteger(HouseConfiguration.floorBlockTag, this.floorBlock.getValue());
		tag.setInteger(HouseConfiguration.ceilingBlockTag, this.ceilingBlock.getValue());
		tag.setInteger(HouseConfiguration.wallWoodTypeTag, this.wallWoodType.getValue());
		tag.setBoolean(HouseConfiguration.isCeilingFlatTag, this.isCeilingFlat);
		tag.setBoolean(HouseConfiguration.addMineShaftTag, this.addMineShaft);
		tag.setInteger(HouseConfiguration.hitXTag, this.hitX);
		tag.setInteger(HouseConfiguration.hitYTag, this.hitY);
		tag.setInteger(HouseConfiguration.hitZTag, this.hitZ);
		tag.setInteger(HouseConfiguration.houseDepthTag, this.houseDepth);
		tag.setInteger(HouseConfiguration.houseWidthTag, this.houseWidth);
		tag.setString(HouseConfiguration.houseFacingTag, this.houseFacing.getName());
		
		return tag;
	}

	public static String GetIntegerOptionStringValue(String name, int value)
	{
		if (name.equals(HouseConfiguration.ceilingBlockName)
				|| name.equals(HouseConfiguration.floorBlockName))
		{
			return " - " + CeilingFloorBlockType.ValueOf(value).name();
		}
		else if (name.equals(HouseConfiguration.wallWoodTypeName))
		{
			return " - " + WallBlockType.ValueOf(value).name();
		}

		return "";
	}

	public static HouseConfiguration ReadFromNBTTagCompound(NBTTagCompound tag)
	{
		HouseConfiguration config = null;

		if (tag != null)
		{
			config = new HouseConfiguration();

			if (tag.hasKey(HouseConfiguration.addTorchesTag))
			{
				config.addTorches = tag.getBoolean(HouseConfiguration.addTorchesTag);
			}

			if (tag.hasKey(HouseConfiguration.addBedTag))
			{
				config.addBed = tag.getBoolean(HouseConfiguration.addBedTag);
			}

			if (tag.hasKey(HouseConfiguration.addCraftingTableTag))
			{
				config.addCraftingTable = tag.getBoolean(HouseConfiguration.addCraftingTableTag);
			}

			if (tag.hasKey(HouseConfiguration.addChestTag))
			{
				config.addChest = tag.getBoolean(HouseConfiguration.addChestTag);
			}

			if (tag.hasKey(HouseConfiguration.addChestContentsTag))
			{
				config.addChestContents = tag.getBoolean(HouseConfiguration.addChestContentsTag);
			}

			if (tag.hasKey(HouseConfiguration.addFarmTag))
			{
				config.addFarm = tag.getBoolean(HouseConfiguration.addFarmTag);
			}

			if (tag.hasKey(HouseConfiguration.floorBlockTag))
			{
				config.floorBlock = CeilingFloorBlockType.ValueOf(tag.getInteger(HouseConfiguration.floorBlockTag));
			}

			if (tag.hasKey(HouseConfiguration.ceilingBlockTag))
			{
				config.ceilingBlock = CeilingFloorBlockType.ValueOf(tag.getInteger(HouseConfiguration.ceilingBlockTag));
			}

			if (tag.hasKey(HouseConfiguration.wallWoodTypeTag))
			{
				config.wallWoodType = WallBlockType.ValueOf(tag.getInteger(HouseConfiguration.wallWoodTypeTag));
			}

			if (tag.hasKey(HouseConfiguration.isCeilingFlatTag))
			{
				config.isCeilingFlat = tag.getBoolean(HouseConfiguration.isCeilingFlatTag);
			}

			if (tag.hasKey(HouseConfiguration.addMineShaftTag))
			{
				config.addMineShaft = tag.getBoolean(HouseConfiguration.addMineShaftTag);
			}

			if (tag.hasKey(HouseConfiguration.hitXTag))
			{
				config.hitX = tag.getInteger(HouseConfiguration.hitXTag);
			}

			if (tag.hasKey(HouseConfiguration.hitYTag))
			{
				config.hitY = tag.getInteger(HouseConfiguration.hitYTag);
			}

			if (tag.hasKey(HouseConfiguration.hitZTag))
			{
				config.hitZ = tag.getInteger(HouseConfiguration.hitZTag);
			}
			
			if (tag.hasKey(HouseConfiguration.houseDepthTag))
			{
				config.houseDepth = tag.getInteger(HouseConfiguration.houseDepthTag);
			}
			
			if (tag.hasKey(HouseConfiguration.houseWidthTag))
			{
				config.houseWidth = tag.getInteger(HouseConfiguration.houseWidthTag);
			}
			
			if (tag.hasKey(HouseConfiguration.houseFacingTag))
			{
				config.houseFacing = EnumFacing.byName(tag.getString(HouseConfiguration.houseFacingTag));
			}
		}

		return config;
	}

}
