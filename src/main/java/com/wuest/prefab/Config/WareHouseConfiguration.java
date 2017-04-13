package com.wuest.prefab.Config;

import net.minecraft.item.EnumDyeColor;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

/**
 * This class is the main configuration holder.
 * @author WuesMan
 */
public class WareHouseConfiguration extends StructureConfiguration
{
	private static String dyeColorTag = "dyeColor";
	private static String advancedTag = "advanced";
	
	/**
	 * Determines the glass color.
	 */
	public EnumDyeColor dyeColor;
	
	/**
	 * Determines if the advanced warehouse is generated instead.
	 */
	public boolean advanced;
	
	/**
	 * Initializes a new instance of the WareHouseConfiguration class.
	 */
	public WareHouseConfiguration()
	{
		super();
	}
		
	@Override
	public void Initialize()
	{
		super.Initialize();
		this.houseFacing = EnumFacing.SOUTH;
		this.dyeColor = EnumDyeColor.CYAN;
		this.advanced = false;
	}
	
	@Override
	protected void CustomReadFromNBTTag(NBTTagCompound messageTag, StructureConfiguration config)
	{
		if (messageTag.hasKey(WareHouseConfiguration.dyeColorTag))
		{
			((WareHouseConfiguration)config).dyeColor = EnumDyeColor.byMetadata(messageTag.getInteger(WareHouseConfiguration.dyeColorTag));
		}
		
		if (messageTag.hasKey(WareHouseConfiguration.advancedTag))
		{
			((WareHouseConfiguration)config).advanced = messageTag.getBoolean(WareHouseConfiguration.advancedTag);
		}
	}
	
	/**
	 * Custom method to read the NBTTagCompound message.
	 * @param messageTag The message to create the configuration from.
	 * @return An new configuration object with the values derived from the NBTTagCompound.
	 */
	public WareHouseConfiguration ReadFromNBTTagCompound(NBTTagCompound messageTag)
	{
		WareHouseConfiguration config = new WareHouseConfiguration();
		
		return (WareHouseConfiguration)super.ReadFromNBTTagCompound(messageTag, config);
	}
	
	@Override
	protected NBTTagCompound CustomWriteToNBTTagCompound(NBTTagCompound tag)
	{
		tag.setInteger(WareHouseConfiguration.dyeColorTag, this.dyeColor.getMetadata());
		
		tag.setBoolean(WareHouseConfiguration.advancedTag, this.advanced);
		
		return tag;
	}
}