package com.wuest.prefab.blocks;

import com.wuest.prefab.Prefab;
import com.wuest.prefab.base.TileBlockBase;
import com.wuest.prefab.blocks.entities.StructureScannerBlockEntity;
import com.wuest.prefab.config.StructureScannerConfig;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class BlockStructureScanner extends TileBlockBase<StructureScannerBlockEntity> {
    public static final DirectionProperty FACING;

    static {
        FACING = HorizontalBlock.FACING;
    }

    /**
     * Initializes a new instance of the BlockStructureScanner class.
     */
    public BlockStructureScanner() {
        super(Block.Properties.of(Material.STONE));

        this.registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.NORTH));
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext ctx) {
        return this.defaultBlockState().setValue(BlockStructureScanner.FACING, ctx.getHorizontalDirection());
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(BlockStructureScanner.FACING, rotation.rotate(state.getValue(BlockStructureScanner.FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(BlockStructureScanner.FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(BlockStructureScanner.FACING);
    }

    @Override
    public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        if (world.isClientSide) {
            TileEntity blockEntity = world.getBlockEntity(pos);

            if (blockEntity instanceof StructureScannerBlockEntity) {
                blockEntity.setLevelAndPosition(world, pos);
                StructureScannerConfig config = ((StructureScannerBlockEntity) blockEntity).getConfig();
                Prefab.proxy.openGuiForBlock(pos, world, config);
            }

            return ActionResultType.SUCCESS;
        } else {
            return ActionResultType.CONSUME;
        }
    }

    @Override
    public TileEntity newBlockEntity(IBlockReader world) {
        return new StructureScannerBlockEntity();
    }
}
