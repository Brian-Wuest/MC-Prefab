package com.wuest.prefab.Events;

import com.wuest.prefab.Prefab;
import com.wuest.prefab.Config.ModConfiguration;
import com.wuest.prefab.Proxy.ClientProxy;
import com.wuest.prefab.Render.StructureRenderHandler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;

public class ClientEventHandler
{
	public static int ticksInGame;

	@SubscribeEvent
	public void onWorldRenderLast(RenderWorldLastEvent event)
	{
		Minecraft mc = Minecraft.getMinecraft();

		if (mc.player != null && mc.objectMouseOver != null && mc.objectMouseOver.getBlockPos() != null && (!mc.player.isSneaking()))
		{
			StructureRenderHandler.renderPlayerLook(mc.player, mc.objectMouseOver);
		}
	}

	@SubscribeEvent
	public void onPlayerInteract(PlayerInteractEvent.RightClickBlock event)
	{
		if (event.getWorld().isRemote)
		{
			if (StructureRenderHandler.currentStructure != null && event.getEntityPlayer() == Minecraft.getMinecraft().player)
			{
				StructureRenderHandler.setStructure(null, EnumFacing.NORTH, null);
				event.setCanceled(true);
			}
		}
	}
	
	@SubscribeEvent
	public void OnClientDisconnectEvent(FMLNetworkEvent.ClientDisconnectionFromServerEvent event)
	{
		// When the player logs out, make sure to re-set the server configuration. 
	 	// This is so a new configuration can be successfully loaded when they switch servers or worlds (on single player.
	 	((ClientProxy)Prefab.proxy).serverConfiguration = null;
	}
	
	@SubscribeEvent
	public void ClientTickEnd(ClientTickEvent event)
	{
		if (event.phase == Phase.END)
		{
			GuiScreen gui = Minecraft.getMinecraft().currentScreen;
			
			if (gui == null || !gui.doesGuiPauseGame()) 
			{
				// Reset the ticks in game if we are getting close to the maximum value of an integer.
				if (Integer.MAX_VALUE - 100 == ClientEventHandler.ticksInGame)
				{
					ClientEventHandler.ticksInGame = 1;
				}
				
				ClientEventHandler.ticksInGame++;
			}
		}
	}
}
