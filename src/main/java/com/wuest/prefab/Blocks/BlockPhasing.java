package com.wuest.prefab.Blocks;

import com.wuest.prefab.Events.ModEventHandler;
import com.wuest.prefab.ModRegistry;
import com.wuest.prefab.Prefab;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.IFluidState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
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
     *
     * @param name The name to register the block as.
     */
    public BlockPhasing(String name) {
        super(Properties.create(Prefab.SeeThroughImmovable)
                .sound(SoundType.STONE)
                .hardnessAndResistance(0.6f)
                .func_226896_b_());

        this.setDefaultState(this.stateContainer.getBaseState().with(Phasing_Out, false).with(Phasing_Progress, EnumPhasingProgress.base));

        ModRegistry.setBlockName(this, name);
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(BlockPhasing.Phasing_Out, BlockPhasing.Phasing_Progress);
    }

    // This was the "onBlockActivated" method.
    @Override
    public ActionResultType func_225533_a_(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult rayTrace) {
        if (!world.isRemote) {
            EnumPhasingProgress progress = state.get(Phasing_Progress);

            if (progress == EnumPhasingProgress.base) {
                // Only trigger the phasing when this block is not currently phasing.
                world.getPendingBlockTicks().scheduleTick(pos, this, this.tickRate);
            }
        }

        return ActionResultType.SUCCESS;
    }

    /**
     * Gets the {@link BlockState} to place
     *
     * @param context The {@link BlockItemUseContext}.
     * @return The state to be placed in the world
     */
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        /*
         * Called by ItemBlocks just before a block is actually set in the world, to allow for adjustments to the
         * BlockState
         */
        boolean poweredSide = context.getWorld().isBlockPowered(context.getPos());

        if (poweredSide) {
            this.updateNeighborPhasicBlocks(true, context.getWorld(), context.getPos(), this.getDefaultState(), false, false);
        }

        return this.getDefaultState().with(Phasing_Out, poweredSide).with(Phasing_Progress, EnumPhasingProgress.base);
    }

    /**
     * Called serverside after this block is replaced with another in Chunk, but before the Tile Entity is updated
     */
    @Override
    public boolean removedByPlayer(BlockState state, World world, BlockPos pos, PlayerEntity player, boolean willHarvest, IFluidState fluid) {
        EnumPhasingProgress currentState = state.get(Phasing_Progress);

        boolean returnValue = super.removedByPlayer(state, world, pos, player, willHarvest, fluid);

        ModEventHandler.RedstoneAffectedBlockPositions.remove(pos);

        boolean poweredSide = world.isBlockPowered(pos);

        if (poweredSide && currentState == EnumPhasingProgress.transparent) {
            // Set this block and all neighbor Phasic Blocks to base. This will cascade to tall touching Phasic blocks.
            this.updateNeighborPhasicBlocks(false, world, pos, state, false, false);
        }

        return returnValue;
    }

    /**
     * How many world ticks before ticking
     */
    @Override
    public int tickRate(IWorldReader worldIn) {
        return this.tickRate;
    }

    /**
     * Called when a neighboring block was changed and marks that this state should perform any checks during a neighbor
     * change. Cases may include when redstone power is updated, cactus blocks popping off due to a neighboring solid
     * block, etc.
     */
    @Override
    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos p_189540_5_, boolean p_220069_6_) {
        if (!worldIn.isRemote) {
            // Only worry about powering blocks.
            if (blockIn.getDefaultState().canProvidePower()) {
                boolean poweredSide = worldIn.isBlockPowered(pos);
                EnumPhasingProgress currentState = state.get(Phasing_Progress);
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

    // TODO: This used to be "tick"
    @Override
    public void func_225534_a_(BlockState state, ServerWorld worldIn, BlockPos pos, Random random) {
        int tickDelay = this.tickRate;

        if (ModEventHandler.RedstoneAffectedBlockPositions.contains(pos)) {
            return;
        }

        EnumPhasingProgress progress = state.get(Phasing_Progress);
        boolean phasingOut = state.get(Phasing_Out);

        // If the state is at base, progress trigger the phasing out to the neighboring blocks.
        if (progress == EnumPhasingProgress.base) {
            for (Direction facing : Direction.values()) {
                Block currentBlock = worldIn.getBlockState(pos.offset(facing)).getBlock();

                if (currentBlock instanceof BlockPhasing && !ModEventHandler.RedstoneAffectedBlockPositions.contains(pos.offset(facing))) {
                    worldIn.getPendingBlockTicks().scheduleTick(pos.offset(facing), currentBlock, tickDelay);
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
        state = state.with(Phasing_Out, phasingOut).with(Phasing_Progress, progress);
        worldIn.setBlockState(pos, state);

        /*if (worldIn.isRemote) {
            ClientWorld clientWorld = (ClientWorld) worldIn;
            clientWorld.markSurroundingsForRerender(pos.getX(), pos.getY(), pos.getZ());
        }*/

        if (tickDelay > 0) {
            worldIn.getPendingBlockTicks().scheduleTick(pos, this, tickDelay);
        }
    }

    /**
     * The type of render function called. MODEL for mixed tesr and static model, MODELBLOCK_ANIMATED for TESR-only,
     * LIQUID for vanilla liquids, INVISIBLE to skip all rendering
     */
    @Override
    public BlockRenderType getRenderType(BlockState state) {
        EnumPhasingProgress progress = state.get(Phasing_Progress);
        return progress != EnumPhasingProgress.transparent ? BlockRenderType.MODEL : BlockRenderType.INVISIBLE;
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        EnumPhasingProgress progress = state.get(Phasing_Progress);

        return progress != EnumPhasingProgress.transparent ? VoxelShapes.fullCube() : VoxelShapes.empty();
    }

    @Nullable
    @Override
    public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        EnumPhasingProgress progress = state.get(Phasing_Progress);

        if (progress == EnumPhasingProgress.transparent) {
            return VoxelShapes.empty();
        }

        return super.getCollisionShape(state, worldIn, pos, context);
    }

    @Override
    public VoxelShape getRaytraceShape(BlockState state, IBlockReader worldIn, BlockPos pos) {
        EnumPhasingProgress progress = state.get(Phasing_Progress);

        if (progress == EnumPhasingProgress.transparent) {
            return VoxelShapes.empty();
        }

        VoxelShape aabb = super.getRaytraceShape(state, worldIn, pos);

        return aabb;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public boolean isSideInvisible(BlockState state, BlockState adjacentBlockState, Direction side) {
        EnumPhasingProgress progress = state.get(Phasing_Progress);

        return progress == EnumPhasingProgress.transparent;
    }

    protected void updateNeighborPhasicBlocks(boolean setToTransparent, World worldIn, BlockPos pos, BlockState phasicBlockState, boolean setCurrentBlock,
                                              boolean triggeredByRedstone) {
        ArrayList<BlockPos> blocksToUpdate = new ArrayList<BlockPos>();
        BlockState updatedBlockState = phasicBlockState
                .with(Phasing_Out, setToTransparent)
                .with(Phasing_Progress, setToTransparent ? EnumPhasingProgress.transparent : EnumPhasingProgress.base);

        // Set this block and all neighbor Phasic Blocks to transparent. This will cascade to all touching Phasic
        // blocks.
        this.findNeighborPhasicBlocks(worldIn, pos, updatedBlockState, 0, blocksToUpdate, setCurrentBlock);

        for (BlockPos positionToUpdate : blocksToUpdate) {
            worldIn.setBlockState(positionToUpdate, updatedBlockState);

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
    private int findNeighborPhasicBlocks(World worldIn, BlockPos pos, BlockState desiredBlockState, int cascadeCount,
                                         ArrayList<BlockPos> cascadedBlockPos, boolean setCurrentBlock) {
        cascadeCount++;

        if (cascadeCount > 100) {
            return cascadeCount;
        }

        if (setCurrentBlock) {
            cascadedBlockPos.add(pos);
        }

        for (Direction facing : Direction.values()) {
            Block neighborBlock = worldIn.getBlockState(pos.offset(facing)).getBlock();

            if (neighborBlock instanceof BlockPhasing) {
                BlockState blockState = worldIn.getBlockState(pos.offset(facing));

                // If the block is already in the correct state or was already checked, there is no need to cascade to
                // it's neighbors.
                EnumPhasingProgress progress = blockState.get(Phasing_Progress);

                if (cascadedBlockPos.contains(pos.offset(facing)) || progress == desiredBlockState.get(Phasing_Progress)) {
                    continue;
                }

                setCurrentBlock = true;
                cascadeCount = this.findNeighborPhasicBlocks(worldIn, pos.offset(facing), desiredBlockState, cascadeCount, cascadedBlockPos, setCurrentBlock);

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
    public enum EnumPhasingProgress implements IStringSerializable {
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
        public String getName() {
            return this.name;
        }
    }
}
