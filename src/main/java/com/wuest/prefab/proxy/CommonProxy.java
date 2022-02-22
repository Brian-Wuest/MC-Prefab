package com.wuest.prefab.proxy;

import com.wuest.prefab.ModRegistry;
import com.wuest.prefab.Prefab;
import com.wuest.prefab.base.BaseConfig;
import com.wuest.prefab.config.ModConfiguration;
import com.wuest.prefab.config.ServerModConfiguration;
import com.wuest.prefab.config.StructureScannerConfig;
import com.wuest.prefab.crafting.RecipeCondition;
import com.wuest.prefab.crafting.SmeltingCondition;
import com.wuest.prefab.structures.custom.base.CustomStructureInfo;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.ParallelDispatchEvent;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.network.NetworkRegistry;
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
    public static ModConfiguration proxyConfiguration;
    public static ForgeConfigSpec COMMON_SPEC;
    public static Path Config_File_Path;
    public static ArrayList<CustomStructureInfo> CustomStructures;

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
        CraftingHelper.register(new RecipeCondition.Serializer());
        CraftingHelper.register(new SmeltingCondition.Serializer());

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
        ModRegistry.registerCustomStructures();
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
}
