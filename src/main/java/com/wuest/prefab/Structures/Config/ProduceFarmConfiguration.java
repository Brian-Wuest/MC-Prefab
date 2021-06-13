package com.wuest.prefab.Structures.Config;

import com.wuest.prefab.ModRegistry;
import com.wuest.prefab.Structures.Predefined.StructureProduceFarm;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * 
 * @author WuestMan
 *
 */
public class ProduceFarmConfiguration extends StructureConfiguration 
{
	private static String dyeColorTag = "dyeColor";
	public EnumDyeColor dyeColor;
	
	@Override
	public void Initialize()
	{
		super.Initialize();
		this.houseFacing = EnumFacing.NORTH;
		this.dyeColor = EnumDyeColor.CYAN;
	}
	
	@Override
	protected void CustomReadFromNBTTag(NBTTagCompound messageTag, StructureConfiguration config)
	{
		if (messageTag.hasKey(ProduceFarmConfiguration.dyeColorTag))
		{
			((ProduceFarmConfiguration)config).dyeColor = EnumDyeColor.byMetadata(messageTag.getInteger(ProduceFarmConfiguration.dyeColorTag));
		}		
	}
	
	@Override
	protected NBTTagCompound CustomWriteToNBTTagCompound(NBTTagCompound tag)
	{
		tag.setInteger(ProduceFarmConfiguration.dyeColorTag, this.dyeColor.getMetadata());
		
		return tag;
	}
	
	/**
	 * Custom method to read the NBTTagCompound message.
	 * @param messageTag The message to create the configuration from.
	 * @return An new configuration object with the values derived from the NBTTagCompound.
	 */
	@Override
	public ProduceFarmConfiguration ReadFromNBTTagCompound(NBTTagCompound messageTag) 
	{
		ProduceFarmConfiguration config = new ProduceFarmConfiguration();
		
		return (ProduceFarmConfiguration)super.ReadFromNBTTagCompound(messageTag, config);
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
		StructureProduceFarm structure = StructureProduceFarm.CreateInstance(StructureProduceFarm.ASSETLOCATION, StructureProduceFarm.class);
		
		if (structure.BuildStructure(this, world, hitBlockPos, EnumFacing.NORTH, player))
		{
			player.inventory.clearMatchingItems(ModRegistry.ProduceFarm, -1, 1, null);
			player.inventoryContainer.detectAndSendChanges();
		}
	}
}