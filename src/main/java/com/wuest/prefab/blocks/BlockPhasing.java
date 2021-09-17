package com.wuest.prefab.blocks;

import com.wuest.prefab.Prefab;
import com.wuest.prefab.events.ModEventHandler;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.core.Direction;
import net.minecraft.util.StringRepresentable;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Random;

/**
 * This is a phasing block.
 *
 * @author WuestMan
 */
@SuppressWarnings({"SpellCheckingInspection", "NullableProblems"})
public class BlockPhasing extends Block {
    /**
     * The phasing progress property.
     */
    private static final EnumProperty<EnumPhasingProgress> Phasing_Progress = EnumProperty.create("phasing_progress", EnumPhasingProgress.class);

    /**
     * The phasing out block property.
     */
    private static final BooleanProperty Phasing_Out = BooleanProperty.create("phasing_out");

    /**
     * The tick rage for this block.
     */
    private int tickRate = 2;

    /**
     * Initializes a new instance of the BlockPhasing class.
     */
    public BlockPhasing() {
        super(Properties.of(Prefab.SeeThroughImmovable)
                .sound(SoundType.STONE)
                .strength(0.6f)
                .noOcclusion());

        this.registerDefaultState(this.getStateDefinition().any().setValue(Phasing_Out, false).setValue(Phasing_Progress, EnumPhasingProgress.base));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(BlockPhasing.Phasing_Out, BlockPhasing.Phasing_Progress);
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult rayTrace) {
        if (!world.isClientSide()) {
            EnumPhasingProgress progress = state.getValue(Phasing_Progress);

            if (progress == EnumPhasingProgress.base) {
                // Only trigger the phasing when this block is not currently phasing.
                world.getBlockTicks().scheduleTick(pos, this, this.tickRate);
            }
        }

        return InteractionResult.SUCCESS;
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
            this.updateNeighborPhasicBlocks(true, context.getLevel(), context.getClickedPos(), this.defaultBlockState(), false, false);
        }

