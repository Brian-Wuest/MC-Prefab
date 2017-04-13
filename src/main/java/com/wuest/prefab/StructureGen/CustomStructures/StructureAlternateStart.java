package com.wuest.prefab.StructureGen.CustomStructures;

import java.util.ArrayList;

import com.wuest.prefab.BuildingMethods;
import com.wuest.prefab.Prefab;
import com.wuest.prefab.Config.*;
import com.wuest.prefab.Items.ItemStartHouse;
import com.wuest.prefab.StructureGen.*;

import net.minecraft.block.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.AchievementList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

/**
 * 
 * @author WuestMan
 *
 */
public class StructureAlternateStart extends Structure
{
	private BlockPos chestPosition = null;
	private BlockPos furnacePosition = null;
	private BlockPos trapDoorPosition = null;
	private BlockPos signPosition = null;
	private static ArrayList<BlockPos> torchPositions = null;

	public static void ScanRanchStructure(World world, BlockPos originalPos, EnumFacing playerFacing)
	{
		BuildClear clearedSpace = new BuildClear();
		clearedSpace.getShape().setDirection(EnumFacing.SOUTH);
		clearedSpace.getShape().setHeight(7);
		clearedSpace.getShape().setLength(21);
		clearedSpace.getShape().setWidth(11);
		clearedSpace.getStartingPosition().setSouthOffset(1);
		clearedSpace.getStartingPosition().setEastOffset(8);
		clearedSpace.getStartingPosition().setHeightOffset(-1);

		Structure.ScanStructure(world, originalPos, originalPos.east(8).south().down(), originalPos.south(22).west(3).up(8),
				"..\\src\\main\\resources\\assets\\prefab\\structures\\ranch_house.zip", clearedSpace,
				playerFacing, false, false);
	}

	public static void ScanLoftStructure(World world, BlockPos originalPos, EnumFacing playerFacing)
	{
		BuildClear clearedSpace = new BuildClear();
		clearedSpace.getShape().setDirection(EnumFacing.SOUTH);
		clearedSpace.getShape().setHeight(9);
		clearedSpace.getShape().setLength(13);
		clearedSpace.getShape().setWidth(16);
		clearedSpace.getStartingPosition().setSouthOffset(1);
		clearedSpace.getStartingPosition().setEastOffset(7);

		Structure.ScanStructure(world, originalPos, originalPos.east(7).south(), originalPos.south(14).west(8).up(9),
				"..\\src\\main\\resources\\assets\\prefab\\structures\\loft_house.zip", clearedSpace, playerFacing, false, false);
	}

	public static void ScanHobbitStructure(World world, BlockPos originalPos, EnumFacing playerFacing)
	{
		BuildClear clearedSpace = new BuildClear();
		clearedSpace.getShape().setDirection(EnumFacing.SOUTH);
		clearedSpace.getShape().setHeight(12);
		clearedSpace.getShape().setLength(16);
		clearedSpace.getShape().setWidth(16);
		clearedSpace.getStartingPosition().setSouthOffset(1);
		clearedSpace.getStartingPosition().setEastOffset(8);
		clearedSpace.getStartingPosition().setHeightOffset(-3);

		Structure.ScanStructure(world, originalPos, originalPos.east(8).south().down(3), originalPos.south(16).west(8).up(12),
				"..\\src\\main\\resources\\assets\\prefab\\structures\\hobbit_house.zip", clearedSpace,
				playerFacing, false, false);
	}
	
	public static void ScanStructure(World world, BlockPos originalPos, EnumFacing playerFacing, String structureFileName, boolean includeAir, boolean excludeWater)
	{
		BuildClear clearedSpace = new BuildClear();
		clearedSpace.getShape().setDirection(EnumFacing.SOUTH);
		clearedSpace.getShape().setHeight(8);
		clearedSpace.getShape().setLength(15);
		clearedSpace.getShape().setWidth(15);
		clearedSpace.getStartingPosition().setSouthOffset(1);
		clearedSpace.getStartingPosition().setEastOffset(8);
		clearedSpace.getStartingPosition().setHeightOffset(-1);
		
		BuildShape buildShape = clearedSpace.getShape();
		PositionOffset offset = clearedSpace.getStartingPosition();
		
		int downOffset = offset.getHeightOffset() < 0 ? Math.abs(offset.getHeightOffset()) : 0;
		BlockPos cornerPos = originalPos.east(offset.getEastOffset()).south(offset.getSouthOffset()).down(downOffset);
		
		Structure.ScanStructure(
				world, 
				originalPos, 
				cornerPos,
				cornerPos.south(buildShape.getLength()).west(buildShape.getWidth()).up(buildShape.getHeight()), 
				"..\\src\\main\\resources\\assets\\prefab\\structures\\" + structureFileName  + ".zip",
				clearedSpace,
				playerFacing,
				includeAir,
				excludeWater);
	}

