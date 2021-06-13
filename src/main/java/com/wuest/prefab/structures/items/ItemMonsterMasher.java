package com.wuest.prefab.structures.items;

import com.wuest.prefab.Prefab;
import com.wuest.prefab.structures.gui.GuiMonsterMasher;
import com.wuest.prefab.structures.predefined.StructureMonsterMasher;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * @author WuestMan
 */
public class ItemMonsterMasher extends StructureItem {
    public ItemMonsterMasher(String name) {
        super(name);
    }

    /**
     * Initializes common fields/properties for this structure item.
     */
    @Override
    protected void PostInit() {
        if (Prefab.proxy.isClient) {
            this.RegisterGui(GuiMonsterMasher.class);
        }
    }

    @Override
    public void scanningMode(EntityPlayer player, World world, BlockPos hitBlockPos, EnumHand hand) {
        StructureMonsterMasher.ScanStructure(
                world,
                hitBlockPos,
                player.getHorizontalFacing());
    }
}
