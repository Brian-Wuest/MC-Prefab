package com.wuest.prefab.Config;

import com.wuest.prefab.ModRegistry;
import com.wuest.prefab.StructureGen.CustomStructures.StructureChickenCoop;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
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
	public EnumFacing houseFacing;
	
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
		this.houseFacing = EnumFacing.NORTH;
	}
	
	/**
	 * Writes the properties to an NBTTagCompound.
	 * @return An NBTTagCompound with the updated properties.
	 */
	public NBTTagCompound WriteToNBTTagCompound()
	{
		NBTTagCompound tag = new NBTTagCompound();
		
		if (this.pos != null)
		{
			tag.setInteger(StructureConfiguration.hitXTag, this.pos.getX());
			tag.setInteger(StructureConfiguration.hitYTag, this.pos.getY());
			tag.setInteger(StructureConfiguration.hitZTag, this.pos.getZ());
		}
		
		tag.setString(StructureConfiguration.houseFacingTag, this.houseFacing.getName());
		
		tag = this.CustomWriteToNBTTagCompound(tag);
		
		return tag;
	}
	
	/**
	 * Reads NBTTagCompound to create a StructureConfiguration object from.
	 * @param messageTag The NBTTagCompound to read the properties from.
	 * @param config The existing StructureConfiguration instance to fill the properties in for.
	 * @return The updated StructureConfiguration instance.
	 */
	public StructureConfiguration ReadFromNBTTagCompound(NBTTagCompound messageTag, StructureConfiguration config)
	{
		if (messageTag != null)
		{
			if (messageTag.hasKey(StructureConfiguration.hitXTag))
			{
				config.pos = new BlockPos(
						messageTag.getInteger(StructureConfiguration.hitXTag), 
						messageTag.getInteger(StructureConfiguration.hitYTag),
						messageTag.getInteger(StructureConfiguration.hitZTag));
			}
			
			if (messageTag.hasKey(StructureConfiguration.houseFacingTag))
			{
				config.houseFacing = EnumFacing.byName(messageTag.getString(StructureConfiguration.houseFacingTag));
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
	public void BuildStructure(EntityPlayer player, World world)
	{
		// This is always on the server.
		BlockPos hitBlockPos = this.pos;
		BlockPos playerPosition = player.getPosition();

		IBlockState hitBlockState = world.getBlockState(hitBlockPos);

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
	protected void ConfigurationSpecificBuildStructure(EntityPlayer player, World world, BlockPos hitBlockPos)
	{
	}
	
	/**
	 * Custom method which can be overridden to write custom properties to the tag.
	 * @param tag The NBTTagCompound to write the custom properties too.
	 * @return The updated tag.
	 */
	protected NBTTagCompound CustomWriteToNBTTagCompound(NBTTagCompound tag)
	{
		return tag;
	}	
	
	/**
	 * Custom method to read the NBTTagCompound message.
	 * @param messageTag The message to create the configuration from.
	 * @param config The configuration to read the settings into.
	 */
	protected void CustomReadFromNBTTag(NBTTagCompound messageTag, StructureConfiguration config)
	{
	}
}
