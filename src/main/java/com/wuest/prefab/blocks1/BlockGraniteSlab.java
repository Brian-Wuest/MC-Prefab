package com.wuest.prefab.blocks;

import com.wuest.prefab.ModRegistry;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.BlockStone;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

import java.util.Random;

/**
 * @author WuestMan
 */
public abstract class BlockGraniteSlab extends BlockSlab {
    /**
     * The property used for the variant. Needed for interactions with ItemSlab.
     */
    private static final PropertyBool VARIANT_PROPERTY = PropertyBool.create("variant");

    public BlockGraniteSlab() {
        super(Material.ROCK);

        this.setSoundType(SoundType.STONE);
        IBlockState iblockstate = this.blockState.getBaseState();
        this.setHardness(0.5F);

        if (!this.isDouble()) {
            iblockstate = iblockstate.withProperty(HALF, BlockSlab.EnumBlockHalf.BOTTOM);
            ModRegistry.setBlockName(this, "block_half_granite_slab");
            this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
        } else {
            ModRegistry.setBlockName(this, "block_granite_slab");
        }

        iblockstate = iblockstate.withProperty(VARIANT_PROPERTY, false);

        this.setDefaultState(iblockstate);
        this.useNeighborBrightness = !this.isDouble();
    }

    @Override
    public String getTranslationKey(int meta) {
        return super.getTranslationKey();
    }

    @Override
    public boolean isDouble() {
        return false;
    }

    @Override
    public IProperty<?> getVariantProperty() {
        return VARIANT_PROPERTY;
    }

    @Override
    public Comparable<?> getTypeForItem(ItemStack stack) {
        return false;
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    @Override
    public IBlockState getStateFromMeta(int meta) {
        IBlockState blockState = this.getDefaultState();
        blockState = blockState.withProperty(VARIANT_PROPERTY, false);

        if (!this.isDouble()) {
            EnumBlockHalf value = EnumBlockHalf.BOTTOM;

            if ((meta & 8) != 0) {
                value = EnumBlockHalf.TOP;
            }

            blockState = blockState.withProperty(HALF, value);
        }

        return blockState;
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    @Override
    public int getMetaFromState(IBlockState state) {
        int i = 0;

        if (!this.isDouble() && state.getValue(HALF) == BlockSlab.EnumBlockHalf.TOP) {
            i |= 8;
        }

        return i;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return this.isDouble() ? new BlockStateContainer(this, new IProperty[]
                {VARIANT_PROPERTY}) : new BlockStateContainer(this, new IProperty[]
                {HALF, VARIANT_PROPERTY});
    }

    /**
     * Get the MapColor for this Block and the given BlockState
     */
    @Override
    public MapColor getMapColor(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        return ((BlockStone) Blocks.STONE).getMapColor(Blocks.STONE.getStateFromMeta(BlockStone.EnumType.GRANITE_SMOOTH.getMetadata()), worldIn, pos);
    }

    /**
     * Get the Item that this Block should drop when harvested.
     */
    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Item.getItemFromBlock(ModRegistry.GraniteSlab);
    }

    /**
     * Gets the metadata of the item this Block can drop. This method is called when the block gets destroyed. It
     * returns the metadata of the dropped item based on the old metadata of the block.
     */
    @Override
    public int damageDropped(IBlockState state) {
        return this.getMetaFromState(this.getDefaultState());
    }

}