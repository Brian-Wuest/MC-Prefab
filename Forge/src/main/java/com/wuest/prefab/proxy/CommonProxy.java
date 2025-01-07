package com.wuest.prefab.proxy;

import com.wuest.prefab.ModRegistry;
import com.wuest.prefab.Prefab;
import com.wuest.prefab.base.BaseConfig;
import com.wuest.prefab.config.ModConfiguration;
import com.wuest.prefab.config.ServerModConfiguration;
import com.wuest.prefab.config.StructureScannerConfig;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.ParallelDispatchEvent;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.registries.RegisterEvent;
import org.apache.commons.lang3.tuple.Pair;

import java.nio.file.Path;
import java.util.ArrayList;

/**
 * This is the server side proxy.
 *
 * @author WuestMan
 */
@SuppressWarnings({"SpellCheckingInspection", "WeakerAccess"})
public class CommonProxy {
    public static final ResourceKey<CreativeModeTab> CREATIVE_TAB_KEY = ResourceKey.create(Registries.CREATIVE_MODE_TAB, new ResourceLocation(Prefab.MODID, "prefab"));

    public static ModConfiguration proxyConfiguration;
    public static ForgeConfigSpec COMMON_SPEC;
    public static Path Config_File_Path;

    public boolean isClient;
    public ArrayList<StructureScannerConfig> structureScanners;

    public CommonProxy() {
        // Builder.build is called during this method.
        Pair<ModConfiguration, ForgeConfigSpec> commonPair = new ForgeConfigSpec.Builder().configure(ModConfiguration::new);
        COMMON_SPEC = commonPair.getRight();
        proxyConfiguration = commonPair.getLeft();

        CommonProxy.Config_File_Path = FMLPaths.CONFIGDIR.get().resolve("prefab.toml");

        ModConfiguration.loadConfig(CommonProxy.COMMON_SPEC, CommonProxy.Config_File_Path);

        this.isClient = false;
        this.structureScanners = new ArrayList<>();
    }

    /*
     * Methods for ClientProxy to Override
     */
    public void registerRenderers() {
    }

    public void RegisterEventHandler() {
    }

    public void preInit(ParallelDispatchEvent event) {
        this.createNetworkInstance();

        // Register messages.
        ModRegistry.RegisterMessages();

        // Register the capabilities.
        ModRegistry.RegisterCapabilities();
    }

    public void createNetworkInstance() {
        Prefab.network = NetworkRegistry.ChannelBuilder.named(new ResourceLocation(Prefab.MODID, "main_channel"))
                .clientAcceptedVersions(Prefab.PROTOCOL_VERSION::equals)
                .serverAcceptedVersions(Prefab.PROTOCOL_VERSION::equals)
                .networkProtocolVersion(() -> Prefab.PROTOCOL_VERSION)
                .simpleChannel();
    }

    public void init(ParallelDispatchEvent event) {
    }

    public void postinit(ParallelDispatchEvent event) {
    }

    public ServerModConfiguration getServerConfiguration() {
        return CommonProxy.proxyConfiguration.serverConfiguration;
    }

    public void openGuiForItem(UseOnContext itemUseContext) {
    }

    public void openGuiForBlock(BlockPos blockPos, Level world, BaseConfig config) {
    }

    public void clientSetup(FMLClientSetupEvent clientSetupEvent) {
    }

    public static void creativeModeTabRegister(RegisterEvent event) {
        event.register(Registries.CREATIVE_MODE_TAB, helper -> {
            helper.register(CommonProxy.CREATIVE_TAB_KEY, CreativeModeTab.builder().icon(() -> new ItemStack(ModRegistry.ItemLogo.get()))
                    .title(Component.translatable("itemGroup.prefab.logo"))
                    .withLabelColor(0x00FF00)
                    .displayItems((params, output) -> {
                        ModRegistry.ITEMS.getEntries().forEach((reg) ->
                        {
                            Item currentItem = reg.get();

                            // Only accept the structure scanner in the creative menu when this is in debug mode.
                            if (Prefab.isDebug && currentItem == ModRegistry.StructureScannerItem.get()) {
                                output.accept(reg.get());
                            } else if (currentItem == ModRegistry.ItemLogo.get()) {
                                return;
                            }

                            output.accept(new ItemStack(reg.get()));
                        });
                    })
                    .build());
        });
    }
}
