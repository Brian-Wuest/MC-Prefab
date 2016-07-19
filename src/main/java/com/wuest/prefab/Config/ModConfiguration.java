package com.wuest.prefab.Config;

import com.wuest.prefab.Prefab;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.config.Configuration;

/**
 * This class is used to hold the mod configuration.
 * @author WuestMan
 *
 */
public class ModConfiguration
{
	public static String OPTIONS = "general.options";
	public static String ChestContentOptions = "general.options.chest contents";
	public static String tagKey = "PrefabConfig";
	
	// Config file option names.
	private static String addHouseItemName = "Add House Item On New Player Join";
	private static String enableHouseGenerationRestrictionName = "Enable House Generation Restrictions";
	private static String maximumHouseSizeName = "Maximum Starting House Size";
	
	// Chest content option names.
	private static String addSwordName = "Add Sword";
	private static String addAxeName = "Add Axe";
	private static String addHoeName = "Add Hoe";
	private static String addShovelName = "Add Shovel";
	private static String addPickAxeName = "Add Pickaxe";
	private static String addArmorName = "Add Armor";
	private static String addFoodName = "Add Food";
	private static String addCropsName = "Add Crops";
	private static String addDirtName = "Add Dirt";
	private static String addCobbleName = "Add Cobblestone";
	private static String addSaplingsName = "Add Saplings";
	private static String addTorchesName = "Add Torches";
	
	// Configuration Options.
	public boolean addHouseItem;
	public boolean enableHouseGenerationRestrictions;
	public int maximumStartingHouseSize;
	
	// Chest content options.
	public boolean addSword;
	public boolean addAxe;
	public boolean addHoe;
	public boolean addShovel;
	public boolean addPickAxe;

	public boolean addArmor;
	public boolean addFood;
	public boolean addCrops;
	public boolean addDirt;
	public boolean addCobble;
	public boolean addSaplings;
	public boolean addTorches;
	
	public ModConfiguration()
	{
		this.addHouseItem = true;
		this.maximumStartingHouseSize = 16;
	}
	
