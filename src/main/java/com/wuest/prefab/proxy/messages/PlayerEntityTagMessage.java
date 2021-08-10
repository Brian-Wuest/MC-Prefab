package com.wuest.prefab.proxy.messages;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;

/**
 * This message is used to sync up server saved player information to the client.
 *
 * @author WuestMan
 */
public class PlayerEntityTagMessage extends TagMessage {
    /**
     * Initializes a new instance of the PlayerEntityTagMessage class.
     *
     * @param tagMessage The message to send.
     */
    public PlayerEntityTagMessage(CompoundTag tagMessage) {
        super(tagMessage);
    }

    public PlayerEntityTagMessage() {
        super();
    }

    public static PlayerEntityTagMessage decode(FriendlyByteBuf buf) {
        return TagMessage.decode(buf, PlayerEntityTagMessage.class);
    }
}