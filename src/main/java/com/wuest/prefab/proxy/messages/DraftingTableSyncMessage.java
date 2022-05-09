package com.wuest.prefab.proxy.messages;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;

public class DraftingTableSyncMessage extends TagMessage {
    /**
     * This class is just here to distinguish the configuration sync message from other messages in the mod.
     *
     * @param writeToNBTTagCompound The NBTTagCompound to write the data too.
     */
    public DraftingTableSyncMessage(CompoundTag writeToNBTTagCompound) {
        super(writeToNBTTagCompound);
    }

    public DraftingTableSyncMessage() {
        super();
    }

    public static DraftingTableSyncMessage decode(FriendlyByteBuf buf) {
        return TagMessage.decode(buf, DraftingTableSyncMessage.class);
    }
}
