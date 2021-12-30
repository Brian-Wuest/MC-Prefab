package com.wuest.prefab.structures.base;

import com.wuest.prefab.ModRegistry;
import com.wuest.prefab.Triple;
import com.wuest.prefab.Tuple;
import com.wuest.prefab.blocks.FullDyeColor;
import com.wuest.prefab.config.ModConfiguration;
import com.wuest.prefab.proxy.CommonProxy;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.entity.FurnaceBlockEntity;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BedPart;
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
    public static ArrayList<ItemStack> ConsolidateDrops(ServerLevel world, BlockPos pos, BlockState state, ArrayList<ItemStack> originalStacks,
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
                if (ItemStack.isSame(existingStack, stack)) {
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
     * @return An Arraylist of ItemStacks which contains the drops from any destroyed blocks.
     */
    public static ArrayList<ItemStack> CreateWall(ServerLevel world, int height, int length, Direction direction, BlockPos startingPosition, Block replacementBlock,
                                                  ArrayList<Item> itemsToNotAdd) {
        ArrayList<ItemStack> itemsDropped = new ArrayList<>();

        BlockPos wallPos;

        // i height, j is the actual wall counter.
        for (int i = 0; i < height; i++) {
            // Reset wall building position to the starting position up by the
            // height counter.
            wallPos = startingPosition.above(i);

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

                wallPos = wallPos.relative(direction);
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
    public static ArrayList<ItemStack> SetFloor(ServerLevel world, BlockPos pos, Block block, int width, int depth, ArrayList<ItemStack> originalStack, Direction facing,
                                                ArrayList<Item> itemsToNotAdd) {
        for (int i = 0; i < width; i++) {
            originalStack.addAll(BuildingMethods.CreateWall(world, 1, depth, facing, pos, block, itemsToNotAdd));

            pos = pos.relative(facing.getClockWise());
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
    public static void ReplaceBlock(Level world, BlockPos pos, Block replacementBlock) {
        BuildingMethods.ReplaceBlock(world, pos, replacementBlock.defaultBlockState(), 3);
    }

    /**
     * Replaces a block at the given position in the world. This is faster as the position is not set to air first.
     *
     * @param world            The world object.
     * @param pos              The position to update.
     * @param replacementBlock The block object to place at this position. The default state is used.
     */
    public static void ReplaceBlockNoAir(Level world, BlockPos pos, Block replacementBlock) {
        BuildingMethods.ReplaceBlockNoAir(world, pos, replacementBlock.defaultBlockState(), 3);
    }

    /**
     * Replaces a block at the given position in the world. The block in the pos will be replaced with air before
     * placing the block;
     *
     * @param world                 The world object.
     * @param pos                   The position to update.
     * @param replacementBlockState The block state to place at this position.
     */
    public static void ReplaceBlock(Level world, BlockPos pos, BlockState replacementBlockState) {
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
    public static void ReplaceBlock(Level world, BlockPos pos, BlockState replacementBlockState, int flags) {
        world.removeBlock(pos, false);
        world.setBlock(pos, replacementBlockState, flags);
    }

    /**
     * Replaces a block at the given position in the world. This is faster as the position is not set to air first.
     *
     * @param world                 The world object.
     * @param pos                   The position to update.
     * @param replacementBlockState The block state to place at this position.
     * @param flags                 The trigger flags, this should always be set to 3 so the clients are updated.
     */
    public static void ReplaceBlockNoAir(Level world, BlockPos pos, BlockState replacementBlockState, int flags) {
        world.setBlock(pos, replacementBlockState, flags);
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
    public static Triple<Boolean, BlockState, BlockPos> CheckBuildSpaceForAllowedBlockReplacement(ServerLevel world, BlockPos startBlockPos, BlockPos endBlockPos,
                                                                                                  Player player) {
        if (!world.isClientSide()) {
            // Check each block in the space to be cleared if it's protected from
            // breaking or placing, if it is return false.
            for (BlockPos currentPos : BlockPos.betweenClosed(startBlockPos, endBlockPos)) {
                BlockState blockState = world.getBlockState(currentPos);

                // First check to see if this is a spawn protected block.
                if (world.getServer().isUnderSpawnProtection(world, currentPos, player)) {
                    // This block is protected by vanilla spawn protection. Don't allow building here.
                    return new Triple<>(false, blockState, currentPos);
                }

                if (!world.isEmptyBlock(currentPos)) {
                    BlockEvent.BreakEvent breakEvent = new BlockEvent.BreakEvent(world, currentPos, world.getBlockState(currentPos), player);

                    if (MinecraftForge.EVENT_BUS.post(breakEvent)) {
                        return new Triple<>(false, blockState, currentPos);
                    }
                }

                EntityPlaceEvent placeEvent = new EntityPlaceEvent(BlockSnapshot.create(world.dimension(), world, currentPos), Blocks.AIR.defaultBlockState(), player);

                if (MinecraftForge.EVENT_BUS.post(placeEvent)) {
                    return new Triple<>(false, blockState, currentPos);
                }

                // A hardness of less than 0 is unbreakable.
                if (blockState.getDestroySpeed(world, currentPos) < 0.0f) {
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
    public static void PlaceColoredBed(Level world, BlockPos bedHeadPos, BlockPos bedFootPos, DyeColor bedColor) {

        Tuple<BlockState, BlockState> bedStates = BuildingMethods.getBedState(bedHeadPos, bedFootPos, bedColor);
        BuildingMethods.ReplaceBlock(world, bedHeadPos, bedStates.getFirst());
        BuildingMethods.ReplaceBlock(world, bedFootPos, bedStates.getSecond());
    }

    public static Tuple<BlockState, BlockState> getBedState(BlockPos bedHeadPos, BlockPos bedFootPos, DyeColor bedColor) {
        BlockState bedHead = null;
        BlockState bedFoot = null;

        switch (bedColor) {
            case BLACK: {
                bedHead = Blocks.BLACK_BED.defaultBlockState().setValue(BedBlock.PART, BedPart.HEAD);
                bedFoot = Blocks.BLACK_BED.defaultBlockState();
                break;
            }
            case BLUE: {
                bedHead = Blocks.BLUE_BED.defaultBlockState().setValue(BedBlock.PART, BedPart.HEAD);
                bedFoot = Blocks.BLUE_BED.defaultBlockState();
                break;
            }

            case BROWN: {
                bedHead = Blocks.BROWN_BED.defaultBlockState().setValue(BedBlock.PART, BedPart.HEAD);
                bedFoot = Blocks.BROWN_BED.defaultBlockState();
                break;
            }

            case CYAN: {
                bedHead = Blocks.CYAN_BED.defaultBlockState().setValue(BedBlock.PART, BedPart.HEAD);
                bedFoot = Blocks.CYAN_BED.defaultBlockState();
                break;
            }

            case GRAY: {
                bedHead = Blocks.GRAY_BED.defaultBlockState().setValue(BedBlock.PART, BedPart.HEAD);
                bedFoot = Blocks.GRAY_BED.defaultBlockState();
                break;
            }

            case GREEN: {
                bedHead = Blocks.GREEN_BED.defaultBlockState().setValue(BedBlock.PART, BedPart.HEAD);
                bedFoot = Blocks.GREEN_BED.defaultBlockState();
                break;
            }

            case LIGHT_BLUE: {
                bedHead = Blocks.LIGHT_BLUE_BED.defaultBlockState().setValue(BedBlock.PART, BedPart.HEAD);
                bedFoot = Blocks.LIGHT_BLUE_BED.defaultBlockState();
                break;
            }

            case LIGHT_GRAY: {
                bedHead = Blocks.LIGHT_GRAY_BED.defaultBlockState().setValue(BedBlock.PART, BedPart.HEAD);
                bedFoot = Blocks.LIGHT_GRAY_BED.defaultBlockState();
                break;
            }

            case LIME: {
                bedHead = Blocks.LIME_BED.defaultBlockState().setValue(BedBlock.PART, BedPart.HEAD);
                bedFoot = Blocks.LIME_BED.defaultBlockState();
                break;
            }

            case MAGENTA: {
                bedHead = Blocks.MAGENTA_BED.defaultBlockState().setValue(BedBlock.PART, BedPart.HEAD);
                bedFoot = Blocks.MAGENTA_BED.defaultBlockState();
                break;
            }

            case ORANGE: {
                bedHead = Blocks.ORANGE_BED.defaultBlockState().setValue(BedBlock.PART, BedPart.HEAD);
                bedFoot = Blocks.ORANGE_BED.defaultBlockState();
                break;
            }

            case PINK: {
                bedHead = Blocks.PINK_BED.defaultBlockState().setValue(BedBlock.PART, BedPart.HEAD);
                bedFoot = Blocks.PINK_BED.defaultBlockState();
                break;
            }

            case PURPLE: {
                bedHead = Blocks.PURPLE_BED.defaultBlockState().setValue(BedBlock.PART, BedPart.HEAD);
                bedFoot = Blocks.PURPLE_BED.defaultBlockState();
                break;
            }

            case RED: {
                bedHead = Blocks.RED_BED.defaultBlockState().setValue(BedBlock.PART, BedPart.HEAD);
                bedFoot = Blocks.RED_BED.defaultBlockState();
                break;
            }

            case WHITE: {
                bedHead = Blocks.WHITE_BED.defaultBlockState().setValue(BedBlock.PART, BedPart.HEAD);
                bedFoot = Blocks.WHITE_BED.defaultBlockState();
                break;
            }

            case YELLOW: {
                bedHead = Blocks.YELLOW_BED.defaultBlockState().setValue(BedBlock.PART, BedPart.HEAD);
                bedFoot = Blocks.YELLOW_BED.defaultBlockState();
                break;
            }
        }

        Direction direction = Direction.NORTH;
        BlockPos tempPos = bedHeadPos.relative(Direction.NORTH);

        while (tempPos.getX() != bedFootPos.getX() || tempPos.getZ() != bedFootPos.getZ()) {
            direction = direction.getClockWise();
            tempPos = bedHeadPos.relative(direction);
        }

        bedHead = bedHead.setValue(BedBlock.FACING, direction.getOpposite());
        bedFoot = bedFoot.setValue(BedBlock.FACING, direction.getOpposite());

        return new Tuple<>(bedHead, bedFoot);
    }

    /**
     * Fills a chest with basic tools and items.
     *
     * @param world        - The world where the chest resides.
     * @param itemPosition - The block position of the chest.
     */
    public static void FillChest(Level world, BlockPos itemPosition) {
        // Add each stone tool to the chest and leather armor.
        BlockEntity tileEntity = world.getBlockEntity(itemPosition);

        if (tileEntity instanceof RandomizableContainerBlockEntity) {
            RandomizableContainerBlockEntity chestTile = (RandomizableContainerBlockEntity) tileEntity;

            int itemSlot = 0;

            // Add the tools.
            if (CommonProxy.proxyConfiguration.serverConfiguration.addAxe) {
                chestTile.setItem(itemSlot++, new ItemStack(Items.STONE_AXE));
            }

            if (CommonProxy.proxyConfiguration.serverConfiguration.addHoe) {
                chestTile.setItem(itemSlot++, new ItemStack(Items.STONE_HOE));
            }

            if (CommonProxy.proxyConfiguration.serverConfiguration.addPickAxe) {
                chestTile.setItem(itemSlot++, new ItemStack(Items.STONE_PICKAXE));
            }

            if (CommonProxy.proxyConfiguration.serverConfiguration.addShovel) {
                chestTile.setItem(itemSlot++, new ItemStack(Items.STONE_SHOVEL));
            }

            if (CommonProxy.proxyConfiguration.serverConfiguration.addSword) {
                Item sword = ModRegistry.SwiftBladeStone.get();

                if (!CommonProxy.proxyConfiguration.serverConfiguration.recipeConfiguration.get(ModConfiguration.SwiftBladeKey)) {
                    // Swift blades are disabled; use a regular stone sword instead.
                    sword = Items.STONE_SWORD;
                }

                chestTile.setItem(itemSlot++, new ItemStack(sword));
            }

            if (CommonProxy.proxyConfiguration.serverConfiguration.addArmor) {
                // Add the armor.
                chestTile.setItem(itemSlot++, new ItemStack(Items.LEATHER_BOOTS));
                chestTile.setItem(itemSlot++, new ItemStack(Items.LEATHER_CHESTPLATE));
                chestTile.setItem(itemSlot++, new ItemStack(Items.LEATHER_HELMET));
                chestTile.setItem(itemSlot++, new ItemStack(Items.LEATHER_LEGGINGS));
            }

            if (CommonProxy.proxyConfiguration.serverConfiguration.addFood) {
                // Add some bread.
                chestTile.setItem(itemSlot++, new ItemStack(Items.BREAD, 20));
            }

            if (CommonProxy.proxyConfiguration.serverConfiguration.addCrops) {
                // Add potatoes.
                chestTile.setItem(itemSlot++, new ItemStack(Items.POTATO, 3));

                // Add carrots.
                chestTile.setItem(itemSlot++, new ItemStack(Items.CARROT, 3));

                // Add seeds.
                chestTile.setItem(itemSlot++, new ItemStack(Items.WHEAT_SEEDS, 3));
            }

            if (CommonProxy.proxyConfiguration.serverConfiguration.addCobble) {
                // Add Cobblestone.
                chestTile.setItem(itemSlot++, new ItemStack(Item.byBlock(Blocks.COBBLESTONE), 64));
            }

            if (CommonProxy.proxyConfiguration.serverConfiguration.addDirt) {
                // Add Dirt.
                chestTile.setItem(itemSlot++, new ItemStack(Item.byBlock(Blocks.DIRT), 64));
            }

            if (CommonProxy.proxyConfiguration.serverConfiguration.addSaplings) {
                // Add oak saplings.
                chestTile.setItem(itemSlot++, new ItemStack(Item.byBlock(Blocks.OAK_SAPLING), 3));
            }

            if (CommonProxy.proxyConfiguration.serverConfiguration.addTorches) {
                // Add a set of 20 torches.
                chestTile.setItem(itemSlot, new ItemStack(Item.byBlock(Blocks.TORCH), 20));
            }

            chestTile.setChanged();
            Packet<ClientGamePacketListener> packet = chestTile.getUpdatePacket();

            if (packet != null) {
                world.getServer().getPlayerList().broadcastAll(packet);
            }
        }
    }

    /**
     * Fills the furnaces with an amount of coal.
     *
     * @param world            The world where the furnaces reside.
     * @param furnacePositions A collection of furnace positions.
     */
    public static void FillFurnaces(Level world, ArrayList<BlockPos> furnacePositions) {
        if (furnacePositions != null && furnacePositions.size() > 0) {
            for (BlockPos furnacePos : furnacePositions) {
                // Fill the furnace.
                BlockEntity tileEntity = world.getBlockEntity(furnacePos);

                if (tileEntity instanceof FurnaceBlockEntity) {
                    FurnaceBlockEntity furnaceTile = (FurnaceBlockEntity) tileEntity;
                    furnaceTile.setItem(1, new ItemStack(Items.COAL, 20));
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
    public static void PlaceMineShaft(ServerLevel world, BlockPos pos, Direction facing, boolean onlyGatherOres) {
        // Keep track of all the items to add to the chest at the end of the
        // shaft.
        ArrayList<ItemStack> stacks = new ArrayList<>();
        ArrayList<Item> blocksToNotAdd = new ArrayList<>();

        if (onlyGatherOres) {
            blocksToNotAdd.add(Item.byBlock(Blocks.SAND));
            blocksToNotAdd.add(Item.byBlock(Blocks.SANDSTONE));
            blocksToNotAdd.add(Item.byBlock(Blocks.COBBLESTONE));
            blocksToNotAdd.add(Item.byBlock(Blocks.STONE));
            blocksToNotAdd.add(Item.byBlock(Blocks.DIRT));
            blocksToNotAdd.add(Item.byBlock(Blocks.GRANITE));
            blocksToNotAdd.add(Item.byBlock(Blocks.ANDESITE));
            blocksToNotAdd.add(Item.byBlock(Blocks.DIORITE));
            blocksToNotAdd.add(Item.byBlock(Blocks.RED_SAND));
            blocksToNotAdd.add(Item.byBlock(Blocks.RED_SANDSTONE));
            blocksToNotAdd.add(Item.byBlock(Blocks.MOSSY_COBBLESTONE));
            blocksToNotAdd.add(Item.byBlock(Blocks.MOSSY_STONE_BRICKS));
        }

        int minimumHeightForMineshaft = world.getMinBuildHeight() + 21;

        Triple<ArrayList<ItemStack>, ArrayList<BlockPos>, ArrayList<BlockPos>> ladderShaftResults = BuildingMethods.CreateLadderShaft(
                world,
                pos,
                stacks,
                facing,
                blocksToNotAdd,
                minimumHeightForMineshaft);

        ArrayList<BlockPos> blockPositions = new ArrayList<>(ladderShaftResults.getThird());

        stacks = ladderShaftResults.getFirst();
        ArrayList<BlockPos> torchPositions = ladderShaftResults.getSecond();

        // Get 20 blocks above the void.
        pos = pos.below(pos.getY() - minimumHeightForMineshaft);

        ArrayList<ItemStack> tempStacks = new ArrayList<>();

        BlockPos ceilingLevel = pos.above(4);

        BuildingMethods.SetFloor(world, ceilingLevel.relative(facing, 2).relative(facing.getClockWise(), 2).relative(facing.getOpposite()), Blocks.STONE, 4, 4, tempStacks,
                facing.getOpposite(), blocksToNotAdd);

        // After setting the floor, make sure to replace the ladder.
        BuildingMethods.ReplaceBlock(world, ceilingLevel, Blocks.LADDER.defaultBlockState().setValue(LadderBlock.FACING, facing), 2);
        blockPositions.add(ceilingLevel);

        BlockState torchState = Blocks.TORCH.defaultBlockState();

        // Place the torches at this point since the entire shaft has been set.
        for (BlockPos torchPos : torchPositions) {
            BlockState surroundingState = world.getBlockState(torchPos);
            Block surroundingBlock = surroundingState.getBlock();
            BuildingMethods.ConsolidateDrops(world, torchPos, surroundingState, tempStacks, blocksToNotAdd);
            BuildingMethods.ReplaceBlock(world, torchPos, torchState, 2);
            blockPositions.add(torchPos);
        }

        // The entire ladder has been created. Create a platform at this level
        // and place a chest next to the ladder.
        tempStacks.addAll(BuildingMethods.SetFloor(world, pos.relative(facing).relative(facing.getClockWise()), Blocks.STONE, 3, 4, tempStacks, facing.getOpposite(), blocksToNotAdd));

        // Remove the ladder stack since they shouldn't be getting that.
        for (int i = 0; i < tempStacks.size(); i++) {
            ItemStack stack = tempStacks.get(i);

            if (stack.getItem() == Item.byBlock(Blocks.LADDER)) {
                tempStacks.remove(i);
                i--;
            }
        }

        // Now that the floor has been set, go up 1 block to star creating the
        // walls.
        pos = pos.above();

        // Clear a space around the ladder pillar and make walls. The walls are
        // necessary if there is a lot of lava down here.
        // Make a wall of air then a wall of stone.

        // South wall.
        tempStacks
                .addAll(BuildingMethods.CreateWall(world, 3, 3, facing.getClockWise(), pos.relative(facing.getOpposite(), 2).relative(facing.getCounterClockWise()), Blocks.AIR, blocksToNotAdd));

        tempStacks.addAll(
                BuildingMethods.CreateWall(world, 3, 3, facing.getClockWise(), pos.relative(facing.getOpposite(), 3).relative(facing.getCounterClockWise()), Blocks.STONE, blocksToNotAdd));

        // East wall.
        tempStacks.addAll(BuildingMethods.CreateWall(world, 3, 4, facing, pos.relative(facing.getOpposite(), 2).relative(facing.getClockWise()), Blocks.AIR, blocksToNotAdd));
        tempStacks.addAll(BuildingMethods.CreateWall(world, 3, 4, facing, pos.relative(facing.getOpposite(), 2).relative(facing.getClockWise(), 2), Blocks.STONE, blocksToNotAdd));

        // North wall.
        tempStacks.addAll(BuildingMethods.CreateWall(world, 3, 3, facing.getCounterClockWise(), pos.relative(facing).relative(facing.getClockWise()), Blocks.AIR, blocksToNotAdd));
        tempStacks.addAll(BuildingMethods.CreateWall(world, 3, 3, facing.getCounterClockWise(), pos.relative(facing, 2).relative(facing.getClockWise()), Blocks.STONE, blocksToNotAdd));

        // West wall.
        tempStacks.addAll(BuildingMethods.CreateWall(world, 3, 4, facing.getOpposite(), pos.relative(facing).relative(facing.getCounterClockWise()), Blocks.AIR, blocksToNotAdd));
        tempStacks.addAll(BuildingMethods.CreateWall(world, 3, 4, facing.getOpposite(), pos.relative(facing, 1).relative(facing.getCounterClockWise(), 2), Blocks.STONE, blocksToNotAdd));

        // Consolidate the stacks.
        for (ItemStack tempStack : tempStacks) {
            boolean foundStack = false;

            for (ItemStack existingStack : stacks) {
                if (ItemStack.isSame(existingStack, tempStack)) {
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
        BlockState blockState = Blocks.TORCH.defaultBlockState();
        BuildingMethods.ReplaceBlock(world, pos.relative(facing.getCounterClockWise()), blockState);

        if (CommonProxy.proxyConfiguration.serverConfiguration.includeMineshaftChest) {
            // Place a chest to the right of the ladder.
            BlockState chestState = Blocks.CHEST.defaultBlockState().setValue(ChestBlock.FACING, facing);
            BuildingMethods.ReplaceBlock(world, pos.relative(facing.getClockWise()), chestState, 2);
            blockPositions.add(pos.relative(facing.getClockWise()));

            if (stacks.size() > 27) {
                // Add another chest to south of the existing chest.
                BuildingMethods.ReplaceBlock(world, pos.relative(facing.getClockWise()).relative(facing.getOpposite()), chestState, 2);
                blockPositions.add(pos.relative(facing.getClockWise()).relative(facing.getOpposite()));
            }

            BlockEntity tileEntity = world.getBlockEntity(pos.relative(facing.getClockWise()));
            BlockEntity tileEntity2 = world.getBlockEntity(pos.relative(facing.getClockWise()).relative(facing.getOpposite()));

            if (tileEntity instanceof ChestBlockEntity) {
                ChestBlockEntity chestTile = (ChestBlockEntity) tileEntity;
                ChestBlockEntity chestTile2 = (ChestBlockEntity) tileEntity2;

                int i = 0;
                boolean fillSecond = false;

                // All the stacks should be consolidated at this point.
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
                        chestTile.setItem(i, stack);
                    }

                    i++;
                }
            }
        }

        for (BlockPos currentPos : blockPositions) {
            Block block = world.getBlockState(pos).getBlock();
            world.blockUpdated(pos, block);
        }
    }

    private static Triple<ArrayList<ItemStack>, ArrayList<BlockPos>, ArrayList<BlockPos>> CreateLadderShaft(
            ServerLevel world,
            BlockPos pos,
            ArrayList<ItemStack> originalStacks,
            Direction houseFacing,
            ArrayList<Item> blocksToNotAdd,
            int minimumHeightForMineshaft) {
        int torchCounter = 0;

        // Keep the "west" facing.
        Direction westWall = houseFacing.getCounterClockWise();

        // Get the ladder state based on the house facing.
        BlockState ladderState = Blocks.LADDER.defaultBlockState().setValue(LadderBlock.FACING, houseFacing);

        // Replace the main floor block with air since we don't want it placed in the chest at the end.
        BuildingMethods.ReplaceBlock(world, pos, Blocks.AIR);
        ArrayList<BlockPos> torchPositions = new ArrayList<>();
        ArrayList<BlockPos> allBlockPositions = new ArrayList<>();

        int lastHeightForTorch = minimumHeightForMineshaft + 6;

        while (pos.getY() > minimumHeightForMineshaft) {
            BlockState state = world.getBlockState(pos);
            Block block = state.getBlock();
            torchCounter++;

            // Make sure all blocks around this one are solid, if they are not
            // replace them with stone.
            for (int i = 0; i < 4; i++) {
                Direction facing;

                switch (i) {
                    case 1: {
                        facing = houseFacing.getClockWise();
                        break;
                    }
                    case 2: {
                        facing = houseFacing.getOpposite();
                        break;
                    }
                    case 3: {
                        facing = houseFacing.getCounterClockWise();
                        break;
                    }
                    default: {
                        facing = houseFacing;
                    }
                }

                // Every 6 blocks, place a torch on the west wall.
                // If we are close to the bottom, don't place a torch. Do the
                // normal processing.
                if (facing == westWall && torchCounter == 6 && pos.getY() > lastHeightForTorch) {
                    // First make sure the blocks around this block are stone, then place the torch.
                    for (int j = 0; j <= 2; j++) {
                        BlockPos tempPos;
                        BlockState surroundingState;
                        Block surroundingBlock;

                        if (j == 0) {
                            tempPos = pos.relative(facing, 2);
                            surroundingState = world.getBlockState(tempPos);
                            surroundingBlock = surroundingState.getBlock();
                        } else if (j == 1) {
                            tempPos = pos.relative(facing).relative(facing.getClockWise());
                            surroundingState = world.getBlockState(tempPos);
                            surroundingBlock = surroundingState.getBlock();
                        } else {
                            tempPos = pos.relative(facing).relative(facing.getCounterClockWise());
                            surroundingState = world.getBlockState(tempPos);
                            surroundingBlock = surroundingState.getBlock();
                        }

                        // Make sure that this is a normal solid block and not a liquid or partial block.
                        if (!(surroundingBlock == Blocks.STONE || surroundingBlock == Blocks.ANDESITE || surroundingBlock == Blocks.DIORITE || surroundingBlock == Blocks.GRANITE)) {
                            // This is not a stone block. Get the drops then replace it with stone.
                            originalStacks = BuildingMethods.ConsolidateDrops(world, tempPos, surroundingState, originalStacks, blocksToNotAdd);

                            BuildingMethods.ReplaceBlock(world, tempPos, Blocks.STONE.defaultBlockState(), 2);
                            allBlockPositions.add(tempPos);
                        }
                    }

                    torchPositions.add(pos.relative(facing));
                    torchCounter = 0;
                } else {
                    BlockPos tempPos = pos.relative(facing);
                    BlockState surroundingState = world.getBlockState(tempPos);
                    Block surroundingBlock = surroundingState.getBlock();

                    if (!surroundingState.isRedstoneConductor(world, tempPos)
                            || surroundingBlock instanceof LiquidBlock) {
                        // This is not a solid block. Get the drops then replace
                        // it with stone.
                        originalStacks = BuildingMethods.ConsolidateDrops(world, tempPos, surroundingState, originalStacks, blocksToNotAdd);

                        BuildingMethods.ReplaceBlock(world, tempPos, Blocks.STONE.defaultBlockState(), 2);
                        allBlockPositions.add(tempPos);
                    }
                }
            }

            // Get the block drops then replace it with a ladder.
            originalStacks = BuildingMethods.ConsolidateDrops(world, pos, state, originalStacks, blocksToNotAdd);

            // Don't place a ladder at this location since it will be destroyed.
            if (pos.getY() >= minimumHeightForMineshaft) {
                BuildingMethods.ReplaceBlock(world, pos, ladderState, 2);
                allBlockPositions.add(pos);
            }

            pos = pos.below();
        }

        return new Triple<>(originalStacks, torchPositions, allBlockPositions);
    }

    public static BlockState getStainedGlassBlock(FullDyeColor color) {
        switch (color) {
            case BLACK: {
                return Blocks.BLACK_STAINED_GLASS.defaultBlockState();
            }
            case BLUE: {
                return Blocks.BLUE_STAINED_GLASS.defaultBlockState();
            }
            case BROWN: {
                return Blocks.BROWN_STAINED_GLASS.defaultBlockState();
            }
            case GRAY: {
                return Blocks.GRAY_STAINED_GLASS.defaultBlockState();
            }
            case GREEN: {
                return Blocks.GREEN_STAINED_GLASS.defaultBlockState();
            }
            case LIGHT_BLUE: {
                return Blocks.LIGHT_BLUE_STAINED_GLASS.defaultBlockState();
            }
            case LIGHT_GRAY: {
                return Blocks.LIGHT_GRAY_STAINED_GLASS.defaultBlockState();
            }
            case LIME: {
                return Blocks.LIME_STAINED_GLASS.defaultBlockState();
            }
            case MAGENTA: {
                return Blocks.MAGENTA_STAINED_GLASS.defaultBlockState();
            }
            case ORANGE: {
                return Blocks.ORANGE_STAINED_GLASS.defaultBlockState();
            }
            case PINK: {
                return Blocks.PINK_STAINED_GLASS.defaultBlockState();
            }
            case PURPLE: {
                return Blocks.PURPLE_STAINED_GLASS.defaultBlockState();
            }
            case RED: {
                return Blocks.RED_STAINED_GLASS.defaultBlockState();
            }
            case WHITE: {
                return Blocks.WHITE_STAINED_GLASS.defaultBlockState();
            }
            case YELLOW: {
                return Blocks.YELLOW_STAINED_GLASS.defaultBlockState();
            }
            case CLEAR: {
                return Blocks.GLASS.defaultBlockState();
            }
            default: {
                return Blocks.CYAN_STAINED_GLASS.defaultBlockState();
            }
        }
    }

    public static BlockState getStainedGlassPaneBlock(FullDyeColor color) {
        switch (color) {
            case BLACK: {
                return Blocks.BLACK_STAINED_GLASS_PANE.defaultBlockState();
            }
            case BLUE: {
                return Blocks.BLUE_STAINED_GLASS_PANE.defaultBlockState();
            }
            case BROWN: {
                return Blocks.BROWN_STAINED_GLASS_PANE.defaultBlockState();
            }
            case GRAY: {
                return Blocks.GRAY_STAINED_GLASS_PANE.defaultBlockState();
            }
            case GREEN: {
                return Blocks.GREEN_STAINED_GLASS_PANE.defaultBlockState();
            }
            case LIGHT_BLUE: {
                return Blocks.LIGHT_BLUE_STAINED_GLASS_PANE.defaultBlockState();
            }
            case LIGHT_GRAY: {
                return Blocks.LIGHT_GRAY_STAINED_GLASS_PANE.defaultBlockState();
            }
            case LIME: {
                return Blocks.LIME_STAINED_GLASS_PANE.defaultBlockState();
            }
            case MAGENTA: {
                return Blocks.MAGENTA_STAINED_GLASS_PANE.defaultBlockState();
            }
            case ORANGE: {
                return Blocks.ORANGE_STAINED_GLASS_PANE.defaultBlockState();
            }
            case PINK: {
                return Blocks.PINK_STAINED_GLASS_PANE.defaultBlockState();
            }
            case PURPLE: {
                return Blocks.PURPLE_STAINED_GLASS_PANE.defaultBlockState();
            }
            case RED: {
                return Blocks.RED_STAINED_GLASS_PANE.defaultBlockState();
            }
            case WHITE: {
                return Blocks.WHITE_STAINED_GLASS_PANE.defaultBlockState();
            }
            case YELLOW: {
                return Blocks.YELLOW_STAINED_GLASS_PANE.defaultBlockState();
            }
            case CLEAR: {
                return Blocks.GLASS_PANE.defaultBlockState();
            }
            default: {
                return Blocks.CYAN_STAINED_GLASS_PANE.defaultBlockState();
            }
        }
    }
}
