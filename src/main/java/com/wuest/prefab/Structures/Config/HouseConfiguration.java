package com.wuest.prefab.Structures.Config;

import java.util.ArrayList;

import com.wuest.prefab.ModRegistry;
import com.wuest.prefab.Prefab;
import com.wuest.prefab.Config.EntityPlayerConfiguration;
import com.wuest.prefab.Config.ModConfiguration.CeilingFloorBlockType;
import com.wuest.prefab.Config.ModConfiguration.WallBlockType;
import com.wuest.prefab.Gui.GuiLangKeys;
import com.wuest.prefab.Proxy.Messages.PlayerEntityTagMessage;
import com.wuest.prefab.Structures.Base.BuildingMethods;
import com.wuest.prefab.Structures.Predefined.StructureAlternateStart;

import net.minecraft.block.BedBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.DoorBlock;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.block.StandingSignBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.properties.BedPart;
import net.minecraft.tileentity.FurnaceTileEntity;
import net.minecraft.tileentity.SignTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.ServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.fml.network.NetworkDirection;

/**
 * This class is used to determine the configuration for a particular house.
 * 
 * @author WuestMan
 *
 */
public class HouseConfiguration extends StructureConfiguration
{
	protected static BlockPos NorthEastCorner;
	protected static BlockPos SouthEastCorner;
	protected static BlockPos SouthWestCorner;
	protected static BlockPos NorthWestCorner;

	private static String addTorchesTag = "addTorches";
	private static String addBedTag = "addBed";
	private static String addCraftingTableTag = "addCraftingTable";
	private static String addFurnaceTag = "addFurnace";
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
	public boolean addFurnace;
	public boolean addChest;
	public boolean addChestContents;
	public boolean addFarm;
	public CeilingFloorBlockType floorBlock;
	public CeilingFloorBlockType ceilingBlock;
	public WallBlockType wallWoodType;
	public boolean isCeilingFlat;
	public boolean addMineShaft;
	public HouseStyle houseStyle;
	public DyeColor glassColor;

	/**
	 * When the house is facing north, this would be the east/west direction.
	 */
	public int houseWidth;

	/**
	 * When the house if facing north, this would be the north/south direction.
	 */
	public int houseDepth;

	/**
	 * Initializes a new instance of the {@link HouseConfiguration} class.
	 */
	public HouseConfiguration()
	{
		super();
	}

	@Override
	public void Initialize()
	{
		super.Initialize();
		this.houseStyle = HouseStyle.BASIC;
		this.glassColor = DyeColor.CYAN;
		this.floorBlock = CeilingFloorBlockType.StoneBrick;
		this.ceilingBlock = CeilingFloorBlockType.StoneBrick;
		this.wallWoodType = WallBlockType.Oak;
		this.houseDepth = 9;
		this.houseWidth = 9;
		this.addTorches = true;
		this.addBed = true;
		this.addCraftingTable = true;
		this.addFurnace = true;
		this.addChest = true;
		this.addChestContents = true;
		this.addFarm = true;
		this.isCeilingFlat = false;
		this.addMineShaft = true;
	}

	@Override
	public CompoundNBT WriteToCompoundNBT()
	{
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
		tag.putBoolean(HouseConfiguration.addFarmTag, this.addFarm);
		tag.putInt(HouseConfiguration.floorBlockTag, this.floorBlock.getValue());
		tag.putInt(HouseConfiguration.ceilingBlockTag, this.ceilingBlock.getValue());
		tag.putInt(HouseConfiguration.wallWoodTypeTag, this.wallWoodType.getValue());
		tag.putBoolean(HouseConfiguration.isCeilingFlatTag, this.isCeilingFlat);
		tag.putBoolean(HouseConfiguration.addMineShaftTag, this.addMineShaft);
		tag.putInt(HouseConfiguration.hitXTag, this.pos.getX());
		tag.putInt(HouseConfiguration.hitYTag, this.pos.getY());
		tag.putInt(HouseConfiguration.hitZTag, this.pos.getZ());
		tag.putInt(HouseConfiguration.houseDepthTag, this.houseDepth);
		tag.putInt(HouseConfiguration.houseWidthTag, this.houseWidth);
		tag.putString(HouseConfiguration.houseFacingTag, this.houseFacing.getName());
		tag.putInt(HouseConfiguration.houseStyleTag, this.houseStyle.value);
		tag.putString(HouseConfiguration.glassColorTag, this.glassColor.getName().toUpperCase());

		return tag;
	}

	/**
	 * Gets the name used in a text slider.
	 * 
	 * @param name  The name of the option to get.
	 * @param value The integer value to associate with the name.
	 * @return A string representing the value to show in the text slider.
	 */
	public static String GetIntegerOptionStringValue(String name, int value)
	{
		if (name.equals(GuiLangKeys.STARTER_HOUSE_CEILING_TYPE)
			|| name.equals(GuiLangKeys.STARTER_HOUSE_FLOOR_STONE))
		{
			return " - " + CeilingFloorBlockType.ValueOf(value).getName();
		}
		else if (name.equals(GuiLangKeys.STARTER_HOUSE_WALL_TYPE))
		{
			return " - " + WallBlockType.ValueOf(value).getName();
		}

		return "";
	}

