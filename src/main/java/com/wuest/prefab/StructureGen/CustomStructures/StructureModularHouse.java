package com.wuest.prefab.StructureGen.CustomStructures;

import com.wuest.prefab.StructureGen.BuildClear;
import com.wuest.prefab.StructureGen.Structure;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * The structure class for the modular house.
 * @author WuestMan
 *
 */
public class StructureModularHouse extends Structure
{
	public static final String ASSETLOCATION = "assets/prefab/structures/modularHouse.zip";

	public static void ScanStructure(World world, BlockPos originalPos, EnumFacing playerFacing)
	{
		BuildClear clearedSpace = new BuildClear();
		clearedSpace.getShape().setDirection(EnumFacing.SOUTH);
		clearedSpace.getShape().setHeight(7);
		clearedSpace.getShape().setLength(5);
		clearedSpace.getShape().setWidth(12);
		clearedSpace.getStartingPosition().setSouthOffset(1);
		clearedSpace.getStartingPosition().setEastOffset(9);
		
		Structure.ScanStructure(
				world, 
				originalPos, 
				originalPos.east(10), 
				originalPos.south(6).west(3).up(7), 
				"C:\\Users\\Brian\\Documents\\GitHub\\MC-Prefab\\src\\main\\resources\\assets\\prefab\\structures\\modularHouse.zip",
				clearedSpace,
				playerFacing, false, false);	
	}
}
