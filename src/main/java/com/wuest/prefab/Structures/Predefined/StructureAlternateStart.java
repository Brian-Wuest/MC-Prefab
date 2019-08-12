package com.wuest.prefab.Structures.Predefined;

import com.wuest.prefab.Proxy.CommonProxy;
import com.wuest.prefab.Structures.Base.*;
import com.wuest.prefab.Structures.Config.HouseConfiguration;
import com.wuest.prefab.Structures.Config.StructureConfiguration;
import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.tileentity.FurnaceTileEntity;
import net.minecraft.tileentity.SignTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;

/**
 * @author WuestMan
 */
public class StructureAlternateStart extends Structure {
    private static ArrayList<BlockPos> torchPositions = null;
    private BlockPos chestPosition = null;
    private BlockPos furnacePosition = null;
    private BlockPos trapDoorPosition = null;
    private BlockPos signPosition = null;

    public static void ScanBasicHouseStructure(World world, BlockPos originalPos, Direction playerFacing) {
        BuildClear clearedSpace = new BuildClear();
        clearedSpace.getShape().setDirection(Direction.SOUTH);
        clearedSpace.getShape().setHeight(10);
        clearedSpace.getShape().setLength(14);
        clearedSpace.getShape().setWidth(11);
        clearedSpace.getStartingPosition().setSouthOffset(1);
        clearedSpace.getStartingPosition().setEastOffset(3);
        clearedSpace.getStartingPosition().setHeightOffset(-1);

        BlockPos corner = originalPos.east(3).south().down();
        BlockPos corner2 = originalPos.west(8).south(15).up(10);

        Structure.ScanStructure(world, originalPos, corner, corner2,
                "..\\src\\main\\resources\\assets\\prefab\\structures\\basic_house.zip", clearedSpace,
                playerFacing, false, false);
    }

