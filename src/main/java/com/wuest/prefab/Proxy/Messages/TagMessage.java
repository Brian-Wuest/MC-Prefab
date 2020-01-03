package com.wuest.prefab.Proxy.Messages;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;

/**
 * @author WuestMan
 */
@SuppressWarnings("WeakerAccess")
public class TagMessage {
    protected CompoundNBT tagMessage;

    protected TagMessage() {
    }

    public TagMessage(CompoundNBT tagMessage) {
        this.tagMessage = tagMessage;
    }

    public static <T extends TagMessage> T decode(PacketBuffer buf, Class<T> clazz) {
        T message = null;

        try {
            message = clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }

        assert message != null;
        message.tagMessage = buf.readCompoundTag();
        return message;
    }

    public static <T extends TagMessage> void encode(T message, PacketBuffer buf) {
        buf.writeCompoundTag(message.tagMessage);
    }

    public CompoundNBT getMessageTag() {
        return this.tagMessage;
    }

    public void setMessageTag(CompoundNBT value) {
        this.tagMessage = value;
    }
}
