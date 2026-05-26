package com.prefab.items;

import com.prefab.PrefabBase;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

/**
 * @author WuestMan
 */
public class ItemBlockWoodenCrate extends BlockItem {
    public final ItemWoodenCrate.CrateType crateType;

    /**
     * Creates a new instance of the ItemWoodenCrateClass.
     */
    public ItemBlockWoodenCrate(Block linkedBlock, ItemWoodenCrate.CrateType crateType, Item.Properties properties) {
        super(linkedBlock, properties
                .craftRemainder(ItemWoodenCrate.getRecipeRemainderForCrateType(crateType)));

        this.crateType = crateType;
    }
}
