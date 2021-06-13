package com.wuest.prefab.items;

import com.wuest.prefab.blocks.IMetaBlock;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

/**
 * @author WuestMan
 */
public class ItemBlockMeta extends ItemBlock {
    public ItemBlockMeta(Block block) {
        super(block);

        if (!(block instanceof IMetaBlock)) {
            throw new IllegalArgumentException(String.format("The given Block %s is not an instance of IMetaBlockName!", block.getTranslationKey()));
        }

        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }

    public int getMetadata(int damage) {
        return damage;
    }

    @Override
    public String getTranslationKey(ItemStack stack) {
        return super.getTranslationKey(stack) + "." + ((IMetaBlock) this.block).getSpecialName(stack);
    }
}
