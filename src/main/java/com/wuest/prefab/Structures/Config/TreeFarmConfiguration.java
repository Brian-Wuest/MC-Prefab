package com.wuest.prefab.Structures.Config;

import com.wuest.prefab.ModRegistry;
import com.wuest.prefab.Structures.Predefined.StructureTreeFarm;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

/**
 * @author WuestMan
 */
public class TreeFarmConfiguration extends StructureConfiguration {
    /**
     * Custom method to read the CompoundNBT message.
     *
     * @param messageTag The message to create the configuration from.
     * @return An new configuration object with the values derived from the CompoundNBT.
     */
    @Override
    public TreeFarmConfiguration ReadFromCompoundNBT(CompoundNBT messageTag) {
        TreeFarmConfiguration config = new TreeFarmConfiguration();

        return (TreeFarmConfiguration) super.ReadFromCompoundNBT(messageTag, config);
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
        StructureTreeFarm structure = StructureTreeFarm.CreateInstance(StructureTreeFarm.ASSETLOCATION, StructureTreeFarm.class);
        if (structure.BuildStructure(this, world, hitBlockPos, Direction.NORTH, player)) {
            this.RemoveStructureItemFromPlayer(player, ModRegistry.TreeFarm());
        }
    }
}
