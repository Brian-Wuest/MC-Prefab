package com.wuest.prefab.Items;

import com.wuest.prefab.*;
import com.wuest.prefab.Config.BasicStructureConfiguration;
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
	
	public ItemInstantBridge(String name)
	{
		super();

		this.setCreativeTab(CreativeTabs.MISC);
		this.setMaxDamage(10);
		this.setMaxStackSize(1);
		ModRegistry.setItemName(this, name);
	}
	
	@Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer player, EnumHand hand)
    {
		if (!worldIn.isRemote)
		{
	        RayTraceResult result = ItemInstantBridge.RayTrace(worldIn, player, 5.0F, true);	
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
	
    /**
     * Called each tick as long the item is on a player inventory. Uses by maps to check if is on a player hand and
     * update it's contents.
     */
    @Override
	public void onUpdate(ItemStack stack, World worldIn, Entity player, int itemSlot, boolean isSelected)
    {
    	if (player instanceof EntityPlayer && worldIn.isRemote)
    	{
	    	EntityPlayer entityPlayer = (EntityPlayer)player;
	    	
	    	ItemStack mainHand = entityPlayer.getHeldItemMainhand();
	    	ItemStack offHand = entityPlayer.getHeldItemOffhand();
	    	
	    	// Check to see if this type of item is in one of the player's hands.
	    	boolean selected = (mainHand != null && mainHand.getItem() instanceof ItemInstantBridge)
	    			|| (offHand != null && offHand.getItem() instanceof ItemInstantBridge)? true : false;
	    	
	        if (selected)
	        {
	        	RayTraceResult result = ItemInstantBridge.RayTrace(worldIn, entityPlayer, 5.0F, true);	
	        	BlockPos blockPos = null;
	        	
	        	blockPos = result.typeOfHit == Type.MISS ? entityPlayer.getPosition().down(1) : result.getBlockPos();

		        if (blockPos != null && ((result.typeOfHit == Type.BLOCK && worldIn.getBlockState(blockPos).getBlock() instanceof BlockLiquid)
		        		|| result.typeOfHit == Type.MISS))
		        {
		        	if (result.typeOfHit == Type.BLOCK)
		        	{
		        		blockPos = blockPos.up();
		        	}
		        	
		        	StructureBasic basic = ((ItemInstantBridge)stack.getItem()).basic;
		        	BasicStructureConfiguration config = ((ItemInstantBridge)stack.getItem()).config;
		        	
		        	if (basic == null)
		        	{
			        	basic = StructureBasic.CreateInstance("assets/prefab/structures/instant_bridge.zip", StructureBasic.class);
			        	basic.setName("instant_bridge");
			        	config = new BasicStructureConfiguration();
			        	
			        	config.pos = new BlockPos(0, 0, 0);
			        	((ItemInstantBridge)stack.getItem()).basic = basic;
			        	((ItemInstantBridge)stack.getItem()).config = config;
		        	}
		        	
		        	if (config.houseFacing != entityPlayer.getHorizontalFacing().getOpposite()
		        			|| config.pos.getDistance(blockPos.getX(), blockPos.getY(), blockPos.getZ()) != 0)
		        	{
		        		config.pos = blockPos;
		        		config.houseFacing = entityPlayer.getHorizontalFacing().getOpposite();
			        	StructureRenderHandler.setStructure(basic, EnumFacing.NORTH, config);
			        	StructureRenderHandler.showedMessage = true;
		        	}
		        }
		        else
		        {
		        	StructureRenderHandler.setStructure(null, EnumFacing.NORTH, null);
		        }
	        }
	        else
	        {
	        	if (StructureRenderHandler.currentStructure != null && StructureRenderHandler.currentStructure instanceof StructureBasic
	        			&& StructureRenderHandler.currentStructure.getName().equals("instant_bridge"))
	        	{
	        		((ItemInstantBridge)stack.getItem()).basic = null;
	        		((ItemInstantBridge)stack.getItem()).config = null;
	        		StructureRenderHandler.setStructure(null, EnumFacing.NORTH, null);
	        	}
	        }
    	}
    }
    
    public static RayTraceResult RayTrace(World world, EntityPlayer player, float distance, boolean includeFluids)
    {
        Vec3d vec3d = new Vec3d(player.posX, player.posY + (double)player.getEyeHeight(), player.posZ);
        Vec3d vec3d1 = player.getLook(1.0F);
        Vec3d vec3d2 = vec3d.addVector(vec3d1.xCoord * distance, vec3d1.yCoord * distance, vec3d1.zCoord * distance);
        return world.rayTraceBlocks(vec3d, vec3d2, includeFluids, false, true);
    }
	
 	private boolean BuildBridge(World worldIn, EntityPlayer playerIn, BlockPos originalBlockPos)
	{
		EnumFacing playerFacing = playerIn.getHorizontalFacing();
		
		BlockPos startingPos = originalBlockPos.offset(playerFacing.rotateYCCW(), 2);
		BlockPos endPos = originalBlockPos.offset(playerFacing, 50).offset(playerFacing.rotateY(), 2).up(2);
		
		if (!BuildingMethods.CheckBuildSpaceForAllowedBlockReplacement(null, worldIn, startingPos, endPos, playerIn))
		{
			playerIn.addChatComponentMessage(new TextComponentTranslation(GuiLangKeys.GUI_STRUCTURE_NOBUILD).setStyle(new Style().setColor(TextFormatting.GREEN)));
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
