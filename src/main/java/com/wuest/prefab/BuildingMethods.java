package com.wuest.prefab;

import java.lang.*;
import java.util.ArrayList;

import com.wuest.prefab.Config.HouseConfiguration;
import com.wuest.prefab.Config.StructureConfiguration;

import net.minecraft.block.Block;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.BlockTorch;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.BlockSnapshot;
import net.minecraftforge.event.world.BlockEvent;

/**
 * This class is used to hold he generalized building methods used by the
 * starting house.
 * 
 * @author WuestMan
 *
 */
public class BuildingMethods
{
	/**
	 * Clears the given space without returning any of the drops.
	 * 
	 * @param world - The world used to clear the space.
	 * @param startingPosition - The starting position, note the width, depth
	 *            will be used to determine the starting corner(s).
	 * @param width - The radius x-axis of how wide to clear. (East/West)
	 * @param height - How high from the starting position to clear (this
	 *            includes starting position).
	 * @param depth - The radius z-axis of how deep to clear. (North/South)
	 */
	public static void ClearSpace(World world, BlockPos startingPosition, int width, int height, int depth, EnumFacing houseFacing)
	{
		BlockPos northSide = startingPosition.offset(houseFacing, 5).offset(houseFacing.rotateY(), 5);
		int clearedWidth = width + 12;
		int wallLength = depth + 12;

		for (int i = 0; i < clearedWidth; i++)
		{
			BuildingMethods.CreateWall(world, height, wallLength, houseFacing.getOpposite(), northSide, Blocks.AIR);
			
			northSide = northSide.offset(houseFacing.rotateYCCW());
		}
	}
	
	/**
	 * Clears the given space without returning any of the drops.
	 * 
	 * @param world - The world used to clear the space.
	 * @param startingPosition - The starting position, note the width, depth
	 *            will be used to determine the starting corner(s).
	 * @param width - The radius x-axis of how wide to clear. (East/West)
	 * @param height - How high from the starting position to clear (this
	 *            includes starting position).
	 * @param depth - The radius z-axis of how deep to clear. (North/South)
	 */
	public static void ClearSpaceExact(World world, BlockPos startingPosition, int width, int height, int depth, EnumFacing houseFacing)
	{
		BlockPos otherCorner = startingPosition.offset(houseFacing.getOpposite(), depth).offset(houseFacing.rotateYCCW(), width);

		for (BlockPos pos : BlockPos.getAllInBox(startingPosition, otherCorner))
		{
			if (!world.isAirBlock(pos))
			{
				BuildingMethods.ReplaceBlock(world, pos, Blocks.AIR);
			}
		}
	}

	public static ArrayList<ItemStack> ConsolidateDrops(Block block, World world, BlockPos pos, IBlockState state, ArrayList<ItemStack> originalStacks)
	{
		for (ItemStack stack : block.getDrops(world, pos, state, 1))
		{
			// Check to see if this stack's item is equal to an existing item
			// stack. If it is just add the count.
			Boolean foundStack = false;

			for (ItemStack existingStack : originalStacks)
			{
				if (ItemStack.areItemsEqual(existingStack, stack))
				{
					// Make sure that this combined stack is at or smaller than
					// the max.
					if (existingStack.stackSize + stack.stackSize <= stack.getMaxStackSize())
					{
						existingStack.stackSize = existingStack.stackSize + stack.stackSize;
						foundStack = true;
						break;
					}
				}
			}

			if (!foundStack)
			{
				originalStacks.add(stack);
			}
		}

		return originalStacks;
	}

	public static ArrayList<ItemStack> CreateWall(World world, int height, int length, EnumFacing direction, BlockPos startingPosition, Block replacementBlock)
	{
		ArrayList<ItemStack> itemsDropped = new ArrayList<ItemStack>();

		BlockPos wallPos = null;

		// i height, j is the actual wall counter.
		for (int i = 0; i < height; i++)
		{
			// Reset wall building position to the starting position up by the
			// height counter.
			wallPos = startingPosition.up(i);

			for (int j = 0; j < length; j++)
			{
				for (ItemStack stack : world.getBlockState(wallPos).getBlock().getDrops(world, wallPos, world.getBlockState(wallPos), 1))
				{
					itemsDropped.add(stack);
				}

				// j is the north/south counter.
				BuildingMethods.ReplaceBlock(world, wallPos, replacementBlock);

				wallPos = wallPos.offset(direction);
			}
		}

		return itemsDropped;
	}

