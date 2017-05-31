package com.wuest.prefab.Config;

import com.wuest.prefab.ModRegistry;
import com.wuest.prefab.StructureGen.CustomStructures.StructureChickenCoop;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * 
 * @author WuestMan
 *
 */
public class ChickenCoopConfiguration extends StructureConfiguration
{
	/**
	 * Custom method to read the NBTTagCompound message.
	 * @param messageTag The message to create the configuration from.
	 * @return An new configuration object with the values derived from the NBTTagCompound.
	 */
	@Override
	public ChickenCoopConfiguration ReadFromNBTTagCompound(NBTTagCompound messageTag) 
	{
		ChickenCoopConfiguration config = new ChickenCoopConfiguration();
		
		return (ChickenCoopConfiguration)super.ReadFromNBTTagCompound(messageTag, config);
	}
	
	@Override
	protected void ConfigurationSpecificBuildStructure(EntityPlayer player, World world, BlockPos hitBlockPos)
	{
		StructureChickenCoop structure = StructureChickenCoop.CreateInstance(StructureChickenCoop.ASSETLOCATION, StructureChickenCoop.class);
		
		if (structure.BuildStructure(this, world, hitBlockPos, EnumFacing.NORTH, player))
		{
			player.inventory.clearMatchingItems(ModRegistry.ChickenCoop(), -1, 1, null);
			player.inventoryContainer.detectAndSendChanges();
		}
	}
}