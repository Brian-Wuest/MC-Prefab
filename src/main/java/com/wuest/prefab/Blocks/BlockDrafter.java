package com.wuest.prefab.Blocks;

import com.wuest.prefab.ModRegistry;
import com.wuest.prefab.Prefab;
import com.wuest.prefab.Base.TileBlockBase;
import com.wuest.prefab.TileEntities.TileEntityDrafter;

import io.netty.channel.embedded.EmbeddedChannel;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.network.FMLOutboundHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.FMLOutboundHandler.OutboundTarget;
import net.minecraftforge.fml.common.network.internal.FMLMessage;
import net.minecraftforge.fml.relauncher.Side;

/**
 * This is the block for the modular house interface.
 * @author WuestMan
 *
 */
public class BlockDrafter extends TileBlockBase<TileEntityDrafter> implements IMetaBlock
{
	/**
	 * The facing property for this block.
	 */
	public static final PropertyEnum<EnumFacing> FACING = PropertyEnum.<EnumFacing>create("facing", EnumFacing.class);
	
	/**
	 * Initializes a new instance of the Modular House Interface class.
	 */
	public BlockDrafter()
	{
		super(Material.ROCK);
		this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
		this.setSoundType(SoundType.STONE);
		this.setBlockUnbreakable();
		this.setResistance(6000000.0F);
		this.setDefaultState(this.blockState.getBaseState().withProperty(BlockDrafter.FACING, EnumFacing.NORTH));
		ModRegistry.setBlockName(this, "block_drafter");
	}
	
	/**
     * Determines if the player can harvest this block, obtaining it's drops when the block is destroyed.
     *
     * @param player The player damaging the block, may be null
     * @param meta The block's current metadata
     * @return True to spawn the drops
     */
    @Override
	public boolean canHarvestBlock(IBlockAccess world, BlockPos pos, EntityPlayer player)
    {
        return false;
    }
    
    /**
     * Called by ItemBlocks just before a block is actually set in the world, to allow for adjustments to the
     * IBlockstate
     */
    @Override
    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
        return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite());
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
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		if (!world.isRemote) 
		{
			// Inventory data needs to be sent to the client to show the gui. This is because chests don't push their data to the client until they are opened.
			// Get the neighbor chest (if there is one) and set the property on the tile entity.
			EnumFacing blockFacing = state.getValue(FACING).rotateYCCW();
			
			// This is to set the item stacks on the tile entity which will be synced to the client.
			TileEntityDrafter drafterTileEntity = this.getLocalTileEntity(world, pos);
			drafterTileEntity.setItemStacks(world, pos.offset(blockFacing));
			
			// Make sure to sync up the client after setting the stacks before opening the GUI.
			SPacketUpdateTileEntity spacketupdatetileentity = drafterTileEntity.getUpdatePacket();

            if (spacketupdatetileentity != null)
            {
                ((EntityPlayerMP)player).connection.sendPacket(spacketupdatetileentity);
            }
            
            player.openGui(Prefab.instance, ModRegistry.GuiDrafter, world, pos.getX(), pos.getY(), pos.getZ());
		}

		return true;
	}
    
	@Override
	public int customUpdateState(World worldIn, BlockPos pos, IBlockState state, TileEntityDrafter tileEntity)
	{
		return 0;
	}

	@Override
	public void customBreakBlock(TileEntityDrafter tileEntity, World worldIn, BlockPos pos, IBlockState state)
	{
		// TODO Auto-generated method stub
		
	}
	
    /**
     * Convert the given metadata into a BlockState for this Block
     */
    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(FACING, EnumFacing.getFront(meta));
    }
	
    /**
     * Convert the BlockState into the correct metadata value
     */
    @Override
    public int getMetaFromState(IBlockState state)
    {
        return ((EnumFacing)state.getValue(FACING)).getIndex();
    }
    
    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] { FACING });
    }

	@Override
	public String getSpecialName(ItemStack stack)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getMetaDataUnLocalizedName(int metaData)
	{
		return this.getRegistryName().getResourcePath();
	}
    
}