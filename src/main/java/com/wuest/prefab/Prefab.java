package com.wuest.prefab;

import com.wuest.prefab.Proxy.ClientProxy;
import com.wuest.prefab.Proxy.CommonProxy;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.block.material.PushReaction;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The starting point to load all of the blocks, items and other objects associated with this mod.
 *
 * @author WuestMan
 */
@SuppressWarnings({"WeakerAccess", "SpellCheckingInspection"})
@Mod(Prefab.MODID)
public class Prefab {
    /**
     * Simulates an air block that blocks movement and cannot be moved.
     */
    public static final Material SeeThroughImmovable = new Material(
            MaterialColor.AIR,
            false,
            true,
            true,
            false,
            true,
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
     * This is the static instance of this class.
     */
    public static Prefab instance;

    /**
     * Says where the client and server 'proxy' code is loaded.
     */
    public static CommonProxy proxy;

    /**
     * The network class used to send messages.
     */
    public static SimpleChannel network;

    static {
        Prefab.isDebug = java.lang.management.ManagementFactory.getRuntimeMXBean().getInputArguments().toString().contains("-agentlib:jdwp");
    }

    public Prefab() {
        // Register the setup method for mod-loading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);

        Prefab.proxy = DistExecutor.runForDist(() -> ClientProxy::new, () -> CommonProxy::new);

        ModRegistry.RegisterModComponents();

        Prefab.proxy.RegisterEventHandler();
    }

    private void setup(final FMLCommonSetupEvent event) {
        Prefab.proxy.preInit(event);
        Prefab.proxy.init(event);
        Prefab.proxy.postinit(event);
    }
}
