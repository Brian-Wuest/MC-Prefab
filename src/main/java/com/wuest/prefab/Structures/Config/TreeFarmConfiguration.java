package com.wuest.prefab.Structures.Config;

import com.wuest.prefab.ModRegistry;
import com.wuest.prefab.Structures.Predefined.StructureTreeFarm;

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
public class TreeFarmConfiguration extends StructureConfiguration 
{
	/**
	 * Custom method to read the NBTTagCompound message.
	 * @param messageTag The message to create the configuration from.
	 * @return An new configuration object with the values derived from the NBTTagCompound.
	 */
	@Override
	public TreeFarmConfiguration ReadFromNBTTagCompound(NBTTagCompound messageTag) 
	{
		TreeFarmConfiguration config = new TreeFarmConfiguration();
		
		return (TreeFarmConfiguration)super.ReadFromNBTTagCompound(messageTag, config);
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
		StructureTreeFarm structure = StructureTreeFarm.CreateInstance(StructureTreeFarm.ASSETLOCATION, StructureTreeFarm.class);
		if (structure.BuildStructure(this, world, hitBlockPos, EnumFacing.NORTH, player))
		{
			player.inventory.clearMatchingItems(ModRegistry.TreeFarm, -1, 1, null);
			player.inventoryContainer.detectAndSendChanges();
		}
	}
}
