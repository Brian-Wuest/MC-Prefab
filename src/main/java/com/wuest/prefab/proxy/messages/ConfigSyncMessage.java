package com.wuest.prefab.proxy.messages;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;

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
    public ConfigSyncMessage(CompoundTag writeToNBTTagCompound) {
        super(writeToNBTTagCompound);
    }

    public ConfigSyncMessage() {
        super();
    }

    public static ConfigSyncMessage decode(FriendlyByteBuf buf) {
        return TagMessage.decode(buf, ConfigSyncMessage.class);
    }
}
