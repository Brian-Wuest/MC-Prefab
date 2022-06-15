package com.wuest.prefab.blocks;


import com.wuest.prefab.ModRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;

import java.util.Random;

public class BlockCustomWall extends WallBlock implements IGrassSpreadable {
    public EnumType BlockVariant;

    public BlockCustomWall(Block modelBlock, EnumType variant) {
        super(Properties.of(variant.getMaterial())
                .strength(modelBlock.defaultDestroyTime(),
                        modelBlock.getExplosionResistance() * 5.0F / 3.0F)
                .sound(modelBlock.defaultBlockState().getSoundType()));

        this.BlockVariant = variant;
    }

    /**
     * Returns whether or not this block is of a type that needs random ticking.
     * Called for ref-counting purposes by ExtendedBlockStorage in order to broadly
     * cull a chunk from the random chunk update list for efficiency's sake.
     */
    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return this.BlockVariant == EnumType.DIRT;
    }

    @Override
    public void randomTick(BlockState state, ServerLevel worldIn, BlockPos pos, RandomSource random) {
        this.DetermineGrassSpread(state, worldIn, pos, random);
    }

    @Override
    public BlockState getGrassBlockState(BlockState originalState) {
        return ModRegistry.GrassWall.get().defaultBlockState()
                .setValue(WallBlock.EAST_WALL, originalState.getValue(WallBlock.EAST_WALL))
                .setValue(WallBlock.WEST_WALL, originalState.getValue(WallBlock.WEST_WALL))
                .setValue(WallBlock.NORTH_WALL, originalState.getValue(WallBlock.NORTH_WALL))
                .setValue(WallBlock.SOUTH_WALL, originalState.getValue(WallBlock.SOUTH_WALL))
                .setValue(WallBlock.WATERLOGGED, originalState.getValue(WallBlock.WATERLOGGED))
                .setValue(WallBlock.UP, originalState.getValue(WallBlock.UP));
    }

    public enum EnumType implements StringRepresentable {
        DIRT(0, "block_dirt_wall", "block_dirt_wall", Material.DIRT),
        GRASS(1, "block_grass_wall", "block_grass_wall", Material.DIRT);

        private static final EnumType[] META_LOOKUP = new EnumType[values().length];

        static {
            for (EnumType customwall$enumtype : values()) {
                META_LOOKUP[customwall$enumtype.getMetadata()] = customwall$enumtype;
            }
        }

        private final int meta;
        private final String name;
        private String unlocalizedName;
        private Material material;

        EnumType(int meta, String name, String unlocalizedName, Material blockMaterial) {
            this.meta = meta;
            this.name = name;
            this.unlocalizedName = unlocalizedName;
            this.material = blockMaterial;
        }

        public static EnumType byMetadata(int meta) {
            if (meta < 0 || meta >= META_LOOKUP.length) {
                meta = 0;
            }

            return META_LOOKUP[meta];
        }

        public int getMetadata() {
            return this.meta;
        }

        public String toString() {
            return this.name;
        }

        @Override
        public String getSerializedName() {
            return this.name;
        }

        public Material getMaterial() {
            return this.material;
        }

        public String getUnlocalizedName() {
            return this.unlocalizedName;
        }
    }
}