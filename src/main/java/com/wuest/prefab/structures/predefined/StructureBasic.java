package com.wuest.prefab.structures.predefined;

import com.wuest.prefab.Tuple;
import com.wuest.prefab.structures.base.*;
import com.wuest.prefab.structures.config.BasicStructureConfiguration;
import com.wuest.prefab.structures.config.BasicStructureConfiguration.EnumBasicStructureName;
import com.wuest.prefab.structures.config.StructureConfiguration;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.block.BlockHopper;
import net.minecraft.block.BlockTrapDoor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;

/**
 * This is the basic structure to be used for structures which don't need a lot of configuration or a custom player created structures.
 *
 * @author WuestMan
 */
public class StructureBasic extends Structure {
    BlockPos customBlockPos = null;
    private ArrayList<Tuple<BlockPos, BlockPos>> bedPositions = new ArrayList<>();

    public static void ScanStructure(World world, BlockPos originalPos, EnumFacing playerFacing, BasicStructureConfiguration configuration, boolean includeAir, boolean excludeWater) {
        BuildClear clearedSpace = new BuildClear();
        clearedSpace.setShape(configuration.chosenOption.getClearShape());
        clearedSpace.setStartingPosition(configuration.chosenOption.getClearPositionOffset());
        clearedSpace.getShape().setDirection(playerFacing);

        if (configuration.IsCustomStructure()) {
            // TODO: This needs to be programmed when custom structures are allowed.
        } else {
            BuildShape buildShape = configuration.chosenOption.getClearShape().Clone();

            // Scanning the structure doesn't contain the starting corner block but the clear does.
            buildShape.setWidth(buildShape.getWidth() - 1);
            buildShape.setLength(buildShape.getLength() - 1);

            PositionOffset offset = configuration.chosenOption.getClearPositionOffset();

            clearedSpace.getShape().setWidth(clearedSpace.getShape().getWidth());
            clearedSpace.getShape().setLength(clearedSpace.getShape().getLength());

            int downOffset = offset.getHeightOffset() < 0 ? Math.abs(offset.getHeightOffset()) : 0;
            BlockPos cornerPos = originalPos
                    .offset(playerFacing.rotateYCCW(), offset.getOffSetValueForFacing(playerFacing.rotateYCCW()))
                    .offset(playerFacing, offset.getOffSetValueForFacing(playerFacing))
                    .down(downOffset);

            BlockPos otherCorner = cornerPos
                    .offset(playerFacing, buildShape.getLength())
                    .offset(playerFacing.rotateY(), buildShape.getWidth())
                    .up(buildShape.getHeight());

            Structure.ScanStructure(
                    world,
                    originalPos,
                    cornerPos,
                    otherCorner,
                    "..\\src\\main\\resources\\assets\\prefab\\structures\\" + configuration.basicStructureName.getName() + ".zip",
                    clearedSpace,
                    playerFacing,
                    includeAir,
                    excludeWater);
        }
    }

    @Override
    protected Boolean CustomBlockProcessingHandled(StructureConfiguration configuration, BuildBlock block, World world, BlockPos originalPos,
                                                   EnumFacing assumedNorth, Block foundBlock, IBlockState blockState, EntityPlayer player) {
        BasicStructureConfiguration config = (BasicStructureConfiguration) configuration;

        if (foundBlock instanceof BlockHopper && config.basicStructureName.getName().equals(EnumBasicStructureName.AdvancedCoop.getName())) {
            customBlockPos = block.getStartingPosition().getRelativePosition(
                    originalPos,
                    this.getClearSpace().getShape().getDirection(),
                    configuration.houseFacing);
        } else if (foundBlock instanceof BlockTrapDoor && config.basicStructureName.getName().equals(EnumBasicStructureName.MineshaftEntrance.getName())) {
            customBlockPos = block.getStartingPosition().getRelativePosition(
                    originalPos,
                    this.getClearSpace().getShape().getDirection(),
                    configuration.houseFacing);
        } else if (foundBlock instanceof BlockBed) {
            BlockPos bedHeadPosition = block.getStartingPosition().getRelativePosition(originalPos, this.getClearSpace().getShape().getDirection(), configuration.houseFacing);
            BlockPos bedFootPosition = block.getSubBlock().getStartingPosition().getRelativePosition(
                    originalPos,
                    this.getClearSpace().getShape().getDirection(),
                    configuration.houseFacing);

            this.bedPositions.add(new Tuple<>(bedHeadPosition, bedFootPosition));

            return true;
        }

        return false;
    }