	@Override
	protected Boolean CustomBlockProcessingHandled(StructureConfiguration configuration, BuildBlock block, World world, BlockPos originalPos,
			EnumFacing assumedNorth, Block foundBlock, IBlockState blockState, EntityPlayer player)
	{
		HouseConfiguration houseConfig = (HouseConfiguration) configuration;

		if ((!houseConfig.addBed && foundBlock instanceof BlockBed) || (!houseConfig.addChest && foundBlock instanceof BlockChest)
				|| (!houseConfig.addTorches && foundBlock instanceof BlockTorch)
				|| (!houseConfig.addCraftingTable && (foundBlock instanceof BlockWorkbench || foundBlock instanceof BlockFurnace)))
		{
			// Don't place the block, returning true means that this has been
			// "handled"
			return true;
		}

		if (foundBlock instanceof BlockFurnace)
		{
			this.furnacePosition = block.getStartingPosition().getRelativePosition(originalPos, configuration.houseFacing);
		}
		else if (foundBlock instanceof BlockTrapDoor && houseConfig.addMineShaft)
		{
			// The trap door will still be added, but the mine shaft may not be
			// built.
			this.trapDoorPosition = block.getStartingPosition().getRelativePosition(originalPos, configuration.houseFacing);
		}
		else if (foundBlock instanceof BlockChest && this.chestPosition == null)
		{
			this.chestPosition = block.getStartingPosition().getRelativePosition(originalPos, configuration.houseFacing);
		}
		else if (foundBlock instanceof BlockStandingSign)
		{
			this.signPosition = block.getStartingPosition().getRelativePosition(originalPos, configuration.houseFacing);
		}

		if (foundBlock.getRegistryName().getResourceDomain().equals(Blocks.STAINED_GLASS.getRegistryName().getResourceDomain())
				&& foundBlock.getRegistryName().getResourcePath().equals(Blocks.STAINED_GLASS.getRegistryName().getResourcePath()))
		{
			blockState = blockState.withProperty(BlockStainedGlass.COLOR, houseConfig.glassColor);
			block.setBlockState(blockState);
			this.priorityOneBlocks.add(block);

			return true;
		}
		else if (foundBlock.getRegistryName().getResourceDomain().equals(Blocks.STAINED_GLASS_PANE.getRegistryName().getResourceDomain())
				&& foundBlock.getRegistryName().getResourcePath().equals(Blocks.STAINED_GLASS_PANE.getRegistryName().getResourcePath()))
		{
			block.setBlockState(foundBlock.getStateFromMeta(houseConfig.glassColor.getMetadata()));
			this.priorityOneBlocks.add(block);
			return true;
		}

		return false;
	}

