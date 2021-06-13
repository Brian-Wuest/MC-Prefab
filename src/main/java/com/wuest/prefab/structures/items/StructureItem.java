package com.wuest.prefab.structures.items;

import com.wuest.prefab.ModRegistry;
import com.wuest.prefab.Prefab;
import com.wuest.prefab.proxy.ClientProxy;
import com.wuest.prefab.structures.gui.GuiStructure;
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
public class StructureItem extends Item {
    /**
     * Initializes a new instance of the StructureItem class.
     */
    public StructureItem(String name) {
        super();

        this.Initialize(name);
    }

    /**
     * Does something when the item is right-clicked.
     */
    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos hitBlockPos, EnumHand hand, EnumFacing side, float hitX,
                                      float hitY, float hitZ) {
        if (world.isRemote) {
            if (side == EnumFacing.UP) {
                if (Prefab.useScanningMode) {
                    this.scanningMode(player, world, hitBlockPos, hand);
                } else {
                    // Open the client side gui to determine the house options.
                    player.openGui(Prefab.instance, 0, player.world, hitBlockPos.getX(), hitBlockPos.getY(), hitBlockPos.getZ());
                }

                return EnumActionResult.PASS;
            }
        }

        return EnumActionResult.FAIL;
    }

    public void scanningMode(EntityPlayer player, World world, BlockPos hitBlockPos, EnumHand hand) {
    }

    /**
     * Initializes common fields/properties for this structure item.
     */
    protected void Initialize(String name) {
        ModRegistry.setItemName(this, name);
        this.setCreativeTab(CreativeTabs.MISC);
        this.PostInit();
    }

    protected void PostInit() {

    }

    protected void RegisterGui(Class<?> classToRegister) {
        try {
            if (Prefab.proxy.isClient) {
                GuiStructure userInterface = (GuiStructure) classToRegister.newInstance();
                ClientProxy.ModGuis.put(this, userInterface);
            }
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
