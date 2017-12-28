package com.wuest.prefab.Items.Structures;

import com.wuest.prefab.ModRegistry;
import com.wuest.prefab.Prefab;
import com.wuest.prefab.Capabilities.IStructureConfigurationCapability;
import com.wuest.prefab.Capabilities.StructureConfigurationCapability;
import com.wuest.prefab.Capabilities.Storage.StructureConfigurationStorage;
import com.wuest.prefab.Config.Structures.BasicStructureConfiguration.EnumBasicStructureName;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * This class is used for basic structures to show the basic GUI.
 * 
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
	 * Does something when the item is right-clicked.
	 */
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world,
			BlockPos hitBlockPos, EnumHand hand, EnumFacing side, float hitX,
			float hitY, float hitZ)
	{
		if (world.isRemote)
		{
			if (side == EnumFacing.UP)
			{
				// Open the client side gui to determine the house options.
				// StructureBasic basicStructure = new StructureBasic();
				// IStructureConfigurationCapability capability =
				// stack.getCapability(ModRegistry.StructureConfiguration,
				// EnumFacing.NORTH);
				// BasicStructureConfiguration structureConfiguration =
				// capability.getConfiguration();
				// basicStructure.ScanStructure(world, hitBlockPos,
				// player.getHorizontalFacing(), structureConfiguration, false,
				// false);

				player.openGui(Prefab.instance, this.guiId, player.world,
						hitBlockPos.getX(), hitBlockPos.getY(),
						hitBlockPos.getZ());
				return EnumActionResult.PASS;
			}
		}

		return EnumActionResult.FAIL;
	}

	/**
	 * returns a list of items with the same ID, but different meta (eg: dye
	 * returns 16 items)
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> subItems)
	{
		if (this.isInCreativeTab(tab))
		{
			for (EnumBasicStructureName value : EnumBasicStructureName.values())
			{
				if (value.getResourceLocation() != null)
				{
					ItemStack stack = new ItemStack(this);
					IStructureConfigurationCapability capability = stack
							.getCapability(ModRegistry.StructureConfiguration,
									EnumFacing.NORTH);
					capability.getConfiguration().basicStructureName = value;

					subItems.add(stack);
				}
			}
		}
	}

	/**
	 * Called each tick as long the item is on a player inventory. Uses by maps
	 * to check if is on a player hand and update it's contents.
	 */
	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn,
			int itemSlot, boolean isSelected)
	{
		if (entityIn instanceof EntityPlayer)
		{
			ItemBasicStructure.getStackCapability(stack);
		}
	}

	/**
	 * Returns the unlocalized name of this item. This version accepts an
	 * ItemStack so different stacks can have different names based on their
	 * damage or NBT.
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
     * This used to be 'display damage' but its really just 'aux' data in the ItemStack, usually shares the same variable as damage.
     * @param stack
     * @return
     */
    @Override
    public int getMetadata(ItemStack stack)
    {
    	if (stack.getTagCompound() == null
    			|| stack.getTagCompound().hasNoTags())
    	{
    		// Make sure to serialize the NBT for this stack so the information is pushed to the client and the appropriate Icon is displayed for this stack.
    		stack.setTagCompound(stack.serializeNBT());
    	}
    	
        return 0;
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
    	if (stack.getTagCompound() == null
    			|| stack.getTagCompound().hasNoTags())
    	{
    		// Make sure to serialize the NBT for this stack so the information is pushed to the client and the appropriate Icon is displayed for this stack.
    		stack.setTagCompound(stack.serializeNBT());
    	}
    	
        return stack.getTagCompound();
    }

	public static ItemStack getBasicStructureItemInHand(EntityPlayer player)
	{
		ItemStack stack = player.getHeldItemOffhand();

		// Get off hand first since that is the right-click hand if there is
		// something in there.
		if (stack == null || !(stack.getItem() instanceof ItemBasicStructure))
		{
			if (player.getHeldItemMainhand() != null
					&& player.getHeldItemMainhand()
							.getItem() instanceof ItemBasicStructure)
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

	public static IStructureConfigurationCapability getStackCapability(
			ItemStack stack)
	{
		if (stack.hasCapability(ModRegistry.StructureConfiguration,
				EnumFacing.NORTH))
		{
			NBTTagCompound forgeCapabilities = stack
					.getSubCompound("ForgeCaps");
			IStructureConfigurationCapability stackCapability = stack
					.getCapability(ModRegistry.StructureConfiguration,
							EnumFacing.NORTH);

			if (forgeCapabilities != null)
			{
				if (stackCapability.getDirty() && forgeCapabilities
						.hasKey(Prefab.MODID + ":structuresconfiguration"))
				{
					StructureConfigurationCapability capabilityTemp = new StructureConfigurationCapability();
					StructureConfigurationStorage storage = new StructureConfigurationStorage();
					storage.readNBT(ModRegistry.StructureConfiguration,
							capabilityTemp, EnumFacing.NORTH,
							forgeCapabilities.getCompoundTag(
									Prefab.MODID + ":structuresconfiguration"));

					if (!capabilityTemp.getConfiguration().basicStructureName
							.getName()
							.equals(stackCapability
									.getConfiguration().basicStructureName
											.getName()))
					{
						stackCapability.setConfiguration(
								capabilityTemp.getConfiguration());
						stackCapability.setDirty(false);
					}
				}
			}

			return stackCapability;
		}

		return null;
	}
}