package com.wuest.prefab.StructureGen.CustomStructures;

import com.wuest.prefab.Config.ProduceFarmConfiguration;
import com.wuest.prefab.Config.StructureConfiguration;
import com.wuest.prefab.StructureGen.BuildBlock;
import com.wuest.prefab.StructureGen.BuildClear;
import com.wuest.prefab.StructureGen.Structure;

import net.minecraft.block.Block;
import net.minecraft.block.BlockStainedGlass;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class StructureProduceFarm extends Structure 
{
	public static final String ASSETLOCATION = "assets/prefab/structures/producefarm.zip";

	public static void ScanStructure(World world, BlockPos originalPos, EnumFacing playerFacing)
	{
		BuildClear clearedSpace = new BuildClear();
		clearedSpace.getShape().setDirection(EnumFacing.SOUTH);
		clearedSpace.getShape().setHeight(9);
		clearedSpace.getShape().setLength(32);
		clearedSpace.getShape().setWidth(32);
		clearedSpace.getStartingPosition().setSouthOffset(1);
		clearedSpace.getStartingPosition().setEastOffset(28);
		clearedSpace.getStartingPosition().setHeightOffset(-1);
		
		Structure.ScanStructure(
				world, 
				originalPos, 
				originalPos.east(28).south().down(1), 
				originalPos.south(32).west(3).up(9), 
				"C:\\Users\\Brian\\Documents\\GitHub\\MC-Prefab\\src\\main\\resources\\assets\\prefab\\structures\\producefarm.zip",
				clearedSpace,
				playerFacing, false, false);	
	}
	
	@Override
	protected Boolean CustomBlockProcessingHandled(StructureConfiguration configuration, BuildBlock block, World world, BlockPos originalPos, EnumFacing assumedNorth,
			Block foundBlock, IBlockState blockState, EntityPlayer player)
	{
		if (foundBlock.getRegistryName().getResourceDomain().equals(Blocks.STAINED_GLASS.getRegistryName().getResourceDomain())
				&& foundBlock.getRegistryName().getResourcePath().equals(Blocks.STAINED_GLASS.getRegistryName().getResourcePath()))
		{
			ProduceFarmConfiguration wareHouseConfiguration = (ProduceFarmConfiguration)configuration;
			
			blockState = blockState.withProperty(BlockStainedGlass.COLOR, wareHouseConfiguration.dyeColor);
			block.setBlockState(blockState);
			//this.placedBlocks.add(block);
			this.priorityOneBlocks.add(block);
			
			return true;
		}
		
		return false;
	}
}
