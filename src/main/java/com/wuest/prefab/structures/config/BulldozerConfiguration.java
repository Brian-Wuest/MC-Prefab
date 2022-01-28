package com.wuest.prefab.structures.config;

import com.wuest.prefab.ModRegistry;
import com.wuest.prefab.structures.predefined.StructureBulldozer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

/**
 * @author WuestMan
 */
public class BulldozerConfiguration extends StructureConfiguration {

    public boolean creativeMode = false;

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
    public BulldozerConfiguration ReadFromCompoundTag(CompoundTag messageTag) {
        BulldozerConfiguration config = new BulldozerConfiguration();

        return (BulldozerConfiguration) super.ReadFromCompoundTag(messageTag, config);
    }

    /**
     * This is used to actually build the structure as it creates the structure instance and calls build structure.
     *
     * @param player      The player which requested the build.
     * @param world       The world instance where the build will occur.
     * @param hitBlockPos This hit block position.
     */
    @Override
    protected void ConfigurationSpecificBuildStructure(Player player, ServerLevel world, BlockPos hitBlockPos) {
        StructureBulldozer structure = new StructureBulldozer();

        if (player.isCreative()) {
            this.creativeMode = true;
        }

        if (structure.BuildStructure(this, world, hitBlockPos, player)) {
            ItemStack stack = player.getItemInHand(InteractionHand.OFF_HAND);
            InteractionHand hand = InteractionHand.OFF_HAND;

            if (stack.getItem() == ModRegistry.Creative_Bulldozer.get()) {
                this.creativeMode = true;
            }

            if (stack.getItem() != ModRegistry.Bulldozer.get()) {
                stack = player.getItemInHand(InteractionHand.MAIN_HAND);
                hand = InteractionHand.MAIN_HAND;

                if (stack.getItem() == ModRegistry.Creative_Bulldozer.get()) {
                    this.creativeMode = true;
                }
            }

            // Only damage the item if this is the regular bulldozer.
            if (stack.getItem() == ModRegistry.Bulldozer.get()) {
                InteractionHand hand1 = hand;
                stack.hurtAndBreak(1, player, (player1) ->
                {
                    player1.broadcastBreakEvent(hand1);
                });
                player.containerMenu.broadcastChanges();
            }
        }
    }

}