	public static void syncConfig()
	{
		Configuration config = Prefab.config;

		if (Prefab.proxy.proxyConfiguration == null)
		{
			Prefab.proxy.proxyConfiguration = new ModConfiguration();
		}

		// General settings.
		Prefab.proxy.proxyConfiguration.addHouseItem = config.getBoolean(ModConfiguration.addHouseItemName, ModConfiguration.OPTIONS, true, "Determines if the house item is added to player inventory when joining the world for the first time. Server configuration overrides client.");
		Prefab.proxy.proxyConfiguration.enableHouseGenerationRestrictions = config.getBoolean(ModConfiguration.enableHouseGenerationRestrictionName, ModConfiguration.OPTIONS, false, "When true this option causes the Crafting Table, Furnace and Chest to not be added when creating a house, regardless of options chosen. Server Configuration overrides client.");
		Prefab.proxy.proxyConfiguration.maximumStartingHouseSize = config.getInt(ModConfiguration.maximumHouseSizeName, ModConfiguration.OPTIONS, 16, 5, 16, "Determines the maximum size the starting house can be generated as. Server configuration overrides client.");
		
		config.setCategoryComment(ModConfiguration.ChestContentOptions, "This category is to determine the contents of the chest created by the house item. When playing on a server, the server configuration is used.");

		Prefab.proxy.proxyConfiguration.addSword = config.getBoolean(ModConfiguration.addSwordName, ModConfiguration.ChestContentOptions, true, "Determines if a Stone Sword is added the the chest when the house is created.");
		Prefab.proxy.proxyConfiguration.addAxe = config.getBoolean(ModConfiguration.addAxeName, ModConfiguration.ChestContentOptions, true, "Determines if a Stone Axe is added the the chest when the house is created.");
		Prefab.proxy.proxyConfiguration.addShovel = config.getBoolean(ModConfiguration.addShovelName, ModConfiguration.ChestContentOptions, true, "Determines if a Stone Shovel is added the the chest when the house is created.");
		Prefab.proxy.proxyConfiguration.addHoe = config.getBoolean(ModConfiguration.addHoeName, ModConfiguration.ChestContentOptions, true, "Determines if a Stone Hoe is added the the chest when the house is created.");
		Prefab.proxy.proxyConfiguration.addPickAxe = config.getBoolean(ModConfiguration.addPickAxeName, ModConfiguration.ChestContentOptions, true, "Determines if a Stone Pickaxe is added the the chest when the house is created.");
		Prefab.proxy.proxyConfiguration.addArmor = config.getBoolean(ModConfiguration.addArmorName, ModConfiguration.ChestContentOptions, true, "Determines if Leather Armor is added the the chest when the house is created.");
		Prefab.proxy.proxyConfiguration.addFood = config.getBoolean(ModConfiguration.addFoodName, ModConfiguration.ChestContentOptions, true, "Determines if Bread is added the the chest when the house is created.");
		Prefab.proxy.proxyConfiguration.addCrops = config.getBoolean(ModConfiguration.addCropsName, ModConfiguration.ChestContentOptions, true, "Determines if seeds, potatoes and carros are added the the chest when the house is created.");
		Prefab.proxy.proxyConfiguration.addDirt = config.getBoolean(ModConfiguration.addDirtName, ModConfiguration.ChestContentOptions, true, "Determines if a stack of dirt is added the the chest when the house is created.");
		Prefab.proxy.proxyConfiguration.addCobble = config.getBoolean(ModConfiguration.addCobbleName, ModConfiguration.ChestContentOptions, true, "Determines if a stack of cobble is added the the chest when the house is created.");
		Prefab.proxy.proxyConfiguration.addSaplings = config.getBoolean(ModConfiguration.addSaplingsName, ModConfiguration.ChestContentOptions, true, "Determines if a set of oak saplings are added the the chest when the house is created.");
		Prefab.proxy.proxyConfiguration.addTorches = config.getBoolean(ModConfiguration.addTorchesName, ModConfiguration.ChestContentOptions, true, "Determines if a set of torches are added the the chest when the house is created.");

		// GUI Options
		//config.setCategoryComment(WuestConfiguration.GuiOptions, "This category is to configure the various GUI options for this mod.");
		
		if (config.hasChanged()) 
		{
			config.save();
		}
	}

	public NBTTagCompound ToNBTTagCompound()
	{
	
		return null;
	}
	
	public void UpdateFromNBTTagCompound(NBTTagCompound tag)
	{
		
	}
	
	public enum CeilingFloorBlockType
	{
		StoneBrick(0),
		Brick(1),
		SandStone(2);

		private final int value;

		CeilingFloorBlockType(int newValue) 
		{
			value = newValue;
		}

		public int getValue() { return value; }

		public static CeilingFloorBlockType ValueOf(int value)
		{
			switch (value)
			{
			case 1:
			{
				return CeilingFloorBlockType.Brick;
			}

			case 2:
			{
				return CeilingFloorBlockType.SandStone;
			}

			default:
			{
				return CeilingFloorBlockType.StoneBrick;
			}
			}
		}
	}

	public enum WallBlockType
	{
		Oak(0),
		Spruce(1),
		Birch(2),
		Jungle(3),
		Acacia(4),
		DarkOak(5);

		private final int value;

		WallBlockType(final int newValue) 
		{
			value = newValue;
		}

		public int getValue() { return value; }

		public static WallBlockType ValueOf(int value)
		{
			switch (value)
			{
			case 1:
			{
				return WallBlockType.Spruce;
			}

			case 2:
			{
				return WallBlockType.Birch;
			}

			case 3:
			{
				return WallBlockType.Jungle;
			}

			case 4:
			{
				return WallBlockType.Acacia;
			}

			case 5:
			{
				return WallBlockType.DarkOak;
			}

			default:
			{
				return WallBlockType.Oak;
			}
			}
		}
	}
}
