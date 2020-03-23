package com.wuest.prefab.Structures.Config;

import com.wuest.prefab.Gui.GuiLangKeys;
import com.wuest.prefab.ModRegistry;
import com.wuest.prefab.Structures.Predefined.StructureAlternateStart;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

/**
 * This class is used to determine the configuration for a particular house.
 *
 * @author WuestMan
 */
public class HouseConfiguration extends StructureConfiguration {
	private static String addTorchesTag = "addTorches";
	private static String addBedTag = "addBed";
	private static String addCraftingTableTag = "addCraftingTable";
	private static String addFurnaceTag = "addFurnace";
	private static String addChestTag = "addChest";
	private static String addChestContentsTag = "addChestContents";
	private static String addMineShaftTag = "addMineShaft";
	private static String hitXTag = "hitX";
	private static String hitYTag = "hitY";
	private static String hitZTag = "hitZ";
	private static String houseFacingTag = "houseFacing";
	private static String houseStyleTag = "houseStyle";
	private static String glassColorTag = "glassColor";
	private static String bedColorTag = "bedColor";

	public boolean addTorches;
	public boolean addBed;
	public boolean addCraftingTable;
	public boolean addFurnace;
	public boolean addChest;
	public boolean addChestContents;
	public boolean addMineShaft;
	public HouseStyle houseStyle;
	public DyeColor glassColor;
	public DyeColor bedColor;

	/**
	 * Initializes a new instance of the {@link HouseConfiguration} class.
	 */
	public HouseConfiguration() {
		super();
	}

	@Override
	public void Initialize() {
		super.Initialize();
		this.houseStyle = HouseStyle.BASIC;
		this.glassColor = DyeColor.LIGHT_GRAY;
		this.bedColor = DyeColor.RED;
		this.addTorches = true;
		this.addBed = true;
		this.addCraftingTable = true;
		this.addFurnace = true;
		this.addChest = true;
		this.addChestContents = true;
		this.addMineShaft = true;
	}

	@Override
	public CompoundNBT WriteToCompoundNBT() {
		CompoundNBT tag = new CompoundNBT();

		// This tag should only be written for options which will NOT be overwritten by server options.
		// Server configuration settings will be used for all other options.
		// This is so the server admin can force a player to not use something.
		tag.putBoolean(HouseConfiguration.addTorchesTag, this.addTorches);
		tag.putBoolean(HouseConfiguration.addBedTag, this.addBed);
		tag.putBoolean(HouseConfiguration.addCraftingTableTag, this.addCraftingTable);
		tag.putBoolean(HouseConfiguration.addFurnaceTag, this.addFurnace);
		tag.putBoolean(HouseConfiguration.addChestTag, this.addChest);
		tag.putBoolean(HouseConfiguration.addChestContentsTag, this.addChestContents);
		tag.putBoolean(HouseConfiguration.addMineShaftTag, this.addMineShaft);
		tag.putInt(HouseConfiguration.hitXTag, this.pos.getX());
		tag.putInt(HouseConfiguration.hitYTag, this.pos.getY());
		tag.putInt(HouseConfiguration.hitZTag, this.pos.getZ());
		tag.putString(HouseConfiguration.houseFacingTag, this.houseFacing.getName());
		tag.putInt(HouseConfiguration.houseStyleTag, this.houseStyle.value);
		tag.putString(HouseConfiguration.glassColorTag, this.glassColor.getName().toUpperCase());
		tag.putString(HouseConfiguration.bedColorTag, this.bedColor.getName().toUpperCase());

		return tag;
	}

