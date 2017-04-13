package com.wuest.prefab.Config;

import net.minecraft.item.EnumDyeColor;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;

/**
 * 
 * @author WuestMan
 *
 */
public class MonsterMasherConfiguration extends StructureConfiguration
{
	private static String dyeColorTag = "dyeColor";
	public EnumDyeColor dyeColor;
	
	@Override
	public void Initialize()
	{
		super.Initialize();
		this.houseFacing = EnumFacing.NORTH;
		this.dyeColor = EnumDyeColor.CYAN;
	}
	
	@Override
	protected void CustomReadFromNBTTag(NBTTagCompound messageTag, StructureConfiguration config)
	{
		if (messageTag.hasKey(MonsterMasherConfiguration.dyeColorTag))
		{
			((MonsterMasherConfiguration)config).dyeColor = EnumDyeColor.byMetadata(messageTag.getInteger(MonsterMasherConfiguration.dyeColorTag));
		}		
	}
	
	@Override
	protected NBTTagCompound CustomWriteToNBTTagCompound(NBTTagCompound tag)
	{
		tag.setInteger(MonsterMasherConfiguration.dyeColorTag, this.dyeColor.getMetadata());
		
		return tag;
	}
	
	/**
	 * Custom method to read the NBTTagCompound message.
	 * @param messageTag The message to create the configuration from.
	 * @return An new configuration object with the values derived from the NBTTagCompound.
	 */
	public MonsterMasherConfiguration ReadFromNBTTagCompound(NBTTagCompound messageTag) 
	{
		MonsterMasherConfiguration config = new MonsterMasherConfiguration();
		
		return (MonsterMasherConfiguration)super.ReadFromNBTTagCompound(messageTag, config);
	}
}
