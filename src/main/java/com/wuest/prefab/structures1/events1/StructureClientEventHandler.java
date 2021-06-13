package com.wuest.prefab.structures.events;

import com.wuest.prefab.structures.render.StructureRenderHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

/**
 * @author WuestMan
 */
@EventBusSubscriber(value = {Side.CLIENT})
public class StructureClientEventHandler {
    /**
     * The player right-click block event. This is used to stop the structure rendering for the preview.
     *
     * @param event The event object.
     */
    @SubscribeEvent
    public static void onPlayerInteract(PlayerInteractEvent.RightClickBlock event) {
        if (event.getWorld().isRemote) {
            if (StructureRenderHandler.currentStructure != null && event.getEntityPlayer() == Minecraft.getMinecraft().player) {
                StructureRenderHandler.setStructure(null, EnumFacing.NORTH, null);
                event.setCanceled(true);
            }
        }
    }
}