	public static ArrayList<ItemStack> CreateWall(World world, int height, int length, EnumFacing direction, BlockPos startingPosition,
			IBlockState replacementBlock)
	{
		ArrayList<ItemStack> itemsDropped = new ArrayList<ItemStack>();

		BlockPos wallPos = null;

		// i height, j is the actual wall counter.
		for (int i = 0; i < height; i++)
		{
			// Reset wall building position to the starting position up by the
			// height counter.
			wallPos = startingPosition.up(i);

			for (int j = 0; j < length; j++)
			{
				for (ItemStack stack : world.getBlockState(wallPos).getBlock().getDrops(world, wallPos, world.getBlockState(wallPos), 1))
				{
					itemsDropped.add(stack);
				}

				// j is the north/south counter.
				BuildingMethods.ReplaceBlock(world, wallPos, replacementBlock);

				wallPos = wallPos.offset(direction);
			}
		}

		return itemsDropped;
	}

	public static ArrayList<ItemStack> SetFloor(World world, BlockPos pos, Block block, int width, int depth, ArrayList<ItemStack> originalStack, EnumFacing facing)
	{
		for (int i = 0; i < width; i++)
		{
			originalStack.addAll(BuildingMethods.CreateWall(world, 1, depth, facing, pos, block));

			pos = pos.offset(facing.rotateY());
		}

		return originalStack;
	}

	public static void SetCeiling(World world, BlockPos pos, Block block, int width, int depth, Block stairs, HouseConfiguration configuration, EnumFacing houseFacing)
	{
		// If the ceiling is flat, call SetFloor since it's laid out the same.
		if (configuration.isCeilingFlat)
		{
			BuildingMethods.SetFloor(world, pos, block, width, depth, new ArrayList<ItemStack>(), houseFacing.getOpposite());
			return;
		}

		// Get the stairs state without the facing since it will change.
		IBlockState stateWithoutFacing = stairs.getBlockState().getBaseState().withProperty(BlockStairs.HALF, BlockStairs.EnumHalf.BOTTOM)
				.withProperty(BlockStairs.SHAPE, BlockStairs.EnumShape.STRAIGHT);

		int wallWidth = configuration.houseWidth;
		int wallDepth = configuration.houseDepth;
		int height = wallWidth / 2;
		boolean isWider = false;
		
		if (wallWidth > wallDepth)
		{
			height = wallDepth / 2;
			isWider = true;
		}

		for (int i = 0; i <= height; i++)
		{
			// I is the vaulted roof level.
			for (int j = 0; j < 4; j++)
			{
				// Default is depth.
				EnumFacing facing = houseFacing.rotateYCCW();
				EnumFacing flowDirection = houseFacing.getOpposite();
				int wallSize = wallDepth;

				switch (j)
				{
					case 1:
					{
						facing = houseFacing;
						flowDirection = houseFacing.rotateYCCW();
						wallSize = wallWidth;
						break;
					}

					case 2:
					{
						facing = houseFacing.rotateY();
						flowDirection = houseFacing;
						wallSize = wallDepth;
						break;
					}

					case 3:
					{
						facing = houseFacing.getOpposite();
						flowDirection = houseFacing.rotateY();
						wallSize = wallWidth;
						break;
					}
				}

				for (int k = 0; k <= wallSize; k++)
				{
					// j is the north/south counter.
					BuildingMethods.ReplaceBlock(world, pos, stateWithoutFacing.withProperty(BlockStairs.FACING, facing));

					pos = pos.offset(flowDirection);
				}
			}

			pos = pos.offset(houseFacing.rotateYCCW()).offset(houseFacing.getOpposite()).up();
			wallWidth = wallWidth - 2;
			wallDepth = wallDepth - 2;
		}

		if (world.isAirBlock(pos.down()))
		{
			int finalStoneCount = wallDepth;
			
			if (isWider)
			{
				finalStoneCount = wallWidth;
			}
			
			// Add the number of blocks based on the depth/width (minimum 1);
			if (finalStoneCount < 1)
			{
				finalStoneCount = 1;
			}
			else
			{
				finalStoneCount = finalStoneCount + 2;
			}
			
			BlockPos torchPos = pos;
			
			for (int i = 0; i < finalStoneCount; i++)
			{
				BuildingMethods.ReplaceBlock(world, pos, block);
				
				if (isWider)
				{
					pos = pos.offset(houseFacing.rotateYCCW());
				}
				else
				{
					pos = pos.offset(houseFacing.getOpposite());	
				}
			}
			
			IBlockState torchLocation = world.getBlockState(torchPos);
			
			if (torchLocation.getBlock().canPlaceTorchOnTop(torchLocation, world, torchPos))
			{
				IBlockState blockState = ((BlockTorch)Blocks.TORCH).getStateFromMeta(5);
				BuildingMethods.ReplaceBlock(world, torchPos.up(), blockState);
			}
		}
	}

	/**
	 * Replaces a block at the given position in the world. The block in the pos will be replaced with air before placing the block;
	 * @param world The world object.
	 * @param pos The position to update.
	 * @param replacementBlock The block object to place at this position. The default state is used.
	 */
	public static void ReplaceBlock(World world, BlockPos pos, Block replacementBlock)
	{
		BuildingMethods.ReplaceBlock(world, pos, replacementBlock.getDefaultState(), 3);
	}
	
