package com.wuest.prefab.structures.predefined;

import com.wuest.prefab.structures.base.BuildClear;
import com.wuest.prefab.structures.base.Structure;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * @author WuestMan
 */
public class StructureTreeFarm extends Structure {
    public static final String ASSETLOCATION = "assets/prefab/structures/treefarm.zip";

    public static void ScanStructure(World world, BlockPos originalPos, Direction playerFacing) {
        BuildClear clearedSpace = new BuildClear();
        clearedSpace.getShape().setDirection(Direction.SOUTH);
        clearedSpace.getShape().setHeight(7);
        clearedSpace.getShape().setLength(38);
        clearedSpace.getShape().setWidth(38);
        clearedSpace.getStartingPosition().setSouthOffset(1);
        clearedSpace.getStartingPosition().setEastOffset(18);

        Structure.ScanStructure(
                world,
                originalPos,
                originalPos.east(18).south(),
                originalPos.south(38).west(19).above(7),
                "..\\src\\main\\resources\\assets\\prefab\\structures\\treefarm.zip",
                clearedSpace,
                playerFacing, false, false);
    }
}
