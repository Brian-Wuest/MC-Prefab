package com.wuest.prefab.StructureGen.CustomStructures;

import com.wuest.prefab.Config.Structures.ModerateHouseConfiguration;
import com.wuest.prefab.StructureGen.BuildClear;
import com.wuest.prefab.StructureGen.BuildShape;
import com.wuest.prefab.StructureGen.PositionOffset;
import com.wuest.prefab.StructureGen.Structure;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * 
 * @author WuestMan
 *
 */
public class StructureModerateHouse extends Structure
{
	public static void ScanStructure(World world, BlockPos originalPos, EnumFacing playerFacing, ModerateHouseConfiguration.HouseStyle houseStyle)
	{
		BuildClear clearedSpace = new BuildClear();
		clearedSpace.getShape().setDirection(EnumFacing.SOUTH);
		clearedSpace.getShape().setHeight(houseStyle.getHeight());
		clearedSpace.getShape().setLength(houseStyle.getLength() + 1);
		clearedSpace.getShape().setWidth(houseStyle.getWidth() + 1);
		clearedSpace.getStartingPosition().setSouthOffset(1);
		clearedSpace.getStartingPosition().setEastOffset(houseStyle.getEastOffSet());
		
		BlockPos cornerPos = originalPos.east(houseStyle.getEastOffSet()).south();
		
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