package com.prefab.neoforge.events;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.prefab.ClientModRegistryBase;
import com.prefab.PrefabBase;
import com.prefab.config.ModConfiguration;
import com.prefab.config.RecipeMapGuiProvider;
import com.prefab.config.StructureOptionGuiProvider;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.gui.registry.GuiRegistry;
import net.minecraft.client.KeyMapping;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.event.RegisterShadersEvent;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.client.settings.KeyConflictContext;
import net.neoforged.neoforge.client.settings.KeyModifier;

import java.io.IOException;
import java.util.ArrayList;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_B;

// You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
@EventBusSubscriber(modid = PrefabBase.MODID, value = Dist.CLIENT)
public class ModClientEvents {
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        ClientModRegistryBase.RegisterGuis();

        GuiRegistry registry = AutoConfig.getGuiRegistry(ModConfiguration.class);
        RecipeMapGuiProvider providerMap = new RecipeMapGuiProvider();
        StructureOptionGuiProvider structureOptionGuiProvider = new StructureOptionGuiProvider();

        registry.registerPredicateProvider(providerMap, (field) -> field.getDeclaringClass() == ModConfiguration.class && field.getName().equals("recipes"));
        registry.registerPredicateProvider(structureOptionGuiProvider, (field) -> field.getDeclaringClass() == ModConfiguration.class && field.getName().equals("structureOptions"));

        ModLoadingContext.get().registerExtensionPoint(IConfigScreenFactory.class, () -> (container, parent) -> {
            return AutoConfig.getConfigScreen(ModConfiguration.class, parent).get();
        });
    }

    @SubscribeEvent
    public static void onRegisterShaders(RegisterShadersEvent event) throws IOException {
        /*event.registerShader(
                new ShaderInstance(
                        event.getResourceProvider(),
                        ResourceLocation.fromNamespaceAndPath(PrefabBase.MODID, "ghost_shimmer"),
                        DefaultVertexFormat.NEW_ENTITY
                ),
                shader -> GhostShaders.GHOST_SHIMMER_SHADER = shader
        );*/
    }

    /**
     * Contains the keybindings registered.
     */
    public static ArrayList<KeyMapping> keyBindings = new ArrayList<KeyMapping>();

    @SubscribeEvent
    public static void KeyBindRegistrationEvent(RegisterKeyMappingsEvent event) {
        KeyMapping binding = new KeyMapping("Build Current Structure",
                KeyConflictContext.IN_GAME, KeyModifier.ALT,
                InputConstants.Type.KEYSYM, GLFW_KEY_B, "Prefab - Structure Preview");

        event.register(binding);

        ModClientEvents.keyBindings.add(binding);
    }

}
