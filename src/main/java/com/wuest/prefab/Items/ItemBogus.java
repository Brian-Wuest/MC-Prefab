package com.wuest.prefab.Items;

import org.lwjgl.opengl.GL11;

import com.wuest.prefab.ModRegistry;
import com.wuest.prefab.Items.Structures.ItemInstantBridge;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ItemBogus extends Item
{
	public static boolean renderTest = false;
	
	public ItemBogus(String name)
	{
		super();

		this.setCreativeTab(CreativeTabs.MATERIALS);
		ModRegistry.setItemName(this, name);
	}

    /**
     * Called when the equipped item is right clicked.
     */
    public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand)
    {
    	if (worldIn.isRemote)
    	{
    		ItemBogus.renderTest = !ItemBogus.renderTest;
    	}
    	
        return new ActionResult(EnumActionResult.PASS, itemStackIn);
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
	    	boolean selected = (mainHand != null && mainHand.getItem() instanceof ItemBogus)
	    			|| (offHand != null && offHand.getItem() instanceof ItemBogus)? true : false;
	    	
	        if (selected)
	        {
	        	ItemBogus.renderTest = true;
	    		//this.RenderTest(worldIn, entityPlayer);
	        }
	        else
	        {
	        	ItemBogus.renderTest = false;
	        }
    	}
    }
}
