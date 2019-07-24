package com.wuest.prefab.Structures.Base;

import com.wuest.prefab.Structures.Config.HouseConfiguration;
import com.wuest.prefab.Structures.Config.StructureConfiguration;
import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.state.properties.Half;
import net.minecraft.state.properties.StairsShape;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.BlockSnapshot;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.BlockEvent.EntityPlaceEvent;

import java.util.ArrayList;

/**
 * This class is used to hold he generalized building methods used by the starting house.
 *
 * @author WuestMan
 */
public class BuildingMethods {
    /**
     * Clears the given space without returning any of the drops.
     *
     * @param world            - The world used to clear the space.
     * @param startingPosition - The starting position, note the width, depth will be used to determine the starting
     *                         corner(s).
     * @param width            - The radius x-axis of how wide to clear. (East/West)
     * @param height           - How high from the starting position to clear (this includes starting position).
     * @param depth            - The radius z-axis of how deep to clear. (North/South).
     * @param houseFacing      This is used to determine which direction the clearing should start.
     */
    public static void ClearSpace(ServerWorld world, BlockPos startingPosition, int width, int height, int depth, Direction houseFacing) {
        BlockPos northSide = startingPosition.offset(houseFacing, 5).offset(houseFacing.rotateY(), 5);
        int clearedWidth = width + 12;
        int wallLength = depth + 12;

        for (int i = 0; i < clearedWidth; i++) {
            BuildingMethods.CreateWall(world, height, wallLength, houseFacing.getOpposite(), northSide, Blocks.AIR, null);

            northSide = northSide.offset(houseFacing.rotateYCCW());
        }
    }

    /**
     * Clears the given space without returning any of the drops.
     *
     * @param world            - The world used to clear the space.
     * @param startingPosition - The starting position, note the width, depth will be used to determine the starting
     *                         corner(s).
     * @param width            - The radius x-axis of how wide to clear. (East/West)
     * @param height           - How high from the starting position to clear (this includes starting position).
     * @param depth            - The radius z-axis of how deep to clear. (North/South)
     * @param houseFacing      This is used to determine which direction the clearing should start.
     */
    public static void ClearSpaceExact(World world, BlockPos startingPosition, int width, int height, int depth, Direction houseFacing) {
        BlockPos otherCorner = startingPosition.offset(houseFacing.getOpposite(), depth).offset(houseFacing.rotateYCCW(), width);

        for (BlockPos pos : BlockPos.getAllInBoxMutable(startingPosition, otherCorner)) {
            if (!world.isAirBlock(pos)) {
                BuildingMethods.ReplaceBlock(world, pos, Blocks.AIR);
            }
        }
    }

