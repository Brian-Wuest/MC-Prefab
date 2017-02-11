package com.wuest.prefab.StructureGen.CustomStructures;

import com.wuest.prefab.Config.VillagerHouseConfiguration;
import com.wuest.prefab.StructureGen.BuildClear;
import com.wuest.prefab.StructureGen.Structure;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * This is the structure class used to generate simple villager houses.
 * @author WuestMan
 *
 */
public class StructureVillagerHouses extends Structure
{
	public static void ScanStructure(World world, BlockPos originalPos, EnumFacing playerFacing, VillagerHouseConfiguration.HouseStyle houseStyle)
	{
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
				playerFacing);	
	}
}
