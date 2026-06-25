package com.prefab.base;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.storage.ValueOutput;

/**
 * This abstract class is used as the base for CompoundNBT configurations.
 *
 * @author WuestMan
 */
public abstract class BaseConfig<T extends BaseConfig<T>>{
	/**
	 * Writes to an CompoundNBT given an existing CompoundNBT.
	 *
	 * @param compound The existing tag to add custom values too.
	 */
	public abstract void WriteToNBTCompound(ValueOutput compound);

	/**
	 * Reads data from an CompoundNBT and creates an instance of the configuration class.
	 *
	 * @param compound The compound containing the data to create the configuration class.
	 * @return An instance containing the data held within the CompoundNBT
	 */
	public abstract T ReadFromCompoundNBT(CompoundTag compound);
}