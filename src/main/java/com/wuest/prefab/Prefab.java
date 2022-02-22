package com.wuest.prefab;

import com.wuest.prefab.items.ItemSickle;
import com.wuest.prefab.proxy.ClientProxy;
import com.wuest.prefab.proxy.CommonProxy;
import com.wuest.prefab.structures.custom.base.CustomStructureInfo;
import com.wuest.prefab.structures.custom.base.ItemInfo;
import net.minecraft.commands.Commands;
import net.minecraft.core.Registry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.simple.SimpleChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Optional;

/**
 * The starting point to load all of the blocks, items and other objects associated with this mod.
 *
 * @author WuestMan
 */
@Mod(Prefab.MODID)
public class Prefab {
    /**
     * Simulates an air block that blocks movement and cannot be moved.
     */
    public static final Material SeeThroughImmovable = new Material(
            MaterialColor.NONE,
            false,
            true,
            true,
            false,
            false,
            false,
            PushReaction.IGNORE);

    /**
     * This is the ModID
     */
    public static final String MODID = "prefab";

    // Directly reference a log4j logger.
    public static final Logger LOGGER = LogManager.getLogger();

    public static final String PROTOCOL_VERSION = Integer.toString(1);

    /**
     * This is used to determine if the mod is currently being debugged.
     */
    public static boolean isDebug = false;

    /**
     * Determines if structure items will scan their defined space or show the build gui. Default is false.
     * Note: this should only be set to true during debug mode.
     */
    public static boolean useScanningMode = false;

    /**
     * Says where the client and server 'proxy' code is loaded.
     */
    public static CommonProxy proxy;

    /**
     * The network class used to send messages.
     */
    public static SimpleChannel network;

    public static Path configFolder;
    public static Path customStructuresFolder;

    static {
        Prefab.isDebug = java.lang.management.ManagementFactory.getRuntimeMXBean().getInputArguments().toString().contains("-agentlib:jdwp");
    }

    public Prefab() {
        // Register the blocks and items for this mod.
        ModRegistry.BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        ModRegistry.ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        ModRegistry.TILE_ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
        ModRegistry.SOUNDS.register(FMLJavaModLoadingContext.get().getModEventBus());

        // Register the setup method for mod-loading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);
        MinecraftForge.EVENT_BUS.addListener(this::serverStart);

        Prefab.proxy = new CommonProxy();
    }

    private void setup(final FMLCommonSetupEvent event) {
        Prefab.proxy.preInit(event);
        Prefab.proxy.init(event);
        Prefab.proxy.postinit(event);
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        Prefab.proxy = new ClientProxy();
        Prefab.proxy.preInit(event);
        Prefab.proxy.init(event);
        Prefab.proxy.postinit(event);

        Prefab.proxy.RegisterEventHandler();

        Prefab.proxy.clientSetup(event);
    }

    // The method that gets called when a server starts up(Singleplayer and multiplayer are both affected)
    public void serverStart(ServerStartingEvent event) {
        // Gets the current server instance.
        MinecraftServer server = event.getServer();

        // Gets the Command manager for the server.
        // This is used to register available commands for the server.
        Commands command = server.getCommands();

        ItemSickle.setEffectiveBlocks();

        // Go through all custom structures to ensure that all items actually exist with the current mods.
        // Print warning messages about invalid custom structures and remove them from the list.
        ArrayList<CustomStructureInfo> structuresToRemove = new ArrayList<>();

        for (CustomStructureInfo info : CommonProxy.CustomStructures) {
            for (ItemInfo itemInfo : info.requiredItems) {
                Optional<Item> registeredItem = Registry.ITEM.getOptional(itemInfo.item);

                if (registeredItem.isPresent()) {
                    itemInfo.registeredItem = registeredItem.get();
                }
                else {
                    Prefab.LOGGER.warn("Unknown item registration: [{}] for file name [{}]", itemInfo.item.toString(), info.infoFileName);
                    structuresToRemove.add(info);
                }
            }
        }

        // Remove any invalid structures.
        for (CustomStructureInfo info : structuresToRemove) {
            Prefab.LOGGER.warn(
                    "Removing invalid structure with file name: {}. This structure is invalid due to unknown items in the required items collection",
                    info.infoFileName);
            CommonProxy.CustomStructures.remove(info);
        }
    }
}
