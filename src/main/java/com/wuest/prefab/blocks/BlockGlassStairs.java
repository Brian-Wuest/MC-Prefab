package com.wuest.prefab.blocks;

import com.wuest.prefab.ModRegistry;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author WuestMan
 */
public class BlockGlassStairs extends BlockStairs {
    /**
     * Initializes a new instance of the BlockGlassStairs class.
     *
     * @param name The registered name of this block.
     */
    public BlockGlassStairs(String name) {
        super(Blocks.GLASS.getDefaultState());

        this.setCreativeTab(ModRegistry.PREFAB_GROUP);
        ModRegistry.setBlockName(this, name);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    protected boolean canSilkHarvest() {
        return true;
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    /**
     * Check if the face of a block should block rendering.
     * <p>
     * Faces which are fully opaque should return true, faces with transparency or faces which do not span the full size
     * of the block should return false.
     *
     * @param state The current block state
     * @param world The current world
     * @param pos   Block position in world
     * @param face  The side to check
     * @return True if the block is opaque on the specified side.
     */
    @Override
    public boolean doesSideBlockRendering(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing face) {
        BlockPos offSetPos = pos.offset(face);
        IBlockState offSetState = world.getBlockState(offSetPos);

        Material offSetMaterial = offSetState.getMaterial();

        if (!offSetMaterial.isOpaque()) {
            return true;
        }

        return false;
    }
}