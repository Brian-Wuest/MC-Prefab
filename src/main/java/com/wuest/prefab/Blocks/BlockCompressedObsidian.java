package com.wuest.prefab.Blocks;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import com.google.common.collect.Lists;
import com.wuest.prefab.ModRegistry;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * This is the compressed Obsidian block class.
 * @author WuestMan
 *
 */
public class BlockCompressedObsidian extends Block implements IMetaBlock
{
	public static final PropertyEnum<BlockCompressedObsidian.EnumType> VARIANT = PropertyEnum.<BlockCompressedObsidian.EnumType>create("variant", BlockCompressedObsidian.EnumType.class);
	
	/**
	 * Initializes a new instance of the BlockCompressedObsidian class.
	 */
	public BlockCompressedObsidian()
	{
		super(Material.ROCK);
		this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
		this.setHardness(50.0F);
		this.setResistance(2000.0F);
		this.setHarvestLevel(null, 0);
		this.setSoundType(SoundType.STONE);
		this.setHarvestLevel("pickaxe", 3);
		this.setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, EnumType.COMPRESSED_OBSIDIAN));
		ModRegistry.setBlockName(this, "block_compressed_obsidian");
	}
	
    /**
     * Gets the localized name of this block. Used for the statistics page.
     */
	@Override
    public String getLocalizedName()
    {
        return I18n.translateToLocal("tile.prefab" + BlockCompressedObsidian.EnumType.COMPRESSED_OBSIDIAN.getUnlocalizedName() + ".name");
    }
	
    /**
     * Get the Item that this Block should drop when harvested.
     */
    @Nullable
    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return Item.getItemFromBlock(ModRegistry.CompressedObsidianBlock());
    }
    
    /**
     * Gets the metadata of the item this Block can drop. This method is called when the block gets destroyed. It
     * returns the metadata of the dropped item based on the old metadata of the block.
     */
    @Override
    public int damageDropped(IBlockState state)
    {
        return this.getMetaFromState(state);
    }
    
    @Override
    public String getSpecialName(ItemStack stack) 
    {
        for (BlockCompressedObsidian.EnumType enumType : BlockCompressedObsidian.EnumType.values())
        {
        	if (enumType.meta == stack.getItemDamage())
        	{
        		return enumType.name;
        	}
        }
        
        return "";
    }
    
    @Override
    public String getMetaDataUnLocalizedName(int metaData)
    {
    	EnumType type = EnumType.byMetadata(metaData);
    	
    	return type.unlocalizedName;
    }
    
    /**
     * returns a list of blocks with the same ID, but different meta (eg: wood returns 4 blocks)
     */
    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> list)
    {
        for (BlockCompressedObsidian.EnumType enumType : BlockCompressedObsidian.EnumType.values())
        {
            list.add(new ItemStack(this, 1, enumType.getMetadata()));
        }
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(VARIANT, BlockCompressedObsidian.EnumType.byMetadata(meta));
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    @Override
    public int getMetaFromState(IBlockState state)
    {
        return ((BlockCompressedObsidian.EnumType)state.getValue(VARIANT)).getMetadata();
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] {VARIANT});
    }
    
    /**
     * Gets the variant for the current state.
     * @param state The state to get the enum type for.
     * @return A enum type for the current state.
     */
    public EnumType getVariantFromState(IBlockState state)
    {
    	return (EnumType)state.getValue(VARIANT);
    }
    
    /**
     * An enum which contains the various types of block variants.
     * @author WuestMan
     *
     */
	public static enum EnumType implements IStringSerializable
	{
		COMPRESSED_OBSIDIAN(0, "block_compressed_obsidian", "block_compressed_obsidian"),
		DOUBLE_COMPRESSED_OBSIDIAN(1, "block_double_compressed_obsidian", "block_double_compressed_obsidian"),;
		
        private final int meta;
		
        /** The EnumType's name. */
        private final String name;
        private final String unlocalizedName;
        /** Array of the Block's BlockStates */
        private static final BlockCompressedObsidian.EnumType[] META_LOOKUP = new BlockCompressedObsidian.EnumType[values().length];
        
        private EnumType(int meta, String name)
        {
            this(meta, name, name);
        }

        private EnumType(int meta, String name, String unlocalizedName)
        {
            this.meta = meta;
            this.name = name;
            this.unlocalizedName = unlocalizedName;
        }
        
        /**
         * A list of resource locations for the names.
         * @return A list of resource locations for the numerous types in this enum.
         */
        public static ResourceLocation[] GetNames()
        {
        	List<ResourceLocation> list = Lists.newArrayList();
        	
        	for (EnumType type : EnumType.values())
        	{
        		list.add(new ResourceLocation("prefab", type.unlocalizedName));
        	}
        	
        	return list.toArray(new ResourceLocation[list.size()]);
        }
        
        /**
         * The EnumType's meta data value.
         * @return the meta data for this block. 
         */
        public int getMetadata()
        {
            return this.meta;
        }
        
        /**
         * Gets the name of this enum value.
         */
        public String toString()
        {
            return this.name;
        }

		@Override
		public String getName()
		{
			return this.name;
		}
		
		/**
		 * The unlocalized name of this EnumType.
		 * @return A string containing the unlocalized name.
		 */
        public String getUnlocalizedName()
        {
            return this.unlocalizedName;
        }
		
        /**
         * Returns an EnumType for the BlockState from a metadata value.
         * @param meta The meta data value to equate to a {@link BlockCompressedObsidian.EnumType} 
         * @return If the meta data is invalid the default will be used, otherwise the EnumType found.
         */
        public static BlockCompressedObsidian.EnumType byMetadata(int meta)
        {
            if (meta < 0 || meta >= META_LOOKUP.length)
            {
                meta = 0;
            }

            return META_LOOKUP[meta];
        }
        
        static
        {
            for (BlockCompressedObsidian.EnumType type : values())
            {
                META_LOOKUP[type.getMetadata()] = type;
            }
        }
	}
    
}
