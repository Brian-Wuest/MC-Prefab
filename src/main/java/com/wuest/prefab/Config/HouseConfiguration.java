package com.wuest.prefab.Config;

import com.wuest.prefab.Config.ModConfiguration.CeilingFloorBlockType;
import com.wuest.prefab.Config.ModConfiguration.WallBlockType;

import net.minecraft.item.EnumDyeColor;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;

/**
 * This class is used to determine the configuration for a particular house.
 * @author WuestMan
 *
 */
public class HouseConfiguration extends StructureConfiguration
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
	private static String houseStyleTag = "houseStyle";
	private static String glassColorTag = "glassColor";

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
	public HouseStyle houseStyle;
	public EnumDyeColor glassColor;
	
	/**
	 * When the house is facing north, this would be the east/west direction.
	 */
	public int houseWidth;
	
	/**
	 * When the house if facing north, this would be the north/south direction.
	 */
	public int houseDepth;
	
	public HouseConfiguration()
	{
		super();
	}
	
	@Override
	public void Initialize()
	{
		super.Initialize();
	}

	@Override
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
		tag.setInteger(HouseConfiguration.hitXTag, this.pos.getX());
		tag.setInteger(HouseConfiguration.hitYTag, this.pos.getY());
		tag.setInteger(HouseConfiguration.hitZTag, this.pos.getZ());
		tag.setInteger(HouseConfiguration.houseDepthTag, this.houseDepth);
		tag.setInteger(HouseConfiguration.houseWidthTag, this.houseWidth);
		tag.setString(HouseConfiguration.houseFacingTag, this.houseFacing.getName());
		tag.setInteger(HouseConfiguration.houseStyleTag, this.houseStyle.value);
		tag.setString(HouseConfiguration.glassColorTag, this.glassColor.getName().toUpperCase());
		
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

	public HouseConfiguration ReadFromNBTTagCompound(NBTTagCompound tag)
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
				config.pos = new BlockPos(tag.getInteger(HouseConfiguration.hitXTag), tag.getInteger(HouseConfiguration.hitYTag), tag.getInteger(HouseConfiguration.hitZTag)); 
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
			
			if (tag.hasKey(HouseConfiguration.houseStyleTag))
			{
				config.houseStyle = HouseStyle.ValueOf(tag.getInteger(HouseConfiguration.houseStyleTag));
			}
			
			if (tag.hasKey(HouseConfiguration.glassColorTag))
			{
				config.glassColor = EnumDyeColor.valueOf(tag.getString(HouseConfiguration.glassColorTag));
			}
		}

		return config;
	}

	public enum HouseStyle
	{
		BASIC(0, "Basic House", new ResourceLocation("prefab", "textures/gui/basicHouse.png"), "A highly configurable house but has a very basic design.", 153, 148),
		RANCH(1, "Ranch Style", new ResourceLocation("prefab", "textures/gui/ranchHouse.png"), "A less configurable house designed in a ranch style.", 152, 89),
		LOFT(2, "Loft Style", new ResourceLocation("prefab", "textures/gui/ranchHouse.png"), "A less configurable house designed with a lofted area for the bed and chests.", 152, 89);

		private final int value;
		private final String displayName;
		private final ResourceLocation housePicture;
		private final String houseNotes;
		private final int imageWidth;
		private final int imageHeight;
		
		HouseStyle(int newValue, String displayName, ResourceLocation housePicture, String houseNotes, int imageWidth, int imageHeight) 
		{
			this.value = newValue;
			this.displayName = displayName;
			this.housePicture = housePicture;
			this.houseNotes = houseNotes;
			this.imageWidth = imageWidth;
			this.imageHeight = imageHeight;
		}

		public int getValue() 
		{
			return value; 
		}
		
		public String getDisplayName() 
		{
			return this.displayName;
		}
		
		public String getHouseNotes()
		{
			return this.houseNotes;
		}
		
		public ResourceLocation getHousePicture()
		{
			return this.housePicture;
		}
		
		public int getImageWidth()
		{
			return this.imageWidth;
		}
		
		public int getImageHeight()
		{
			return this.imageHeight;
		}

		public static HouseStyle ValueOf(int value)
		{
			switch (value)
			{
				case 0:
				{
					return HouseStyle.BASIC;
				}
	
				case 1:
				{
					return HouseStyle.RANCH;
				}
				
				case 2:
				{
					return HouseStyle.LOFT;
				}
	
				default:
				{
					return HouseStyle.BASIC;
				}
			}
		}
	}
}
