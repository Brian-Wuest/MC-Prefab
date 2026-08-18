package com.prefab.neoforge;

import com.mojang.serialization.MapCodec;
import com.prefab.ModRegistryBase;
import com.prefab.PrefabBase;
import com.prefab.blockItems.ToolTipBlockItem;
import com.prefab.blocks.BlockCompressedObsidian;
import com.prefab.blocks.BlockCompressedStone;
import com.prefab.blocks.BlockCustomWall;
import com.prefab.blocks.BlockStructureScanner;
import com.prefab.blocks.entities.LightSwitchBlockEntity;
import com.prefab.blocks.entities.StructureScannerBlockEntity;
import com.prefab.neoforge.blocks.*;
import com.prefab.neoforge.items.ItemBulldozer;
import com.prefab.neoforge.items.ItemCompressedChest;
import com.prefab.neoforge.items.ItemSickle;
import com.prefab.neoforge.network.ClientPayloadHandler;
import com.prefab.neoforge.network.ServerPayloadHandler;
import com.prefab.neoforge.recipe.RecipeEnabledCondition;
import com.prefab.network.payloads.*;
import com.prefab.structures.config.BasicStructureConfiguration;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.neoforged.neoforge.registries.RegisterEvent;

import java.util.ArrayList;
import java.util.Objects;

import static com.prefab.neoforge.Prefab.CREATIVE_MODE_TABS;

public class ModRegistry extends ModRegistryBase {
    public static final DeferredRegister<MapCodec<? extends ICondition>> CUSTOM_CONDITION_TYPES =
            DeferredRegister.create(NeoForgeRegistries.Keys.CONDITION_CODECS, PrefabBase.MODID);
    public static final DeferredHolder<MapCodec<? extends ICondition>, MapCodec<RecipeEnabledCondition>> RECIPE_ENABLED =
            CUSTOM_CONDITION_TYPES.register("recipe_enabled", () -> RecipeEnabledCondition.CODEC);

    // Creates a creative tab with the id "examplemod:example_tab" for the example item, that is placed after the combat tab
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> EXAMPLE_TAB = CREATIVE_MODE_TABS.register("example_tab", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.prefab.logo")) //The language key for the title of your CreativeModeTab
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .icon(() -> ModRegistryBase.LogoItem.getDefaultInstance())
            .displayItems((parameters, output) -> {
                for (Item item : ModRegistry.ModItems) {
                    if (item == ModRegistryBase.StructureScannerItem && !PrefabBase.isDebug) {
                        continue;
                    } else if (item == ModRegistryBase.LogoItem) {
                        continue;
                    }

                    output.accept(item);
                }
            }).build());
    public PayloadRegistrar registrar;

    @Override
    public void initializeModLoaderBlocks() {
        // Always make sure to fully qualify WHICH block we are creating...
        ModRegistryBase.Boundary = new BlockBoundary(
                this.setBlockId(PrefabBase.SeeThroughImmovable.get(), BlockBoundary.BlockName));
        ModRegistryBase.GlassSlab = new BlockGlassSlab(
                this.setBlockId(Block.Properties.ofFullCopy(Blocks.GLASS), BlockGlassSlab.BlockName));
        ModRegistryBase.GlassStairs = new BlockGlassStairs(Blocks.GLASS.defaultBlockState(),
                this.setBlockId(Block.Properties.ofFullCopy(Blocks.GLASS), BlockGlassStairs.BlockName));
        ModRegistryBase.PaperLantern = new BlockPaperLantern(
                this.setBlockId(PrefabBase.SeeThroughImmovable.get(), BlockPaperLantern.BlockName));
        ModRegistryBase.Phasic = new BlockPhasic(
                this.setBlockId(PrefabBase.SeeThroughImmovable.get(), BlockPhasic.BlockName));
    }

    @Override
    public void initializeModLoaderBlockItems() {
        // Always make sure do re-do the block item when replacing a block.
        ModRegistryBase.BoundaryItem = new ToolTipBlockItem(ModRegistryBase.Boundary,
                this.setItemBlockId(new Item.Properties(), ModRegistryBase.Boundary),
                ToolTipBlockItem.ToolTipInfo.BLOCK_BOUNDARY);

        ModRegistryBase.GlassSlabItem = new BlockItem(ModRegistryBase.GlassSlab,
                this.setItemBlockId(new Item.Properties(), ModRegistryBase.GlassSlab));

        ModRegistryBase.GlassStairsItem = new BlockItem(ModRegistryBase.GlassStairs,
                this.setItemBlockId(new Item.Properties(), ModRegistryBase.GlassStairs));

        ModRegistryBase.PaperLanternItem = new BlockItem(ModRegistryBase.PaperLantern,
                this.setItemBlockId(new Item.Properties(), ModRegistryBase.PaperLantern));

        ModRegistryBase.PhasicItem = new BlockItem(ModRegistryBase.Phasic,
                this.setItemBlockId(new Item.Properties(), ModRegistryBase.Phasic));
    }

