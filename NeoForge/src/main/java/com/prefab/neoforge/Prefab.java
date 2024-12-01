package com.prefab.neoforge;

import com.prefab.PrefabBase;
import com.prefab.config.ModConfiguration;
import com.prefab.neoforge.events.GameServerEvents;
import com.prefab.neoforge.network.NetworkWrapper;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.registries.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(PrefabBase.MODID)
public class Prefab
{   // Create a Deferred Register to hold CreativeModeTabs which will all be registered under the "examplemod" namespace
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, PrefabBase.MODID);

    public GameServerEvents serverEvents;
    public ModRegistry modRegistry;

    // The constructor for the mod class is the first code that is run when your mod is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
    public Prefab(IEventBus modEventBus, ModContainer modContainer)
    {
        this.modRegistry = new ModRegistry();
        PrefabBase.eventCaller = new EventCaller();
        PrefabBase.networkWrapper = new NetworkWrapper();
        this.serverEvents = new GameServerEvents();

        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // Add listener for general registration event.
        modEventBus.addListener(this.modRegistry::register);

        // Add listener for general register payload event.
        modEventBus.addListener(this.modRegistry::registerPayLoads);

        NeoForge.EVENT_BUS.register(this.serverEvents);

        // Register the Deferred Register to the mod event bus so tabs get registered
        CREATIVE_MODE_TABS.register(modEventBus);

        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        AutoConfig.register(ModConfiguration.class, GsonConfigSerializer::new);

        PrefabBase.serverConfiguration = new ModConfiguration();
        ModConfiguration config = AutoConfig.getConfigHolder(ModConfiguration.class).getConfig();

        // Make sure the static mod configuration object is separate from the object loaded from the file system.
        // This way we don't have issues when players swap between servers and local worlds.
        CompoundTag tag = config.writeCompoundTag();
        PrefabBase.configuration = new ModConfiguration();
        PrefabBase.configuration.readFromTag(tag);
    }

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event)
    {
    }
}
