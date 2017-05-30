package com.wuest.prefab.Config;

import com.wuest.prefab.ModRegistry;
import com.wuest.prefab.StructureGen.CustomStructures.StructureFishPond;

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
public class FishPondConfiguration extends StructureConfiguration
{
	/**
	 * Custom method to read the NBTTagCompound message.
	 * @param messageTag The message to create the configuration from.
	 * @return An new configuration object with the values derived from the NBTTagCompound.
	 */
	@Override
	public FishPondConfiguration ReadFromNBTTagCompound(NBTTagCompound messageTag) 
	{
		FishPondConfiguration config = new FishPondConfiguration();
		
		return (FishPondConfiguration)super.ReadFromNBTTagCompound(messageTag, config);
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
		StructureFishPond structure = StructureFishPond.CreateInstance(StructureFishPond.ASSETLOCATION, StructureFishPond.class);
		
		if (structure.BuildStructure(this, world, hitBlockPos, EnumFacing.NORTH, player))
		{
			player.inventory.clearMatchingItems(ModRegistry.FishPond(), -1, 1, null);
			player.inventoryContainer.detectAndSendChanges();
		}
	}
}
