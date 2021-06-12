package com.wuest.prefab.blocks;

import net.minecraft.block.*;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathType;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.state.properties.SlabType;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ITag;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

@SuppressWarnings("NullableProblems")
public class BlockGlassSlab extends GlassBlock implements IWaterLoggable {

    private static final VoxelShape BOTTOM_SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D);
    private static final VoxelShape TOP_SHAPE = Block.box(0.0D, 8.0D, 0.0D, 16.0D, 16.0D, 16.0D);
    private static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public BlockGlassSlab(Block.Properties properties) {
        super(properties);
        this.registerDefaultState(this.getStateDefinition().any().setValue(SlabBlock.TYPE, SlabType.BOTTOM).setValue(WATERLOGGED, Boolean.FALSE));
    }

    // This is basically the "isTransparent" function.
    @Override
    public boolean useShapeForLightOcclusion(BlockState state) {
        return state.getValue(SlabBlock.TYPE) != SlabType.DOUBLE;
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(SlabBlock.TYPE, WATERLOGGED);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean skipRendering(BlockState state, BlockState adjacentBlockState, Direction side) {
        ITag<Block> tags = BlockTags.getAllTags().getTag(new ResourceLocation("forge", "glass"));
        Block adjacentBlock = adjacentBlockState.getBlock();

		/*
			Hide this side under the following conditions
			1. The other block is a "Glass" block (this includes colored glass).
			2. This block and the other block has a matching type.
			3. The other block is a double slab and this is a single slab.
		*/
        return tags.contains(adjacentBlock) || (adjacentBlock == this
                && (adjacentBlockState.getValue(SlabBlock.TYPE) == state.getValue(SlabBlock.TYPE)
                || (adjacentBlockState.getValue(SlabBlock.TYPE) == SlabType.DOUBLE
                && state.getValue(SlabBlock.TYPE) != SlabType.DOUBLE)));
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        SlabType slabtype = state.getValue(SlabBlock.TYPE);
        switch (slabtype) {
            case DOUBLE:
                return VoxelShapes.block();
            case TOP:
                return BlockGlassSlab.TOP_SHAPE;
            default:
                return BlockGlassSlab.BOTTOM_SHAPE;
        }
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        BlockPos blockpos = context.getClickedPos();
        BlockState blockstate = context.getLevel().getBlockState(blockpos);
        if (blockstate.getBlock() == this) {
            boolean wasWaterlogged = blockstate.getValue(WATERLOGGED);
            return blockstate.setValue(SlabBlock.TYPE, SlabType.DOUBLE).setValue(WATERLOGGED, wasWaterlogged);
        } else {
            FluidState ifluidstate = context.getLevel().getFluidState(blockpos);
            BlockState blockstate1 = this.defaultBlockState().setValue(SlabBlock.TYPE, SlabType.BOTTOM).setValue(WATERLOGGED, ifluidstate.getType() == Fluids.WATER);
            Direction direction = context.getClickedFace();
            return direction != Direction.DOWN && (direction == Direction.UP || !(context.getClickLocation().y - (double) blockpos.getY() > 0.5D)) ? blockstate1 : blockstate1.setValue(SlabBlock.TYPE, SlabType.TOP);
        }
    }

    @Override
    public boolean canBeReplaced(BlockState state, BlockItemUseContext useContext) {
        ItemStack itemstack = useContext.getItemInHand();
        SlabType slabtype = state.getValue(SlabBlock.TYPE);
        if (slabtype != SlabType.DOUBLE && itemstack.getItem() == this.asItem()) {
            if (useContext.replacingClickedOnBlock()) {
                boolean flag = useContext.getClickLocation().y - (double) useContext.getClickedPos().getY() > 0.5D;
                Direction direction = useContext.getClickedFace();
                if (slabtype == SlabType.BOTTOM) {
                    return direction == Direction.UP || flag && direction.getAxis().isHorizontal();
                } else {
                    return direction == Direction.DOWN || !flag && direction.getAxis().isHorizontal();
                }
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    public boolean placeLiquid(IWorld worldIn, BlockPos pos, BlockState state, FluidState fluidStateIn) {
        return state.getValue(SlabBlock.TYPE) != SlabType.DOUBLE && this.slabReceiveFluid(worldIn, pos, state, fluidStateIn);
    }

    @Override
    public boolean canPlaceLiquid(IBlockReader worldIn, BlockPos pos, BlockState state, Fluid fluidIn) {
        return state.getValue(SlabBlock.TYPE) != SlabType.DOUBLE && this.slabCanContainFluid(worldIn, pos, state, fluidIn);
    }

    private boolean slabCanContainFluid(IBlockReader worldIn, BlockPos pos, BlockState state, Fluid fluidIn) {
        return !state.getValue(BlockStateProperties.WATERLOGGED) && fluidIn == Fluids.WATER;
    }

    private boolean slabReceiveFluid(IWorld worldIn, BlockPos pos, BlockState state, FluidState fluidStateIn) {
        if (!state.getValue(BlockStateProperties.WATERLOGGED) && fluidStateIn.getType() == Fluids.WATER) {
            if (!worldIn.isClientSide()) {
                worldIn.setBlock(pos, state.setValue(BlockStateProperties.WATERLOGGED, Boolean.TRUE), 3);
                worldIn.getLiquidTicks().scheduleTick(pos, fluidStateIn.getType(), fluidStateIn.getType().getTickDelay(worldIn));
            }

            return true;
        } else {
            return false;
        }
    }

    @Override
    public Fluid takeLiquid(IWorld worldIn, BlockPos pos, BlockState state) {
        if (state.getValue(BlockStateProperties.WATERLOGGED) && state.getValue(SlabBlock.TYPE) != SlabType.DOUBLE) {
            worldIn.setBlock(pos, state.setValue(BlockStateProperties.WATERLOGGED, Boolean.FALSE), 3);
            return Fluids.WATER;
        } else {
            return Fluids.EMPTY;
        }
    }

    /**
     * Update the provided state given the provided neighbor facing and neighbor state, returning a new state.
     * For example, fences make their connections to the passed in state if possible, and wet concrete powder immediately
     * returns its solidified counterpart.
     * Note that this method should ideally consider only the specific face passed in.
     */
    @Override
    public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
        if (stateIn.getValue(WATERLOGGED)) {
            worldIn.getLiquidTicks().scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(worldIn));
        }

        return super.updateShape(stateIn, facing, facingState, worldIn, currentPos, facingPos);
    }

    @Override
    public boolean isPathfindable(BlockState state, IBlockReader worldIn, BlockPos pos, PathType type) {
        if (type == PathType.WATER) {
            return worldIn.getFluidState(pos).is(FluidTags.WATER);
        }

        return false;
    }
}
