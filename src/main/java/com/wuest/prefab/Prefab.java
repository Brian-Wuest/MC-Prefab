package com.wuest.prefab;

import com.wuest.prefab.proxy.CommonProxy;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;

/**
 * The starting point to load all of the blocks, items and other objects associated with this mod.
 *
 * @author WuestMan
 */
@Mod(modid = Prefab.MODID, version = Prefab.VERSION, acceptedMinecraftVersions = "[1.12]", guiFactory = "com.wuest.prefab.gui.ConfigGuiFactory", updateJSON = "https://raw.githubusercontent.com/Brian-Wuest/MC-Prefab/master/changeLog.json")
public class Prefab {
    /**
     * This is the ModID
     */
    public static final String MODID = "prefab";

    /**
     * This is the current mod version.
     */
    public static final String VERSION = "@VERSION@";

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
     * This is the static instance of this class.
     */
    @Instance(value = Prefab.MODID)
    public static Prefab instance;

    /**
     * Says where the client and server 'proxy' code is loaded.
     */
    @SidedProxy(clientSide = "com.wuest.prefab.proxy.ClientProxy", serverSide = "com.wuest.prefab.proxy.CommonProxy")
    public static CommonProxy proxy;

    /**
     * The network class used to send messages.
     */
    public static SimpleNetworkWrapper network;

    /**
     * This is the configuration of the mod.
     */
    public static Configuration config;

    static {
        Prefab.isDebug = java.lang.management.ManagementFactory.getRuntimeMXBean().getInputArguments().toString().contains("-agentlib:jdwp");
    }

    /**
     * The pre-initialization event.
     *
     * @param event The event from forge.
     */
    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        Prefab.proxy.preInit(event);
    }

    /**
     * The initialization event.
     *
     * @param event The event from forge.
     */
    @EventHandler
    public void init(FMLInitializationEvent event) {
        Prefab.proxy.init(event);
    }

    /**
     * The post-initialization event.
     *
     * @param event The event from forge.
     */
    @EventHandler
    public void postinit(FMLPostInitializationEvent event) {
        Prefab.proxy.postinit(event);
    }
}
