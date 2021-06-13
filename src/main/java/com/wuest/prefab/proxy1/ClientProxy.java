package com.wuest.prefab.proxy;

import com.wuest.prefab.ModRegistry;
import com.wuest.prefab.Prefab;
import com.wuest.prefab.config.ModConfiguration;
import com.wuest.prefab.events.ClientEventHandler;
import com.wuest.prefab.structures.events.StructureClientEventHandler;
import com.wuest.prefab.structures.render.ShaderHelper;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

/**
 * @author WuestMan
 */
public class ClientProxy extends CommonProxy {
    public static ClientEventHandler clientEventHandler = new ClientEventHandler();
    public static StructureClientEventHandler structureClientEventHandler = new StructureClientEventHandler();
    public ModConfiguration serverConfiguration = null;

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);

        ModRegistry.AddGuis();

        // After all items have been registered and all recipes loaded, register any necessary renderer.
        Prefab.proxy.registerRenderers();
    }

    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);
    }

    @Override
    public void postinit(FMLPostInitializationEvent event) {
        super.postinit(event);
    }

    @Override
    public void registerRenderers() {
        ShaderHelper.Initialize();
    }

    @Override
    public ModConfiguration getServerConfiguration() {
        if (this.serverConfiguration == null) {
            // Get the server configuration.
            return CommonProxy.proxyConfiguration;
        } else {
            return this.serverConfiguration;
        }
    }
}