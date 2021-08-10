package com.wuest.prefab.structures.predefined;

import com.wuest.prefab.Tuple;
import com.wuest.prefab.structures.base.*;
import com.wuest.prefab.structures.config.BasicStructureConfiguration;
import com.wuest.prefab.structures.config.BasicStructureConfiguration.EnumBasicStructureName;
import com.wuest.prefab.structures.config.StructureConfiguration;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;

import java.util.ArrayList;

/**
 * This is the basic structure to be used for structures which don't need a lot of configuration or a custom player
 * created structures.
 *
 * @author WuestMan
 */
public class StructureBasic extends Structure {
    private BlockPos customBlockPos = null;
    private ArrayList<Tuple<BlockPos, BlockPos>> bedPositions = new ArrayList<>();

    public static void ScanStructure(Level world, BlockPos originalPos, Direction playerFacing, BasicStructureConfiguration configuration, boolean includeAir, boolean excludeWater) {
        BuildClear clearedSpace = new BuildClear();
        clearedSpace.setShape(configuration.chosenOption.getClearShape());
        clearedSpace.setStartingPosition(configuration.chosenOption.getClearPositionOffset());
        clearedSpace.getShape().setDirection(playerFacing);

        if (!configuration.IsCustomStructure()) {
            BuildShape buildShape = configuration.chosenOption.getClearShape().Clone();

            // Scanning the structure doesn't contain the starting corner block but the clear does.
            buildShape.setWidth(buildShape.getWidth() - 1);
            buildShape.setLength(buildShape.getLength() - 1);

            PositionOffset offset = configuration.chosenOption.getClearPositionOffset();

            clearedSpace.getShape().setWidth(clearedSpace.getShape().getWidth());
            clearedSpace.getShape().setLength(clearedSpace.getShape().getLength());

            int downOffset = offset.getHeightOffset() < 0 ? Math.abs(offset.getHeightOffset()) : 0;
            BlockPos cornerPos = originalPos
                    .relative(playerFacing.getCounterClockWise(), offset.getOffSetValueForFacing(playerFacing.getCounterClockWise()))
                    .relative(playerFacing, offset.getOffSetValueForFacing(playerFacing))
                    .below(downOffset);

            BlockPos otherCorner = cornerPos
                    .relative(playerFacing, buildShape.getLength())
                    .relative(playerFacing.getClockWise(), buildShape.getWidth())
                    .above(buildShape.getHeight());

            Structure.ScanStructure(
                    world,
                    originalPos,
                    cornerPos,
                    otherCorner,
                    "..\\src\\main\\resources\\" + configuration.chosenOption.getAssetLocation(),
                    clearedSpace,
                    playerFacing,
                    includeAir,
                    excludeWater);
        }
    }

    @Override
    protected Boolean CustomBlockProcessingHandled(StructureConfiguration configuration, BuildBlock block, Level world, BlockPos originalPos,
                                                   Direction assumedNorth, Block foundBlock, BlockState blockState, Player player) {
        BasicStructureConfiguration config = (BasicStructureConfiguration) configuration;

        if (foundBlock instanceof HopperBlock && config.basicStructureName.getName().equals(EnumBasicStructureName.AdvancedCoop.getName())) {
            this.customBlockPos = block.getStartingPosition().getRelativePosition(
                    originalPos,
                    this.getClearSpace().getShape().getDirection(),
                    configuration.houseFacing);
        } else if (foundBlock instanceof FenceGateBlock && config.basicStructureName.getName().equals(EnumBasicStructureName.ChickenCoop.getName())) {
            this.customBlockPos = block.getStartingPosition().getRelativePosition(
                            originalPos,
                            this.getClearSpace().getShape().getDirection(),
                            configuration.houseFacing)
                    .relative(configuration.houseFacing.getOpposite(), 2)
                    .above();
        } else if (foundBlock instanceof TrapDoorBlock && config.basicStructureName.getName().equals(EnumBasicStructureName.MineshaftEntrance.getName())) {
            this.customBlockPos = block.getStartingPosition().getRelativePosition(
                    originalPos,
                    this.getClearSpace().getShape().getDirection(),
                    configuration.houseFacing);
        } else if (foundBlock == Blocks.SPONGE
                && config.basicStructureName.getName().equals(BasicStructureConfiguration.EnumBasicStructureName.WorkShop.getName())) {
            // Sponges are sometimes used in-place of trapdoors when trapdoors are used for decoration.
            this.customBlockPos = block.getStartingPosition().getRelativePosition(
                    originalPos,
                    this.getClearSpace().getShape().getDirection(),
                    configuration.houseFacing).above();
        } else if (foundBlock instanceof BedBlock && config.chosenOption.getHasBedColor()) {
            // Even if a structure has a bed; we may want to keep a specific color to match what the design of the structure is.
            BlockPos bedHeadPosition = block.getStartingPosition().getRelativePosition(originalPos, this.getClearSpace().getShape().getDirection(), configuration.houseFacing);
            BlockPos bedFootPosition = block.getSubBlock().getStartingPosition().getRelativePosition(
                    originalPos,
                    this.getClearSpace().getShape().getDirection(),
                    configuration.houseFacing);

            this.bedPositions.add(new Tuple<>(bedHeadPosition, bedFootPosition));

            return true;
        } else if (foundBlock.getRegistryName().getNamespace().equals(Blocks.WHITE_STAINED_GLASS.getRegistryName().getNamespace())
                && foundBlock.getRegistryName().getPath().endsWith("stained_glass")
                && config.chosenOption.getHasGlassColor()) {
            blockState = this.getStainedGlassBlock(config.glassColor);
            block.setBlockState(blockState);
            this.priorityOneBlocks.add(block);

            return true;
        } else if (foundBlock.getRegistryName().getNamespace().equals(Blocks.WHITE_STAINED_GLASS_PANE.getRegistryName().getNamespace())
                && foundBlock.getRegistryName().getPath().endsWith("stained_glass_pane")
                && config.chosenOption.getHasGlassColor()) {
            blockState = this.getStainedGlassPaneBlock(config.glassColor);

            BuildBlock.SetBlockState(
                    configuration,
                    world,
                    originalPos,
                    assumedNorth,
                    block,
                    foundBlock,
                    blockState,
                    this);

            this.priorityOneBlocks.add(block);
            return true;
        }

        return false;
    }

