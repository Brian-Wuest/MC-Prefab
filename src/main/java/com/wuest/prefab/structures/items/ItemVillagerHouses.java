package com.wuest.prefab.structures.items;

import com.wuest.prefab.Prefab;
import com.wuest.prefab.structures.config.VillagerHouseConfiguration;
import com.wuest.prefab.structures.gui.GuiVillagerHouses;
import com.wuest.prefab.structures.predefined.StructureVillagerHouses;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * @author WuestMan
 */
public class ItemVillagerHouses extends StructureItem {
    public ItemVillagerHouses(String name) {
        super(name);

        this.setMaxDamage(10);
        this.setMaxStackSize(1);
    }

    /**
     * Initializes common fields/properties for this structure item.
     */
    @Override
    protected void PostInit() {
        if (Prefab.proxy.isClient) {
            this.RegisterGui(GuiVillagerHouses.class);
        }
    }

    @Override
    public void scanningMode(EntityPlayer player, World world, BlockPos hitBlockPos, EnumHand hand) {
        StructureVillagerHouses.ScanStructure(
                world,
                hitBlockPos,
                player.getHorizontalFacing(),
                VillagerHouseConfiguration.HouseStyle.BLACKSMITH);
    }
}
