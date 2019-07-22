package com.wuest.prefab.Structures.Config;

import com.wuest.prefab.ModRegistry;
import com.wuest.prefab.Structures.Items.StructureItem;
import com.wuest.prefab.Structures.Predefined.StructureWarehouse;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ServerWorld;
import net.minecraft.world.World;

/**
 * This class is the main configuration holder.
 * 
 * @author WuesMan
 */
public class WareHouseConfiguration extends StructureConfiguration
{
	private static String dyeColorTag = "dyeColor";
	private static String advancedTag = "advanced";

	/**
	 * Determines the glass color.
	 */
	public DyeColor dyeColor;

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
		this.houseFacing = Direction.SOUTH;
		this.dyeColor = DyeColor.CYAN;
		this.advanced = false;
	}

	@Override
	protected void CustomReadFromNBTTag(CompoundNBT messageTag, StructureConfiguration config)
	{
		if (messageTag.contains(WareHouseConfiguration.dyeColorTag))
		{
			((WareHouseConfiguration) config).dyeColor = DyeColor.byId(messageTag.getInt(WareHouseConfiguration.dyeColorTag));
		}

		if (messageTag.contains(WareHouseConfiguration.advancedTag))
		{
			((WareHouseConfiguration) config).advanced = messageTag.getBoolean(WareHouseConfiguration.advancedTag);
		}
	}

	/**
	 * Custom method to read the CompoundNBT message.
	 * 
	 * @param messageTag The message to create the configuration from.
	 * @return An new configuration object with the values derived from the CompoundNBT.
	 */
	@Override
	public WareHouseConfiguration ReadFromCompoundNBT(CompoundNBT messageTag)
	{
		WareHouseConfiguration config = new WareHouseConfiguration();

		return (WareHouseConfiguration) super.ReadFromCompoundNBT(messageTag, config);
	}

	@Override
	protected CompoundNBT CustomWriteToCompoundNBT(CompoundNBT tag)
	{
		tag.putInt(WareHouseConfiguration.dyeColorTag, this.dyeColor.getId());

		tag.putBoolean(WareHouseConfiguration.advancedTag, this.advanced);

		return tag;
	}

	/**
	 * This is used to actually build the structure as it creates the structure instance and calls build structure.
	 * 
	 * @param player      The player which requested the build.
	 * @param world       The world instance where the build will occur.
	 * @param hitBlockPos This hit block position.
	 */
	@Override
	protected void ConfigurationSpecificBuildStructure(PlayerEntity player, ServerWorld world, BlockPos hitBlockPos)
	{
		String assetLocation = this.advanced ? StructureWarehouse.ADVANCED_ASSET_LOCATION : StructureWarehouse.ASSETLOCATION;

		StructureWarehouse structure = StructureWarehouse.CreateInstance(assetLocation, StructureWarehouse.class);

		if (structure.BuildStructure(this, world, hitBlockPos, Direction.NORTH, player))
		{
			StructureItem structureItem = this.advanced ? ModRegistry.AdvancedWareHouse() : ModRegistry.WareHouse();

			this.RemoveStructureItemFromPlayer(player, structureItem);
		}
	}
}