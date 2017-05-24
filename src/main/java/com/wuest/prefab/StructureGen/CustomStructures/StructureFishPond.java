package com.wuest.prefab.StructureGen.CustomStructures;

import com.wuest.prefab.Config.HouseConfiguration;
import com.wuest.prefab.Config.StructureConfiguration;
import com.wuest.prefab.StructureGen.BuildBlock;
import com.wuest.prefab.StructureGen.BuildClear;
import com.wuest.prefab.StructureGen.Structure;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockFurnace;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockStainedGlass;
import net.minecraft.block.BlockStandingSign;
import net.minecraft.block.BlockTorch;
import net.minecraft.block.BlockTrapDoor;
import net.minecraft.block.BlockWorkbench;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;

/**
 * 
 * @author WuestMan
 *
 */
public class StructureFishPond extends Structure
{
	public static final String ASSETLOCATION = "assets/prefab/structures/fishpond.zip";

	public static void ScanStructure(World world, BlockPos originalPos, EnumFacing playerFacing)
	{
		BuildClear clearedSpace = new BuildClear();
		clearedSpace.getShape().setDirection(EnumFacing.SOUTH);
		clearedSpace.getShape().setHeight(15);
		clearedSpace.getShape().setLength(32);
		clearedSpace.getShape().setWidth(32);
		clearedSpace.getStartingPosition().setSouthOffset(1);
		clearedSpace.getStartingPosition().setEastOffset(16);
		clearedSpace.getStartingPosition().setHeightOffset(-3);
		
		Structure.ScanStructure(
				world, 
				originalPos, 
				originalPos.east(16).south().down(3), 
				originalPos.south(32).west(15).up(12),
				"..\\src\\main\\resources\\assets\\prefab\\structures\\fishpond.zip",
				clearedSpace,
				playerFacing, false, false);	
	}
}
