package com.wuest.prefab.StructureGen.CustomStructures;

import com.wuest.prefab.Config.StructureConfiguration;
import com.wuest.prefab.Config.WareHouseConfiguration;
import com.wuest.prefab.StructureGen.BuildBlock;
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