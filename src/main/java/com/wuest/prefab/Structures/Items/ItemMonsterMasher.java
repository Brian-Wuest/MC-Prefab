package com.wuest.prefab.Structures.Items;

import com.wuest.prefab.ModRegistry;
import com.wuest.prefab.Prefab;

import net.minecraft.entity.player.EntityPlayer;
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
public class ItemMonsterMasher extends StructureItem
{
	public ItemMonsterMasher(String name)
	{
		super(name, ModRegistry.GuiMonsterMasher);
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
				//StructureMonsterMasher monsterMasher = new StructureMonsterMasher();
				//monsterMasher.ScanStructure(world, hitBlockPos, player.getHorizontalFacing());
				
				// Open the client side gui to determine the house options.
				player.openGui(Prefab.instance, this.guiId, player.world, hitBlockPos.getX(), hitBlockPos.getY(), hitBlockPos.getZ());
				return EnumActionResult.PASS;
			}
		}

		return EnumActionResult.FAIL;
	}
}
