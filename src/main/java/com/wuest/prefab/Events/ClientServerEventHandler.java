package com.wuest.prefab.Events;

import com.wuest.prefab.ModRegistry;
import com.wuest.prefab.Prefab;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * @author WuestMan
 */
@Mod.EventBusSubscriber(modid = Prefab.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientServerEventHandler {
    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        for (Block block : ModRegistry.ModBlocks) {
            Prefab.LOGGER.debug("Logging Block With Name: " + block.getRegistryName() + " and type: " + block.toString());
            event.getRegistry().register(block);
        }

    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll(ModRegistry.ModItems.toArray(new Item[0]));
    }
}
