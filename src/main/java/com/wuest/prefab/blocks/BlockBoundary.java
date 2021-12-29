package com.wuest.prefab.blocks;

import com.wuest.prefab.Prefab;
import com.wuest.prefab.events.ModEventHandler;
import com.wuest.prefab.gui.GuiLangKeys;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author WuestMan
 */
@SuppressWarnings({"NullableProblems", "WeakerAccess"})
public class BlockBoundary extends Block {
    /**
     * The powered meta data property.
     */
    public static final BooleanProperty Powered = BooleanProperty.create("powered");

    public final CreativeModeTab itemGroup;

    /**
     * Initializes a new instance of the BlockBoundary class.
     */
    public BlockBoundary() {
        super(Block.Properties.of(Prefab.SeeThroughImmovable)
                .sound(SoundType.STONE)
                .strength(0.6F)
                .noOcclusion());

        this.itemGroup = CreativeModeTab.TAB_BUILDING_BLOCKS;
        this.registerDefaultState(this.getStateDefinition().any().setValue(Powered, false));
    }

    /**
     * Queries if this block should render in a given layer.
     */
    public static boolean canRenderInLayer(RenderType layer) {
        // NOTE: This code is in a partial state. Need to find out how to get block state to determine if the block should be rendered this pass.
        boolean powered = false;// state.get(Powered);

        // first part is translucent, second is for solid.
        return (layer == RenderType.translucent() && !powered) || (layer == RenderType.solid() && powered);
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
     * @param state       The current state.
     * @param world       The current world
     * @param player      The player damaging the block, may be null
     * @param pos         Block position in world
     */
    @Override
    public void playerWillDestroy(Level world, BlockPos pos, BlockState state, Player player) {
        super.playerWillDestroy(world, pos, state, player);

        ModEventHandler.RedstoneAffectedBlockPositions.remove(pos);

        boolean poweredSide = world.hasNeighborSignal(pos);

        if (poweredSide) {
            this.setNeighborGlassBlocksPoweredStatus(world, pos, false, 0, new ArrayList<>(), false);
        }
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
    public void neighborChanged(BlockState state, Level worldIn, BlockPos pos, Block blockIn, BlockPos p_189540_5_, boolean p_220069_6_) {
        if (!worldIn.isClientSide) {
            // Only worry about powering blocks.
            if (blockIn.defaultBlockState().isSignalSource()) {
                boolean poweredSide = worldIn.hasNeighborSignal(pos);

                this.setNeighborGlassBlocksPoweredStatus(worldIn, pos, poweredSide, 0, new ArrayList<>(), true);
            }
        }
    }

    /**
     * allows items to add custom lines of information to the mouseover description
     */
    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, @Nullable BlockGetter worldIn, List<Component> tooltip, TooltipFlag advanced) {
        super.appendHoverText(stack, worldIn, tooltip, advanced);

        boolean advancedKeyDown = Screen.hasShiftDown();

        if (!advancedKeyDown) {
            tooltip.add(GuiLangKeys.translateToComponent(GuiLangKeys.SHIFT_TOOLTIP));
        } else {
            tooltip.add(GuiLangKeys.translateToComponent(GuiLangKeys.BOUNDARY_TOOLTIP));
        }
    }

    @Override
    public int getLightBlock(BlockState state, BlockGetter worldIn, BlockPos pos) {
        boolean powered = state.getValue(Powered);

        if (powered && state.isSolidRender(worldIn, pos)) {
            return worldIn.getMaxLightLevel();
        } else {
            return state.propagatesSkylightDown(worldIn, pos) ? 0 : 1;
        }
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, BlockGetter reader, BlockPos pos) {
        boolean powered = state.getValue(Powered);

        return !powered || (!Block.isShapeFullBlock(state.getShape(reader, pos)) && state.getFluidState().isEmpty());
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

    @OnlyIn(Dist.CLIENT)
    @Override
    public boolean skipRendering(BlockState state, BlockState adjacentBlockState, Direction side) {
        return !state.getValue(Powered);
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
                // If the block is already in the correct state, there is no need to cascade to it's neighbors.
                if (cascadedBlockPos.contains(pos.relative(facing))) {
                    continue;
                }

                // running this method for the neighbor block will cascade out to it's other neighbors until there are
                // no more Phasic blocks around.
                ((BlockBoundary) neighborBlock).setNeighborGlassBlocksPoweredStatus(world, pos.relative(facing), isPowered, cascadeCount, cascadedBlockPos, true);
            }
        }
    }
}
