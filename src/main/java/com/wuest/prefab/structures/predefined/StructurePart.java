package com.wuest.prefab.structures.predefined;

import com.wuest.prefab.structures.base.BuildBlock;
import com.wuest.prefab.structures.base.BuildClear;
import com.wuest.prefab.structures.base.Structure;
import com.wuest.prefab.structures.config.StructureConfiguration;
import com.wuest.prefab.structures.config.StructurePartConfiguration;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.StairsShape;

import java.util.ArrayList;

/**
 * @author WuestMan
 */
@SuppressWarnings({"unused", "UnusedAssignment"})
public class StructurePart extends Structure {
    /*
     * Initializes a new instance of the StructurePart class.
     */
    public StructurePart() {
        super();
    }

    /**
     * Creates an instance of the structure after reading from a resource location and converting it from JSON.
     *
     * @return A new instance of this class.
     */
    public static StructurePart CreateInstance() {
        StructurePart structure = new StructurePart();
        return structure;
    }

    /**
     * This is the main building method for this structure.
     *
     * @param configuration The configuration the user updated.
     * @param world         The current world.
     * @param originalPos   The block the user clicked on.
     * @param assumedNorth  This should always be "NORTH" when the file is based on a scan.
     * @param player        The player requesting the structure.
     * @return True if the build can occur, otherwise false.
     */
    @Override
    public boolean BuildStructure(StructureConfiguration configuration, ServerLevel world, BlockPos originalPos, Direction assumedNorth, Player player) {
        StructurePartConfiguration specificConfig = (StructurePartConfiguration) configuration;

        this.setClearSpace(new BuildClear());

        this.setupStructure(world, specificConfig, originalPos);

        return super.BuildStructure(specificConfig, world, originalPos, assumedNorth, player);
    }

    public void setupStructure(Level world, StructurePartConfiguration configuration, BlockPos originalPos) {
        ArrayList<BuildBlock> buildingBlocks = new ArrayList<BuildBlock>();
        BlockState materialState = configuration.partMaterial.getBlockType();
        Direction facing = Direction.SOUTH;

        switch (configuration.style) {
            case Frame: {
                buildingBlocks = this.setupFrame(configuration, originalPos, materialState, facing);
                break;
            }

            case Gate: {
                buildingBlocks = this.setupGate(configuration, originalPos, materialState, facing);
                break;
            }

            case Stairs: {
                buildingBlocks = this.setupStairs(configuration, originalPos, configuration.stairsMaterial.stairsState, facing);
                break;
            }

            case Wall: {
                buildingBlocks = this.setupWall(configuration, originalPos, materialState, facing);
                break;
            }

            case DoorWay: {
                buildingBlocks = this.setupDoorway(configuration, originalPos, materialState, facing);
                break;
            }

            case Floor: {
                buildingBlocks = this.setupFloor(configuration, originalPos, materialState, facing);
                break;
            }

            case Roof: {
                buildingBlocks = this.setupRoof(configuration, originalPos, configuration.stairsMaterial.stairsState, facing);
                break;
            }

            default: {
                break;
            }
        }

        this.setBlocks(buildingBlocks);
    }

