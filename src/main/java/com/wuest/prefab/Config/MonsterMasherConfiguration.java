package com.wuest.prefab.Config;

import net.minecraft.item.EnumDyeColor;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;

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
	
	public MonsterMasherConfiguration ReadFromNBTTagCompound(NBTTagCompound messageTag) 
	{
		MonsterMasherConfiguration config = new MonsterMasherConfiguration();
		
		return (MonsterMasherConfiguration)super.ReadFromNBTTagCompound(messageTag, config);
	}
}
