package com.wuest.prefab.StructureGen.CustomStructures;

import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wuest.prefab.BuildingMethods;
import com.wuest.prefab.ZipUtil;
import com.wuest.prefab.Config.Structures.InstantBridgeConfiguration;
import com.wuest.prefab.Config.Structures.StructureConfiguration;
import com.wuest.prefab.StructureGen.BuildBlock;
import com.wuest.prefab.StructureGen.BuildClear;
import com.wuest.prefab.StructureGen.Structure;

import net.minecraft.block.BlockTorch;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * 
 * @author WuestMan
 *
 */
public class StructureInstantBridge extends Structure
{
	/*
	 * Initializes a new instance of the StructureInstantBridge class.
	 */
	public StructureInstantBridge()
	{
		super();
	}

	/**
	 * Creates an instance of the structure after reading from a resource location and converting it from JSON.
	 * @return A new instance of this class.
	 */
	public static StructureInstantBridge CreateInstance()
	{
		StructureInstantBridge structure = new StructureInstantBridge();
		return structure;
	}
	
	/**
	 * This is the main building method for this structure.
	 * 
	 * @param configuration The configuration the user updated.
	 * @param world The current world.
	 * @param originalPos The block the user clicked on.
	 * @param assumedNorth This should always be "NORTH" when the file is based on a scan.
	 * @param player The player requesting the structure.
	 * @return True if the build can occur, otherwise false.
	 */
	@Override
	public boolean BuildStructure(StructureConfiguration configuration, World world, BlockPos originalPos, EnumFacing assumedNorth, EntityPlayer player)
	{
		InstantBridgeConfiguration specificConfig = (InstantBridgeConfiguration)configuration;
		this.setupClearSpace(specificConfig);
		
		this.setupStructure(specificConfig, originalPos);
		
		return super.BuildStructure(specificConfig, world, originalPos, assumedNorth, player);
	}
	
	public void setupStructure(InstantBridgeConfiguration configuration, BlockPos originalPos)
	{
		ArrayList<BuildBlock> buildingBlocks = new ArrayList<BuildBlock>();
		IBlockState materialState = configuration.bridgeMaterial.getBlockType();
		EnumFacing facing = EnumFacing.SOUTH;
		
		IBlockState torchState = Blocks.TORCH.getDefaultState();
		IBlockState glassState = Blocks.GLASS_PANE.getDefaultState();
		
		for (int i = 1; i <= configuration.bridgeLength; i++)
		{
			BlockPos currentPos = originalPos.offset(facing, i);			
			
			// Place the floor
			buildingBlocks.add(Structure.createBuildBlockFromBlockState(materialState, materialState.getBlock(), currentPos.offset(facing.rotateYCCW(), 2), originalPos));
			buildingBlocks.add(Structure.createBuildBlockFromBlockState(materialState, materialState.getBlock(), currentPos.offset(facing.rotateYCCW()), originalPos));
			buildingBlocks.add(Structure.createBuildBlockFromBlockState(materialState, materialState.getBlock(), currentPos, originalPos));
			buildingBlocks.add(Structure.createBuildBlockFromBlockState(materialState, materialState.getBlock(), currentPos.offset(facing.rotateY()), originalPos));
			buildingBlocks.add(Structure.createBuildBlockFromBlockState(materialState, materialState.getBlock(), currentPos.offset(facing.rotateY(), 2), originalPos));

			// Build the walls.
			buildingBlocks.add(Structure.createBuildBlockFromBlockState(materialState, materialState.getBlock(), currentPos.offset(facing.rotateYCCW(), 2).up(), originalPos));
			buildingBlocks.add(Structure.createBuildBlockFromBlockState(materialState, materialState.getBlock(), currentPos.offset(facing.rotateY(), 2).up(), originalPos));
			
			if (configuration.includeRoof)
			{
				// Build the roof.
				buildingBlocks.add(Structure.createBuildBlockFromBlockState(materialState, materialState.getBlock(), currentPos.offset(facing.rotateYCCW(), 2).up(3), originalPos));
				buildingBlocks.add(Structure.createBuildBlockFromBlockState(materialState, materialState.getBlock(), currentPos.offset(facing.rotateYCCW()).up(4), originalPos));
				buildingBlocks.add(Structure.createBuildBlockFromBlockState(materialState, materialState.getBlock(), currentPos.up(4), originalPos));
				buildingBlocks.add(Structure.createBuildBlockFromBlockState(materialState, materialState.getBlock(), currentPos.offset(facing.rotateY()).up(4), originalPos));
				buildingBlocks.add(Structure.createBuildBlockFromBlockState(materialState, materialState.getBlock(), currentPos.offset(facing.rotateY(), 2).up(3), originalPos));
			}
			
			if (i % 6 == 0)
			{
				// Place torches.
				buildingBlocks.add(Structure.createBuildBlockFromBlockState(torchState, torchState.getBlock(), currentPos.offset(facing.rotateYCCW(), 2).up(2), originalPos));
				buildingBlocks.add(Structure.createBuildBlockFromBlockState(torchState, torchState.getBlock(), currentPos.offset(facing.rotateY(), 2).up(2), originalPos));
			}
			else
			{
				// Place Glass panes
				buildingBlocks.add(Structure.createBuildBlockFromBlockState(glassState, glassState.getBlock(), currentPos.offset(facing.rotateYCCW(), 2).up(2), originalPos));
				buildingBlocks.add(Structure.createBuildBlockFromBlockState(glassState, glassState.getBlock(), currentPos.offset(facing.rotateY(), 2).up(2), originalPos));
			}
		}
		
		this.setBlocks(buildingBlocks);
	}
	
	private void setupClearSpace(InstantBridgeConfiguration configuration)
	{
		BuildClear clear = new BuildClear();
		clear.getStartingPosition().setSouthOffset(1);
		clear.getStartingPosition().setEastOffset(2);
		clear.getShape().setDirection(EnumFacing.SOUTH);
		clear.getShape().setHeight(5);
		clear.getShape().setWidth(5);
		clear.getShape().setLength(configuration.bridgeLength - 1);
		
		this.setClearSpace(clear);
	}
}