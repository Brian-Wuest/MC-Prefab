package com.wuest.prefab.Structures.Config;

import com.wuest.prefab.Structures.Base.EnumStructureMaterial;
import com.wuest.prefab.Structures.Items.ItemStructurePart;
import com.wuest.prefab.Structures.Predefined.StructurePart;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * 
 * @author WuestMan
 *
 */
public class StructurePartConfiguration extends StructureConfiguration
{
	/**
	 * Determines the type of material to build the part with.
	 */
	public EnumStructureMaterial partMaterial;

	/**
	 * The wall\Gate dimension chosen by the player.
	 */
	public EnumDimensions dimensions;

	/**
	 * The width of stairs when the style is set to Stairs.
	 */
	public int stairWidth;

	/**
	 * The height of the stairs when the style is set to Stairs.
	 */
	public int stairHeight;

	/**
	 * The style of this part.
	 */
	public EnumStyle style;

	/**
	 * Initializes any properties for this class.
	 */
	@Override
	public void Initialize()
	{
		super.Initialize();

		this.partMaterial = EnumStructureMaterial.Cobblestone;
		this.dimensions = EnumDimensions.ThreeXThree;
		this.style = EnumStyle.Wall;
		this.stairHeight = 3;
		this.stairWidth = 2;
	}

	/**
	 * Custom method to read the NBTTagCompound message.
	 * 
	 * @param messageTag The message to create the configuration from.
	 * @return An new configuration object with the values derived from the NBTTagCompound.
	 */
	@Override
	public StructurePartConfiguration ReadFromNBTTagCompound(NBTTagCompound messageTag)
	{
		StructurePartConfiguration config = new StructurePartConfiguration();

		return (StructurePartConfiguration) super.ReadFromNBTTagCompound(messageTag, config);
	}

	@Override
	protected void ConfigurationSpecificBuildStructure(EntityPlayer player, World world, BlockPos hitBlockPos)
	{
		StructurePart structure = StructurePart.CreateInstance();

		if (structure.BuildStructure(this, world, hitBlockPos, EnumFacing.NORTH, player))
		{
			ItemStack usedItemStack = player.getHeldItemMainhand();

			if (!(usedItemStack.getItem() instanceof ItemStructurePart))
			{
				usedItemStack = player.getHeldItemOffhand();
			}

			usedItemStack.damageItem(1, player);
			player.inventoryContainer.detectAndSendChanges();
		}

	}

	/**
	 * Custom method which can be overridden to write custom properties to the tag.
	 * 
	 * @param tag The NBTTagCompound to write the custom properties too.
	 * @return The updated tag.
	 */
	@Override
	protected NBTTagCompound CustomWriteToNBTTagCompound(NBTTagCompound tag)
	{
		tag.setString("material", this.partMaterial.name());
		tag.setString("dimensions", this.dimensions.name());
		tag.setString("style", this.style.name());
		tag.setInteger("stair_height", this.stairHeight);
		tag.setInteger("stair_width", this.stairWidth);
		return tag;
	}

	/**
	 * Custom method to read the NBTTagCompound message.
	 * 
	 * @param messageTag The message to create the configuration from.
	 * @param config The configuration to read the settings into.
	 */
	@Override
	protected void CustomReadFromNBTTag(NBTTagCompound messageTag, StructureConfiguration config)
	{
		if (messageTag.hasKey("material"))
		{
			this.partMaterial = EnumStructureMaterial.valueOf(messageTag.getString("material"));
		}

		if (messageTag.hasKey("dimensions"))
		{
			this.dimensions = EnumDimensions.valueOf(messageTag.getString("dimensions"));
		}

		if (messageTag.hasKey("style"))
		{
			this.style = EnumStyle.valueOf(messageTag.getString("style"));
		}

		if (messageTag.hasKey("stair_height"))
		{
			this.stairHeight = messageTag.getInteger("stair_height");
		}

		if (messageTag.hasKey("stair_width"))
		{
			this.stairWidth = messageTag.getInteger("stair_witdth");
		}
	}

	/**
	 * 
	 * @author WuestMan
	 *
	 */
	public enum EnumDimensions
	{
		ThreeXThree("3x3", 3, 3), FivexFive("5x5", 5, 5), SevenxSeven("7x7", 7, 7), NinexNine("9x9", 9, 9);

		public final String diaplayName;
		public final int height;
		public final int width;

		private EnumDimensions(String displayName, int height, int width)
		{
			this.height = height;
			this.width = width;
			this.diaplayName = displayName;
		}

		@Override
		public String toString()
		{
			return this.diaplayName;
		}
	}

	/**
	 * 
	 * @author WuestMan
	 *
	 */
	public enum EnumStyle
	{
		Wall(), Gate(), Frame(), Stairs(), Circle();

		private EnumStyle()
		{
		}
	}
}