	/**
	 * Custom method to read the CompoundNBT message.
	 * 
	 * @param tag The message to create the configuration from.
	 * @return An new configuration object with the values derived from the CompoundNBT.
	 */
	@Override
	public HouseConfiguration ReadFromCompoundNBT(CompoundNBT tag)
	{
		HouseConfiguration config = null;

		if (tag != null)
		{
			config = new HouseConfiguration();

			if (tag.contains(HouseConfiguration.addTorchesTag))
			{
				config.addTorches = tag.getBoolean(HouseConfiguration.addTorchesTag);
			}

			if (tag.contains(HouseConfiguration.addBedTag))
			{
				config.addBed = tag.getBoolean(HouseConfiguration.addBedTag);
			}

			if (tag.contains(HouseConfiguration.addCraftingTableTag))
			{
				config.addCraftingTable = tag.getBoolean(HouseConfiguration.addCraftingTableTag);
			}

			if (tag.contains(HouseConfiguration.addFurnaceTag))
			{
				config.addFurnace = tag.getBoolean(HouseConfiguration.addFurnaceTag);
			}

			if (tag.contains(HouseConfiguration.addChestTag))
			{
				config.addChest = tag.getBoolean(HouseConfiguration.addChestTag);
			}

			if (tag.contains(HouseConfiguration.addChestContentsTag))
			{
				config.addChestContents = tag.getBoolean(HouseConfiguration.addChestContentsTag);
			}

			if (tag.contains(HouseConfiguration.addFarmTag))
			{
				config.addFarm = tag.getBoolean(HouseConfiguration.addFarmTag);
			}

			if (tag.contains(HouseConfiguration.floorBlockTag))
			{
				config.floorBlock = CeilingFloorBlockType.ValueOf(tag.getInt(HouseConfiguration.floorBlockTag));
			}

			if (tag.contains(HouseConfiguration.ceilingBlockTag))
			{
				config.ceilingBlock = CeilingFloorBlockType.ValueOf(tag.getInt(HouseConfiguration.ceilingBlockTag));
			}

			if (tag.contains(HouseConfiguration.wallWoodTypeTag))
			{
				config.wallWoodType = WallBlockType.ValueOf(tag.getInt(HouseConfiguration.wallWoodTypeTag));
			}

			if (tag.contains(HouseConfiguration.isCeilingFlatTag))
			{
				config.isCeilingFlat = tag.getBoolean(HouseConfiguration.isCeilingFlatTag);
			}

			if (tag.contains(HouseConfiguration.addMineShaftTag))
			{
				config.addMineShaft = tag.getBoolean(HouseConfiguration.addMineShaftTag);
			}

			if (tag.contains(HouseConfiguration.hitXTag))
			{
				config.pos = new BlockPos(tag.getInt(HouseConfiguration.hitXTag), tag.getInt(HouseConfiguration.hitYTag), tag.getInt(HouseConfiguration.hitZTag));
			}

			if (tag.contains(HouseConfiguration.houseDepthTag))
			{
				config.houseDepth = tag.getInt(HouseConfiguration.houseDepthTag);
			}

			if (tag.contains(HouseConfiguration.houseWidthTag))
			{
				config.houseWidth = tag.getInt(HouseConfiguration.houseWidthTag);
			}

			if (tag.contains(HouseConfiguration.houseFacingTag))
			{
				config.houseFacing = Direction.byName(tag.getString(HouseConfiguration.houseFacingTag));
			}

			if (tag.contains(HouseConfiguration.houseStyleTag))
			{
				config.houseStyle = HouseStyle.ValueOf(tag.getInt(HouseConfiguration.houseStyleTag));
			}

			if (tag.contains(HouseConfiguration.glassColorTag))
			{
				config.glassColor = DyeColor.valueOf(tag.getString(HouseConfiguration.glassColorTag));
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
	protected void ConfigurationSpecificBuildStructure(PlayerEntity player, ServerWorld world, BlockPos hitBlockPos)
	{
		boolean houseBuilt = true;

		if (this.houseStyle == HouseConfiguration.HouseStyle.BASIC)
		{
			// We hit a block, let's start building!!!!!
			BlockPos startingPosition = hitBlockPos.up();

			// Get the new "North" facing. This is the orientation of
			// the house and all building will be based on this.
			Direction northFace = this.houseFacing;

			// Get the "South" facing of the house to make rotating
			// easier.
			Direction southFace = northFace.getOpposite();

			// Set the "North East" corner.
			/*
			 * ItemStartHouse.NorthEastCorner = startingPosition.offset(northFace, (int)
			 * Math.floor(configuration.houseDepth / 2) + 1) .offset(northFace.rotateY(), (int)
			 * Math.floor(configuration.houseWidth / 2) + 1);
			 */
			HouseConfiguration.NorthEastCorner = startingPosition.offset(southFace).offset(northFace.rotateY());

			// Set the "South East" corner.
			HouseConfiguration.SouthEastCorner = HouseConfiguration.NorthEastCorner.offset(southFace, this.houseDepth + 1);

			// Set the "South West" corner.
			HouseConfiguration.SouthWestCorner = HouseConfiguration.SouthEastCorner.offset(northFace.rotateYCCW(), this.houseWidth + 1);

			// Set the "North West" corner.
			HouseConfiguration.NorthWestCorner = HouseConfiguration.NorthEastCorner.offset(northFace.rotateYCCW(), this.houseWidth + 1);

			// Put the starting position in the middle of the house as that's what the rest of the methods expect.
			startingPosition = HouseConfiguration.NorthEastCorner.offset(southFace, (int) Math.floor(this.houseDepth / 2) + 1)
				.offset(northFace.rotateYCCW(), (int) Math.floor(this.houseWidth / 2) + 1);

			BlockPos endBlockPos = startingPosition.offset(northFace.rotateYCCW(), this.houseWidth + 11)
				.offset(southFace, this.houseDepth + 11)
				.offset(Direction.UP, 15);

			// Make sure this structure can be placed here.
			if (!BuildingMethods.CheckBuildSpaceForAllowedBlockReplacement(this, world, startingPosition, endBlockPos, player))
			{
				// Send a message to the player saying that the structure could not
				// be built.
				player.sendMessage(new TranslationTextComponent(GuiLangKeys.GUI_STRUCTURE_NOBUILD).setStyle(new Style().setColor(TextFormatting.GREEN)));
				return;
			}

			// Clear the space before the user is teleported. This
			// is in-case they right-click on a space that is only 1
			// block tall.
			BuildingMethods.ClearSpace(world, HouseConfiguration.NorthEastCorner, this.houseWidth, 15, this.houseDepth, northFace);

			// Build the basic structure.
			HouseConfiguration.BuildStructure(world, startingPosition, this, northFace);

			// Build the interior.
			HouseConfiguration.BuildInterior(world, startingPosition, player, this, northFace);

			// Set up the exterior.
			HouseConfiguration.BuildExterior(world, startingPosition, player, this, northFace);

			if (this.addMineShaft && startingPosition.getY() > 15)
			{
				// Set up the mineshaft.
				HouseConfiguration.PlaceMineShaft(world, startingPosition, this.houseDepth, northFace);
			}

			houseBuilt = true;
		}
		else
		{
			// Build the alternate starter house instead.
			StructureAlternateStart structure = StructureAlternateStart.CreateInstance(this.houseStyle.getStructureLocation(), StructureAlternateStart.class);
			houseBuilt = structure.BuildStructure(this, world, hitBlockPos, Direction.NORTH, player);
		}

		// The house was successfully built, remove the item from the inventory.
		if (houseBuilt)
		{
			EntityPlayerConfiguration playerConfig = EntityPlayerConfiguration.loadFromEntityData((ServerPlayerEntity) player);
			playerConfig.builtStarterHouse = true;
			playerConfig.saveToPlayer(player);

			this.RemoveStructureItemFromPlayer(player, ModRegistry.StartHouse());

			// Make sure to send a message to the client to sync up the server player information and the client player
			// information.
			Prefab.network.sendTo(new PlayerEntityTagMessage(playerConfig.getModIsPlayerNewTag(player)), ((ServerPlayerEntity) player).connection.netManager,
				NetworkDirection.PLAY_TO_CLIENT);
		}
	}

	private static void BuildStructure(ServerWorld world, BlockPos startingPosition, HouseConfiguration configuration, Direction facing)
	{
		// Make sure that the area beneath the house is all there. Don't want
		// the house to be hanging in the air.
		BlockPos pos = HouseConfiguration.NorthEastCorner;

		BuildingMethods.SetFloor(world, pos.down(2), Blocks.DIRT, configuration.houseWidth + 2, configuration.houseDepth + 2, new ArrayList<ItemStack>(),
			facing.getOpposite(), null);

		Block floor = null;

		switch (configuration.floorBlock)
		{
			case Brick:
			{
				floor = Blocks.BRICKS;
				break;
			}

			case SandStone:
			{
				floor = Blocks.SANDSTONE;
				break;
			}

			default:
			{
				floor = Blocks.STONE_BRICKS;
				break;
			}
		}

		// Create the floor.
		BuildingMethods.SetFloor(world, pos.down(), floor, configuration.houseWidth + 2, configuration.houseDepth + 2, new ArrayList<ItemStack>(),
			facing.getOpposite(), null);

		// Create the walls.
		HouseConfiguration.SetWalls(world, configuration.wallWoodType.getBlockState(), configuration, facing);

		Block ceiling = null;
		Block stairs = null;

		switch (configuration.ceilingBlock)
		{
			case Brick:
			{
				ceiling = Blocks.BRICKS;
				stairs = Blocks.BRICK_STAIRS;
				break;
			}

			case SandStone:
			{
				ceiling = Blocks.SANDSTONE;
				stairs = Blocks.SANDSTONE_STAIRS;
				break;
			}

			default:
			{
				ceiling = Blocks.STONE_BRICKS;
				stairs = Blocks.STONE_BRICK_STAIRS;
				break;
			}
		}

		// Set the ceiling.
		pos = pos.up(4);
		BuildingMethods.SetCeiling(world, pos, ceiling, configuration.houseWidth + 2, configuration.houseDepth + 2, stairs, configuration, facing, null);
	}

	private static void BuildInterior(World world, BlockPos startingPosition, PlayerEntity player, HouseConfiguration configuration, Direction facing)
	{
		// Keep the corner positions since they are important.
		BlockPos northEastCornerPosition = HouseConfiguration.NorthEastCorner.up();
		BlockPos southEastCornerPosition = HouseConfiguration.SouthEastCorner.up();
		BlockPos northWestCornerPosition = HouseConfiguration.NorthWestCorner.up();
		BlockPos southWestCornerPosition = HouseConfiguration.SouthWestCorner.up();

		// Use a separate position for each item.
		BlockPos itemPosition = northEastCornerPosition;

		// Set the torch locations so it's not dark in the house.
		HouseConfiguration.PlaceInsideTorches(world, configuration, facing);

		// Create an oak door in the north east corner
		HouseConfiguration.DecorateDoor(world, northEastCornerPosition, player, configuration, facing);

		if (configuration.addBed)
		{
			// Place a bed in the north west corner.
			HouseConfiguration.PlaceBed(world, northWestCornerPosition, facing);
		}

		// Place a crafting table in the south west corner.
		HouseConfiguration.PlaceAndFillCraftingMachines(player, world, southWestCornerPosition, facing, configuration.addCraftingTable, configuration.addFurnace);

		if (configuration.addChest)
		{
			// Place a double chest in the south east corner.
			HouseConfiguration.PlaceAndFillChest(player, world, southEastCornerPosition, configuration, facing);
		}
	}

	private static void BuildExterior(World world, BlockPos startingPosition, PlayerEntity player, HouseConfiguration configuration, Direction facing)
	{
		// Keep the corner positions since they are important.
		// These positions are level with 1 block above the floor.
		BlockPos northEastCornerPosition = HouseConfiguration.NorthEastCorner.up();
		BlockPos southEastCornerPosition = HouseConfiguration.SouthEastCorner.up();
		BlockPos northWestCornerPosition = HouseConfiguration.NorthWestCorner.up();
		BlockPos southWestCornerPosition = HouseConfiguration.SouthWestCorner.up();

		HouseConfiguration.PlaceOutsideTorches(world, northEastCornerPosition, configuration, facing);

		if (configuration.addFarm)
		{
			HouseConfiguration.PlaceSmallFarm(world, northEastCornerPosition.down(), facing);
		}
	}

	private static void SetWalls(ServerWorld world, BlockState block, HouseConfiguration configuration, Direction facing)
	{
		// Get the north east corner.
		BlockPos pos = HouseConfiguration.NorthEastCorner;

		facing = facing.getOpposite();

		// East Wall.
		BuildingMethods.CreateWall(world, 4, configuration.houseDepth + 1, facing, pos, block);

		facing = facing.rotateY();

		// South Wall.
		pos = HouseConfiguration.SouthEastCorner;
		BuildingMethods.CreateWall(world, 4, configuration.houseWidth + 2, facing, pos, block);

		facing = facing.rotateY();

		// West Wall.
		pos = HouseConfiguration.SouthWestCorner;
		BuildingMethods.CreateWall(world, 4, configuration.houseDepth + 2, facing, pos, block);

		facing = facing.rotateY();

		// North Wall.
		pos = HouseConfiguration.NorthWestCorner;
		BuildingMethods.CreateWall(world, 4, configuration.houseWidth + 2, facing, pos, block);
	}

	private static void PlaceInsideTorches(World world, HouseConfiguration configuration, Direction facing)
	{
		// Use a separate position for each item.
		BlockPos itemPosition = null;
		int torchWidthOffset = configuration.houseWidth < 9 ? 2 : 4;
		int torchDepthOffset = configuration.houseDepth < 9 ? 2 : 4;

		/*
		 * Torch Facings 1 = East 2 = West 3 = South 4 = North 5 = Up
		 */

		// North Wall torches.
		itemPosition = HouseConfiguration.NorthEastCorner.offset(facing.rotateYCCW(), torchWidthOffset).offset(facing.getOpposite()).up();
		BlockState blockState = Blocks.TORCH.getDefaultState();
		BuildingMethods.ReplaceBlock(world, itemPosition, blockState);

		int blocksMoved = torchWidthOffset;

		while (blocksMoved < configuration.houseWidth)
		{
			itemPosition = itemPosition.offset(facing.rotateYCCW(), torchWidthOffset);

			if (world.isAirBlock(itemPosition) && !world.isAirBlock(itemPosition.offset(facing)))
			{
				BuildingMethods.ReplaceBlock(world, itemPosition, blockState);
			}

			blocksMoved += torchWidthOffset;
		}

		// East wall torches.
		itemPosition = HouseConfiguration.NorthEastCorner.offset(facing.getOpposite(), torchDepthOffset).offset(facing.rotateYCCW()).up();
		blockState = Blocks.TORCH.getDefaultState();
		BuildingMethods.ReplaceBlock(world, itemPosition, blockState);

		blocksMoved = torchDepthOffset;

		while (blocksMoved < configuration.houseDepth)
		{
			itemPosition = itemPosition.offset(facing.getOpposite(), torchDepthOffset);

			if (world.isAirBlock(itemPosition) && !world.isAirBlock(itemPosition.offset(facing.rotateY())))
			{
				BuildingMethods.ReplaceBlock(world, itemPosition, blockState);
			}

			blocksMoved += torchDepthOffset;
		}

		// West wall torches.
		itemPosition = HouseConfiguration.NorthWestCorner.offset(facing.getOpposite(), torchDepthOffset).offset(facing.rotateY()).up();
		blockState = Blocks.TORCH.getDefaultState();
		BuildingMethods.ReplaceBlock(world, itemPosition, blockState);

		blocksMoved = torchDepthOffset;

		while (blocksMoved < configuration.houseDepth)
		{
			itemPosition = itemPosition.offset(facing.getOpposite(), torchDepthOffset);

			if (world.isAirBlock(itemPosition) && !world.isAirBlock(itemPosition.offset(facing.rotateYCCW())))
			{
				BuildingMethods.ReplaceBlock(world, itemPosition, blockState);
			}

			blocksMoved += torchDepthOffset;
		}

		// South wall torches.
		itemPosition = HouseConfiguration.SouthEastCorner.offset(facing.rotateYCCW(), torchWidthOffset).offset(facing).up();
		blockState = Blocks.TORCH.getDefaultState();
		BuildingMethods.ReplaceBlock(world, itemPosition, blockState);

		blocksMoved = torchDepthOffset;

		while (blocksMoved < configuration.houseWidth)
		{
			itemPosition = itemPosition.offset(facing.rotateYCCW(), torchDepthOffset);

			if (world.isAirBlock(itemPosition) && !world.isAirBlock(itemPosition.offset(facing.getOpposite())))
			{
				BuildingMethods.ReplaceBlock(world, itemPosition, blockState);
			}

			blocksMoved += torchDepthOffset;
		}
	}

	private static void DecorateDoor(World world, BlockPos cornerPosition, PlayerEntity player, HouseConfiguration configuration, Direction facing)
	{
		BlockPos itemPosition = cornerPosition.offset(facing.rotateYCCW());

		world.removeBlock(itemPosition, false);

		DoorBlock door = null;
		Block stairs = null;

		switch (configuration.wallWoodType)
		{
			case Spruce:
			{
				door = (DoorBlock) Blocks.SPRUCE_DOOR;
				stairs = Blocks.SPRUCE_STAIRS;
				break;
			}

			case Birch:
			{
				door = (DoorBlock) Blocks.BIRCH_DOOR;
				stairs = Blocks.BIRCH_STAIRS;
				break;
			}

			case Jungle:
			{
				door = (DoorBlock) Blocks.JUNGLE_DOOR;
				stairs = Blocks.JUNGLE_STAIRS;
				break;
			}

			case Acacia:
			{
				door = (DoorBlock) Blocks.ACACIA_DOOR;
				stairs = Blocks.ACACIA_STAIRS;
				break;
			}

			case DarkOak:
			{
				door = (DoorBlock) Blocks.DARK_OAK_DOOR;
				stairs = Blocks.DARK_OAK_STAIRS;
				break;
			}

			default:
			{
				door = (DoorBlock) Blocks.OAK_DOOR;
				stairs = Blocks.OAK_STAIRS;
				break;
			}
		}

		// Set the door.
		BuildingMethods.ReplaceBlock(world, itemPosition.down(), door);

		world.removeBlock(itemPosition, false);
		door.onBlockPlacedBy(world, itemPosition, door.getDefaultState(), player, null);

		// Put a glass pane above the door.
		BuildingMethods.ReplaceBlock(world, itemPosition.up(), Blocks.GLASS_PANE);

		// Create a pressure plate for the door, no need to re-set the item
		// position here since it needs to be in relation to the door.
		itemPosition = itemPosition.offset(facing.getOpposite()).down();
		BuildingMethods.ReplaceBlock(world, itemPosition, Blocks.OAK_PRESSURE_PLATE);

		// Place a sign.
		itemPosition = itemPosition.offset(facing, 2).offset(facing.rotateYCCW());
		StandingSignBlock sign = (StandingSignBlock) Blocks.OAK_SIGN;
		int signMeta = 8;

		switch (facing)
		{
			case EAST:
			{
				signMeta = 12;
				break;
			}

			case SOUTH:
			{
				signMeta = 0;
				break;
			}

			case WEST:
			{
				signMeta = 4;
				break;
			}
			default:
			{
				break;
			}
		}

		// Make sure that there is dirt under the sign.
		BuildingMethods.ReplaceBlock(world, itemPosition.down(), Blocks.DIRT);
		BuildingMethods.ReplaceBlock(world, itemPosition, sign.getDefaultState().with(StandingSignBlock.ROTATION, signMeta));

		TileEntity tileEntity = world.getTileEntity(itemPosition);

		if (tileEntity instanceof SignTileEntity)
		{
			SignTileEntity signTile = (SignTileEntity) tileEntity;
			signTile.signText[0] = new StringTextComponent("This is");
			String playerName = player.getDisplayName().getString();

			if (playerName.length() >= 15)
			{
				signTile.signText[1] = new StringTextComponent(playerName);
			}
			else
			{
				signTile.signText[1] = new StringTextComponent(playerName + "'s");
			}

			signTile.signText[2] = new StringTextComponent("house!");
		}
	}

	private static void PlaceBed(World world, BlockPos cornerPosition, Direction facing)
	{
		// This is the "north west" corner.
		BlockPos itemPosition = cornerPosition.offset(facing.rotateY(), 1).offset(facing.getOpposite(), 2).down();

		BlockState bedFootState = Blocks.RED_BED.getDefaultState().with(HorizontalBlock.HORIZONTAL_FACING, facing)
			.with(BedBlock.OCCUPIED, Boolean.valueOf(false)).with(BedBlock.PART, BedPart.FOOT);

		if (world.setBlockState(itemPosition, bedFootState, 3))
		{
			BlockState bedHeadState = bedFootState.with(BedBlock.PART, BedPart.HEAD);
			world.setBlockState(itemPosition.offset(facing), bedHeadState, 3);
		}

	}

	private static void PlaceAndFillChest(PlayerEntity player, World world, BlockPos cornerPosition, HouseConfiguration configuration, Direction facing)
	{
		// Create a double wide chest.
		BlockPos itemPosition = cornerPosition.offset(facing).offset(facing.rotateYCCW()).down();
		BlockState chestState = Blocks.CHEST.getDefaultState().with(ChestBlock.FACING, facing);
		BuildingMethods.ReplaceBlock(world, itemPosition, chestState);

		itemPosition = itemPosition.offset(facing.rotateYCCW());
		BuildingMethods.ReplaceBlock(world, itemPosition, chestState);

		if (configuration.addChestContents)
		{
			StructureAlternateStart.FillChest(world, itemPosition, configuration, player);
		}
	}

	private static void PlaceAndFillCraftingMachines(PlayerEntity player, World world, BlockPos cornerPosition, Direction facing, boolean addCraftingTable, boolean addFurnace)
	{
		BlockPos itemPosition = cornerPosition.offset(facing.rotateY()).offset(facing).down();

		if (addCraftingTable)
		{
			BuildingMethods.ReplaceBlock(world, itemPosition, Blocks.CRAFTING_TABLE);
		}

		// Place a furnace next to the crafting table and fill it with 20 coal.
		if (addFurnace)
		{
			itemPosition = itemPosition.offset(facing.rotateY());
			BuildingMethods.ReplaceBlock(world, itemPosition, Blocks.FURNACE.getDefaultState().with(HorizontalBlock.HORIZONTAL_FACING, facing));

			TileEntity tileEntity = world.getTileEntity(itemPosition);

			if (tileEntity instanceof FurnaceTileEntity)
			{
				FurnaceTileEntity furnaceTile = (FurnaceTileEntity) tileEntity;
				furnaceTile.setInventorySlotContents(1, new ItemStack(Items.COAL, 20));
			}
		}
	}

	private static void PlaceOutsideTorches(World world, BlockPos cornerPosition, HouseConfiguration configuration, Direction facing)
	{
		BlockPos itemPosition = HouseConfiguration.NorthEastCorner.offset(facing);
		int houseDepth = configuration.houseDepth + 2;
		int houseWidth = configuration.houseWidth + 2;
		int torchWidthOffset = configuration.houseWidth < 9 ? 3 : 4;
		int torchDepthOffset = configuration.houseDepth < 9 ? 3 : 4;

		// North Wall torches going west.
		BlockState blockState = Blocks.TORCH.getDefaultState();
		BuildingMethods.ReplaceBlock(world, itemPosition, blockState);

		int blocksMoved = torchWidthOffset;

		while (blocksMoved <= houseWidth)
		{
			itemPosition = itemPosition.offset(facing.rotateYCCW(), torchWidthOffset);

			if (world.isAirBlock(itemPosition) && Blocks.TORCH.func_220055_a(world, itemPosition.offset(facing), facing))
			{
				BuildingMethods.ReplaceBlock(world, itemPosition, blockState);
			}

			blocksMoved += torchWidthOffset;
		}

		// East wall torches going south.
		itemPosition = HouseConfiguration.NorthEastCorner.offset(facing.rotateY());
		blockState = Blocks.TORCH.getDefaultState();
		BuildingMethods.ReplaceBlock(world, itemPosition, blockState);

		blocksMoved = torchDepthOffset;

		while (blocksMoved <= houseDepth)
		{
			itemPosition = itemPosition.offset(facing.getOpposite(), torchDepthOffset);

			if (world.isAirBlock(itemPosition) && Blocks.TORCH.func_220055_a(world, itemPosition.offset(facing), facing.rotateY()))
			{
				BuildingMethods.ReplaceBlock(world, itemPosition, blockState);
			}

			blocksMoved += torchDepthOffset;
		}

		// West wall torches going south.
		itemPosition = HouseConfiguration.NorthWestCorner.offset(facing.rotateYCCW());
		blockState = Blocks.TORCH.getDefaultState();
		BuildingMethods.ReplaceBlock(world, itemPosition, blockState);

		blocksMoved = torchDepthOffset;

		while (blocksMoved <= houseDepth)
		{
			itemPosition = itemPosition.offset(facing.getOpposite(), torchDepthOffset);

			if (world.isAirBlock(itemPosition) && Blocks.TORCH.func_220055_a(world, itemPosition.offset(facing), facing.rotateYCCW()))
			{
				BuildingMethods.ReplaceBlock(world, itemPosition, blockState);
			}

			blocksMoved += torchDepthOffset;
		}

		// South wall torches going west.
		itemPosition = HouseConfiguration.SouthEastCorner.offset(facing.getOpposite());
		blockState = Blocks.TORCH.getDefaultState();
		BuildingMethods.ReplaceBlock(world, itemPosition, blockState);

		blocksMoved = torchDepthOffset;

		while (blocksMoved <= houseWidth)
		{
			itemPosition = itemPosition.offset(facing.rotateYCCW(), torchDepthOffset);

			if (world.isAirBlock(itemPosition) && Blocks.TORCH.func_220055_a(world, itemPosition.offset(facing), facing.getOpposite()))
			{
				BuildingMethods.ReplaceBlock(world, itemPosition, blockState);
			}

			blocksMoved += torchDepthOffset;
		}

		if (configuration.isCeilingFlat)
		{
			// Roof Torches
			// North east corner.
			cornerPosition = cornerPosition.up(4);
			blockState = Blocks.TORCH.getDefaultState();

			for (int i = 0; i <= houseDepth; i += torchDepthOffset)
			{
				itemPosition = cornerPosition.offset(facing.getOpposite(), i);

				for (int j = 0; j <= houseWidth; j += torchWidthOffset)
				{
					// When placing the torch, make sure that the torch will be on a solid block otherwise it will just
					// fall.
					if (world.isAirBlock(itemPosition) && !world.isAirBlock(itemPosition.down()))
					{
						BuildingMethods.ReplaceBlock(world, itemPosition, blockState);
					}

					itemPosition = itemPosition.offset(facing.rotateYCCW(), torchWidthOffset);
				}
			}
		}
	}

	private static void PlaceSmallFarm(World world, BlockPos cornerPosition, Direction facing)
	{
		BlockPos farmStart = cornerPosition.offset(facing, 4).offset(facing.rotateYCCW(), 5);
		BlockState state = world.getBlockState(farmStart);

		farmStart = farmStart.down();

		boolean setWaterToCobbleStone = world.getDimension().getType() == DimensionType.field_223228_b_ || world.getDimension().getType() == DimensionType.field_223229_c_;
		Block waterBlock = setWaterToCobbleStone ? Blocks.COBBLESTONE : Blocks.WATER;

		// We are now at the surface and this is where the first water source
		// will be.
		BuildingMethods.ReplaceBlock(world, farmStart, waterBlock);
		BuildingMethods.ReplaceBlock(world, farmStart.down(), Blocks.DIRT);
		BuildingMethods.ReplaceBlock(world, farmStart.up(), Blocks.GLASS);
		BuildingMethods.ReplaceBlock(world, farmStart.up(2), Blocks.TORCH);
		BuildingMethods.ReplaceBlock(world, farmStart.offset(facing), Blocks.FARMLAND);
		BuildingMethods.ReplaceBlock(world, farmStart.offset(facing).offset(facing.rotateYCCW()), Blocks.FARMLAND);
		BuildingMethods.ReplaceBlock(world, farmStart.offset(facing.rotateYCCW()), Blocks.FARMLAND);
		BuildingMethods.ReplaceBlock(world, farmStart.offset(facing.getOpposite()), Blocks.FARMLAND);
		BuildingMethods.ReplaceBlock(world, farmStart.offset(facing.getOpposite()).offset(facing.rotateYCCW()), Blocks.FARMLAND);

		farmStart = farmStart.offset(facing.rotateY());

		BuildingMethods.ReplaceBlock(world, farmStart, waterBlock);
		BuildingMethods.ReplaceBlock(world, farmStart.down(), Blocks.DIRT);
		BuildingMethods.ReplaceBlock(world, farmStart.up(), Blocks.GLASS);
		BuildingMethods.ReplaceBlock(world, farmStart.up(2), Blocks.TORCH);
		BuildingMethods.ReplaceBlock(world, farmStart.offset(facing), Blocks.FARMLAND);
		BuildingMethods.ReplaceBlock(world, farmStart.offset(facing.getOpposite()), Blocks.FARMLAND);

		farmStart = farmStart.offset(facing.rotateY());

		BuildingMethods.ReplaceBlock(world, farmStart, waterBlock);
		BuildingMethods.ReplaceBlock(world, farmStart.down(), Blocks.DIRT);
		BuildingMethods.ReplaceBlock(world, farmStart.up(), Blocks.GLASS);
		BuildingMethods.ReplaceBlock(world, farmStart.up(2), Blocks.TORCH);
		BuildingMethods.ReplaceBlock(world, farmStart.offset(facing), Blocks.FARMLAND);
		BuildingMethods.ReplaceBlock(world, farmStart.offset(facing).offset(facing.rotateY()), Blocks.FARMLAND);
		BuildingMethods.ReplaceBlock(world, farmStart.offset(facing.rotateY()), Blocks.FARMLAND);
		BuildingMethods.ReplaceBlock(world, farmStart.offset(facing.getOpposite()), Blocks.FARMLAND);
		BuildingMethods.ReplaceBlock(world, farmStart.offset(facing.getOpposite()).offset(facing.rotateY()), Blocks.FARMLAND);
	}

	private static void PlaceMineShaft(ServerWorld world, BlockPos pos, int houseDepth, Direction facing)
	{
		// The initial position is where the character was teleported too, go
		// back X blocks and start building the mine shaft.
		if ((houseDepth & 1) != 0)
		{
			houseDepth = (int) Math.floor(houseDepth / 2);
		}
		else
		{
			houseDepth = (int) Math.floor(houseDepth / 2) - 1;
		}
		
		pos = pos.offset(facing.getOpposite(), houseDepth).down();

		StructureAlternateStart.PlaceMineShaft(world, pos, facing, false);
	}

	/**
	 * This enum is used to contain the different type of starting houses available to the player.
	 * 
	 * @author WuestMan
	 *
	 */
	public enum HouseStyle
	{
		BASIC(0, GuiLangKeys.STARTER_HOUSE_BASIC_DISPLAY, new ResourceLocation("prefab", "textures/gui/basic_house.png"), GuiLangKeys.STARTER_HOUSE_BASIC_NOTES, 153, 148, ""),
		RANCH(1, GuiLangKeys.STARTER_HOUSE_RANCH_DISPLAY, new ResourceLocation("prefab", "textures/gui/ranch_house.png"), GuiLangKeys.STARTER_HOUSE_RANCH_NOTES, 152, 89,
			"assets/prefab/structures/ranch_house.zip"),
		LOFT(2, GuiLangKeys.STARTER_HOUSE_LOFT_DISPLAY, new ResourceLocation("prefab", "textures/gui/loft_house.png"), GuiLangKeys.STARTER_HOUSE_LOFT_NOTES, 152, 87,
			"assets/prefab/structures/loft_house.zip"),
		HOBBIT(3, GuiLangKeys.STARTER_HOUSE_HOBBIT_DISPLAY, new ResourceLocation("prefab", "textures/gui/hobbit_house.png"), GuiLangKeys.STARTER_HOUSE_HOBBIT_NOTES, 151, 133,
			"assets/prefab/structures/hobbit_house.zip"),
		DESERT(4, GuiLangKeys.STARTER_HOUSE_DESERT_DISPLAY, new ResourceLocation("prefab", "textures/gui/desert_house.png"), GuiLangKeys.STARTER_HOUSE_DESERT_NOTES, 152, 131,
			"assets/prefab/structures/desert_house.zip"),
		SNOWY(5, GuiLangKeys.STARTER_HOUSE_SNOWY_DISPLAY, new ResourceLocation("prefab", "textures/gui/snowy_house.png"), GuiLangKeys.STARTER_HOUSE_SNOWY_NOTES, 150, 125,
			"assets/prefab/structures/snowy_house.zip");

		private final int value;
		private final String displayName;
		private final ResourceLocation housePicture;
		private final String houseNotes;
		private final int imageWidth;
		private final int imageHeight;
		private final String structureLocation;

		HouseStyle(int newValue, String displayName, ResourceLocation housePicture, String houseNotes, int imageWidth, int imageHeight, String structureLocation)
		{
			this.value = newValue;
			this.displayName = displayName;
			this.housePicture = housePicture;
			this.houseNotes = houseNotes;
			this.imageWidth = imageWidth;
			this.imageHeight = imageHeight;
			this.structureLocation = structureLocation;
		}

		/**
		 * Gets a unique identifier for this style.
		 * 
		 * @return An integer representing the ID of this style.
		 */
		public int getValue()
		{
			return value;
		}

		/**
		 * Gets the display name for this style.
		 * 
		 * @return A string representing the name of this style.
		 */
		public String getDisplayName()
		{
			return GuiLangKeys.translateString(this.displayName);
		}

		/**
		 * Gets the notes for this house style.
		 * 
		 * @return A string representing the translated notes for this style.
		 */
		public String getHouseNotes()
		{
			return GuiLangKeys.translateString(this.houseNotes);
		}

		/**
		 * Gets the picture used in the GUI for this style.
		 * 
		 * @return A resource location representing the image to use for this style.
		 */
		public ResourceLocation getHousePicture()
		{
			return this.housePicture;
		}

		/**
		 * Gets the width of the image to use with this style.
		 * 
		 * @return An integer representing the image width.
		 */
		public int getImageWidth()
		{
			return this.imageWidth;
		}

		/**
		 * Gets the height of the image to use with this style.
		 * 
		 * @return An integer representing the image height.
		 */
		public int getImageHeight()
		{
			return this.imageHeight;
		}

		/**
		 * Gets a string for the resource location of this style.
		 * 
		 * @return A string representing the location of the structure asset in the mod.
		 */
		public String getStructureLocation()
		{
			return this.structureLocation;
		}

		/**
		 * Returns a house style based off of an integer value.
		 * 
		 * @param value The integer value representing the house style.
		 * @return The house style found or HouseStyle.Basic if none found.
		 */
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

				case 3:
				{
					return HouseStyle.HOBBIT;
				}

				case 4:
				{
					return HouseStyle.DESERT;
				}

				case 5:
				{
					return HouseStyle.SNOWY;
				}

				default:
				{
					return HouseStyle.BASIC;
				}
			}
		}
	}
}