    @Override
    protected Boolean BlockShouldBeClearedDuringConstruction(StructureConfiguration configuration, Level world, BlockPos originalPos, Direction assumedNorth, BlockPos blockPos) {
        BasicStructureConfiguration config = (BasicStructureConfiguration) configuration;

        if (config.basicStructureName.getName().equals(EnumBasicStructureName.AquaBase.getName())
                || config.basicStructureName.getName().equals(EnumBasicStructureName.AdvancedAquaBase.getName())) {
            BlockState blockState = world.getBlockState(blockPos);
            // Don't clear water blocks for this building.
            return blockState.getMaterial() != Material.WATER;
        }

        return true;
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
    public void AfterBuilding(StructureConfiguration configuration, ServerLevel world, BlockPos originalPos, Direction assumedNorth, Player player) {
        BasicStructureConfiguration config = (BasicStructureConfiguration) configuration;

        if (this.customBlockPos != null) {
            if (config.basicStructureName.getName().equals(EnumBasicStructureName.AdvancedCoop.getName())) {
                // For the advanced chicken coop, spawn 4 chickens above the hopper.
                for (int i = 0; i < 4; i++) {
                    Chicken entity = new Chicken(EntityType.CHICKEN, world);
                    entity.setPos(this.customBlockPos.getX(), this.customBlockPos.above().getY(), this.customBlockPos.getZ());
                    world.addFreshEntity(entity);
                }
            } else if (config.basicStructureName.getName().equals(EnumBasicStructureName.ChickenCoop.getName())) {
                // For the advanced chicken coop, spawn 4 chickens above the hopper.
                Chicken entity = new Chicken(EntityType.CHICKEN, world);
                entity.setPos(this.customBlockPos.getX(), this.customBlockPos.above().getY(), this.customBlockPos.getZ());
                world.addFreshEntity(entity);
            } else if (config.basicStructureName.getName().equals(EnumBasicStructureName.MineshaftEntrance.getName())
                    || config.basicStructureName.getName().equals(BasicStructureConfiguration.EnumBasicStructureName.WorkShop.getName())) {
                // Build the mineshaft where the trap door exists.
                BuildingMethods.PlaceMineShaft(world, this.customBlockPos.below(), configuration.houseFacing, true);
            }

            this.customBlockPos = null;
        }

        if (this.bedPositions.size() > 0) {
            for (Tuple<BlockPos, BlockPos> bedPosition : this.bedPositions) {
                BuildingMethods.PlaceColoredBed(world, bedPosition.getFirst(), bedPosition.getSecond(), config.bedColor);
            }
        }

        if (config.basicStructureName.getName().equals(EnumBasicStructureName.AquaBase.getName())
                || config.basicStructureName.getName().equals(EnumBasicStructureName.AdvancedAquaBase.getName())) {
            // Replace the entrance area with air blocks.
            BlockPos airPos = originalPos.above(4).relative(configuration.houseFacing.getOpposite(), 1);

            // This is the first wall.
            world.removeBlock(airPos.relative(configuration.houseFacing.getClockWise()), false);
            world.removeBlock(airPos, false);
            world.removeBlock(airPos.relative(configuration.houseFacing.getCounterClockWise()), false);

            airPos = airPos.below();
            world.removeBlock(airPos.relative(configuration.houseFacing.getClockWise()), false);
            world.removeBlock(airPos, false);
            world.removeBlock(airPos.relative(configuration.houseFacing.getCounterClockWise()), false);

            airPos = airPos.below();
            world.removeBlock(airPos.relative(configuration.houseFacing.getClockWise()), false);
            world.removeBlock(airPos, false);
            world.removeBlock(airPos.relative(configuration.houseFacing.getCounterClockWise()), false);

            airPos = airPos.below();
            world.removeBlock(airPos.relative(configuration.houseFacing.getClockWise()), false);
            world.removeBlock(airPos, false);
            world.removeBlock(airPos.relative(configuration.houseFacing.getCounterClockWise()), false);

            // Second part of the wall.
            airPos = airPos.relative(configuration.houseFacing.getOpposite()).above();
            world.removeBlock(airPos.relative(configuration.houseFacing.getClockWise()), false);
            world.removeBlock(airPos, false);
            world.removeBlock(airPos.relative(configuration.houseFacing.getCounterClockWise()), false);

            airPos = airPos.above();
            world.removeBlock(airPos.relative(configuration.houseFacing.getClockWise()), false);
            world.removeBlock(airPos, false);
            world.removeBlock(airPos.relative(configuration.houseFacing.getCounterClockWise()), false);

            airPos = airPos.above();
            world.removeBlock(airPos, false);
        }
    }
}
