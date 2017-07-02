package com.wuest.prefab.StructureGen.CustomStructures;

import com.wuest.prefab.Config.BasicStructureConfiguration;
import com.wuest.prefab.Config.StructureConfiguration;
import com.wuest.prefab.Config.BasicStructureConfiguration.EnumBasicStructureName;
import com.wuest.prefab.StructureGen.*;
import net.minecraft.block.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * This is the basic structure to be used for structures which don't need a lot of configuration or a custom player created structures.
 * @author WuestMan
 *
 */
public class StructureBasic extends Structure
{
	BlockPos customBlockPos = null;
			
	public static void ScanStructure(World world, BlockPos originalPos, EnumFacing playerFacing, BasicStructureConfiguration configuration, boolean includeAir, boolean excludeWater)
	{
		BuildClear clearedSpace = new BuildClear();
		clearedSpace.setShape(configuration.basicStructureName.getClearShape());
		clearedSpace.setStartingPosition(configuration.basicStructureName.getClearPositionOffset());
		clearedSpace.getShape().setDirection(EnumFacing.SOUTH);
		
		if (configuration.IsCustomStructure())
		{
			// TODO: This needs to be programmed when custom structures are allowed.
		}
		else
		{
			BuildShape buildShape = configuration.basicStructureName.getClearShape().Clone();
			PositionOffset offset = configuration.basicStructureName.getClearPositionOffset();
			
			clearedSpace.getShape().setWidth(clearedSpace.getShape().getWidth() + 1);
			clearedSpace.getShape().setLength(clearedSpace.getShape().getLength() + 1);
			
			int downOffset = offset.getHeightOffset() < 0 ? Math.abs(offset.getHeightOffset()) : 0;
			BlockPos cornerPos = originalPos.east(offset.getEastOffset()).south(offset.getSouthOffset()).down(downOffset);
			
			Structure.ScanStructure(
					world, 
					originalPos, 
					cornerPos,
					cornerPos.south(buildShape.getLength()).west(buildShape.getWidth()).up(buildShape.getHeight()), 
					"..\\src\\main\\resources\\assets\\prefab\\structures\\" + configuration.basicStructureName.getName()  + ".zip",
					clearedSpace,
					playerFacing,
					includeAir,
					excludeWater);
		}
	}
	
	@Override
	protected Boolean CustomBlockProcessingHandled(StructureConfiguration configuration, BuildBlock block, World world, BlockPos originalPos,
			EnumFacing assumedNorth, Block foundBlock, IBlockState blockState, EntityPlayer player)
	{
		BasicStructureConfiguration config = (BasicStructureConfiguration)configuration;
		
		if (foundBlock instanceof BlockHopper && config.basicStructureName.getName().equals(EnumBasicStructureName.AdvancedCoop.getName()))
		{
			customBlockPos = block.getStartingPosition().getRelativePosition(originalPos, configuration.houseFacing);
		}
		else if (foundBlock instanceof BlockTrapDoor && config.basicStructureName.getName().equals(EnumBasicStructureName.MineshaftEntrance.getName()))
		{
			customBlockPos = block.getStartingPosition().getRelativePosition(originalPos, configuration.houseFacing);
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
		BasicStructureConfiguration config = (BasicStructureConfiguration)configuration;
		
		if (this.customBlockPos != null)
		{
			if (config.basicStructureName.getName().equals(EnumBasicStructureName.AdvancedCoop.getName()))
			{
				// For the advanced chicken coop, spawn 4 chickens above the hopper.
				for (int i = 0; i < 4; i++)
				{
					EntityChicken entity = new EntityChicken(world);
					entity.setPosition(this.customBlockPos.getX(), this.customBlockPos.up().getY(), this.customBlockPos.getZ());
					world.spawnEntity(entity);
				}
			}
			else if (config.basicStructureName.getName().equals(EnumBasicStructureName.MineshaftEntrance.getName()))
			{
				// Build the mineshaft where the trap door exists.
				StructureAlternateStart.PlaceMineShaft(world, this.customBlockPos.down(), configuration.houseFacing, true);
			}
			
			this.customBlockPos = null;
		}
		
		if (config.basicStructureName.getName().equals(EnumBasicStructureName.AquaBase.getName()))
		{
			// Replace the entrance area with air blocks.
			BlockPos airPos = originalPos.up(4).offset(configuration.houseFacing.getOpposite(), 1);

			// This is the first wall.
			world.setBlockToAir(airPos.offset(configuration.houseFacing.rotateY()));
			world.setBlockToAir(airPos);
			world.setBlockToAir(airPos.offset(configuration.houseFacing.rotateYCCW()));
			
			airPos = airPos.down();
			world.setBlockToAir(airPos.offset(configuration.houseFacing.rotateY()));
			world.setBlockToAir(airPos);
			world.setBlockToAir(airPos.offset(configuration.houseFacing.rotateYCCW()));
			
			airPos = airPos.down();
			world.setBlockToAir(airPos.offset(configuration.houseFacing.rotateY()));
			world.setBlockToAir(airPos);
			world.setBlockToAir(airPos.offset(configuration.houseFacing.rotateYCCW()));
			
			airPos = airPos.down();
			world.setBlockToAir(airPos.offset(configuration.houseFacing.rotateY()));
			world.setBlockToAir(airPos);
			world.setBlockToAir(airPos.offset(configuration.houseFacing.rotateYCCW()));
			
			// Second part of the wall.
			airPos = airPos.offset(configuration.houseFacing.getOpposite()).up();
			world.setBlockToAir(airPos.offset(configuration.houseFacing.rotateY()));
			world.setBlockToAir(airPos);
			world.setBlockToAir(airPos.offset(configuration.houseFacing.rotateYCCW()));
			
			airPos = airPos.up();
			world.setBlockToAir(airPos.offset(configuration.houseFacing.rotateY()));
			world.setBlockToAir(airPos);
			world.setBlockToAir(airPos.offset(configuration.houseFacing.rotateYCCW()));
			
			airPos = airPos.up();
			world.setBlockToAir(airPos);
		}
	}
}
