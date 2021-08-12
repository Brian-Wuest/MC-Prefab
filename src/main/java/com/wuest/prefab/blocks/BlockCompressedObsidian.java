package com.wuest.prefab.blocks;

import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;

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
        super(Block.Properties.of(Material.STONE)
                .strength(50.0f, 2000.0f)
                .sound(SoundType.STONE)
                .requiresCorrectToolForDrops());

        this.typeofStone = stoneType;
    }

    /**
     * An enum which contains the various types of block variants.
     *
     * @author WuestMan
     */
    @SuppressWarnings("NullableProblems")
    public enum EnumType implements StringRepresentable {
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
        public String getSerializedName() {
            return this.name;
        }
    }
}
