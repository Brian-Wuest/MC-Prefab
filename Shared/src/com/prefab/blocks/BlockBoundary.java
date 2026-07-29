package com.prefab.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.EmptyBlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.redstone.Orientation;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

/**
 * @author WuestMan
 */
@SuppressWarnings({"NullableProblems", "WeakerAccess"})
public class BlockBoundary extends Block {
    /**
     * The powered meta data property.
     */
    public static final BooleanProperty Powered = BooleanProperty.create("powered");

    public static final String BlockName = "block_boundary";

    /**
     * Initializes a new instance of the BlockBoundary class.
     */
    public BlockBoundary(BlockBehaviour.Properties properties) {
        super(properties.sound(SoundType.STONE)
                        .strength(0.6F));

        this.registerDefaultState(this.getStateDefinition().any().setValue(Powered, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(BlockBoundary.Powered);
    }

    /**
     * The type of render function called. MODEL for mixed tesr and static model, MODELBLOCK_ANIMATED for TESR-only,
     * LIQUID for vanilla liquids, INVISIBLE to skip all rendering
     */
    @Override
    public RenderShape getRenderShape(BlockState state) {
        boolean powered = state.getValue(Powered);
        return powered ? RenderShape.MODEL : RenderShape.INVISIBLE;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        boolean powered = state.getValue(Powered);

        return powered ? Shapes.block() : Shapes.empty();
    }

    /**
     * Called when a player removes a block. This is responsible for actually destroying the block, and the block is
     * intact at time of call. This is called regardless of whether the player can harvest the block or not.
     * <p>
     * Return true if the block is actually destroyed.
     * <p>
     * Note: When used in multi-player, this is called on both client and server sides!
     *
     * @param state  The current state.
     * @param world  The current world
     * @param player The player damaging the block, may be null
     * @param pos    Block position in world
     * @return
     */
    @Override
    public BlockState playerWillDestroy(Level world, BlockPos pos, BlockState state, Player player) {
        super.playerWillDestroy(world, pos, state, player);

        boolean poweredSide = world.hasNeighborSignal(pos);

        if (poweredSide) {
            this.setNeighborGlassBlocksPoweredStatus(world, pos, false, 0, new ArrayList<>(), false);
        }
        return state;
    }

    /**
     * Gets the {@link BlockState} to place
     *
     * @param context The {@link BlockPlaceContext}.
     * @return The state to be placed in the world
     */
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        /*
         * Called by ItemBlocks just before a block is actually set in the world, to allow for adjustments to the
         * BlockState
         */
        boolean poweredSide = context.getLevel().hasNeighborSignal(context.getClickedPos());

        if (poweredSide) {
            this.setNeighborGlassBlocksPoweredStatus(context.getLevel(), context.getClickedPos(), true, 0, new ArrayList<>(), false);
        }

        return this.defaultBlockState().setValue(Powered, poweredSide);
    }

    /**
     * Called when a neighboring block was changed and marks that this state should perform any checks during a neighbor
     * change. Cases may include when red-stone power is updated, cactus blocks popping off due to a neighboring solid
     * block, etc.
     */
    @Override
    public void neighborChanged(BlockState blockState, Level level, BlockPos blockPos, Block block,
                                @Nullable Orientation orientation, boolean bl) {
        if (!level.isClientSide()) {
            // Only worry about powering blocks.
            if (block.defaultBlockState().isSignalSource()) {
                boolean poweredSide = level.hasNeighborSignal(blockPos);

                this.setNeighborGlassBlocksPoweredStatus(level, blockPos, poweredSide, 0, new ArrayList<>(), true);
            }
        }
    }

    @Override
    public int getLightBlock(BlockState blockState) {
        boolean powered = blockState.getValue(Powered);

        if (powered && blockState.isSolidRender()) {
            return 15;
        } else {
            return blockState.propagatesSkylightDown() ? 0 : 1;
        }
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state) {
        boolean powered = state.getValue(Powered);

        return !powered || (!Block.isShapeFullBlock(state.getShape(EmptyBlockGetter.INSTANCE, BlockPos.ZERO))
                && state.getFluidState().isEmpty());
    }

    /**
     * Get's the collision shape.
     *
     * @param state   The block state.
     * @param worldIn The world object.
     * @param pos     The block Position.
     * @param context The selection context.
     * @return Returns a shape.
     */
    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return Shapes.block();
    }

    @Deprecated
    @Override
    public VoxelShape getInteractionShape(BlockState state, BlockGetter worldIn, BlockPos pos) {
        if (!state.getValue(Powered)) {
            return Shapes.empty();
        } else {
            return Shapes.block();
        }
    }

    @Override
    public float getShadeBrightness(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        if (!blockState.getValue(Powered)) {
            return 1.0F;
        } else {
            return 0.2F;
        }
    }

    /**
     * Sets the neighbor powered status
     *
     * @param world            The world where the block resides.
     * @param pos              The position of the block.
     * @param isPowered        Determines if the block is powered.
     * @param cascadeCount     How many times this has been cascaded.
     * @param cascadedBlockPos All of the block positions which have been cascaded too.
     * @param setCurrentBlock  Determines if the current block should be set.
     */
    protected void setNeighborGlassBlocksPoweredStatus(Level world, BlockPos pos, boolean isPowered, int cascadeCount, ArrayList<BlockPos> cascadedBlockPos,
                                                       boolean setCurrentBlock) {
        cascadeCount++;

        if (cascadeCount > 100) {
            return;
        }

        if (setCurrentBlock) {
            BlockState state = world.getBlockState(pos);
            world.setBlock(pos, state.setValue(Powered, isPowered), 3);
        }

        cascadedBlockPos.add(pos);

        for (Direction facing : Direction.values()) {
            Block neighborBlock = world.getBlockState(pos.relative(facing)).getBlock();

            if (neighborBlock instanceof BlockBoundary) {
                // If the block is already in the correct state, there is no need to cascade to its neighbors.
                if (cascadedBlockPos.contains(pos.relative(facing))) {
                    continue;
                }

                // running this method for the neighbor block will cascade out to its other neighbors until there are
                // no more Phasic blocks around.
                ((BlockBoundary) neighborBlock).setNeighborGlassBlocksPoweredStatus(world, pos.relative(facing), isPowered, cascadeCount, cascadedBlockPos, true);
            }
        }
    }
}