    @Override
    public void initializeModLoaderItems() {
        ModRegistryBase.CompressedChest = new ItemCompressedChest();
        ModRegistryBase.SickleDiamond = new ItemSickle(ToolMaterial.DIAMOND, this.setItemId(new Item.Properties(), "item_sickle_diamond"));
        ModRegistryBase.SickleGold = new ItemSickle(ToolMaterial.GOLD, this.setItemId(new Item.Properties(), "item_sickle_gold"));
        ModRegistryBase.SickleNetherite = new ItemSickle(ToolMaterial.NETHERITE, this.setItemId(new Item.Properties(), "item_sickle_netherite"));
        ModRegistryBase.SickleIron = new ItemSickle(ToolMaterial.IRON, this.setItemId(new Item.Properties(), "item_sickle_iron"));
        ModRegistryBase.SickleStone = new ItemSickle(ToolMaterial.STONE, this.setItemId(new Item.Properties(), "item_sickle_stone"));
        ModRegistryBase.SickleWood = new ItemSickle(ToolMaterial.WOOD, this.setItemId(new Item.Properties(), "item_sickle_wood"));
    }

    @Override
    public void initializeModLoaderBluePrintItems() {
        // Always make sure to fully qualify WHICH item we are creating...
        ModRegistryBase.Bulldozer = new ItemBulldozer(this.setItemId(new Item.Properties(), "item_bulldozer"));
        ModRegistryBase.CreativeBulldozer = new ItemBulldozer(this.setItemId(new Item.Properties(), "item_creative_bulldozer"), true);
    }

    public void register(RegisterEvent event) {
        // This event is called once for each type of registry.
        if (event.getRegistryKey() == Registries.BLOCK) {
            this.initializeBlocks();

            this.registerBlocks();
        } else if (event.getRegistryKey() == Registries.ITEM) {
            this.initializeItems();
            this.initializeBluePrintItems();
            this.initializeBlockItems();

            this.registerItems();

            this.registerBluePrints();

            this.registerItemBlocks();
        } else if (event.getRegistryKey() == Registries.RECIPE_SERIALIZER) {
            this.initializeRecipeSerializers();

            this.RegisterRecipeSerializers();
        } else if (event.getRegistryKey() == Registries.ENTITY_TYPE) {
            this.registerBlockEntities();
        } else if (event.getRegistryKey() == Registries.SOUND_EVENT) {
            this.initializeSounds();

            this.registerSounds();
        }
    }

    public void registerPayLoads(final RegisterPayloadHandlersEvent event) {
        // Sets the current network version
        // The version is always 1 as we are always version 1 of ourselves.
        this.registrar = event.registrar("1");

        registrar.playToServer(
                ScannerConfigPayload.PACKET_TYPE,
                ScannerConfigPayload.STREAM_CODEC,
                ServerPayloadHandler::scannerConfigHandler
        );

        registrar.playToServer(
                ScanShapePayload.PACKET_TYPE,
                ScanShapePayload.STREAM_CODEC,
                ServerPayloadHandler::scannerScanHandler
        );

        registrar.playToServer(
                StructurePayload.PACKET_TYPE,
                StructurePayload.STREAM_CODEC,
                ServerPayloadHandler::structureBuilderHandler
        );

        registrar.playToClient(
                PlayerConfigPayload.PACKET_TYPE,
                PlayerConfigPayload.STREAM_CODEC,
                ClientPayloadHandler::PlayerConfigHandler);

        registrar.playToClient(
                ConfigSyncPayload.PACKET_TYPE,
                ConfigSyncPayload.STREAM_CODEC,
                ClientPayloadHandler::ModConfigHandler);
    }

    private void registerSounds() {
        Registry.register(BuiltInRegistries.SOUND_EVENT, Objects.requireNonNull(Identifier.tryBuild(PrefabBase.MODID, "building_blueprint")), ModRegistryBase.BuildingBlueprint);
    }

    private void registerBlockEntities() {
        if (PrefabBase.isDebug) {
            ModRegistryBase.StructureScannerEntityType = Registry.register(
                    BuiltInRegistries.BLOCK_ENTITY_TYPE,
                    Identifier.fromNamespaceAndPath(PrefabBase.MODID, "structure_scanner_entity"),
                    new BlockEntityType<>(StructureScannerBlockEntity::new, ModRegistryBase.StructureScanner));
        }

        ModRegistryBase.LightSwitchEntityType = Registry.register(
                BuiltInRegistries.BLOCK_ENTITY_TYPE,
                Identifier.fromNamespaceAndPath(PrefabBase.MODID, "light_switch_entity"),
                new BlockEntityType<>(LightSwitchBlockEntity::new,
                        ModRegistryBase.LightSwitch));
    }

    private void RegisterRecipeSerializers() {
    }
}
