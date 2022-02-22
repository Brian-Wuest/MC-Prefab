package com.wuest.prefab.proxy.messages.handlers;

import com.wuest.prefab.ModRegistry;
import com.wuest.prefab.Prefab;
import com.wuest.prefab.config.ServerModConfiguration;
import com.wuest.prefab.proxy.ClientProxy;
import com.wuest.prefab.proxy.CommonProxy;
import com.wuest.prefab.proxy.messages.CustomStructureSyncMessage;
import com.wuest.prefab.structures.custom.base.CustomStructureInfo;
import net.minecraftforge.network.NetworkEvent;

import java.util.ArrayList;
import java.util.function.Supplier;

public class CustomStructureSyncHandler {
    public static void handle(final CustomStructureSyncMessage message, Supplier<NetworkEvent.Context> ctx) {
        NetworkEvent.Context context = ctx.get();

        context.enqueueWork(() -> {
            // This is client side.
            ArrayList<CustomStructureInfo> tempServerRegisteredStructures = message.getDecodedStructures();

            // Always clear out the list of server registered structures to avoid adding duplicates or from other worlds.
            ClientProxy.ServerRegisteredStructures = new ArrayList<>();

            // Nested loop for server and client custom structures to ensure that everything is in-sync.
            if (tempServerRegisteredStructures != null && CommonProxy.CustomStructures != null) {
                for (CustomStructureInfo serverStructure : tempServerRegisteredStructures) {
                    boolean foundClientStructure = false;

                    for (CustomStructureInfo clientStructure : CommonProxy.CustomStructures) {
                        if (serverStructure.infoFileName.equalsIgnoreCase(clientStructure.infoFileName)) {
                            // Update the server structure to use the client structure file path.
                            // This is for the preview function.
                            // Since this path is important for the client is the only piece of information which should come solely from the client.
                            serverStructure.structureFilePath = clientStructure.structureFilePath;
                            foundClientStructure = true;
                            ClientProxy.ServerRegisteredStructures.add(serverStructure);
                            break;
                        }
                    }

                    if(foundClientStructure) {
                        break;
                    }
                }
            }

        });

        context.setPacketHandled(true);
    }
}
