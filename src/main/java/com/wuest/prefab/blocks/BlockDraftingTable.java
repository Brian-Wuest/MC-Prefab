package com.wuest.prefab.blocks;

import com.wuest.prefab.Prefab;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BlockDraftingTable extends HorizontalDirectionalBlock {
    public static VoxelShape SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 15.0D, 16.0D);
    public static VoxelShape SHAPE_NORTH = Block.box(1.0D, 0.0D, 2.0D, 15.0D, 13.0D, 15.0D);
    public static VoxelShape SHAPE_SOUTH = Block.box(1.0D, 0.0D, 1.0D, 15.0D, 13.0D, 14.0D);
    public static VoxelShape SHAPE_EAST = Block.box(1.0D, 0.0D, 1.0D, 14.0D, 13.0D, 15.0D);
    public static VoxelShape SHAPE_WEST = Block.box(2.0D, 0.0D, 1.0D, 15.0D, 13.0D, 15.0D);

    /**
     * Initializes a new instance of the BlockPaperLantern class.
     */
    public BlockDraftingTable() {
        super(BlockBehaviour.Properties.of(Prefab.SeeThroughImmovable)
                .sound(SoundType.WOOD)
                .strength(0.6f)
                .noOcclusion());

        this.registerDefaultState(this.stateDefinition.any().setValue(HorizontalDirectionalBlock.FACING, Direction.NORTH));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(HorizontalDirectionalBlock.FACING);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(HorizontalDirectionalBlock.FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public RenderShape getRenderShape(BlockState blockState) {
        return RenderShape.MODEL;
    }

    @Override
    public VoxelShape getOcclusionShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        return BlockDraftingTable.SHAPE;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return BlockDraftingTable.SHAPE;
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        switch (blockState.getValue(FACING)) {
            case NORTH: {
                return BlockDraftingTable.SHAPE_NORTH;
            }
            case SOUTH: {
                return BlockDraftingTable.SHAPE_SOUTH;
            }
            case EAST: {
                return BlockDraftingTable.SHAPE_EAST;
            }
            case WEST: {
                return BlockDraftingTable.SHAPE_WEST;
            }
            default: {
                return BlockDraftingTable.SHAPE;
            }
        }
    }
}
