package com.wuest.prefab.blocks;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;
import net.minecraft.util.StringRepresentable;

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
        super(Properties.of(Material.DECORATION)
                .strength(1.5F, 10.0F)
                .sound(typeOfStone.getSoundType())
                .lightLevel(value -> typeOfStone == EnumType.COMPRESSED_GLOWSTONE || typeOfStone == EnumType.DOUBLE_COMPRESSED_GLOWSTONE ? 15 : 0));

        this.typeofStone = typeOfStone;
    }

    /**
     * An enum which contains the various types of block variants.
     *
     * @author WuestMan
     */
    @SuppressWarnings({"NullableProblems", "SpellCheckingInspection"})
    public enum EnumType implements StringRepresentable {
        COMPRESSED_STONE(0, "block_compressed_stone", "block_compressed_stone", SoundType.STONE),
        DOUBLE_COMPRESSED_STONE(1, "block_double_compressed_stone", "block_double_compressed_stone", SoundType.STONE),
        TRIPLE_COMPRESSED_STONE(2, "block_triple_compressed_stone", "block_triple_compressed_stone", SoundType.STONE),
        COMPRESSED_GLOWSTONE(3, "block_compressed_glowstone", "block_compressed_glowstone", SoundType.GLASS),
        DOUBLE_COMPRESSED_GLOWSTONE(4, "block_double_compressed_glowstone", "block_double_compressed_glowstone", SoundType.GLASS),
        COMPRESSED_DIRT(5, "block_compressed_dirt", "block_compressed_dirt", SoundType.GRAVEL),
        DOUBLE_COMPRESSED_DIRT(6, "block_double_compressed_dirt", "block_double_compressed_dirt", SoundType.GRAVEL),
        COMPRESSED_QUARTZCRETE(7, "block_compressed_quartz_crete", "block_compressed_quartz_crete", SoundType.STONE),
        DOUBLE_COMPRESSED_QUARTZCRETE(8, "block_double_compressed_quartz_crete", "block_double_compressed_quartz_crete", SoundType.STONE);

        private final int meta;
        /**
         * The EnumType's name.
         */
        private final String name;
        private final String unlocalizedName;
        private final SoundType soundType;

        EnumType(int meta, String name, String unlocalizedName, SoundType soundType) {
            this.meta = meta;
            this.name = name;
            this.unlocalizedName = unlocalizedName;
            this.soundType = soundType;
        }

        /**
         * The EnumType's meta data value.
         *
         * @return the meta data for this block.
         */
        public int getMetadata() {
            return this.meta;
        }

        public SoundType getSoundType() {
            return this.soundType;
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
