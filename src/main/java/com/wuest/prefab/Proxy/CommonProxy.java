package com.wuest.prefab.Proxy;

import com.wuest.prefab.Config.ModConfiguration;
import com.wuest.prefab.Config.ServerModConfiguration;
import com.wuest.prefab.Crafting.RecipeCondition;
import com.wuest.prefab.Events.ModEventHandler;
import com.wuest.prefab.ModRegistry;
import com.wuest.prefab.Prefab;
import com.wuest.prefab.Structures.Events.StructureEventHandler;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.fml.network.NetworkRegistry;
import org.apache.commons.lang3.tuple.Pair;

/**
 * This is the server side proxy.
 *
 * @author WuestMan
 */
public class CommonProxy {
    public static ModEventHandler eventHandler = new ModEventHandler();
    public static StructureEventHandler structureEventHandler = new StructureEventHandler();
    public static ModConfiguration proxyConfiguration;
    public static ForgeConfigSpec COMMON_SPEC;

    public CommonProxy() {
        // Builder.build is called during this method.
        Pair<ModConfiguration, ForgeConfigSpec> commonPair = new ForgeConfigSpec.Builder().configure(ModConfiguration::new);
        COMMON_SPEC = commonPair.getRight();
        proxyConfiguration = commonPair.getLeft();
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, COMMON_SPEC);

        ModConfiguration.loadConfig(CommonProxy.COMMON_SPEC, FMLPaths.CONFIGDIR.get().resolve("prefab.toml"));
    }

    /*
     * Methods for ClientProxy to Override
     */
    public void registerRenderers() {
    }

    public void RegisterEventHandler() {
        //FMLJavaModLoadingContext.get().getModEventBus().register(CommonProxy.eventHandler);
        //FMLJavaModLoadingContext.get().getModEventBus().register(CommonProxy.structureEventHandler);
    }

    public void preInit(FMLCommonSetupEvent event) {
        CraftingHelper.register(RecipeCondition.KEY, new RecipeCondition());

        Prefab.network = NetworkRegistry.ChannelBuilder.named(new ResourceLocation(Prefab.MODID, "main_channel"))
                .clientAcceptedVersions(Prefab.PROTOCOL_VERSION::equals)
                .serverAcceptedVersions(Prefab.PROTOCOL_VERSION::equals)
                .networkProtocolVersion(() -> Prefab.PROTOCOL_VERSION)
                .simpleChannel();

        // Register messages.
        ModRegistry.RegisterMessages();

        // Register the capabilities.
        ModRegistry.RegisterCapabilities();
    }

    public void init(FMLCommonSetupEvent event) {
    }

    public void postinit(FMLCommonSetupEvent event) {
    }

    public ServerModConfiguration getServerConfiguration() {
        return CommonProxy.proxyConfiguration.serverConfiguration;
    }

    public void openGuiForItem(ItemUseContext itemUseContext)
    {
    }
}
