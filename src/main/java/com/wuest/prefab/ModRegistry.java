package com.wuest.prefab;

import com.wuest.prefab.blocks.*;
import com.wuest.prefab.blocks.entities.StructureScannerBlockEntity;
import com.wuest.prefab.items.*;
import com.wuest.prefab.proxy.messages.ConfigSyncMessage;
import com.wuest.prefab.proxy.messages.PlayerEntityTagMessage;
import com.wuest.prefab.proxy.messages.handlers.ConfigSyncHandler;
import com.wuest.prefab.proxy.messages.handlers.PlayerEntityHandler;
import com.wuest.prefab.structures.config.BasicStructureConfiguration.EnumBasicStructureName;
import com.wuest.prefab.structures.items.*;
import com.wuest.prefab.structures.messages.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.LazyLoadedValue;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * This is the mod registry so there is a way to get to all instances of the blocks/items created by this mod.
 *
 * @author WuestMan
 */
@SuppressWarnings({"unused", "ConstantConditions"})
public class ModRegistry {
    public static final ArrayList<Consumer<Object>> guiRegistrations = new ArrayList<>();

    /**
     * Deferred registry for items.
     */
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Prefab.MODID);
    /**
     * Deferred registry for blocks.
     */
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Prefab.MODID);
    /**
     * Deferred registry for tile entities.
     */
    public static final DeferredRegister<BlockEntityType<?>> TILE_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, Prefab.MODID);
    public static final RegistryObject<BlockCompressedStone> CompressedStone = BLOCKS.register(BlockCompressedStone.EnumType.COMPRESSED_STONE.getUnlocalizedName(), () -> new BlockCompressedStone(BlockCompressedStone.EnumType.COMPRESSED_STONE));

    public static final CreativeModeTab PREFAB_GROUP = new CreativeModeTab("prefab.logo") {
        @OnlyIn(Dist.CLIENT)
        public ItemStack makeIcon() {
            return new ItemStack(ModRegistry.ItemLogo.get());
        }
    };

    /* *********************************** Blocks *********************************** */
    public static final RegistryObject<BlockItem> CompressedStoneItem = ITEMS.register(BlockCompressedStone.EnumType.COMPRESSED_STONE.getUnlocalizedName(), () -> new BlockItem(CompressedStone.get(), new Item.Properties().tab(ModRegistry.PREFAB_GROUP)));
    public static final RegistryObject<BlockCompressedStone> DoubleCompressedStone = BLOCKS.register(BlockCompressedStone.EnumType.DOUBLE_COMPRESSED_STONE.getUnlocalizedName(), () -> new BlockCompressedStone(BlockCompressedStone.EnumType.DOUBLE_COMPRESSED_STONE));
    public static final RegistryObject<BlockItem> DoubleCompressedStoneItem = ITEMS.register(BlockCompressedStone.EnumType.DOUBLE_COMPRESSED_STONE.getUnlocalizedName(), () -> new BlockItem(DoubleCompressedStone.get(), new Item.Properties().tab(ModRegistry.PREFAB_GROUP)));
    public static final RegistryObject<BlockCompressedStone> TripleCompressedStone = BLOCKS.register(BlockCompressedStone.EnumType.TRIPLE_COMPRESSED_STONE.getUnlocalizedName(), () -> new BlockCompressedStone(BlockCompressedStone.EnumType.TRIPLE_COMPRESSED_STONE));
    public static final RegistryObject<BlockItem> TripleCompressedStoneItem = ITEMS.register(BlockCompressedStone.EnumType.TRIPLE_COMPRESSED_STONE.getUnlocalizedName(), () -> new BlockItem(TripleCompressedStone.get(), new Item.Properties().tab(ModRegistry.PREFAB_GROUP)));
    public static final RegistryObject<BlockCompressedStone> CompressedGlowStone = BLOCKS.register(BlockCompressedStone.EnumType.COMPRESSED_GLOWSTONE.getUnlocalizedName(), () -> new BlockCompressedStone(BlockCompressedStone.EnumType.COMPRESSED_GLOWSTONE));
    public static final RegistryObject<BlockItem> CompressedGlowStoneItem = ITEMS.register(BlockCompressedStone.EnumType.COMPRESSED_GLOWSTONE.getUnlocalizedName(), () -> new BlockItem(CompressedGlowStone.get(), new Item.Properties().tab(ModRegistry.PREFAB_GROUP)));
    public static final RegistryObject<BlockCompressedStone> DoubleCompressedGlowStone = BLOCKS.register(BlockCompressedStone.EnumType.DOUBLE_COMPRESSED_GLOWSTONE.getUnlocalizedName(), () -> new BlockCompressedStone(BlockCompressedStone.EnumType.DOUBLE_COMPRESSED_GLOWSTONE));
    public static final RegistryObject<BlockItem> DoubleCompressedGlowStoneItem = ITEMS.register(BlockCompressedStone.EnumType.DOUBLE_COMPRESSED_GLOWSTONE.getUnlocalizedName(), () -> new BlockItem(DoubleCompressedGlowStone.get(), new Item.Properties().tab(ModRegistry.PREFAB_GROUP)));
    public static final RegistryObject<BlockCompressedStone> CompressedDirt = BLOCKS.register(BlockCompressedStone.EnumType.COMPRESSED_DIRT.getUnlocalizedName(), () -> new BlockCompressedStone(BlockCompressedStone.EnumType.COMPRESSED_DIRT));
    public static final RegistryObject<BlockItem> CompressedDirtItem = ITEMS.register(BlockCompressedStone.EnumType.COMPRESSED_DIRT.getUnlocalizedName(), () -> new BlockItem(CompressedDirt.get(), new Item.Properties().tab(ModRegistry.PREFAB_GROUP)));
    public static final RegistryObject<BlockCompressedStone> DoubleCompressedDirt = BLOCKS.register(BlockCompressedStone.EnumType.DOUBLE_COMPRESSED_DIRT.getUnlocalizedName(), () -> new BlockCompressedStone(BlockCompressedStone.EnumType.DOUBLE_COMPRESSED_DIRT));
    public static final RegistryObject<BlockItem> DoubleCompressedDirtItem = ITEMS.register(BlockCompressedStone.EnumType.DOUBLE_COMPRESSED_DIRT.getUnlocalizedName(), () -> new BlockItem(DoubleCompressedDirt.get(), new Item.Properties().tab(ModRegistry.PREFAB_GROUP)));
    public static final RegistryObject<BlockCompressedObsidian> CompressedObsidian = BLOCKS.register(BlockCompressedObsidian.EnumType.COMPRESSED_OBSIDIAN.getSerializedName(), () -> new BlockCompressedObsidian(BlockCompressedObsidian.EnumType.COMPRESSED_OBSIDIAN));
    public static final RegistryObject<BlockItem> CompressedObsidianItem = ITEMS.register(BlockCompressedObsidian.EnumType.COMPRESSED_OBSIDIAN.getSerializedName(), () -> new BlockItem(CompressedObsidian.get(), new Item.Properties().tab(ModRegistry.PREFAB_GROUP)));
    public static final RegistryObject<BlockCompressedObsidian> DoubleCompressedObsidian = BLOCKS.register(BlockCompressedObsidian.EnumType.DOUBLE_COMPRESSED_OBSIDIAN.getSerializedName(), () -> new BlockCompressedObsidian(BlockCompressedObsidian.EnumType.DOUBLE_COMPRESSED_OBSIDIAN));
    public static final RegistryObject<BlockItem> DoubleCompressedObsidianItem = ITEMS.register(BlockCompressedObsidian.EnumType.DOUBLE_COMPRESSED_OBSIDIAN.getSerializedName(), () -> new BlockItem(DoubleCompressedObsidian.get(), new Item.Properties().tab(ModRegistry.PREFAB_GROUP)));
    public static final RegistryObject<BlockPhasing> BlockPhasing = BLOCKS.register("block_phasing", com.wuest.prefab.blocks.BlockPhasing::new);
    public static final RegistryObject<BlockItem> BlockPhasingItem = ITEMS.register("block_phasing", () -> new BlockItem(BlockPhasing.get(), new Item.Properties().tab(ModRegistry.PREFAB_GROUP)));
    public static final RegistryObject<BlockBoundary> BlockBoundary = BLOCKS.register("block_boundary", com.wuest.prefab.blocks.BlockBoundary::new);

    /* public static final RegistryObject<BlockStructureScanner> StructureScanner = BLOCKS.register("block_structure_scanner", com.wuest.prefab.blocks.BlockStructureScanner::new);
     */
    /* *********************************** Item Blocks *********************************** */
    public static final RegistryObject<BlockItem> BlockBoundaryItem = ITEMS.register("block_boundary", () -> new BlockItem(BlockBoundary.get(), new Item.Properties().tab(ModRegistry.PREFAB_GROUP)));
    public static final RegistryObject<BlockPaperLantern> PaperLantern = BLOCKS.register("block_paper_lantern", BlockPaperLantern::new);
    public static final RegistryObject<BlockItem> PaperLanternItem = ITEMS.register("block_paper_lantern", () -> new BlockItem(PaperLantern.get(), new Item.Properties().tab(ModRegistry.PREFAB_GROUP)));
    public static final RegistryObject<BlockGlassStairs> GlassStairs = BLOCKS.register("block_glass_stairs", () -> new BlockGlassStairs(Blocks.GLASS.defaultBlockState(), Block.Properties.copy(Blocks.GLASS)));
    public static final RegistryObject<BlockItem> GlassStairsItem = ITEMS.register("block_glass_stairs", () -> new BlockItem(GlassStairs.get(), new Item.Properties().tab(ModRegistry.PREFAB_GROUP)));
    public static final RegistryObject<BlockGlassSlab> GlassSlab = BLOCKS.register("block_glass_slab", () -> new BlockGlassSlab(Block.Properties.copy(Blocks.GLASS)));
    public static final RegistryObject<BlockItem> GlassSlabItem = ITEMS.register("block_glass_slab", () -> new BlockItem(GlassSlab.get(), new Item.Properties().tab(ModRegistry.PREFAB_GROUP)));
    public static final RegistryObject<BlockCustomWall> DirtWall = BLOCKS.register("block_dirt_wall", () -> new BlockCustomWall(Blocks.DIRT, BlockCustomWall.EnumType.DIRT));
    public static final RegistryObject<BlockItem> DirtWallItem = ITEMS.register("block_dirt_wall", () -> new BlockItem(DirtWall.get(), new Item.Properties().tab(ModRegistry.PREFAB_GROUP)));
    public static final RegistryObject<BlockCustomWall> GrassWall = BLOCKS.register("block_grass_wall", () -> new BlockCustomWall(Blocks.GRASS_BLOCK, BlockCustomWall.EnumType.GRASS));
    public static final RegistryObject<BlockItem> GrassWallItem = ITEMS.register("block_grass_wall", () -> new BlockItem(GrassWall.get(), new Item.Properties().tab(ModRegistry.PREFAB_GROUP)));
    public static final RegistryObject<BlockDirtSlab> DirtSlab = BLOCKS.register("block_dirt_slab", com.wuest.prefab.blocks.BlockDirtSlab::new);
    public static final RegistryObject<BlockItem> DirtSlabItem = ITEMS.register("block_dirt_slab", () -> new BlockItem(DirtSlab.get(), new Item.Properties().tab(ModRegistry.PREFAB_GROUP)));
    public static final RegistryObject<BlockGrassSlab> GrassSlab = BLOCKS.register("block_grass_slab", com.wuest.prefab.blocks.BlockGrassSlab::new);
    public static final RegistryObject<BlockItem> GrassSlabItem = ITEMS.register("block_grass_slab", () -> new BlockItem(GrassSlab.get(), new Item.Properties().tab(ModRegistry.PREFAB_GROUP)));
    public static final RegistryObject<BlockDirtStairs> DirtStairs = BLOCKS.register("block_dirt_stairs", com.wuest.prefab.blocks.BlockDirtStairs::new);
    public static final RegistryObject<BlockItem> DirtStairsItem = ITEMS.register("block_dirt_stairs", () -> new BlockItem(DirtStairs.get(), new Item.Properties().tab(ModRegistry.PREFAB_GROUP)));
    public static final RegistryObject<BlockGrassStairs> GrassStairs = BLOCKS.register("block_grass_stairs", com.wuest.prefab.blocks.BlockGrassStairs::new);
    public static final RegistryObject<BlockItem> GrassStairsItem = ITEMS.register("block_grass_stairs", () -> new BlockItem(GrassStairs.get(), new Item.Properties().tab(ModRegistry.PREFAB_GROUP)));

    /*public static final RegistryObject<BlockItem> StructureScannerItem = ITEMS.register("block_structure_scanner", () -> new BlockItem(StructureScanner.get(), new Item.Properties().tab(ModRegistry.PREFAB_GROUP)) );
     */
    /* *********************************** Tile Entities *********************************** */
    /*public static final RegistryObject<TileEntityType<StructureScannerBlockEntity>> StructureScannerTileEntity = TILE_ENTITIES.register("structure_scanner_entity", () -> {
        ModRegistry.StructureScannerEntityType = new TileEntityType<>(
                StructureScannerBlockEntity::new, new HashSet<>(Arrays.asList(ModRegistry.StructureScanner.get())), null);

        return ModRegistry.StructureScannerEntityType;
    });*/

    /* *********************************** Items *********************************** */
    public static final RegistryObject<Item> ItemLogo = ITEMS.register("item_logo", () -> new Item(new Item.Properties()));
    
    public static final RegistryObject<ItemPileOfBricks> ItemPileOfBricks = ITEMS.register("item_pile_of_bricks", com.wuest.prefab.items.ItemPileOfBricks::new);
    public static final RegistryObject<ItemPalletOfBricks> ItemPalletOfBricks = ITEMS.register("item_pallet_of_bricks", com.wuest.prefab.items.ItemPalletOfBricks::new);
    public static final RegistryObject<ItemBundleOfTimber> ItemBundleOfTimber = ITEMS.register("item_bundle_of_timber", com.wuest.prefab.items.ItemBundleOfTimber::new);
    public static final RegistryObject<ItemBundleOfTimber> ItemHeapOfTimber = ITEMS.register("item_heap_of_timber", com.wuest.prefab.items.ItemBundleOfTimber::new);
    public static final RegistryObject<ItemBundleOfTimber> ItemTonOfTimber = ITEMS.register("item_ton_of_timber", com.wuest.prefab.items.ItemBundleOfTimber::new);
    public static final RegistryObject<ItemCompressedChest> ItemCompressedChest = ITEMS.register("item_compressed_chest", com.wuest.prefab.items.ItemCompressedChest::new);
    public static final RegistryObject<ItemStringOfLanterns> ItemStringOfLanterns = ITEMS.register("item_string_of_lanterns", com.wuest.prefab.items.ItemStringOfLanterns::new);
    public static final RegistryObject<ItemCoilOfLanterns> ItemCoilOfLanterns = ITEMS.register("item_coil_of_lanterns", com.wuest.prefab.items.ItemCoilOfLanterns::new);

    public static final RegistryObject<ItemSwiftBlade> SwiftBladeWood = ITEMS.register("item_swift_blade_wood", () -> new ItemSwiftBlade(Tiers.WOOD, 2, .5f));
    public static final RegistryObject<ItemSwiftBlade> SwiftBladeStone = ITEMS.register("item_swift_blade_stone", () -> new ItemSwiftBlade(Tiers.STONE, 2, .5f));
    public static final RegistryObject<ItemSwiftBlade> SwiftBladeIron = ITEMS.register("item_swift_blade_iron", () -> new ItemSwiftBlade(Tiers.IRON, 2, .5f));
    public static final RegistryObject<ItemSwiftBlade> SwiftBladeDiamond = ITEMS.register("item_swift_blade_diamond", () -> new ItemSwiftBlade(Tiers.DIAMOND, 2, .5f));
    public static final RegistryObject<ItemSwiftBlade> SwiftBladeGold = ITEMS.register("item_swift_blade_gold", () -> new ItemSwiftBlade(Tiers.GOLD, 2, .5f));
    public static final RegistryObject<ItemSwiftBlade> SwiftBladeCopper = ITEMS.register("item_swift_blade_copper", () -> new ItemSwiftBlade(CustomItemTier.COPPER, 2, .5f));
    public static final RegistryObject<ItemSwiftBlade> SwiftBladeOsmium = ITEMS.register("item_swift_blade_osmium", () -> new ItemSwiftBlade(CustomItemTier.OSMIUM, 2, .5f));
    public static final RegistryObject<ItemSwiftBlade> SwiftBladeBronze = ITEMS.register("item_swift_blade_bronze", () -> new ItemSwiftBlade(CustomItemTier.BRONZE, 2, .5f));
    public static final RegistryObject<ItemSwiftBlade> SwiftBladeSteel = ITEMS.register("item_swift_blade_steel", () -> new ItemSwiftBlade(CustomItemTier.STEEL, 2, .5f));
    public static final RegistryObject<ItemSwiftBlade> SwiftBladeObsidian = ITEMS.register("item_swift_blade_obsidian", () -> new ItemSwiftBlade(CustomItemTier.OBSIDIAN, 2, .5f));
    public static final RegistryObject<ItemSwiftBlade> SwiftBladeNetherite = ITEMS.register("item_swift_blade_netherite", () -> new ItemSwiftBlade(Tiers.NETHERITE, 2, .5f));

    public static final RegistryObject<ItemSickle> SickleWood = ITEMS.register("item_sickle_wood", () -> new ItemSickle(Tiers.WOOD));
    public static final RegistryObject<ItemSickle> SickleStone = ITEMS.register("item_sickle_stone", () -> new ItemSickle(Tiers.STONE));
    public static final RegistryObject<ItemSickle> SickleGold = ITEMS.register("item_sickle_gold", () -> new ItemSickle(Tiers.GOLD));
    public static final RegistryObject<ItemSickle> SickleIron = ITEMS.register("item_sickle_iron", () -> new ItemSickle(Tiers.IRON));
    public static final RegistryObject<ItemSickle> SickleDiamond = ITEMS.register("item_sickle_diamond", () -> new ItemSickle(Tiers.DIAMOND));
    public static final RegistryObject<ItemSickle> SickleNetherite = ITEMS.register("item_sickle_netherite", () -> new ItemSickle(Tiers.NETHERITE));

    public static final RegistryObject<ItemWoodenCrate> EmptyCrate = ITEMS.register("item_wooden_crate", () -> new ItemWoodenCrate(ItemWoodenCrate.CrateType.Empty));
    public static final RegistryObject<ItemWoodenCrate> ClutchOfEggs = ITEMS.register("item_clutch_of_eggs", () -> new ItemWoodenCrate(ItemWoodenCrate.CrateType.Empty));
    public static final RegistryObject<ItemWoodenCrate> CartonOfEggs = ITEMS.register("item_carton_of_eggs", () -> new ItemWoodenCrate(ItemWoodenCrate.CrateType.Empty));
    public static final RegistryObject<ItemWoodenCrate> BunchOfPotatoes = ITEMS.register("item_bunch_of_potatoes", () -> new ItemWoodenCrate(ItemWoodenCrate.CrateType.Empty));
    public static final RegistryObject<ItemWoodenCrate> CrateOfPotatoes = ITEMS.register("item_crate_of_potatoes", () -> new ItemWoodenCrate(ItemWoodenCrate.CrateType.Empty));
    public static final RegistryObject<ItemWoodenCrate> BunchOfCarrots = ITEMS.register("item_bunch_of_carrots", () -> new ItemWoodenCrate(ItemWoodenCrate.CrateType.Empty));
    public static final RegistryObject<ItemWoodenCrate> CrateOfCarrots = ITEMS.register("item_crate_of_carrots", () -> new ItemWoodenCrate(ItemWoodenCrate.CrateType.Empty));
    public static final RegistryObject<ItemWoodenCrate> BunchOfBeets = ITEMS.register("item_bunch_of_beets", () -> new ItemWoodenCrate(ItemWoodenCrate.CrateType.Empty));
    public static final RegistryObject<ItemWoodenCrate> CrateOfBeets = ITEMS.register("item_crate_of_beets", () -> new ItemWoodenCrate(ItemWoodenCrate.CrateType.Empty));
    public static final RegistryObject<ItemStartHouse> StartHouse = ITEMS.register("item_start_house", ItemStartHouse::new);

    /* *********************************** Blueprint Items *********************************** */
    public static final RegistryObject<ItemWarehouseUpgrade> WarehouseUpgrade = ITEMS.register("item_warehouse_upgrade", com.wuest.prefab.items.ItemWarehouseUpgrade::new);
    public static final RegistryObject<ItemInstantBridge> InstantBridge = ITEMS.register("item_instant_bridge", com.wuest.prefab.structures.items.ItemInstantBridge::new);
    public static final RegistryObject<ItemModerateHouse> ModerateHouse = ITEMS.register("item_moderate_house", com.wuest.prefab.structures.items.ItemModerateHouse::new);
    public static final RegistryObject<ItemBulldozer> Bulldozer = ITEMS.register("item_bulldozer", com.wuest.prefab.structures.items.ItemBulldozer::new);
    public static final RegistryObject<ItemBulldozer> Creative_Bulldozer = ITEMS.register("item_creative_bulldozer", () -> new ItemBulldozer(true));

    public static final RegistryObject<ItemBasicStructure> MachineryTower = ITEMS.register(EnumBasicStructureName.MachineryTower.getItemTextureLocation().getPath(), () -> new ItemBasicStructure(EnumBasicStructureName.MachineryTower));
    public static final RegistryObject<ItemBasicStructure> DefenseBunker = ITEMS.register(EnumBasicStructureName.DefenseBunker.getItemTextureLocation().getPath(), () -> new ItemBasicStructure(EnumBasicStructureName.DefenseBunker));
    public static final RegistryObject<ItemBasicStructure> MineshaftEntrance = ITEMS.register(EnumBasicStructureName.MineshaftEntrance.getItemTextureLocation().getPath(), () -> new ItemBasicStructure(EnumBasicStructureName.MineshaftEntrance));
    public static final RegistryObject<ItemBasicStructure> EnderGateway = ITEMS.register(EnumBasicStructureName.EnderGateway.getItemTextureLocation().getPath(), () -> new ItemBasicStructure(EnumBasicStructureName.EnderGateway));
    public static final RegistryObject<ItemBasicStructure> AquaBase = ITEMS.register(EnumBasicStructureName.AquaBase.getItemTextureLocation().getPath(), () -> new ItemBasicStructure(EnumBasicStructureName.AquaBase));
    public static final RegistryObject<ItemBasicStructure> GrassyPlain = ITEMS.register(EnumBasicStructureName.GrassyPlain.getItemTextureLocation().getPath(), () -> new ItemBasicStructure(EnumBasicStructureName.GrassyPlain));
    public static final RegistryObject<ItemBasicStructure> MagicTemple = ITEMS.register(EnumBasicStructureName.MagicTemple.getItemTextureLocation().getPath(), () -> new ItemBasicStructure(EnumBasicStructureName.MagicTemple));
    public static final RegistryObject<ItemBasicStructure> WatchTower = ITEMS.register(EnumBasicStructureName.WatchTower.getItemTextureLocation().getPath(), () -> new ItemBasicStructure(EnumBasicStructureName.WatchTower));
    public static final RegistryObject<ItemBasicStructure> WelcomeCenter = ITEMS.register(EnumBasicStructureName.WelcomeCenter.getItemTextureLocation().getPath(), () -> new ItemBasicStructure(EnumBasicStructureName.WelcomeCenter));
    public static final RegistryObject<ItemBasicStructure> Jail = ITEMS.register(EnumBasicStructureName.Jail.getItemTextureLocation().getPath(), () -> new ItemBasicStructure(EnumBasicStructureName.Jail));
    public static final RegistryObject<ItemBasicStructure> Saloon = ITEMS.register(EnumBasicStructureName.Saloon.getItemTextureLocation().getPath(), () -> new ItemBasicStructure(EnumBasicStructureName.Saloon));
    public static final RegistryObject<ItemBasicStructure> SkiLodge = ITEMS.register(EnumBasicStructureName.SkiLodge.getItemTextureLocation().getPath(), () -> new ItemBasicStructure(EnumBasicStructureName.SkiLodge));
    public static final RegistryObject<ItemBasicStructure> WindMill = ITEMS.register(EnumBasicStructureName.WindMill.getItemTextureLocation().getPath(), () -> new ItemBasicStructure(EnumBasicStructureName.WindMill));
    public static final RegistryObject<ItemBasicStructure> TownHall = ITEMS.register(EnumBasicStructureName.TownHall.getItemTextureLocation().getPath(), () -> new ItemBasicStructure(EnumBasicStructureName.TownHall));
    public static final RegistryObject<ItemBasicStructure> NetherGate = ITEMS.register(EnumBasicStructureName.NetherGate.getItemTextureLocation().getPath(), () -> new ItemBasicStructure(EnumBasicStructureName.NetherGate));
    public static final RegistryObject<ItemBasicStructure> AdvancedAquaBase = ITEMS.register(EnumBasicStructureName.AdvancedAquaBase.getItemTextureLocation().getPath(), () -> new ItemBasicStructure(EnumBasicStructureName.AdvancedAquaBase));
    public static final RegistryObject<ItemBasicStructure> Workshop = ITEMS.register(EnumBasicStructureName.WorkShop.getItemTextureLocation().getPath(), () -> new ItemBasicStructure(EnumBasicStructureName.WorkShop));
    public static final RegistryObject<ItemBasicStructure> VillagerHouses = ITEMS.register(EnumBasicStructureName.VillagerHouses.getItemTextureLocation().getPath(), () -> new ItemBasicStructure(EnumBasicStructureName.VillagerHouses, 10));
    public static final RegistryObject<ItemBasicStructure> AdvancedWareHouse = ITEMS.register(EnumBasicStructureName.AdvancedWarehouse.getItemTextureLocation().getPath(), () -> new ItemBasicStructure(EnumBasicStructureName.AdvancedWarehouse));
    public static final RegistryObject<ItemBasicStructure> WareHouse = ITEMS.register(EnumBasicStructureName.Warehouse.getItemTextureLocation().getPath(), () -> new ItemBasicStructure(EnumBasicStructureName.Warehouse));
    public static final RegistryObject<ItemBasicStructure> ModernBuilding = ITEMS.register(EnumBasicStructureName.ModernBuildings.getItemTextureLocation().getPath(), () -> new ItemBasicStructure(EnumBasicStructureName.ModernBuildings, 5));
    public static BlockEntityType<StructureScannerBlockEntity> StructureScannerEntityType = null;
    public static final RegistryObject<ItemBasicStructure> StarterFarm = ITEMS.register(EnumBasicStructureName.StarterFarm.getItemTextureLocation().getPath(), () -> new ItemBasicStructure(EnumBasicStructureName.StarterFarm));
    public static final RegistryObject<ItemBasicStructure> ModerateFarm = ITEMS.register(EnumBasicStructureName.ModerateFarm.getItemTextureLocation().getPath(), () -> new ItemBasicStructure(EnumBasicStructureName.ModerateFarm));
    public static final RegistryObject<ItemBasicStructure> AdvancedFarm = ITEMS.register(EnumBasicStructureName.AdvancedFarm.getItemTextureLocation().getPath(), () -> new ItemBasicStructure(EnumBasicStructureName.AdvancedFarm));

    /**
     * This is where the mod messages are registered.
     */
    public static void RegisterMessages() {
        AtomicInteger index = new AtomicInteger();
        Prefab.network.messageBuilder(ConfigSyncMessage.class, index.getAndIncrement())
                .encoder(ConfigSyncMessage::encode)
                .decoder(ConfigSyncMessage::decode)
                .consumer(ConfigSyncHandler::handle)
                .add();

        Prefab.network.messageBuilder(PlayerEntityTagMessage.class, index.getAndIncrement())
                .encoder(PlayerEntityTagMessage::encode)
                .decoder(PlayerEntityTagMessage::decode)
                .consumer(PlayerEntityHandler::handle)
                .add();

        Prefab.network.messageBuilder(StructureTagMessage.class, index.getAndIncrement())
                .encoder(StructureTagMessage::encode)
                .decoder(StructureTagMessage::decode)
                .consumer(StructureHandler::handle)
                .add();

        Prefab.network.messageBuilder(StructureScannerActionMessage.class, index.getAndIncrement())
                .encoder(StructureScannerActionMessage::encode)
                .decoder(StructureScannerActionMessage::decode)
                .consumer(StructureScannerActionHandler::handle)
                .add();

        Prefab.network.messageBuilder(StructureScannerSyncMessage.class, index.getAndIncrement())
                .encoder(StructureScannerSyncMessage::encode)
                .decoder(StructureScannerSyncMessage::decode)
                .consumer(StructureScannerSyncHandler::handle)
                .add();
    }

    /**
     * This is where mod capabilities are registered.
     */
    public static void RegisterCapabilities() {
    }

    public enum CustomItemTier implements Tier {
        COPPER("Copper", Tiers.STONE.getLevel(), Tiers.STONE.getUses(), Tiers.STONE.getSpeed(),
                Tiers.STONE.getAttackDamageBonus(), Tiers.STONE.getEnchantmentValue(), () -> {
            return Ingredient
                    .of(ItemTags.getAllTags().getTag(new ResourceLocation("c", "copper_ingots")));
        }),
        OSMIUM("Osmium", Tiers.IRON.getLevel(), 500, Tiers.IRON.getSpeed(),
                Tiers.IRON.getAttackDamageBonus() + .5f, Tiers.IRON.getEnchantmentValue(), () -> {
            return Ingredient
                    .of(ItemTags.getAllTags().getTag(new ResourceLocation("c", "osmium_ingots")));
        }),
        BRONZE("Bronze", Tiers.IRON.getLevel(), Tiers.IRON.getUses(), Tiers.IRON.getSpeed(),
                Tiers.IRON.getAttackDamageBonus(), Tiers.IRON.getEnchantmentValue(), () -> {
            return Ingredient
                    .of(ItemTags.getAllTags().getTag(new ResourceLocation("c", "bronze_ingots")));
        }),
        STEEL("Steel", Tiers.DIAMOND.getLevel(), (int) (Tiers.IRON.getUses() * 1.5),
                Tiers.DIAMOND.getSpeed(), Tiers.DIAMOND.getAttackDamageBonus(),
                Tiers.DIAMOND.getEnchantmentValue(), () -> {
            return Ingredient
                    .of(ItemTags.getAllTags().getTag(new ResourceLocation("c", "steel_ingots")));
        }),
        OBSIDIAN("Obsidian", Tiers.DIAMOND.getLevel(), (int) (Tiers.DIAMOND.getUses() * 1.5),
                Tiers.DIAMOND.getSpeed(), Tiers.DIAMOND.getAttackDamageBonus(),
                Tiers.DIAMOND.getEnchantmentValue(), () -> {
            return Ingredient.of(Item.byBlock(Blocks.OBSIDIAN));
        });

        private final String name;
        private final int harvestLevel;
        private final int maxUses;
        private final float efficiency;
        private final float attackDamage;
        private final int enchantability;
        private final LazyLoadedValue<Ingredient> repairMaterial;

        CustomItemTier(String name, int harvestLevelIn, int maxUsesIn, float efficiencyIn, float attackDamageIn,
                       int enchantability, Supplier<Ingredient> repairMaterialIn) {
            this.name = name;
            this.harvestLevel = harvestLevelIn;
            this.maxUses = maxUsesIn;
            this.efficiency = efficiencyIn;
            this.attackDamage = attackDamageIn;
            this.enchantability = enchantability;
            this.repairMaterial = new LazyLoadedValue<>(repairMaterialIn);
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

        public int getLevel() {
            return this.harvestLevel;
        }

        public int getEnchantmentValue() {
            return this.enchantability;
        }

        public Ingredient getRepairIngredient() {
            return this.repairMaterial.get();
        }
    }
}