package com.wuest.prefab.Items.Structures;

import com.wuest.prefab.ModRegistry;
import com.wuest.prefab.Prefab;
import com.wuest.prefab.StructureGen.CustomStructures.StructureWarehouse;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

/**
 * 
 * @author WuestMan
 *
 */
public class ItemAdvancedWareHouse extends ItemWareHouse
{
	public ItemAdvancedWareHouse(String name)
	{
		super(name);
		
		this.guiId = ModRegistry.GuiAdvancedWareHouse;
	}
}
