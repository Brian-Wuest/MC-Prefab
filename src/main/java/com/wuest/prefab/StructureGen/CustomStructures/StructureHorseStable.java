package com.wuest.prefab.StructureGen.CustomStructures;

import com.wuest.prefab.StructureGen.BuildClear;
import com.wuest.prefab.StructureGen.Structure;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * 
 * @author WuestMan
 *
 */
public class StructureHorseStable extends Structure
{
	public static final String ASSETLOCATION = "assets/prefab/structures/horsestable.zip";

	public static void ScanStructure(World world, BlockPos originalPos, EnumFacing playerFacing)
	{
		BuildClear clearedSpace = new BuildClear();
		clearedSpace.getShape().setDirection(EnumFacing.SOUTH);
		clearedSpace.getShape().setHeight(11);
		clearedSpace.getShape().setLength(10);
		clearedSpace.getShape().setWidth(8);
		clearedSpace.getStartingPosition().setSouthOffset(1);
		clearedSpace.getStartingPosition().setEastOffset(4);
		clearedSpace.getStartingPosition().setHeightOffset(-4);
		
		Structure.ScanStructure(
				world, 
				originalPos, 
				originalPos.east(4).down(4), 
				originalPos.south(10).west(3).up(7), 
				"..\\src\\main\\resources\\assets\\prefab\\structures\\horsestable.zip",
				clearedSpace,
				playerFacing, false, false);	
	}
}
