package com.wuest.prefab.structures.messages;

import com.wuest.prefab.structures.config.StructureConfiguration;
import com.wuest.prefab.structures.messages.StructureTagMessage.EnumStructureConfiguration;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * @author WuestMan
 */
@SuppressWarnings("ConstantConditions")
public class StructureHandler {
    /**
     * Initializes a new instance of the StructureHandler class.
     */
    public StructureHandler() {
    }

    public static void handle(final StructureTagMessage message, Supplier<NetworkEvent.Context> ctx) {
        NetworkEvent.Context context = ctx.get();

        context.enqueueWork(() -> {
            // This is server side. Build the structure.
            EnumStructureConfiguration structureConfig = message.getStructureConfig();

            StructureConfiguration configuration = structureConfig.structureConfig.ReadFromCompoundTag(message.getMessageTag());
            configuration.BuildStructure(context.getSender(), context.getSender().getLevel());
        });

        context.setPacketHandled(true);
    }

}
