package com.wuest.prefab.structures.predefined;

import com.wuest.prefab.structures.base.BuildClear;
import com.wuest.prefab.structures.base.Structure;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * @author WuestMan
 */
public class StructureHorseStable extends Structure {
	public static final String ASSETLOCATION = "assets/prefab/structures/horsestable.zip";

	public static void ScanStructure(World world, BlockPos originalPos, Direction playerFacing) {
		BuildClear clearedSpace = new BuildClear();
		clearedSpace.getShape().setDirection(Direction.SOUTH);
		clearedSpace.getShape().setHeight(11);
		clearedSpace.getShape().setLength(10);
		clearedSpace.getShape().setWidth(8);
		clearedSpace.getStartingPosition().setSouthOffset(1);
		clearedSpace.getStartingPosition().setEastOffset(4);
		clearedSpace.getStartingPosition().setHeightOffset(-4);

		Structure.ScanStructure(
				world,
				originalPos,
				originalPos.east(4).below(4).south(),
				originalPos.south(10).west(3).above(7),
				"..\\src\\main\\resources\\assets\\prefab\\structures\\horsestable.zip",
				clearedSpace,
				playerFacing, false, false);
	}
}
