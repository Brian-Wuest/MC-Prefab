package com.prefab.fabric;

import com.prefab.ModRegistryBase;
import com.prefab.PrefabBase;
import com.prefab.Utils;
import com.prefab.blockItems.ToolTipBlockItem;
import com.prefab.blocks.BlockCompressedObsidian;
import com.prefab.blocks.BlockCompressedStone;
import com.prefab.blocks.BlockCustomWall;
import com.prefab.blocks.BlockStructureScanner;
import com.prefab.blocks.entities.LightSwitchBlockEntity;
import com.prefab.blocks.entities.StructureScannerBlockEntity;
import com.prefab.fabric.blocks.*;
import com.prefab.fabric.items.ItemBulldozer;
import com.prefab.fabric.items.ItemCompressedChest;
import com.prefab.fabric.items.ItemSickle;
import com.prefab.fabric.network.ServerPayloadHandler;
import com.prefab.fabric.recipe.RecipeEnabledCondition;
import com.prefab.network.payloads.*;
import com.prefab.structures.config.BasicStructureConfiguration;
import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceConditionType;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceConditions;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

/**
 * This is the mod registry so there is a way to get to all instances of the blocks/items created by this mod.
 *
 * @author WuestMan
 */
public class ModRegistry extends ModRegistryBase {
    public static final ResourceConditionType<RecipeEnabledCondition> RECIPE_ENABLED =
            ResourceConditionType.create(Identifier.fromNamespaceAndPath(PrefabBase.MODID, "recipe_enabled"), RecipeEnabledCondition.CODEC);

    /* *********************************** Item Group *********************************** */
    private static final CreativeModeTab ITEM_GROUP = FabricCreativeModeTab.builder()
            .icon(() -> new ItemStack(ModRegistryBase.LogoItem))
            .displayItems((context, entries) -> {
                for (Item item : ModRegistryBase.ModItems) {
                    if (item == ModRegistryBase.StructureScannerItem && !PrefabBase.isDebug) {
                        continue;
                    } else if (item == ModRegistryBase.LogoItem) {
                        continue;
                    }

                    entries.accept(item);
                }
            })
            .title(Utils.createTextComponent("Prefab"))
            .build();
    // This variable may not be used, but the registration is still needed.
    public static final CreativeModeTab creativeModeTab = Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, Identifier.tryBuild(PrefabBase.MODID, "logo"), ITEM_GROUP);

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

    public void registerModComponents() {
        this.registerSounds();

        this.registerBlockEntities();

        this.registerPayloadTypes();

        this.RegisterClientToServerMessageHandlers();

        this.RegisterRecipeSerializers();

        this.registerResourceConditions();
    }

    private void registerSounds() {
        Registry.register(BuiltInRegistries.SOUND_EVENT, Identifier.tryBuild(PrefabBase.MODID, "building_blueprint"), ModRegistryBase.BuildingBlueprint);
    }

    private void registerBlockEntities() {
        if (PrefabBase.isDebug) {
            ModRegistryBase.StructureScannerEntityType = Registry.register(
                    BuiltInRegistries.BLOCK_ENTITY_TYPE,
                    "prefab:structure_scanner_entity",
                    FabricBlockEntityTypeBuilder.create
                                    (StructureScannerBlockEntity::new, ModRegistryBase.StructureScanner)
                            .build());
        }

        ModRegistryBase.LightSwitchEntityType = Registry.register(
                BuiltInRegistries.BLOCK_ENTITY_TYPE,
                "prefab:light_switch_entity",
                FabricBlockEntityTypeBuilder.create(LightSwitchBlockEntity::new, ModRegistryBase.LightSwitch)
                        .build());
    }

    /**
     * This is where the mod messages are registered.
     */
    private void RegisterClientToServerMessageHandlers() {
        ServerPlayNetworking.registerGlobalReceiver(StructurePayload.PACKET_TYPE, ServerPayloadHandler::structureBuilderHandler);
        ServerPlayNetworking.registerGlobalReceiver(ScannerConfigPayload.PACKET_TYPE, ServerPayloadHandler::scannerConfigHandler);
        ServerPlayNetworking.registerGlobalReceiver(ScanShapePayload.PACKET_TYPE, ServerPayloadHandler::scannerScanHandler);
    }

    private void RegisterRecipeSerializers() {
    }

    private void registerPayloadTypes() {
        PayloadTypeRegistry.serverboundPlay().register(StructurePayload.PACKET_TYPE, StructurePayload.STREAM_CODEC);
        PayloadTypeRegistry.serverboundPlay().register(ScannerConfigPayload.PACKET_TYPE, ScannerConfigPayload.STREAM_CODEC);
        PayloadTypeRegistry.serverboundPlay().register(ScanShapePayload.PACKET_TYPE, ScanShapePayload.STREAM_CODEC);
        PayloadTypeRegistry.clientboundPlay().register(ConfigSyncPayload.PACKET_TYPE, ConfigSyncPayload.STREAM_CODEC);
        PayloadTypeRegistry.clientboundPlay().register(PlayerConfigPayload.PACKET_TYPE, PlayerConfigPayload.STREAM_CODEC);
    }

    private void registerResourceConditions() {
        ResourceConditions.register(RECIPE_ENABLED);
    }
}