package com.wuest.prefab.Proxy.Messages;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;

/**
 * This message is used to sync up server saved player information to the client.
 *
 * @author WuestMan
 */
public class PlayerEntityTagMessage extends TagMessage {
    /**
     * Initializes a new instance of the PlayerEntityTagMessage class.
     */
    public PlayerEntityTagMessage() {
    }

    /**
     * Initializes a new instance of the PlayerEntityTagMessage class.
     *
     * @param tagMessage The message to send.
     */
    public PlayerEntityTagMessage(CompoundNBT tagMessage) {
        super(tagMessage);
    }

    public static PlayerEntityTagMessage decode(PacketBuffer buf) {
        return TagMessage.decode(buf, PlayerEntityTagMessage.class);
    }
}