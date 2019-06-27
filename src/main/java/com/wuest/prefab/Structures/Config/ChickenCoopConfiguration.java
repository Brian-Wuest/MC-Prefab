package com.wuest.prefab.Structures.Config;

import com.wuest.prefab.ModRegistry;
import com.wuest.prefab.Structures.Predefined.StructureChickenCoop;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
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
	public ChickenCoopConfiguration ReadFromCompoundNBT(CompoundNBT messageTag) 
	{
		ChickenCoopConfiguration config = new ChickenCoopConfiguration();
		
		return (ChickenCoopConfiguration)super.ReadFromCompoundNBT(messageTag, config);
	}
	
	@Override
	protected void ConfigurationSpecificBuildStructure(PlayerEntity player, World world, BlockPos hitBlockPos)
	{
		StructureChickenCoop structure = StructureChickenCoop.CreateInstance(StructureChickenCoop.ASSETLOCATION, StructureChickenCoop.class);
		
		if (structure.BuildStructure(this, world, hitBlockPos, Direction.NORTH, player))
		{
			this.RemoveStructureItemFromPlayer(player, ModRegistry.ChickenCoop());
		}
	}
}