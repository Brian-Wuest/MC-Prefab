package com.wuest.prefab.Items;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.BlockFurnace;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.BlockLadder;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockSign;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.BlockTorch;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDoor;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.AchievementList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import com.wuest.prefab.BuildingMethods;
import com.wuest.prefab.ModRegistry;
import com.wuest.prefab.Prefab;
import com.wuest.prefab.Config.HouseConfiguration;
import com.wuest.prefab.Proxy.CommonProxy;
import com.wuest.prefab.StructureGen.CustomStructures.StructureAlternateStart;
import com.wuest.prefab.StructureGen.CustomStructures.StructureChickenCoop;

public class ItemStartHouse extends Item
{
	protected static BlockPos NorthEastCorner;
	protected static BlockPos SouthEastCorner;
	protected static BlockPos SouthWestCorner;
	protected static BlockPos NorthWestCorner;

	private HouseConfiguration currentConfiguration = null;

	public ItemStartHouse(String name)
	{
		super();

		this.setCreativeTab(CreativeTabs.MISC);
		ModRegistry.setItemName(this, name);
	}

	/**
	 * Does something when the item is right-clicked.
	 */
	@Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos hitBlockPos, EnumHand hand, EnumFacing side, float hitX,
			float hitY, float hitZ)
	{
		if (world.isRemote)
		{
			if (side == EnumFacing.UP)
			{
				// Open the client side gui to determine the house options.
				//StructureAlternateStart alternateStart = new StructureAlternateStart();
				//alternateStart.ScanLoftStructure(world, hitBlockPos, player.getHorizontalFacing());
				
				player.openGui(Prefab.instance, ModRegistry.GuiStartHouseChooser, player.worldObj, hitBlockPos.getX(), hitBlockPos.getY(), hitBlockPos.getZ());
				return EnumActionResult.PASS;
			}
		}

		return EnumActionResult.FAIL;
	}

	public static void BuildHouse(EntityPlayer player, World world, HouseConfiguration configuration)
	{
		// This is always on the server.
		if (configuration != null)
		{
			BlockPos hitBlockPos = configuration.pos;
			BlockPos playerPosition = player.getPosition();

			IBlockState hitBlockState = world.getBlockState(hitBlockPos);

			if (hitBlockState != null)
			{
				Block hitBlock = hitBlockState.getBlock();

				if (hitBlock != null)
				{
					if (configuration.houseStyle == HouseConfiguration.HouseStyle.BASIC)
					{
						// We hit a block, let's start building!!!!!
						BlockPos startingPosition = hitBlockPos.up();
	
						// Get the new "North" facing. This is the orientation of
						// the house and all building will be based on this.
						EnumFacing northFace = configuration.houseFacing;
	
						// Get the "South" facing of the house to make rotating
						// easier.
						EnumFacing southFace = northFace.getOpposite();
	
						// Set the "North East" corner.
						ItemStartHouse.NorthEastCorner = startingPosition.offset(northFace, (int) Math.floor(configuration.houseDepth / 2) + 1)
								.offset(northFace.rotateY(), (int) Math.floor(configuration.houseWidth / 2) + 1);
	
						// Set the "South East" corner.
						ItemStartHouse.SouthEastCorner = ItemStartHouse.NorthEastCorner.offset(southFace, configuration.houseDepth + 1);
	
						// Set the "South West" corner.
						ItemStartHouse.SouthWestCorner = ItemStartHouse.SouthEastCorner.offset(northFace.rotateYCCW(), configuration.houseWidth + 1);
	
						// Set the "North West" corner.
						ItemStartHouse.NorthWestCorner = ItemStartHouse.NorthEastCorner.offset(northFace.rotateYCCW(), configuration.houseWidth + 1);
	
						// Clear the space before the user is teleported. This
						// is in-case they right-click on a space that is only 1
						// block tall.
						BuildingMethods.ClearSpace(world, ItemStartHouse.NorthEastCorner, configuration.houseWidth, 15, configuration.houseDepth, northFace);
	
						// Teleport the player to the middle of the house so
						// they don't die while house is created.
						player.attemptTeleport(startingPosition.up(2).getX(), startingPosition.up(2).getY(), startingPosition.up(2).getZ());
	
						// Build the basic structure.
						ItemStartHouse.BuildStructure(world, startingPosition, configuration, northFace);
	
						// Build the interior.
						ItemStartHouse.BuildInterior(world, startingPosition, player, configuration, northFace);
	
						// Set up the exterior.
						ItemStartHouse.BuildExterior(world, startingPosition, player, configuration, northFace);
	
						if (configuration.addMineShaft && startingPosition.getY() > 15)
						{
							// Set up the mineshaft.
							ItemStartHouse.PlaceMineShaft(world, startingPosition, configuration.houseDepth, northFace);
						}
					}
					else
					{
						// Build the alternate starter house instead.
						StructureAlternateStart structure = StructureAlternateStart.CreateInstance(configuration.houseStyle.getStructureLocation(), StructureAlternateStart.class);
						structure.BuildStructure(configuration, world, hitBlockPos, EnumFacing.NORTH, player);
					}

					player.inventory.clearMatchingItems(ModRegistry.StartHouse(), -1, 1, null);
					player.inventoryContainer.detectAndSendChanges();
				}
			}

			// Make sure to remove the tag so the next time the player opens the
			// GUI we overwrite the existing tag.
			player.getEntityData().removeTag(HouseConfiguration.tagKey);
		}
	}

	private static void BuildStructure(World world, BlockPos startingPosition, HouseConfiguration configuration, EnumFacing facing)
	{
		// Make sure that the area beneath the house is all there. Don't want
		// the house to be hanging in the air.
		BlockPos pos = ItemStartHouse.NorthEastCorner;

		BuildingMethods.SetFloor(world, pos.down(2), Blocks.DIRT, configuration.houseWidth + 2, configuration.houseDepth + 2, new ArrayList<ItemStack>(),
				facing.getOpposite());

		Block floor = null;

		switch (configuration.floorBlock)
		{
			case Brick:
			{
				floor = Blocks.BRICK_BLOCK;
				break;
			}

			case SandStone:
			{
				floor = Blocks.SANDSTONE;
				break;
			}

			default:
			{
				floor = Blocks.STONEBRICK;
				break;
			}
		}

		// Create the floor.
		BuildingMethods.SetFloor(world, pos.down(), floor, configuration.houseWidth + 2, configuration.houseDepth + 2, new ArrayList<ItemStack>(),
				facing.getOpposite());

		// Create the walls.
		ItemStartHouse.SetWalls(world, ((BlockPlanks) Blocks.PLANKS).getStateFromMeta(configuration.wallWoodType.getValue()), configuration, facing);

		Block ceiling = null;
		Block stairs = null;

		switch (configuration.ceilingBlock)
		{
			case Brick:
			{
				ceiling = Blocks.BRICK_BLOCK;
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
				ceiling = Blocks.STONEBRICK;
				stairs = Blocks.STONE_BRICK_STAIRS;
				break;
			}
		}

		// Set the ceiling.
		pos = pos.up(4);
		BuildingMethods.SetCeiling(world, pos, ceiling, configuration.houseWidth + 2, configuration.houseDepth + 2, stairs, configuration, facing);
	}

	private static void BuildInterior(World world, BlockPos startingPosition, EntityPlayer player, HouseConfiguration configuration, EnumFacing facing)
	{
		// Keep the corner positions since they are important.
		BlockPos northEastCornerPosition = ItemStartHouse.NorthEastCorner.up();
		BlockPos southEastCornerPosition = ItemStartHouse.SouthEastCorner.up();
		BlockPos northWestCornerPosition = ItemStartHouse.NorthWestCorner.up();
		BlockPos southWestCornerPosition = ItemStartHouse.SouthWestCorner.up();

		// Use a separate position for each item.
		BlockPos itemPosition = northEastCornerPosition;

		if (configuration.addTorches)
		{
			// Set the torch locations so it's not dark in the house.
			ItemStartHouse.PlaceInsideTorches(world, configuration, facing);
		}

		// Create an oak door in the north east corner
		ItemStartHouse.DecorateDoor(world, northEastCornerPosition, player, configuration, facing);

		if (configuration.addBed)
		{
			// Place a bed in the north west corner.
			ItemStartHouse.PlaceBed(world, northWestCornerPosition, facing);
		}

		if (configuration.addCraftingTable && !Prefab.proxy.proxyConfiguration.enableHouseGenerationRestrictions)
		{
			// Place a crafting table in the south west corner.
			ItemStartHouse.PlaceAndFillCraftingMachines(player, world, southWestCornerPosition, facing);
		}

		if (configuration.addChest && !Prefab.proxy.proxyConfiguration.enableHouseGenerationRestrictions)
		{
			// Place a double chest in the south east corner.
			ItemStartHouse.PlaceAndFillChest(player, world, southEastCornerPosition, configuration, facing);
		}
	}

	private static void BuildExterior(World world, BlockPos startingPosition, EntityPlayer player, HouseConfiguration configuration, EnumFacing facing)
	{
		// Keep the corner positions since they are important.
		// These positions are level with 1 block above the floor.
		BlockPos northEastCornerPosition = ItemStartHouse.NorthEastCorner.up();
		BlockPos southEastCornerPosition = ItemStartHouse.SouthEastCorner.up();
		BlockPos northWestCornerPosition = ItemStartHouse.NorthWestCorner.up();
		BlockPos southWestCornerPosition = ItemStartHouse.SouthWestCorner.up();

		if (configuration.addTorches)
		{
			ItemStartHouse.PlaceOutsideTorches(world, northEastCornerPosition, configuration, facing);
		}

		if (configuration.addFarm)
		{
			ItemStartHouse.PlaceSmallFarm(world, northEastCornerPosition.down(), facing);
		}
	}

	private static void SetWalls(World world, IBlockState block, HouseConfiguration configuration, EnumFacing facing)
	{
		// Get the north east corner.
		BlockPos pos = ItemStartHouse.NorthEastCorner;

		facing = facing.getOpposite();

		// East Wall.
		BuildingMethods.CreateWall(world, 4, configuration.houseDepth + 1, facing, pos, block);

		facing = facing.rotateY();

		// South Wall.
		pos = ItemStartHouse.SouthEastCorner;
		BuildingMethods.CreateWall(world, 4, configuration.houseWidth + 2, facing, pos, block);

		facing = facing.rotateY();

		// West Wall.
		pos = ItemStartHouse.SouthWestCorner;
		BuildingMethods.CreateWall(world, 4, configuration.houseDepth + 2, facing, pos, block);

		facing = facing.rotateY();

		// North Wall.
		pos = ItemStartHouse.NorthWestCorner;
		BuildingMethods.CreateWall(world, 4, configuration.houseWidth + 2, facing, pos, block);
	}

	private static void PlaceInsideTorches(World world, HouseConfiguration configuration, EnumFacing facing)
	{
		// Use a separate position for each item.
		BlockPos itemPosition = null;
		int torchWidthOffset = configuration.houseWidth < 9 ? 2 : 4;
		int torchDepthOffset = configuration.houseDepth < 9 ? 2 : 4;

		/*
		 * Torch Facings 1 = East 2 = West 3 = South 4 = North 5 = Up
		 */

		// North Wall torches.
		itemPosition = ItemStartHouse.NorthEastCorner.offset(facing.rotateYCCW(), torchWidthOffset).offset(facing.getOpposite()).up();
		IBlockState blockState = Blocks.TORCH.getDefaultState().withProperty(BlockTorch.FACING, facing.getOpposite());
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
		itemPosition = ItemStartHouse.NorthEastCorner.offset(facing.getOpposite(), torchDepthOffset).offset(facing.rotateYCCW()).up();
		blockState = Blocks.TORCH.getDefaultState().withProperty(BlockTorch.FACING, facing.rotateYCCW());
		BuildingMethods.ReplaceBlock(world, itemPosition, blockState);

		blocksMoved = torchDepthOffset;

		while (blocksMoved < configuration.houseDepth)
		{
			itemPosition = itemPosition.offset(facing.getOpposite(), torchDepthOffset);

			if (world.isAirBlock(itemPosition)&& !world.isAirBlock(itemPosition.offset(facing.rotateY())))
			{
				BuildingMethods.ReplaceBlock(world, itemPosition, blockState);
			}

			blocksMoved += torchDepthOffset;
		}

		// West wall torches.
		itemPosition = ItemStartHouse.NorthWestCorner.offset(facing.getOpposite(), torchDepthOffset).offset(facing.rotateY()).up();
		blockState = Blocks.TORCH.getDefaultState().withProperty(BlockTorch.FACING, facing.rotateY());
		BuildingMethods.ReplaceBlock(world, itemPosition, blockState);

		blocksMoved = torchDepthOffset;

		while (blocksMoved < configuration.houseDepth)
		{
			itemPosition = itemPosition.offset(facing.getOpposite(), torchDepthOffset);

			if (world.isAirBlock(itemPosition)&& !world.isAirBlock(itemPosition.offset(facing.rotateYCCW())))
			{
				BuildingMethods.ReplaceBlock(world, itemPosition, blockState);
			}

			blocksMoved += torchDepthOffset;
		}

		// South wall torches.
		itemPosition = ItemStartHouse.SouthEastCorner.offset(facing.rotateYCCW(), torchWidthOffset).offset(facing).up();
		blockState = Blocks.TORCH.getDefaultState().withProperty(BlockTorch.FACING, facing);
		BuildingMethods.ReplaceBlock(world, itemPosition, blockState);

		blocksMoved = torchDepthOffset;

		while (blocksMoved < configuration.houseWidth)
		{
			itemPosition = itemPosition.offset(facing.rotateYCCW(), torchDepthOffset);

			if (world.isAirBlock(itemPosition)&& !world.isAirBlock(itemPosition.offset(facing.getOpposite())))
			{
				BuildingMethods.ReplaceBlock(world, itemPosition, blockState);
			}

			blocksMoved += torchDepthOffset;
		}
	}

	private static void DecorateDoor(World world, BlockPos cornerPosition, EntityPlayer player, HouseConfiguration configuration, EnumFacing facing)
	{
		BlockPos itemPosition = cornerPosition.offset(facing.rotateYCCW());

		world.setBlockToAir(itemPosition);

		Block door = null;
		Block stairs = null;

		switch (configuration.wallWoodType)
		{
			case Spruce:
			{
				door = Blocks.SPRUCE_DOOR;
				stairs = Blocks.SPRUCE_STAIRS;
				break;
			}

			case Birch:
			{
				door = Blocks.BIRCH_DOOR;
				stairs = Blocks.BIRCH_STAIRS;
				break;
			}

			case Jungle:
			{
				door = Blocks.JUNGLE_DOOR;
				stairs = Blocks.JUNGLE_STAIRS;
				break;
			}

			case Acacia:
			{
				door = Blocks.ACACIA_DOOR;
				stairs = Blocks.ACACIA_STAIRS;
				break;
			}

			case DarkOak:
			{
				door = Blocks.DARK_OAK_DOOR;
				stairs = Blocks.DARK_OAK_STAIRS;
				break;
			}

			default:
			{
				door = Blocks.OAK_DOOR;
				stairs = Blocks.OAK_STAIRS;
				break;
			}
		}

		ItemDoor.placeDoor(world, itemPosition.down(), facing, door, true);

		// Put a glass pane above the door.
		BuildingMethods.ReplaceBlock(world, itemPosition.up(), Blocks.GLASS_PANE);

		// Create a pressure plate for the door, no need to re-set the item
		// position here since it needs to be in relation to the door.
		itemPosition = itemPosition.offset(facing.getOpposite()).down();
		BuildingMethods.ReplaceBlock(world, itemPosition, Blocks.WOODEN_PRESSURE_PLATE);

		// Place a sign.
		itemPosition = itemPosition.offset(facing, 2).offset(facing.rotateYCCW());
		BlockSign sign = (BlockSign) Blocks.STANDING_SIGN;
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
		BuildingMethods.ReplaceBlock(world, itemPosition, sign.getStateFromMeta(signMeta));

		TileEntity tileEntity = world.getTileEntity(itemPosition);

		if (tileEntity instanceof TileEntitySign)
		{
			TileEntitySign signTile = (TileEntitySign) tileEntity;
			signTile.signText[0] = new TextComponentString("This is");

			if (player.getDisplayNameString().length() >= 15)
			{
				signTile.signText[1] = new TextComponentString(player.getDisplayNameString());
			}
			else
			{
				signTile.signText[1] = new TextComponentString(player.getDisplayNameString() + "'s");
			}

			signTile.signText[2] = new TextComponentString("house!");
		}
	}

	private static void PlaceBed(World world, BlockPos cornerPosition, EnumFacing facing)
	{
		// This is the "north west" corner.
		BlockPos itemPosition = cornerPosition.offset(facing.rotateY(), 1).offset(facing.getOpposite(), 2).down();

		IBlockState bedFootState = Blocks.BED.getDefaultState().withProperty(BlockHorizontal.FACING, facing)
				.withProperty(BlockBed.OCCUPIED, Boolean.valueOf(false)).withProperty(BlockBed.PART, BlockBed.EnumPartType.FOOT);

		if (world.setBlockState(itemPosition, bedFootState, 3))
		{
			IBlockState bedHeadState = bedFootState.withProperty(BlockBed.PART, BlockBed.EnumPartType.HEAD);
			world.setBlockState(itemPosition.offset(facing), bedHeadState, 3);
		}

	}

	private static void PlaceAndFillChest(EntityPlayer player, World world, BlockPos cornerPosition, HouseConfiguration configuration, EnumFacing facing)
	{
		// Create a double wide chest.
		BlockPos itemPosition = cornerPosition.offset(facing).offset(facing.rotateYCCW()).down();
		IBlockState chestState = Blocks.CHEST.getDefaultState().withProperty(BlockChest.FACING, facing);
		BuildingMethods.ReplaceBlock(world, itemPosition, chestState);

		itemPosition = itemPosition.offset(facing.rotateYCCW());
		BuildingMethods.ReplaceBlock(world, itemPosition, chestState);

		if (configuration.addChestContents)
		{
			StructureAlternateStart.FillChest(world, itemPosition, configuration, player);
		}
	}

	private static void PlaceAndFillCraftingMachines(EntityPlayer player, World world, BlockPos cornerPosition, EnumFacing facing)
	{
		BlockPos itemPosition = cornerPosition.offset(facing.rotateY()).offset(facing).down();
		BuildingMethods.ReplaceBlock(world, itemPosition, Blocks.CRAFTING_TABLE);

		// Trigger the workbench achievement.
		player.addStat(AchievementList.BUILD_WORK_BENCH);

		// Place a furnace next to the crafting table and fill it with 20 coal.
		itemPosition = itemPosition.offset(facing.rotateY());
		BuildingMethods.ReplaceBlock(world, itemPosition, Blocks.FURNACE.getDefaultState().withProperty(BlockFurnace.FACING, facing));

		TileEntity tileEntity = world.getTileEntity(itemPosition);

		if (tileEntity instanceof TileEntityFurnace)
		{
			TileEntityFurnace furnaceTile = (TileEntityFurnace) tileEntity;
			furnaceTile.setInventorySlotContents(1, new ItemStack(Items.COAL, 20));
		}
	}

	private static void PlaceOutsideTorches(World world, BlockPos cornerPosition, HouseConfiguration configuration, EnumFacing facing)
	{
		BlockPos itemPosition = ItemStartHouse.NorthEastCorner.offset(facing);
		int houseDepth = configuration.houseDepth + 2;
		int houseWidth = configuration.houseWidth + 2;
		int torchWidthOffset = configuration.houseWidth < 9 ? 3 : 4;
		int torchDepthOffset = configuration.houseDepth < 9 ? 3 : 4;

		// North Wall torches going west.
		IBlockState blockState = Blocks.TORCH.getDefaultState().withProperty(BlockTorch.FACING, facing);
		BuildingMethods.ReplaceBlock(world, itemPosition, blockState);

		int blocksMoved = torchWidthOffset;
		
		while (blocksMoved <= houseWidth)
		{
			itemPosition = itemPosition.offset(facing.rotateYCCW(), torchWidthOffset);

			if (world.isAirBlock(itemPosition) && world.isSideSolid(itemPosition.offset(facing.getOpposite()), facing))
			{
				BuildingMethods.ReplaceBlock(world, itemPosition, blockState);
			}

			blocksMoved += torchWidthOffset;
		}

		// East wall torches going south.
		itemPosition = ItemStartHouse.NorthEastCorner.offset(facing.rotateY());
		blockState = Blocks.TORCH.getDefaultState().withProperty(BlockTorch.FACING, facing.rotateY());
		BuildingMethods.ReplaceBlock(world, itemPosition, blockState);

		blocksMoved = torchDepthOffset;

		while (blocksMoved <= houseDepth)
		{
			itemPosition = itemPosition.offset(facing.getOpposite(), torchDepthOffset);

			if (world.isAirBlock(itemPosition) && world.isSideSolid(itemPosition.offset(facing.rotateYCCW()), facing.rotateY()))
			{
				BuildingMethods.ReplaceBlock(world, itemPosition, blockState);
			}

			blocksMoved += torchDepthOffset;
		}

		// West wall torches going south.
		itemPosition = ItemStartHouse.NorthWestCorner.offset(facing.rotateYCCW());
		blockState = Blocks.TORCH.getDefaultState().withProperty(BlockTorch.FACING, facing.rotateYCCW());
		BuildingMethods.ReplaceBlock(world, itemPosition, blockState);

		blocksMoved = torchDepthOffset;

		while (blocksMoved <= houseDepth)
		{
			itemPosition = itemPosition.offset(facing.getOpposite(), torchDepthOffset);

			if (world.isAirBlock(itemPosition) && world.isSideSolid(itemPosition.offset(facing.rotateY()), facing.rotateYCCW()))
			{
				BuildingMethods.ReplaceBlock(world, itemPosition, blockState);
			}

			blocksMoved += torchDepthOffset;
		}

		// South wall torches going west.
		itemPosition = ItemStartHouse.SouthEastCorner.offset(facing.getOpposite());
		blockState = Blocks.TORCH.getDefaultState().withProperty(BlockTorch.FACING, facing.getOpposite());
		BuildingMethods.ReplaceBlock(world, itemPosition, blockState);

		blocksMoved = torchDepthOffset;

		while (blocksMoved <= houseWidth)
		{
			itemPosition = itemPosition.offset(facing.rotateYCCW(), torchDepthOffset);

			if (world.isAirBlock(itemPosition) && world.isSideSolid(itemPosition.offset(facing), facing.getOpposite()))
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
			blockState = Blocks.TORCH.getStateFromMeta(BuildingMethods.GetTorchFacing(EnumFacing.UP));

			for (int i = 0; i <= houseDepth; i += torchDepthOffset)
			{
				itemPosition = cornerPosition.offset(facing.getOpposite(), i);

				for (int j = 0; j <= houseWidth; j += torchWidthOffset)
				{
					// When placing the torch, make sure that the torch will be on a solid block otherwise it will just fall.
					if (world.isAirBlock(itemPosition) && !world.isAirBlock(itemPosition.down()))
					{
						BuildingMethods.ReplaceBlock(world, itemPosition, blockState);
					}
					
					itemPosition = itemPosition.offset(facing.rotateYCCW(), torchWidthOffset);
				}
			}
		}
	}

	private static void PlaceSmallFarm(World world, BlockPos cornerPosition, EnumFacing facing)
	{
		BlockPos farmStart = cornerPosition.offset(facing, 4).offset(facing.rotateYCCW(), 5);
		IBlockState state = world.getBlockState(farmStart);

		farmStart = farmStart.down();

		// We are now at the surface and this is where the first water source
		// will be.
		BuildingMethods.ReplaceBlock(world, farmStart, Blocks.WATER);
		BuildingMethods.ReplaceBlock(world, farmStart.down(), Blocks.DIRT);
		BuildingMethods.ReplaceBlock(world, farmStart.up(), Blocks.GLASS);
		BuildingMethods.ReplaceBlock(world, farmStart.up(2), Blocks.TORCH);
		BuildingMethods.ReplaceBlock(world, farmStart.offset(facing), Blocks.FARMLAND);
		BuildingMethods.ReplaceBlock(world, farmStart.offset(facing).offset(facing.rotateYCCW()), Blocks.FARMLAND);
		BuildingMethods.ReplaceBlock(world, farmStart.offset(facing.rotateYCCW()), Blocks.FARMLAND);
		BuildingMethods.ReplaceBlock(world, farmStart.offset(facing.getOpposite()), Blocks.FARMLAND);
		BuildingMethods.ReplaceBlock(world, farmStart.offset(facing.getOpposite()).offset(facing.rotateYCCW()), Blocks.FARMLAND);

		farmStart = farmStart.offset(facing.rotateY());

		BuildingMethods.ReplaceBlock(world, farmStart, Blocks.WATER);
		BuildingMethods.ReplaceBlock(world, farmStart.down(), Blocks.DIRT);
		BuildingMethods.ReplaceBlock(world, farmStart.up(), Blocks.GLASS);
		BuildingMethods.ReplaceBlock(world, farmStart.up(2), Blocks.TORCH);
		BuildingMethods.ReplaceBlock(world, farmStart.offset(facing), Blocks.FARMLAND);
		BuildingMethods.ReplaceBlock(world, farmStart.offset(facing.getOpposite()), Blocks.FARMLAND);

		farmStart = farmStart.offset(facing.rotateY());

		BuildingMethods.ReplaceBlock(world, farmStart, Blocks.WATER);
		BuildingMethods.ReplaceBlock(world, farmStart.down(), Blocks.DIRT);
		BuildingMethods.ReplaceBlock(world, farmStart.up(), Blocks.GLASS);
		BuildingMethods.ReplaceBlock(world, farmStart.up(2), Blocks.TORCH);
		BuildingMethods.ReplaceBlock(world, farmStart.offset(facing), Blocks.FARMLAND);
		BuildingMethods.ReplaceBlock(world, farmStart.offset(facing).offset(facing.rotateY()), Blocks.FARMLAND);
		BuildingMethods.ReplaceBlock(world, farmStart.offset(facing.rotateY()), Blocks.FARMLAND);
		BuildingMethods.ReplaceBlock(world, farmStart.offset(facing.getOpposite()), Blocks.FARMLAND);
		BuildingMethods.ReplaceBlock(world, farmStart.offset(facing.getOpposite()).offset(facing.rotateY()), Blocks.FARMLAND);
	}

	private static void PlaceMineShaft(World world, BlockPos pos, int houseDepth, EnumFacing facing)
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

		StructureAlternateStart.PlaceMineShaft(world, pos, facing);
	}

}