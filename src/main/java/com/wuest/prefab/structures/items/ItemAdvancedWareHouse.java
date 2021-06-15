package com.wuest.prefab.structures.items;

import com.wuest.prefab.Prefab;
import com.wuest.prefab.structures.gui.GuiAdvancedWareHouse;
import com.wuest.prefab.structures.predefined.StructureWarehouse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * @author WuestMan
 */
public class ItemAdvancedWareHouse extends ItemWareHouse {
    public ItemAdvancedWareHouse(String name) {
        super(name);
    }

    /**
     * Initializes common fields/properties for this structure item.
     */
    @Override
    protected void PostInit() {
        if (Prefab.proxy.isClient) {
            this.RegisterGui(GuiAdvancedWareHouse.class);
        }
    }

    @Override
    public void scanningMode(EntityPlayer player, World world, BlockPos hitBlockPos, EnumHand hand) {
        StructureWarehouse.ScanStructure(
                world,
                hitBlockPos,
                player.getHorizontalFacing(),
                false);
    }
}
