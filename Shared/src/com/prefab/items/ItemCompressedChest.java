package com.prefab.items;

import com.prefab.PrefabBase;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

/**
 * This is a condensed chest used in the construction of the warehouse.
 *
 * @author WuestMan
 */
public class ItemCompressedChest extends Item {
    /**
     * Initializes a new instance of the ItemCondensedChest class.
     */
    public ItemCompressedChest() {
        super(new Item.Properties().setId(
                ResourceKey.create(Registries.ITEM,
                        ResourceLocation.fromNamespaceAndPath(PrefabBase.MODID, "item_compressed_chest"))));

    }
}