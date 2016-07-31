package com.wuest.prefab.StructureGen.CustomStructures;

import com.wuest.prefab.Config.StructureConfiguration;
import com.wuest.prefab.Config.WareHouseConfiguration;
import com.wuest.prefab.StructureGen.BuildBlock;
import com.wuest.prefab.StructureGen.BuildClear;
import com.wuest.prefab.StructureGen.Structure;

import net.minecraft.block.Block;
import net.minecraft.block.BlockStainedGlass;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class StructureWarehouse extends Structure
{
	public static final String ASSETLOCATION = "assets/prefab/structures/warehouse.zip"; 
	
	public static void ScanStructure(World world, BlockPos originalPos, EnumFacing playerFacing)
	{
		BuildClear clearedSpace = new BuildClear();
		clearedSpace.getShape().setDirection(EnumFacing.SOUTH);
		clearedSpace.getShape().setHeight(17);
		clearedSpace.getShape().setLength(17);
		clearedSpace.getShape().setWidth(17);
		clearedSpace.getStartingPosition().setEastOffset(7);
		clearedSpace.getStartingPosition().setHeightOffset(-7);
		clearedSpace.getStartingPosition().setSouthOffset(1);
		
		Structure.ScanStructure(
				world, 
				originalPos, 
				originalPos.east(7).south(1).down(5), 
				originalPos.west(8).south(16).up(10), 
				"C:\\Users\\Brian\\Documents\\GitHub\\MC-Prefab\\src\\main\\resources\\assets\\prefab\\structures\\warehouse.zip",
				clearedSpace,
				playerFacing);	
	}
	
	@Override
	protected Boolean CustomBlockProcessingHandled(StructureConfiguration configuration, BuildBlock block, World world, BlockPos originalPos, EnumFacing assumedNorth,
			Block foundBlock, IBlockState blockState)
	{
		if (foundBlock.getRegistryName().getResourceDomain().equals(Blocks.STAINED_GLASS.getRegistryName().getResourceDomain())
				&& foundBlock.getRegistryName().getResourcePath().equals(Blocks.STAINED_GLASS.getRegistryName().getResourcePath()))
		{
			WareHouseConfiguration wareHouseConfiguration = (WareHouseConfiguration)configuration;
			
			blockState = blockState.withProperty(BlockStainedGlass.COLOR, wareHouseConfiguration.dyeColor);
			block.setBlockState(blockState);
			this.placedBlocks.add(block);
			
			return true;
		}
		
		return false;
	}
}