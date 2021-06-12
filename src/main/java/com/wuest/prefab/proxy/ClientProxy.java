package com.wuest.prefab.proxy;

import com.wuest.prefab.ModRegistry;
import com.wuest.prefab.Prefab;
import com.wuest.prefab.blocks.BlockBoundary;
import com.wuest.prefab.config.ServerModConfiguration;
import com.wuest.prefab.events.ClientEventHandler;
import com.wuest.prefab.gui.GuiPrefab;
import com.wuest.prefab.structures.gui.GuiStructure;
import com.wuest.prefab.structures.items.StructureItem;
import com.wuest.prefab.structures.render.ShaderHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraft.item.ItemUseContext;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Supplier;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_B;

/**
 * @author WuestMan
 */
@SuppressWarnings({"WeakerAccess", "SpellCheckingInspection"})
public class ClientProxy extends CommonProxy {
    /**
     * The hashmap of mod guis.
     */
    public static HashMap<StructureItem, GuiStructure> ModGuis = new HashMap<>();

    public ServerModConfiguration serverConfiguration = null;

    public ClientProxy() {
        super();

        this.isClient = true;
    }

    /**
     * Adds all of the Mod Guis to the HasMap.
     */
    public static void AddGuis() {
    }

    @Override
    public void preInit(FMLCommonSetupEvent event) {
        super.preInit(event);

        ClientProxy.AddGuis();

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
        Optional<? extends ModContainer> modContainer = ModList.get().getModContainerById(Prefab.MODID);

        if (modContainer != null && modContainer.isPresent()) {
            Supplier<BiFunction<Minecraft, Screen, Screen>> prefabGui = () -> GuiPrefab::new;
            modContainer.get().registerExtensionPoint(ExtensionPoint.CONFIGGUIFACTORY, prefabGui);
        }
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

    @Override
    public void openGuiForItem(ItemUseContext itemUseContext) {
        for (Map.Entry<StructureItem, GuiStructure> entry : ClientProxy.ModGuis.entrySet()) {
            if (entry.getKey() == itemUseContext.getItemInHand().getItem()) {
                GuiStructure screen = entry.getValue();
                screen.pos = itemUseContext.getClickedPos();

                Minecraft.getInstance().setScreen(screen);
            }
        }
    }

    @Override
    public void clientSetup(FMLClientSetupEvent clientSetupEvent) {
        this.registerKeyBindings(clientSetupEvent);

        RenderTypeLookup.setRenderLayer(ModRegistry.BlockBoundary.get(), BlockBoundary::canRenderInLayer);

        // This render type (func_228643_e_) is the "cutout" render type.
        RenderTypeLookup.setRenderLayer(ModRegistry.GlassSlab.get(), RenderType.cutout());

        RenderTypeLookup.setRenderLayer(ModRegistry.GlassStairs.get(), RenderType.cutout());

        RenderTypeLookup.setRenderLayer(ModRegistry.PaperLantern.get(), RenderType.cutout());

        // This is the "translucent" type.
        RenderTypeLookup.setRenderLayer(ModRegistry.BlockPhasing.get(), RenderType.translucent());
    }

    private void registerKeyBindings(FMLClientSetupEvent clientSetupEvent) {
        clientSetupEvent.enqueueWork(() -> {
            KeyBinding binding = new KeyBinding("Build Current Structure",
                    KeyConflictContext.IN_GAME, KeyModifier.ALT,
                    InputMappings.Type.KEYSYM, GLFW_KEY_B, "Prefab - Structure Preview");

            ClientEventHandler.keyBindings.add(binding);
            ClientRegistry.registerKeyBinding(binding);
        });
    }
}