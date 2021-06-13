package com.wuest.prefab.structures.items;

import com.wuest.prefab.Prefab;
import com.wuest.prefab.structures.gui.GuiProduceFarm;
import com.wuest.prefab.structures.predefined.StructureProduceFarm;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * @author WuestMan
 */
public class ItemProduceFarm extends StructureItem {
    public ItemProduceFarm(String name) {
        super(name);
    }

    /**
     * Initializes common fields/properties for this structure item.
     */
    @Override
    protected void PostInit() {
        if (Prefab.proxy.isClient) {
            this.RegisterGui(GuiProduceFarm.class);
        }
    }

    @Override
    public void scanningMode(EntityPlayer player, World world, BlockPos hitBlockPos, EnumHand hand) {
        StructureProduceFarm.ScanStructure(
                world,
                hitBlockPos,
                player.getHorizontalFacing());
    }
}