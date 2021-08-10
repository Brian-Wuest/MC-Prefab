package com.wuest.prefab.structures.messages;

import com.wuest.prefab.proxy.messages.TagMessage;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;

public class StructureScannerSyncMessage extends TagMessage {
    /**
     * This class is just here to distinguish this message from other messages in the mod.
     *
     * @param writeToNBTTagCompound The NBTTagCompound to write the data too.
     */
    public StructureScannerSyncMessage(CompoundTag writeToNBTTagCompound) {
        super(writeToNBTTagCompound);
    }

    public StructureScannerSyncMessage() {
        super();
    }

    public static StructureScannerSyncMessage decode(FriendlyByteBuf buf) {
        return TagMessage.decode(buf, StructureScannerSyncMessage.class);
    }
}
