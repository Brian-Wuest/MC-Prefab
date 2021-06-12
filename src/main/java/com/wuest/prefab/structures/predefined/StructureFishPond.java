package com.wuest.prefab.structures.predefined;

import com.wuest.prefab.structures.base.BuildClear;
import com.wuest.prefab.structures.base.Structure;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * @author WuestMan
 */
public class StructureFishPond extends Structure {
    public static final String ASSETLOCATION = "assets/prefab/structures/fishpond.zip";

    public static void ScanStructure(World world, BlockPos originalPos, Direction playerFacing) {
        BuildClear clearedSpace = new BuildClear();
        clearedSpace.getShape().setDirection(Direction.SOUTH);
        clearedSpace.getShape().setHeight(15);
        clearedSpace.getShape().setLength(32);
        clearedSpace.getShape().setWidth(32);
        clearedSpace.getStartingPosition().setSouthOffset(1);
        clearedSpace.getStartingPosition().setEastOffset(16);
        clearedSpace.getStartingPosition().setHeightOffset(-3);

        Structure.ScanStructure(
                world,
                originalPos,
                originalPos.east(16).south().below(3),
                originalPos.south(32).west(15).above(12),
                "..\\src\\main\\resources\\assets\\prefab\\structures\\fishpond.zip",
                clearedSpace,
                playerFacing, false, false);
    }
}
