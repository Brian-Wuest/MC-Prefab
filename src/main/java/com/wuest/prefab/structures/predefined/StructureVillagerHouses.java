package com.wuest.prefab.structures.predefined;

import com.wuest.prefab.Tuple;
import com.wuest.prefab.structures.base.BuildBlock;
import com.wuest.prefab.structures.base.BuildClear;
import com.wuest.prefab.structures.base.BuildingMethods;
import com.wuest.prefab.structures.base.Structure;
import com.wuest.prefab.structures.config.StructureConfiguration;
import com.wuest.prefab.structures.config.VillagerHouseConfiguration;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;

/**
 * This is the structure class used to generate simple villager houses.
 *
 * @author WuestMan
 */
public class StructureVillagerHouses extends Structure {
    private ArrayList<Tuple<BlockPos, BlockPos>> bedPositions = new ArrayList<>();

    public static void ScanStructure(World world, BlockPos originalPos, EnumFacing playerFacing, VillagerHouseConfiguration.HouseStyle houseStyle) {
        BuildClear clearedSpace = new BuildClear();
        clearedSpace.getShape().setDirection(EnumFacing.SOUTH);
        clearedSpace.getShape().setHeight(houseStyle.getHeight());
        clearedSpace.getShape().setLength(houseStyle.getLength());
        clearedSpace.getShape().setWidth(houseStyle.getWidth());
        clearedSpace.getStartingPosition().setSouthOffset(0);
        clearedSpace.getStartingPosition().setEastOffset(houseStyle.getEastOffSet());

        BlockPos cornerPos = originalPos.east(houseStyle.getEastOffSet());
        Structure.ScanStructure(
                world,
                originalPos,
                cornerPos,
                cornerPos.south(houseStyle.getLength()).west(houseStyle.getWidth()).up(houseStyle.getHeight()),
                "../src/main/resources/" + houseStyle.getStructureLocation(),
                clearedSpace,
                playerFacing, false, false);
    }

    @Override
    protected Boolean CustomBlockProcessingHandled(StructureConfiguration configuration, BuildBlock block, World world, BlockPos originalPos,
                                                   EnumFacing assumedNorth, Block foundBlock, IBlockState blockState, EntityPlayer player) {
        if (foundBlock instanceof BlockBed) {
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
     * This method is used after the main building is build for any additional structures or modifications.
     *
     * @param configuration The structure configuration.
     * @param world         The current world.
     * @param originalPos   The original position clicked on.
     * @param assumedNorth  The assumed northern direction.
     * @param player        The player which initiated the construction.
     */
    @Override
    public void AfterBuilding(StructureConfiguration configuration, World world, BlockPos originalPos, EnumFacing assumedNorth, EntityPlayer player) {
        VillagerHouseConfiguration houseConfig = (VillagerHouseConfiguration) configuration;

        if (this.bedPositions.size() > 0) {
            for (Tuple<BlockPos, BlockPos> bedPosition : this.bedPositions) {
                BuildingMethods.PlaceColoredBed(world, bedPosition.getFirst(), bedPosition.getSecond(), houseConfig.bedColor);
            }
        }
    }
}
