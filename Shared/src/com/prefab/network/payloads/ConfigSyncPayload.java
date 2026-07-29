package com.prefab.network.payloads;

import com.prefab.PrefabBase;
import com.prefab.network.message.TagMessage;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;

public class ConfigSyncPayload implements CustomPacketPayload {
    private final TagMessage tagMessage;

    public static final CustomPacketPayload.Type<ConfigSyncPayload> PACKET_TYPE = new CustomPacketPayload.Type<>(
            Identifier.tryBuild(PrefabBase.MODID, "config_sync"));

    public static final StreamCodec<FriendlyByteBuf, ConfigSyncPayload> STREAM_CODEC = CustomPacketPayload.codec(
            ConfigSyncPayload::write,
            ConfigSyncPayload::new);

    public ConfigSyncPayload(TagMessage tagMessage) {
        this.tagMessage = tagMessage;
    }

    public ConfigSyncPayload(FriendlyByteBuf friendlyByteBuf) {
        this(new TagMessage(friendlyByteBuf));
    }

    public void write(FriendlyByteBuf buf) {
        this.tagMessage.write(buf);
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return PACKET_TYPE;
    }

    public TagMessage tagMessage() {
        return this.tagMessage;
    }
}
