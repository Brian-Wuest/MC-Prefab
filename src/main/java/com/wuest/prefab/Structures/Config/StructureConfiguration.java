package com.wuest.prefab.Structures.Config;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * This is the base configuration class used by all structures.
 * @author WuestMan
 *
 */
public class StructureConfiguration
{
	public static String houseFacingName = "House Facing";
	
	private static String hitXTag = "hitX";
	private static String hitYTag = "hitY";
	private static String hitZTag = "hitZ";
	private static String houseFacingTag = "wareHouseFacing";
	
	/**
	 * The structure facing property.
	 */
	public Direction houseFacing;
	
	/**
	 * The position of the structure.
	 */
	public BlockPos pos;
	
	/**
	 * Initializes a new instance of the StructureConfiguration class.
	 */
	public StructureConfiguration()
	{
		this.Initialize();
	}
	
	/**
	 * Initializes any properties for this class.
	 */
	public void Initialize()
	{
		this.houseFacing = Direction.NORTH;
	}
	
	/**
	 * Writes the properties to an CompoundNBT.
	 * @return An CompoundNBT with the updated properties.
	 */
	public CompoundNBT WriteToCompoundNBT()
	{
		CompoundNBT tag = new CompoundNBT();
		
		if (this.pos != null)
		{
			tag.putInt(StructureConfiguration.hitXTag, this.pos.getX());
			tag.putInt(StructureConfiguration.hitYTag, this.pos.getY());
			tag.putInt(StructureConfiguration.hitZTag, this.pos.getZ());
		}
		
		tag.putString(StructureConfiguration.houseFacingTag, this.houseFacing.getName());
		
		tag = this.CustomWriteToCompoundNBT(tag);
		
		return tag;
	}
	
	/**
	 * Reads CompoundNBT to create a StructureConfiguration object from.
	 * @param messageTag The CompoundNBT to read the properties from.
	 * @return The updated StructureConfiguration instance.
	 */
	public StructureConfiguration ReadFromCompoundNBT(CompoundNBT messageTag)
	{
		return null;
	}
	
	/**
	 * Reads CompoundNBT to create a StructureConfiguration object from.
	 * @param messageTag The CompoundNBT to read the properties from.
	 * @param config The existing StructureConfiguration instance to fill the properties in for.
	 * @return The updated StructureConfiguration instance.
	 */
	public StructureConfiguration ReadFromCompoundNBT(CompoundNBT messageTag, StructureConfiguration config)
	{
		if (messageTag != null)
		{
			if (messageTag.contains(StructureConfiguration.hitXTag))
			{
				config.pos = new BlockPos(
						messageTag.getInt(StructureConfiguration.hitXTag), 
						messageTag.getInt(StructureConfiguration.hitYTag),
						messageTag.getInt(StructureConfiguration.hitZTag));
			}
			
			if (messageTag.contains(StructureConfiguration.houseFacingTag))
			{
				config.houseFacing = Direction.byName(messageTag.getString(StructureConfiguration.houseFacingTag));
			}
			
			this.CustomReadFromNBTTag(messageTag, config);
		}
		
		return config;
	}
	
	/**
	 * Generic method to start the building of the structure.
	 * @param player The player which requested the build.
	 * @param world The world instance where the build will occur.
	 */
	public void BuildStructure(PlayerEntity player, World world)
	{
		// This is always on the server.
		BlockPos hitBlockPos = this.pos;
		BlockPos playerPosition = player.getPosition();

		BlockState hitBlockState = world.getBlockState(hitBlockPos);

		if (hitBlockState != null)
		{
			Block hitBlock = hitBlockState.getBlock();
 
			if (hitBlock != null)
			{
				this.ConfigurationSpecificBuildStructure(player, world, hitBlockPos);
			}
		}
	}
	
	/**
	 * This is used to actually build the structure as it creates the structure instance and calls build structure.
	 * @param player The player which requested the build.
	 * @param world The world instance where the build will occur.
	 * @param hitBlockPos This hit block position.
	 */
	protected void ConfigurationSpecificBuildStructure(PlayerEntity player, World world, BlockPos hitBlockPos)
	{
	}
	
	/**
	 * Custom method which can be overridden to write custom properties to the tag.
	 * @param tag The CompoundNBT to write the custom properties too.
	 * @return The updated tag.
	 */
	protected CompoundNBT CustomWriteToCompoundNBT(CompoundNBT tag)
	{
		return tag;
	}	
	
	/**
	 * Custom method to read the CompoundNBT message.
	 * @param messageTag The message to create the configuration from.
	 * @param config The configuration to read the settings into.
	 */
	protected void CustomReadFromNBTTag(CompoundNBT messageTag, StructureConfiguration config)
	{
	}
}
