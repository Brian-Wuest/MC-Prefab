package com.wuest.prefab.StructureGen.CustomStructures;

import com.wuest.prefab.Config.BasicStructureConfiguration;
import com.wuest.prefab.Config.StructureConfiguration;
import com.wuest.prefab.Config.BasicStructureConfiguration.EnumBasicStructureName;
import com.wuest.prefab.StructureGen.BuildBlock;
import com.wuest.prefab.StructureGen.BuildClear;
import com.wuest.prefab.StructureGen.Structure;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.BlockHalfWoodSlab;
import net.minecraft.block.BlockHay;
import net.minecraft.block.BlockHopper;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.BlockSlab.EnumBlockHalf;
import net.minecraft.block.BlockWoodSlab;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class StructureChickenCoop extends Structure
{
	public static final String ASSETLOCATION = "assets/prefab/structures/chickencoop.zip";
	BlockPos chickenCoopBlockPos = null;
	
	
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
				"C:\\Users\\Brian\\Documents\\GitHub\\MC-Prefab\\src\\main\\resources\\assets\\prefab\\structures\\chickencoop.zip",
				clearedSpace,
				playerFacing, false, false);	
	}
	
	@Override
	protected Boolean CustomBlockProcessingHandled(StructureConfiguration configuration, BuildBlock block, World world, BlockPos originalPos,
			EnumFacing assumedNorth, Block foundBlock, IBlockState blockState, EntityPlayer player)
	{
		if (foundBlock instanceof BlockFenceGate && this.chickenCoopBlockPos == null)
		{ 
			chickenCoopBlockPos = block.getStartingPosition().getRelativePosition(originalPos, configuration.houseFacing).offset(configuration.houseFacing.getOpposite(), 2);
		}
		
		return false;
	}
	
	/**
	 * This method is used after the main building is build for any additional
	 * structures or modifications.
	 * 
	 * @param configuration The structure configuration.
	 * @param world The current world.
	 * @param originalPos The original position clicked on.
	 * @param assumedNorth The assumed northern direction.
	 * @param player The player which initiated the construction.
	 */
	@Override
	public void AfterBuilding(StructureConfiguration configuration, World world, BlockPos originalPos, EnumFacing assumedNorth, EntityPlayer player)
	{
		if (this.chickenCoopBlockPos != null)
		{
			// For the chicken coop, spawn 1 chicken above a hay bale.
			for (int i = 0; i < 1; i++)
			{
				EntityChicken entity = new EntityChicken(world);
				entity.setPosition(this.chickenCoopBlockPos.getX(), this.chickenCoopBlockPos.getY(), this.chickenCoopBlockPos.getZ());
				world.spawnEntity(entity);
			}
			
			this.chickenCoopBlockPos = null;
		}
	}
}