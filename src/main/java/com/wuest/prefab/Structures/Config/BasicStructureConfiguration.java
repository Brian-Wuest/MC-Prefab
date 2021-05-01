package com.wuest.prefab.Structures.Config;

import com.wuest.prefab.Structures.Config.Enums.*;
import com.wuest.prefab.Structures.Items.ItemBasicStructure;
import com.wuest.prefab.Structures.Predefined.StructureBasic;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

/**
 * This class is used for the basic structures in the mod.
 *
 * @author WuestMan
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class BasicStructureConfiguration extends StructureConfiguration {
	private static String structureEnumNameTag = "structureEnumName";
	private static String structureDisplayNameTag = "structureDisplayName";
	private static String bedColorTag = "bedColor";
	private static String chosenOptionTag = "chosenOption";

	static {
		// This static method is used to set up the clear shapes for the basic structure names.
		EnumBasicStructureName.AdvancedCoop.baseOption.getSpecificOptions().get(0).getClearShape().setDirection(Direction.SOUTH);
		EnumBasicStructureName.AdvancedCoop.baseOption.getSpecificOptions().get(0).getClearShape().setHeight(10);
		EnumBasicStructureName.AdvancedCoop.baseOption.getSpecificOptions().get(0).getClearShape().setWidth(11);
		EnumBasicStructureName.AdvancedCoop.baseOption.getSpecificOptions().get(0).getClearShape().setLength(11);
		EnumBasicStructureName.AdvancedCoop.baseOption.getSpecificOptions().get(0).getClearPositionOffset().setSouthOffset(1);
		EnumBasicStructureName.AdvancedCoop.baseOption.getSpecificOptions().get(0).getClearPositionOffset().setEastOffset(5);

		EnumBasicStructureName.AdvancedHorseStable.baseOption.getSpecificOptions().get(0).getClearShape().setDirection(Direction.SOUTH);
		EnumBasicStructureName.AdvancedHorseStable.baseOption.getSpecificOptions().get(0).getClearShape().setHeight(8);
		EnumBasicStructureName.AdvancedHorseStable.baseOption.getSpecificOptions().get(0).getClearShape().setWidth(17);
		EnumBasicStructureName.AdvancedHorseStable.baseOption.getSpecificOptions().get(0).getClearShape().setLength(34);
		EnumBasicStructureName.AdvancedHorseStable.baseOption.getSpecificOptions().get(0).getClearPositionOffset().setSouthOffset(1);
		EnumBasicStructureName.AdvancedHorseStable.baseOption.getSpecificOptions().get(0).getClearPositionOffset().setEastOffset(8);

		EnumBasicStructureName.Barn.baseOption.getSpecificOptions().get(0).getClearShape().setDirection(Direction.SOUTH);
		EnumBasicStructureName.Barn.baseOption.getSpecificOptions().get(0).getClearShape().setHeight(10);
		EnumBasicStructureName.Barn.baseOption.getSpecificOptions().get(0).getClearShape().setWidth(30);
		EnumBasicStructureName.Barn.baseOption.getSpecificOptions().get(0).getClearShape().setLength(35);
		EnumBasicStructureName.Barn.baseOption.getSpecificOptions().get(0).getClearPositionOffset().setSouthOffset(1);
		EnumBasicStructureName.Barn.baseOption.getSpecificOptions().get(0).getClearPositionOffset().setEastOffset(15);

		EnumBasicStructureName.MachineryTower.baseOption.getSpecificOptions().get(0).getClearShape().setDirection(Direction.SOUTH);
		EnumBasicStructureName.MachineryTower.baseOption.getSpecificOptions().get(0).getClearShape().setHeight(12);
		EnumBasicStructureName.MachineryTower.baseOption.getSpecificOptions().get(0).getClearShape().setWidth(16);
		EnumBasicStructureName.MachineryTower.baseOption.getSpecificOptions().get(0).getClearShape().setLength(16);
		EnumBasicStructureName.MachineryTower.baseOption.getSpecificOptions().get(0).getClearPositionOffset().setSouthOffset(1);
		EnumBasicStructureName.MachineryTower.baseOption.getSpecificOptions().get(0).getClearPositionOffset().setEastOffset(8);

		// Defense bunker.
		EnumBasicStructureName.DefenseBunker.baseOption.getSpecificOptions().get(0).getClearShape().setDirection(Direction.SOUTH);
		EnumBasicStructureName.DefenseBunker.baseOption.getSpecificOptions().get(0).getClearShape().setHeight(17);
		EnumBasicStructureName.DefenseBunker.baseOption.getSpecificOptions().get(0).getClearShape().setWidth(32);
		EnumBasicStructureName.DefenseBunker.baseOption.getSpecificOptions().get(0).getClearShape().setLength(32);
		EnumBasicStructureName.DefenseBunker.baseOption.getSpecificOptions().get(0).getClearPositionOffset().setSouthOffset(1);
		EnumBasicStructureName.DefenseBunker.baseOption.getSpecificOptions().get(0).getClearPositionOffset().setEastOffset(15);

		// Mineshaft entrance.
		EnumBasicStructureName.MineshaftEntrance.baseOption.getSpecificOptions().get(0).getClearShape().setDirection(Direction.SOUTH);
		EnumBasicStructureName.MineshaftEntrance.baseOption.getSpecificOptions().get(0).getClearShape().setHeight(6);
		EnumBasicStructureName.MineshaftEntrance.baseOption.getSpecificOptions().get(0).getClearShape().setWidth(7);
		EnumBasicStructureName.MineshaftEntrance.baseOption.getSpecificOptions().get(0).getClearShape().setLength(7);
		EnumBasicStructureName.MineshaftEntrance.baseOption.getSpecificOptions().get(0).getClearPositionOffset().setSouthOffset(1);
		EnumBasicStructureName.MineshaftEntrance.baseOption.getSpecificOptions().get(0).getClearPositionOffset().setEastOffset(3);

		// Ender Gateway.
		EnumBasicStructureName.EnderGateway.baseOption.getSpecificOptions().get(0).getClearShape().setDirection(Direction.SOUTH);
		EnumBasicStructureName.EnderGateway.baseOption.getSpecificOptions().get(0).getClearShape().setHeight(26);
		EnumBasicStructureName.EnderGateway.baseOption.getSpecificOptions().get(0).getClearShape().setWidth(17);
		EnumBasicStructureName.EnderGateway.baseOption.getSpecificOptions().get(0).getClearShape().setLength(17);
		EnumBasicStructureName.EnderGateway.baseOption.getSpecificOptions().get(0).getClearPositionOffset().setSouthOffset(1);
		EnumBasicStructureName.EnderGateway.baseOption.getSpecificOptions().get(0).getClearPositionOffset().setEastOffset(8);

		// Aqua Base.
		EnumBasicStructureName.AquaBase.baseOption.getSpecificOptions().get(0).getClearShape().setDirection(Direction.SOUTH);
		EnumBasicStructureName.AquaBase.baseOption.getSpecificOptions().get(0).getClearShape().setHeight(27);
		EnumBasicStructureName.AquaBase.baseOption.getSpecificOptions().get(0).getClearShape().setWidth(25);
		EnumBasicStructureName.AquaBase.baseOption.getSpecificOptions().get(0).getClearShape().setLength(38);
		EnumBasicStructureName.AquaBase.baseOption.getSpecificOptions().get(0).getClearPositionOffset().setSouthOffset(1);
		EnumBasicStructureName.AquaBase.baseOption.getSpecificOptions().get(0).getClearPositionOffset().setEastOffset(12);

		// Grassy Plain.
		EnumBasicStructureName.GrassyPlain.baseOption.getSpecificOptions().get(0).getClearShape().setDirection(Direction.SOUTH);
		EnumBasicStructureName.GrassyPlain.baseOption.getSpecificOptions().get(0).getClearShape().setHeight(4);
		EnumBasicStructureName.GrassyPlain.baseOption.getSpecificOptions().get(0).getClearShape().setWidth(15);
		EnumBasicStructureName.GrassyPlain.baseOption.getSpecificOptions().get(0).getClearShape().setLength(15);
		EnumBasicStructureName.GrassyPlain.baseOption.getSpecificOptions().get(0).getClearPositionOffset().setSouthOffset(1);
		EnumBasicStructureName.GrassyPlain.baseOption.getSpecificOptions().get(0).getClearPositionOffset().setEastOffset(8);
		EnumBasicStructureName.GrassyPlain.baseOption.getSpecificOptions().get(0).getClearPositionOffset().setHeightOffset(-1);

		// Magic Temple.
		EnumBasicStructureName.MagicTemple.baseOption.getSpecificOptions().get(0).getClearShape().setDirection(Direction.SOUTH);
		EnumBasicStructureName.MagicTemple.baseOption.getSpecificOptions().get(0).getClearShape().setHeight(13);
		EnumBasicStructureName.MagicTemple.baseOption.getSpecificOptions().get(0).getClearShape().setWidth(12);
		EnumBasicStructureName.MagicTemple.baseOption.getSpecificOptions().get(0).getClearShape().setLength(13);
		EnumBasicStructureName.MagicTemple.baseOption.getSpecificOptions().get(0).getClearPositionOffset().setSouthOffset(1);
		EnumBasicStructureName.MagicTemple.baseOption.getSpecificOptions().get(0).getClearPositionOffset().setEastOffset(6);

		// Greenhouse.
		EnumBasicStructureName.GreenHouse.baseOption.getSpecificOptions().get(0).getClearShape().setDirection(Direction.SOUTH);
		EnumBasicStructureName.GreenHouse.baseOption.getSpecificOptions().get(0).getClearShape().setHeight(10);
		EnumBasicStructureName.GreenHouse.baseOption.getSpecificOptions().get(0).getClearShape().setWidth(16);
		EnumBasicStructureName.GreenHouse.baseOption.getSpecificOptions().get(0).getClearShape().setLength(32);
		EnumBasicStructureName.GreenHouse.baseOption.getSpecificOptions().get(0).getClearPositionOffset().setSouthOffset(1);
		EnumBasicStructureName.GreenHouse.baseOption.getSpecificOptions().get(0).getClearPositionOffset().setEastOffset(8);

		// Watch Tower
		EnumBasicStructureName.WatchTower.baseOption.getSpecificOptions().get(0).getClearShape().setDirection(Direction.SOUTH);
		EnumBasicStructureName.WatchTower.baseOption.getSpecificOptions().get(0).getClearShape().setHeight(16);
		EnumBasicStructureName.WatchTower.baseOption.getSpecificOptions().get(0).getClearShape().setWidth(9);
		EnumBasicStructureName.WatchTower.baseOption.getSpecificOptions().get(0).getClearShape().setLength(9);
		EnumBasicStructureName.WatchTower.baseOption.getSpecificOptions().get(0).getClearPositionOffset().setSouthOffset(1);
		EnumBasicStructureName.WatchTower.baseOption.getSpecificOptions().get(0).getClearPositionOffset().setEastOffset(4);

		// Test
		/*
		 * EnumBasicStructureName.Test.getClearShape().setDirection(Direction.SOUTH);
		 * EnumBasicStructureName.Test.getClearShape().setHeight(4);
		 * EnumBasicStructureName.Test.getClearShape().setWidth(6);
		 * EnumBasicStructureName.Test.getClearShape().setLength(4);
		 * EnumBasicStructureName.Test.getClearPositionOffset().setSouthOffset(1);
		 * EnumBasicStructureName.Test.getClearPositionOffset().setWestOffset(1);
		 */

		// Welcome Center
		EnumBasicStructureName.WelcomeCenter.baseOption.getSpecificOptions().get(0).getClearShape().setDirection(Direction.SOUTH);
		EnumBasicStructureName.WelcomeCenter.baseOption.getSpecificOptions().get(0).getClearShape().setHeight(24);
		EnumBasicStructureName.WelcomeCenter.baseOption.getSpecificOptions().get(0).getClearShape().setWidth(19);
		EnumBasicStructureName.WelcomeCenter.baseOption.getSpecificOptions().get(0).getClearShape().setLength(48);
		EnumBasicStructureName.WelcomeCenter.baseOption.getSpecificOptions().get(0).getClearPositionOffset().setSouthOffset(1);
		EnumBasicStructureName.WelcomeCenter.baseOption.getSpecificOptions().get(0).getClearPositionOffset().setEastOffset(5);
		EnumBasicStructureName.WelcomeCenter.baseOption.getSpecificOptions().get(0).getClearPositionOffset().setHeightOffset(-5);

		// Jail
		EnumBasicStructureName.Jail.baseOption.getSpecificOptions().get(0).getClearShape().setDirection(Direction.SOUTH);
		EnumBasicStructureName.Jail.baseOption.getSpecificOptions().get(0).getClearShape().setHeight(14);
		EnumBasicStructureName.Jail.baseOption.getSpecificOptions().get(0).getClearShape().setWidth(28);
		EnumBasicStructureName.Jail.baseOption.getSpecificOptions().get(0).getClearShape().setLength(33);
		EnumBasicStructureName.Jail.baseOption.getSpecificOptions().get(0).getClearPositionOffset().setSouthOffset(1);
		EnumBasicStructureName.Jail.baseOption.getSpecificOptions().get(0).getClearPositionOffset().setEastOffset(25);
		EnumBasicStructureName.Jail.baseOption.getSpecificOptions().get(0).getClearPositionOffset().setHeightOffset(-3);

		// Saloon
		EnumBasicStructureName.Saloon.baseOption.getSpecificOptions().get(0).getClearShape().setDirection(Direction.SOUTH);
		EnumBasicStructureName.Saloon.baseOption.getSpecificOptions().get(0).getClearShape().setHeight(14);
		EnumBasicStructureName.Saloon.baseOption.getSpecificOptions().get(0).getClearShape().setWidth(18);
		EnumBasicStructureName.Saloon.baseOption.getSpecificOptions().get(0).getClearShape().setLength(16);
		EnumBasicStructureName.Saloon.baseOption.getSpecificOptions().get(0).getClearPositionOffset().setSouthOffset(1);
		EnumBasicStructureName.Saloon.baseOption.getSpecificOptions().get(0).getClearPositionOffset().setEastOffset(10);
		EnumBasicStructureName.Saloon.baseOption.getSpecificOptions().get(0).getClearPositionOffset().setHeightOffset(-1);

		// Ski Lodge
		EnumBasicStructureName.SkiLodge.baseOption.getSpecificOptions().get(0).getClearShape().setDirection(Direction.SOUTH);
		EnumBasicStructureName.SkiLodge.baseOption.getSpecificOptions().get(0).getClearShape().setHeight(25);
		EnumBasicStructureName.SkiLodge.baseOption.getSpecificOptions().get(0).getClearShape().setWidth(46);
		EnumBasicStructureName.SkiLodge.baseOption.getSpecificOptions().get(0).getClearShape().setLength(35);
		EnumBasicStructureName.SkiLodge.baseOption.getSpecificOptions().get(0).getClearPositionOffset().setSouthOffset(1);
		EnumBasicStructureName.SkiLodge.baseOption.getSpecificOptions().get(0).getClearPositionOffset().setEastOffset(20);
		EnumBasicStructureName.SkiLodge.baseOption.getSpecificOptions().get(0).getClearPositionOffset().setHeightOffset(-1);

		// Windmill
		EnumBasicStructureName.WindMill.baseOption.getSpecificOptions().get(0).getClearShape().setDirection(Direction.SOUTH);
		EnumBasicStructureName.WindMill.baseOption.getSpecificOptions().get(0).getClearShape().setHeight(31);
		EnumBasicStructureName.WindMill.baseOption.getSpecificOptions().get(0).getClearShape().setWidth(17);
		EnumBasicStructureName.WindMill.baseOption.getSpecificOptions().get(0).getClearShape().setLength(13);
		EnumBasicStructureName.WindMill.baseOption.getSpecificOptions().get(0).getClearPositionOffset().setSouthOffset(1);
		EnumBasicStructureName.WindMill.baseOption.getSpecificOptions().get(0).getClearPositionOffset().setEastOffset(8);
		EnumBasicStructureName.WindMill.baseOption.getSpecificOptions().get(0).getClearPositionOffset().setHeightOffset(-1);

		// Town Hall
		EnumBasicStructureName.TownHall.baseOption.getSpecificOptions().get(0).getClearShape().setDirection(Direction.SOUTH);
		EnumBasicStructureName.TownHall.baseOption.getSpecificOptions().get(0).getClearShape().setHeight(12);
		EnumBasicStructureName.TownHall.baseOption.getSpecificOptions().get(0).getClearShape().setWidth(27);
		EnumBasicStructureName.TownHall.baseOption.getSpecificOptions().get(0).getClearShape().setLength(27);
		EnumBasicStructureName.TownHall.baseOption.getSpecificOptions().get(0).getClearPositionOffset().setSouthOffset(1);
		EnumBasicStructureName.TownHall.baseOption.getSpecificOptions().get(0).getClearPositionOffset().setEastOffset(20);
		EnumBasicStructureName.TownHall.baseOption.getSpecificOptions().get(0).getClearPositionOffset().setHeightOffset(-1);

		// Nether Gate - Ancient Skull
		EnumBasicStructureName.NetherGate.baseOption.getSpecificOptions().get(0).getClearShape().setDirection(Direction.SOUTH);
		EnumBasicStructureName.NetherGate.baseOption.getSpecificOptions().get(0).getClearShape().setHeight(13);
		EnumBasicStructureName.NetherGate.baseOption.getSpecificOptions().get(0).getClearShape().setLength(26);
		EnumBasicStructureName.NetherGate.baseOption.getSpecificOptions().get(0).getClearShape().setWidth(15);
		EnumBasicStructureName.NetherGate.baseOption.getSpecificOptions().get(0).getClearPositionOffset().setSouthOffset(1);
		EnumBasicStructureName.NetherGate.baseOption.getSpecificOptions().get(0).getClearPositionOffset().setEastOffset(7);
		EnumBasicStructureName.NetherGate.baseOption.getSpecificOptions().get(0).getClearPositionOffset().setHeightOffset(-2);

		// Nether Gate - Corrupted Tree
		EnumBasicStructureName.NetherGate.baseOption.getSpecificOptions().get(1).getClearShape().setDirection(Direction.SOUTH);
		EnumBasicStructureName.NetherGate.baseOption.getSpecificOptions().get(1).getClearShape().setHeight(20);
		EnumBasicStructureName.NetherGate.baseOption.getSpecificOptions().get(1).getClearShape().setLength(16);
		EnumBasicStructureName.NetherGate.baseOption.getSpecificOptions().get(1).getClearShape().setWidth(16);
		EnumBasicStructureName.NetherGate.baseOption.getSpecificOptions().get(1).getClearPositionOffset().setSouthOffset(1);
		EnumBasicStructureName.NetherGate.baseOption.getSpecificOptions().get(1).getClearPositionOffset().setEastOffset(9);
		EnumBasicStructureName.NetherGate.baseOption.getSpecificOptions().get(1).getClearPositionOffset().setHeightOffset(-7);

		// Sugar Cane Farm
		EnumBasicStructureName.SugarCaneFarm.baseOption.getSpecificOptions().get(0).getClearShape().setDirection(Direction.SOUTH);
		EnumBasicStructureName.SugarCaneFarm.baseOption.getSpecificOptions().get(0).getClearShape().setHeight(9);
		EnumBasicStructureName.SugarCaneFarm.baseOption.getSpecificOptions().get(0).getClearShape().setLength(6);
		EnumBasicStructureName.SugarCaneFarm.baseOption.getSpecificOptions().get(0).getClearShape().setWidth(12);
		EnumBasicStructureName.SugarCaneFarm.baseOption.getSpecificOptions().get(0).getClearPositionOffset().setSouthOffset(1);
		EnumBasicStructureName.SugarCaneFarm.baseOption.getSpecificOptions().get(0).getClearPositionOffset().setEastOffset(10);
		EnumBasicStructureName.SugarCaneFarm.baseOption.getSpecificOptions().get(0).getClearPositionOffset().setHeightOffset(-1);

		// Advanced Aqua Base.
		EnumBasicStructureName.AdvancedAquaBase.baseOption.getSpecificOptions().get(0).getClearShape().setDirection(Direction.SOUTH);
		EnumBasicStructureName.AdvancedAquaBase.baseOption.getSpecificOptions().get(0).getClearShape().setHeight(27);
		EnumBasicStructureName.AdvancedAquaBase.baseOption.getSpecificOptions().get(0).getClearShape().setWidth(25);
		EnumBasicStructureName.AdvancedAquaBase.baseOption.getSpecificOptions().get(0).getClearShape().setLength(38);
		EnumBasicStructureName.AdvancedAquaBase.baseOption.getSpecificOptions().get(0).getClearPositionOffset().setSouthOffset(1);
		EnumBasicStructureName.AdvancedAquaBase.baseOption.getSpecificOptions().get(0).getClearPositionOffset().setEastOffset(12);
	}

	/**
	 * This field is used to contain the {@link EnumBasicStructureName} used by this instance.
	 */
	public EnumBasicStructureName basicStructureName;
	/**
	 * This field is used to contain the display name for the structure.
	 */
	public String structureDisplayName;

	public DyeColor bedColor;

	public BaseOption chosenOption;

	/**
	 * Initializes a new instance of the BasicStructureConfiguration class.
	 */
	public BasicStructureConfiguration() {
		super();
	}

	/**
	 * Gets the display name for this structure.
	 *
	 * @return The unlocalized display name for this structure
	 */
	public String getDisplayName() {
		if (this.basicStructureName == EnumBasicStructureName.Custom) {
			return this.structureDisplayName;
		} else {
			return this.basicStructureName.getUnlocalizedName();
		}
	}

	/**
	 * Determines if this is a custom structure.
	 *
	 * @return A value indicating whether this is a custom structure.
	 */
	public boolean IsCustomStructure() {
		return this.basicStructureName == EnumBasicStructureName.Custom;
	}

	@Override
	public void Initialize() {
		super.Initialize();
		this.houseFacing = Direction.NORTH;
		this.basicStructureName = EnumBasicStructureName.AdvancedCoop;
		this.bedColor = DyeColor.RED;
		this.chosenOption = this.basicStructureName.baseOption.getSpecificOptions().get(0);
	}

	@Override
	protected void CustomReadFromNBTTag(CompoundNBT messageTag, StructureConfiguration config) {
		BasicStructureConfiguration basicConfig = (BasicStructureConfiguration) config;

		if (messageTag.contains(BasicStructureConfiguration.structureEnumNameTag)) {
			basicConfig.basicStructureName = EnumBasicStructureName.valueOf(messageTag.getString(BasicStructureConfiguration.structureEnumNameTag));
		}

		if (messageTag.contains(BasicStructureConfiguration.structureDisplayNameTag)) {
			basicConfig.structureDisplayName = messageTag.getString(BasicStructureConfiguration.structureDisplayNameTag);
		}

		if (messageTag.contains(BasicStructureConfiguration.bedColorTag)) {
			basicConfig.bedColor = DyeColor.valueOf(messageTag.getString(BasicStructureConfiguration.bedColorTag));
		}

		if (messageTag.contains(BasicStructureConfiguration.chosenOptionTag)) {
			basicConfig.chosenOption = basicConfig.basicStructureName.baseOption.getOptionByTranslationString(messageTag.getString(BasicStructureConfiguration.chosenOptionTag));
		}
	}

	@Override
	protected CompoundNBT CustomWriteToCompoundNBT(CompoundNBT tag) {
		tag.putString(BasicStructureConfiguration.structureEnumNameTag, this.basicStructureName.name());

		if (this.structureDisplayName != null) {
			tag.putString(BasicStructureConfiguration.structureDisplayNameTag, this.structureDisplayName);
		}

		tag.putString(BasicStructureConfiguration.bedColorTag, this.bedColor.getSerializedName().toUpperCase());
		tag.putString(BasicStructureConfiguration.chosenOptionTag, this.chosenOption.getTranslationString());

		return tag;
	}

	/**
	 * Reads information from an NBTTagCompound.
	 *
	 * @param messageTag The tag to read the data from.
	 * @return An instance of {@link BasicStructureConfiguration} with vaules pulled from the NBTTagCompound.
	 */
	@Override
	public BasicStructureConfiguration ReadFromCompoundNBT(CompoundNBT messageTag) {
		BasicStructureConfiguration config = new BasicStructureConfiguration();

		return (BasicStructureConfiguration) super.ReadFromCompoundNBT(messageTag, config);
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
		String assetLocation = "";

		if (!this.IsCustomStructure()) {
			assetLocation = this.chosenOption.getAssetLocation();
		}

		StructureBasic structure = StructureBasic.CreateInstance(assetLocation, StructureBasic.class);

		if (structure.BuildStructure(this, world, hitBlockPos, Direction.NORTH, player)) {
			ItemStack stack = ItemBasicStructure.getBasicStructureItemInHand(player);

			if (stack.getCount() == 1) {
				player.inventory.removeItem(stack);
			} else {
				stack.setCount(stack.getCount() - 1);
			}

			player.containerMenu.broadcastChanges();
		}
	}

	/**
	 * This enum is used to list the names of the basic structures and provide other information necessary.
	 *
	 * @author WuestMan
	 */
	@SuppressWarnings("SpellCheckingInspection")
	public enum EnumBasicStructureName {
		Custom("custom", null, null, null),
		AdvancedCoop("advancedcoop", "item.prefab.advanced.chicken.coop", "item_advanced_chicken_coop", AdvancedCoopOptions.Default),
		AdvancedHorseStable("advanced_horse_stable", "item.prefab.advanced.horse.stable", "item_advanced_horse_stable", AdvancedHorseStableOptions.Default),
		Barn("barn", "item.prefab.barn", "item_barn", BarnOptions.Default),
		MachineryTower("machinery_tower", "item.prefab.machinery.tower", "item_machinery_tower", MachineryTowerOptions.Default),
		DefenseBunker("defense_bunker", "item.prefab.defense.bunker", "item_defense_bunker", DefenseBunkerOptions.Default),
		MineshaftEntrance("mineshaft_entrance", "item.prefab.mineshaft.entrance", "item_mineshaft_entrance", MineshaftEntranceOptions.Default),
		EnderGateway("ender_gateway", "item.prefab.ender_gateway", "item_ender_gateway", EnderGatewayOptions.Default),
		AquaBase("aqua_base", "item.prefab.aqua_base", "item_aqua_base", AquaBaseOptions.Default),
		GrassyPlain("grassy_plain", "item.prefab.grassy_plain", "item_grassy_plain", GrassyPlainOptions.Default),
		MagicTemple("magic_temple", "item.prefab.magic_temple", "item_magic_temple", MagicTempleOptions.Default),
		GreenHouse("green_house", "item.prefab.green_house", "item_green_house", GreenHouseOptions.Default),
		WatchTower("watch_tower", "item.prefab.watch_tower", "item_watch_tower", WatchTowerOptions.Default),
		/*
		 * Test("test", "item.prefab.test", "assets/prefab/structures/test.zip", "textures/gui/watch_tower_topdown.png",
		 * "item_test", 176, 133),
		 */
		WelcomeCenter("welcome_center", "item.prefab.welcome_center", "item_welcome_center", WelcomeCenterOptions.Default),
		Jail("jail", "item.prefab.jail", "item_jail", JailOptions.Default),
		Saloon("saloon", "item.prefab.saloon", "item_saloon", SaloonOptions.Default),
		SkiLodge("ski_lodge", "item.prefab.ski_lodge", "item_ski_lodge", SkiLodgeOptions.Default),
		WindMill("wind_mill", "item.prefab.wind_mill", "item_wind_mill", WindMillOptions.Default),
		TownHall("town_hall", "item.prefab.town_hall", "item_town_hall", TownHallOptions.Default),
		NetherGate("nether_gate", "item.prefab.nether_gate", "item_nether_gate", NetherGateOptions.AncientSkull),
		SugarCaneFarm("sugar_cane_farm", "item.prefab.sugar_cane_farm", "item_sugar_cane_farm", SugarCaneFarmOptions.Default),
		AdvancedAquaBase("advanced_aqua_base", "item.prefab.advanced_aqua_base", "item_advanced_aqua_base", AdvancedAquaBaseOptions.Default);

		private String name;
		private String unlocalizedName;
		private ResourceLocation resourceLocation;
		private BaseOption baseOption;

		/**
		 * This is a basic structure which doesn't have any (or limited) custom processing.
		 *
		 * @param name             - This is the name for this structure. This is used for comparative purposes in
		 *                         item stacks.
		 * @param unlocalizedName  - This is the localization key to determine the displayed name to the user.
		 * @param resourceLocation - This is the resource location for the item's texture when it's in the players
		 *                         and or in inventories/the world.
		 */
		EnumBasicStructureName(String name, String unlocalizedName, String resourceLocation, BaseOption baseOption) {
			this.name = name;
			this.unlocalizedName = unlocalizedName;

			if (resourceLocation != null) {
				this.resourceLocation = new ResourceLocation("prefab", resourceLocation);
			}

			this.baseOption = baseOption;
		}

		/**
		 * The enum name.
		 *
		 * @return The enum name.
		 */
		public String getName() {
			return this.name;
		}

		/**
		 * The unlocalized name.
		 *
		 * @return The unlocalized name for this structure.
		 */
		public String getUnlocalizedName() {
			return this.unlocalizedName;
		}

		/**
		 * This is the resource location for the item's texture when it's in the players and or in inventories/the
		 * world.
		 *
		 * @return The resource location for the item texture.
		 */
		public ResourceLocation getResourceLocation() {
			return this.resourceLocation;
		}

		public BaseOption getBaseOption() {
			return this.baseOption;
		}
	}
}
