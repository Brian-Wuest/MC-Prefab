package com.wuest.prefab.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BlockShaped extends Block {
    private final BlockShape shape;

    public BlockShaped(BlockShape shape, Properties properties) {
        super(properties);

        this.shape = shape;
    }

    @Override
    public VoxelShape getShape(BlockState p_220053_1_, BlockGetter p_220053_2_, BlockPos p_220053_3_, CollisionContext p_220053_4_) {
        return this.shape.getShape();
    }

    public enum BlockShape {

        PileOfBricks(Block.box(3.0D, 0.0D, 3.0D, 13.0D, 5.0D, 12.0D)),
        PalletOfBricks(Block.box(1.0D, 0.0D, 0.0D, 15.0D, 15.0D, 16.0D)),
        BundleOfTimber(Block.box(0.0D, 0.0D, 0.0D, 15.0D, 4.0D, 15.0D)),
        HeapOfTimber(Block.box(3.0D, 0.0D, 2.0D, 13.0D, 6.0D, 14.0D)),
        TonOfTimber(Block.box(1.0D, 0.0D, 2.0D, 14.0D, 9.0D, 14.0D));

        private final VoxelShape shape;

        BlockShape(VoxelShape shape) {
            this.shape = shape;
        }

        public VoxelShape getShape() {
            return this.shape;
        }
    }
}
