package com.wuest.prefab.Items.Structures;

import com.wuest.prefab.ModRegistry;
import com.wuest.prefab.Prefab;
import com.wuest.prefab.Config.Structures.NetherGateConfiguration;
import com.wuest.prefab.StructureGen.CustomStructures.StructureNetherGate;

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
 * This is the item used to generate the Nether Gate structure.
 */
public class ItemNetherGate extends StructureItem
{
	public ItemNetherGate(String name)
	{
		super(name, ModRegistry.GuiNetherGate);
	}
}