    private ArrayList<BuildBlock> setupFrame(StructurePartConfiguration configuration, BlockPos originalPos, BlockState materialState, Direction facing) {
        ArrayList<BuildBlock> buildingBlocks = new ArrayList<BuildBlock>();
        int width = configuration.generalWidth - 1;
        int height = configuration.generalHeight - 1;

        // Get all 8 Corners
        BlockPos lowerNearLeft = originalPos.west(configuration.generalWidth / 2);
        BlockPos upperNearLeft = lowerNearLeft.above(height);
        BlockPos lowerFarLeft = lowerNearLeft.north(width);
        BlockPos upperFarLeft = lowerNearLeft.north(width).above(height);
        BlockPos lowerNearRight = lowerNearLeft.east(width);
        BlockPos upperNearRight = lowerNearRight.above(height);
        BlockPos lowerFarRight = lowerNearRight.north(width);
        BlockPos upperFarRight = lowerNearRight.north(width).above(height);

        // Now make ALL connections.
        this.makeBlockListForPositions(buildingBlocks, configuration, originalPos, materialState, facing, lowerNearLeft, lowerFarLeft);
        this.makeBlockListForPositions(buildingBlocks, configuration, originalPos, materialState, facing, lowerNearLeft, lowerNearRight);
        this.makeBlockListForPositions(buildingBlocks, configuration, originalPos, materialState, facing, lowerNearLeft, upperNearLeft);
        this.makeBlockListForPositions(buildingBlocks, configuration, originalPos, materialState, facing, lowerFarLeft, lowerFarRight);
        this.makeBlockListForPositions(buildingBlocks, configuration, originalPos, materialState, facing, lowerFarLeft, upperFarLeft);
        this.makeBlockListForPositions(buildingBlocks, configuration, originalPos, materialState, facing, lowerNearRight, lowerFarRight);
        this.makeBlockListForPositions(buildingBlocks, configuration, originalPos, materialState, facing, lowerNearRight, upperNearRight);
        this.makeBlockListForPositions(buildingBlocks, configuration, originalPos, materialState, facing, lowerFarRight, upperFarRight);
        this.makeBlockListForPositions(buildingBlocks, configuration, originalPos, materialState, facing, upperNearLeft, upperNearRight);
        this.makeBlockListForPositions(buildingBlocks, configuration, originalPos, materialState, facing, upperNearLeft, upperFarLeft);
        this.makeBlockListForPositions(buildingBlocks, configuration, originalPos, materialState, facing, upperFarRight, upperFarLeft);
        this.makeBlockListForPositions(buildingBlocks, configuration, originalPos, materialState, facing, upperFarRight, upperNearRight);

        return buildingBlocks;
    }

    private void makeBlockListForPositions(ArrayList<BuildBlock> buildingBlocks, StructurePartConfiguration configuration, BlockPos originalPos,
                                           BlockState materialState, Direction facing, BlockPos position1, BlockPos position2) {
        for (BlockPos pos : BlockPos.betweenClosed(position1, position2)) {
            buildingBlocks.add(Structure.createBuildBlockFromBlockState(materialState, materialState.getBlock(), pos, originalPos));
        }
    }

    private ArrayList<BuildBlock> setupGate(StructurePartConfiguration configuration, BlockPos originalPos, BlockState materialState, Direction facing) {
        ArrayList<BuildBlock> buildingBlocks = new ArrayList<BuildBlock>();

        BlockPos gatePos = null;
        BlockPos gateOriginalPos = originalPos.west(configuration.generalWidth / 2).above();

        ArrayList<Long> ignoredPositions = new ArrayList<Long>();
        ignoredPositions.add(originalPos.above().asLong());
        ignoredPositions.add(originalPos.above(2).asLong());

        // Only create a 3x3 opening if there are enough blocks for it. Otherwise we are essentially doing nothing.
        if (configuration.generalWidth > 3 && configuration.generalHeight > 3) {
            ignoredPositions.add(originalPos.above(3).asLong());
            ignoredPositions.add(originalPos.above().west().asLong());
            ignoredPositions.add(originalPos.above(2).west().asLong());
            ignoredPositions.add(originalPos.above(3).west().asLong());

            ignoredPositions.add(originalPos.above().east().asLong());
            ignoredPositions.add(originalPos.above(2).east().asLong());
            ignoredPositions.add(originalPos.above(3).east().asLong());
        }

        for (int i = 0; i < configuration.generalHeight; i++) {
            // Reset gate building position to the starting position up by the
            // height counter.
            gatePos = gateOriginalPos.above(i);

            for (int j = 0; j < configuration.generalWidth; j++) {
                if (ignoredPositions.contains(gatePos.asLong())) {
                    gatePos = gatePos.relative(facing.getCounterClockWise());
                    continue;
                }

                // j is the north/south counter.
                buildingBlocks.add(Structure.createBuildBlockFromBlockState(materialState, materialState.getBlock(), gatePos, originalPos));

                gatePos = gatePos.relative(facing.getCounterClockWise());
            }
        }

        return buildingBlocks;
    }

