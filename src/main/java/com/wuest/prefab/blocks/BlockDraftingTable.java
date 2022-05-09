package com.wuest.prefab.blocks;

import com.wuest.prefab.Prefab;
import com.wuest.prefab.base.TileBlockBase;
import com.wuest.prefab.blocks.entities.DraftingTableBlockEntity;
import com.wuest.prefab.gui.screens.menus.DraftingTableMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class BlockDraftingTable extends TileBlockBase<DraftingTableBlockEntity> {
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
    public BlockState rotate(BlockState blockState, Rotation rotation) {
        return blockState.setValue(HorizontalDirectionalBlock.FACING, rotation.rotate(blockState.getValue(HorizontalDirectionalBlock.FACING)));
    }

    @Override
    public BlockState mirror(BlockState blockState, Mirror mirror) {
        return blockState.rotate(mirror.getRotation(blockState.getValue(HorizontalDirectionalBlock.FACING)));
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
        switch (blockState.getValue(HorizontalDirectionalBlock.FACING)) {
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

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (world.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            player.openMenu(state.getMenuProvider(world, pos));
            return InteractionResult.CONSUME;
        }
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new DraftingTableBlockEntity(pos, state);
    }

    @Override
    @Nullable
    public MenuProvider getMenuProvider(BlockState blockState, Level level, BlockPos blockPos) {
        return new SimpleMenuProvider((i, inventory, player) -> {
            return new DraftingTableMenu(i, inventory, ContainerLevelAccess.create(level, blockPos));
        }, new TextComponent(""));
    }
}
