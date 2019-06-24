package com.wuest.prefab.Blocks;

import java.util.List;

import javax.annotation.Nullable;

import com.google.common.collect.Lists;
import com.wuest.prefab.ModRegistry;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ToolType;

/**
 * This is the compressed Obsidian block class.
 * 
 * @author WuestMan
 *
 */
public class BlockCompressedObsidian extends Block
{
	public final EnumType typeofStone;

	public final ItemGroup itemGroup;
	
	/**
	 * Initializes a new instance of the BlockCompressedObsidian class.
	 */
	public BlockCompressedObsidian(EnumType stoneType)
	{
		super(Block.Properties.create(Material.ROCK)
			.hardnessAndResistance(50.0f, 2000.0f)
			.sound(SoundType.STONE));
		
		this.itemGroup = ItemGroup.BUILDING_BLOCKS;
		this.typeofStone = stoneType;
		ModRegistry.setBlockName(this, stoneType.unlocalizedName);
	}
	
	   @Nullable
	   @Override
	   public ToolType getHarvestTool(BlockState state) {
	      return ToolType.PICKAXE;
	   }

	   @Override
	   public int getHarvestLevel(BlockState state) {
	      return 3;
	   }

	/**
	 * An enum which contains the various types of block variants.
	 * 
	 * @author WuestMan
	 *
	 */
	public static enum EnumType implements IStringSerializable
	{
		COMPRESSED_OBSIDIAN(0, "block_compressed_obsidian", "block_compressed_obsidian"),
		DOUBLE_COMPRESSED_OBSIDIAN(1, "block_double_compressed_obsidian", "block_double_compressed_obsidian"),;

		private final int meta;

		/** The EnumType's name. */
		private final String name;
		private final String unlocalizedName;
		/** Array of the Block's BlockStates */
		private static final BlockCompressedObsidian.EnumType[] META_LOOKUP = new BlockCompressedObsidian.EnumType[values().length];

		private EnumType(int meta, String name)
		{
			this(meta, name, name);
		}

		private EnumType(int meta, String name, String unlocalizedName)
		{
			this.meta = meta;
			this.name = name;
			this.unlocalizedName = unlocalizedName;
		}

		/**
		 * A list of resource locations for the names.
		 * 
		 * @return A list of resource locations for the numerous types in this enum.
		 */
		public static ResourceLocation[] GetNames()
		{
			List<ResourceLocation> list = Lists.newArrayList();

			for (EnumType type : EnumType.values())
			{
				list.add(new ResourceLocation("prefab", type.unlocalizedName));
			}

			return list.toArray(new ResourceLocation[list.size()]);
		}

		/**
		 * The EnumType's meta data value.
		 * 
		 * @return the meta data for this block.
		 */
		public int getMetadata()
		{
			return this.meta;
		}

		/**
		 * Gets the name of this enum value.
		 */
		public String toString()
		{
			return this.name;
		}

		@Override
		public String getName()
		{
			return this.name;
		}

		/**
		 * The unlocalized name of this EnumType.
		 * 
		 * @return A string containing the unlocalized name.
		 */
		public String getUnlocalizedName()
		{
			return this.unlocalizedName;
		}

		/**
		 * Returns an EnumType for the BlockState from a metadata value.
		 * 
		 * @param meta The meta data value to equate to a {@link BlockCompressedObsidian.EnumType}
		 * @return If the meta data is invalid the default will be used, otherwise the EnumType found.
		 */
		public static BlockCompressedObsidian.EnumType byMetadata(int meta)
		{
			if (meta < 0 || meta >= META_LOOKUP.length)
			{
				meta = 0;
			}

			return META_LOOKUP[meta];
		}

		static
		{
			for (BlockCompressedObsidian.EnumType type : values())
			{
				META_LOOKUP[type.getMetadata()] = type;
			}
		}
	}

}