    private ArrayList<BuildBlock> setupDoorway(StructurePartConfiguration configuration, BlockPos originalPos, BlockState materialState, Direction facing) {
        ArrayList<BuildBlock> buildingBlocks = new ArrayList<BuildBlock>();

        BlockPos gatePos = null;
        BlockPos gateOriginalPos = originalPos.west(configuration.generalWidth / 2).above();

        for (int i = 0; i < configuration.generalHeight; i++) {
            // Reset gate building position to the starting position up by the
            // height counter.
            gatePos = gateOriginalPos.above(i);

            for (int j = 0; j < configuration.generalWidth; j++) {
                if (gatePos.asLong() == originalPos.above().asLong() || gatePos.asLong() == originalPos.above(2).asLong()) {
                    gatePos = gatePos.relative(facing.getCounterClockWise());
                    continue;
                }

                // j is the north/south counter.
                buildingBlocks.add(Structure.createBuildBlockFromBlockState(materialState, materialState.getBlock(), gatePos, originalPos));

                gatePos = gatePos.relative(facing.getCounterClockWise());
            }
        }

        DoorBlock door = (DoorBlock) Blocks.OAK_DOOR;
        BuildBlock doorBlockBottom = Structure.createBuildBlockFromBlockState(door.defaultBlockState(), door, originalPos.above(), originalPos);
        BuildBlock doorBlockTop = Structure.createBuildBlockFromBlockState(door.defaultBlockState().setValue(DoorBlock.HALF, DoubleBlockHalf.UPPER),
                door, originalPos.above(2), originalPos);
        doorBlockBottom.setSubBlock(doorBlockTop);
        buildingBlocks.add(doorBlockBottom);

        return buildingBlocks;
    }

    private ArrayList<BuildBlock> setupStairs(StructurePartConfiguration configuration, BlockPos originalPos, BlockState materialState, Direction facing) {
        ArrayList<BuildBlock> buildingBlocks = new ArrayList<BuildBlock>();
        BlockPos stepPos = null;
        BlockPos stepOriginalPos = originalPos.west((int) (configuration.stairWidth - .2) / 2).above();

        for (int i = 0; i < configuration.stairHeight; i++) {
            // Reset step building position to the starting position up by the
            // height counter.
            stepPos = stepOriginalPos.above(i).north(i);

            for (int j = 0; j < configuration.stairWidth; j++) {
                // j is the north/south counter.
                buildingBlocks.add(Structure.createBuildBlockFromBlockState(materialState, materialState.getBlock(), stepPos, originalPos));

                stepPos = stepPos.relative(facing.getCounterClockWise());
            }
        }

        return buildingBlocks;
    }

    private ArrayList<BuildBlock> setupWall(StructurePartConfiguration configuration, BlockPos originalPos, BlockState materialState, Direction facing) {
        ArrayList<BuildBlock> buildingBlocks = new ArrayList<BuildBlock>();
        BlockPos wallPos = null;
        BlockPos wallOriginalPos = originalPos.west(configuration.generalWidth / 2).above();

        for (int i = 0; i < configuration.generalHeight; i++) {
            // Reset wall building position to the starting position up by the
            // height counter.
            wallPos = wallOriginalPos.above(i);

            for (int j = 0; j < configuration.generalWidth; j++) {
                // j is the north/south counter.
                buildingBlocks.add(Structure.createBuildBlockFromBlockState(materialState, materialState.getBlock(), wallPos, originalPos));

                wallPos = wallPos.relative(facing.getCounterClockWise());
            }
        }

        return buildingBlocks;
    }

