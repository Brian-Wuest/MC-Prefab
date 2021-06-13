package com.wuest.prefab.structures.items;

import com.wuest.prefab.Prefab;
import com.wuest.prefab.structures.config.ModerateHouseConfiguration;
import com.wuest.prefab.structures.gui.GuiModerateHouse;
import com.wuest.prefab.structures.predefined.StructureModerateHouse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * @author WuestMan
 */
public class ItemModerateHouse extends StructureItem {
    /**
     * Initializes a new instance of the {@link ItemModerateHouse} class.
     *
     * @param name The name to register this item as.
     */
    public ItemModerateHouse(String name) {
        super(name);
    }

    /**
     * Initializes common fields/properties for this structure item.
     */
    @Override
    protected void PostInit() {
        if (Prefab.proxy.isClient) {
            this.RegisterGui(GuiModerateHouse.class);
        }
    }

    @Override
    public void scanningMode(EntityPlayer player, World world, BlockPos hitBlockPos, EnumHand hand) {
        StructureModerateHouse.ScanStructure(
                world,
                hitBlockPos,
                player.getHorizontalFacing(),
                ModerateHouseConfiguration.HouseStyle.JUNGLE_TREE_HOME);
    }
}
