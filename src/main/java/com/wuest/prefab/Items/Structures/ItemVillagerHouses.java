package com.wuest.prefab.Items.Structures;

import com.wuest.prefab.ModRegistry;
import com.wuest.prefab.Prefab;
import com.wuest.prefab.Capabilities.IStructureConfigurationCapability;
import com.wuest.prefab.Config.Structures.BasicStructureConfiguration;
import com.wuest.prefab.Config.Structures.TreeFarmConfiguration;
import com.wuest.prefab.Config.Structures.VillagerHouseConfiguration;
import com.wuest.prefab.StructureGen.CustomStructures.StructureBasic;
import com.wuest.prefab.StructureGen.CustomStructures.StructureTreeFarm;
import com.wuest.prefab.StructureGen.CustomStructures.StructureVillagerHouses;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureVillagePieces;

/**
 * 
 * @author WuestMan
 *
 */
public class ItemVillagerHouses extends StructureItem
{
	public ItemVillagerHouses(String name)
	{
		super(name, ModRegistry.GuiVillagerHouses);

		this.setMaxDamage(10);
		this.setMaxStackSize(1);
	}
}
