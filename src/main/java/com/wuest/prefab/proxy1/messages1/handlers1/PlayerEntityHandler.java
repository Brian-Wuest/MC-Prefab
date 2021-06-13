package com.wuest.prefab.proxy.messages.handlers;

import com.wuest.prefab.config.EntityPlayerConfiguration;
import com.wuest.prefab.events.ClientEventHandler;
import com.wuest.prefab.proxy.messages.PlayerEntityTagMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * @author WuestMan
 */
public class PlayerEntityHandler implements IMessageHandler<PlayerEntityTagMessage, IMessage> {
    /**
     * Initializes a new instance of the StructureHandler class.
     */
    public PlayerEntityHandler() {
    }

    @Override
    public IMessage onMessage(final PlayerEntityTagMessage message, final MessageContext ctx) {
        // Or Minecraft.getMinecraft() on the client.
        IThreadListener mainThread = Minecraft.getMinecraft();

        mainThread.addScheduledTask(new Runnable() {
            @Override
            public void run() {
                // This is client side.
                NBTTagCompound newPlayerTag = Minecraft.getMinecraft().player.getEntityData();
                newPlayerTag.setTag(EntityPlayerConfiguration.PLAYER_ENTITY_TAG, message.getMessageTag());
                ClientEventHandler.playerConfig.loadFromNBTTagCompound(message.getMessageTag());
            }
        });

        // no response in this case
        return null;
    }
}
