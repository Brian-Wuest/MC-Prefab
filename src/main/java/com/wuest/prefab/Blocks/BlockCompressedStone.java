package com.wuest.prefab.Blocks;

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

import javax.annotation.Nullable;
import java.util.List;

/**
 * Provides a way to store large amounts of stone.
 *
 * @author WuestMan
 */
public class BlockCompressedStone extends Block {
    public final EnumType typeofStone;

    public final ItemGroup itemGroup;

    /**
     * Initializes a new instance of the CompressedStone class.
     */
    public BlockCompressedStone(EnumType typeOfStone) {
        super(Block.Properties.create(Material.ROCK)
                .hardnessAndResistance(1.5F, 10.0F)
                .sound(SoundType.STONE)
                .lightValue(typeOfStone == EnumType.COMPRESSED_GLOWSTONE || typeOfStone == EnumType.DOUBLE_COMPRESSED_GLOWSTONE ? 15 : 0));

        this.itemGroup = ItemGroup.BUILDING_BLOCKS;
        ModRegistry.setBlockName(this, typeOfStone.unlocalizedName);
        this.typeofStone = typeOfStone;
    }

    @Nullable
    @Override
    public ToolType getHarvestTool(BlockState state) {
        return null;
    }

    @Override
    public int getHarvestLevel(BlockState state) {
        return 0;
    }

    /**
     * An enum which contains the various types of block variants.
     *
     * @author WuestMan
     */
    public enum EnumType implements IStringSerializable {
        COMPRESSED_STONE(0, "block_compressed_stone", "block_compressed_stone"),
        DOUBLE_COMPRESSED_STONE(1, "block_double_compressed_stone", "block_double_compressed_stone"),
        TRIPLE_COMPRESSED_STONE(2, "block_triple_compressed_stone", "block_triple_compressed_stone"),
        COMPRESSED_GLOWSTONE(3, "block_compressed_glowstone", "block_compressed_glowstone"),
        DOUBLE_COMPRESSED_GLOWSTONE(4, "block_double_compressed_glowstone", "block_double_compressed_glowstone"),
        COMPRESSED_DIRT(5, "block_compressed_dirt", "block_compressed_dirt"),
        DOUBLE_COMPRESSED_DIRT(6, "block_double_compressed_dirt", "block_double_compressed_dirt");

        /**
         * Array of the Block's BlockStates
         */
        private static final BlockCompressedStone.EnumType[] META_LOOKUP = new BlockCompressedStone.EnumType[values().length];

        static {
            for (BlockCompressedStone.EnumType type : values()) {
                META_LOOKUP[type.getMetadata()] = type;
            }
        }

        private final int meta;
        /**
         * The EnumType's name.
         */
        private final String name;
        private final String unlocalizedName;

        EnumType(int meta, String name) {
            this(meta, name, name);
        }

        EnumType(int meta, String name, String unlocalizedName) {
            this.meta = meta;
            this.name = name;
            this.unlocalizedName = unlocalizedName;
        }

        /**
         * A list of resource locations for the names.
         *
         * @return A list of resource locations for the numerous types in this enum.
         */
        public static ResourceLocation[] GetNames() {
            List<ResourceLocation> list = Lists.newArrayList();

            for (EnumType type : EnumType.values()) {
                list.add(new ResourceLocation("prefab", type.unlocalizedName));
            }

            return list.toArray(new ResourceLocation[list.size()]);
        }

        /**
         * Returns an EnumType for the BlockState from a metadata value.
         *
         * @param meta The meta data value to equate to a {@link BlockCompressedObsidian.EnumType}
         * @return If the meta data is invalid the default will be used, otherwise the EnumType found.
         */
        public static BlockCompressedStone.EnumType byMetadata(int meta) {
            if (meta < 0 || meta >= META_LOOKUP.length) {
                meta = 0;
            }

            return META_LOOKUP[meta];
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
