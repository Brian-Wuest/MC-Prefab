package com.wuest.prefab;

import com.wuest.prefab.blocks.*;
import com.wuest.prefab.items.*;
import com.wuest.prefab.proxy.messages.ConfigSyncMessage;
import com.wuest.prefab.proxy.messages.PlayerEntityTagMessage;
import com.wuest.prefab.proxy.messages.handlers.ConfigSyncHandler;
import com.wuest.prefab.proxy.messages.handlers.PlayerEntityHandler;
import com.wuest.prefab.structures.config.BasicStructureConfiguration.EnumBasicStructureName;
import com.wuest.prefab.structures.items.*;
import com.wuest.prefab.structures.messages.StructureHandler;
import com.wuest.prefab.structures.messages.StructureTagMessage;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * This is the mod registry so there is a way to get to all instances of the blocks/items created by this mod.
 *
 * @author WuestMan
 */
@SuppressWarnings({"unused", "ConstantConditions"})
public class ModRegistry {

    /**
     * Deferred registry for items.
     */
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Prefab.MODID);

    /**
     * Deferred registry for blocks.
     */
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Prefab.MODID);

    /* *********************************** Blocks *********************************** */

    public static final RegistryObject<BlockCompressedStone> CompressedStone = BLOCKS.register(BlockCompressedStone.EnumType.COMPRESSED_STONE.getUnlocalizedName(), () -> new BlockCompressedStone(BlockCompressedStone.EnumType.COMPRESSED_STONE));
    public static final RegistryObject<BlockCompressedStone> DoubleCompressedStone = BLOCKS.register(BlockCompressedStone.EnumType.DOUBLE_COMPRESSED_STONE.getUnlocalizedName(), () -> new BlockCompressedStone(BlockCompressedStone.EnumType.DOUBLE_COMPRESSED_STONE));
    public static final RegistryObject<BlockCompressedStone> TripleCompressedStone = BLOCKS.register(BlockCompressedStone.EnumType.TRIPLE_COMPRESSED_STONE.getUnlocalizedName(), () -> new BlockCompressedStone(BlockCompressedStone.EnumType.TRIPLE_COMPRESSED_STONE));
    public static final RegistryObject<BlockCompressedStone> CompressedGlowStone = BLOCKS.register(BlockCompressedStone.EnumType.COMPRESSED_GLOWSTONE.getUnlocalizedName(), () -> new BlockCompressedStone(BlockCompressedStone.EnumType.COMPRESSED_GLOWSTONE));
    public static final RegistryObject<BlockCompressedStone> DoubleCompressedGlowStone = BLOCKS.register(BlockCompressedStone.EnumType.DOUBLE_COMPRESSED_GLOWSTONE.getUnlocalizedName(), () -> new BlockCompressedStone(BlockCompressedStone.EnumType.DOUBLE_COMPRESSED_GLOWSTONE));
    public static final RegistryObject<BlockCompressedStone> CompressedDirt = BLOCKS.register(BlockCompressedStone.EnumType.COMPRESSED_DIRT.getUnlocalizedName(), () -> new BlockCompressedStone(BlockCompressedStone.EnumType.COMPRESSED_DIRT));
    public static final RegistryObject<BlockCompressedStone> DoubleCompressedDirt = BLOCKS.register(BlockCompressedStone.EnumType.DOUBLE_COMPRESSED_DIRT.getUnlocalizedName(), () -> new BlockCompressedStone(BlockCompressedStone.EnumType.DOUBLE_COMPRESSED_DIRT));
    public static final RegistryObject<BlockCompressedObsidian> CompressedObsidian = BLOCKS.register(BlockCompressedObsidian.EnumType.COMPRESSED_OBSIDIAN.getSerializedName(), () -> new BlockCompressedObsidian(BlockCompressedObsidian.EnumType.COMPRESSED_OBSIDIAN));
    public static final RegistryObject<BlockCompressedObsidian> DoubleCompressedObsidian = BLOCKS.register(BlockCompressedObsidian.EnumType.DOUBLE_COMPRESSED_OBSIDIAN.getSerializedName(), () -> new BlockCompressedObsidian(BlockCompressedObsidian.EnumType.DOUBLE_COMPRESSED_OBSIDIAN));
    public static final RegistryObject<BlockPhasing> BlockPhasing = BLOCKS.register("block_phasing", com.wuest.prefab.blocks.BlockPhasing::new);
    public static final RegistryObject<BlockBoundary> BlockBoundary = BLOCKS.register("block_boundary", com.wuest.prefab.blocks.BlockBoundary::new);
    public static final RegistryObject<BlockPaperLantern> PaperLantern = BLOCKS.register("block_paper_lantern", BlockPaperLantern::new);
    public static final RegistryObject<BlockGlassStairs> GlassStairs = BLOCKS.register("block_glass_stairs", () -> new BlockGlassStairs(Blocks.GLASS.defaultBlockState(), Block.Properties.copy(Blocks.GLASS)));
    public static final RegistryObject<BlockGlassSlab> GlassSlab = BLOCKS.register("block_glass_slab", () -> new BlockGlassSlab(Block.Properties.copy(Blocks.GLASS)));

    /* *********************************** Item Blocks *********************************** */

    public static final RegistryObject<BlockItem> CompressedStoneItem = ITEMS.register(BlockCompressedStone.EnumType.COMPRESSED_STONE.getUnlocalizedName(), () -> new BlockItem(CompressedStone.get(), new Item.Properties().tab(ItemGroup.TAB_BUILDING_BLOCKS)));
    public static final RegistryObject<BlockItem> DoubleCompressedStoneItem = ITEMS.register(BlockCompressedStone.EnumType.DOUBLE_COMPRESSED_STONE.getUnlocalizedName(), () -> new BlockItem(DoubleCompressedStone.get(), new Item.Properties().tab(ItemGroup.TAB_BUILDING_BLOCKS)));
    public static final RegistryObject<BlockItem> TripleCompressedStoneItem = ITEMS.register(BlockCompressedStone.EnumType.TRIPLE_COMPRESSED_STONE.getUnlocalizedName(), () -> new BlockItem(TripleCompressedStone.get(), new Item.Properties().tab(ItemGroup.TAB_BUILDING_BLOCKS)));
    public static final RegistryObject<BlockItem> CompressedGlowStoneItem = ITEMS.register(BlockCompressedStone.EnumType.COMPRESSED_GLOWSTONE.getUnlocalizedName(), () -> new BlockItem(CompressedGlowStone.get(), new Item.Properties().tab(ItemGroup.TAB_BUILDING_BLOCKS)));
    public static final RegistryObject<BlockItem> DoubleCompressedGlowStoneItem = ITEMS.register(BlockCompressedStone.EnumType.DOUBLE_COMPRESSED_GLOWSTONE.getUnlocalizedName(), () -> new BlockItem(DoubleCompressedGlowStone.get(), new Item.Properties().tab(ItemGroup.TAB_BUILDING_BLOCKS)));
    public static final RegistryObject<BlockItem> CompressedDirtItem = ITEMS.register(BlockCompressedStone.EnumType.COMPRESSED_DIRT.getUnlocalizedName(), () -> new BlockItem(CompressedDirt.get(), new Item.Properties().tab(ItemGroup.TAB_BUILDING_BLOCKS)));
    public static final RegistryObject<BlockItem> DoubleCompressedDirtItem = ITEMS.register(BlockCompressedStone.EnumType.DOUBLE_COMPRESSED_DIRT.getUnlocalizedName(), () -> new BlockItem(DoubleCompressedDirt.get(), new Item.Properties().tab(ItemGroup.TAB_BUILDING_BLOCKS)));
    public static final RegistryObject<BlockItem> CompressedObsidianItem = ITEMS.register(BlockCompressedObsidian.EnumType.COMPRESSED_OBSIDIAN.getSerializedName(), () -> new BlockItem(CompressedObsidian.get(), new Item.Properties().tab(ItemGroup.TAB_BUILDING_BLOCKS)));
    public static final RegistryObject<BlockItem> DoubleCompressedObsidianItem = ITEMS.register(BlockCompressedObsidian.EnumType.DOUBLE_COMPRESSED_OBSIDIAN.getSerializedName(), () -> new BlockItem(DoubleCompressedObsidian.get(), new Item.Properties().tab(ItemGroup.TAB_BUILDING_BLOCKS)));
    public static final RegistryObject<BlockItem> BlockPhasingItem = ITEMS.register("block_phasing", () -> new BlockItem(BlockPhasing.get(), new Item.Properties().tab(ItemGroup.TAB_BUILDING_BLOCKS)));
    public static final RegistryObject<BlockItem> BlockBoundaryItem = ITEMS.register("block_boundary", () -> new BlockItem(BlockBoundary.get(), new Item.Properties().tab(ItemGroup.TAB_BUILDING_BLOCKS)));
    public static final RegistryObject<BlockItem> PaperLanternItem = ITEMS.register("block_paper_lantern", () -> new BlockItem(PaperLantern.get(), new Item.Properties().tab(ItemGroup.TAB_BUILDING_BLOCKS)));
    public static final RegistryObject<BlockItem> GlassStairsItem = ITEMS.register("block_glass_stairs", () -> new BlockItem(GlassStairs.get(), new Item.Properties().tab(ItemGroup.TAB_BUILDING_BLOCKS)));
    public static final RegistryObject<BlockItem> GlassSlabItem = ITEMS.register("block_glass_slab", () -> new BlockItem(GlassSlab.get(), new Item.Properties().tab(ItemGroup.TAB_BUILDING_BLOCKS)));

    /* *********************************** Items *********************************** */

    public static final RegistryObject<ItemPileOfBricks> ItemPileOfBricks = ITEMS.register("item_pile_of_bricks", com.wuest.prefab.items.ItemPileOfBricks::new);
    public static final RegistryObject<ItemPalletOfBricks> ItemPalletOfBricks = ITEMS.register("item_pallet_of_bricks", com.wuest.prefab.items.ItemPalletOfBricks::new);
    public static final RegistryObject<ItemBundleOfTimber> ItemBundleOfTimber = ITEMS.register("item_bundle_of_timber", com.wuest.prefab.items.ItemBundleOfTimber::new);
    public static final RegistryObject<ItemBundleOfTimber> ItemHeapOfTimber = ITEMS.register("item_heap_of_timber", com.wuest.prefab.items.ItemBundleOfTimber::new);
    public static final RegistryObject<ItemBundleOfTimber> ItemTonOfTimber = ITEMS.register("item_ton_of_timber", com.wuest.prefab.items.ItemBundleOfTimber::new);
    public static final RegistryObject<ItemCompressedChest> ItemCompressedChest = ITEMS.register("item_compressed_chest", com.wuest.prefab.items.ItemCompressedChest::new);
    public static final RegistryObject<ItemStringOfLanterns> ItemStringOfLanterns = ITEMS.register("item_string_of_lanterns", com.wuest.prefab.items.ItemStringOfLanterns::new);
    public static final RegistryObject<ItemCoilOfLanterns> ItemCoilOfLanterns = ITEMS.register("item_coil_of_lanterns", com.wuest.prefab.items.ItemCoilOfLanterns::new);

    /* *********************************** Blueprint Items *********************************** */

    public static final RegistryObject<ItemStartHouse> StartHouse = ITEMS.register("item_start_house", ItemStartHouse::new);
    public static final RegistryObject<ItemWarehouseUpgrade> WarehouseUpgrade = ITEMS.register("item_warehouse_upgrade", com.wuest.prefab.items.ItemWarehouseUpgrade::new);
    public static final RegistryObject<ItemInstantBridge> InstantBridge = ITEMS.register("item_instant_bridge", com.wuest.prefab.structures.items.ItemInstantBridge::new);
    public static final RegistryObject<ItemModerateHouse> ModerateHouse = ITEMS.register("item_moderate_house", com.wuest.prefab.structures.items.ItemModerateHouse::new);
    public static final RegistryObject<ItemBulldozer> Bulldozer = ITEMS.register("item_bulldozer", com.wuest.prefab.structures.items.ItemBulldozer::new);
    public static final RegistryObject<ItemBulldozer> Creative_Bulldozer = ITEMS.register("item_creative_bulldozer", () -> new ItemBulldozer(true));
    public static final RegistryObject<ItemStructurePart> StructurePart = ITEMS.register("item_structure_part", com.wuest.prefab.structures.items.ItemStructurePart::new);

    public static final RegistryObject<ItemBasicStructure> Barn = ITEMS.register(EnumBasicStructureName.Barn.getItemTextureLocation().getPath(), () -> new ItemBasicStructure(EnumBasicStructureName.Barn));
    public static final RegistryObject<ItemBasicStructure> AdvancedCoop = ITEMS.register(EnumBasicStructureName.AdvancedCoop.getItemTextureLocation().getPath(), () -> new ItemBasicStructure(EnumBasicStructureName.AdvancedCoop));
    public static final RegistryObject<ItemBasicStructure> AdvancedHorseStable = ITEMS.register(EnumBasicStructureName.AdvancedHorseStable.getItemTextureLocation().getPath(), () -> new ItemBasicStructure(EnumBasicStructureName.AdvancedHorseStable));
    public static final RegistryObject<ItemBasicStructure> MachineryTower = ITEMS.register(EnumBasicStructureName.MachineryTower.getItemTextureLocation().getPath(), () -> new ItemBasicStructure(EnumBasicStructureName.MachineryTower));
    public static final RegistryObject<ItemBasicStructure> DefenseBunker = ITEMS.register(EnumBasicStructureName.DefenseBunker.getItemTextureLocation().getPath(), () -> new ItemBasicStructure(EnumBasicStructureName.DefenseBunker));
    public static final RegistryObject<ItemBasicStructure> MineshaftEntrance = ITEMS.register(EnumBasicStructureName.MineshaftEntrance.getItemTextureLocation().getPath(), () -> new ItemBasicStructure(EnumBasicStructureName.MineshaftEntrance));
    public static final RegistryObject<ItemBasicStructure> EnderGateway = ITEMS.register(EnumBasicStructureName.EnderGateway.getItemTextureLocation().getPath(), () -> new ItemBasicStructure(EnumBasicStructureName.EnderGateway));
    public static final RegistryObject<ItemBasicStructure> AquaBase = ITEMS.register(EnumBasicStructureName.AquaBase.getItemTextureLocation().getPath(), () -> new ItemBasicStructure(EnumBasicStructureName.AquaBase));
    public static final RegistryObject<ItemBasicStructure> GrassyPlain = ITEMS.register(EnumBasicStructureName.GrassyPlain.getItemTextureLocation().getPath(), () -> new ItemBasicStructure(EnumBasicStructureName.GrassyPlain));
    public static final RegistryObject<ItemBasicStructure> MagicTemple = ITEMS.register(EnumBasicStructureName.MagicTemple.getItemTextureLocation().getPath(), () -> new ItemBasicStructure(EnumBasicStructureName.MagicTemple));
    public static final RegistryObject<ItemBasicStructure> GreenHouse = ITEMS.register(EnumBasicStructureName.GreenHouse.getItemTextureLocation().getPath(), () -> new ItemBasicStructure(EnumBasicStructureName.GreenHouse));
    public static final RegistryObject<ItemBasicStructure> WatchTower = ITEMS.register(EnumBasicStructureName.WatchTower.getItemTextureLocation().getPath(), () -> new ItemBasicStructure(EnumBasicStructureName.WatchTower));
    public static final RegistryObject<ItemBasicStructure> WelcomeCenter = ITEMS.register(EnumBasicStructureName.WelcomeCenter.getItemTextureLocation().getPath(), () -> new ItemBasicStructure(EnumBasicStructureName.WelcomeCenter));
    public static final RegistryObject<ItemBasicStructure> Jail = ITEMS.register(EnumBasicStructureName.Jail.getItemTextureLocation().getPath(), () -> new ItemBasicStructure(EnumBasicStructureName.Jail));
    public static final RegistryObject<ItemBasicStructure> Saloon = ITEMS.register(EnumBasicStructureName.Saloon.getItemTextureLocation().getPath(), () -> new ItemBasicStructure(EnumBasicStructureName.Saloon));
    public static final RegistryObject<ItemBasicStructure> SkiLodge = ITEMS.register(EnumBasicStructureName.SkiLodge.getItemTextureLocation().getPath(), () -> new ItemBasicStructure(EnumBasicStructureName.SkiLodge));
    public static final RegistryObject<ItemBasicStructure> WindMill = ITEMS.register(EnumBasicStructureName.WindMill.getItemTextureLocation().getPath(), () -> new ItemBasicStructure(EnumBasicStructureName.WindMill));
    public static final RegistryObject<ItemBasicStructure> TownHall = ITEMS.register(EnumBasicStructureName.TownHall.getItemTextureLocation().getPath(), () -> new ItemBasicStructure(EnumBasicStructureName.TownHall));
    public static final RegistryObject<ItemBasicStructure> NetherGate = ITEMS.register(EnumBasicStructureName.NetherGate.getItemTextureLocation().getPath(), () -> new ItemBasicStructure(EnumBasicStructureName.NetherGate));
    public static final RegistryObject<ItemBasicStructure> SugarCaneFarm = ITEMS.register(EnumBasicStructureName.SugarCaneFarm.getItemTextureLocation().getPath(), () -> new ItemBasicStructure(EnumBasicStructureName.SugarCaneFarm));
    public static final RegistryObject<ItemBasicStructure> AdvancedAquaBase = ITEMS.register(EnumBasicStructureName.AdvancedAquaBase.getItemTextureLocation().getPath(), () -> new ItemBasicStructure(EnumBasicStructureName.AdvancedAquaBase));
    public static final RegistryObject<ItemBasicStructure> Workshop = ITEMS.register(EnumBasicStructureName.WorkShop.getItemTextureLocation().getPath(), () -> new ItemBasicStructure(EnumBasicStructureName.WorkShop));
    public static final RegistryObject<ItemBasicStructure> FishPond = ITEMS.register(EnumBasicStructureName.FishPond.getItemTextureLocation().getPath(), () -> new ItemBasicStructure(EnumBasicStructureName.FishPond));
    public static final RegistryObject<ItemBasicStructure> HorseStable = ITEMS.register(EnumBasicStructureName.HorseStable.getItemTextureLocation().getPath(), () -> new ItemBasicStructure(EnumBasicStructureName.HorseStable));
    public static final RegistryObject<ItemBasicStructure> TreeFarm = ITEMS.register(EnumBasicStructureName.TreeFarm.getItemTextureLocation().getPath(), () -> new ItemBasicStructure(EnumBasicStructureName.TreeFarm));
    public static final RegistryObject<ItemBasicStructure> VillagerHouses = ITEMS.register(EnumBasicStructureName.VillagerHouses.getItemTextureLocation().getPath(), () -> new ItemBasicStructure(EnumBasicStructureName.VillagerHouses, 10));
    public static final RegistryObject<ItemBasicStructure> ChickenCoop = ITEMS.register(EnumBasicStructureName.ChickenCoop.getItemTextureLocation().getPath(), () -> new ItemBasicStructure(EnumBasicStructureName.ChickenCoop));
    public static final RegistryObject<ItemBasicStructure> ProduceFarm = ITEMS.register(EnumBasicStructureName.ProduceFarm.getItemTextureLocation().getPath(), () -> new ItemBasicStructure(EnumBasicStructureName.ProduceFarm));
    public static final RegistryObject<ItemBasicStructure> MonsterMasher = ITEMS.register(EnumBasicStructureName.MonsterMasher.getItemTextureLocation().getPath(), () -> new ItemBasicStructure(EnumBasicStructureName.MonsterMasher));
    public static final RegistryObject<ItemBasicStructure> AdvancedWareHouse = ITEMS.register(EnumBasicStructureName.AdvancedWarehouse.getItemTextureLocation().getPath(), () -> new ItemBasicStructure(EnumBasicStructureName.AdvancedWarehouse));
    public static final RegistryObject<ItemBasicStructure> WareHouse = ITEMS.register(EnumBasicStructureName.Warehouse.getItemTextureLocation().getPath(), () -> new ItemBasicStructure(EnumBasicStructureName.Warehouse));

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
    }

    /**
     * This is where mod capabilities are registered.
     */
    public static void RegisterCapabilities() {
    }
}