    /**
     * This method consolidate drops for the current block into an existing ArrayList
     *
     * @param block          The block to get the drops from.
     * @param world          The world which the block resides.
     * @param pos            The block position.
     * @param state          The current block state.
     * @param originalStacks The original list of stacks.
     * @param itemsToNotAdd  The items to not add to the list.
     * @return An updated list of item stacks.
     */
    public static ArrayList<ItemStack> ConsolidateDrops(Block block, ServerWorld world, BlockPos pos, BlockState state, ArrayList<ItemStack> originalStacks,
                                                        ArrayList<Item> itemsToNotAdd) {
        for (ItemStack stack : Block.getDrops(state, world, pos, null)) {
            if (itemsToNotAdd != null) {
                if (itemsToNotAdd.contains(stack.getItem())) {
                    continue;
                }
            }

            // Check to see if this stack's item is equal to an existing item
            // stack. If it is just add the count.
            Boolean foundStack = false;

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
        ArrayList<ItemStack> itemsDropped = new ArrayList<ItemStack>();

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
     * Creates a wall of blocks.
     *
     * @param world            The world to create the wall.
     * @param height           The height of the wall.
     * @param length           The length of the wall.
     * @param direction        The direction of the wall.
     * @param startingPosition Where the wall should start.
     * @param replacementBlock The block to create the wall out of.
     * @return An Arraylist of Itemstacks which contains the drops from any destroyed blocks.
     */
    public static ArrayList<ItemStack> CreateWall(ServerWorld world, int height, int length, Direction direction, BlockPos startingPosition,
                                                  BlockState replacementBlock) {
        ArrayList<ItemStack> itemsDropped = new ArrayList<ItemStack>();

        BlockPos wallPos = null;

        // i height, j is the actual wall counter.
        for (int i = 0; i < height; i++) {
            // Reset wall building position to the starting position up by the
            // height counter.
            wallPos = startingPosition.up(i);

            for (int j = 0; j < length; j++) {
                BlockState currentBlockPosState = world.getBlockState(wallPos);

                for (ItemStack stack : Block.getDrops(currentBlockPosState, world, wallPos, null)) {
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
     * Creates a ceiling (floating floor) in the world.
     *
     * @param world         The world to create the floor in.
     * @param pos           The block position to start creating the floor.
     * @param block         The Type of block to create the floor out of.
     * @param width         The width of the floor.
     * @param depth         The length of the floor.
     * @param stairs        The type of block to create the roof out of.
     * @param configuration The house configuration object. This is specifically used in the basic house.
     * @param houseFacing   The direction to start the ceiling.
     * @param itemsToNotAdd The items to not include in the harvested blocks.
     */
    public static void SetCeiling(ServerWorld world, BlockPos pos, Block block, int width, int depth, Block stairs, HouseConfiguration configuration, Direction houseFacing,
                                  ArrayList<Item> itemsToNotAdd) {
        // If the ceiling is flat, call SetFloor since it's laid out the same.
        if (configuration.isCeilingFlat) {
            BuildingMethods.SetFloor(world, pos, block, width, depth, new ArrayList<ItemStack>(), houseFacing.getOpposite(), itemsToNotAdd);
            return;
        }

        // Get the stairs state without the facing since it will change.
        BlockState stateWithoutFacing = stairs.getDefaultState().with(StairsBlock.HALF, Half.BOTTOM)
                .with(StairsBlock.SHAPE, StairsShape.STRAIGHT);

        int wallWidth = configuration.houseWidth;
        int wallDepth = configuration.houseDepth;
        int height = wallWidth / 2;
        boolean isWider = false;

        if (wallWidth > wallDepth) {
            height = wallDepth / 2;
            isWider = true;
        }

        for (int i = 0; i <= height; i++) {
            // I is the vaulted roof level.
            for (int j = 0; j < 4; j++) {
                // Default is depth.
                Direction facing = houseFacing.rotateYCCW();
                Direction flowDirection = houseFacing.getOpposite();
                int wallSize = wallDepth;

                switch (j) {
                    case 1: {
                        facing = houseFacing;
                        flowDirection = houseFacing.rotateYCCW();
                        wallSize = wallWidth;
                        break;
                    }

                    case 2: {
                        facing = houseFacing.rotateY();
                        flowDirection = houseFacing;
                        wallSize = wallDepth;
                        break;
                    }

                    case 3: {
                        facing = houseFacing.getOpposite();
                        flowDirection = houseFacing.rotateY();
                        wallSize = wallWidth;
                        break;
                    }
                }

                for (int k = 0; k <= wallSize; k++) {
                    // j is the north/south counter.
                    BuildingMethods.ReplaceBlock(world, pos, stateWithoutFacing.with(StairsBlock.FACING, facing));

                    pos = pos.offset(flowDirection);
                }
            }

            pos = pos.offset(houseFacing.rotateYCCW()).offset(houseFacing.getOpposite()).up();
            wallWidth = wallWidth - 2;
            wallDepth = wallDepth - 2;
        }

        if (world.isAirBlock(pos.down())) {
            int finalStoneCount = wallDepth;

            if (isWider) {
                finalStoneCount = wallWidth;
            }

            // Add the number of blocks based on the depth/width (minimum 1);
            if (finalStoneCount < 1) {
                finalStoneCount = 1;
            } else {
                finalStoneCount = finalStoneCount + 2;
            }

            BlockPos torchPos = pos;

            for (int i = 0; i < finalStoneCount; i++) {
                BuildingMethods.ReplaceBlock(world, pos, block);

                if (isWider) {
                    pos = pos.offset(houseFacing.rotateYCCW());
                } else {
                    pos = pos.offset(houseFacing.getOpposite());
                }
            }

            BlockState torchLocation = world.getBlockState(torchPos);

            if (torchLocation.getBlock().func_220055_a(world, torchPos.down(), Direction.UP)) {
                BlockState blockState = Blocks.TORCH.getDefaultState();
                BuildingMethods.ReplaceBlock(world, torchPos.up(), blockState);
            }
        }
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
     * Replaces a block at the given position in the world. This is faster as the position is not set to air first.
     *
     * @param world                 The world object.
     * @param pos                   The position to update.
     * @param replacementBlockState The block state to place at this position.
     */
    public static void ReplaceBlockNoAir(World world, BlockPos pos, BlockState replacementBlockState) {
        BuildingMethods.ReplaceBlockNoAir(world, pos, replacementBlockState, 3);
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
     * Gets the integer representation of a torch facing.
     *
     * @param facing The facing that the torch should look.
     * @return The integer used for the block state of a BlockTorch object.
     */
    public static int GetTorchFacing(Direction facing) {
        /*
         * Torch Facings: 1 = East, 2 = West, 3 = South, 4 = North, 5 = Up
         */
        switch (facing) {
            case NORTH: {
                return 4;
            }

            case EAST: {
                return 1;
            }

            case SOUTH: {
                return 3;
            }

            case WEST: {
                return 2;
            }

            default: {
                return 5;
            }
        }
    }

    /**
     * This method is used to determine if the player can build the structure.
     *
     * @param configuration The structure configuration.
     * @param world         The world to build the structure in.
     * @param startBlockPos The start location to start checking blocks.
     * @param endBlockPos   The end location for checking blocks. Combined with the startBlockPos, this should be a
     *                      cube.
     * @param player        The player running this build request.
     * @return True if all blocks can be replaced. Otherwise false and send a message to the player.
     */
    public static boolean CheckBuildSpaceForAllowedBlockReplacement(StructureConfiguration configuration, World world, BlockPos startBlockPos, BlockPos endBlockPos,
                                                                    PlayerEntity player) {
        // Check each block in the space to be cleared if it's protected from
        // breaking or placing, if it is return false.
        for (BlockPos currentPos : BlockPos.getAllInBoxMutable(startBlockPos, endBlockPos)) {
            BlockState blockState = world.getBlockState(currentPos);

            if (!blockState.getBlock().isAir(blockState, world, currentPos)) {
                BlockEvent.BreakEvent breakEvent = new BlockEvent.BreakEvent(world, currentPos, world.getBlockState(currentPos), player);

                if (MinecraftForge.EVENT_BUS.post(breakEvent)) {
                    return false;
                }
            }

            EntityPlaceEvent placeEvent = new EntityPlaceEvent(new BlockSnapshot(world, currentPos, blockState), Blocks.AIR.getDefaultState(), player);

            if (MinecraftForge.EVENT_BUS.post(placeEvent)) {
                return false;
            }

            // A hardness of less than 0 is unbreakable.
            if (blockState.getBlockHardness(world, currentPos) < 0.0f) {
                // This is bedrock or some other type of unbreakable block. Don't allow this block to be broken by a
                // structure.
                return false;
            }
        }

        return true;
    }
}
