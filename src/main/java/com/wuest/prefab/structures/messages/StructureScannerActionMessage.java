package com.wuest.prefab.structures.messages;

import com.wuest.prefab.proxy.messages.TagMessage;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;

public class StructureScannerActionMessage extends TagMessage {
    /**
     * This class is just here to distinguish this message from other messages in the mod.
     *
     * @param writeToNBTTagCompound The NBTTagCompound to write the data too.
     */
    public StructureScannerActionMessage(CompoundNBT writeToNBTTagCompound) {
        super(writeToNBTTagCompound);
    }

    public StructureScannerActionMessage() {
        super();
    }

    public static StructureScannerActionMessage decode(PacketBuffer buf) {
        return TagMessage.decode(buf, StructureScannerActionMessage.class);
    }
}
