package com.wuest.prefab.Structures.Predefined;

import com.wuest.prefab.Structures.Base.BuildClear;
import com.wuest.prefab.Structures.Base.Structure;
import com.wuest.prefab.Structures.Config.VillagerHouseConfiguration;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * This is the structure class used to generate simple villager houses.
 *
 * @author WuestMan
 */
public class StructureVillagerHouses extends Structure {
    public static void ScanStructure(World world, BlockPos originalPos, Direction playerFacing, VillagerHouseConfiguration.HouseStyle houseStyle) {
        BuildClear clearedSpace = new BuildClear();
        clearedSpace.getShape().setDirection(Direction.SOUTH);
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
}
