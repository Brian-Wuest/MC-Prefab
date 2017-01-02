package com.wuest.prefab.Items;

import com.wuest.prefab.*;
import com.wuest.prefab.Gui.GuiLangKeys;

import net.minecraft.block.BlockLiquid;
import net.minecraft.creativetab.CreativeTabs;
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

/**
 * This is the instant bridge item.
 * @author WuestMan
 *
 */
public class ItemInstantBridge extends Item
{
	public ItemInstantBridge(String name)
	{
		super();

		this.setCreativeTab(CreativeTabs.MISC);
		this.setMaxDamage(10);
		this.setMaxStackSize(1);
		ModRegistry.setItemName(this, name);
	}
	
	@Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer player, EnumHand hand)
    {
		if (!worldIn.isRemote)
		{
	        Vec3d vec3d = player.getPositionEyes(1.0F);
	        Vec3d vec3d1 = player.getLook(1.0F);
	        Vec3d vec3d2 = vec3d.addVector(vec3d1.xCoord * 5.0F, vec3d1.yCoord * 5.0F, vec3d1.zCoord * 5.0F);
	        RayTraceResult result = worldIn.rayTraceBlocks(vec3d, vec3d2, true, false, true);
			
	        BlockPos blockPos = null;
	        
	        if (result.typeOfHit == Type.MISS)
	        {
	        	// The block position is the player's current position.
	        	blockPos = player.getPosition().down(2);
	        }
	        else if (result.typeOfHit == Type.BLOCK)
	        {
		        blockPos = result.getBlockPos();
	        }
	        
	        if (blockPos != null && ((result.typeOfHit == Type.BLOCK && worldIn.getBlockState(blockPos).getBlock() instanceof BlockLiquid)
	        		|| result.typeOfHit == Type.MISS))
	        {
	        	// Found a liquid block for where the player was looking, build the bridge.
	        	if (this.BuildBridge(worldIn, player, blockPos))
	        	{
	        		ItemStack stack = player.getHeldItem(hand);
	        		
	        		stack.damageItem(1, player);
					
					player.inventoryContainer.detectAndSendChanges();
	        	}
	        }
		}
        
        return new ActionResult(EnumActionResult.PASS, player.getHeldItem(hand));
    }
	
	private boolean BuildBridge(World worldIn, EntityPlayer playerIn, BlockPos originalBlockPos)
	{
		EnumFacing playerFacing = playerIn.getHorizontalFacing();
		
		BlockPos startingPos = originalBlockPos.offset(playerFacing.rotateYCCW(), 2);
		BlockPos endPos = originalBlockPos.offset(playerFacing, 50).offset(playerFacing.rotateY(), 2).up(2);
		
		if (!BuildingMethods.CheckBuildSpaceForAllowedBlockReplacement(null, worldIn, startingPos, endPos, playerIn))
		{
			playerIn.addChatComponentMessage(new TextComponentTranslation(GuiLangKeys.GUI_STRUCTURE_NOBUILD).setStyle(new Style().setColor(TextFormatting.GREEN)), true);
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
