package com.wuest.prefab.Structures.Predefined;

import com.wuest.prefab.Structures.Base.BuildClear;
import com.wuest.prefab.Structures.Base.Structure;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * 
 * @author WuestMan
 *
 */
public class StructureNetherGate extends Structure
{
	public static final String ASSETLOCATION = "assets/prefab/structures/nethergate.zip";

	public static void ScanStructure(World world, BlockPos originalPos, EnumFacing playerFacing)
	{
		BuildClear clearedSpace = new BuildClear();
		clearedSpace.getShape().setDirection(EnumFacing.SOUTH);
		clearedSpace.getShape().setHeight(13);
		clearedSpace.getShape().setLength(26);
		clearedSpace.getShape().setWidth(15);
		clearedSpace.getStartingPosition().setSouthOffset(1);
		clearedSpace.getStartingPosition().setEastOffset(7);
		clearedSpace.getStartingPosition().setHeightOffset(-2);
		
		Structure.ScanStructure(
				world, 
				originalPos, 
				originalPos.east(7).down(2).south(), 
				originalPos.south(26).west(7).up(11), 
				"..\\src\\main\\resources\\assets\\prefab\\structures\\nethergate.zip",
				clearedSpace,
				playerFacing, false, false);	
	}
}
