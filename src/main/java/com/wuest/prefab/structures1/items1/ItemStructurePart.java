package com.wuest.prefab.structures.items;

import com.wuest.prefab.ModRegistry;
import com.wuest.prefab.Prefab;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * @author WuestMan
 */
public class ItemStructurePart extends Item {
    /**
     * Get's the GuiId to show to the user when this item is used.
     */
    protected int guiId = ModRegistry.GuiStructurePart;

    public ItemStructurePart(String name) {
        super();

        this.setCreativeTab(CreativeTabs.MISC);
        this.setMaxDamage(10);
        this.setMaxStackSize(1);
        ModRegistry.setItemName(this, name);
    }

    /**
     * Does something when the item is right-clicked.
     */
    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos hitBlockPos, EnumHand hand, EnumFacing side, float hitX, float hitY,
                                      float hitZ) {
        if (world.isRemote) {
            if (side == EnumFacing.UP) {
                // Open the client side gui to determine the house options.
                player.openGui(Prefab.instance, this.guiId, player.world, hitBlockPos.getX(), hitBlockPos.getY(), hitBlockPos.getZ());
                return EnumActionResult.PASS;
            }
        }

        return EnumActionResult.FAIL;
    }
}
