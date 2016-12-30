package com.wuest.prefab.Config;

import net.minecraft.item.EnumDyeColor;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public class StructureConfiguration
{
	public static String houseFacingName = "House Facing";
	
	private static String hitXTag = "hitX";
	private static String hitYTag = "hitY";
	private static String hitZTag = "hitZ";
	private static String houseFacingTag = "wareHouseFacing";
	
	public EnumFacing houseFacing; 
	public BlockPos pos;
	
	public StructureConfiguration()
	{
		this.Initialize();
	}
	
	public void Initialize()
	{
		this.houseFacing = EnumFacing.NORTH;
	}
	
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
	
	protected NBTTagCompound CustomWriteToNBTTagCompound(NBTTagCompound tag)
	{
		return tag;
	}	
	
	protected void CustomReadFromNBTTag(NBTTagCompound messageTag, StructureConfiguration config)
	{
	}
}
