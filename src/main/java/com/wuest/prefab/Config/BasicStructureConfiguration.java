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
		
		EnumBasicStructureName.AdvancedHorseStable.getClearShape().setDirection(EnumFacing.SOUTH);
		EnumBasicStructureName.AdvancedHorseStable.getClearShape().setHeight(8);
		EnumBasicStructureName.AdvancedHorseStable.getClearShape().setWidth(17);
		EnumBasicStructureName.AdvancedHorseStable.getClearShape().setLength(34);
		EnumBasicStructureName.AdvancedHorseStable.getClearPositionOffset().setSouthOffset(1);
		EnumBasicStructureName.AdvancedHorseStable.getClearPositionOffset().setEastOffset(8);
		
		EnumBasicStructureName.Barn.getClearShape().setDirection(EnumFacing.SOUTH);
		EnumBasicStructureName.Barn.getClearShape().setHeight(10);
		EnumBasicStructureName.Barn.getClearShape().setWidth(30);
		EnumBasicStructureName.Barn.getClearShape().setLength(35);
		EnumBasicStructureName.Barn.getClearPositionOffset().setSouthOffset(1);
		EnumBasicStructureName.Barn.getClearPositionOffset().setEastOffset(15);
		
		EnumBasicStructureName.MachineryTower.getClearShape().setDirection(EnumFacing.SOUTH);
		EnumBasicStructureName.MachineryTower.getClearShape().setHeight(12);
		EnumBasicStructureName.MachineryTower.getClearShape().setWidth(16);
		EnumBasicStructureName.MachineryTower.getClearShape().setLength(16);
		EnumBasicStructureName.MachineryTower.getClearPositionOffset().setSouthOffset(1);
		EnumBasicStructureName.MachineryTower.getClearPositionOffset().setEastOffset(8);
		
		// Defense bunker.
		EnumBasicStructureName.DefenseBunker.getClearShape().setDirection(EnumFacing.SOUTH);
		EnumBasicStructureName.DefenseBunker.getClearShape().setHeight(17);
		EnumBasicStructureName.DefenseBunker.getClearShape().setWidth(32);
		EnumBasicStructureName.DefenseBunker.getClearShape().setLength(32);
		EnumBasicStructureName.DefenseBunker.getClearPositionOffset().setSouthOffset(1);
		EnumBasicStructureName.DefenseBunker.getClearPositionOffset().setEastOffset(15);
		
		// Defense bunker.
		EnumBasicStructureName.MineshaftEntrance.getClearShape().setDirection(EnumFacing.SOUTH);
		EnumBasicStructureName.MineshaftEntrance.getClearShape().setHeight(6);
		EnumBasicStructureName.MineshaftEntrance.getClearShape().setWidth(7);
		EnumBasicStructureName.MineshaftEntrance.getClearShape().setLength(7);
		EnumBasicStructureName.MineshaftEntrance.getClearPositionOffset().setSouthOffset(1);
		EnumBasicStructureName.MineshaftEntrance.getClearPositionOffset().setEastOffset(3);
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
		BasicStructureConfiguration basicConfig = (BasicStructureConfiguration)config;
		
		if (messageTag.hasKey(BasicStructureConfiguration.structureEnumNameTag))
		{
			basicConfig.basicStructureName = EnumBasicStructureName.valueOf(messageTag.getString(BasicStructureConfiguration.structureEnumNameTag));
		}
		
		if (messageTag.hasKey(BasicStructureConfiguration.structureDisplayNameTag))
		{
			basicConfig.structureDisplayName = messageTag.getString(BasicStructureConfiguration.structureDisplayNameTag);
		}
	}
	
	@Override
	protected NBTTagCompound CustomWriteToNBTTagCompound(NBTTagCompound tag)
	{
		tag.setString(BasicStructureConfiguration.structureEnumNameTag, this.basicStructureName.name());
		
		if (this.structureDisplayName != null)
		{
			tag.setString(BasicStructureConfiguration.structureDisplayNameTag, this.structureDisplayName);
		}
		
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
		Custom("custom", null, null, null, null, 0, 0),
		AdavancedCoop("advancedcoop", "item.prefab.advanced.chicken.coop", 
				"assets/prefab/structures/advancedcoop.zip", "textures/gui/advanced_chicken_coop_topdown.png", "item_advanced_chicken_coop", 
				156, 121),
		AdvancedHorseStable("advanced_horse_stable", "item.prefab.advanced.horse.stable", 
				"assets/prefab/structures/advanced_horse_stable.zip", "textures/gui/advanced_horse_stable_topdown.png", "item_advanced_horse_stable", 
				128, 158),
		Barn("barn", "item.prefab.barn", "assets/prefab/structures/barn.zip", "textures/gui/barn_topdown.png", "item_barn", 164, 160),
		MachineryTower("machinery_tower", "item.prefab.machinery.tower",
				"assets/prefab/structures/machinery_tower.zip",
				"textures/gui/machinery_tower_topdown.png",
				"item_machinery_tower",
				153, 175),
		DefenseBunker("defense_bunker", "item.prefab.defense.bunker",
				"assets/prefab/structures/defense_bunker.zip",
				"textures/gui/defense_bunker_topdown.png",
				"item_defense_bunker",
				153, 175),
		MineshaftEntrance("mineshaft_entrance", "item.prefab.mineshaft.entrance",
				"assets/prefab/structures/mineshaft_entrance.zip",
				"textures/gui/mineshaft_entrance_topdown.png",
				"item_mineshaft_entrance",
				135, 159);
		
		private String name;
		private String assetLocation;
		private String topDownPictureLocation;
		private String unlocalizedName;
		private BuildShape clearShape;
		private ResourceLocation resourceLocation;
		private PositionOffset clearPositionOffset;
		private int imageHeight;
		private int imageWidth;
		
		/**
		 * This is a basic structure which doesn't have any (or limited) custom processing.
		 * @param name - This is the name for this structure. This is used for comparative purposes in item stacks.
		 * @param unlocalizedName - This is the localization key to determine the displayed name to the user.
		 * @param assetLocation - This is location of the structure zip file in the jar file.
		 * @param topDownPictureLocation - This is the picture location used in the basic GUI when the player uses the item.
		 * @param resourceLocation - This is the resource location for the item's texture when it's in the players and or in inventories/the world.
		 * @param imageHeight - This is the height of the image shown to the user in the build structure GUI.
		 * @param imageWidth - This is the width of the image shown to the user in the build structure GUI.
		 */
		private EnumBasicStructureName(String name, String unlocalizedName, String assetLocation, String topDownPictureLocation, String resourceLocation, int imageHeight, int imageWidth)
		{
			this.name = name;
			this.unlocalizedName = unlocalizedName;
			this.assetLocation = assetLocation;
			this.topDownPictureLocation = topDownPictureLocation;
			this.imageHeight = imageHeight;
			this.imageWidth = imageWidth;
			
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
		
		public int getImageHeight()
		{
			return this.imageHeight;
		}
		
		public int getImageWidth()
		{
			return this.imageWidth;
		}
	}
}
