package com.wuest.prefab.Items.Structures;

import com.wuest.prefab.ModRegistry;
import com.wuest.prefab.Prefab;
import com.wuest.prefab.Config.Structures.ChickenCoopConfiguration;
import com.wuest.prefab.Gui.Structures.GuiChickenCoop;
import com.wuest.prefab.StructureGen.CustomStructures.StructureChickenCoop;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * 
 * @author WuestMan
 *
 */
public class ItemChickenCoop extends StructureItem
{
	public ItemChickenCoop(String name)
	{
		super(name, ModRegistry.GuiChickenCoop);
	}
}
