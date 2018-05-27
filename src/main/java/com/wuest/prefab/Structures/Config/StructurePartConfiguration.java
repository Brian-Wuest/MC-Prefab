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
	 * The height for this non-stairs style.
	 */
	public int generalWidth;

	/**
	 * The width for this non-stairs style.
	 */
	public int generalHeight;

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
		this.style = EnumStyle.Wall;
		this.stairHeight = 3;
		this.stairWidth = 2;
		this.generalHeight = 3;
		this.generalWidth = 3;
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
		tag.setString("style", this.style.name());
		tag.setInteger("stair_height", this.stairHeight);
		tag.setInteger("stair_width", this.stairWidth);
		tag.setInteger("general_height", this.generalHeight);
		tag.setInteger("general_width", this.generalWidth);
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
			((StructurePartConfiguration) config).partMaterial = EnumStructureMaterial.valueOf(messageTag.getString("material"));
		}

		if (messageTag.hasKey("style"))
		{
			((StructurePartConfiguration) config).style = EnumStyle.valueOf(messageTag.getString("style"));
		}

		if (messageTag.hasKey("stair_height"))
		{
			((StructurePartConfiguration) config).stairHeight = messageTag.getInteger("stair_height");
		}

		if (messageTag.hasKey("stair_width"))
		{
			((StructurePartConfiguration) config).stairWidth = messageTag.getInteger("stair_width");
		}

		if (messageTag.hasKey("general_height"))
		{
			((StructurePartConfiguration) config).generalHeight = messageTag.getInteger("general_height");
		}

		if (messageTag.hasKey("general_width"))
		{
			((StructurePartConfiguration) config).generalWidth = messageTag.getInteger("general_width");
		}
	}

	/**
	 * 
	 * @author WuestMan
	 *
	 */
	public enum EnumStyle
	{
		Wall("prefab.gui.part_style.wall"),
		Gate("prefab.gui.part_style.gate"),
		Frame("prefab.gui.part_style.frame"),
		Stairs("prefab.gui.part_style.stairs"),
		DoorWay("prefab.gui.part_style.door_way");

		private String translateKey = "";

		private EnumStyle(String translateKey)
		{
			this.translateKey = translateKey;
		}

		public String getTranslateKey()
		{
			return this.translateKey;
		}

		public static EnumStyle getByOrdinal(int ordinal)
		{
			for (EnumStyle dimension : EnumStyle.values())
			{
				if (dimension.ordinal() == ordinal)
				{
					return dimension;
				}
			}

			return EnumStyle.Wall;
		}
	}
}