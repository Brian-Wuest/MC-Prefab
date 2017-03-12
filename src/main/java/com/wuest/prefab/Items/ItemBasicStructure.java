package com.wuest.prefab.Items;

import java.util.List;

import com.wuest.prefab.ModRegistry;
import com.wuest.prefab.Prefab;
import com.wuest.prefab.Capabilities.*;
import com.wuest.prefab.Capabilities.Storage.StructureConfigurationStorage;
import com.wuest.prefab.Config.BasicStructureConfiguration;
import com.wuest.prefab.Config.BasicStructureConfiguration.EnumBasicStructureName;
import com.wuest.prefab.StructureGen.CustomStructures.StructureBasic;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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
		this.setHasSubtypes(true);
	}
	
	/**
	 * Does something when the item is right-clicked.
	 */
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos hitBlockPos, EnumHand hand, EnumFacing side, float hitX,
			float hitY, float hitZ)
	{
		if (world.isRemote)
		{
			if (side == EnumFacing.UP)
			{
				ItemStack stack = player.getHeldItem(hand);
				
				IStructureConfigurationCapability capability = stack.getCapability(ModRegistry.StructureConfiguration,  EnumFacing.NORTH);
				
				if (capability != null)
				{
					BasicStructureConfiguration structureConfiguration = capability.getConfiguration();
					
					// Open the client side gui to determine the house options.
					//StructureBasic basicStructure = new StructureBasic();
					//basicStructure.ScanStructure(world, hitBlockPos, player.getHorizontalFacing(), structureConfiguration, true, true);
					player.openGui(Prefab.instance, ModRegistry.GuiBasicStructure, player.worldObj, hitBlockPos.getX(), hitBlockPos.getY(), hitBlockPos.getZ());

					return EnumActionResult.PASS;
				}
			}
		}

		return EnumActionResult.FAIL;
	}
	
    /**
     * returns a list of items with the same ID, but different meta (eg: dye returns 16 items)
     */
    @Override
	@SideOnly(Side.CLIENT)
    public void getSubItems(Item itemIn, CreativeTabs tab, NonNullList<ItemStack> subItems)
    {
		for (EnumBasicStructureName value : EnumBasicStructureName.values())
		{
			if (value.getResourceLocation() != null)
			{
				ItemStack stack = new ItemStack(itemIn);
				IStructureConfigurationCapability capability = stack.getCapability(ModRegistry.StructureConfiguration, EnumFacing.NORTH);
				capability.getConfiguration().basicStructureName = value;
				
				subItems.add(stack);
			}
		}
    }
	
    /**
     * Called each tick as long the item is on a player inventory. Uses by maps to check if is on a player hand and
     * update it's contents.
     */
    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
    {
    	if (entityIn instanceof EntityPlayer)
    	{
    		ItemBasicStructure.getStackCapability(stack);
    	}
    }
    
    /**
     * Returns the unlocalized name of this item. This version accepts an ItemStack so different stacks can have
     * different names based on their damage or NBT.
     */
    @Override
	public String getUnlocalizedName(ItemStack stack)
    {
    	IStructureConfigurationCapability capability = ItemBasicStructure.getStackCapability(stack);

    	if (capability != null)
    	{
    		return capability.getConfiguration().getDisplayName();
    	}

        return this.getUnlocalizedName();
    }
    
    public String getItemStackDisplayName(ItemStack stack)
    {
        return ("" + I18n.translateToLocal(this.getUnlocalizedNameInefficiently(stack))).trim();
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
					
					if (!configuration.IsCustomStructure())
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
						
						if (stack.getCount() == 1)
						{
							player.inventory.deleteStack(stack);
						}
						else
						{
							stack.setCount(stack.getCount() - 1);
						}
						
						player.inventoryContainer.detectAndSendChanges();
					}
				}
			}
		}
	}
	
    /**
     * Override this method to change the NBT data being sent to the client.
     * You should ONLY override this when you have no other choice, as this might change behavior client side!
     *
     * @param stack The stack to send the NBT tag for
     * @return The NBT tag
     */
    @Override
	public NBTTagCompound getNBTShareTag(ItemStack stack)
    {
    	if (stack.getTagCompound() == null)
    	{
    		// Make sure to serialize the NBT for this stack so the information is pushed to the client and the appropriate Icon is displayed for this stack.
    		return stack.serializeNBT();
    	}
    	
        return stack.getTagCompound();
    }
	
	public static ItemStack getBasicStructureItemInHand(EntityPlayer player)
	{
		ItemStack stack = player.getHeldItemOffhand();
		
		// Get off hand first since that is the right-click hand if there is something in there.
		if (stack == null || !(stack.getItem() instanceof ItemBasicStructure))
		{
			if (player.getHeldItemMainhand() != null  && player.getHeldItemMainhand().getItem() instanceof ItemBasicStructure)
			{
				stack = player.getHeldItemMainhand();
			}
			else
			{
				stack = null;
			}
		}
		
		return stack;
	}
	
	public static IStructureConfigurationCapability getStackCapability(ItemStack stack)
	{
		if (stack.hasCapability(ModRegistry.StructureConfiguration, EnumFacing.NORTH))
		{
			NBTTagCompound forgeCapabilities = stack.getSubCompound("ForgeCaps");
			IStructureConfigurationCapability stackCapability = stack.getCapability(ModRegistry.StructureConfiguration, EnumFacing.NORTH);
			
			if (forgeCapabilities != null)
			{
				if (stackCapability.getDirty() && forgeCapabilities.hasKey(Prefab.MODID + ":structuresconfiguration"))
    			{
    				StructureConfigurationCapability capabilityTemp = new StructureConfigurationCapability();
    				StructureConfigurationStorage storage = new StructureConfigurationStorage();
    				storage.readNBT(ModRegistry.StructureConfiguration, capabilityTemp, EnumFacing.NORTH, forgeCapabilities.getCompoundTag(Prefab.MODID + ":structuresconfiguration"));
    				
    				if (!capabilityTemp.getConfiguration().basicStructureName.getName().equals(stackCapability.getConfiguration().basicStructureName.getName()))
    				{
    					stackCapability.setConfiguration(capabilityTemp.getConfiguration());
    					stackCapability.setDirty(false);
    				}
    			}
			}
			
			return stackCapability;
		}
		
		return null;
	}
}