	/**
	 * Custom method to read the CompoundNBT message.
	 *
	 * @param tag The message to create the configuration from.
	 * @return An new configuration object with the values derived from the CompoundNBT.
	 */
	@Override
	public HouseConfiguration ReadFromCompoundNBT(CompoundNBT tag) {
		HouseConfiguration config = null;

		if (tag != null) {
			config = new HouseConfiguration();

			if (tag.contains(HouseConfiguration.addTorchesTag)) {
				config.addTorches = tag.getBoolean(HouseConfiguration.addTorchesTag);
			}

			if (tag.contains(HouseConfiguration.addBedTag)) {
				config.addBed = tag.getBoolean(HouseConfiguration.addBedTag);
			}

			if (tag.contains(HouseConfiguration.addCraftingTableTag)) {
				config.addCraftingTable = tag.getBoolean(HouseConfiguration.addCraftingTableTag);
			}

			if (tag.contains(HouseConfiguration.addFurnaceTag)) {
				config.addFurnace = tag.getBoolean(HouseConfiguration.addFurnaceTag);
			}

			if (tag.contains(HouseConfiguration.addChestTag)) {
				config.addChest = tag.getBoolean(HouseConfiguration.addChestTag);
			}

			if (tag.contains(HouseConfiguration.addChestContentsTag)) {
				config.addChestContents = tag.getBoolean(HouseConfiguration.addChestContentsTag);
			}

			if (tag.contains(HouseConfiguration.addMineShaftTag)) {
				config.addMineShaft = tag.getBoolean(HouseConfiguration.addMineShaftTag);
			}

			if (tag.contains(HouseConfiguration.hitXTag)) {
				config.pos = new BlockPos(tag.getInt(HouseConfiguration.hitXTag), tag.getInt(HouseConfiguration.hitYTag), tag.getInt(HouseConfiguration.hitZTag));
			}

			if (tag.contains(HouseConfiguration.houseFacingTag)) {
				config.houseFacing = Direction.byName(tag.getString(HouseConfiguration.houseFacingTag));
			}

			if (tag.contains(HouseConfiguration.houseStyleTag)) {
				config.houseStyle = HouseStyle.ValueOf(tag.getInt(HouseConfiguration.houseStyleTag));
			}

			if (tag.contains(HouseConfiguration.glassColorTag)) {
				config.glassColor = DyeColor.valueOf(tag.getString(HouseConfiguration.glassColorTag));
			}

			if (tag.contains(HouseConfiguration.bedColorTag)) {
				config.bedColor = DyeColor.valueOf(tag.getString(HouseConfiguration.bedColorTag));
			}
		}

		return config;
	}

	/**
	 * This is used to actually build the structure as it creates the structure instance and calls build structure.
	 *
	 * @param player      The player which requested the build.
	 * @param world       The world instance where the build will occur.
	 * @param hitBlockPos This hit block position.
	 */
	@Override
	protected void ConfigurationSpecificBuildStructure(PlayerEntity player, ServerWorld world, BlockPos hitBlockPos) {
		boolean houseBuilt = true;

		// Build the alternate starter house instead.
		StructureAlternateStart structure = StructureAlternateStart.CreateInstance(this.houseStyle.getStructureLocation(), StructureAlternateStart.class);
		houseBuilt = structure.BuildStructure(this, world, hitBlockPos, Direction.NORTH, player);

		// The house was successfully built, remove the item from the inventory.
		if (houseBuilt) {
			this.RemoveStructureItemFromPlayer(player, ModRegistry.StartHouse());
		}
	}

