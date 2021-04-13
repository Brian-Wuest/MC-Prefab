package com.wuest.prefab;

import com.wuest.prefab.Config.ModConfiguration;
import com.wuest.prefab.Events.ModEventHandler;
import com.wuest.prefab.Proxy.ClientProxy;
import com.wuest.prefab.Proxy.CommonProxy;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.block.material.PushReaction;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Path;

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

	static {
		Prefab.isDebug = java.lang.management.ManagementFactory.getRuntimeMXBean().getInputArguments().toString().contains("-agentlib:jdwp");
	}

	public Prefab() {
		// Register the blocks and items for this mod.
		ModRegistry.BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
		ModRegistry.ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());

		// Register the setup method for mod-loading
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);

		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);

		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::modConfigReload);

		Prefab.proxy = DistExecutor.safeRunForDist(() -> ClientProxy::new, () -> CommonProxy::new);

		Prefab.proxy.RegisterEventHandler();

		ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.DISPLAYTEST, () -> Pair.of(() -> "ANY", (remote, isServer) -> true));
	}

	private void setup(final FMLCommonSetupEvent event) {
		Prefab.proxy.preInit(event);
		Prefab.proxy.init(event);
		Prefab.proxy.postinit(event);
	}

	private void doClientStuff(final FMLClientSetupEvent event) {
		Prefab.proxy.clientSetup(event);
	}

	private void modConfigReload(ModConfig.Loading loadingEvent) {
		if (loadingEvent.getConfig().getModId().equals(Prefab.MODID)) {
			Prefab.LOGGER.info("Reloading mod configuration.");
			ForgeConfigSpec spec = loadingEvent.getConfig().getSpec();
			Path configPath = loadingEvent.getConfig().getFullPath();
			ModConfiguration.loadConfig(spec, configPath);
		}
	}
}