	/**
	 * This method is used after the main building is build for any additional
	 * structures or modifications.
	 * 
	 * @param configuration The structure configuration.
	 * @param world The current world.
	 * @param originalPos The original position clicked on.
	 * @param assumedNorth The assumed northern direction.
	 * @param player The player which initiated the construction.
	 */
	@Override
	public void AfterBuilding(StructureConfiguration configuration, World world, BlockPos originalPos, EnumFacing assumedNorth, EntityPlayer player)
	{
		HouseConfiguration houseConfig = (HouseConfiguration) configuration;

		if (this.furnacePosition != null)
		{
			// Fill the furnace.
			TileEntity tileEntity = world.getTileEntity(this.furnacePosition);
			if (tileEntity instanceof TileEntityFurnace)
			{
				TileEntityFurnace furnaceTile = (TileEntityFurnace) tileEntity;
				furnaceTile.setInventorySlotContents(1, new ItemStack(Items.COAL, 20));
			}
		}

		if (this.chestPosition != null)
		{
			// Fill the chest.
			StructureAlternateStart.FillChest(world, this.chestPosition, houseConfig, player);
		}

		if (this.trapDoorPosition != null && this.trapDoorPosition.getY() > 15)
		{
			// Build the mineshaft.
			StructureAlternateStart.PlaceMineShaft(world, this.trapDoorPosition.down(), houseConfig.houseFacing, false);
		}
		
		if (this.signPosition != null)
		{
			TileEntity tileEntity = world.getTileEntity(this.signPosition);

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
	}

	public static void FillChest(World world, BlockPos itemPosition, HouseConfiguration configuration, EntityPlayer player)
	{
		// Add each stone tool to the chest and leather armor.
		TileEntity tileEntity = world.getTileEntity(itemPosition);

		if (tileEntity instanceof TileEntityChest)
		{
			TileEntityChest chestTile = (TileEntityChest) tileEntity;

			int itemSlot = 0;

			// Add the tools.
			if (Prefab.proxy.proxyConfiguration.addAxe)
			{
				chestTile.setInventorySlotContents(itemSlot++, new ItemStack(Items.STONE_AXE));
			}

			if (Prefab.proxy.proxyConfiguration.addHoe)
			{
				chestTile.setInventorySlotContents(itemSlot++, new ItemStack(Items.STONE_HOE));

				// Trigger the "Time to Farm!" achievement.
				player.addStat(AchievementList.BUILD_HOE);
			}

			if (Prefab.proxy.proxyConfiguration.addPickAxe)
			{
				// Trigger the "Time to Mine" achievement and the better
				// pick axe achievement.
				chestTile.setInventorySlotContents(itemSlot++, new ItemStack(Items.STONE_PICKAXE));

				player.addStat(AchievementList.BUILD_PICKAXE);
				player.addStat(AchievementList.BUILD_BETTER_PICKAXE);

				if (configuration.addCraftingTable)
				{
					// If the furnace/crafting table was created, trigger
					// the "Hot Topic" achievement.
					player.addStat(AchievementList.BUILD_FURNACE);
				}
			}

			if (Prefab.proxy.proxyConfiguration.addShovel)
			{
				chestTile.setInventorySlotContents(itemSlot++, new ItemStack(Items.STONE_SHOVEL));
			}

			if (Prefab.proxy.proxyConfiguration.addSword)
			{
				// Include the swift blade if WuestUtilities has registered the
				// swift blades.
				ResourceLocation name = new ResourceLocation("wuestutilities", "itemSwiftBladeStone");
				Item sword = Item.REGISTRY.getObject(name);

				if (sword == null)
				{
					sword = Items.STONE_SWORD;
				}

				chestTile.setInventorySlotContents(itemSlot++, new ItemStack(sword));

				// Trigger the "Time to Strike" achievement.
				player.addStat(AchievementList.BUILD_SWORD);
			}

			if (Prefab.proxy.proxyConfiguration.addArmor)
			{
				// Add the armor.
				chestTile.setInventorySlotContents(itemSlot++, new ItemStack(Items.LEATHER_BOOTS));
				chestTile.setInventorySlotContents(itemSlot++, new ItemStack(Items.LEATHER_CHESTPLATE));
				chestTile.setInventorySlotContents(itemSlot++, new ItemStack(Items.LEATHER_HELMET));
				chestTile.setInventorySlotContents(itemSlot++, new ItemStack(Items.LEATHER_LEGGINGS));
			}

			if (Prefab.proxy.proxyConfiguration.addFood)
			{
				// Add some bread.
				chestTile.setInventorySlotContents(itemSlot++, new ItemStack(Items.BREAD, 20));
				
				player.addStat(AchievementList.MAKE_BREAD);
			}

			if (Prefab.proxy.proxyConfiguration.addCrops)
			{
				// Add potatoes.
				chestTile.setInventorySlotContents(itemSlot++, new ItemStack(Items.POTATO, 3));

				// Add carrots.
				chestTile.setInventorySlotContents(itemSlot++, new ItemStack(Items.CARROT, 3));

				// Add seeds.
				chestTile.setInventorySlotContents(itemSlot++, new ItemStack(Items.WHEAT_SEEDS, 3));
			}

			if (Prefab.proxy.proxyConfiguration.addCobble)
			{
				// Add Cobblestone.
				chestTile.setInventorySlotContents(itemSlot++, new ItemStack(Item.getItemFromBlock(Blocks.COBBLESTONE), 64));
			}

			if (Prefab.proxy.proxyConfiguration.addDirt)
			{
				// Add Dirt.
				chestTile.setInventorySlotContents(itemSlot++, new ItemStack(Item.getItemFromBlock(Blocks.DIRT), 64));
			}

			if (Prefab.proxy.proxyConfiguration.addSaplings)
			{
				// Add oak saplings.
				chestTile.setInventorySlotContents(itemSlot++, new ItemStack(Item.getItemFromBlock(Blocks.SAPLING), 3));
			}

			if (Prefab.proxy.proxyConfiguration.addTorches)
			{
				// Add a set of 20 torches.
				chestTile.setInventorySlotContents(itemSlot++, new ItemStack(Item.getItemFromBlock(Blocks.TORCH), 20));
			}
		}
	}

	public static void PlaceMineShaft(World world, BlockPos pos, EnumFacing facing, boolean onlyGatherOres)
	{
		// Keep track of all of the items to add to the chest at the end of the
		// shaft.
		ArrayList<ItemStack> stacks = new ArrayList<ItemStack>();

		ArrayList<Item> blocksToNotAdd = new ArrayList<Item>();

		if (onlyGatherOres)
		{
			blocksToNotAdd.add(Item.getItemFromBlock(Blocks.SAND));
			blocksToNotAdd.add(Item.getItemFromBlock(Blocks.SANDSTONE));
			blocksToNotAdd.add(Item.getItemFromBlock(Blocks.COBBLESTONE));
			blocksToNotAdd.add(Item.getItemFromBlock(Blocks.STONE));
			blocksToNotAdd.add(Item.getItemFromBlock(Blocks.DIRT));
		}
		
		stacks = StructureAlternateStart.CreateLadderShaft(world, pos, stacks, facing, blocksToNotAdd);

		// Get to Y11;
		pos = pos.down(pos.getY() - 10);
		
		ArrayList<ItemStack> tempStacks = new ArrayList<ItemStack>();
		
		BlockPos ceilingLevel = pos.up(4);
		
		tempStacks = BuildingMethods.SetFloor(world, ceilingLevel.offset(facing, 2).offset(facing.rotateY(), 2).offset(facing.getOpposite()), Blocks.STONE, 4, 4, tempStacks, facing.getOpposite(), blocksToNotAdd);

		// After setting the floor, make sure to replace the ladder.
		BuildingMethods.ReplaceBlock(world, ceilingLevel, Blocks.LADDER.getDefaultState().withProperty(BlockLadder.FACING, facing));
		
		IBlockState torchState = Blocks.TORCH.getStateFromMeta(5);
		
		// Place the torches at this point since the entire shaft has been set.
		for (BlockPos torchPos : StructureAlternateStart.torchPositions)
		{
			IBlockState surroundingState = world.getBlockState(torchPos);
			Block surroundingBlock = surroundingState.getBlock();
			tempStacks = BuildingMethods.ConsolidateDrops( surroundingBlock, world, torchPos, surroundingState, tempStacks, blocksToNotAdd);
			BuildingMethods.ReplaceBlock(world, torchPos, torchState);
		}
		
		// The entire ladder has been created. Create a platform at this level
		// and place a chest next to the ladder.
		tempStacks.addAll(BuildingMethods.SetFloor(world, pos.offset(facing).offset(facing.rotateY()), Blocks.STONE, 3, 4, tempStacks, facing.getOpposite(), blocksToNotAdd));

		// Remove the ladder stack since they shouldn't be getting that.
		for (int i = 0; i < tempStacks.size(); i++)
		{
			ItemStack stack = tempStacks.get(i);

			if (stack.getItem() == Item.getItemFromBlock(Blocks.LADDER))
			{
				tempStacks.remove(i);
				i--;
			}
		}

		// Now that the floor has been set, go up 1 block to star creating the
		// walls.
		pos = pos.up();

		// Clear a space around the ladder pillar and make walls. The walls are
		// necessary if there is a lot of lava down here.
		// Make a wall of air then a wall of stone.

		// South wall.
		tempStacks
				.addAll(BuildingMethods.CreateWall(world, 3, 3, facing.rotateY(), pos.offset(facing.getOpposite(), 2).offset(facing.rotateYCCW()), Blocks.AIR, blocksToNotAdd));
		
		tempStacks.addAll(
				BuildingMethods.CreateWall(world, 3, 3, facing.rotateY(), pos.offset(facing.getOpposite(), 3).offset(facing.rotateYCCW()), Blocks.STONE, blocksToNotAdd));

		// East wall.
		tempStacks.addAll(BuildingMethods.CreateWall(world, 3, 4, facing, pos.offset(facing.getOpposite(), 2).offset(facing.rotateY()), Blocks.AIR, blocksToNotAdd));
		tempStacks.addAll(BuildingMethods.CreateWall(world, 3, 4, facing, pos.offset(facing.getOpposite(), 2).offset(facing.rotateY(), 2), Blocks.STONE, blocksToNotAdd));

		// North wall.
		tempStacks.addAll(BuildingMethods.CreateWall(world, 3, 3, facing.rotateYCCW(), pos.offset(facing).offset(facing.rotateY()), Blocks.AIR, blocksToNotAdd));
		tempStacks.addAll(BuildingMethods.CreateWall(world, 3, 3, facing.rotateYCCW(), pos.offset(facing, 2).offset(facing.rotateY()), Blocks.STONE, blocksToNotAdd));

		// West wall.
		tempStacks.addAll(BuildingMethods.CreateWall(world, 3, 4, facing.getOpposite(), pos.offset(facing).offset(facing.rotateYCCW()), Blocks.AIR, blocksToNotAdd));
		tempStacks.addAll(BuildingMethods.CreateWall(world, 3, 4, facing.getOpposite(), pos.offset(facing, 1).offset(facing.rotateYCCW(), 2), Blocks.STONE, blocksToNotAdd));

		// Consolidate the stacks.
		for (ItemStack tempStack : tempStacks)
		{
			Boolean foundStack = false;

			for (ItemStack existingStack : stacks)
			{
				if (ItemStack.areItemsEqual(existingStack, tempStack))
				{
					// Make sure that this combined stack is at or smaller than
					// the max.
					if (existingStack.stackSize + tempStack.stackSize <= tempStack.getMaxStackSize())
					{
						existingStack.stackSize = existingStack.stackSize + tempStack.stackSize;
						foundStack = true;
						break;
					}
				}
			}

			if (!foundStack)
			{
				stacks.add(tempStack);
			}
		}

		// Place a torch to the left of the ladder.
		IBlockState blockState = Blocks.TORCH.getStateFromMeta(5);
		BuildingMethods.ReplaceBlock(world, pos.offset(facing.rotateYCCW()), blockState);

		if (!Prefab.proxy.proxyConfiguration.enableHouseGenerationRestrictions)
		{
			// Place a chest to the right of the ladder.
			IBlockState chestState = Blocks.CHEST.getDefaultState().withProperty(BlockChest.FACING, facing);
			BuildingMethods.ReplaceBlock(world, pos.offset(facing.rotateY()), chestState);
			
			if (stacks.size() > 27)
			{
				// Add another chest to south of the existing chest.
				BuildingMethods.ReplaceBlock(world, pos.offset(facing.rotateY()).offset(facing.getOpposite()), chestState);
			}
			
			TileEntity tileEntity = world.getTileEntity(pos.offset(facing.rotateY()));
			TileEntity tileEntity2 = world.getTileEntity(pos.offset(facing.rotateY()).offset(facing.getOpposite()));

			if (tileEntity instanceof TileEntityChest)
			{
				TileEntityChest chestTile = (TileEntityChest) tileEntity;
				TileEntityChest chestTile2 = (TileEntityChest) tileEntity2;

				int i = 0;
				boolean fillSecond = false;
				
				// All of the stacks should be consolidated at this point.
				for (ItemStack stack : stacks)
				{
					if (i == 27 && !fillSecond)
					{
						// Start filling the second chest.
						fillSecond = true;
						i = 0;
						chestTile = chestTile2;
					}
					
					if (i >= 27 && fillSecond) 
					{
						// Too many items, discard the rest.
						break;
					}
					else
					{
						chestTile.setInventorySlotContents(i, stack);
					}
					
					i++;
				}
			}
		}
	}
	
	private static ArrayList<ItemStack> CreateLadderShaft(World world, BlockPos pos, ArrayList<ItemStack> originalStacks, EnumFacing houseFacing, ArrayList<Item> blocksToNotAdd)
	{
		int torchCounter = 0;

		// Keep the "west" facing.
		EnumFacing westWall = houseFacing.rotateYCCW();

		// Get the ladder state based on the house facing.
		IBlockState ladderState = Blocks.LADDER.getDefaultState().withProperty(BlockLadder.FACING, houseFacing);

		// Replace the main floor block with air since we don't want it placed in the chest at the end.
		BuildingMethods.ReplaceBlock(world, pos, Blocks.AIR);
		StructureAlternateStart.torchPositions = new ArrayList<BlockPos>();
		
		while (pos.getY() > 8)
		{
			IBlockState state = world.getBlockState(pos);
			Block block = state.getBlock();
			torchCounter++;

			// Make sure all blocks around this one are solid, if they are not
			// replace them with stone.
			for (int i = 0; i < 4; i++)
			{
				EnumFacing facing = houseFacing;

				switch (i)
				{
					case 1:
					{
						facing = houseFacing.rotateY();
						break;
					}
					case 2:
					{
						facing = houseFacing.getOpposite();
						break;
					}
					case 3:
					{
						facing = houseFacing.rotateYCCW();
						break;
					}
					default:
					{
						facing = houseFacing;
					}
				}

				// Every 6 blocks, place a torch on the west wall.
				// If we are close to the bottom, don't place a torch. Do the
				// normal processing.
				if (facing == westWall && torchCounter == 6 && pos.getY() > 14)
				{
					// First make sure the blocks around this block are stone, then place the torch.
					for (int j = 0; j <= 2; j++)
					{
						BlockPos tempPos = null;
						IBlockState surroundingState = null;
						Block surroundingBlock = null;
						
						if (j == 0)
						{
							tempPos = pos.offset(facing, 2);
							surroundingState = world.getBlockState(tempPos);
							surroundingBlock = surroundingState.getBlock();
						}
						else if (j == 1)
						{
							tempPos = pos.offset(facing).offset(facing.rotateY());
							surroundingState = world.getBlockState(tempPos);
							surroundingBlock = surroundingState.getBlock();
						}
						else
						{
							tempPos = pos.offset(facing).offset(facing.rotateYCCW());
							surroundingState = world.getBlockState(tempPos);
							surroundingBlock = surroundingState.getBlock();
						}
						
						// Make sure that this is a normal solid block and not a liquid or partial block.
						if (!(surroundingBlock instanceof BlockStone))
						{
							// This is not a stone block. Get the drops then replace it with stone.
							originalStacks = BuildingMethods.ConsolidateDrops(surroundingBlock, world, tempPos, surroundingState, originalStacks, blocksToNotAdd);

							BuildingMethods.ReplaceBlock(world, tempPos, Blocks.STONE);
						}
					}

					StructureAlternateStart.torchPositions.add(pos.offset(facing));
					torchCounter = 0;
				}
				else
				{
					BlockPos tempPos = pos.offset(facing);
					IBlockState surroundingState = world.getBlockState(tempPos);
					Block surroundingBlock = surroundingState.getBlock();

					if (!surroundingBlock.isBlockNormalCube(surroundingState)
							|| surroundingBlock instanceof BlockLiquid)
					{
						// This is not a solid block. Get the drops then replace
						// it with stone.
						originalStacks = BuildingMethods.ConsolidateDrops(surroundingBlock, world, tempPos, surroundingState, originalStacks, blocksToNotAdd);

						BuildingMethods.ReplaceBlock(world, tempPos, Blocks.STONE);
					}
				}
			}

			// Get the block drops then replace it with a ladder.
			originalStacks = BuildingMethods.ConsolidateDrops(block, world, pos, state, originalStacks, blocksToNotAdd);

			// Don't place a ladder at this location since it will be destroyed.
			if (pos.getY() >= 10)
			{
				BuildingMethods.ReplaceBlock(world, pos, ladderState);
			}

			pos = pos.down();
		}
		
		return originalStacks;
	}
}
