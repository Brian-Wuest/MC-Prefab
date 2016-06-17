package com.wuest.prefab.Items;

import com.wuest.prefab.Blocks.IMetaBlock;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockMeta extends ItemBlock
{
	   public ItemBlockMeta(Block block) {
	        super(block);
	        if (!(block instanceof IMetaBlock)) {
	            throw new IllegalArgumentException(String.format("The given Block %s is not an instance of IMetaBlockName!", block.getUnlocalizedName()));
	        }
	        this.setMaxDamage(0);
	        this.setHasSubtypes(true);
	    }
	   
	    public int getMetadata(int damage)
	    {
	        return damage;
	    }

	    @Override
	    public String getUnlocalizedName(ItemStack stack) {
	        return super.getUnlocalizedName(stack) + "." + ((IMetaBlock)this.block).getSpecialName(stack);
	    }
}