        return this.defaultBlockState().setValue(Phasing_Out, poweredSide).setValue(Phasing_Progress, EnumPhasingProgress.base);
    }

    /**
     * Called serverside after this block is replaced with another in Chunk, but before the Tile Entity is updated
     */
    @Override
    public boolean removedByPlayer(BlockState state, Level world, BlockPos pos, Player player, boolean willHarvest, FluidState fluid) {
        EnumPhasingProgress currentState = state.getValue(Phasing_Progress);

        boolean returnValue = super.removedByPlayer(state, world, pos, player, willHarvest, fluid);

        ModEventHandler.RedstoneAffectedBlockPositions.remove(pos);

        boolean poweredSide = world.hasNeighborSignal(pos);

        if (poweredSide && currentState == EnumPhasingProgress.transparent) {
            // Set this block and all neighbor Phasic Blocks to base. This will cascade to tall touching Phasic blocks.
            this.updateNeighborPhasicBlocks(false, world, pos, state, false, false);
        }

        return returnValue;
    }

    /**
     * Called when a neighboring block was changed and marks that this state should perform any checks during a neighbor
     * change. Cases may include when redstone power is updated, cactus blocks popping off due to a neighboring solid
     * block, etc.
     */
    @Override
    public void neighborChanged(BlockState state, Level worldIn, BlockPos pos, Block blockIn, BlockPos p_189540_5_, boolean p_220069_6_) {
        if (!worldIn.isClientSide()) {
            // Only worry about powering blocks.
            if (blockIn.defaultBlockState().isSignalSource()) {
                boolean poweredSide = worldIn.hasNeighborSignal(pos);
                EnumPhasingProgress currentState = state.getValue(Phasing_Progress);
                boolean setToTransparent = false;

                if (poweredSide && currentState == EnumPhasingProgress.base) {
                    setToTransparent = true;
                }

                if (currentState == EnumPhasingProgress.base || currentState == EnumPhasingProgress.transparent) {
                    this.updateNeighborPhasicBlocks(setToTransparent, worldIn, pos, state, true, true);
                }
            }
        }
    }

    @Override
    public void tick(BlockState state, ServerLevel worldIn, BlockPos pos, Random random) {
        int tickDelay = this.tickRate;

        if (ModEventHandler.RedstoneAffectedBlockPositions.contains(pos)) {
            return;
        }

        EnumPhasingProgress progress = state.getValue(Phasing_Progress);
        boolean phasingOut = state.getValue(Phasing_Out);

        // If the state is at base, progress trigger the phasing out to the neighboring blocks.
        if (progress == EnumPhasingProgress.base) {
            for (Direction facing : Direction.values()) {
                Block currentBlock = worldIn.getBlockState(pos.relative(facing)).getBlock();

                if (currentBlock instanceof BlockPhasing && !ModEventHandler.RedstoneAffectedBlockPositions.contains(pos.relative(facing))) {
                    worldIn.getBlockTicks().scheduleTick(pos.relative(facing), currentBlock, tickDelay);
                }
            }

            phasingOut = true;
        }

        int updatedMeta = progress.getMeta();

        if (updatedMeta == EnumPhasingProgress.eighty_percent.getMeta()
                && phasingOut) {
            // This next phase should take 100 ticks (5 seconds) since this is the phase out.
            tickDelay = 100;
        }

        if (updatedMeta == EnumPhasingProgress.transparent.getMeta()
                && phasingOut) {
            // set the phasing to in.
            phasingOut = false;
        }

        if (updatedMeta == EnumPhasingProgress.twenty_percent.getMeta()
                && !phasingOut) {
            // Phasing in for this delay, set the tick delay to -1 and stop the phasing. Reset the phasing out property.
            tickDelay = -1;
        }

        updatedMeta = phasingOut ? updatedMeta + 2 : updatedMeta - 2;
        progress = EnumPhasingProgress.ValueOf(updatedMeta);

        // Update the state in the world, update the world and (possibly) schedule a state update.
        state = state.setValue(Phasing_Out, phasingOut).setValue(Phasing_Progress, progress);
        worldIn.setBlock(pos, state, 3);

        /*if (worldIn.isRemote) {
            ClientLevelclientLevel= (ClientWorld) worldIn;
            clientWorld.markSurroundingsForRerender(pos.getX(), pos.getY(), pos.getZ());
        }*/

        if (tickDelay > 0) {
            worldIn.getBlockTicks().scheduleTick(pos, this, tickDelay);
        }
    }

    /**
     * The type of render function called. MODEL for mixed tesr and static model, MODELBLOCK_ANIMATED for TESR-only,
     * LIQUID for vanilla liquids, INVISIBLE to skip all rendering
     */
    @Override
    public RenderShape getRenderShape(BlockState state) {
        EnumPhasingProgress progress = state.getValue(Phasing_Progress);
        return progress != EnumPhasingProgress.transparent ? RenderShape.MODEL : RenderShape.INVISIBLE;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        EnumPhasingProgress progress = state.getValue(Phasing_Progress);

        return progress != EnumPhasingProgress.transparent ? Shapes.block() : Shapes.empty();
    }

    @Nullable
    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        EnumPhasingProgress progress = state.getValue(Phasing_Progress);

        if (progress == EnumPhasingProgress.transparent) {
            return Shapes.empty();
        }

        return super.getCollisionShape(state, worldIn, pos, context);
    }

    @Override
    public VoxelShape getInteractionShape(BlockState state, BlockGetter worldIn, BlockPos pos) {
        EnumPhasingProgress progress = state.getValue(Phasing_Progress);

        if (progress == EnumPhasingProgress.transparent) {
            return Shapes.empty();
        }

        VoxelShape aabb = super.getInteractionShape(state, worldIn, pos);

        return aabb;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public boolean skipRendering(BlockState state, BlockState adjacentBlockState, Direction side) {
        EnumPhasingProgress progress = state.getValue(Phasing_Progress);

        return progress == EnumPhasingProgress.transparent;
    }

    protected void updateNeighborPhasicBlocks(boolean setToTransparent, Level worldIn, BlockPos pos, BlockState phasicBlockState, boolean setCurrentBlock,
                                              boolean triggeredByRedstone) {
        ArrayList<BlockPos> blocksToUpdate = new ArrayList<BlockPos>();
        BlockState updatedBlockState = phasicBlockState
                .setValue(Phasing_Out, setToTransparent)
                .setValue(Phasing_Progress, setToTransparent ? EnumPhasingProgress.transparent : EnumPhasingProgress.base);

        // Set this block and all neighbor Phasic Blocks to transparent. This will cascade to all touching Phasic
        // blocks.
        this.findNeighborPhasicBlocks(worldIn, pos, updatedBlockState, 0, blocksToUpdate, setCurrentBlock);

        for (BlockPos positionToUpdate : blocksToUpdate) {
            worldIn.setBlock(positionToUpdate, updatedBlockState, 3);

            if (triggeredByRedstone) {
                if (ModEventHandler.RedstoneAffectedBlockPositions.contains(positionToUpdate) && !setToTransparent) {
                    ModEventHandler.RedstoneAffectedBlockPositions.remove(positionToUpdate);
                } else if (!ModEventHandler.RedstoneAffectedBlockPositions.contains(positionToUpdate) && setToTransparent) {
                    ModEventHandler.RedstoneAffectedBlockPositions.add(positionToUpdate);
                }
            }
        }
    }

    /**
     * Sets the powered status and updates the block's neighbor.
     *
     * @param worldIn           The world where the block resides.
     * @param pos               The position of the block.
     * @param desiredBlockState The current state of the block at the position.
     * @param cascadeCount      The number of times it has cascaded.
     * @param cascadedBlockPos  The list of cascaded block positions, this is used to determine if this block should
     *                          be processed again.
     * @param setCurrentBlock   Determines if the current block should be set.
     */
    private int findNeighborPhasicBlocks(Level worldIn, BlockPos pos, BlockState desiredBlockState, int cascadeCount,
                                         ArrayList<BlockPos> cascadedBlockPos, boolean setCurrentBlock) {
        cascadeCount++;

        if (cascadeCount > 100) {
            return cascadeCount;
        }

        if (setCurrentBlock) {
            cascadedBlockPos.add(pos);
        }

        for (Direction facing : Direction.values()) {
            Block neighborBlock = worldIn.getBlockState(pos.relative(facing)).getBlock();

            if (neighborBlock instanceof BlockPhasing) {
                BlockState blockState = worldIn.getBlockState(pos.relative(facing));

                // If the block is already in the correct state or was already checked, there is no need to cascade to
                // it's neighbors.
                EnumPhasingProgress progress = blockState.getValue(Phasing_Progress);

                if (cascadedBlockPos.contains(pos.relative(facing)) || progress == desiredBlockState.getValue(Phasing_Progress)) {
                    continue;
                }

                setCurrentBlock = true;
                cascadeCount = this.findNeighborPhasicBlocks(worldIn, pos.relative(facing), desiredBlockState, cascadeCount, cascadedBlockPos, setCurrentBlock);

                if (cascadeCount > 100) {
                    break;
                }
            }
        }

        return cascadeCount;
    }

    /**
     * The enum used to determine the meta data for this block.
     *
     * @author WuestMan
     */
    public enum EnumPhasingProgress implements StringRepresentable {
        base(0, "base"),
        twenty_percent(2, "twenty_percent"),
        forty_percent(4, "forty_percent"),
        sixty_percent(6, "sixty_percent"),
        eighty_percent(8, "eighty_percent"),
        transparent(10, "transparent");

        private int meta;
        private String name;

        EnumPhasingProgress(int meta, String name) {
            this.meta = meta;
            this.name = name;
        }

        /**
         * Gets a instance based on the meta data.
         *
         * @param meta The meta data value to get the enum value for.
         * @return An instance of the enum.
         */
        public static EnumPhasingProgress ValueOf(int meta) {
            for (EnumPhasingProgress progress : EnumPhasingProgress.values()) {
                if (progress.meta == meta) {
                    return progress;
                }
            }

            return EnumPhasingProgress.base;
        }

        public int getMeta() {
            return this.meta;
        }

        @Override
        public String getSerializedName() {
            return this.name;
        }
    }
}
