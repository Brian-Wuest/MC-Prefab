package com.wuest.prefab.Config;

import com.wuest.prefab.StructureGen.BuildShape;
import com.wuest.prefab.StructureGen.PositionOffset;

import net.minecraft.item.EnumDyeColor;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

/**
 * This class is used for the basic structures in the mod.
 * @author WuestMan
 *
 */
public class BasicStructureConfiguration extends StructureConfiguration
{
	private static String structureEnumNameTag = "structureEnumName";
	private static String structureDisplayNameTag = "structureDisplayName";
	
	public EnumBasicStructureName basicStructureName;
	public String structureDisplayName;
	
	static
	{
		// This static method is used to set up the clear shapes for the basic structure names.
		EnumBasicStructureName.AdavancedCoop.getClearShape().setDirection(EnumFacing.SOUTH);
		EnumBasicStructureName.AdavancedCoop.getClearShape().setHeight(10);
		EnumBasicStructureName.AdavancedCoop.getClearShape().setWidth(11);
		EnumBasicStructureName.AdavancedCoop.getClearShape().setLength(11);
		EnumBasicStructureName.AdavancedCoop.getClearPositionOffset().setSouthOffset(1);
		EnumBasicStructureName.AdavancedCoop.getClearPositionOffset().setEastOffset(5);
	}
	
	/**
	 * Initializes a new instance of the BasicStructureConfiguration class.
	 */
	public BasicStructureConfiguration()
	{
		super();
	}
	
	public String getDisplayName()
	{
		if (this.basicStructureName == EnumBasicStructureName.Custom)
		{
			return this.structureDisplayName;
		}
		else
		{
			return this.basicStructureName.getUnlocalizedName();
		}
	}
	
	public boolean IsCustomStructure()
	{
		return this.basicStructureName == EnumBasicStructureName.Custom;
	}
	
	@Override
	public void Initialize()
	{
		super.Initialize();
		this.houseFacing = EnumFacing.NORTH;
		this.basicStructureName = EnumBasicStructureName.Custom;
	}
	
	@Override
	protected void CustomReadFromNBTTag(NBTTagCompound messageTag, StructureConfiguration config)
	{
	
	}
	
	@Override
	protected NBTTagCompound CustomWriteToNBTTagCompound(NBTTagCompound tag)
	{

		return tag;
	}
	
	public BasicStructureConfiguration ReadFromNBTTagCompound(NBTTagCompound messageTag) 
	{
		BasicStructureConfiguration config = new BasicStructureConfiguration();
		
		return (BasicStructureConfiguration)super.ReadFromNBTTagCompound(messageTag, config);
	}
	
	/**
	 * This enum is used to list the names of the basic structures and provide other information necessary. 
	 * @author WuestMan
	 *
	 */
	public enum EnumBasicStructureName
	{
		Custom("custom", null, null, null, null),
		AdavancedCoop("advancedcoop", "item.advanced.chicken.coop", "assets/prefab/structures/advanced_chicken_coop.zip", "textures/gui/advanced_chicken_coop_topdown.png", "item_advanced_chicken_coop");
		
		private String name;
		private String assetLocation;
		private String topDownPictureLocation;
		private String unlocalizedName;
		private BuildShape clearShape;
		private ResourceLocation resourceLocation;
		private PositionOffset clearPositionOffset;
		
		private EnumBasicStructureName(String name, String unlocalizedName, String assetLocation, String topDownPictureLocation, String resourceLocation)
		{
			this.name = name;
			this.unlocalizedName = unlocalizedName;
			this.assetLocation = assetLocation;
			this.topDownPictureLocation = topDownPictureLocation;
			
			this.clearShape = new BuildShape();
			this.clearPositionOffset = new PositionOffset();
			
			if (resourceLocation != null)
			{
				this.resourceLocation = new ResourceLocation("prefab", resourceLocation);
			}
		}
		
		public String getName()
		{
			return this.name;
		}
		
		public String getUnlocalizedName()
		{
			return this.unlocalizedName;
		}
		
		public String getAssetLocation()
		{
			return this.assetLocation;
		}
		
		public ResourceLocation getTopDownPictureLocation()
		{
			if (this.topDownPictureLocation != null)
			{
				return new ResourceLocation("prefab", this.topDownPictureLocation);
			}
			
			return null;
		}
		
		public BuildShape getClearShape()
		{
			return this.clearShape;
		}
		
		public PositionOffset getClearPositionOffset()
		{
			return this.clearPositionOffset;
		}
		
		public ResourceLocation getResourceLocation()
		{
			return this.resourceLocation;
		}
	}
}