	/**
	 * This enum is used to contain the different type of starting houses available to the player.
	 *
	 * @author WuestMan
	 */
	public enum HouseStyle {
		BASIC(
				0,
				GuiLangKeys.STARTER_HOUSE_BASIC_DISPLAY,
				new ResourceLocation("prefab", "textures/gui/basic_house.png"),
				GuiLangKeys.STARTER_HOUSE_BASIC_NOTES,
				163,
				146,
				"assets/prefab/structures/basic_house.zip"),
		RANCH(1, GuiLangKeys.STARTER_HOUSE_RANCH_DISPLAY, new ResourceLocation("prefab", "textures/gui/ranch_house.png"), GuiLangKeys.STARTER_HOUSE_RANCH_NOTES, 152, 89,
				"assets/prefab/structures/ranch_house.zip"),
		LOFT(2, GuiLangKeys.STARTER_HOUSE_LOFT_DISPLAY, new ResourceLocation("prefab", "textures/gui/loft_house.png"), GuiLangKeys.STARTER_HOUSE_LOFT_NOTES, 152, 87,
				"assets/prefab/structures/loft_house.zip"),
		HOBBIT(3, GuiLangKeys.STARTER_HOUSE_HOBBIT_DISPLAY, new ResourceLocation("prefab", "textures/gui/hobbit_house.png"), GuiLangKeys.STARTER_HOUSE_HOBBIT_NOTES, 151, 133,
				"assets/prefab/structures/hobbit_house.zip"),
		DESERT(4, GuiLangKeys.STARTER_HOUSE_DESERT_DISPLAY, new ResourceLocation("prefab", "textures/gui/desert_house.png"), GuiLangKeys.STARTER_HOUSE_DESERT_NOTES, 152, 131,
				"assets/prefab/structures/desert_house.zip"),
		SNOWY(5, GuiLangKeys.STARTER_HOUSE_SNOWY_DISPLAY, new ResourceLocation("prefab", "textures/gui/snowy_house.png"), GuiLangKeys.STARTER_HOUSE_SNOWY_NOTES, 150, 125,
				"assets/prefab/structures/snowy_house.zip"),
		DESERT2(6,
				GuiLangKeys.STARTER_HOUSE_DESERT_DISPLAY2,
				new ResourceLocation("prefab", "textures/gui/desert_house2.png"),
				GuiLangKeys.STARTER_HOUSE_DESERT_NOTES2,
				145,
				153,
				"assets/prefab/structures/desert_house2.zip"),
		SUBAQUATIC(7,
				GuiLangKeys.STARTER_HOUSE_SUBAQUATIC_DISPLAY,
				new ResourceLocation("prefab", "textures/gui/subaquatic_house.png"),
				GuiLangKeys.STARTER_HOUSE_SUBAQUATIC_NOTES,
				144,
				162,
				"assets/prefab/structures/subaquatic_house.zip");

		private final int value;
		private final String displayName;
		private final ResourceLocation housePicture;
		private final String houseNotes;
		private final int imageWidth;
		private final int imageHeight;
		private final String structureLocation;

		HouseStyle(int newValue, String displayName, ResourceLocation housePicture, String houseNotes, int imageWidth, int imageHeight, String structureLocation) {
			this.value = newValue;
			this.displayName = displayName;
			this.housePicture = housePicture;
			this.houseNotes = houseNotes;
			this.imageWidth = imageWidth;
			this.imageHeight = imageHeight;
			this.structureLocation = structureLocation;
		}

		/**
		 * Returns a house style based off of an integer value.
		 *
		 * @param value The integer value representing the house style.
		 * @return The house style found or HouseStyle.Basic if none found.
		 */
		public static HouseStyle ValueOf(int value) {
			HouseStyle returnValue = HouseStyle.BASIC;

			for (HouseStyle current : HouseStyle.values()) {
				if (current.value == value) {
					returnValue = current;
					break;
				}
			}

			return returnValue;
		}

		/**
		 * Gets a unique identifier for this style.
		 *
		 * @return An integer representing the ID of this style.
		 */
		public int getValue() {
			return value;
		}

		/**
		 * Gets the display name for this style.
		 *
		 * @return A string representing the name of this style.
		 */
		public String getDisplayName() {
			return GuiLangKeys.translateString(this.displayName);
		}

		/**
		 * Gets the notes for this house style.
		 *
		 * @return A string representing the translated notes for this style.
		 */
		public String getHouseNotes() {
			return GuiLangKeys.translateString(this.houseNotes);
		}

		/**
		 * Gets the picture used in the GUI for this style.
		 *
		 * @return A resource location representing the image to use for this style.
		 */
		public ResourceLocation getHousePicture() {
			return this.housePicture;
		}

		/**
		 * Gets the width of the image to use with this style.
		 *
		 * @return An integer representing the image width.
		 */
		public int getImageWidth() {
			return this.imageWidth;
		}

		/**
		 * Gets the height of the image to use with this style.
		 *
		 * @return An integer representing the image height.
		 */
		public int getImageHeight() {
			return this.imageHeight;
		}

		/**
		 * Gets a string for the resource location of this style.
		 *
		 * @return A string representing the location of the structure asset in the mod.
		 */
		public String getStructureLocation() {
			return this.structureLocation;
		}
	}
}
