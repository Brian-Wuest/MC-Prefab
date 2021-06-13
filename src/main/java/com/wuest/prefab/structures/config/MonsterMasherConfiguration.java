package com.wuest.prefab.structures.config;

import com.wuest.prefab.ModRegistry;
import com.wuest.prefab.blocks.FullDyeColor;
import com.wuest.prefab.structures.predefined.StructureMonsterMasher;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;

/**
 * @author WuestMan
 */
public class MonsterMasherConfiguration extends StructureConfiguration {
    private static String dyeColorTag = "dyeColor";
    public FullDyeColor dyeColor;

    @Override
    public void Initialize() {
        super.Initialize();
        this.houseFacing = EnumFacing.NORTH;
        this.dyeColor = FullDyeColor.CYAN;
    }

    @Override
    protected void CustomReadFromNBTTag(NBTTagCompound messageTag, StructureConfiguration config) {
        if (messageTag.hasKey(MonsterMasherConfiguration.dyeColorTag)) {
            ((MonsterMasherConfiguration) config).dyeColor = FullDyeColor.ById(messageTag.getInteger(MonsterMasherConfiguration.dyeColorTag));
        }
    }

    @Override
    protected NBTTagCompound CustomWriteToCompoundNBT(NBTTagCompound tag) {
        tag.setInteger(MonsterMasherConfiguration.dyeColorTag, this.dyeColor.getId());

        return tag;
    }

    /**
     * Custom method to read the CompoundNBT message.
     *
     * @param messageTag The message to create the configuration from.
     * @return An new configuration object with the values derived from the CompoundNBT.
     */
    @Override
    public MonsterMasherConfiguration ReadFromCompoundNBT(NBTTagCompound messageTag) {
        MonsterMasherConfiguration config = new MonsterMasherConfiguration();

        return (MonsterMasherConfiguration) super.ReadFromCompoundNBT(messageTag, config);
    }

    /**
     * This is used to actually build the structure as it creates the structure instance and calls build structure.
     *
     * @param player      The player which requested the build.
     * @param world       The world instance where the build will occur.
     * @param hitBlockPos This hit block position.
     */
    @Override
    protected void ConfigurationSpecificBuildStructure(EntityPlayer player, WorldServer world, BlockPos hitBlockPos) {
        StructureMonsterMasher structure = StructureMonsterMasher.CreateInstance(StructureMonsterMasher.ASSETLOCATION, StructureMonsterMasher.class);

        if (structure.BuildStructure(this, world, hitBlockPos, EnumFacing.NORTH, player)) {
            this.RemoveStructureItemFromPlayer(player, ModRegistry.MonsterMasher);
        }
    }
}
