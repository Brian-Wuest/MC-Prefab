package com.wuest.prefab.Structures.Config;

import com.wuest.prefab.ModRegistry;
import com.wuest.prefab.Structures.Predefined.StructureBulldozer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

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
    public BulldozerConfiguration ReadFromCompoundNBT(CompoundNBT messageTag) {
        BulldozerConfiguration config = new BulldozerConfiguration();

        return (BulldozerConfiguration) super.ReadFromCompoundNBT(messageTag, config);
    }

    /**
     * This is used to actually build the structure as it creates the structure instance and calls build structure.
     *
     * @param player      The player which requested the build.
     * @param world       The world instance where the build will occur.
     * @param hitBlockPos This hit block position.
     */
    @Override
    protected void ConfigurationSpecificBuildStructure(PlayerEntity player, ServerWorld world, BlockPos hitBlockPos) {
        StructureBulldozer structure = new StructureBulldozer();

        if (structure.BuildStructure(this, world, hitBlockPos, Direction.NORTH, player)) {
            ItemStack stack = player.getHeldItem(Hand.OFF_HAND);
            Hand hand = Hand.OFF_HAND;

            if (stack.getItem() != ModRegistry.Bulldozer()) {
                stack = player.getHeldItem(Hand.MAIN_HAND);
                hand = Hand.MAIN_HAND;
            }

            if (stack.getItem() == ModRegistry.Bulldozer()) {
                Hand hand1 = hand;
                stack.damageItem(1, player, (player1) ->
                {
                    player1.sendBreakAnimation(hand1);
                });
                player.openContainer.detectAndSendChanges();
            }
        }
    }

}