package com.wuest.prefab.Items.Structures;

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
public class ItemBasicStructure extends StructureItem
{

	public ItemBasicStructure(String name)
	{
		super(name, ModRegistry.GuiBasicStructure);

		this.setHasSubtypes(true);
	}
	
    /**
     * returns a list of items with the same ID, but different meta (eg: dye returns 16 items)
     */
    @Override
	@SideOnly(Side.CLIENT)
    public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems)
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
			NBTTagCompound forgeCapabilities = stack.getSubCompound("ForgeCaps", false);
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
