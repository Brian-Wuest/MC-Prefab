package com.wuest.prefab.Structures.Config;

import com.wuest.prefab.Blocks.FullDyeColor;
import com.wuest.prefab.ModRegistry;
import com.wuest.prefab.Structures.Predefined.StructureProduceFarm;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

/**
 * @author WuestMan
 */
public class ProduceFarmConfiguration extends StructureConfiguration {
    private static String dyeColorTag = "dyeColor";
    public FullDyeColor dyeColor;

    @Override
    public void Initialize() {
        super.Initialize();
        this.houseFacing = Direction.NORTH;
        this.dyeColor = FullDyeColor.CYAN;
    }

    @Override
    protected void CustomReadFromNBTTag(CompoundNBT messageTag, StructureConfiguration config) {
        if (messageTag.contains(ProduceFarmConfiguration.dyeColorTag)) {
            ((ProduceFarmConfiguration) config).dyeColor = FullDyeColor.ById(messageTag.getInt(ProduceFarmConfiguration.dyeColorTag));
        }
    }

    @Override
    protected CompoundNBT CustomWriteToCompoundNBT(CompoundNBT tag) {
        tag.putInt(ProduceFarmConfiguration.dyeColorTag, this.dyeColor.getId());

        return tag;
    }

    /**
     * Custom method to read the CompoundNBT message.
     *
     * @param messageTag The message to create the configuration from.
     * @return An new configuration object with the values derived from the CompoundNBT.
     */
    @Override
    public ProduceFarmConfiguration ReadFromCompoundNBT(CompoundNBT messageTag) {
        ProduceFarmConfiguration config = new ProduceFarmConfiguration();

        return (ProduceFarmConfiguration) super.ReadFromCompoundNBT(messageTag, config);
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
        StructureProduceFarm structure = StructureProduceFarm.CreateInstance(StructureProduceFarm.ASSETLOCATION, StructureProduceFarm.class);

        if (structure.BuildStructure(this, world, hitBlockPos, Direction.NORTH, player)) {
            this.RemoveStructureItemFromPlayer(player, ModRegistry.ProduceFarm.get());
        }
    }
}