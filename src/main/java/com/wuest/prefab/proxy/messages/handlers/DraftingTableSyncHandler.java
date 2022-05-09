package com.wuest.prefab.proxy.messages.handlers;

import com.wuest.prefab.gui.screens.menus.DraftingTableMenu;
import com.wuest.prefab.proxy.messages.DraftingTableSyncMessage;
import com.wuest.prefab.structures.custom.base.CustomStructureInfo;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class DraftingTableSyncHandler {
    public static void handle(final DraftingTableSyncMessage message, Supplier<NetworkEvent.Context> ctx) {
        NetworkEvent.Context context = ctx.get();

        CustomStructureInfo structureInfo = new CustomStructureInfo();
        structureInfo.readFromTag(message.getMessageTag());

        context.enqueueWork(() -> {
            // This is server side. Update the configuration.
            if (context.getSender().containerMenu instanceof DraftingTableMenu draftingTableMenu) {
                draftingTableMenu.setSelectedStructureInfo(structureInfo);
            }
        });

        context.setPacketHandled(true);
    }
}
