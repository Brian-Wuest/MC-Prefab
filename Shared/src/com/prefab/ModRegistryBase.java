package com.prefab;

import com.prefab.blocks.*;
import com.prefab.blocks.BlockBoundary;
import com.prefab.blocks.BlockGlassSlab;
import com.prefab.blocks.BlockGlassStairs;
import com.prefab.blocks.BlockPaperLantern;
import com.prefab.items.ItemCompressedChest;
import com.prefab.items.ItemSwiftBlade;
import com.prefab.registries.ModRegistries;
import com.prefab.structures.items.*;
import com.prefab.blocks.entities.LightSwitchBlockEntity;
import com.prefab.blocks.entities.StructureScannerBlockEntity;
import com.prefab.items.ItemBlockWoodenCrate;
import com.prefab.items.ItemSickle;
import com.prefab.items.ItemWoodenCrate;
import com.prefab.recipe.ConditionedSmeltingRecipe;
import com.prefab.structures.config.BasicStructureConfiguration;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.LazyLoadedValue;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static net.minecraft.world.item.Tiers.WOOD;

public class ModRegistryBase {
    public static ModRegistries serverModRegistries;
    public static final ArrayList<Consumer<Object>> guiRegistrations = new ArrayList<>();

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

    /* *********************************** Recipe Serializers *********************************** */
    public static RecipeSerializer<ConditionedSmeltingRecipe> ConditionedSmeltingRecipeSeriaizer;

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
        ModRegistryBase.CompressedStone = new BlockCompressedStone(BlockCompressedStone.EnumType.COMPRESSED_STONE);
        ModRegistryBase.DoubleCompressedStone = new BlockCompressedStone(BlockCompressedStone.EnumType.DOUBLE_COMPRESSED_STONE);
        ModRegistryBase.TripleCompressedStone = new BlockCompressedStone(BlockCompressedStone.EnumType.TRIPLE_COMPRESSED_STONE);
        ModRegistryBase.CompressedDirt = new BlockCompressedStone(BlockCompressedStone.EnumType.COMPRESSED_DIRT);
        ModRegistryBase.DoubleCompressedDirt = new BlockCompressedStone(BlockCompressedStone.EnumType.DOUBLE_COMPRESSED_DIRT);
        ModRegistryBase.CompressedGlowstone = new BlockCompressedStone(BlockCompressedStone.EnumType.COMPRESSED_GLOWSTONE);
        ModRegistryBase.DoubleCompressedGlowstone = new BlockCompressedStone(BlockCompressedStone.EnumType.DOUBLE_COMPRESSED_GLOWSTONE);
        ModRegistryBase.CompressedQuartzCrete = new BlockCompressedStone(BlockCompressedStone.EnumType.COMPRESSED_QUARTZCRETE);
        ModRegistryBase.DoubleCompressedQuartzCrete = new BlockCompressedStone(BlockCompressedStone.EnumType.DOUBLE_COMPRESSED_QUARTZCRETE);

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
        ModRegistryBase.PileOfBricks = new BlockRotatableHorizontalShaped(BlockShaped.BlockShape.PileOfBricks, Block.Properties.ofFullCopy(Blocks.BRICKS).mapColor(MapColor.COLOR_RED).noOcclusion().isViewBlocking(ModRegistryBase::never));
        ModRegistryBase.PalletOfBricks = new BlockRotatableHorizontalShaped(BlockShaped.BlockShape.PalletOfBricks, Block.Properties.ofFullCopy(Blocks.BRICKS).mapColor(MapColor.COLOR_RED).noOcclusion().isViewBlocking(ModRegistryBase::never));
        ModRegistryBase.BundleOfTimber = new BlockRotatableHorizontalShaped(BlockShaped.BlockShape.BundleOfTimber, Block.Properties.ofFullCopy(Blocks.OAK_WOOD).mapColor(MapColor.COLOR_BROWN).sound(SoundType.WOOD).noOcclusion().isViewBlocking(ModRegistryBase::never));
        ModRegistryBase.HeapOfTimber = new BlockRotatableHorizontalShaped(BlockShaped.BlockShape.HeapOfTimber, Block.Properties.ofFullCopy(Blocks.OAK_WOOD).mapColor(MapColor.COLOR_BROWN).sound(SoundType.WOOD).noOcclusion().isViewBlocking(ModRegistryBase::never));
        ModRegistryBase.TonOfTimber = new BlockRotatableHorizontalShaped(BlockShaped.BlockShape.TonOfTimber, Block.Properties.ofFullCopy(Blocks.OAK_WOOD).mapColor(MapColor.COLOR_BROWN).sound(SoundType.WOOD).noOcclusion().isViewBlocking(ModRegistryBase::never));
        ModRegistryBase.EmptyCrate = new BlockRotatable(Block.Properties.ofFullCopy(Blocks.OAK_WOOD).sound(SoundType.WOOD));
        ModRegistryBase.CartonOfEggs = new BlockRotatable(Block.Properties.ofFullCopy(Blocks.OAK_WOOD).sound(SoundType.WOOD));
        ModRegistryBase.CrateOfPotatoes = new BlockRotatable(Block.Properties.ofFullCopy(Blocks.OAK_WOOD).sound(SoundType.WOOD));
        ModRegistryBase.CrateOfCarrots = new BlockRotatable(Block.Properties.ofFullCopy(Blocks.OAK_WOOD).sound(SoundType.WOOD));
        ModRegistryBase.CrateOfBeets = new BlockRotatable(Block.Properties.ofFullCopy(Blocks.OAK_WOOD).sound(SoundType.WOOD));
        ModRegistryBase.QuartzCrete = new Block(Block.Properties.ofFullCopy(Blocks.QUARTZ_BLOCK));
        ModRegistryBase.QuartzCreteWall = new WallBlock(Block.Properties.ofFullCopy(ModRegistryBase.QuartzCrete));
        ModRegistryBase.QuartzCreteBricks = new Block(Block.Properties.ofFullCopy(ModRegistryBase.QuartzCrete));
        ModRegistryBase.ChiseledQuartzCrete = new Block(Block.Properties.ofFullCopy(Blocks.CHISELED_QUARTZ_BLOCK));
        ModRegistryBase.QuartzCretePillar = new RotatedPillarBlock(Block.Properties.ofFullCopy(Blocks.QUARTZ_PILLAR));
        ModRegistryBase.QuartzCreteStairs = new BlockCustomStairs(ModRegistryBase.QuartzCrete.defaultBlockState(), Block.Properties.ofFullCopy(ModRegistryBase.QuartzCrete));
        ModRegistryBase.QuartzCreteSlab = new SlabBlock(Block.Properties.ofFullCopy(ModRegistryBase.QuartzCrete));
        ModRegistryBase.SmoothQuartzCrete = new Block(Block.Properties.ofFullCopy(ModRegistryBase.QuartzCrete));
        ModRegistryBase.SmoothQuartzCreteWall = new WallBlock(Block.Properties.ofFullCopy(ModRegistryBase.SmoothQuartzCrete));
        ModRegistryBase.SmoothQuartzCreteStairs = new BlockCustomStairs(ModRegistryBase.SmoothQuartzCrete.defaultBlockState(), Block.Properties.ofFullCopy(ModRegistryBase.SmoothQuartzCrete));
        ModRegistryBase.SmoothQuartzCreteSlab = new SlabBlock(Block.Properties.ofFullCopy(SmoothQuartzCrete));