    /**
     * This method is used after the main building is build for any additional
     * structures or modifications.
     *
     * @param configuration The structure configuration.
     * @param world         The current world.
     * @param originalPos   The original position clicked on.
     * @param assumedNorth  The assumed northern direction.
     * @param player        The player which initiated the construction.
     */
    @Override
    public void AfterBuilding(StructureConfiguration configuration, World world, BlockPos originalPos, EnumFacing assumedNorth, EntityPlayer player) {
        BasicStructureConfiguration config = (BasicStructureConfiguration) configuration;

        if (this.customBlockPos != null) {
            if (config.basicStructureName.getName().equals(EnumBasicStructureName.AdvancedCoop.getName())) {
                // For the advanced chicken coop, spawn 4 chickens above the hopper.
                for (int i = 0; i < 4; i++) {
                    EntityChicken entity = new EntityChicken(world);
                    entity.setPosition(this.customBlockPos.getX(), this.customBlockPos.up().getY(), this.customBlockPos.getZ());
                    world.spawnEntity(entity);
                }
            } else if (config.basicStructureName.getName().equals(EnumBasicStructureName.MineshaftEntrance.getName())) {
                // Build the mineshaft where the trap door exists.
                StructureAlternateStart.PlaceMineShaft(world, this.customBlockPos.down(), configuration.houseFacing, true);
            }

            this.customBlockPos = null;
        }

        if (this.bedPositions.size() > 0) {
            for (Tuple<BlockPos, BlockPos> bedPosition : this.bedPositions) {
                BuildingMethods.PlaceColoredBed(world, bedPosition.getFirst(), bedPosition.getSecond(), config.bedColor);
            }
        }

        if (config.basicStructureName.getName().equals(EnumBasicStructureName.AquaBase.getName())) {
            // Replace the entrance area with air blocks.
            BlockPos airPos = originalPos.up(4).offset(configuration.houseFacing.getOpposite(), 1);

            // This is the first wall.
            world.setBlockToAir(airPos.offset(configuration.houseFacing.rotateY()));
            world.setBlockToAir(airPos);
            world.setBlockToAir(airPos.offset(configuration.houseFacing.rotateYCCW()));

            airPos = airPos.down();
            world.setBlockToAir(airPos.offset(configuration.houseFacing.rotateY()));
            world.setBlockToAir(airPos);
            world.setBlockToAir(airPos.offset(configuration.houseFacing.rotateYCCW()));

            airPos = airPos.down();
            world.setBlockToAir(airPos.offset(configuration.houseFacing.rotateY()));
            world.setBlockToAir(airPos);
            world.setBlockToAir(airPos.offset(configuration.houseFacing.rotateYCCW()));

            airPos = airPos.down();
            world.setBlockToAir(airPos.offset(configuration.houseFacing.rotateY()));
            world.setBlockToAir(airPos);
            world.setBlockToAir(airPos.offset(configuration.houseFacing.rotateYCCW()));

            // Second part of the wall.
            airPos = airPos.offset(configuration.houseFacing.getOpposite()).up();
            world.setBlockToAir(airPos.offset(configuration.houseFacing.rotateY()));
            world.setBlockToAir(airPos);
            world.setBlockToAir(airPos.offset(configuration.houseFacing.rotateYCCW()));

            airPos = airPos.up();
            world.setBlockToAir(airPos.offset(configuration.houseFacing.rotateY()));
            world.setBlockToAir(airPos);
            world.setBlockToAir(airPos.offset(configuration.houseFacing.rotateYCCW()));

            airPos = airPos.up();
            world.setBlockToAir(airPos);
        }
    }
}
