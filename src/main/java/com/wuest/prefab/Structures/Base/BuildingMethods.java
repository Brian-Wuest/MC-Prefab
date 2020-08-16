package com.wuest.prefab.Structures.Base;

import com.wuest.prefab.Proxy.CommonProxy;
import com.wuest.prefab.Triple;
import com.wuest.prefab.Tuple;
import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.state.properties.BedPart;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.tileentity.FurnaceTileEntity;
import net.minecraft.tileentity.LockableLootTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DimensionType;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.BlockSnapshot;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.BlockEvent.EntityPlaceEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;

/**
 * This class is used to hold he generalized building methods used by the starting house.
 *
 * @author WuestMan
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class BuildingMethods {
	/**
	 * This method consolidate drops for the current block into an existing ArrayList
	 *
	 * @param world          The world which the block resides.
	 * @param pos            The block position.
	 * @param state          The current block state.
	 * @param originalStacks The original list of stacks.
	 * @param itemsToNotAdd  The items to not add to the list.
	 * @return An updated list of item stacks.
	 */
	public static ArrayList<ItemStack> ConsolidateDrops(ServerWorld world, BlockPos pos, BlockState state, ArrayList<ItemStack> originalStacks,
														ArrayList<Item> itemsToNotAdd) {
		for (ItemStack stack : Block.getDrops(state, world, pos, null)) {
			if (itemsToNotAdd != null) {
				if (itemsToNotAdd.contains(stack.getItem())) {
					continue;
				}
			}

			// Check to see if this stack's item is equal to an existing item
			// stack. If it is just add the count.
			boolean foundStack = false;

			for (ItemStack existingStack : originalStacks) {
				if (ItemStack.areItemsEqual(existingStack, stack)) {
					// Make sure that this combined stack is at or smaller than
					// the max.
					if (existingStack.getCount() + stack.getCount() <= stack.getMaxStackSize()) {
						existingStack.setCount(existingStack.getCount() + stack.getCount());
						foundStack = true;
						break;
					}
				}
			}

			if (!foundStack) {
				originalStacks.add(stack);
			}
		}

		return originalStacks;
	}

	/**
	 * Creates a wall of blocks.
	 *
	 * @param world            The world to create the wall.
	 * @param height           The height of the wall.
	 * @param length           The length of the wall.
	 * @param direction        The direction of the wall.
	 * @param startingPosition Where the wall should start.
	 * @param replacementBlock The block to create the wall out of.
	 * @param itemsToNotAdd    When consolidating drops, the items to not include in the returned list.
	 * @return An Arraylist of Itemstacks which contains the drops from any destroyed blocks.
	 */
	public static ArrayList<ItemStack> CreateWall(ServerWorld world, int height, int length, Direction direction, BlockPos startingPosition, Block replacementBlock,
												  ArrayList<Item> itemsToNotAdd) {
		ArrayList<ItemStack> itemsDropped = new ArrayList<>();

		BlockPos wallPos = null;

		// i height, j is the actual wall counter.
		for (int i = 0; i < height; i++) {
			// Reset wall building position to the starting position up by the
			// height counter.
			wallPos = startingPosition.up(i);

			for (int j = 0; j < length; j++) {
				BlockState currentBlockPosState = world.getBlockState(wallPos);

				for (ItemStack stack : Block.getDrops(currentBlockPosState, world, wallPos, null)) {
					if (itemsToNotAdd != null && itemsToNotAdd.contains(stack.getItem())) {
						continue;
					}

					itemsDropped.add(stack);
				}

				// j is the north/south counter.
				BuildingMethods.ReplaceBlock(world, wallPos, replacementBlock);

				wallPos = wallPos.offset(direction);
			}
		}

		return itemsDropped;
	}

	/**
	 * Creates a square floor of blocks.
	 *
	 * @param world         The world to create the floor in.
	 * @param pos           The block position to start creating the floor.
	 * @param block         The Type of block to create the floor out of.
	 * @param width         The width of the floor.
	 * @param depth         The length of the floor.
	 * @param originalStack The original stack of items from previously harvested blocks.
	 * @param facing        The direction of the floor.
	 * @param itemsToNotAdd The items to not include in the returned consolidated items.
	 * @return An ArrayList of Itemstacks which contains the drops from all harvested blocks.
	 */
	public static ArrayList<ItemStack> SetFloor(ServerWorld world, BlockPos pos, Block block, int width, int depth, ArrayList<ItemStack> originalStack, Direction facing,
												ArrayList<Item> itemsToNotAdd) {
		for (int i = 0; i < width; i++) {
			originalStack.addAll(BuildingMethods.CreateWall(world, 1, depth, facing, pos, block, itemsToNotAdd));

			pos = pos.offset(facing.rotateY());
		}

		return originalStack;
	}

	/**
	 * Replaces a block at the given position in the world. The block in the pos will be replaced with air before
	 * placing the block;
	 *
	 * @param world            The world object.
	 * @param pos              The position to update.
	 * @param replacementBlock The block object to place at this position. The default state is used.
	 */
	public static void ReplaceBlock(World world, BlockPos pos, Block replacementBlock) {
		BuildingMethods.ReplaceBlock(world, pos, replacementBlock.getDefaultState(), 3);
	}

	/**
	 * Replaces a block at the given position in the world. This is faster as the position is not set to air first.
	 *
	 * @param world            The world object.
	 * @param pos              The position to update.
	 * @param replacementBlock The block object to place at this position. The default state is used.
	 */
	public static void ReplaceBlockNoAir(World world, BlockPos pos, Block replacementBlock) {
		BuildingMethods.ReplaceBlockNoAir(world, pos, replacementBlock.getDefaultState(), 3);
	}

	/**
	 * Replaces a block at the given position in the world. The block in the pos will be replaced with air before
	 * placing the block;
	 *
	 * @param world                 The world object.
	 * @param pos                   The position to update.
	 * @param replacementBlockState The block state to place at this position.
	 */
	public static void ReplaceBlock(World world, BlockPos pos, BlockState replacementBlockState) {
		BuildingMethods.ReplaceBlock(world, pos, replacementBlockState, 3);
	}

	/**
	 * Replaces a block at the given position in the world. The block in the pos will be replaced with air before
	 * placing the block;
	 *
	 * @param world                 The world object.
	 * @param pos                   The position to update.
	 * @param replacementBlockState The block state to place at this position.
	 * @param flags                 The trigger flags, this should always be set to 3 so the clients are updated.
	 */
	public static void ReplaceBlock(World world, BlockPos pos, BlockState replacementBlockState, int flags) {
		world.removeBlock(pos, false);
		world.setBlockState(pos, replacementBlockState, flags);
	}

	/**
	 * Replaces a block at the given position in the world. This is faster as the position is not set to air first.
	 *
	 * @param world                 The world object.
	 * @param pos                   The position to update.
	 * @param replacementBlockState The block state to place at this position.
	 * @param flags                 The trigger flags, this should always be set to 3 so the clients are updated.
	 */
	public static void ReplaceBlockNoAir(World world, BlockPos pos, BlockState replacementBlockState, int flags) {
		world.setBlockState(pos, replacementBlockState, flags);
	}

	/**
	 * This method is used to determine if the player can build the structure.
	 *
	 * @param world         The world to build the structure in.
	 * @param startBlockPos The start location to start checking blocks.
	 * @param endBlockPos   The end location for checking blocks. Combined with the startBlockPos, this should be a
	 *                      cube.
	 * @param player        The player running this build request.
	 * @return True if all blocks can be replaced. Otherwise false and send a message to the player.
	 */
	public static Triple<Boolean, BlockState, BlockPos> CheckBuildSpaceForAllowedBlockReplacement(ServerWorld world, BlockPos startBlockPos, BlockPos endBlockPos,
																								  PlayerEntity player) {
		if (!world.isRemote) {
			// Check each block in the space to be cleared if it's protected from
			// breaking or placing, if it is return false.
			for (BlockPos currentPos : BlockPos.getAllInBoxMutable(startBlockPos, endBlockPos)) {
				BlockState blockState = world.getBlockState(currentPos);

				// First check to see if this is a spawn protected block.
				if (world.getServer().isBlockProtected(world, currentPos, player)) {
					// This block is protected by vanilla spawn protection. Don't allow building here.
					return new Triple<>(false, blockState, currentPos);
				}

				if (!blockState.getBlock().isAir(blockState, world, currentPos)) {
					BlockEvent.BreakEvent breakEvent = new BlockEvent.BreakEvent(world, currentPos, world.getBlockState(currentPos), player);

					if (MinecraftForge.EVENT_BUS.post(breakEvent)) {
						return new Triple<>(false, blockState, currentPos);
					}
				}

				// TODO: world.func_234923_W_ gets the RegistryKey<World> for the current world.
				EntityPlaceEvent placeEvent = new EntityPlaceEvent(BlockSnapshot.create(world.func_234923_W_(), world, currentPos), Blocks.AIR.getDefaultState(), player);

				if (MinecraftForge.EVENT_BUS.post(placeEvent)) {
					return new Triple<>(false, blockState, currentPos);
				}

				// A hardness of less than 0 is unbreakable.
				if (blockState.getBlockHardness(world, currentPos) < 0.0f) {
					// This is bedrock or some other type of unbreakable block. Don't allow this block to be broken by a
					// structure.
					return new Triple<>(false, blockState, currentPos);
				}
			}
		}

		return new Triple<>(true, null, null);
	}

	/**
	 * This method places a bed with the specified color and at the specified location.
	 *
	 * @param world      The world to set the blocks in.
	 * @param bedHeadPos The position of the head of the bed.
	 * @param bedFootPos The position of the foot of the bed.
	 * @param bedColor   The color of the bed to place.
	 */
	public static void PlaceColoredBed(World world, BlockPos bedHeadPos, BlockPos bedFootPos, DyeColor bedColor) {
		BlockState bedHead = null;
		BlockState bedFoot = null;

		switch (bedColor) {
			case BLACK: {
				bedHead = Blocks.BLACK_BED.getDefaultState().with(BedBlock.PART, BedPart.HEAD);
				bedFoot = Blocks.BLACK_BED.getDefaultState();
				break;
			}
			case BLUE: {
				bedHead = Blocks.BLUE_BED.getDefaultState().with(BedBlock.PART, BedPart.HEAD);
				bedFoot = Blocks.BLUE_BED.getDefaultState();
				break;
			}

			case BROWN: {
				bedHead = Blocks.BROWN_BED.getDefaultState().with(BedBlock.PART, BedPart.HEAD);
				bedFoot = Blocks.BROWN_BED.getDefaultState();
				break;
			}

			case CYAN: {
				bedHead = Blocks.CYAN_BED.getDefaultState().with(BedBlock.PART, BedPart.HEAD);
				bedFoot = Blocks.CYAN_BED.getDefaultState();
				break;
			}

			case GRAY: {
				bedHead = Blocks.GRAY_BED.getDefaultState().with(BedBlock.PART, BedPart.HEAD);
				bedFoot = Blocks.GRAY_BED.getDefaultState();
				break;
			}

			case GREEN: {
				bedHead = Blocks.GREEN_BED.getDefaultState().with(BedBlock.PART, BedPart.HEAD);
				bedFoot = Blocks.GREEN_BED.getDefaultState();
				break;
			}

			case LIGHT_BLUE: {
				bedHead = Blocks.LIGHT_BLUE_BED.getDefaultState().with(BedBlock.PART, BedPart.HEAD);
				bedFoot = Blocks.LIGHT_BLUE_BED.getDefaultState();
				break;
			}

			case LIGHT_GRAY: {
				bedHead = Blocks.LIGHT_GRAY_BED.getDefaultState().with(BedBlock.PART, BedPart.HEAD);
				bedFoot = Blocks.LIGHT_GRAY_BED.getDefaultState();
				break;
			}

			case LIME: {
				bedHead = Blocks.LIME_BED.getDefaultState().with(BedBlock.PART, BedPart.HEAD);
				bedFoot = Blocks.LIME_BED.getDefaultState();
				break;
			}

			case MAGENTA: {
				bedHead = Blocks.MAGENTA_BED.getDefaultState().with(BedBlock.PART, BedPart.HEAD);
				bedFoot = Blocks.MAGENTA_BED.getDefaultState();
				break;
			}

			case ORANGE: {
				bedHead = Blocks.ORANGE_BED.getDefaultState().with(BedBlock.PART, BedPart.HEAD);
				bedFoot = Blocks.ORANGE_BED.getDefaultState();
				break;
			}

			case PINK: {
				bedHead = Blocks.PINK_BED.getDefaultState().with(BedBlock.PART, BedPart.HEAD);
				bedFoot = Blocks.PINK_BED.getDefaultState();
				break;
			}

			case PURPLE: {
				bedHead = Blocks.PURPLE_BED.getDefaultState().with(BedBlock.PART, BedPart.HEAD);
				bedFoot = Blocks.PURPLE_BED.getDefaultState();
				break;
			}

			case RED: {
				bedHead = Blocks.RED_BED.getDefaultState().with(BedBlock.PART, BedPart.HEAD);
				bedFoot = Blocks.RED_BED.getDefaultState();
				break;
			}

			case WHITE: {
				bedHead = Blocks.WHITE_BED.getDefaultState().with(BedBlock.PART, BedPart.HEAD);
				bedFoot = Blocks.WHITE_BED.getDefaultState();
				break;
			}

			case YELLOW: {
				bedHead = Blocks.YELLOW_BED.getDefaultState().with(BedBlock.PART, BedPart.HEAD);
				bedFoot = Blocks.YELLOW_BED.getDefaultState();
				break;
			}
		}

		Direction direction = Direction.NORTH;
		BlockPos tempPos = bedHeadPos.offset(Direction.NORTH);

		while (tempPos.getX() != bedFootPos.getX() || tempPos.getZ() != bedFootPos.getZ()) {
			direction = direction.rotateY();
			tempPos = bedHeadPos.offset(direction);
		}

		bedHead = bedHead.with(BedBlock.HORIZONTAL_FACING, direction.getOpposite());
		bedFoot = bedFoot.with(BedBlock.HORIZONTAL_FACING, direction.getOpposite());

		BuildingMethods.ReplaceBlock(world, bedHeadPos, bedHead);
		BuildingMethods.ReplaceBlock(world, bedFootPos, bedFoot);
	}

	/**
	 * Fills a chest with basic tools and items.
	 *
	 * @param world        - The world where the chest resides.
	 * @param itemPosition - The block position of the chest.
	 */
	public static void FillChest(World world, BlockPos itemPosition) {
		// Add each stone tool to the chest and leather armor.
		TileEntity tileEntity = world.getTileEntity(itemPosition);

		if (tileEntity instanceof LockableLootTileEntity) {
			LockableLootTileEntity chestTile = (LockableLootTileEntity) tileEntity;

			int itemSlot = 0;

			// Add the tools.
			if (CommonProxy.proxyConfiguration.serverConfiguration.addAxe) {
				chestTile.setInventorySlotContents(itemSlot++, new ItemStack(Items.STONE_AXE));
			}

			if (CommonProxy.proxyConfiguration.serverConfiguration.addHoe) {
				chestTile.setInventorySlotContents(itemSlot++, new ItemStack(Items.STONE_HOE));
			}

			if (CommonProxy.proxyConfiguration.serverConfiguration.addPickAxe) {
				chestTile.setInventorySlotContents(itemSlot++, new ItemStack(Items.STONE_PICKAXE));
			}

			if (CommonProxy.proxyConfiguration.serverConfiguration.addShovel) {
				chestTile.setInventorySlotContents(itemSlot++, new ItemStack(Items.STONE_SHOVEL));
			}

			if (CommonProxy.proxyConfiguration.serverConfiguration.addSword) {
				Item sword = Items.STONE_SWORD;

				if (ModList.get().isLoaded("repurpose")) {
					ResourceLocation name = new ResourceLocation("repurpose", "item_swift_blade_stone");

					if (ForgeRegistries.ITEMS.containsKey(name)) {
						sword = ForgeRegistries.ITEMS.getValue(name);
					}
				}

				chestTile.setInventorySlotContents(itemSlot++, new ItemStack(sword));
			}

			if (CommonProxy.proxyConfiguration.serverConfiguration.addArmor) {
				// Add the armor.
				chestTile.setInventorySlotContents(itemSlot++, new ItemStack(Items.LEATHER_BOOTS));
				chestTile.setInventorySlotContents(itemSlot++, new ItemStack(Items.LEATHER_CHESTPLATE));
				chestTile.setInventorySlotContents(itemSlot++, new ItemStack(Items.LEATHER_HELMET));
				chestTile.setInventorySlotContents(itemSlot++, new ItemStack(Items.LEATHER_LEGGINGS));
			}

			if (CommonProxy.proxyConfiguration.serverConfiguration.addFood) {
				// Add some bread.
				chestTile.setInventorySlotContents(itemSlot++, new ItemStack(Items.BREAD, 20));
			}

			if (CommonProxy.proxyConfiguration.serverConfiguration.addCrops) {
				// Add potatoes.
				chestTile.setInventorySlotContents(itemSlot++, new ItemStack(Items.POTATO, 3));

				// Add carrots.
				chestTile.setInventorySlotContents(itemSlot++, new ItemStack(Items.CARROT, 3));

				// Add seeds.
				chestTile.setInventorySlotContents(itemSlot++, new ItemStack(Items.WHEAT_SEEDS, 3));
			}

			if (CommonProxy.proxyConfiguration.serverConfiguration.addCobble) {
				// Add Cobblestone.
				chestTile.setInventorySlotContents(itemSlot++, new ItemStack(Item.getItemFromBlock(Blocks.COBBLESTONE), 64));
			}

			if (CommonProxy.proxyConfiguration.serverConfiguration.addDirt) {
				// Add Dirt.
				chestTile.setInventorySlotContents(itemSlot++, new ItemStack(Item.getItemFromBlock(Blocks.DIRT), 64));
			}

			if (CommonProxy.proxyConfiguration.serverConfiguration.addSaplings) {
				// Add oak saplings.
				chestTile.setInventorySlotContents(itemSlot++, new ItemStack(Item.getItemFromBlock(Blocks.OAK_SAPLING), 3));
			}

			if (CommonProxy.proxyConfiguration.serverConfiguration.addTorches) {
				// Add a set of 20 torches.
				chestTile.setInventorySlotContents(itemSlot++, new ItemStack(Item.getItemFromBlock(Blocks.TORCH), 20));
			}
		}
	}

	/**
	 * Fills the furnaces with an amount of coal.
	 *
	 * @param world            The world where the furnaces reside.
	 * @param furnacePositions A collection of furnace positions.
	 */
	public static void FillFurnaces(World world, ArrayList<BlockPos> furnacePositions) {
		if (furnacePositions != null && furnacePositions.size() > 0) {
			for (BlockPos furnacePos : furnacePositions) {
				// Fill the furnace.
				TileEntity tileEntity = world.getTileEntity(furnacePos);

				if (tileEntity instanceof FurnaceTileEntity) {
					FurnaceTileEntity furnaceTile = (FurnaceTileEntity) tileEntity;
					furnaceTile.setInventorySlotContents(1, new ItemStack(Items.COAL, 20));
				}
			}
		}
	}

	/**
	 * Places a mineshaft with ladder in the world.
	 *
	 * @param world          - The world where the mineshaft will be added.
	 * @param pos            - The starting position for the mineshaft.
	 * @param facing         - The direction where the ladder will be placed.
	 * @param onlyGatherOres - Determines if vanilla non-ore blocks will be gathered.
	 */
	public static void PlaceMineShaft(ServerWorld world, BlockPos pos, Direction facing, boolean onlyGatherOres) {
		// Keep track of all of the items to add to the chest at the end of the
		// shaft.
		ArrayList<ItemStack> stacks = new ArrayList<ItemStack>();

		ArrayList<Item> blocksToNotAdd = new ArrayList<Item>();

		if (onlyGatherOres) {
			blocksToNotAdd.add(Item.getItemFromBlock(Blocks.SAND));
			blocksToNotAdd.add(Item.getItemFromBlock(Blocks.SANDSTONE));
			blocksToNotAdd.add(Item.getItemFromBlock(Blocks.COBBLESTONE));
			blocksToNotAdd.add(Item.getItemFromBlock(Blocks.STONE));
			blocksToNotAdd.add(Item.getItemFromBlock(Blocks.DIRT));
			blocksToNotAdd.add(Item.getItemFromBlock(Blocks.GRANITE));
			blocksToNotAdd.add(Item.getItemFromBlock(Blocks.ANDESITE));
			blocksToNotAdd.add(Item.getItemFromBlock(Blocks.DIORITE));
			blocksToNotAdd.add(Item.getItemFromBlock(Blocks.RED_SAND));
			blocksToNotAdd.add(Item.getItemFromBlock(Blocks.RED_SANDSTONE));
			blocksToNotAdd.add(Item.getItemFromBlock(Blocks.MOSSY_COBBLESTONE));
			blocksToNotAdd.add(Item.getItemFromBlock(Blocks.MOSSY_STONE_BRICKS));
		}

		Tuple<ArrayList<ItemStack>, ArrayList<BlockPos>> ladderShaftResults = BuildingMethods.CreateLadderShaft(world, pos, stacks, facing, blocksToNotAdd);
		stacks = ladderShaftResults.getFirst();
		ArrayList<BlockPos> torchPositions = ladderShaftResults.getSecond();

		// Get to Y11;
		pos = pos.down(pos.getY() - 10);

		ArrayList<ItemStack> tempStacks = new ArrayList<ItemStack>();

		BlockPos ceilingLevel = pos.up(4);

		tempStacks = BuildingMethods.SetFloor(world, ceilingLevel.offset(facing, 2).offset(facing.rotateY(), 2).offset(facing.getOpposite()), Blocks.STONE, 4, 4, tempStacks,
				facing.getOpposite(), blocksToNotAdd);

		// After setting the floor, make sure to replace the ladder.
		BuildingMethods.ReplaceBlock(world, ceilingLevel, Blocks.LADDER.getDefaultState().with(LadderBlock.FACING, facing));

		BlockState torchState = Blocks.TORCH.getDefaultState();

		// Place the torches at this point since the entire shaft has been set.
		for (BlockPos torchPos : torchPositions) {
			BlockState surroundingState = world.getBlockState(torchPos);
			Block surroundingBlock = surroundingState.getBlock();
			tempStacks = BuildingMethods.ConsolidateDrops(world, torchPos, surroundingState, tempStacks, blocksToNotAdd);
			BuildingMethods.ReplaceBlock(world, torchPos, torchState);
		}

		// The entire ladder has been created. Create a platform at this level
		// and place a chest next to the ladder.
		tempStacks.addAll(BuildingMethods.SetFloor(world, pos.offset(facing).offset(facing.rotateY()), Blocks.STONE, 3, 4, tempStacks, facing.getOpposite(), blocksToNotAdd));

		// Remove the ladder stack since they shouldn't be getting that.
		for (int i = 0; i < tempStacks.size(); i++) {
			ItemStack stack = tempStacks.get(i);

			if (stack.getItem() == Item.getItemFromBlock(Blocks.LADDER)) {
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
		for (ItemStack tempStack : tempStacks) {
			boolean foundStack = false;

			for (ItemStack existingStack : stacks) {
				if (ItemStack.areItemsEqual(existingStack, tempStack)) {
					// Make sure that this combined stack is at or smaller than
					// the max.
					if (existingStack.getCount() + tempStack.getCount() <= tempStack.getMaxStackSize()) {
						existingStack.setCount(existingStack.getCount() + tempStack.getCount());
						foundStack = true;
						break;
					}
				}
			}

			if (!foundStack) {
				stacks.add(tempStack);
			}
		}

		// Place a torch to the left of the ladder.
		BlockState blockState = Blocks.TORCH.getDefaultState();
		BuildingMethods.ReplaceBlock(world, pos.offset(facing.rotateYCCW()), blockState);

		if (CommonProxy.proxyConfiguration.serverConfiguration.includeMineshaftChest) {
			// Place a chest to the right of the ladder.
			BlockState chestState = Blocks.CHEST.getDefaultState().with(ChestBlock.FACING, facing);
			BuildingMethods.ReplaceBlock(world, pos.offset(facing.rotateY()), chestState);

			if (stacks.size() > 27) {
				// Add another chest to south of the existing chest.
				BuildingMethods.ReplaceBlock(world, pos.offset(facing.rotateY()).offset(facing.getOpposite()), chestState);
			}

			TileEntity tileEntity = world.getTileEntity(pos.offset(facing.rotateY()));
			TileEntity tileEntity2 = world.getTileEntity(pos.offset(facing.rotateY()).offset(facing.getOpposite()));

			if (tileEntity instanceof ChestTileEntity) {
				ChestTileEntity chestTile = (ChestTileEntity) tileEntity;
				ChestTileEntity chestTile2 = (ChestTileEntity) tileEntity2;

				int i = 0;
				boolean fillSecond = false;

				// All of the stacks should be consolidated at this point.
				for (ItemStack stack : stacks) {
					if (i == 27 && !fillSecond) {
						// Start filling the second chest.
						fillSecond = true;
						i = 0;
						chestTile = chestTile2;
					}

					if (i >= 27 && fillSecond) {
						// Too many items, discard the rest.
						break;
					} else {
						chestTile.setInventorySlotContents(i, stack);
					}

					i++;
				}
			}
		}
	}

	private static Tuple<ArrayList<ItemStack>, ArrayList<BlockPos>> CreateLadderShaft(ServerWorld world, BlockPos pos, ArrayList<ItemStack> originalStacks, Direction houseFacing,
																					  ArrayList<Item> blocksToNotAdd) {
		int torchCounter = 0;

		// Keep the "west" facing.
		Direction westWall = houseFacing.rotateYCCW();

		// Get the ladder state based on the house facing.
		BlockState ladderState = Blocks.LADDER.getDefaultState().with(LadderBlock.FACING, houseFacing);

		// Replace the main floor block with air since we don't want it placed in the chest at the end.
		BuildingMethods.ReplaceBlock(world, pos, Blocks.AIR);
		ArrayList<BlockPos> torchPositions = new ArrayList<>();

		while (pos.getY() > 8) {
			BlockState state = world.getBlockState(pos);
			Block block = state.getBlock();
			torchCounter++;

			// Make sure all blocks around this one are solid, if they are not
			// replace them with stone.
			for (int i = 0; i < 4; i++) {
				Direction facing = houseFacing;

				switch (i) {
					case 1: {
						facing = houseFacing.rotateY();
						break;
					}
					case 2: {
						facing = houseFacing.getOpposite();
						break;
					}
					case 3: {
						facing = houseFacing.rotateYCCW();
						break;
					}
					default: {
						facing = houseFacing;
					}
				}

				// Every 6 blocks, place a torch on the west wall.
				// If we are close to the bottom, don't place a torch. Do the
				// normal processing.
				if (facing == westWall && torchCounter == 6 && pos.getY() > 14) {
					// First make sure the blocks around this block are stone, then place the torch.
					for (int j = 0; j <= 2; j++) {
						BlockPos tempPos = null;
						BlockState surroundingState = null;
						Block surroundingBlock = null;

						if (j == 0) {
							tempPos = pos.offset(facing, 2);
							surroundingState = world.getBlockState(tempPos);
							surroundingBlock = surroundingState.getBlock();
						} else if (j == 1) {
							tempPos = pos.offset(facing).offset(facing.rotateY());
							surroundingState = world.getBlockState(tempPos);
							surroundingBlock = surroundingState.getBlock();
						} else {
							tempPos = pos.offset(facing).offset(facing.rotateYCCW());
							surroundingState = world.getBlockState(tempPos);
							surroundingBlock = surroundingState.getBlock();
						}

						// Make sure that this is a normal solid block and not a liquid or partial block.
						if (!(surroundingBlock == Blocks.STONE || surroundingBlock == Blocks.ANDESITE || surroundingBlock == Blocks.DIORITE || surroundingBlock == Blocks.GRANITE)) {
							// This is not a stone block. Get the drops then replace it with stone.
							originalStacks = BuildingMethods.ConsolidateDrops(world, tempPos, surroundingState, originalStacks, blocksToNotAdd);

							BuildingMethods.ReplaceBlock(world, tempPos, Blocks.STONE);
						}
					}

					torchPositions.add(pos.offset(facing));
					torchCounter = 0;
				} else {
					BlockPos tempPos = pos.offset(facing);
					BlockState surroundingState = world.getBlockState(tempPos);
					Block surroundingBlock = surroundingState.getBlock();

					if (!surroundingState.isNormalCube(world, tempPos)
							|| surroundingBlock instanceof FlowingFluidBlock) {
						// This is not a solid block. Get the drops then replace
						// it with stone.
						originalStacks = BuildingMethods.ConsolidateDrops(world, tempPos, surroundingState, originalStacks, blocksToNotAdd);

						BuildingMethods.ReplaceBlock(world, tempPos, Blocks.STONE);
					}
				}
			}

			// Get the block drops then replace it with a ladder.
			originalStacks = BuildingMethods.ConsolidateDrops(world, pos, state, originalStacks, blocksToNotAdd);

			// Don't place a ladder at this location since it will be destroyed.
			if (pos.getY() >= 10) {
				BuildingMethods.ReplaceBlock(world, pos, ladderState);
			}

			pos = pos.down();
		}

		return new Tuple<>(originalStacks, torchPositions);
	}
}
