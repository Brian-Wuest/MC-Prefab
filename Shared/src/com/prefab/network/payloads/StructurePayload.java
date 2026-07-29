package com.prefab.network.payloads;

import com.prefab.PrefabBase;
import com.prefab.structures.messages.StructureTagMessage;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;

public class StructurePayload implements CustomPacketPayload {
    private final StructureTagMessage structureTagMessage;

    public static final CustomPacketPayload.Type<StructurePayload> PACKET_TYPE = new CustomPacketPayload.Type<>(
            Identifier.tryBuild(PrefabBase.MODID, "structure_payload"));

    public static final StreamCodec<FriendlyByteBuf, StructurePayload> STREAM_CODEC = CustomPacketPayload.codec(
            StructurePayload::write,
            StructurePayload::new);

    public StructurePayload(StructureTagMessage structureTagMessage) {
        this.structureTagMessage = structureTagMessage;
    }

    public StructurePayload(FriendlyByteBuf friendlyByteBuf) {
        this(new StructureTagMessage(friendlyByteBuf));
    }

    public void write(FriendlyByteBuf buf) {
        this.structureTagMessage.write(buf);
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return PACKET_TYPE;
    }

    public StructureTagMessage structureTagMessage() {
        return this.structureTagMessage;
    }
}
