package com.wuest.prefab.Config;

import java.util.ArrayList;
import java.util.Map.Entry;

import com.wuest.prefab.ModRegistry;
import com.wuest.prefab.Prefab;
import com.wuest.prefab.UpdateChecker;
import com.wuest.prefab.Gui.GuiLangKeys;

import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.registry.GameRegistry;
import scala.Tuple2;

/**
 * This class is used to hold the mod configuration.
 * @author WuestMan
 *
 */
public class ModConfiguration
{
	public static String OPTIONS = "general.options";
	public static String ChestContentOptions = "general.options.chest contents";
	public static String RecipeOptions = "general.options.recipes";
	public static String tagKey = "PrefabConfig";
	
	// Config file option names.
	private static String addHouseItemName = "Add House Item On New Player Join";
	private static String enableHouseGenerationRestrictionName = "Enable House Generation Restrictions";
	private static String maximumHouseSizeName = "Maximum Starting House Size";
	private static String enableVersionCheckMessageName = "Enable Version Checking";
	private static String enableLoftHouseName = "Enable Loft House";
	private static String includeSpawnersInMasherName = "Include Spawners in Monster Masher";
	private static String enableStructurePreviewName = "Include Structure Previews";
	
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
	
	private static String versionMessageName = "Version Message";
	private static String showMessageName = "Show Message";
	
	// Configuration Options.
	public boolean addHouseItem;
	public boolean enableHouseGenerationRestrictions;
	public int maximumStartingHouseSize;
	public boolean enableVersionCheckMessage;
	public boolean enableLoftHouse;
	public boolean includeSpawnersInMasher;
	public boolean enableStructurePreview;
	
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
	
	// Version Check Message Info
	public String versionMessage = "";
	public boolean showMessage = false;
	
	public ModConfiguration()
	{
		this.addHouseItem = true;
		this.maximumStartingHouseSize = 16;
		this.enableVersionCheckMessage = true;
		this.includeSpawnersInMasher = true;
		this.enableStructurePreview = true;
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
		Prefab.proxy.proxyConfiguration.enableVersionCheckMessage = config.getBoolean(ModConfiguration.enableVersionCheckMessageName, ModConfiguration.OPTIONS, true, "Determines if version checking is enabled when application starts. Also determines if the chat message about old versions is shown when joining a world. Server configuration overrides client.");
		Prefab.proxy.proxyConfiguration.enableLoftHouse = config.getBoolean(ModConfiguration.enableLoftHouseName, ModConfiguration.OPTIONS, false, "Determines if the loft starter house is enabled. This house contains Nether materials in it's construction. Server configuration overrides client.");
		Prefab.proxy.proxyConfiguration.includeSpawnersInMasher = config.getBoolean(ModConfiguration.includeSpawnersInMasherName, ModConfiguration.OPTIONS, true, "Determines if the spawners for the Monster Masher building are included. Server configuration overrides client.");
		Prefab.proxy.proxyConfiguration.enableStructurePreview = config.getBoolean(ModConfiguration.enableStructurePreviewName, ModConfiguration.OPTIONS, true, "Determines if the Preview buttons in structure GUIs and other structure previews functions are enabled. Client side only.");
		
		// Make this property require a restart.
		config.get(ModConfiguration.OPTIONS, ModConfiguration.enableVersionCheckMessageName, true).setRequiresMcRestart(true);
		config.get(ModConfiguration.OPTIONS, ModConfiguration.enableLoftHouseName, false).setRequiresMcRestart(true);
		
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
		
		config.setCategoryComment(ModConfiguration.RecipeOptions, "This category determines if the recipes for the blocks/items in this are enabled");
		config.setCategoryRequiresMcRestart(ModConfiguration.RecipeOptions, true);
		
		// Recipe configuration.
		for (Entry<String, Tuple2<Boolean, ArrayList<IRecipe>>> set : ModRegistry.SavedRecipes.entrySet())
		{
			boolean value = config.getBoolean(set.getKey(), RecipeOptions, true, "Determines if the recipe(s) associated with the " + set.getKey() + " are enabled.");
			boolean originalValue = set.getValue()._1;
			
			if (value != originalValue)
			{
				set.setValue(new Tuple2<Boolean, ArrayList<IRecipe>>(value, set.getValue()._2));
				
				if (value)
				{
					// Re-add the recipes to the game registry.
					for (IRecipe recipe : set.getValue()._2)
					{
						GameRegistry.addRecipe(recipe);
					}
				}
				else
				{
					// Remove the recipes from the game registry.
					for (IRecipe recipe :set.getValue()._2)
					{
						CraftingManager.getInstance().getRecipeList().remove(recipe);
					}
				}
			}
		}
		
		if (config.hasChanged()) 
		{
			config.save();
		}
	}

