package com.wuest.prefab.Items.Structures;

import com.wuest.prefab.ModRegistry;
import com.wuest.prefab.Prefab;
import com.wuest.prefab.Capabilities.IStructureConfigurationCapability;
import com.wuest.prefab.Config.BasicStructureConfiguration;
import com.wuest.prefab.Config.TreeFarmConfiguration;
import com.wuest.prefab.Config.VillagerHouseConfiguration;
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
		super();

		this.guiId = ModRegistry.GuiVillagerHouses;
		this.setMaxDamage(10);
		this.setMaxStackSize(1);
		ModRegistry.setItemName(this, name);
	}
	
	public static void BuildHouse(EntityPlayer player, World world, VillagerHouseConfiguration configuration)
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
					StructureVillagerHouses structure = StructureVillagerHouses.CreateInstance(configuration.houseStyle.getStructureLocation(), StructureVillagerHouses.class);
					structure.BuildStructure(configuration, world, hitBlockPos, EnumFacing.NORTH, player);
					
					ItemStack stack = player.getHeldItemMainhand().getItem() instanceof ItemVillagerHouses ? player.getHeldItemMainhand() : player.getHeldItemOffhand();
	        		
	        		stack.damageItem(1, player);

					player.inventoryContainer.detectAndSendChanges();
				}
			}
		}
	}
}
