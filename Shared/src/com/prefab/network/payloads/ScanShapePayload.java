package com.prefab.network.payloads;

import com.prefab.PrefabBase;
import com.prefab.network.message.ScannerInfo;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class ScanShapePayload implements CustomPacketPayload {
    private final ScannerInfo scannerInfo;

    public static final Type<ScanShapePayload> PACKET_TYPE = new Type<>(
            Objects.requireNonNull(Identifier.tryBuild(PrefabBase.MODID, "structure_scanner_action")));

    public static final StreamCodec<FriendlyByteBuf, ScanShapePayload> STREAM_CODEC = CustomPacketPayload.codec(
            ScanShapePayload::write,
            ScanShapePayload::new);

    public ScanShapePayload(ScannerInfo scannerInfo) {
        this.scannerInfo = scannerInfo;
    }

    public ScanShapePayload(FriendlyByteBuf friendlyByteBuf) {
        this(new ScannerInfo(friendlyByteBuf));
    }

    public void write(FriendlyByteBuf buf) {
        this.scannerInfo.write(buf);
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return PACKET_TYPE;
    }

    public ScannerInfo scannerInfo() {
        return this.scannerInfo;
    }
}
