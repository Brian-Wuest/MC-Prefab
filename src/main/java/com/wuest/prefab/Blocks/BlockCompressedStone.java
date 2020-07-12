package com.wuest.prefab.Blocks;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.util.IStringSerializable;

/**
 * Provides a way to store large amounts of stone.
 *
 * @author WuestMan
 */
public class BlockCompressedStone extends Block {
	public final EnumType typeofStone;

	/**
	 * Initializes a new instance of the CompressedStone class.
	 */
	public BlockCompressedStone(EnumType typeOfStone) {
		super(Properties.create(Material.EARTH)
				.hardnessAndResistance(1.5F, 10.0F)
				.sound(SoundType.STONE)
				.setLightLevel(value -> typeOfStone == EnumType.COMPRESSED_GLOWSTONE || typeOfStone == EnumType.DOUBLE_COMPRESSED_GLOWSTONE ? 15 : 0)
				.harvestLevel(0)
				.harvestTool(null));

		this.typeofStone = typeOfStone;
	}

	/**
	 * An enum which contains the various types of block variants.
	 *
	 * @author WuestMan
	 */
	@SuppressWarnings({"NullableProblems", "SpellCheckingInspection"})
	public enum EnumType implements IStringSerializable {
		COMPRESSED_STONE(0, "block_compressed_stone", "block_compressed_stone"),
		DOUBLE_COMPRESSED_STONE(1, "block_double_compressed_stone", "block_double_compressed_stone"),
		TRIPLE_COMPRESSED_STONE(2, "block_triple_compressed_stone", "block_triple_compressed_stone"),
		COMPRESSED_GLOWSTONE(3, "block_compressed_glowstone", "block_compressed_glowstone"),
		DOUBLE_COMPRESSED_GLOWSTONE(4, "block_double_compressed_glowstone", "block_double_compressed_glowstone"),
		COMPRESSED_DIRT(5, "block_compressed_dirt", "block_compressed_dirt"),
		DOUBLE_COMPRESSED_DIRT(6, "block_double_compressed_dirt", "block_double_compressed_dirt");

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
		public String getString() {
			return this.name;
		}

		/**
		 * The unlocalized name of this EnumType.
		 *
		 * @return A string containing the unlocalized name.
		 */
		public String getUnlocalizedName() {
			return this.unlocalizedName;
		}
	}
}
