package com.wuest.prefab.proxy;

import com.mojang.blaze3d.platform.InputConstants;
import com.wuest.prefab.ModRegistry;
import com.wuest.prefab.Prefab;
import com.wuest.prefab.base.BaseConfig;
import com.wuest.prefab.blocks.BlockBoundary;
import com.wuest.prefab.blocks.BlockCustomWall;
import com.wuest.prefab.blocks.BlockGrassSlab;
import com.wuest.prefab.blocks.BlockGrassStairs;
import com.wuest.prefab.config.ServerModConfiguration;
import com.wuest.prefab.config.StructureScannerConfig;
import com.wuest.prefab.events.ClientEventHandler;
import com.wuest.prefab.gui.GuiBase;
import com.wuest.prefab.gui.GuiPrefab;
import com.wuest.prefab.gui.screens.GuiStructureScanner;
import com.wuest.prefab.structures.gui.GuiStructure;
import com.wuest.prefab.structures.items.StructureItem;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.GrassColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.client.ConfigGuiHandler;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.ParallelDispatchEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

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
        for (Consumer<Object> consumer : ModRegistry.guiRegistrations) {
            consumer.accept(null);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static void RegisterBlockRenderer() {
        // Register the block renderer.
        Minecraft.getInstance().getBlockColors().register((state, worldIn, pos, tintIndex) -> worldIn != null && pos != null
                ? BiomeColors.getAverageGrassColor(worldIn, pos)
                : GrassColor.get(0.5D, 1.0D), ModRegistry.GrassWall.get(), ModRegistry.GrassSlab.get(), ModRegistry.GrassStairs.get());

        // Register the item renderer.
        Minecraft.getInstance().getItemColors().register((stack, tintIndex) -> {
            // Get the item for this stack.
            Item item = stack.getItem();

            if (item instanceof BlockItem) {
                // Get the block for this item and determine if it's a grass stairs.
                BlockItem itemBlock = (BlockItem) item;
                boolean paintBlock = false;

                if (itemBlock.getBlock() instanceof BlockCustomWall) {
                    BlockCustomWall customWall = (BlockCustomWall) itemBlock.getBlock();

                    if (customWall.BlockVariant == BlockCustomWall.EnumType.GRASS) {
                        paintBlock = true;
                    }
                } else if (itemBlock.getBlock() instanceof BlockGrassSlab) {
                    paintBlock = true;
                } else if (itemBlock.getBlock() instanceof BlockGrassStairs) {
                    paintBlock = true;
                }

                if (paintBlock) {
                    // TODO: This used to be the getPosition method.
                    BlockPos pos = Minecraft.getInstance().player.blockPosition();
                    ClientLevel world = Minecraft.getInstance().level;
                    return BiomeColors.getAverageGrassColor(world, pos);
                }
            }

            return -1;
        }, new Block[]{ModRegistry.GrassWall.get(), ModRegistry.GrassSlab.get(), ModRegistry.GrassStairs.get()});
    }

    @Override
    public void preInit(ParallelDispatchEvent event) {
        ClientProxy.AddGuis();
    }

    @Override
    public void init(ParallelDispatchEvent event) {
        super.init(event);

        // After all items have been registered and all recipes loaded, register any necessary renderer.
        Prefab.proxy.registerRenderers();

        ClientProxy.RegisterBlockRenderer();

        ClientProxy.AddGuis();
    }

    @Override
    public void postinit(ParallelDispatchEvent event) {
        super.postinit(event);
    }

    @Override
    public void RegisterEventHandler() {
        Optional<? extends ModContainer> modContainer = ModList.get().getModContainerById(Prefab.MODID);

        if (modContainer != null && modContainer.isPresent()) {
            modContainer.get().registerExtensionPoint(ConfigGuiHandler.ConfigGuiFactory.class, () -> {
                return new ConfigGuiHandler.ConfigGuiFactory((minecraft, screen) -> new GuiPrefab(minecraft, screen));
            });
        }
    }

    @Override
    public void registerRenderers() {
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
    public void openGuiForItem(UseOnContext itemUseContext) {
        for (Map.Entry<StructureItem, GuiStructure> entry : ClientProxy.ModGuis.entrySet()) {
            if (entry.getKey() == itemUseContext.getItemInHand().getItem()) {
                GuiStructure screen = entry.getValue();
                screen.pos = itemUseContext.getClickedPos();

                Minecraft.getInstance().setScreen(screen);
            }
        }
    }

    @Override
    public void openGuiForBlock(BlockPos blockPos, Level world, BaseConfig config) {
        GuiBase screen = null;

        if (config instanceof StructureScannerConfig) {
            screen = new GuiStructureScanner(blockPos, world, (StructureScannerConfig) config);
        }

        if (screen != null) {
            Minecraft.getInstance().setScreen(screen);
        }
    }

    @Override
    public void clientSetup(FMLClientSetupEvent clientSetupEvent) {
        this.registerKeyBindings(clientSetupEvent);

        ItemBlockRenderTypes.setRenderLayer(ModRegistry.BlockBoundary.get(), BlockBoundary::canRenderInLayer);

        // This render type (func_228643_e_) is the "cutout" render type.
        ItemBlockRenderTypes.setRenderLayer(ModRegistry.GlassSlab.get(), RenderType.cutout());

        ItemBlockRenderTypes.setRenderLayer(ModRegistry.GlassStairs.get(), RenderType.cutout());

        ItemBlockRenderTypes.setRenderLayer(ModRegistry.PaperLantern.get(), RenderType.cutout());

        // This is the "translucent" type.
        ItemBlockRenderTypes.setRenderLayer(ModRegistry.BlockPhasing.get(), RenderType.translucent());

        ItemBlockRenderTypes.setRenderLayer(ModRegistry.DirtWall.get(), RenderType.cutoutMipped());

        ItemBlockRenderTypes.setRenderLayer(ModRegistry.GrassWall.get(), RenderType.cutoutMipped());

        ItemBlockRenderTypes.setRenderLayer(ModRegistry.GrassStairs.get(), RenderType.cutoutMipped());

        ItemBlockRenderTypes.setRenderLayer(ModRegistry.DirtStairs.get(), RenderType.cutoutMipped());

        ItemBlockRenderTypes.setRenderLayer(ModRegistry.DirtSlab.get(), RenderType.cutoutMipped());

        ItemBlockRenderTypes.setRenderLayer(ModRegistry.GrassSlab.get(), RenderType.cutoutMipped());
    }

    private void registerKeyBindings(FMLClientSetupEvent clientSetupEvent) {
        clientSetupEvent.enqueueWork(() -> {
            KeyMapping binding = new KeyMapping("Build Current Structure",
                    KeyConflictContext.IN_GAME, KeyModifier.ALT,
                    InputConstants.Type.KEYSYM, GLFW_KEY_B, "Prefab - Structure Preview");

            ClientEventHandler.keyBindings.add(binding);
            ClientRegistry.registerKeyBinding(binding);
        });
    }
}