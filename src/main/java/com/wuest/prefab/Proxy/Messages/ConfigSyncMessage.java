package com.wuest.prefab.Proxy.Messages;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;

/**
 * @author WuestMan
 */
@SuppressWarnings("unused")
public class ConfigSyncMessage extends TagMessage {
    /**
     * This class is just here to distinguish the configuration sync message from other messages in the mod.
     *
     * @param writeToNBTTagCompound The NBTTagCompound to write the data too.
     */
    public ConfigSyncMessage(CompoundNBT writeToNBTTagCompound) {
        super(writeToNBTTagCompound);
    }

    public ConfigSyncMessage() {
        super();
    }

    public static ConfigSyncMessage decode(PacketBuffer buf) {
        return TagMessage.decode(buf, ConfigSyncMessage.class);
    }
}
