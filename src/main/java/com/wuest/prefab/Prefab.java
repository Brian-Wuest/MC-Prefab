package com.wuest.prefab;

import com.wuest.prefab.Gui.ConfigGuiFactory;
import com.wuest.prefab.Gui.GuiPrefab;
import com.wuest.prefab.Proxy.ClientProxy;
import com.wuest.prefab.Proxy.CommonProxy;

import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.simple.SimpleChannel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The starting point to load all of the blocks, items and other objects associated with this mod.
 * 
 * @author WuestMan
 *
 */
@Mod(Prefab.MODID)
public class Prefab
{
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

    // Directly reference a log4j logger.
    public static final Logger LOGGER = LogManager.getLogger();
    
    public static final String PROTOCOL_VERSION = Integer.toString(1);
	
	static
	{
		Prefab.isDebug = java.lang.management.ManagementFactory.getRuntimeMXBean().getInputArguments().toString().contains("-agentlib:jdwp");
	}
	
    public Prefab() {
        // Register the setup method for mod-loading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);

        // Register the doClientStuff method for mod-loading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);
        this.proxy = DistExecutor.runForDist(() -> ClientProxy::new, () -> CommonProxy::new);
    }
    
    private void setup(final FMLCommonSetupEvent event)
    {
        this.proxy.preInit(event);
        this.proxy.init(event);
        this.proxy.postinit(event);
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
    }
}
