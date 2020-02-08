package com.wuest.prefab.Blocks;

import com.wuest.prefab.ModRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.util.IStringSerializable;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nullable;

/**
 * This is the compressed Obsidian block class.
 *
 * @author WuestMan
 */
public class BlockCompressedObsidian extends Block {
	public final EnumType typeofStone;

	/**
	 * Initializes a new instance of the BlockCompressedObsidian class.
	 */
	public BlockCompressedObsidian(EnumType stoneType) {
		super(Block.Properties.create(Material.ROCK)
				.hardnessAndResistance(50.0f, 2000.0f)
				.sound(SoundType.STONE));

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
	 */
	@SuppressWarnings("NullableProblems")
	public enum EnumType implements IStringSerializable {
		COMPRESSED_OBSIDIAN(0, "block_compressed_obsidian", "block_compressed_obsidian"),
		DOUBLE_COMPRESSED_OBSIDIAN(1, "block_double_compressed_obsidian", "block_double_compressed_obsidian"),
		;

		/**
		 * Array of the Block's BlockStates
		 */
		private static final BlockCompressedObsidian.EnumType[] META_LOOKUP = new BlockCompressedObsidian.EnumType[values().length];

		static {
			for (BlockCompressedObsidian.EnumType type : values()) {
				META_LOOKUP[type.getMetadata()] = type;
			}
		}

		private final int meta;
		/**
		 * The EnumType's name.
		 */
		private final String name;
		private final String unlocalizedName;

		EnumType(int meta, String name, String unlocalizedName) {
			this.meta = meta;
			this.name = name;
			this.unlocalizedName = unlocalizedName;
		}

		/**
		 * The EnumType's meta data value.
		 *
		 * @return the meta data for this block.
		 */
		public int getMetadata() {
			return this.meta;
		}

		/**
		 * Gets the name of this enum value.
		 */
		public String toString() {
			return this.name;
		}

		@Override
		public String getName() {
			return this.name;
		}
	}
}
