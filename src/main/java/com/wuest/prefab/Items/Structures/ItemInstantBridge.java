package com.wuest.prefab.Items.Structures;

import com.wuest.prefab.*;
import com.wuest.prefab.Config.Structures.BasicStructureConfiguration;
import com.wuest.prefab.Gui.GuiLangKeys;
import com.wuest.prefab.Render.StructureRenderHandler;
import com.wuest.prefab.StructureGen.CustomStructures.StructureBasic;

import net.minecraft.block.BlockLiquid;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapData;

/**
 * This is the instant bridge item.
 * @author WuestMan
 *
 */
public class ItemInstantBridge extends Item
{
	private StructureBasic basic;
	private BasicStructureConfiguration config; 
	
	/**
	 * Get's the GuiId to show to the user when this item is used.
	 */
	protected int guiId = 17;
	
	public ItemInstantBridge(String name)
	{
		super();

		this.setCreativeTab(CreativeTabs.MISC);
		this.setMaxDamage(10);
		this.setMaxStackSize(1);
		ModRegistry.setItemName(this, name);
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
				// Open the client side gui to determine the house options.
				player.openGui(Prefab.instance, this.guiId, player.world, hitBlockPos.getX(), hitBlockPos.getY(), hitBlockPos.getZ());
				return EnumActionResult.PASS;
			}
		}

		return EnumActionResult.FAIL;
	}
	
    public static RayTraceResult RayTrace(World world, EntityPlayer player, float distance, boolean includeFluids)
    {
        Vec3d vec3d = new Vec3d(player.posX, player.posY + (double)player.getEyeHeight(), player.posZ);
        Vec3d vec3d1 = player.getLook(1.0F);
        Vec3d vec3d2 = vec3d.addVector(vec3d1.x * distance, vec3d1.y * distance, vec3d1.z * distance);
        return world.rayTraceBlocks(vec3d, vec3d2, includeFluids, false, true);
    }
	
 	private boolean BuildBridge(World worldIn, EntityPlayer playerIn, BlockPos originalBlockPos)
	{
		EnumFacing playerFacing = playerIn.getHorizontalFacing();
		
		BlockPos startingPos = originalBlockPos.offset(playerFacing.rotateYCCW(), 2);
		BlockPos endPos = originalBlockPos.offset(playerFacing, 50).offset(playerFacing.rotateY(), 2).up(2);
		
		if (!BuildingMethods.CheckBuildSpaceForAllowedBlockReplacement(null, worldIn, startingPos, endPos, playerIn))
		{
			playerIn.sendMessage(new TextComponentTranslation(GuiLangKeys.GUI_STRUCTURE_NOBUILD).setStyle(new Style().setColor(TextFormatting.GREEN)));
			return false;
		}
		
		for (int i = 0; i < 50; i++)
		{
			BlockPos currentPos = originalBlockPos.offset(playerFacing, i).up();
			
			// Place the floor
			BuildingMethods.ReplaceBlock(worldIn, currentPos, Blocks.COBBLESTONE);
			BuildingMethods.ReplaceBlock(worldIn, currentPos.offset(playerFacing.rotateYCCW()), Blocks.COBBLESTONE);
			BuildingMethods.ReplaceBlock(worldIn, currentPos.offset(playerFacing.rotateYCCW(), 2), Blocks.COBBLESTONE);
			BuildingMethods.ReplaceBlock(worldIn, currentPos.offset(playerFacing.rotateY()), Blocks.COBBLESTONE);
			BuildingMethods.ReplaceBlock(worldIn, currentPos.offset(playerFacing.rotateY(), 2), Blocks.COBBLESTONE);
			
			// Build the walls.
			BuildingMethods.ReplaceBlock(worldIn, currentPos.offset(playerFacing.rotateYCCW(), 2).up(), Blocks.COBBLESTONE_WALL);
			BuildingMethods.ReplaceBlock(worldIn, currentPos.offset(playerFacing.rotateY(), 2).up(), Blocks.COBBLESTONE_WALL);
			
			if (i % 6 == 0)
			{
				// Place torches.
				BuildingMethods.ReplaceBlock(worldIn, currentPos.offset(playerFacing.rotateYCCW(), 2).up(2), Blocks.TORCH);
				BuildingMethods.ReplaceBlock(worldIn, currentPos.offset(playerFacing.rotateY(), 2).up(2), Blocks.TORCH);
			}
		}
		
		return true;
	}
}
