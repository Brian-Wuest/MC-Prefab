package com.prefab;

import com.prefab.blocks.*;
import com.prefab.blocks.entities.LightSwitchBlockEntity;
import com.prefab.blocks.entities.StructureScannerBlockEntity;
import com.prefab.items.*;
import com.prefab.registries.ModRegistries;
import com.prefab.structures.config.BasicStructureConfiguration;
import com.prefab.structures.items.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;

import java.util.ArrayList;
import java.util.function.Consumer;

public class ModRegistryBase {
    public static final ArrayList<Consumer<Object>> guiRegistrations = new ArrayList<>();
    public static final TagKey<Item> COPPER_INGOTS = TagKey.create(Registries.ITEM, ResourceLocation.tryBuild("c", "ingots/copper"));

    /* *********************************** TagKeys *********************************** */
    public static final TagKey<Item> OSMIUM_INGOTS = TagKey.create(Registries.ITEM, ResourceLocation.tryBuild("c", "ingots/osmium"));
    public static final TagKey<Item> BRONZE_INGOTS = TagKey.create(Registries.ITEM, ResourceLocation.tryBuild("c", "ingots/bronze"));
    public static final TagKey<Item> STEEL_INGOTS = TagKey.create(Registries.ITEM, ResourceLocation.tryBuild("c", "ingots/steel"));
    public static final TagKey<Item> OBSIDIAN_TAG = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("c", "obsidians/normal"));
    public static final ToolMaterial COPPER_MATERIAL = new ToolMaterial(
            BlockTags.INCORRECT_FOR_STONE_TOOL,
            ToolMaterial.STONE.durability(),
            ToolMaterial.STONE.speed(),
            ToolMaterial.STONE.attackDamageBonus(),
            ToolMaterial.STONE.enchantmentValue(),
            COPPER_INGOTS
    );
    public static final ToolMaterial OSMIUM_MATERIAL = new ToolMaterial(
            BlockTags.INCORRECT_FOR_IRON_TOOL,
            500,
            ToolMaterial.IRON.speed(),
            ToolMaterial.IRON.attackDamageBonus() + .5f,
            ToolMaterial.IRON.enchantmentValue(),
            OSMIUM_INGOTS
    );
    public static final ToolMaterial BRONZE_MATERIAL = new ToolMaterial(
            BlockTags.INCORRECT_FOR_IRON_TOOL,
            ToolMaterial.IRON.durability(),
            ToolMaterial.IRON.speed(),
            ToolMaterial.IRON.attackDamageBonus(),
            ToolMaterial.IRON.enchantmentValue(),
            BRONZE_INGOTS
    );
    public static final ToolMaterial STEEL_MATERIAL = new ToolMaterial(
            BlockTags.INCORRECT_FOR_DIAMOND_TOOL,
            (int) (ToolMaterial.IRON.durability() * 1.5),
            ToolMaterial.DIAMOND.speed(),
            ToolMaterial.DIAMOND.attackDamageBonus(),
            ToolMaterial.DIAMOND.enchantmentValue(),
            STEEL_INGOTS
    );
    public static final ToolMaterial OBSIDIAN_MATERIAL = new ToolMaterial(
            BlockTags.INCORRECT_FOR_DIAMOND_TOOL,
            (int) (ToolMaterial.DIAMOND.durability() * 1.5),
            ToolMaterial.DIAMOND.speed(),
            ToolMaterial.DIAMOND.attackDamageBonus(),
            ToolMaterial.DIAMOND.enchantmentValue(),
            OBSIDIAN_TAG
    );
    public static ModRegistries serverModRegistries;
    /* *********************************** Blocks *********************************** */
    public static BlockCompressedStone CompressedStone;
    public static BlockCompressedStone DoubleCompressedStone;
    public static BlockCompressedStone TripleCompressedStone;
    public static BlockCompressedStone CompressedDirt;
    public static BlockCompressedStone DoubleCompressedDirt;
    public static BlockCompressedStone CompressedGlowstone;
    public static BlockCompressedStone DoubleCompressedGlowstone;
    public static BlockCompressedStone CompressedQuartzCrete;
    public static BlockCompressedStone DoubleCompressedQuartzCrete;
    public static BlockCompressedObsidian CompressedObsidian;
    public static BlockCompressedObsidian DoubleCompressedObsidian;
    public static BlockGlassSlab GlassSlab;
    public static BlockGlassStairs GlassStairs;
    public static BlockPaperLantern PaperLantern;
    public static BlockPhasic Phasic;
    public static BlockBoundary Boundary;
    public static BlockGrassSlab GrassSlab;
    public static BlockGrassStairs GrassStairs;
    public static BlockCustomWall GrassWall;
    public static BlockCustomWall DirtWall;
    public static BlockDirtStairs DirtStairs;
    public static BlockDirtSlab DirtSlab;
    public static BlockStructureScanner StructureScanner;
    public static BlockLightSwitch LightSwitch;
    public static BlockDarkLamp DarkLamp;
    public static BlockRotatableHorizontalShaped PileOfBricks;
    public static BlockRotatableHorizontalShaped PalletOfBricks;
    public static BlockRotatableHorizontalShaped BundleOfTimber;
    public static BlockRotatableHorizontalShaped HeapOfTimber;
    public static BlockRotatableHorizontalShaped TonOfTimber;
    public static BlockRotatable EmptyCrate;
    public static BlockRotatable CartonOfEggs;
    public static BlockRotatable CrateOfPotatoes;
    public static BlockRotatable CrateOfCarrots;
    public static BlockRotatable CrateOfBeets;
    public static Block QuartzCrete;
    public static WallBlock QuartzCreteWall;
    public static Block QuartzCreteBricks;
    public static Block ChiseledQuartzCrete;
    public static RotatedPillarBlock QuartzCretePillar;
    public static BlockCustomStairs QuartzCreteStairs;
    public static SlabBlock QuartzCreteSlab;
    public static Block SmoothQuartzCrete;
    public static WallBlock SmoothQuartzCreteWall;
    public static BlockCustomStairs SmoothQuartzCreteStairs;
    public static SlabBlock SmoothQuartzCreteSlab;
    /* *********************************** Item Blocks *********************************** */
    public static BlockItem CompressedStoneItem;
    public static BlockItem DoubleCompressedStoneItem;
    public static BlockItem TripleCompressedStoneItem;
    public static BlockItem CompressedDirtItem;
    public static BlockItem DoubleCompressedDirtItem;
    public static BlockItem CompressedGlowstoneItem;
    public static BlockItem DoubleCompressedGlowstoneItem;
    public static BlockItem CompressedQuartzCreteItem;
    public static BlockItem DoubleCompressedQuartzCreteItem;
    public static BlockItem CompressedObsidianItem;
    public static BlockItem DoubleCompressedObsidianItem;
    public static BlockItem GlassSlabItem;
    public static BlockItem GlassStairsItem;
    public static BlockItem PaperLanternItem;
    public static BlockItem PhasicItem;
    public static BlockItem BoundaryItem;
    public static BlockItem GrassSlabItem;
    public static BlockItem GrassStairsItem;
    public static BlockItem GrassWallItem;
    public static BlockItem DirtWallItem;
    public static BlockItem DirtStairsItem;
    public static BlockItem DirtSlabItem;
    public static BlockItem StructureScannerItem;
    public static BlockItem LightSwitchItem;
    public static BlockItem DarkLampItem;
    public static BlockItem QuartzCreteItem;
    public static BlockItem QuartzCreteWallItem;
    public static BlockItem QuartzCreteBricksItem;
    public static BlockItem ChiseledQuartzCreteItem;
    public static BlockItem QuartzCretePillarItem;
    public static BlockItem QuartzCreteStairsItem;

    /* *********************************** Tool Materials *********************************** */
    public static BlockItem QuartzCreteSlabItem;
    public static BlockItem SmoothQuartzCreteItem;
    public static BlockItem SmoothQuartzCreteWallItem;
    public static BlockItem SmoothQuartzCreteStairsItem;
    public static BlockItem SmoothQuartzCreteSlabItem;
    /* *********************************** Items *********************************** */
    public static Item LogoItem;
    public static ItemCompressedChest CompressedChest;
    public static Item ItemPileOfBricks;
    public static Item ItemPalletOfBricks;
    public static Item ItemBundleOfTimber;
    public static Item ItemHeapOfTimber;
    public static Item ItemTonOfTimber;
    public static Item StringOfLanterns;
    public static Item CoilOfLanterns;
    public static Item Upgrade;
    public static Item SwiftBladeWood;
    public static Item SwiftBladeStone;
    public static Item SwiftBladeIron;
    public static Item SwiftBladeDiamond;
    public static Item SwiftBladeGold;
    public static Item SwiftBladeCopper;
    public static Item SwiftBladeOsmium;
    public static Item SwiftBladeBronze;
    public static Item SwiftBladeSteel;
    public static Item SwiftBladeObsidian;
    public static Item SwiftBladeNetherite;

    // These will be overridden in mod-loader files.
    public static ItemSickle SickleWood;
    public static ItemSickle SickleStone;
    public static ItemSickle SickleGold;
    public static ItemSickle SickleIron;
    public static ItemSickle SickleDiamond;
    public static ItemSickle SickleNetherite;

    // Note: Empty crate must be created registered first to avoid null-pointer errors with the rest of the ItemWoodenCrate items.
    public static ItemBlockWoodenCrate ItemEmptyCrate;
    public static ItemWoodenCrate ClutchOfEggs;
    public static ItemBlockWoodenCrate ItemCartonOfEggs;
    public static ItemWoodenCrate BunchOfPotatoes;
    public static ItemBlockWoodenCrate ItemCrateOfPotatoes;
    public static ItemWoodenCrate BunchOfCarrots;
    public static ItemBlockWoodenCrate ItemCrateOfCarrots;
    public static ItemWoodenCrate BunchOfBeets;
    public static ItemBlockWoodenCrate ItemCrateOfBeets;

    /* *********************************** Blueprint Items *********************************** */
    public static ItemInstantBridge InstantBridge;
    public static ItemHouse House;
    public static ItemHouseImproved HouseImproved;
    public static ItemHouseAdvanced HouseAdvanced;
    public static ItemBulldozer Bulldozer;
    public static ItemBulldozer CreativeBulldozer;
    public static ItemBasicStructure MachineryTower;
    public static ItemBasicStructure DefenseBunker;
    public static ItemBasicStructure MineshaftEntrance;
    public static ItemBasicStructure EnderGateway;
    public static ItemBasicStructure AquaBase;
    public static ItemBasicStructure GrassyPlain;
    public static ItemBasicStructure MagicTemple;
    public static ItemBasicStructure WatchTower;
    public static ItemBasicStructure WelcomeCenter;
    public static ItemBasicStructure Jail;
    public static ItemBasicStructure Saloon;
    public static ItemBasicStructure SkiLodge;
    public static ItemBasicStructure WindMill;
    public static ItemBasicStructure TownHall;
    public static ItemBasicStructure NetherGate;
    public static ItemBasicStructure AquaBaseImproved;
    public static ItemBasicStructure Warehouse;
    public static ItemBasicStructure WareHouseImproved;
    public static ItemBasicStructure VillagerHouses;
    public static ItemBasicStructure ModernBuildings;
    public static ItemBasicStructure ModernBuildingsImproved;
    public static ItemBasicStructure ModernBuildingsAdvanced;
    public static ItemBasicStructure Farm;
    public static ItemBasicStructure FarmImproved;
    public static ItemBasicStructure FarmAdvanced;

    /* *********************************** Sounds *********************************** */
    public static SoundEvent BuildingBlueprint;

    /* *********************************** Block Entities Types *********************************** */
    public static BlockEntityType<StructureScannerBlockEntity> StructureScannerEntityType;
    public static BlockEntityType<LightSwitchBlockEntity> LightSwitchEntityType;

    public static boolean always(BlockState state, BlockGetter world, BlockPos pos) {
        return true;
    }

    public static boolean never(BlockState state, BlockGetter world, BlockPos pos) {
        return false;
    }

    /**
     * Use this to initialize everything in one go.
     */
    public void initializeEverything() {
        this.initializeBlocks();

        this.initializeBlockItems();

        this.initializeItems();

        this.initializeBluePrintItems();

        this.initializeRecipeSerializers();

        this.initializeSounds();
    }

    public void initializeBlocks() {
        ModRegistryBase.CompressedStone = new BlockCompressedStone(BlockCompressedStone.EnumType.COMPRESSED_STONE,
                this.setBlockId(BlockBehaviour.Properties.of(), BlockCompressedStone.EnumType.COMPRESSED_STONE.toString()));

        ModRegistryBase.DoubleCompressedStone = new BlockCompressedStone(BlockCompressedStone.EnumType.DOUBLE_COMPRESSED_STONE,
                this.setBlockId(BlockBehaviour.Properties.of(), BlockCompressedStone.EnumType.DOUBLE_COMPRESSED_STONE.toString()));

        ModRegistryBase.TripleCompressedStone = new BlockCompressedStone(BlockCompressedStone.EnumType.TRIPLE_COMPRESSED_STONE,
                this.setBlockId(BlockBehaviour.Properties.of(), BlockCompressedStone.EnumType.TRIPLE_COMPRESSED_STONE.toString()));

        ModRegistryBase.CompressedDirt = new BlockCompressedStone(BlockCompressedStone.EnumType.COMPRESSED_DIRT,
                this.setBlockId(BlockBehaviour.Properties.of(), BlockCompressedStone.EnumType.COMPRESSED_DIRT.toString()));

        ModRegistryBase.DoubleCompressedDirt = new BlockCompressedStone(BlockCompressedStone.EnumType.DOUBLE_COMPRESSED_DIRT,
                this.setBlockId(BlockBehaviour.Properties.of(), BlockCompressedStone.EnumType.DOUBLE_COMPRESSED_DIRT.toString()));

        ModRegistryBase.CompressedGlowstone = new BlockCompressedStone(BlockCompressedStone.EnumType.COMPRESSED_GLOWSTONE,
            this.setBlockId(BlockBehaviour.Properties.of(), BlockCompressedStone.EnumType.COMPRESSED_GLOWSTONE.toString()));

        ModRegistryBase.DoubleCompressedGlowstone = new BlockCompressedStone(BlockCompressedStone.EnumType.DOUBLE_COMPRESSED_GLOWSTONE,
                this.setBlockId(BlockBehaviour.Properties.of(), BlockCompressedStone.EnumType.DOUBLE_COMPRESSED_GLOWSTONE.toString()));

        ModRegistryBase.CompressedQuartzCrete = new BlockCompressedStone(BlockCompressedStone.EnumType.COMPRESSED_QUARTZCRETE,
                this.setBlockId(BlockBehaviour.Properties.of(), BlockCompressedStone.EnumType.COMPRESSED_QUARTZCRETE.toString()));

        ModRegistryBase.DoubleCompressedQuartzCrete = new BlockCompressedStone(BlockCompressedStone.EnumType.DOUBLE_COMPRESSED_QUARTZCRETE,
                this.setBlockId(BlockBehaviour.Properties.of(), BlockCompressedStone.EnumType.DOUBLE_COMPRESSED_QUARTZCRETE.toString()));

        ModRegistryBase.CompressedObsidian = new BlockCompressedObsidian(BlockCompressedObsidian.EnumType.COMPRESSED_OBSIDIAN);
        ModRegistryBase.DoubleCompressedObsidian = new BlockCompressedObsidian(BlockCompressedObsidian.EnumType.DOUBLE_COMPRESSED_OBSIDIAN);
        ModRegistryBase.GrassSlab = new BlockGrassSlab();
        ModRegistryBase.GrassStairs = new BlockGrassStairs();
        ModRegistryBase.GrassWall = new BlockCustomWall(Blocks.GRASS_BLOCK, BlockCustomWall.EnumType.GRASS);
        ModRegistryBase.DirtWall = new BlockCustomWall(Blocks.DIRT, BlockCustomWall.EnumType.DIRT);
        ModRegistryBase.DirtStairs = new BlockDirtStairs();
        ModRegistryBase.DirtSlab = new BlockDirtSlab();
        ModRegistryBase.LightSwitch = new BlockLightSwitch();
        ModRegistryBase.DarkLamp = new BlockDarkLamp();

        ModRegistryBase.PileOfBricks = new BlockRotatableHorizontalShaped(BlockShaped.BlockShape.PileOfBricks,
                this.setBlockId(Block.Properties.ofFullCopy(Blocks.BRICKS), "item_pile_of_bricks")
                        .mapColor(MapColor.COLOR_RED).noOcclusion().isViewBlocking(ModRegistryBase::never));
        ModRegistryBase.PalletOfBricks = new BlockRotatableHorizontalShaped(BlockShaped.BlockShape.PalletOfBricks,
                this.setBlockId(Block.Properties.ofFullCopy(Blocks.BRICKS), "item_pallet_of_bricks")
                        .mapColor(MapColor.COLOR_RED).noOcclusion().isViewBlocking(ModRegistryBase::never));
        ModRegistryBase.BundleOfTimber = new BlockRotatableHorizontalShaped(BlockShaped.BlockShape.BundleOfTimber,
                this.setBlockId(Block.Properties.ofFullCopy(Blocks.OAK_WOOD), "item_bundle_of_timber")
                        .mapColor(MapColor.COLOR_BROWN).sound(SoundType.WOOD).noOcclusion().isViewBlocking(ModRegistryBase::never));
        ModRegistryBase.HeapOfTimber = new BlockRotatableHorizontalShaped(BlockShaped.BlockShape.HeapOfTimber,
                this.setBlockId(Block.Properties.ofFullCopy(Blocks.OAK_WOOD), "item_heap_of_timber")
                        .mapColor(MapColor.COLOR_BROWN).sound(SoundType.WOOD).noOcclusion().isViewBlocking(ModRegistryBase::never));
        ModRegistryBase.TonOfTimber = new BlockRotatableHorizontalShaped(BlockShaped.BlockShape.TonOfTimber,
                this.setBlockId(Block.Properties.ofFullCopy(Blocks.OAK_WOOD), "item_ton_of_timber")
                        .mapColor(MapColor.COLOR_BROWN).sound(SoundType.WOOD).noOcclusion().isViewBlocking(ModRegistryBase::never));

        ModRegistryBase.EmptyCrate = new BlockRotatable(
                this.setBlockId(Block.Properties.ofFullCopy(Blocks.OAK_WOOD), "item_wooden_crate")
                        .sound(SoundType.WOOD));
        ModRegistryBase.CartonOfEggs = new BlockRotatable(
                this.setBlockId(Block.Properties.ofFullCopy(Blocks.OAK_WOOD), "item_carton_of_eggs")
                        .sound(SoundType.WOOD));
        ModRegistryBase.CrateOfPotatoes = new BlockRotatable(
                this.setBlockId(Block.Properties.ofFullCopy(Blocks.OAK_WOOD), "item_crate_of_potatoes")
                        .sound(SoundType.WOOD));
        ModRegistryBase.CrateOfCarrots = new BlockRotatable(
                this.setBlockId(Block.Properties.ofFullCopy(Blocks.OAK_WOOD), "item_crate_of_carrots")
                        .sound(SoundType.WOOD));
        ModRegistryBase.CrateOfBeets = new BlockRotatable(
                this.setBlockId(Block.Properties.ofFullCopy(Blocks.OAK_WOOD), "item_crate_of_beets")
                        .sound(SoundType.WOOD));

        ModRegistryBase.QuartzCrete = new Block(
                this.setBlockId(Block.Properties.ofFullCopy(Blocks.QUARTZ_BLOCK), "block_quartz_crete")
        );
        ModRegistryBase.QuartzCreteWall = new WallBlock(
                this.setBlockId(Block.Properties.ofFullCopy(ModRegistryBase.QuartzCrete), "block_quartz_crete_wall")
        );
        ModRegistryBase.QuartzCreteBricks = new Block(
                this.setBlockId(Block.Properties.ofFullCopy(ModRegistryBase.QuartzCrete), "block_quartz_crete_bricks")
        );
        ModRegistryBase.ChiseledQuartzCrete = new Block(
                this.setBlockId(Block.Properties.ofFullCopy(Blocks.CHISELED_QUARTZ_BLOCK), "block_quartz_crete_chiseled")
        );
        ModRegistryBase.QuartzCretePillar = new RotatedPillarBlock(
                this.setBlockId(Block.Properties.ofFullCopy(Blocks.QUARTZ_PILLAR), "block_quartz_crete_pillar")
        );
        ModRegistryBase.QuartzCreteStairs = new BlockCustomStairs(ModRegistryBase.QuartzCrete.defaultBlockState(),
                this.setBlockId(Block.Properties.ofFullCopy(ModRegistryBase.QuartzCrete), "block_quartz_crete_stairs")
        );
        ModRegistryBase.QuartzCreteSlab = new SlabBlock(
                this.setBlockId(Block.Properties.ofFullCopy(ModRegistryBase.QuartzCrete), "block_quartz_crete_slab")
        );
        ModRegistryBase.SmoothQuartzCrete = new Block(
                this.setBlockId(Block.Properties.ofFullCopy(ModRegistryBase.QuartzCrete), "block_quartz_crete_smooth")
        );
        ModRegistryBase.SmoothQuartzCreteWall = new WallBlock(
                this.setBlockId(Block.Properties.ofFullCopy(ModRegistryBase.SmoothQuartzCrete), "block_quartz_crete_smooth_wall")
        );
        ModRegistryBase.SmoothQuartzCreteStairs = new BlockCustomStairs(ModRegistryBase.SmoothQuartzCrete.defaultBlockState(),
                this.setBlockId(Block.Properties.ofFullCopy(ModRegistryBase.SmoothQuartzCrete), "block_quartz_crete_smooth_stairs")
        );
        ModRegistryBase.SmoothQuartzCreteSlab = new SlabBlock(
                this.setBlockId(Block.Properties.ofFullCopy(ModRegistryBase.SmoothQuartzCrete), "block_quartz_crete_smooth_slab")
        );

        this.initializeModLoaderBlocks();
    }

    /**
     * This will initialize default implementations of blocks unless overridden.
     * This is called at the end of initializeBlocks.
     */
    public void initializeModLoaderBlocks() {
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

    public void initializeBlockItems() {
        ModRegistryBase.CompressedStoneItem = new BlockItem(ModRegistryBase.CompressedStone,
                this.setItemBlockId(new Item.Properties(), ModRegistryBase.CompressedStone));

        ModRegistryBase.DoubleCompressedStoneItem = new BlockItem(ModRegistryBase.DoubleCompressedStone,
                this.setItemBlockId(new Item.Properties(), ModRegistryBase.DoubleCompressedStone));

        ModRegistryBase.TripleCompressedStoneItem = new BlockItem(ModRegistryBase.TripleCompressedStone,
                this.setItemBlockId(new Item.Properties(), ModRegistryBase.TripleCompressedStone));

        ModRegistryBase.CompressedDirtItem = new BlockItem(ModRegistryBase.CompressedDirt,
                this.setItemBlockId(new Item.Properties(), ModRegistryBase.CompressedDirt));

        ModRegistryBase.DoubleCompressedDirtItem = new BlockItem(ModRegistryBase.DoubleCompressedDirt,
                this.setItemBlockId(new Item.Properties(), ModRegistryBase.DoubleCompressedDirt));

        ModRegistryBase.CompressedGlowstoneItem = new BlockItem(ModRegistryBase.CompressedGlowstone,
                this.setItemBlockId(new Item.Properties(), ModRegistryBase.CompressedGlowstone));

        ModRegistryBase.DoubleCompressedGlowstoneItem = new BlockItem(ModRegistryBase.DoubleCompressedGlowstone,
                this.setItemBlockId(new Item.Properties(), ModRegistryBase.DoubleCompressedGlowstone));

        ModRegistryBase.CompressedQuartzCreteItem = new BlockItem(ModRegistryBase.CompressedQuartzCrete,
                this.setItemBlockId(new Item.Properties(), ModRegistryBase.CompressedQuartzCrete));

        ModRegistryBase.DoubleCompressedQuartzCreteItem = new BlockItem(ModRegistryBase.DoubleCompressedQuartzCrete,
                this.setItemBlockId(new Item.Properties(), ModRegistryBase.DoubleCompressedQuartzCrete));

        ModRegistryBase.CompressedObsidianItem = new BlockItem(ModRegistryBase.CompressedObsidian,
                this.setItemBlockId(new Item.Properties(), ModRegistryBase.CompressedObsidian));

        ModRegistryBase.DoubleCompressedObsidianItem = new BlockItem(ModRegistryBase.DoubleCompressedObsidian,
                this.setItemBlockId(new Item.Properties(), ModRegistryBase.DoubleCompressedObsidian));

        ModRegistryBase.GrassSlabItem = new BlockItem(ModRegistryBase.GrassSlab,
                this.setItemBlockId(new Item.Properties(), ModRegistryBase.GrassSlab));

        ModRegistryBase.GrassStairsItem = new BlockItem(ModRegistryBase.GrassStairs,
                this.setItemBlockId(new Item.Properties(), ModRegistryBase.GrassStairs));

        ModRegistryBase.GrassWallItem = new BlockItem(ModRegistryBase.GrassWall,
                this.setItemBlockId(new Item.Properties(), ModRegistryBase.GrassWall));

        ModRegistryBase.DirtWallItem = new BlockItem(ModRegistryBase.DirtWall,
                this.setItemBlockId(new Item.Properties(), ModRegistryBase.DirtWall));

        ModRegistryBase.DirtStairsItem = new BlockItem(ModRegistryBase.DirtStairs,
                this.setItemBlockId(new Item.Properties(), ModRegistryBase.DirtStairs));

        ModRegistryBase.DirtSlabItem = new BlockItem(ModRegistryBase.DirtSlab,
                this.setItemBlockId(new Item.Properties(), ModRegistryBase.DirtSlab));

        ModRegistryBase.LightSwitchItem = new BlockItem(ModRegistryBase.LightSwitch,
                this.setItemBlockId(new Item.Properties(), ModRegistryBase.LightSwitch));

        ModRegistryBase.DarkLampItem = new BlockItem(ModRegistryBase.DarkLamp,
                this.setItemBlockId(new Item.Properties(), ModRegistryBase.DarkLamp));

        ModRegistryBase.QuartzCreteItem = new BlockItem(ModRegistryBase.QuartzCrete,
                this.setItemBlockId(new Item.Properties(), ModRegistryBase.QuartzCrete));

        ModRegistryBase.QuartzCreteWallItem = new BlockItem(ModRegistryBase.QuartzCreteWall,
                this.setItemBlockId(new Item.Properties(), ModRegistryBase.QuartzCreteWall));

        ModRegistryBase.QuartzCreteBricksItem = new BlockItem(ModRegistryBase.QuartzCreteBricks,
                this.setItemBlockId(new Item.Properties(), ModRegistryBase.QuartzCreteBricks));

        ModRegistryBase.ChiseledQuartzCreteItem = new BlockItem(ModRegistryBase.ChiseledQuartzCrete,
                this.setItemBlockId(new Item.Properties(), ModRegistryBase.ChiseledQuartzCrete));

        ModRegistryBase.QuartzCretePillarItem = new BlockItem(ModRegistryBase.QuartzCretePillar,
                this.setItemBlockId(new Item.Properties(), ModRegistryBase.QuartzCretePillar));

        ModRegistryBase.QuartzCreteStairsItem = new BlockItem(ModRegistryBase.QuartzCreteStairs,
                this.setItemBlockId(new Item.Properties(), ModRegistryBase.QuartzCreteStairs));

        ModRegistryBase.QuartzCreteSlabItem = new BlockItem(ModRegistryBase.QuartzCreteSlab,
                this.setItemBlockId(new Item.Properties(), ModRegistryBase.QuartzCreteSlab));

        ModRegistryBase.SmoothQuartzCreteItem = new BlockItem(ModRegistryBase.SmoothQuartzCrete,
                this.setItemBlockId(new Item.Properties(), ModRegistryBase.SmoothQuartzCrete));

        ModRegistryBase.SmoothQuartzCreteWallItem = new BlockItem(ModRegistryBase.SmoothQuartzCreteWall,
                this.setItemBlockId(new Item.Properties(), ModRegistryBase.SmoothQuartzCreteWall));

        ModRegistryBase.SmoothQuartzCreteStairsItem = new BlockItem(ModRegistryBase.SmoothQuartzCreteStairs,
                this.setItemBlockId(new Item.Properties(), ModRegistryBase.SmoothQuartzCreteStairs));

        ModRegistryBase.SmoothQuartzCreteSlabItem = new BlockItem(ModRegistryBase.SmoothQuartzCreteSlab,
                this.setItemBlockId(new Item.Properties(), ModRegistryBase.SmoothQuartzCreteSlab));

        ModRegistryBase.ItemEmptyCrate = new ItemBlockWoodenCrate(ModRegistryBase.EmptyCrate, ItemWoodenCrate.CrateType.Empty,
                this.setItemBlockId(new Item.Properties(), ModRegistryBase.EmptyCrate));

        ModRegistryBase.ItemCartonOfEggs = new ItemBlockWoodenCrate(ModRegistryBase.CartonOfEggs, ItemWoodenCrate.CrateType.Carton_Of_Eggs,
                this.setItemBlockId(new Item.Properties(), ModRegistryBase.CartonOfEggs));

        ModRegistryBase.ItemCrateOfPotatoes = new ItemBlockWoodenCrate(ModRegistryBase.CrateOfPotatoes, ItemWoodenCrate.CrateType.Crate_Of_Potatoes,
                this.setItemBlockId(new Item.Properties(), ModRegistryBase.CrateOfPotatoes));

        ModRegistryBase.ItemCrateOfCarrots = new ItemBlockWoodenCrate(ModRegistryBase.CrateOfCarrots, ItemWoodenCrate.CrateType.Crate_Of_Carrots,
                this.setItemBlockId(new Item.Properties(), ModRegistryBase.CrateOfCarrots));

        ModRegistryBase.ItemCrateOfBeets = new ItemBlockWoodenCrate(ModRegistryBase.CrateOfBeets, ItemWoodenCrate.CrateType.Crate_Of_Beets,
                this.setItemBlockId(new Item.Properties(), ModRegistryBase.CrateOfBeets));

        this.initializeModLoaderBlockItems();
    }

    /**
     * This will initialize default implementations of block items unless overridden.
     * This is called at the end of initializeBlockItems.
     */
    public void initializeModLoaderBlockItems() {
        ModRegistryBase.BoundaryItem = new BlockItem(ModRegistryBase.Boundary,
                this.setItemBlockId(new Item.Properties(), ModRegistryBase.Boundary));

        ModRegistryBase.GlassSlabItem = new BlockItem(ModRegistryBase.GlassSlab,
                this.setItemBlockId(new Item.Properties(), ModRegistryBase.GlassSlab));

        ModRegistryBase.GlassStairsItem = new BlockItem(ModRegistryBase.GlassStairs,
                this.setItemBlockId(new Item.Properties(), ModRegistryBase.GlassStairs));

        ModRegistryBase.PaperLanternItem = new BlockItem(ModRegistryBase.PaperLantern,
                this.setItemBlockId(new Item.Properties(), ModRegistryBase.PaperLantern));

        ModRegistryBase.PhasicItem = new BlockItem(ModRegistryBase.Phasic,
                this.setItemBlockId(new Item.Properties(), ModRegistryBase.Phasic));
    }

    public void initializeItems() {
        ModRegistryBase.LogoItem = new Item(this.setItemId(new Item.Properties(), "item_logo"));

        ModRegistryBase.ItemPileOfBricks = new BlockItem(ModRegistryBase.PileOfBricks,
                this.setItemBlockId(new Item.Properties(), ModRegistryBase.PileOfBricks));

        ModRegistryBase.ItemPalletOfBricks = new BlockItem(ModRegistryBase.PalletOfBricks,
                this.setItemBlockId(new Item.Properties(), ModRegistryBase.PalletOfBricks));

        ModRegistryBase.ItemBundleOfTimber = new BlockItem(ModRegistryBase.BundleOfTimber,
                this.setItemBlockId(new Item.Properties(), ModRegistryBase.BundleOfTimber));

        ModRegistryBase.ItemHeapOfTimber = new BlockItem(ModRegistryBase.HeapOfTimber,
                this.setItemBlockId(new Item.Properties(), ModRegistryBase.HeapOfTimber));

        ModRegistryBase.ItemTonOfTimber = new BlockItem(ModRegistryBase.TonOfTimber,
                this.setItemBlockId(new Item.Properties(), ModRegistryBase.TonOfTimber));

        ModRegistryBase.StringOfLanterns = new Item(this.setItemId(new Item.Properties(), "item_string_of_lanterns"));
        ModRegistryBase.CoilOfLanterns = new Item(this.setItemId(new Item.Properties(), "item_coil_of_lanterns"));
        ModRegistryBase.Upgrade = new Item(this.setItemId(new Item.Properties(), "item_upgrade"));

        ModRegistryBase.SwiftBladeWood = new ItemSwiftBlade(ToolMaterial.WOOD, 2, .5f, this.setItemId(new Item.Properties(), "item_swift_blade_wood"));
        ModRegistryBase.SwiftBladeStone = new ItemSwiftBlade(ToolMaterial.STONE, 2, .5f, this.setItemId(new Item.Properties(), "item_swift_blade_stone"));
        ModRegistryBase.SwiftBladeIron = new ItemSwiftBlade(ToolMaterial.IRON, 2, .5f, this.setItemId(new Item.Properties(), "item_swift_blade_iron"));
        ModRegistryBase.SwiftBladeDiamond = new ItemSwiftBlade(ToolMaterial.DIAMOND, 2, .5f, this.setItemId(new Item.Properties(), "item_swift_blade_diamond"));
        ModRegistryBase.SwiftBladeGold = new ItemSwiftBlade(ToolMaterial.GOLD, 2, .5f, this.setItemId(new Item.Properties(), "item_swift_blade_gold"));
        ModRegistryBase.SwiftBladeCopper = new ItemSwiftBlade(COPPER_MATERIAL, 2, .5f, this.setItemId(new Item.Properties(), "item_swift_blade_copper"));
        ModRegistryBase.SwiftBladeOsmium = new ItemSwiftBlade(OSMIUM_MATERIAL, 2, .5f, this.setItemId(new Item.Properties(), "item_swift_blade_osmium"));
        ModRegistryBase.SwiftBladeBronze = new ItemSwiftBlade(BRONZE_MATERIAL, 2, .5f, this.setItemId(new Item.Properties(), "item_swift_blade_bronze"));
        ModRegistryBase.SwiftBladeSteel = new ItemSwiftBlade(STEEL_MATERIAL, 2, .5f, this.setItemId(new Item.Properties(), "item_swift_blade_steel"));
        ModRegistryBase.SwiftBladeObsidian = new ItemSwiftBlade(OBSIDIAN_MATERIAL, 2, .5f, this.setItemId(new Item.Properties(), "item_swift_blade_obsidian"));
        ModRegistryBase.SwiftBladeNetherite = new ItemSwiftBlade(ToolMaterial.NETHERITE, 2, .5f, this.setItemId(new Item.Properties(), "item_swift_blade_netherite"));

        ModRegistryBase.ClutchOfEggs = new ItemWoodenCrate(ItemWoodenCrate.CrateType.Clutch_Of_Eggs,
                this.setItemId(new Item.Properties(), "item_clutch_of_eggs"));

        ModRegistryBase.BunchOfPotatoes = new ItemWoodenCrate(ItemWoodenCrate.CrateType.Bunch_Of_Potatoes,
                this.setItemId(new Item.Properties(), "item_bunch_of_potatoes"));

        ModRegistryBase.BunchOfCarrots = new ItemWoodenCrate(ItemWoodenCrate.CrateType.Bunch_Of_Carrots,
                this.setItemId(new Item.Properties(), "item_bunch_of_carrots"));

        ModRegistryBase.BunchOfBeets = new ItemWoodenCrate(ItemWoodenCrate.CrateType.Bunch_Of_Beets,
                this.setItemId(new Item.Properties(), "item_bunch_of_beets"));

        this.initializeModLoaderItems();
    }

    /**
     * This will initialize default implementations of items unless overridden.
     * This is called at the end of initializeItems.
     */
    public void initializeModLoaderItems() {
        ModRegistryBase.CompressedChest = new ItemCompressedChest();
        ModRegistryBase.SickleDiamond = new ItemSickle(ToolMaterial.DIAMOND, this.setItemId(new Item.Properties(), "item_sickle_diamond"));
        ModRegistryBase.SickleGold = new ItemSickle(ToolMaterial.GOLD, this.setItemId(new Item.Properties(), "item_sickle_gold"));
        ModRegistryBase.SickleNetherite = new ItemSickle(ToolMaterial.NETHERITE, this.setItemId(new Item.Properties(), "item_sickle_netherite"));
        ModRegistryBase.SickleIron = new ItemSickle(ToolMaterial.IRON, this.setItemId(new Item.Properties(), "item_sickle_iron"));
        ModRegistryBase.SickleStone = new ItemSickle(ToolMaterial.STONE, this.setItemId(new Item.Properties(), "item_sickle_stone"));
        ModRegistryBase.SickleWood = new ItemSickle(ToolMaterial.WOOD, this.setItemId(new Item.Properties(), "item_sickle_wood"));
    }

    public void initializeBluePrintItems() {
        ModRegistryBase.House = new ItemHouse(this.setItemId(new Item.Properties(), "item_house"));
        ModRegistryBase.HouseImproved = new ItemHouseImproved(this.setItemId(new Item.Properties(), "item_house_improved"));
        ModRegistryBase.HouseAdvanced = new ItemHouseAdvanced(this.setItemId(new Item.Properties(), "item_house_advanced"));
        ModRegistryBase.InstantBridge = new ItemInstantBridge(this.setItemId(new Item.Properties(), "item_instant_bridge"));
        ModRegistryBase.MachineryTower = new ItemBasicStructure(BasicStructureConfiguration.EnumBasicStructureName.MachineryTower, this.setItemId(new Item.Properties(), "item_machinery_tower"));
        ModRegistryBase.DefenseBunker = new ItemBasicStructure(BasicStructureConfiguration.EnumBasicStructureName.DefenseBunker, this.setItemId(new Item.Properties(), "item_defense_bunker"));
        ModRegistryBase.MineshaftEntrance = new ItemBasicStructure(BasicStructureConfiguration.EnumBasicStructureName.MineshaftEntrance, this.setItemId(new Item.Properties(), "item_mineshaft_entrance"));
        ModRegistryBase.EnderGateway = new ItemBasicStructure(BasicStructureConfiguration.EnumBasicStructureName.EnderGateway, this.setItemId(new Item.Properties(), "item_ender_gateway"));
        ModRegistryBase.AquaBase = new ItemBasicStructure(BasicStructureConfiguration.EnumBasicStructureName.AquaBase, this.setItemId(new Item.Properties(), "item_aqua_base"));
        ModRegistryBase.GrassyPlain = new ItemBasicStructure(BasicStructureConfiguration.EnumBasicStructureName.GrassyPlain, this.setItemId(new Item.Properties(), "item_grassy_plain"));
        ModRegistryBase.MagicTemple = new ItemBasicStructure(BasicStructureConfiguration.EnumBasicStructureName.MagicTemple, this.setItemId(new Item.Properties(), "item_magic_temple"));
        ModRegistryBase.WatchTower = new ItemBasicStructure(BasicStructureConfiguration.EnumBasicStructureName.WatchTower, this.setItemId(new Item.Properties(), "item_watch_tower"));
        ModRegistryBase.WelcomeCenter = new ItemBasicStructure(BasicStructureConfiguration.EnumBasicStructureName.WelcomeCenter, this.setItemId(new Item.Properties(), "item_welcome_center"));
        ModRegistryBase.Jail = new ItemBasicStructure(BasicStructureConfiguration.EnumBasicStructureName.Jail, this.setItemId(new Item.Properties(), "item_jail"));
        ModRegistryBase.Saloon = new ItemBasicStructure(BasicStructureConfiguration.EnumBasicStructureName.Saloon, this.setItemId(new Item.Properties(), "item_saloon"));
        ModRegistryBase.SkiLodge = new ItemBasicStructure(BasicStructureConfiguration.EnumBasicStructureName.SkiLodge, this.setItemId(new Item.Properties(), "item_ski_lodge"));
        ModRegistryBase.WindMill = new ItemBasicStructure(BasicStructureConfiguration.EnumBasicStructureName.WindMill, this.setItemId(new Item.Properties(), "item_wind_mill"));
        ModRegistryBase.TownHall = new ItemBasicStructure(BasicStructureConfiguration.EnumBasicStructureName.TownHall, this.setItemId(new Item.Properties(), "item_town_hall"));
        ModRegistryBase.NetherGate = new ItemBasicStructure(BasicStructureConfiguration.EnumBasicStructureName.NetherGate, this.setItemId(new Item.Properties(), "item_nether_gate"));
        ModRegistryBase.AquaBaseImproved = new ItemBasicStructure(BasicStructureConfiguration.EnumBasicStructureName.AquaBaseImproved, this.setItemId(new Item.Properties(), "item_aqua_base_improved"));
        ModRegistryBase.Warehouse = new ItemBasicStructure(BasicStructureConfiguration.EnumBasicStructureName.Warehouse, this.setItemId(new Item.Properties(), "item_warehouse"));
        ModRegistryBase.WareHouseImproved = new ItemBasicStructure(BasicStructureConfiguration.EnumBasicStructureName.WarehouseImproved, this.setItemId(new Item.Properties(), "item_warehouse_improved"));
        ModRegistryBase.VillagerHouses = new ItemBasicStructure(BasicStructureConfiguration.EnumBasicStructureName.VillagerHouses, this.setItemId(new Item.Properties(), "item_villager_houses"), 10);
        ModRegistryBase.ModernBuildings = new ItemBasicStructure(BasicStructureConfiguration.EnumBasicStructureName.ModernBuildings, this.setItemId(new Item.Properties(), "item_modern_buildings"));
        ModRegistryBase.ModernBuildingsImproved = new ItemBasicStructure(BasicStructureConfiguration.EnumBasicStructureName.ModernBuildingsImproved, this.setItemId(new Item.Properties(), "item_modern_buildings_improved"));
        ModRegistryBase.ModernBuildingsAdvanced = new ItemBasicStructure(BasicStructureConfiguration.EnumBasicStructureName.ModernBuildingsAdvanced, this.setItemId(new Item.Properties(), "item_modern_buildings_advanced"));
        ModRegistryBase.Farm = new ItemBasicStructure(BasicStructureConfiguration.EnumBasicStructureName.Farm, this.setItemId(new Item.Properties(), "item_farm"));
        ModRegistryBase.FarmImproved = new ItemBasicStructure(BasicStructureConfiguration.EnumBasicStructureName.FarmImproved, this.setItemId(new Item.Properties(), "item_farm_improved"));
        ModRegistryBase.FarmAdvanced = new ItemBasicStructure(BasicStructureConfiguration.EnumBasicStructureName.FarmAdvanced, this.setItemId(new Item.Properties(), "item_farm_advanced"));

        this.initializeModLoaderBluePrintItems();
    }

    /**
     * This will initialize default implementations of blueprints unless overridden.
     * This is called at the end of initializeBluePrintItems.
     */
    public void initializeModLoaderBluePrintItems() {
        ModRegistryBase.Bulldozer = new ItemBulldozer(this.setItemId(new Item.Properties(), "item_bulldozer"));
        ModRegistryBase.CreativeBulldozer = new ItemBulldozer(this.setItemId(new Item.Properties(), "item_creative_bulldozer"), true);
    }

    public void initializeRecipeSerializers() {
    }

    public void initializeSounds() {
        ModRegistryBase.BuildingBlueprint = SoundEvent.createVariableRangeEvent(ResourceLocation.tryBuild(PrefabBase.MODID, "building_blueprint"));
    }

    public ResourceKey<Block> createBlockKey(String name) {
        return ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(PrefabBase.MODID, name));
    }

    public ResourceKey<Item> createItemKey(String name) {
        return ResourceKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(PrefabBase.MODID, name));
    }

    public BlockBehaviour.Properties setBlockId(BlockBehaviour.Properties properties, String name) {
        return properties.setId(createBlockKey(name));
    }

    public Item.Properties setItemId(Item.Properties properties, String name) {
        return properties.setId(createItemKey(name));
    }

    public Item.Properties setItemBlockId(Item.Properties properties, BlockBehaviour block) {
        return this.setItemId(properties.useBlockDescriptionPrefix(),  block.getDescriptionId().replace("block.prefab.", ""));
    }
}