	/**
	 * Replaces a block at the given position in the world. This is faster as the position is not set to air first.
	 * @param world The world object.
	 * @param pos The position to update.
	 * @param replacementBlock The block object to place at this position. The default state is used.
	 */
	public static void ReplaceBlockNoAir(World world, BlockPos pos, Block replacementBlock)
	{
		BuildingMethods.ReplaceBlockNoAir(world, pos, replacementBlock.getDefaultState(), 3);
	}

	/**
	 * Replaces a block at the given position in the world. The block in the pos will be replaced with air before placing the block;
	 * @param world The world object.
	 * @param pos The position to update.
	 * @param replacementBlockState The block state to place at this position.
	 */
	public static void ReplaceBlock(World world, BlockPos pos, IBlockState replacementBlockState)
	{
		BuildingMethods.ReplaceBlock(world, pos, replacementBlockState, 3);
	}
	
	/**
	 * Replaces a block at the given position in the world. This is faster as the position is not set to air first.
	 * @param world The world object.
	 * @param pos The position to update.
	 * @param replacementBlockState The block state to place at this position.
	 */
	public static void ReplaceBlockNoAir(World world, BlockPos pos, IBlockState replacementBlockState)
	{
		BuildingMethods.ReplaceBlockNoAir(world, pos, replacementBlockState, 3);
	}

	/**
	 * 
	 * Replaces a block at the given position in the world. The block in the pos will be replaced with air before placing the block;
	 * @param world The world object.
	 * @param pos The position to update.
	 * @param replacementBlockState The block state to place at this position.
	 * @param flags The trigger flags, this should always be set to 3 so the clients are updated.
	 */
	public static void ReplaceBlock(World world, BlockPos pos, IBlockState replacementBlockState, int flags)
	{
		world.setBlockToAir(pos);
		world.setBlockState(pos, replacementBlockState, flags);
	}
	
	/**
	 * Replaces a block at the given position in the world. This is faster as the position is not set to air first.
	 * @param world The world object.
	 * @param pos The position to update.
	 * @param replacementBlockState The block state to place at this position.
	 * @param flags The trigger flags, this should always be set to 3 so the clients are updated.
	 */
	public static void ReplaceBlockNoAir(World world, BlockPos pos, IBlockState replacementBlockState, int flags)
	{
		world.setBlockState(pos, replacementBlockState, flags);
	}
	
	/**
	 * Gets the integer representation of a torch facing.
	 * @param facing The facing that the torch should look.
	 * @return The integer used for the block state of a BlockTorch object.
	 */
	public static int GetTorchFacing(EnumFacing facing)
	{
		/*
		 * Torch Facings: 1 = East, 2 = West, 3 = South, 4 = North, 5 = Up
		 */
		switch (facing)
		{
			case NORTH:
			{
				return 4;
			}
			
			case EAST:
			{
				return 1;
			}
			
			case SOUTH:
			{
				return 3;
			}
			
			case WEST:
			{
				return 2;
			}
			
			default:
			{
				return 5;
			}
		}
	}

	/**
	 * This method is used to determine if the player can build the structure.
	 * 
	 * @param configuration The structure configuration.
	 * @param world The world to build the structure in.
	 * @param originalPos The original block position.
	 * @param assumedNorth The assumed north of the structure (usually just
	 *            EnumFacing.North).
	 * @param player The player running this build request.
	 * @return True if all blocks can be replaced. Otherwise false and send a
	 *         message to the player.
	 */
	public static boolean CheckBuildSpaceForAllowedBlockReplacement(StructureConfiguration configuration, World world, BlockPos startBlockPos, BlockPos endBlockPos, EntityPlayer player)
	{
		// Check each block in the space to be cleared if it's protected from
		// breaking or placing, if it is return false.
		for (BlockPos currentPos : BlockPos.getAllInBox(startBlockPos, endBlockPos))
		{
			IBlockState blockState = world.getBlockState(currentPos);
			
			if (!blockState.getBlock().isAir(blockState, world, currentPos))
			{
				BlockEvent.BreakEvent breakEvent = new BlockEvent.BreakEvent(world, currentPos, world.getBlockState(currentPos), player);
			
				if (MinecraftForge.EVENT_BUS.post(breakEvent))
				{
					return false;
				}
			}
			 
			BlockEvent.PlaceEvent placeEvent = new BlockEvent.PlaceEvent(new BlockSnapshot(world, currentPos, blockState), Blocks.AIR.getDefaultState(), player);

			if (MinecraftForge.EVENT_BUS.post(placeEvent))
			{
				return false;
			}
		}

		return true;
	}
}
