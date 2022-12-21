package com.wuest.prefab.items;

import com.wuest.prefab.ModRegistry;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.CreativeModeTab;

/**
 * @author WuestMan
 */
public class ItemWoodenCrate extends Item {
    public final CrateType crateType;

    /**
     * Creates a new instance of the ItemWoodenCrateClass.
     */
    public ItemWoodenCrate(CrateType crateType) {
        super(new Item.Properties()
                .craftRemainder(ItemWoodenCrate.getRecipeRemainderForCrateType(crateType)));

        this.crateType = crateType;
    }

    public static ItemBlockWoodenCrate getRecipeRemainderForCrateType(CrateType crateType) {
        if (crateType.isCrateOfFood) {
            return ModRegistry.ItemEmptyCrate.get();
        }

        return null;
    }

    /**
     * This enum is used to identify the crate types.
     *
     * @author WuestMan
     */
    public enum CrateType {
        Empty(0, false),
        Clutch_Of_Eggs(1, false),
        Carton_Of_Eggs(2, true),
        Bunch_Of_Potatoes(3, false),
        Crate_Of_Potatoes(4, true),
        Bunch_Of_Carrots(5, false),
        Crate_Of_Carrots(6, true),
        Bunch_Of_Beets(7, false),
        Crate_Of_Beets(8, true);

        public final int meta;
        public final boolean isCrateOfFood;

        CrateType(int meta, boolean isCrateOfFood) {
            this.meta = meta;
            this.isCrateOfFood = isCrateOfFood;
        }

        public static CrateType getValueFromMeta(int meta) {
            for (CrateType type : CrateType.values()) {
                if (type.meta == meta) {
                    return type;
                }
            }

            return CrateType.Empty;
        }

        @Override
        public String toString() {
            return this.name().toLowerCase();
        }
    }
}
