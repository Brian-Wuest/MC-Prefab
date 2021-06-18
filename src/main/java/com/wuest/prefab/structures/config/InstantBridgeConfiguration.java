package com.wuest.prefab.structures.config;

import com.wuest.prefab.ModRegistry;
import com.wuest.prefab.structures.base.EnumStructureMaterial;
import com.wuest.prefab.structures.predefined.StructureInstantBridge;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;

/**
 * @author WuestMan
 */
public class InstantBridgeConfiguration extends StructureConfiguration {
    /**
     * Determines how long this bridge is.
     */
    public int bridgeLength;

    /**
     * Determines the type of material to build the bridge with.
     */
    public EnumStructureMaterial bridgeMaterial;

    /**
     * Determine is a roof is included.
     */
    public boolean includeRoof;

    /**
     * Determines how tall the inside of the bridge is if there is a roof.
     */
    public int interiorHeight;

    /**
     * Initializes any properties for this class.
     */
    @Override
    public void Initialize() {
        super.Initialize();
        this.bridgeLength = 25;
        this.bridgeMaterial = EnumStructureMaterial.Cobblestone;
        this.interiorHeight = 3;
        this.includeRoof = true;
    }

    /**
     * Custom method to read the CompoundNBT message.
     *
     * @param messageTag The message to create the configuration from.
     * @return An new configuration object with the values derived from the CompoundNBT.
     */
    @Override
    public InstantBridgeConfiguration ReadFromCompoundNBT(NBTTagCompound messageTag) {
        InstantBridgeConfiguration config = new InstantBridgeConfiguration();

        return (InstantBridgeConfiguration) super.ReadFromCompoundNBT(messageTag, config);
    }

    @Override
    protected void ConfigurationSpecificBuildStructure(EntityPlayer player, WorldServer world, BlockPos hitBlockPos) {
        StructureInstantBridge structure = StructureInstantBridge.CreateInstance();

        if (structure.BuildStructure(this, world, hitBlockPos, EnumFacing.NORTH, player)) {
            this.DamageHeldItem(player, ModRegistry.InstantBridge);
        }
    }

    /**
     * Custom method which can be overridden to write custom properties to the tag.
     *
     * @param tag The CompoundNBT to write the custom properties too.
     * @return The updated tag.
     */
    @Override
    protected NBTTagCompound CustomWriteToCompoundNBT(NBTTagCompound tag) {
        tag.setInteger("bridgeLength", this.bridgeLength);
        tag.setInteger("bridgeMaterial", this.bridgeMaterial.getNumber());
        tag.setBoolean("includeRoof", this.includeRoof);
        tag.setInteger("interiorHeight", this.interiorHeight);
        return tag;
    }

    /**
     * Custom method to read the CompoundNBT message.
     *
     * @param messageTag The message to create the configuration from.
     * @param config     The configuration to read the settings into.
     */
    @Override
    protected void CustomReadFromNBTTag(NBTTagCompound messageTag, StructureConfiguration config) {
        if (messageTag.hasKey("bridgeLength")) {
            ((InstantBridgeConfiguration) config).bridgeLength = messageTag.getInteger("bridgeLength");
        }

        if (messageTag.hasKey("bridgeMaterial")) {
            ((InstantBridgeConfiguration) config).bridgeMaterial = EnumStructureMaterial.getMaterialByNumber(messageTag.getInteger("bridgeMaterial"));
        }

        if (messageTag.hasKey("includeRoof")) {
            ((InstantBridgeConfiguration) config).includeRoof = messageTag.getBoolean("includeRoof");
        }

        if (messageTag.hasKey("interiorHeight")) {
            ((InstantBridgeConfiguration) config).interiorHeight = messageTag.getInteger("interiorHeight");
        }
    }
}