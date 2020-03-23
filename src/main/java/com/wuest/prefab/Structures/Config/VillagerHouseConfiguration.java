package com.wuest.prefab.Structures.Config;

import com.wuest.prefab.Gui.GuiLangKeys;
import com.wuest.prefab.ModRegistry;
import com.wuest.prefab.Structures.Predefined.StructureVillagerHouses;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

/**
 * This is the villager house configuration.
 *
 * @author WuestMan
 */
public class VillagerHouseConfiguration extends StructureConfiguration {
	public static String tagKey = "villagerHouseConfig";

	private static String houseStyleTag = "houseStyle";
	private static String bedColorTag = "bedColor";

	/**
	 * The house style.
	 */
	public HouseStyle houseStyle;

	public DyeColor bedColor;

	/**
	 * Initializes a new instance of the VillagerHouseConfiguration class.
	 */
	public VillagerHouseConfiguration() {
		super();
	}

	@Override
	public void Initialize() {
		super.Initialize();
		this.houseStyle = HouseStyle.FLAT_ROOF;
		this.bedColor = DyeColor.RED;
	}

	@Override
	protected CompoundNBT CustomWriteToCompoundNBT(CompoundNBT tag) {
		tag.putInt(VillagerHouseConfiguration.houseStyleTag, this.houseStyle.value);
		tag.putString(VillagerHouseConfiguration.bedColorTag, this.bedColor.getName().toUpperCase());
		return tag;
	}

	@Override
	protected void CustomReadFromNBTTag(CompoundNBT messageTag, StructureConfiguration config) {
		VillagerHouseConfiguration houseConfiguration = ((VillagerHouseConfiguration) config);

		if (messageTag.contains(VillagerHouseConfiguration.houseStyleTag)) {
			((VillagerHouseConfiguration) config).houseStyle = HouseStyle.ValueOf(messageTag.getInt(VillagerHouseConfiguration.houseStyleTag));
		}

		if (messageTag.contains(VillagerHouseConfiguration.bedColorTag)) {
			houseConfiguration.bedColor = DyeColor.valueOf(messageTag.getString(VillagerHouseConfiguration.bedColorTag));
		}
	}

	/**
	 * Custom method to read the CompoundNBT message.
	 *
	 * @param messageTag The message to create the configuration from.
	 * @return An new configuration object with the values derived from the CompoundNBT.
	 */
	@Override
	public VillagerHouseConfiguration ReadFromCompoundNBT(CompoundNBT messageTag) {
		VillagerHouseConfiguration config = new VillagerHouseConfiguration();

		return (VillagerHouseConfiguration) super.ReadFromCompoundNBT(messageTag, config);
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
		StructureVillagerHouses structure = StructureVillagerHouses.CreateInstance(this.houseStyle.getStructureLocation(), StructureVillagerHouses.class);
		if (structure.BuildStructure(this, world, hitBlockPos, Direction.NORTH, player)) {
			this.DamageHeldItem(player, ModRegistry.VillagerHouses());
		}
	}

	/**
	 * This house style is used to determine what type of houses are available to the user.
	 *
	 * @author WuestMan
	 */
	public enum HouseStyle {
		FLAT_ROOF(0, GuiLangKeys.VILLAGER_HOUSE_FLAT_ROOF, new ResourceLocation("prefab", "textures/gui/village_house_flat.png"), 165, 128,
				"assets/prefab/structures/villager_house_flat.zip", 4, 5, 5, 2),
		ANGLED_ROOF(1, GuiLangKeys.VILLAGER_HOUSE_ANGLED_ROOF, new ResourceLocation("prefab", "textures/gui/village_house_angled.png"), 161, 155,
				"assets/prefab/structures/villager_house_angled.zip", 4, 5, 6, 2),
		FENCED_ROOF(2, GuiLangKeys.VILLAGER_HOUSE_FENCED_ROOF, new ResourceLocation("prefab", "textures/gui/village_house_fenced.png"), 159, 156,
				"assets/prefab/structures/villager_house_fenced.zip", 4, 5, 6, 2),
		BLACKSMITH(3, GuiLangKeys.VILLAGER_HOUSE_BLACKSMITH, new ResourceLocation("prefab", "textures/gui/village_house_blacksmith.png"), 157, 73,
				"assets/prefab/structures/villager_house_blacksmith.zip", 7, 5, 6, 5),
		LONG_HOUSE(4, GuiLangKeys.VILLAGER_HOUSE_LONGHOUSE, new ResourceLocation("prefab", "textures/gui/village_house_long.png"), 157, 114,
				"assets/prefab/structures/villager_house_long.zip", 8, 6, 7, 4);

		private final int value;
		private final String displayName;
		private final ResourceLocation housePicture;
		private final int imageWidth;
		private final int imageHeight;
		private final String structureLocation;
		private final int width;
		private final int length;
		private final int height;
		private final int eastOffSet;

		HouseStyle(int newValue, String displayName, ResourceLocation housePicture, int imageWidth, int imageHeight, String structureLocation, int width, int length, int height,
				   int eastOffSet) {
			this.value = newValue;
			this.displayName = displayName;
			this.housePicture = housePicture;
			this.imageWidth = imageWidth;
			this.imageHeight = imageHeight;
			this.structureLocation = structureLocation;
			this.width = width;
			this.length = length;
			this.height = height;
			this.eastOffSet = eastOffSet;
		}

		public static HouseStyle ValueOf(int value) {
			switch (value) {
				case 1: {
					return HouseStyle.ANGLED_ROOF;
				}

				case 2: {
					return HouseStyle.FENCED_ROOF;
				}

				case 3: {
					return HouseStyle.BLACKSMITH;
				}

				case 4: {
					return HouseStyle.LONG_HOUSE;
				}

				default: {
					return HouseStyle.FLAT_ROOF;
				}
			}
		}

		public int getValue() {
			return value;
		}

		public String getDisplayName() {
			return GuiLangKeys.translateString(this.displayName);
		}

		public ResourceLocation getHousePicture() {
			return this.housePicture;
		}

		public int getImageWidth() {
			return this.imageWidth;
		}

		public int getImageHeight() {
			return this.imageHeight;
		}

		public int getWidth() {
			return this.width;
		}

		public int getLength() {
			return this.length;
		}

		public int getHeight() {
			return this.height;
		}

		public int getEastOffSet() {
			return this.eastOffSet;
		}

		public String getStructureLocation() {
			return this.structureLocation;
		}
	}
}
