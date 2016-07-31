package com.wuest.prefab.StructureGen.CustomStructures;

import com.wuest.prefab.Config.StructureConfiguration;
import com.wuest.prefab.StructureGen.BuildBlock;
import com.wuest.prefab.StructureGen.BuildClear;
import com.wuest.prefab.StructureGen.Structure;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class StructureAlternateStart extends Structure
{
	public static final String RANCH_HOUSE = "assets/prefab/structures/ranch_house.zip";
	public static final String LOFT_HOUSE = "assets/prefab/structures/loft_house.zip";
	
	public static void ScanRanchStructure(World world, BlockPos originalPos, EnumFacing playerFacing)
	{
		BuildClear clearedSpace = new BuildClear();
		clearedSpace.getShape().setDirection(EnumFacing.SOUTH);
		clearedSpace.getShape().setHeight(7);
		clearedSpace.getShape().setLength(21);
		clearedSpace.getShape().setWidth(11);
		clearedSpace.getStartingPosition().setSouthOffset(1);
		clearedSpace.getStartingPosition().setEastOffset(8);
		clearedSpace.getStartingPosition().setHeightOffset(-1);
		
		Structure.ScanStructure(
				world, 
				originalPos, 
				originalPos.east(8).down(), 
				originalPos.south(22).west(3).up(8), 
				"C:\\Users\\Brian\\Documents\\GitHub\\MC-Prefab\\src\\main\\resources\\assets\\prefab\\structures\\ranch_house.zip",
				clearedSpace,
				playerFacing);	
	}
	
	public static void ScanLoftStructure(World world, BlockPos originalPos, EnumFacing playerFacing)
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
				"C:\\Users\\Brian\\Documents\\GitHub\\MC-Prefab\\src\\main\\resources\\assets\\prefab\\structures\\loft_house.zip",
				clearedSpace,
				playerFacing);	
	}
	
	@Override
	protected Boolean CustomBlockProcessingHandled(StructureConfiguration configuration, BuildBlock block, World world, BlockPos originalPos, EnumFacing assumedNorth,
			Block foundBlock, IBlockState blockState)
	{
		return false;
	}
	
	/**
	 * This method is used after the main building is build for any additional structures or modifications.
	 * @param configuration The structure configuration.
	 * @param world The current world.
	 * @param originalPos The original position clicked on.
	 * @param assumedNorth The assumed northern direction.
	 */
	@Override
	protected void AfterBuilding(StructureConfiguration configuration, World world, BlockPos originalPos, EnumFacing assumedNorth)
	{
		 
	}
}
