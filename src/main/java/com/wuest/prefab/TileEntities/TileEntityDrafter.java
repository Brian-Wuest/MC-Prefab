package com.wuest.prefab.TileEntities;

import java.util.ArrayList;

import com.wuest.prefab.Base.TileEntityBase;
import com.wuest.prefab.Config.DrafterTileEntityConfig;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * This is the tile entity for the Drafter block. This block will contain the configuration data.
 * @author WuestMan
 */
public class TileEntityDrafter extends TileEntityBase<DrafterTileEntityConfig>
{
	private ArrayList<ItemStack> stacks;
	
	/**
	 * Initializes a new instance of the TileEntityDrafter class.
	 */
	public TileEntityDrafter()
	{
		super();
		this.config = new DrafterTileEntityConfig();
		this.stacks = new ArrayList<ItemStack>();
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) 
	{
		super.writeToNBT(compound);
	
		NBTTagCompound stacks = new NBTTagCompound();
		int key = 0;
		
		for (ItemStack stack : this.stacks)
		{
			NBTTagCompound stackCompound = new NBTTagCompound();
			
			stack.writeToNBT(stackCompound);
			
			stacks.setTag(key + "", stackCompound);
			key++;
		}
		
		compound.setTag("item_stacks", stacks);
		
		return compound;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) 
	{
		super.readFromNBT(compound);
		this.stacks.clear();
		
		if (compound.hasKey("item_stacks"))
		{
			NBTTagCompound stacks = compound.getCompoundTag("item_stacks");
			
			for (String key : stacks.getKeySet())
			{
				
				ItemStack stack = new ItemStack(stacks.getCompoundTag(key));
				
				if (stack != null)
				{
					this.stacks.add(stack);
				}
			}
		}
	}
	
	public void setItemStacks(World world, BlockPos pos)
	{
		TileEntity tileEntity = world.getTileEntity(pos);
		this.stacks.clear();
		
		if (tileEntity != null && tileEntity instanceof IInventory)
		{
			IInventory inventory = (IInventory)tileEntity;
			
			for (int i = 0; i < inventory.getSizeInventory(); i++)
			{
				ItemStack foundStack = inventory.getStackInSlot(i);
				
				if (foundStack != null)
				{
					this.stacks.add(foundStack);
				}
			}
		}
		
		this.markDirty();
	}

	public ArrayList<ItemStack> getStacks()
	{
		return this.stacks;
	}
}