    private ArrayList<BuildBlock> setupFloor(StructurePartConfiguration configuration, BlockPos originalPos, BlockState materialState, Direction facing) {
        ArrayList<BuildBlock> buildingBlocks = new ArrayList<BuildBlock>();
        BlockPos floorPos = null;
        BlockPos floorOriginalPos = originalPos.west(configuration.generalWidth / 2);

        for (int i = 0; i < configuration.generalHeight; i++) {
            // Reset wall building position to the starting position up by the
            // height counter.
            floorPos = floorOriginalPos.north(i);

            for (int j = 0; j < configuration.generalWidth; j++) {
                // j is the north/south counter.
                buildingBlocks.add(Structure.createBuildBlockFromBlockState(materialState, materialState.getBlock(), floorPos, originalPos));

                floorPos = floorPos.relative(facing.getCounterClockWise());
            }
        }

        return buildingBlocks;
    }

    private ArrayList<BuildBlock> setupRoof(StructurePartConfiguration configuration, BlockPos originalPos, BlockState materialState, Direction facing) {
        ArrayList<BuildBlock> buildingBlocks = new ArrayList<BuildBlock>();
        BlockPos wallPos = null;
        BlockPos wallOriginalPos = originalPos.west(configuration.stairWidth / 2).above();

        // Get the stairs state without the facing since it will change.
        BlockState stateWithoutFacing = materialState.setValue(StairBlock.HALF, Half.BOTTOM).setValue(StairBlock.SHAPE,
                StairsShape.STRAIGHT);

        int wallWidth = configuration.stairWidth;
        int wallDepth = configuration.stairWidth;
        int height = wallWidth / 2;
        boolean isWider = false;

        if (wallWidth > wallDepth) {
            height = wallDepth / 2;
            isWider = true;
        }

        wallPos = wallOriginalPos;

        for (int i = 0; i <= height; i++) {
            // I is the vaulted roof level.
            for (int j = 0; j < 4; j++) {
                // Default is depth.
                Direction tempFacing = facing.getCounterClockWise();
                Direction flowDirection = facing.getOpposite();
                int wallSize = wallDepth;

                switch (j) {
                    case 1: {
                        tempFacing = facing;
                        flowDirection = facing.getCounterClockWise();
                        wallSize = wallWidth;
                        break;
                    }

                    case 2: {
                        tempFacing = facing.getClockWise();
                        flowDirection = facing;
                        wallSize = wallDepth;
                        break;
                    }

                    case 3: {
                        tempFacing = facing.getOpposite();
                        flowDirection = facing.getClockWise();
                        wallSize = wallWidth;
                        break;
                    }
                }

                for (int k = 0; k <= wallSize; k++) {
                    // j is the north/south counter.
                    buildingBlocks.add(Structure.createBuildBlockFromBlockState(stateWithoutFacing.setValue(StairBlock.FACING, tempFacing),
                            materialState.getBlock(), wallPos, originalPos));

                    wallPos = wallPos.relative(flowDirection);
                }
            }

            wallPos = wallPos.relative(facing.getCounterClockWise()).relative(facing.getOpposite()).above();
            wallWidth = wallWidth - 2;
            wallDepth = wallDepth - 2;
        }

        long wallPosLong = wallPos.below().asLong();

        if (buildingBlocks.stream().noneMatch(x -> x.blockPos.asLong() == wallPosLong)) {
            // Create final blocks.
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

            for (int i = 0; i < finalStoneCount; i++) {
                buildingBlocks.add(Structure.createBuildBlockFromBlockState(configuration.stairsMaterial.getFullBlock(),
                        configuration.stairsMaterial.getFullBlock().getBlock(), wallPos, originalPos));

                if (isWider) {
                    wallPos = wallPos.relative(facing.getCounterClockWise());
                } else {
                    wallPos = wallPos.relative(facing.getOpposite());
                }
            }
        }

        return buildingBlocks;
    }
}
