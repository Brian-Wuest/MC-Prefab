package com.wuest.prefab.Structures.Events;

import com.wuest.prefab.Structures.Render.StructureRenderHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Direction;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

/**
 * @author WuestMan
 */
@EventBusSubscriber(value =
        {Dist.CLIENT})
public class StructureClientEventHandler {
    /**
     * The player right-click block event. This is used to stop the structure rendering for the preview.
     *
     * @param event The event object.
     */
    @SubscribeEvent
    public void onPlayerInteract(PlayerInteractEvent.RightClickBlock event) {
        if (event.getWorld().isRemote) {
            if (StructureRenderHandler.currentStructure != null && event.getEntityPlayer() == Minecraft.getInstance().player) {
                StructureRenderHandler.setStructure(null, Direction.NORTH, null);
                event.setCanceled(true);
            }
        }
    }
}
