package com.wuest.prefab.structures.config;

import com.wuest.prefab.ModRegistry;
import com.wuest.prefab.structures.predefined.StructureBulldozer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * @author WuestMan
 */
public class BulldozerConfiguration extends StructureConfiguration {

    /**
     * Initializes a new instance of the {@link BulldozerConfiguration} class.
     */
    public BulldozerConfiguration() {
    }

    /**
     * Custom method to read the NBTTagCompound message.
     *
     * @param messageTag The message to create the configuration from.
     * @return An new configuration object with the values derived from the NBTTagCompound.
     */
    @Override
    public BulldozerConfiguration ReadFromNBTTagCompound(NBTTagCompound messageTag) {
        BulldozerConfiguration config = new BulldozerConfiguration();

        return (BulldozerConfiguration) super.ReadFromNBTTagCompound(messageTag, config);
    }

    /**
     * This is used to actually build the structure as it creates the structure instance and calls build structure.
     *
     * @param player      The player which requested the build.
     * @param world       The world instance where the build will occur.
     * @param hitBlockPos This hit block position.
     */
    @Override
    protected void ConfigurationSpecificBuildStructure(EntityPlayer player, World world, BlockPos hitBlockPos) {
        StructureBulldozer structure = new StructureBulldozer();

        if (structure.BuildStructure(this, world, hitBlockPos, EnumFacing.NORTH, player)) {
            ItemStack stack = player.getHeldItem(EnumHand.OFF_HAND);

            if (stack.getItem() != ModRegistry.Bulldozer) {
                stack = player.getHeldItem(EnumHand.MAIN_HAND);
            }

            if (stack.getItem() == ModRegistry.Bulldozer) {
                stack.damageItem(1, player);
                player.inventoryContainer.detectAndSendChanges();
            }
        }
    }

}