package com.wuest.prefab.StructureGen.CustomStructures;

import com.wuest.prefab.Config.ChickenCoopConfiguration;
import com.wuest.prefab.StructureGen.BuildClear;
import com.wuest.prefab.StructureGen.Structure;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class StructureChickenCoop extends Structure
{
	public static final String ASSETLOCATION = "assets/prefab/structures/chickencoop.json";

	public static void ScanStructure(World world, BlockPos originalPos, EnumFacing playerFacing)
	{
		BuildClear clearedSpace = new BuildClear();
		clearedSpace.getShape().setDirection(EnumFacing.WEST);
		clearedSpace.getShape().setHeight(7);
		clearedSpace.getShape().setLength(5);
		clearedSpace.getShape().setWidth(12);
		clearedSpace.getStartingPosition().setSouthOffset(1);
		clearedSpace.getStartingPosition().setEastOffset(9);
		
		Structure.ScanStructure(
				world, 
				originalPos, 
				originalPos.south(9).west(1), 
				originalPos.west(5).north(2).up(7), 
				"C:\\Users\\Brian\\Documents\\GitHub\\MC-Prefab\\src\\main\\resources\\assets\\prefab\\structures\\chickencoop.json",
				clearedSpace,
				playerFacing);	
	}
	
}
