package com.wuest.prefab.base;

import net.minecraft.block.*;

/**
 * The base block for any block associated with a tile entity.
 *
 * @author WuestMan
 */
public abstract class TileBlockBase<T extends TileEntityBase> extends Block implements ITileEntityProvider {

    /**
     * Initializes a new instance of the TileBlockBase class.
     */
    public TileBlockBase(AbstractBlock.Properties properties) {
        super(properties);
    }

    @Override
    public BlockRenderType getRenderShape(BlockState state) {
        return BlockRenderType.MODEL;
    }
}