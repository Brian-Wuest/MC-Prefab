package com.wuest.prefab.Proxy.Messages.Handlers;

import java.util.function.Supplier;

import com.wuest.prefab.Config.EntityPlayerConfiguration;
import com.wuest.prefab.Events.ClientEventHandler;
import com.wuest.prefab.Proxy.Messages.PlayerEntityTagMessage;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;


/**
 * 
 * @author WuestMan
 *
 */
public class PlayerEntityHandler
{
	/**
	 * Initializes a new instance of the StructureHandler class.
	 */
	public PlayerEntityHandler()
	{
	}

	public static void handle(final PlayerEntityTagMessage message, Supplier<NetworkEvent.Context> ctx)
	{
		NetworkEvent.Context context = ctx.get();
		
		context.enqueueWork(new Runnable()
		{
			@Override
			public void run()
			{
				// This is client side.
				CompoundNBT newPlayerTag = Minecraft.getInstance().player.getEntityData();
				newPlayerTag.put(EntityPlayerConfiguration.PLAYER_ENTITY_TAG, message.getMessageTag());
				ClientEventHandler.playerConfig.loadFromNBTTagCompound(message.getMessageTag());
			}
		});

		context.setPacketHandled(true);
	}
}
