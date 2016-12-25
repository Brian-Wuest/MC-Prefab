package com.wuest.prefab.Items;

import com.wuest.prefab.ModRegistry;
import com.wuest.prefab.Prefab;
import com.wuest.prefab.Capabilities.*;
import com.wuest.prefab.Config.BasicStructureConfiguration;
import com.wuest.prefab.Config.ModularHouseConfiguration;
import com.wuest.prefab.StructureGen.CustomStructures.StructureBasic;
import com.wuest.prefab.StructureGen.CustomStructures.StructureModularHouse;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * This class is used for basic structures to show the basic GUI.
 * @author WuestMan
 *
 */
public class ItemBasicStructure extends Item
{

	public ItemBasicStructure(String name)
	{
		super();

		this.setCreativeTab(CreativeTabs.MISC);
		ModRegistry.setItemName(this, name);
	}
	
	/**
	 * Does something when the item is right-clicked.
	 */
	@Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos hitBlockPos, EnumHand hand, EnumFacing side, float hitX,
			float hitY, float hitZ)
	{
		if (world.isRemote)
		{
			if (side == EnumFacing.UP)
			{
				NBTTagCompound tagCompound = stack.getTagCompound();
				
				if (tagCompound.hasKey("structureConfiguration"))
				{
					BasicStructureConfiguration structureConfiguration = new BasicStructureConfiguration().ReadFromNBTTagCompound(tagCompound.getCompoundTag("structureConfiguration"));
					
					// Open the client side gui to determine the house options.
					//StructureBasic basicStructure = new StructureBasic();
					//basicStructure.ScanStructure(world, hitBlockPos, player.getHorizontalFacing());
					player.openGui(Prefab.instance, ModRegistry.GuiBasicStructure, player.worldObj, hitBlockPos.getX(), hitBlockPos.getY(), hitBlockPos.getZ());
					return EnumActionResult.PASS;
				}
			}
		}

		return EnumActionResult.FAIL;
	}
	
    /**
     * Returns the unlocalized name of this item. This version accepts an ItemStack so different stacks can have
     * different names based on their damage or NBT.
     */
    @Override
	public String getUnlocalizedName(ItemStack stack)
    {
    	NBTTagCompound tagCompound = stack.getTagCompound();
		
		if (tagCompound.hasKey("structureConfiguration"))
		{
			BasicStructureConfiguration structureConfiguration = new BasicStructureConfiguration().ReadFromNBTTagCompound(tagCompound.getCompoundTag("structureConfiguration"));
			return structureConfiguration.getDisplayName();
		}
    	/*if (stack.hasCapability(ModRegistry.StructureConfiguration, EnumFacing.NORTH))
    	{
    		IStructureConfigurationCapability capability = stack.getCapability(ModRegistry.StructureConfiguration, EnumFacing.NORTH);
    		return capability.getConfiguration().getDisplayName();
    	}*/
    	
        return "item." + this.getUnlocalizedName();
    }

	public static void BuildHouse(EntityPlayer player, World world, BasicStructureConfiguration configuration)
	{
		// This is always on the server.
		if (configuration != null)
		{
			BlockPos hitBlockPos = configuration.pos;
			BlockPos playerPosition = player.getPosition();

			IBlockState hitBlockState = world.getBlockState(hitBlockPos);

			if (hitBlockState != null)
			{
				Block hitBlock = hitBlockState.getBlock();
 
				if (hitBlock != null)
				{
					String assetLocation = "";
					
					if (configuration.IsCustomStructure())
					{
						assetLocation = configuration.basicStructureName.getAssetLocation();
					}
					else
					{
						// TODO: Pull the asset information from the NBTTag from the item stack currently in the player's hand.
					}
					
					StructureBasic structure = StructureBasic.CreateInstance(assetLocation, StructureBasic.class);
					
					if (structure.BuildStructure(configuration, world, hitBlockPos, EnumFacing.NORTH, player))
					{
						ItemStack stack = ItemBasicStructure.getBasicStructureItemInHand(player);
						player.inventory.clearMatchingItems(stack.getItem(), -1, 1, stack.getTagCompound());
						player.inventoryContainer.detectAndSendChanges();
					}
				}
			}
		}
	}
	
	public static ItemStack getBasicStructureItemInHand(EntityPlayer player)
	{
		ItemStack stack = null;
		
		if (player.getHeldItemMainhand().getItem() instanceof ItemBasicStructure)
		{
			stack = player.getHeldItemMainhand();
		}
		else if (player.getHeldItemOffhand().getItem() instanceof ItemBasicStructure)
		{
			stack = player.getHeldItemOffhand();
		}
		
		return stack;
	}
}
