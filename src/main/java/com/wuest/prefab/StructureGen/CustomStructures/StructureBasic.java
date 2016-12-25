package com.wuest.prefab.StructureGen.CustomStructures;

import com.wuest.prefab.Config.BasicStructureConfiguration;
import com.wuest.prefab.StructureGen.BuildClear;
import com.wuest.prefab.StructureGen.BuildShape;
import com.wuest.prefab.StructureGen.PositionOffset;
import com.wuest.prefab.StructureGen.Structure;

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
	public static void ScanStructure(World world, BlockPos originalPos, EnumFacing playerFacing, BasicStructureConfiguration configuration)
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
			BuildShape buildShape = configuration.basicStructureName.getClearShape();
			PositionOffset offset = configuration.basicStructureName.getClearPositionOffset();
			
			int downOffset = offset.getHeightOffset() < 0 ? offset.getHeightOffset() : 0;
			
			Structure.ScanStructure(
					world, 
					originalPos, 
					originalPos.east(offset.getEastOffset()).south(offset.getSouthOffset()).down(downOffset), 
					originalPos.south(buildShape.getLength()).west(buildShape.getWidth()).up(buildShape.getHeight()), 
					"C:\\Users\\Brian\\Documents\\GitHub\\MC-Prefab\\src\\main\\resources\\assets\\prefab\\structures\\" + configuration.basicStructureName.getName()  + ".zip",
					clearedSpace,
					playerFacing);
		}
	}
}
