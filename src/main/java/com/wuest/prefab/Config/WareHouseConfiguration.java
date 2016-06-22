package com.wuest.prefab.Config;

import net.minecraft.item.EnumDyeColor;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

/**
 * This class is the main configuration holder.
 * @author WuesMan
 */
public class WareHouseConfiguration
{
	public static String houseFacingName = "Warehouse Facing";
	
	private static String hitXTag = "hitX";
	private static String hitYTag = "hitY";
	private static String hitZTag = "hitZ";
	private static String dyeColorTag = "dyeColor";
	private static String houseFacingTag = "wareHouseFacing";
	
	public EnumDyeColor dyeColor;
	public EnumFacing wareHouseFacing; 
	public BlockPos pos;
	
	public WareHouseConfiguration()
	{
		this.dyeColor = EnumDyeColor.byMetadata(0);
		this.wareHouseFacing = EnumFacing.NORTH;
	}
	
	public NBTTagCompound WriteToNBTTagCompound()
	{
		NBTTagCompound tag = new NBTTagCompound();
		
		tag.setInteger(WareHouseConfiguration.hitXTag, this.pos.getX());
		tag.setInteger(WareHouseConfiguration.hitYTag, this.pos.getY());
		tag.setInteger(WareHouseConfiguration.hitZTag, this.pos.getZ());
		tag.setInteger(WareHouseConfiguration.dyeColorTag, this.dyeColor.getMetadata());
		tag.setString(WareHouseConfiguration.houseFacingTag, this.wareHouseFacing.getName());
		
		return tag;
	}
	
	public static WareHouseConfiguration ReadFromNBTTagCompound(NBTTagCompound messageTag)
	{
		WareHouseConfiguration config = null;
		
		if (messageTag != null)
		{
			config = new WareHouseConfiguration();
			
			if (messageTag.hasKey(WareHouseConfiguration.hitXTag))
			{
				config.pos = new BlockPos(
						messageTag.getInteger(WareHouseConfiguration.hitXTag), 
						messageTag.getInteger(WareHouseConfiguration.hitYTag),
						messageTag.getInteger(WareHouseConfiguration.hitZTag));
			}
			
			if (messageTag.hasKey(WareHouseConfiguration.dyeColorTag))
			{
				config.dyeColor = EnumDyeColor.byMetadata(messageTag.getInteger(WareHouseConfiguration.dyeColorTag));
			}
			
			if (messageTag.hasKey(WareHouseConfiguration.houseFacingTag))
			{
				config.wareHouseFacing = EnumFacing.byName(messageTag.getString(WareHouseConfiguration.houseFacingTag));
			}
		}
		
		return config;
	}
}