	public NBTTagCompound ToNBTTagCompound()
	{
		NBTTagCompound tag = new NBTTagCompound();
		
		tag.setBoolean(ModConfiguration.addHouseItemName, this.addHouseItem);
		tag.setBoolean(ModConfiguration.enableHouseGenerationRestrictionName, this.enableHouseGenerationRestrictions);
		tag.setInteger(ModConfiguration.maximumHouseSizeName, this.maximumStartingHouseSize);
		tag.setBoolean(ModConfiguration.enableVersionCheckMessageName, this.enableVersionCheckMessage);
		tag.setBoolean(ModConfiguration.enableLoftHouseName, this.enableLoftHouse);
		tag.setBoolean(ModConfiguration.includeSpawnersInMasherName, this.includeSpawnersInMasher);
		tag.setBoolean(ModConfiguration.enableStructurePreviewName, this.enableStructurePreview);
		
		tag.setBoolean(ModConfiguration.addSwordName, this.addSword);
		tag.setBoolean(ModConfiguration.addAxeName, this.addAxe);
		tag.setBoolean(ModConfiguration.addShovelName, this.addShovel);
		tag.setBoolean(ModConfiguration.addHoeName, this.addHoe);
		tag.setBoolean(ModConfiguration.addPickAxeName, this.addPickAxe);
		tag.setBoolean(ModConfiguration.addArmorName, this.addArmor);
		tag.setBoolean(ModConfiguration.addFoodName, this.addFood);
		tag.setBoolean(ModConfiguration.addCropsName, this.addCrops);
		tag.setBoolean(ModConfiguration.addDirtName, this.addDirt);
		tag.setBoolean(ModConfiguration.addCobbleName, this.addCobble);
		tag.setBoolean(ModConfiguration.addSaplingsName, this.addSaplings);
		tag.setBoolean(ModConfiguration.addTorchesName, this.addTorches);
		
		tag.setString(ModConfiguration.versionMessageName, UpdateChecker.messageToShow);
		tag.setBoolean(ModConfiguration.showMessageName, UpdateChecker.showMessage);
		
		for (Entry<String, Tuple2<Boolean, ArrayList<IRecipe>>> set : ModRegistry.SavedRecipes.entrySet())
		{
			tag.setBoolean(set.getKey(), set.getValue()._1());
		}
		
		return tag;
	}
	
	public static ModConfiguration getFromNBTTagCompound(NBTTagCompound tag)
	{
		ModConfiguration config = new ModConfiguration();
		
		config.addHouseItem = tag.getBoolean(ModConfiguration.addHouseItemName);
		config.enableHouseGenerationRestrictions = tag.getBoolean(ModConfiguration.enableHouseGenerationRestrictionName);
		config.enableVersionCheckMessage = tag.getBoolean(ModConfiguration.enableVersionCheckMessageName);
		config.enableLoftHouse = tag.getBoolean(ModConfiguration.enableLoftHouseName);
		config.maximumStartingHouseSize = tag.getInteger(ModConfiguration.maximumHouseSizeName);
		config.includeSpawnersInMasher = tag.getBoolean(ModConfiguration.includeSpawnersInMasherName);
		config.enableStructurePreview = tag.getBoolean(ModConfiguration.enableStructurePreviewName);
		
		// Make sure the server admin didn't set the maximum starting size to an invalid value from the configuration file.
		if (config.maximumStartingHouseSize < 5 || config.maximumStartingHouseSize > 16)
		{
			config.maximumStartingHouseSize = 16;
		}
		
		config.addSword = tag.getBoolean(ModConfiguration.addSwordName);
		config.addAxe = tag.getBoolean(ModConfiguration.addAxeName);
		config.addShovel = tag.getBoolean(ModConfiguration.addShovelName);
		config.addHoe = tag.getBoolean(ModConfiguration.addHoeName);
		config.addPickAxe = tag.getBoolean(ModConfiguration.addPickAxeName);
		config.addArmor = tag.getBoolean(ModConfiguration.addArmorName);
		config.addFood = tag.getBoolean(ModConfiguration.addFoodName);
		config.addCrops = tag.getBoolean(ModConfiguration.addCropsName);
		config.addDirt = tag.getBoolean(ModConfiguration.addDirtName);
		config.addCobble = tag.getBoolean(ModConfiguration.addCobbleName);
		config.addSaplings = tag.getBoolean(ModConfiguration.addSaplingsName);
		config.addTorches = tag.getBoolean(ModConfiguration.addTorchesName);
		
		config.versionMessage = tag.getString(ModConfiguration.versionMessageName);
		config.showMessage = tag.getBoolean(ModConfiguration.showMessageName);
		
		for (Entry<String, Tuple2<Boolean, ArrayList<IRecipe>>> set : ModRegistry.SavedRecipes.entrySet())
		{
			if (tag.hasKey(set.getKey()))
			{
				boolean value = tag.getBoolean(set.getKey());
				set.setValue(new Tuple2<Boolean, ArrayList<IRecipe>>(value, set.getValue()._2));
			}
		}
		
		return config;
	}
	
	public enum CeilingFloorBlockType
	{
		StoneBrick(0, GuiLangKeys.CEILING_BLOCK_TYPE_STONE),
		Brick(1, GuiLangKeys.CEILING_BLOCK_TYPE_BRICK),
		SandStone(2, GuiLangKeys.CEILING_BLOCK_TYPE_SAND);

		private final int value;
		private final String langKey;

		CeilingFloorBlockType(int newValue, String langKey) 
		{
			this.value = newValue;
			this.langKey = langKey;
		}

		public int getValue() { return value; }
		
		public String getName()
		{
			return GuiLangKeys.translateString(this.langKey);
		}

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
		Oak(0, GuiLangKeys.WALL_BLOCK_TYPE_OAK),
		Spruce(1, GuiLangKeys.WALL_BLOCK_TYPE_SPRUCE),
		Birch(2, GuiLangKeys.WALL_BLOCK_TYPE_BIRCH),
		Jungle(3, GuiLangKeys.WALL_BLOCK_TYPE_JUNGLE),
		Acacia(4, GuiLangKeys.WALL_BLOCK_TYPE_ACACIA),
		DarkOak(5, GuiLangKeys.WALL_BLOCK_TYPE_DARK_OAK);

		private final int value;
		private final String langKey;

		WallBlockType(final int newValue, String langKey)
		{
			value = newValue;
			this.langKey = langKey;
		}

		public int getValue() { return value; }

		public String getName()
		{
			return GuiLangKeys.translateString(this.langKey);
		}
		
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
