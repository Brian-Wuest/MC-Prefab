package com.wuest.prefab.Structures.Config;

import com.wuest.prefab.ModRegistry;
import com.wuest.prefab.Structures.Predefined.StructureWarehouse;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

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
	@Override
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
	
	/**
	 * This is used to actually build the structure as it creates the structure instance and calls build structure.
	 * @param player The player which requested the build.
	 * @param world The world instance where the build will occur.
	 * @param hitBlockPos This hit block position.
	 */
	@Override
	protected void ConfigurationSpecificBuildStructure(EntityPlayer player, World world, BlockPos hitBlockPos)
	{
		String assetLocation = this.advanced ? StructureWarehouse.ADVANCED_ASSET_LOCATION : StructureWarehouse.ASSETLOCATION;
		
		StructureWarehouse structure = StructureWarehouse.CreateInstance(assetLocation, StructureWarehouse.class);
		
		if (structure.BuildStructure(this, world, hitBlockPos, EnumFacing.NORTH, player))
		{
			if (this.advanced)
			{
				player.inventory.clearMatchingItems(ModRegistry.AdvancedWareHouse, -1, 1, null);
			}
			else
			{
				player.inventory.clearMatchingItems(ModRegistry.WareHouse, -1, 1, null);
			}
			
			player.inventoryContainer.detectAndSendChanges();
		}
	}
}