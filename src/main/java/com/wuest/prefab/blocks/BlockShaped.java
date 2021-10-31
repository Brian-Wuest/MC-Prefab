package com.wuest.prefab.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;

public class BlockShaped extends Block {
    private final BlockShape shape;

    public BlockShaped(BlockShape shape, Properties properties) {
        super(properties);

        this.shape = shape;
    }

    @Override
    public VoxelShape getShape(BlockState p_220053_1_, IBlockReader p_220053_2_, BlockPos p_220053_3_, ISelectionContext p_220053_4_) {
        return this.shape.getShape();
    }

    public enum BlockShape {

        PileOfBricks(Block.box(3.0D, 0.0D, 3.0D, 13.0D, 5.0D, 12.0D)),
        PalletOfBricks(Block.box(1.0D, 0.0D, 0.0D, 15.0D, 15.0D, 16.0D)),
        BundleOfTimber(Block.box(5.0D, 0.0D, 2.0D, 12.0D, 4.0D, 14.0D)),
        HeapOfTimber(Block.box(3.0D, 0.0D, 2.0D, 13.0D, 6.0D, 14.0D)),
        TonOfTimber(Block.box(1.0D, 0.0D, 2.0D, 14.0D, 9.0D, 14.0D));

        private final VoxelShape shape;

        private BlockShape(VoxelShape shape) {
            this.shape = shape;
        }

        public VoxelShape getShape() {
            return this.shape;
        }
    }
}