    public static void ScanRanchStructure(World world, BlockPos originalPos, Direction playerFacing) {
        BuildClear clearedSpace = new BuildClear();
        clearedSpace.getShape().setDirection(Direction.SOUTH);
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

    public static void ScanLoftStructure(World world, BlockPos originalPos, Direction playerFacing) {
        BuildClear clearedSpace = new BuildClear();
        clearedSpace.getShape().setDirection(Direction.SOUTH);
        clearedSpace.getShape().setHeight(9);
        clearedSpace.getShape().setLength(13);
        clearedSpace.getShape().setWidth(16);
        clearedSpace.getStartingPosition().setSouthOffset(1);
        clearedSpace.getStartingPosition().setEastOffset(7);

        Structure.ScanStructure(world, originalPos, originalPos.east(7).south(), originalPos.south(14).west(8).up(9),
                "..\\src\\main\\resources\\assets\\prefab\\structures\\loft_house.zip", clearedSpace, playerFacing, false, false);
    }

    public static void ScanHobbitStructure(World world, BlockPos originalPos, Direction playerFacing) {
        BuildClear clearedSpace = new BuildClear();
        clearedSpace.getShape().setDirection(Direction.SOUTH);
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

    public static void ScanStructure(World world, BlockPos originalPos, Direction playerFacing, String structureFileName, boolean includeAir, boolean excludeWater) {
        BuildClear clearedSpace = new BuildClear();
        clearedSpace.getShape().setDirection(Direction.SOUTH);
        clearedSpace.getShape().setHeight(8);
        clearedSpace.getShape().setLength(16);
        clearedSpace.getShape().setWidth(16);
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
                "..\\src\\main\\resources\\assets\\prefab\\structures\\" + structureFileName + ".zip",
                clearedSpace,
                playerFacing,
                includeAir,
                excludeWater);
    }

    public static void FillChest(World world, BlockPos itemPosition, HouseConfiguration configuration, PlayerEntity player) {
        // Add each stone tool to the chest and leather armor.
        TileEntity tileEntity = world.getTileEntity(itemPosition);

        if (tileEntity instanceof ChestTileEntity) {
            ChestTileEntity chestTile = (ChestTileEntity) tileEntity;

            int itemSlot = 0;

            // Add the tools.
            if (CommonProxy.proxyConfiguration.serverConfiguration.addAxe) {
                chestTile.setInventorySlotContents(itemSlot++, new ItemStack(Items.STONE_AXE));
            }

            if (CommonProxy.proxyConfiguration.serverConfiguration.addHoe) {
                chestTile.setInventorySlotContents(itemSlot++, new ItemStack(Items.STONE_HOE));
            }

            if (CommonProxy.proxyConfiguration.serverConfiguration.addPickAxe) {
                // Trigger the "Time to Mine" achievement and the better
                // pick axe achievement.
                chestTile.setInventorySlotContents(itemSlot++, new ItemStack(Items.STONE_PICKAXE));
            }

            if (CommonProxy.proxyConfiguration.serverConfiguration.addShovel) {
                chestTile.setInventorySlotContents(itemSlot++, new ItemStack(Items.STONE_SHOVEL));
            }

            if (CommonProxy.proxyConfiguration.serverConfiguration.addSword) {
                Item sword = Items.STONE_SWORD;

                if (ModList.get().isLoaded("repurpose")) {
                    ResourceLocation name = new ResourceLocation("repurpose", "itemSwiftBladeStone");

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
        }

        stacks = StructureAlternateStart.CreateLadderShaft(world, pos, stacks, facing, blocksToNotAdd);

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
        for (BlockPos torchPos : StructureAlternateStart.torchPositions) {
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
            Boolean foundStack = false;

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

    private static ArrayList<ItemStack> CreateLadderShaft(ServerWorld world, BlockPos pos, ArrayList<ItemStack> originalStacks, Direction houseFacing,
                                                          ArrayList<Item> blocksToNotAdd) {
        int torchCounter = 0;

        // Keep the "west" facing.
        Direction westWall = houseFacing.rotateYCCW();

        // Get the ladder state based on the house facing.
        BlockState ladderState = Blocks.LADDER.getDefaultState().with(LadderBlock.FACING, houseFacing);

        // Replace the main floor block with air since we don't want it placed in the chest at the end.
        BuildingMethods.ReplaceBlock(world, pos, Blocks.AIR);
        StructureAlternateStart.torchPositions = new ArrayList<BlockPos>();

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

                    StructureAlternateStart.torchPositions.add(pos.offset(facing));
                    torchCounter = 0;
                } else {
                    BlockPos tempPos = pos.offset(facing);
                    BlockState surroundingState = world.getBlockState(tempPos);
                    Block surroundingBlock = surroundingState.getBlock();

                    if (!surroundingBlock.isNormalCube(surroundingState, world, tempPos)
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

        return originalStacks;
    }

    @Override
    protected Boolean CustomBlockProcessingHandled(StructureConfiguration configuration, BuildBlock block, World world, BlockPos originalPos,
                                                   Direction assumedNorth, Block foundBlock, BlockState blockState, PlayerEntity player) {
        HouseConfiguration houseConfig = (HouseConfiguration) configuration;

        if ((!houseConfig.addBed && foundBlock instanceof BedBlock) || (!houseConfig.addChest && foundBlock instanceof ChestBlock)
                || (!houseConfig.addTorches && foundBlock instanceof TorchBlock)
                || (!houseConfig.addCraftingTable && foundBlock instanceof CraftingTableBlock)
                || (!houseConfig.addFurnace && foundBlock instanceof FurnaceBlock)) {
            // Don't place the block, returning true means that this has been
            // "handled"
            return true;
        }

        if (foundBlock instanceof FurnaceBlock) {
            this.furnacePosition = block.getStartingPosition().getRelativePosition(
                    originalPos,
                    this.getClearSpace().getShape().getDirection(),
                    configuration.houseFacing);
        } else if (foundBlock instanceof TrapDoorBlock && houseConfig.addMineShaft) {
            // The trap door will still be added, but the mine shaft may not be
            // built.
            this.trapDoorPosition = block.getStartingPosition().getRelativePosition(
                    originalPos,
                    this.getClearSpace().getShape().getDirection(),
                    configuration.houseFacing);
        } else if (foundBlock instanceof ChestBlock && this.chestPosition == null) {
            this.chestPosition = block.getStartingPosition().getRelativePosition(
                    originalPos,
                    this.getClearSpace().getShape().getDirection(),
                    configuration.houseFacing);
        } else if (foundBlock instanceof StandingSignBlock) {
            this.signPosition = block.getStartingPosition().getRelativePosition(
                    originalPos,
                    this.getClearSpace().getShape().getDirection(),
                    configuration.houseFacing);
        }

        if (foundBlock.getRegistryName().getNamespace().equals(Blocks.WHITE_STAINED_GLASS.getRegistryName().getNamespace())
                && foundBlock.getRegistryName().getPath().endsWith("stained_glass")) {
            blockState = this.getStainedGlassBlock(houseConfig.glassColor);

            block.setBlockState(blockState);
            this.priorityOneBlocks.add(block);

            return true;
        } else if (foundBlock.getRegistryName().getNamespace().equals(Blocks.WHITE_STAINED_GLASS_PANE.getRegistryName().getNamespace())
                && foundBlock.getRegistryName().getPath().endsWith("stained_glass_pane")) {
            blockState = this.getStainedGlassPaneBlock(houseConfig.glassColor);
            block.setBlockState(blockState);

            this.priorityOneBlocks.add(block);
            return true;
        }

        return false;
    }

    /**
     * This method is used after the main building is build for any additional structures or modifications.
     *
     * @param configuration The structure configuration.
     * @param world         The current world.
     * @param originalPos   The original position clicked on.
     * @param assumedNorth  The assumed northern direction.
     * @param player        The player which initiated the construction.
     */
    @Override
    public void AfterBuilding(StructureConfiguration configuration, ServerWorld world, BlockPos originalPos, Direction assumedNorth, PlayerEntity player) {
        HouseConfiguration houseConfig = (HouseConfiguration) configuration;

        if (this.furnacePosition != null) {
            // Fill the furnace.
            TileEntity tileEntity = world.getTileEntity(this.furnacePosition);
            if (tileEntity instanceof FurnaceTileEntity) {
                FurnaceTileEntity furnaceTile = (FurnaceTileEntity) tileEntity;
                furnaceTile.setInventorySlotContents(1, new ItemStack(Items.COAL, 20));
            }
        }

        if (this.chestPosition != null && houseConfig.addChestContents) {
            // Fill the chest.
            StructureAlternateStart.FillChest(world, this.chestPosition, houseConfig, player);
        }

        if (this.trapDoorPosition != null && this.trapDoorPosition.getY() > 15 && houseConfig.addMineShaft) {
            // Build the mineshaft.
            StructureAlternateStart.PlaceMineShaft(world, this.trapDoorPosition.down(), houseConfig.houseFacing, false);
        }

        if (this.signPosition != null) {
            TileEntity tileEntity = world.getTileEntity(this.signPosition);

            if (tileEntity instanceof SignTileEntity) {
                SignTileEntity signTile = (SignTileEntity) tileEntity;
                signTile.signText[0] = new StringTextComponent("This is");

                if (player.getDisplayName().getString().length() >= 15) {
                    signTile.signText[1] = new StringTextComponent(player.getDisplayName().getString());
                } else {
                    signTile.signText[1] = new StringTextComponent(player.getDisplayName().getString() + "'s");
                }

                signTile.signText[2] = new StringTextComponent("house!");
            }
        }
    }
}
