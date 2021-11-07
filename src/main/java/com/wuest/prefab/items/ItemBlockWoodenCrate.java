package com.wuest.prefab.items;

import com.wuest.prefab.ModRegistry;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;

/**
 * @author WuestMan
 */
public class ItemBlockWoodenCrate extends BlockItem {
    public final ItemWoodenCrate.CrateType crateType;

    /**
     * Creates a new instance of the ItemWoodenCrateClass.
     */
    public ItemBlockWoodenCrate(Block linkedBlock, ItemWoodenCrate.CrateType crateType) {
        super(linkedBlock, new Properties()
                .tab(ModRegistry.PREFAB_GROUP)
                .craftRemainder(ItemWoodenCrate.getRecipeRemainderForCrateType(crateType)));

        this.crateType = crateType;
    }
}
