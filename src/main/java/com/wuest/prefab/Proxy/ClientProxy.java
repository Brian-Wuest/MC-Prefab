package com.wuest.prefab.Proxy;

import com.wuest.prefab.Config.ServerModConfiguration;
import com.wuest.prefab.Events.ClientEventHandler;
import com.wuest.prefab.ModRegistry;
import com.wuest.prefab.Prefab;
import com.wuest.prefab.Structures.Events.StructureClientEventHandler;
import com.wuest.prefab.Structures.Render.ShaderHelper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

/**
 * @author WuestMan
 */
public class ClientProxy extends CommonProxy {
    public static ClientEventHandler clientEventHandler = new ClientEventHandler();
    public static StructureClientEventHandler structureClientEventHandler = new StructureClientEventHandler();
    public ServerModConfiguration serverConfiguration = null;

    public ClientProxy() {
        super();
    }

    @Override
    public void preInit(FMLCommonSetupEvent event) {
        super.preInit(event);

        ModRegistry.AddGuis();

        // After all items have been registered and all recipes loaded, register any necessary renderer.
        Prefab.proxy.registerRenderers();
    }

    @Override
    public void init(FMLCommonSetupEvent event) {
        super.init(event);
    }

    @Override
    public void postinit(FMLCommonSetupEvent event) {
        super.postinit(event);
    }

    @Override
    public void RegisterEventHandler() {
        MinecraftForge.EVENT_BUS.register(ClientProxy.clientEventHandler);
        MinecraftForge.EVENT_BUS.register(ClientProxy.structureClientEventHandler);
    }

    @Override
    public void registerRenderers() {
        ShaderHelper.Initialize();
    }

    @Override
    public ServerModConfiguration getServerConfiguration() {
        if (this.serverConfiguration == null) {
            // Get the server configuration.
            return CommonProxy.proxyConfiguration.serverConfiguration;
        } else {
            return this.serverConfiguration;
        }
    }
}