        this.initializeModLoaderBlocks();
    }

    /**
     * This will initialize default implementations of blocks unless overridden.
     * This is called at the end of initializeBlocks.
     */
    public void initializeModLoaderBlocks() {
        ModRegistryBase.Boundary = new BlockBoundary();
        ModRegistryBase.GlassSlab = new BlockGlassSlab(Block.Properties.ofFullCopy(Blocks.GLASS));
        ModRegistryBase.GlassStairs = new BlockGlassStairs(Blocks.GLASS.defaultBlockState(), Block.Properties.ofFullCopy(Blocks.GLASS));
        ModRegistryBase.PaperLantern = new BlockPaperLantern();
        ModRegistryBase.Phasic = new BlockPhasic();
    }

    public void initializeBlockItems() {
        ModRegistryBase.CompressedStoneItem = new BlockItem(ModRegistryBase.CompressedStone, new Item.Properties());
        ModRegistryBase.DoubleCompressedStoneItem = new BlockItem(ModRegistryBase.DoubleCompressedStone, new Item.Properties());
        ModRegistryBase.TripleCompressedStoneItem = new BlockItem(ModRegistryBase.TripleCompressedStone, new Item.Properties());
        ModRegistryBase.CompressedDirtItem = new BlockItem(ModRegistryBase.CompressedDirt, new Item.Properties());
        ModRegistryBase.DoubleCompressedDirtItem = new BlockItem(ModRegistryBase.DoubleCompressedDirt, new Item.Properties());
        ModRegistryBase.CompressedGlowstoneItem = new BlockItem(ModRegistryBase.CompressedGlowstone, new Item.Properties());
        ModRegistryBase.DoubleCompressedGlowstoneItem = new BlockItem(ModRegistryBase.DoubleCompressedGlowstone, new Item.Properties());
        ModRegistryBase.CompressedQuartzCreteItem = new BlockItem(ModRegistryBase.CompressedQuartzCrete, new Item.Properties());
        ModRegistryBase.DoubleCompressedQuartzCreteItem = new BlockItem(ModRegistryBase.DoubleCompressedQuartzCrete, new Item.Properties());

        ModRegistryBase.CompressedObsidianItem = new BlockItem(ModRegistryBase.CompressedObsidian, new Item.Properties());
        ModRegistryBase.DoubleCompressedObsidianItem = new BlockItem(ModRegistryBase.DoubleCompressedObsidian, new Item.Properties());
        ModRegistryBase.GrassSlabItem = new BlockItem(ModRegistryBase.GrassSlab, new Item.Properties());
        ModRegistryBase.GrassStairsItem = new BlockItem(ModRegistryBase.GrassStairs, new Item.Properties());
        ModRegistryBase.GrassWallItem = new BlockItem(ModRegistryBase.GrassWall, new Item.Properties());
        ModRegistryBase.DirtWallItem = new BlockItem(ModRegistryBase.DirtWall, new Item.Properties());
        ModRegistryBase.DirtStairsItem = new BlockItem(ModRegistryBase.DirtStairs, new Item.Properties());
        ModRegistryBase.DirtSlabItem = new BlockItem(ModRegistryBase.DirtSlab, new Item.Properties());
        ModRegistryBase.LightSwitchItem = new BlockItem(ModRegistryBase.LightSwitch, new Item.Properties());
        ModRegistryBase.DarkLampItem = new BlockItem(ModRegistryBase.DarkLamp, new Item.Properties());
        ModRegistryBase.QuartzCreteItem = new BlockItem(ModRegistryBase.QuartzCrete, new Item.Properties());
        ModRegistryBase.QuartzCreteWallItem = new BlockItem(ModRegistryBase.QuartzCreteWall, new Item.Properties());
        ModRegistryBase.QuartzCreteBricksItem = new BlockItem(ModRegistryBase.QuartzCreteBricks, new Item.Properties());
        ModRegistryBase.ChiseledQuartzCreteItem = new BlockItem(ModRegistryBase.ChiseledQuartzCrete, new Item.Properties());
        ModRegistryBase.QuartzCretePillarItem = new BlockItem(ModRegistryBase.QuartzCretePillar, new Item.Properties());
        ModRegistryBase.QuartzCreteStairsItem = new BlockItem(ModRegistryBase.QuartzCreteStairs, new Item.Properties());
        ModRegistryBase.QuartzCreteSlabItem = new BlockItem(ModRegistryBase.QuartzCreteSlab, new Item.Properties());
        ModRegistryBase.SmoothQuartzCreteItem = new BlockItem(ModRegistryBase.SmoothQuartzCrete, new Item.Properties());
        ModRegistryBase.SmoothQuartzCreteWallItem = new BlockItem(ModRegistryBase.SmoothQuartzCreteWall, new Item.Properties());
        ModRegistryBase.SmoothQuartzCreteStairsItem = new BlockItem(ModRegistryBase.SmoothQuartzCreteStairs, new Item.Properties());
        ModRegistryBase.SmoothQuartzCreteSlabItem = new BlockItem(ModRegistryBase.SmoothQuartzCreteSlab, new Item.Properties());

        ModRegistryBase.ItemEmptyCrate = new ItemBlockWoodenCrate(ModRegistryBase.EmptyCrate, ItemWoodenCrate.CrateType.Empty);
        ModRegistryBase.ItemCartonOfEggs = new ItemBlockWoodenCrate(ModRegistryBase.CartonOfEggs, ItemWoodenCrate.CrateType.Carton_Of_Eggs);
        ModRegistryBase.ItemCrateOfPotatoes = new ItemBlockWoodenCrate(ModRegistryBase.CrateOfPotatoes, ItemWoodenCrate.CrateType.Crate_Of_Potatoes);
        ModRegistryBase.ItemCrateOfCarrots = new ItemBlockWoodenCrate(ModRegistryBase.CrateOfCarrots, ItemWoodenCrate.CrateType.Crate_Of_Carrots);
        ModRegistryBase.ItemCrateOfBeets = new ItemBlockWoodenCrate(ModRegistryBase.CrateOfBeets, ItemWoodenCrate.CrateType.Crate_Of_Beets);

        this.initializeModLoaderBlockItems();
    }

    /**
     * This will initialize default implementations of block items unless overridden.
     * This is called at the end of initializeBlockItems.
     */
    public void initializeModLoaderBlockItems() {
        ModRegistryBase.BoundaryItem = new BlockItem(ModRegistryBase.Boundary, new Item.Properties());
        ModRegistryBase.GlassSlabItem = new BlockItem(ModRegistryBase.GlassSlab, new Item.Properties());
        ModRegistryBase.GlassStairsItem = new BlockItem(ModRegistryBase.GlassStairs, new Item.Properties());
        ModRegistryBase.PaperLanternItem = new BlockItem(ModRegistryBase.PaperLantern, new Item.Properties());
        ModRegistryBase.PhasicItem = new BlockItem(ModRegistryBase.Phasic, new Item.Properties());
    }

    public void initializeItems() {
        ModRegistryBase.LogoItem = new Item(new Item.Properties());
        ModRegistryBase.ItemPileOfBricks = new BlockItem(ModRegistryBase.PileOfBricks, new Item.Properties());
        ModRegistryBase.ItemPalletOfBricks = new BlockItem(ModRegistryBase.PalletOfBricks, new Item.Properties());
        ModRegistryBase.ItemBundleOfTimber = new BlockItem(ModRegistryBase.BundleOfTimber, new Item.Properties());
        ModRegistryBase.ItemHeapOfTimber = new BlockItem(ModRegistryBase.HeapOfTimber, new Item.Properties());
        ModRegistryBase.ItemTonOfTimber = new BlockItem(ModRegistryBase.TonOfTimber, new Item.Properties());
        ModRegistryBase.StringOfLanterns = new Item(new Item.Properties());
        ModRegistryBase.CoilOfLanterns = new Item(new Item.Properties());
        ModRegistryBase.Upgrade = new Item(new Item.Properties());
        ModRegistryBase.SwiftBladeWood = new ItemSwiftBlade(WOOD, 2, .5f);
        ModRegistryBase.SwiftBladeStone = new ItemSwiftBlade(Tiers.STONE, 2, .5f);
        ModRegistryBase.SwiftBladeIron = new ItemSwiftBlade(Tiers.IRON, 2, .5f);
        ModRegistryBase.SwiftBladeDiamond = new ItemSwiftBlade(Tiers.DIAMOND, 2, .5f);
        ModRegistryBase.SwiftBladeGold = new ItemSwiftBlade(Tiers.GOLD, 2, .5f);
        ModRegistryBase.SwiftBladeCopper = new ItemSwiftBlade(ModRegistryBase.CustomItemTier.COPPER, 2, .5f);
        ModRegistryBase.SwiftBladeOsmium = new ItemSwiftBlade(ModRegistryBase.CustomItemTier.OSMIUM, 2, .5f);
        ModRegistryBase.SwiftBladeBronze = new ItemSwiftBlade(ModRegistryBase.CustomItemTier.BRONZE, 2, .5f);
        ModRegistryBase.SwiftBladeSteel = new ItemSwiftBlade(ModRegistryBase.CustomItemTier.STEEL, 2, .5f);
        ModRegistryBase.SwiftBladeObsidian = new ItemSwiftBlade(ModRegistryBase.CustomItemTier.OBSIDIAN, 2, .5f);
        ModRegistryBase.SwiftBladeNetherite = new ItemSwiftBlade(Tiers.NETHERITE, 2, .5f);
        ModRegistryBase.ClutchOfEggs = new ItemWoodenCrate(ItemWoodenCrate.CrateType.Clutch_Of_Eggs);
        ModRegistryBase.BunchOfPotatoes = new ItemWoodenCrate(ItemWoodenCrate.CrateType.Bunch_Of_Potatoes);
        ModRegistryBase.BunchOfCarrots = new ItemWoodenCrate(ItemWoodenCrate.CrateType.Bunch_Of_Carrots);
        ModRegistryBase.BunchOfBeets = new ItemWoodenCrate(ItemWoodenCrate.CrateType.Bunch_Of_Beets);

        this.initializeModLoaderItems();
    }

    /**
     * This will initialize default implementations of items unless overridden.
     * This is called at the end of initializeItems.
     */
    public void initializeModLoaderItems() {
        ModRegistryBase.CompressedChest = new ItemCompressedChest();
        ModRegistryBase.SickleDiamond = new ItemSickle(Tiers.DIAMOND);
        ModRegistryBase.SickleGold = new ItemSickle(Tiers.GOLD);
        ModRegistryBase.SickleNetherite = new ItemSickle(Tiers.NETHERITE);
        ModRegistryBase.SickleIron = new ItemSickle(Tiers.IRON);
        ModRegistryBase.SickleStone = new ItemSickle(Tiers.STONE);
        ModRegistryBase.SickleWood = new ItemSickle(Tiers.WOOD);
    }

    public void initializeBluePrintItems() {
        ModRegistryBase.House = new ItemHouse();
        ModRegistryBase.HouseImproved = new ItemHouseImproved();
        ModRegistryBase.HouseAdvanced = new ItemHouseAdvanced();
        ModRegistryBase.InstantBridge = new ItemInstantBridge();
        ModRegistryBase.MachineryTower = new ItemBasicStructure(BasicStructureConfiguration.EnumBasicStructureName.MachineryTower);
        ModRegistryBase.DefenseBunker = new ItemBasicStructure(BasicStructureConfiguration.EnumBasicStructureName.DefenseBunker);
        ModRegistryBase.MineshaftEntrance = new ItemBasicStructure(BasicStructureConfiguration.EnumBasicStructureName.MineshaftEntrance);
        ModRegistryBase.EnderGateway = new ItemBasicStructure(BasicStructureConfiguration.EnumBasicStructureName.EnderGateway);
        ModRegistryBase.AquaBase = new ItemBasicStructure(BasicStructureConfiguration.EnumBasicStructureName.AquaBase);
        ModRegistryBase.GrassyPlain = new ItemBasicStructure(BasicStructureConfiguration.EnumBasicStructureName.GrassyPlain);
        ModRegistryBase.MagicTemple = new ItemBasicStructure(BasicStructureConfiguration.EnumBasicStructureName.MagicTemple);
        ModRegistryBase.WatchTower = new ItemBasicStructure(BasicStructureConfiguration.EnumBasicStructureName.WatchTower);
        ModRegistryBase.WelcomeCenter = new ItemBasicStructure(BasicStructureConfiguration.EnumBasicStructureName.WelcomeCenter);
        ModRegistryBase.Jail = new ItemBasicStructure(BasicStructureConfiguration.EnumBasicStructureName.Jail);
        ModRegistryBase.Saloon = new ItemBasicStructure(BasicStructureConfiguration.EnumBasicStructureName.Saloon);
        ModRegistryBase.SkiLodge = new ItemBasicStructure(BasicStructureConfiguration.EnumBasicStructureName.SkiLodge);
        ModRegistryBase.WindMill = new ItemBasicStructure(BasicStructureConfiguration.EnumBasicStructureName.WindMill);
        ModRegistryBase.TownHall = new ItemBasicStructure(BasicStructureConfiguration.EnumBasicStructureName.TownHall);
        ModRegistryBase.NetherGate = new ItemBasicStructure(BasicStructureConfiguration.EnumBasicStructureName.NetherGate);
        ModRegistryBase.AquaBaseImproved = new ItemBasicStructure(BasicStructureConfiguration.EnumBasicStructureName.AquaBaseImproved);
        ModRegistryBase.Warehouse = new ItemBasicStructure(BasicStructureConfiguration.EnumBasicStructureName.Warehouse);
        ModRegistryBase.WareHouseImproved = new ItemBasicStructure(BasicStructureConfiguration.EnumBasicStructureName.WarehouseImproved);
        ModRegistryBase.VillagerHouses = new ItemBasicStructure(BasicStructureConfiguration.EnumBasicStructureName.VillagerHouses, 10);
        ModRegistryBase.ModernBuildings = new ItemBasicStructure(BasicStructureConfiguration.EnumBasicStructureName.ModernBuildings);
        ModRegistryBase.ModernBuildingsImproved = new ItemBasicStructure(BasicStructureConfiguration.EnumBasicStructureName.ModernBuildingsImproved);
        ModRegistryBase.ModernBuildingsAdvanced = new ItemBasicStructure(BasicStructureConfiguration.EnumBasicStructureName.ModernBuildingsAdvanced);
        ModRegistryBase.Farm = new ItemBasicStructure(BasicStructureConfiguration.EnumBasicStructureName.Farm);
        ModRegistryBase.FarmImproved = new ItemBasicStructure(BasicStructureConfiguration.EnumBasicStructureName.FarmImproved);
        ModRegistryBase.FarmAdvanced = new ItemBasicStructure(BasicStructureConfiguration.EnumBasicStructureName.FarmAdvanced);

        this.initializeModLoaderBluePrintItems();
    }

    /**
     * This will initialize default implementations of blueprints unless overridden.
     * This is called at the end of initializeBluePrintItems.
     */
    public void initializeModLoaderBluePrintItems() {
        ModRegistryBase.Bulldozer = new ItemBulldozer();
        ModRegistryBase.CreativeBulldozer = new ItemBulldozer(true);
    }

    public void initializeRecipeSerializers() {
        ModRegistryBase.ConditionedSmeltingRecipeSeriaizer = new ConditionedSmeltingRecipe.Serializer();
    }

    public void initializeSounds() {
        ModRegistryBase.BuildingBlueprint = SoundEvent.createVariableRangeEvent(ResourceLocation.tryBuild(PrefabBase.MODID, "building_blueprint"));
    }

    public enum CustomItemTier implements Tier {
        COPPER("Copper", (int)Tiers.STONE.getAttackDamageBonus(), Tiers.STONE.getUses(), Tiers.STONE.getSpeed(),
                Tiers.STONE.getAttackDamageBonus(), Tiers.STONE.getEnchantmentValue(), () -> {
            return Ingredient
                    .of(Utils.getItemStacksWithTag(ResourceLocation.tryBuild("c", "ingots/copper")).stream());
        }, BlockTags.INCORRECT_FOR_STONE_TOOL),
        OSMIUM("Osmium", (int)Tiers.IRON.getAttackDamageBonus(), 500, Tiers.IRON.getSpeed(),
                Tiers.IRON.getAttackDamageBonus() + .5f, Tiers.IRON.getEnchantmentValue(), () -> {
            return Ingredient
                    .of(Utils.getItemStacksWithTag(ResourceLocation.tryBuild("c", "ingots/osmium")).stream());
        }, BlockTags.INCORRECT_FOR_IRON_TOOL),
        BRONZE("Bronze", (int)Tiers.IRON.getAttackDamageBonus(), Tiers.IRON.getUses(), Tiers.IRON.getSpeed(),
                Tiers.IRON.getAttackDamageBonus(), Tiers.IRON.getEnchantmentValue(), () -> {
            return Ingredient
                    .of(Utils.getItemStacksWithTag(ResourceLocation.tryBuild("c", "ingots/bronze")).stream());
        }, BlockTags.INCORRECT_FOR_IRON_TOOL),
        STEEL("Steel", (int)Tiers.DIAMOND.getAttackDamageBonus(), (int) (Tiers.IRON.getUses() * 1.5),
                Tiers.DIAMOND.getSpeed(), Tiers.DIAMOND.getAttackDamageBonus(),
                Tiers.DIAMOND.getEnchantmentValue(), () -> {
            return Ingredient
                    .of(Utils.getItemStacksWithTag(ResourceLocation.tryBuild("c", "ingots/steel")).stream());
        }, BlockTags.INCORRECT_FOR_DIAMOND_TOOL),
        OBSIDIAN("Obsidian", (int)Tiers.DIAMOND.getAttackDamageBonus(), (int) (Tiers.DIAMOND.getUses() * 1.5),
                Tiers.DIAMOND.getSpeed(), Tiers.DIAMOND.getAttackDamageBonus(),
                Tiers.DIAMOND.getEnchantmentValue(), () -> {
            return Ingredient.of(Item.byBlock(Blocks.OBSIDIAN));
        }, BlockTags.INCORRECT_FOR_DIAMOND_TOOL);

        private final String name;
        private final int harvestLevel;
        private final int maxUses;
        private final float efficiency;
        private final float attackDamage;
        private final int enchantability;
        private final LazyLoadedValue<Ingredient> repairMaterial;
        private final TagKey<Block> incorrectBlocksForDrops;

        CustomItemTier(String name, int harvestLevelIn, int maxUsesIn, float efficiencyIn, float attackDamageIn,
                       int enchantability, Supplier<Ingredient> repairMaterialIn, TagKey<Block> incorrectBlocksForDrops) {
            this.name = name;
            this.harvestLevel = harvestLevelIn;
            this.maxUses = maxUsesIn;
            this.efficiency = efficiencyIn;
            this.attackDamage = attackDamageIn;
            this.enchantability = enchantability;
            this.repairMaterial = new LazyLoadedValue<>(repairMaterialIn);
            this.incorrectBlocksForDrops = incorrectBlocksForDrops;
        }

        public static CustomItemTier getByName(String name) {
            for (CustomItemTier item : CustomItemTier.values()) {
                if (item.getName().equals(name)) {
                    return item;
                }
            }

            return null;
        }

        public String getName() {
            return this.name;
        }

        public int getUses() {
            return this.maxUses;
        }

        public float getSpeed() {
            return this.efficiency;
        }

        public float getAttackDamageBonus() {
            return this.attackDamage;
        }

        @Override
        public @NotNull TagKey<Block> getIncorrectBlocksForDrops() {
            return this.incorrectBlocksForDrops;
        }

        public int getLevel() {
            return this.harvestLevel;
        }

        public int getEnchantmentValue() {
            return this.enchantability;
        }

        public @NotNull Ingredient getRepairIngredient() {
            return this.repairMaterial.get();
        }
    }
}
