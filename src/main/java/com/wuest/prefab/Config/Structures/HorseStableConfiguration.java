package com.wuest.prefab.Config.Structures;

import com.wuest.prefab.ModRegistry;
import com.wuest.prefab.StructureGen.CustomStructures.StructureHorseStable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * The horse stable configuration.
 * @author WuestMan
 */
public class HorseStableConfiguration extends StructureConfiguration
{
	/**
	 * Custom method to read the NBTTagCompound message.
	 * @param messageTag The message to create the configuration from.
	 * @return An new configuration object with the values derived from the NBTTagCompound.
	 */
	@Override
	public HorseStableConfiguration ReadFromNBTTagCompound(NBTTagCompound messageTag) 
	{
		HorseStableConfiguration config = new HorseStableConfiguration();
		
		return (HorseStableConfiguration)super.ReadFromNBTTagCompound(messageTag, config);
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
		StructureHorseStable structure = StructureHorseStable.CreateInstance(StructureHorseStable.ASSETLOCATION, StructureHorseStable.class);
		
		if (structure.BuildStructure(this, world, hitBlockPos, EnumFacing.NORTH, player))
		{
			player.inventory.clearMatchingItems(ModRegistry.HorseStable(), -1, 1, null);
			player.inventoryContainer.detectAndSendChanges();
		}
	}
}
