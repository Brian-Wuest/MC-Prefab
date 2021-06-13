package com.wuest.prefab.structures.config;

import com.wuest.prefab.ModRegistry;
import com.wuest.prefab.structures.predefined.StructureBulldozer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;

/**
 * @author WuestMan
 */
public class BulldozerConfiguration extends StructureConfiguration {

	public boolean creativeMode = false;

	/**
	 * Initializes a new instance of the {@link BulldozerConfiguration} class.
	 */
	public BulldozerConfiguration() {
	}

	/**
	 * Custom method to read the NBTTagCompound message.
	 *
	 * @param messageTag The message to create the configuration from.
	 * @return An new configuration object with the values derived from the NBTTagCompound.
	 */
	@Override
	public BulldozerConfiguration ReadFromCompoundNBT(NBTTagCompound messageTag) {
		BulldozerConfiguration config = new BulldozerConfiguration();

		return (BulldozerConfiguration) super.ReadFromCompoundNBT(messageTag, config);
	}

	/**
	 * This is used to actually build the structure as it creates the structure instance and calls build structure.
	 *
	 * @param player      The player which requested the build.
	 * @param world       The world instance where the build will occur.
	 * @param hitBlockPos This hit block position.
	 */
	@Override
	protected void ConfigurationSpecificBuildStructure(EntityPlayer player, WorldServer world, BlockPos hitBlockPos) {
		StructureBulldozer structure = new StructureBulldozer();

		if (player.isCreative()) {
			this.creativeMode = true;
		}

		if (structure.BuildStructure(this, world, hitBlockPos, EnumFacing.NORTH, player)) {
			ItemStack stack = player.getHeldItem(EnumHand.OFF_HAND);
			EnumHand hand = EnumHand.OFF_HAND;

			if (stack.getItem() == ModRegistry.Creative_Bulldozer) {
				this.creativeMode = true;
			}

			if (stack.getItem() != ModRegistry.Bulldozer) {
				stack = player.getHeldItem(EnumHand.MAIN_HAND);
				hand = EnumHand.MAIN_HAND;

				if (stack.getItem() == ModRegistry.Creative_Bulldozer) {
					this.creativeMode = true;
				}
			}

			// Only damage the item if this is the regular bulldozer.
			if (stack.getItem() == ModRegistry.Bulldozer) {
				stack.damageItem(1, player);
			}
		}
	}

}