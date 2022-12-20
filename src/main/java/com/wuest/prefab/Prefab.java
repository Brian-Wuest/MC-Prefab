package com.wuest.prefab;

import com.mojang.blaze3d.platform.InputConstants;
import com.wuest.prefab.events.ClientEventHandler;
import com.wuest.prefab.items.ItemSickle;
import com.wuest.prefab.proxy.ClientProxy;
import com.wuest.prefab.proxy.CommonProxy;
import com.wuest.prefab.registries.ModRegistries;
import net.minecraft.client.KeyMapping;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.event.server.ServerAboutToStartEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.simple.SimpleChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_B;

/**
 * The starting point to load all of the blocks, items and other objects associated with this mod.
 *
 * @author WuestMan
 */
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
        ModRegistry.TILE_ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
        ModRegistry.SOUNDS.register(FMLJavaModLoadingContext.get().getModEventBus());

        // Register the setup method for mod-loading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);

        MinecraftForge.EVENT_BUS.addListener(this::serverStart);

        Prefab.proxy = new CommonProxy();
    }

    private void setup(final FMLCommonSetupEvent event) {
        Prefab.proxy.preInit(event);
        Prefab.proxy.init(event);
        Prefab.proxy.postinit(event);
    }

    // The method that gets called when a server starts up(Singleplayer and multiplayer are both affected)
    public void serverStart(ServerAboutToStartEvent event) {
        // Get's the current server instance.
        MinecraftServer server = event.getServer();

        // Get's the Command manager for the server.
        // This is used to register available commands for the server.
        Commands command = server.getCommands();

        ItemSickle.setEffectiveBlocks();

        ModRegistry.serverModRegistries = new ModRegistries();
    }

    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            Prefab.proxy = new ClientProxy();
            Prefab.proxy.preInit(event);
            Prefab.proxy.init(event);
            Prefab.proxy.postinit(event);

            Prefab.proxy.RegisterEventHandler();

            Prefab.proxy.clientSetup(event);
        }

        @SubscribeEvent
        public static void KeyBindRegistrationEvent(RegisterKeyMappingsEvent event) {
            KeyMapping binding = new KeyMapping("Build Current Structure",
                    KeyConflictContext.IN_GAME, KeyModifier.ALT,
                    InputConstants.Type.KEYSYM, GLFW_KEY_B, "Prefab - Structure Preview");

            event.register(binding);

            ClientEventHandler.keyBindings.add(binding);
        }

        @SubscribeEvent
        public static void onCreativeModeTabRegister(CreativeModeTabEvent.Register event) {
            ModRegistry.PREFAB_GROUP = event.registerCreativeModeTab(new ResourceLocation(Prefab.MODID, "logo"),
                    builder -> builder.icon(() -> new ItemStack(ModRegistry.ItemLogo.get()))
                    .title(Component.translatable("itemGroup.prefab.logo"))
                    .withLabelColor(0x00FF00)
                    .displayItems((features, entries, hasPermissions) -> {
                        entries.accept(ModRegistry.CompressedStoneItem.get());
                        entries.accept(ModRegistry.DoubleCompressedStoneItem.get());
                        entries.accept(ModRegistry.TripleCompressedStoneItem.get());
                        entries.accept(ModRegistry.CompressedDirtItem.get());
                        entries.accept(ModRegistry.DoubleCompressedDirtItem.get());
                        entries.accept(ModRegistry.CompressedGlowstoneItem);
                        entries.accept(ModRegistry.DoubleCompressedGlowstoneItem);
                        entries.accept(ModRegistry.CompressedQuartzCreteItem.get());
                        entries.accept(ModRegistry.DoubleCompressedQuartzCreteItem.get());
                        entries.accept(ModRegistry.CompressedObsidianItem.get());
                        entries.accept(ModRegistry.DoubleCompressedObsidianItem.get());
                        entries.accept(ModRegistry.GlassSlabItem.get());
                        entries.accept(ModRegistry.GlassStairsItem.get());
                        entries.accept(ModRegistry.PaperLanternItem.get());
                        entries.accept(ModRegistry.PhasicItem);
                        entries.accept(ModRegistry.BoundaryItem);
                        entries.accept(ModRegistry.GrassSlabItem.get());
                        entries.accept(ModRegistry.GrassStairsItem.get());
                        entries.accept(ModRegistry.GrassWallItem.get());
                        entries.accept(ModRegistry.DirtWallItem.get());
                        entries.accept(ModRegistry.DirtStairsItem.get());
                        entries.accept(ModRegistry.DirtSlabItem.get());
                        entries.accept(ModRegistry.LightSwitchItem.get());
                        entries.accept(ModRegistry.DarkLampItem.get());
                        entries.accept(ModRegistry.QuartzCreteItem.get());
                        entries.accept(ModRegistry.QuartzCreteWallItem.get());
                        entries.accept(ModRegistry.QuartzCreteBricksItem.get());
                        entries.accept(ModRegistry.ChiseledQuartzCreteItem.get());
                        entries.accept(ModRegistry.QuartzCretePillarItem.get());
                        entries.accept(ModRegistry.QuartzCreteStairsItem.get());
                        entries.accept(ModRegistry.QuartzCreteSlabItem.get());
                        entries.accept(ModRegistry.SmoothQuartzCreteItem.get());
                        entries.accept(ModRegistry.SmoothQuartzCreteWallItem.get());
                        entries.accept(ModRegistry.SmoothQuartzCreteStairsItem.get());
                        entries.accept(ModRegistry.SmoothQuartzCreteSlabItem.get());

                        entries.accept(ModRegistry.CompressedChest);
                        entries.accept(ModRegistry.ItemPileOfBricks.get());
                        entries.accept(ModRegistry.ItemPalletOfBricks.get());
                        entries.accept(ModRegistry.ItemBundleOfTimber.get());
                        entries.accept(ModRegistry.ItemHeapOfTimber.get());
                        entries.accept(ModRegistry.ItemTonOfTimber.get());
                        entries.accept(ModRegistry.StringOfLanterns);
                        entries.accept(ModRegistry.CoilOfLanterns);
                        entries.accept(ModRegistry.Upgrade);
                        entries.accept(ModRegistry.SwiftBladeWood.get());
                        entries.accept(ModRegistry.SwiftBladeStone.get());
                        entries.accept(ModRegistry.SwiftBladeIron.get());
                        entries.accept(ModRegistry.SwiftBladeDiamond.get());
                        entries.accept(ModRegistry.SwiftBladeGold.get());
                        entries.accept(ModRegistry.SwiftBladeCopper.get());
                        entries.accept(ModRegistry.SwiftBladeOsmium.get());
                        entries.accept(ModRegistry.SwiftBladeBronze.get());
                        entries.accept(ModRegistry.SwiftBladeSteel.get());
                        entries.accept(ModRegistry.SwiftBladeObsidian.get());
                        entries.accept(ModRegistry.SwiftBladeNetherite.get());
                        entries.accept(ModRegistry.SickleWood.get());
                        entries.accept(ModRegistry.SickleStone.get());
                        entries.accept(ModRegistry.SickleGold.get());
                        entries.accept(ModRegistry.SickleIron.get());
                        entries.accept(ModRegistry.SickleDiamond.get());
                        entries.accept(ModRegistry.SickleNetherite.get());
                        entries.accept(ModRegistry.ItemEmptyCrate.get());
                        entries.accept(ModRegistry.ClutchOfEggs.get());
                        entries.accept(ModRegistry.ItemCartonOfEggs.get());
                        entries.accept(ModRegistry.BunchOfPotatoes.get());
                        entries.accept(ModRegistry.ItemCrateOfPotatoes.get());
                        entries.accept(ModRegistry.BunchOfCarrots.get());
                        entries.accept(ModRegistry.ItemCrateOfCarrots.get());
                        entries.accept(ModRegistry.BunchOfBeets.get());
                        entries.accept(ModRegistry.ItemCrateOfBeets.get());

                        entries.accept(ModRegistry.InstantBridge.get());
                        entries.accept(ModRegistry.House);
                        entries.accept(ModRegistry.HouseImproved);
                        entries.accept(ModRegistry.HouseAdvanced);
                        entries.accept(ModRegistry.Bulldozer.get());
                        entries.accept(ModRegistry.CreativeBulldozer);
                        entries.accept(ModRegistry.MachineryTower.get());
                        entries.accept(ModRegistry.DefenseBunker.get());
                        entries.accept(ModRegistry.MineshaftEntrance.get());
                        entries.accept(ModRegistry.EnderGateway.get());
                        entries.accept(ModRegistry.GrassyPlain.get());
                        entries.accept(ModRegistry.MagicTemple.get());
                        entries.accept(ModRegistry.WatchTower.get());
                        entries.accept(ModRegistry.WelcomeCenter.get());
                        entries.accept(ModRegistry.Jail.get());
                        entries.accept(ModRegistry.Saloon.get());
                        entries.accept(ModRegistry.SkiLodge.get());
                        entries.accept(ModRegistry.WindMill.get());
                        entries.accept(ModRegistry.TownHall.get());
                        entries.accept(ModRegistry.NetherGate.get());
                        entries.accept(ModRegistry.AquaBase.get());
                        entries.accept(ModRegistry.AquaBaseImproved);
                        entries.accept(ModRegistry.Warehouse);
                        entries.accept(ModRegistry.WareHouseImproved);
                        entries.accept(ModRegistry.VillagerHouses.get());
                        entries.accept(ModRegistry.ModernBuildings);
                        entries.accept(ModRegistry.ModernBuildingsImproved);
                        entries.accept(ModRegistry.ModernBuildingsAdvanced);
                        entries.accept(ModRegistry.Farm);
                        entries.accept(ModRegistry.FarmImproved);
                        entries.accept(ModRegistry.FarmAdvanced);

                        if (Prefab.isDebug) {
                            entries.accept(ModRegistry.StructureScannerItem.get());
                        }
                    }));
        }
    }
}
