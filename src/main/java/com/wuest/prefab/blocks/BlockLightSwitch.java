package com.wuest.prefab.blocks;

import com.wuest.prefab.ModRegistry;
import com.wuest.prefab.base.TileBlockBase;
import com.wuest.prefab.blocks.entities.LightSwitchBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class BlockLightSwitch extends TileBlockBase<LightSwitchBlockEntity> {
    protected static final VoxelShape NORTH_AABB;
    protected static final VoxelShape SOUTH_AABB;
    protected static final VoxelShape WEST_AABB;
    protected static final VoxelShape EAST_AABB;
    protected static final VoxelShape UP_AABB;
    protected static final VoxelShape DOWN_AABB;

    public static final DirectionProperty FACING;

    public static final EnumProperty<AttachFace> FACE;

    public static final BooleanProperty POWERED;

    static {
        FACING = HorizontalDirectionalBlock.FACING;
        FACE = EnumProperty.create("face", AttachFace.class);
        POWERED = BooleanProperty.create("powered");

        /*
            d<g
            e<h
            f<i
         */
        NORTH_AABB = Block.box(0.0, 0.0, 12.0, 16.0, 16.0, 16.0);
        SOUTH_AABB = Block.box(0.0, 0.0, 0.0, 16.0, 16.0, 4.0);
        WEST_AABB = Block.box(12.0, 0.0, 0.0, 16.0, 16.0, 16.0);
        EAST_AABB = Block.box(0.0, 0.0, 0.0, 4.0, 16.0, 16.0);
        UP_AABB = Block.box(0.0, 0.0, 0.0, 16.0, 4.0, 16.0);
        DOWN_AABB = Block.box(0.0, 12.0, 0.0, 16.0, 16.0, 16.0);
    }

    public static boolean canAttach(LevelReader levelReader, BlockPos blockPos, Direction direction) {
        BlockPos blockPos2 = blockPos.relative(direction);
        return levelReader.getBlockState(blockPos2).isFaceSturdy(levelReader, blockPos2, direction.getOpposite());
    }

    protected static Direction getConnectedDirection(BlockState blockState) {
        switch (blockState.getValue(FACE)) {
            case CEILING:
                return Direction.DOWN;
            case FLOOR:
                return Direction.UP;
            default:
                return blockState.getValue(FACING);
        }
    }

    /**
     * Initializes a new instance of the TileBlockBase class.
     */
    public BlockLightSwitch() {
        super(Properties.of(Material.DECORATION).noCollission().strength(0.5F).sound(SoundType.WOOD));

        this.registerDefaultState(this.defaultBlockState()
                .setValue(BlockLightSwitch.FACING, Direction.NORTH)
                .setValue(BlockLightSwitch.FACE, AttachFace.FLOOR)
                .setValue(BlockLightSwitch.POWERED, false));
    }

    @Override
    public void onRemove(BlockState blockState, Level level, BlockPos blockPos, BlockState blockState2, boolean bl) {
        if (!level.isClientSide) {
            // Check to see if the state is just changing.
            // if the state is just changing, don't remove it from the registry.
            if (blockState.getBlock() != blockState2.getBlock()) {
                // Remove this switch from the registry and turn off the registered blocks.
                ModRegistry.serverModRegistries.getLightSwitchRegistry().remove(level, blockPos);
            }
        }
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        if (!level.isClientSide) {
            BlockState updatedBlockState = this.cycleSwitch(blockState, level, blockPos);
            float f = updatedBlockState.getValue(POWERED) ? 0.6F : 0.5F;
            SoundEvent soundEvent = updatedBlockState.getValue(POWERED) ? SoundEvents.STONE_BUTTON_CLICK_ON : SoundEvents.STONE_BUTTON_CLICK_OFF;
            level.playSound(null, blockPos, soundEvent, SoundSource.BLOCKS, 0.3F, f);
            level.gameEvent(player, updatedBlockState.getValue(POWERED) ? GameEvent.BLOCK_ACTIVATE : GameEvent.BLOCK_DEACTIVATE, blockPos);

            // Tell registered lights that this switch is on/off.
            return InteractionResult.CONSUME;
        }

        return InteractionResult.SUCCESS;
    }

    public BlockState cycleSwitch(BlockState blockState, Level level, BlockPos blockPos) {
        blockState = blockState.cycle(POWERED);
        level.setBlock(blockPos, blockState, 3);

        ModRegistry.serverModRegistries.getLightSwitchRegistry().flipSwitch(level, blockPos, blockState.getValue(POWERED));
        return blockState;
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        switch (blockState.getValue(FACE)) {
            case FLOOR:
                return UP_AABB;
            case WALL:
                switch (blockState.getValue(FACING)) {
                    case EAST:
                        return EAST_AABB;
                    case WEST:
                        return WEST_AABB;
                    case SOUTH:
                        return SOUTH_AABB;
                    case NORTH:
                    default:
                        return NORTH_AABB;
                }
            case CEILING:
            default:
                return DOWN_AABB;
        }
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        Direction[] directions = ctx.getNearestLookingDirections();

        for (Direction direction : directions) {
            BlockState blockState;

            if (direction.getAxis() == Direction.Axis.Y) {
                blockState = (this.defaultBlockState().setValue(FACE, direction == Direction.UP
                        ? AttachFace.CEILING
                        : AttachFace.FLOOR)).setValue(FACING, ctx.getHorizontalDirection());
            } else {
                blockState = (this.defaultBlockState().setValue(FACE, AttachFace.WALL)).setValue(FACING, direction.getOpposite());
            }

            if (blockState.canSurvive(ctx.getLevel(), ctx.getClickedPos())) {
                return blockState;
            }
        }

        return null;
    }

    @Override
    public void setPlacedBy(Level level, BlockPos blockPos, BlockState blockState, @Nullable LivingEntity livingEntity, ItemStack itemStack) {
        if (!level.isClientSide) {
            ModRegistry.serverModRegistries.getLightSwitchRegistry().register(level, blockPos);
        }
    }

    @Override
    public boolean canSurvive(BlockState blockState, LevelReader levelReader, BlockPos blockPos) {
        return BlockLightSwitch.canAttach(levelReader, blockPos, BlockLightSwitch.getConnectedDirection(blockState).getOpposite());
    }

    @Override
    public BlockState updateShape(BlockState blockState, Direction direction, BlockState blockState2, LevelAccessor levelAccessor, BlockPos blockPos, BlockPos blockPos2) {
        return BlockLightSwitch.getConnectedDirection(blockState).getOpposite() == direction && !blockState.canSurvive(levelAccessor, blockPos)
                ? Blocks.AIR.defaultBlockState()
                : super.updateShape(blockState, direction, blockState2, levelAccessor, blockPos, blockPos2);
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(BlockLightSwitch.FACING, rotation.rotate(state.getValue(BlockLightSwitch.FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(BlockLightSwitch.FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(BlockLightSwitch.FACING)
                .add(BlockLightSwitch.FACE)
                .add(BlockLightSwitch.POWERED);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new LightSwitchBlockEntity(blockPos, blockState);
    }
}
