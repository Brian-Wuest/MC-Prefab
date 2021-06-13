package com.wuest.prefab.structures.items;

import com.wuest.prefab.ModRegistry;
import com.wuest.prefab.Prefab;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
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
        super(name, ModRegistry.GuiModerateHouse);
    }

    /**
     * Does something when the item is right-clicked.
     */
    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos hitBlockPos, EnumHand hand, EnumFacing side, float hitX,
                                      float hitY, float hitZ) {
        if (world.isRemote) {
            if (side == EnumFacing.UP) {
                // Open the client side gui to determine the house options.
                //StructureModerateHouse structure = new StructureModerateHouse();
                //structure.ScanStructure(world, hitBlockPos, player.getHorizontalFacing(), ModerateHouseConfiguration.HouseStyle.JUNGLE_TREE_HOME);

                player.openGui(Prefab.instance, this.guiId, player.world, hitBlockPos.getX(), hitBlockPos.getY(), hitBlockPos.getZ());
                return EnumActionResult.PASS;
            }
        }

        return EnumActionResult.FAIL;
    }
}
