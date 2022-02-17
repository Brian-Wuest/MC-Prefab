package com.wuest.prefab;

import com.wuest.prefab.blocks.*;
import com.wuest.prefab.items.*;
import com.wuest.prefab.proxy.messages.ConfigSyncMessage;
import com.wuest.prefab.proxy.messages.PlayerEntityTagMessage;
import com.wuest.prefab.proxy.messages.handlers.ConfigSyncHandler;
import com.wuest.prefab.proxy.messages.handlers.PlayerEntityHandler;
import com.wuest.prefab.structures.config.BasicStructureConfiguration;
import com.wuest.prefab.structures.items.*;
import com.wuest.prefab.structures.messages.StructureHandler;
import com.wuest.prefab.structures.messages.StructureTagMessage;
import com.wuest.prefab.tileEntities.TileEntityDrafter;
import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Objects;

/**
 * This is the mod registry so there is a way to get to all instances of the blocks/items created by this mod.
 *
 * @author WuestMan
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class ModRegistry {

    /**
     * The identifier for the Drafter GUI.
     */
    public static final int GuiDrafter = 12;

    /**
     * The ArrayList of mod registered items.
     */
    public static ArrayList<Item> ModItems = new ArrayList<>();
    /**
     * The ArrayList of mod registered blocks.
     */
    public static ArrayList<Block> ModBlocks = new ArrayList<>();
    /**
     * The hashmap of mod guis.
     */
    public static HashMap<Integer, Class> ModGuis = new HashMap<>();

    public static final CreativeTabs PREFAB_GROUP = new CreativeTabs("prefab.logo") {
        @SideOnly(Side.CLIENT)
        public ItemStack createIcon() {
            return new ItemStack(ModRegistry.ItemLogo);
        }
    };

    /* *********************************** Blocks *********************************** */
    public static BlockCompressedStone CompressedStoneBlock;
    public static BlockCompressedStone DoubleCompressedStoneBlock;
    public static BlockCompressedStone TripleCompressedStoneBlock;
    public static BlockCompressedStone CompressedGlowstoneBlock;
    public static BlockCompressedStone DoubleCompressedGlowstoneBlock;
    public static BlockCompressedStone CompressedDirtBlock;
    public static BlockCompressedStone DoubleCompressedDirtBlock;
    public static BlockCompressedObsidian CompressedObsidianBlock;
    public static BlockCompressedObsidian DoubleCompressedObsidianBlock;
    public static BlockDrafter Drafter;
    public static BlockPaperLantern PaperLantern;
    public static BlockPhasing PhasingBlock;
    public static BlockGlassStairs GlassStairs;
    public static BlockAndesiteStairs AndesiteStairs;
    public static BlockDioriteStairs DioriteStairs;
    public static BlockGraniteStairs GraniteStairs;
    public static BlockHalfGlassSlab GlassSlab;
    public static BlockHalfAndesiteSlab AndesiteSlab;
    public static BlockHalfDioriteSlab DioriteSlab;
    public static BlockHalfGraniteSlab GraniteSlab;
    public static BlockBoundary BoundaryBlock;

    /* *********************************** Item Blocks *********************************** */
    public static ItemBlock CompressedStoneItem;
    public static ItemBlock DoubleCompressedStoneItem;
    public static ItemBlock TripleCompressedStoneItem;
    public static ItemBlock CompressedGlowstoneItem;
    public static ItemBlock DoubleCompressedGlowstoneItem;
    public static ItemBlock CompressedDirtItem;
    public static ItemBlock DoubleCompressedDirtItem;
    public static ItemBlock CompressedObsidianItem;
    public static ItemBlock DoubleCompressedObsidianItem;
    public static ItemBlock PaperLanternItem;
    public static ItemBlock PhasicBlockItem;
    public static ItemBlock BoundaryBlockItem;
    public static ItemBlock GlassStairsItem;
    public static ItemBlock AndesiteStairsItem;
    public static ItemBlock DioriteStairsItem;
    public static ItemBlock GraniteStairsItem;
    public static ItemBlockGlassSlab GlassSlabItem;
    public static ItemBlockAndesiteSlab AndesiteSlabItem;
    public static ItemBlockDioriteSlab DioriteSlabItem;
    public static ItemBlockGraniteSlab GraniteSlabItem;
    public static ItemBlock DrafterItem;

    /* *********************************** Items *********************************** */
    public static ItemCompressedChest CompressedChestItem;
    public static ItemPileOfBricks PileOfBricks;
    public static ItemPalletOfBricks PalletOfBricks;
    public static ItemStringOfLanterns StringOfLanterns;
    public static ItemCoilOfLanterns CoilOfLanterns;

    public static Item ItemLogo;

    /* *********************************** Blueprint Items *********************************** */

    public static ItemStartHouse StartHouse;
    public static ItemWarehouseUpgrade WareHouseUpgrade;
    public static ItemBundleOfTimber BundleOfTimber;
    public static ItemModularHouse ModularHouse;
    public static ItemInstantBridge InstantBridge;
    public static ItemStructurePart StructurePart;
    public static ItemModerateHouse ModerateHouse;
    public static ItemBulldozer Bulldozer;
    public static ItemBulldozer Creative_Bulldozer;

    /**
     * Gets the gui screen for the ID and passes position data to it.
     *
     * @param id The ID of the screen to get.
     * @param x  The X-Axis of where this screen was created from, this is used to create a BlockPos.
     * @param y  The Y-Axis of where this screen was created from, this is used to create a BlockPos.
     * @param z  The Z-Axis of where this screen was created from, this is used to create a BlockPos.
     * @return Null if the screen wasn't found, otherwise the screen found.
     */
    public static GuiScreen GetModGuiByID(int id, int x, int y, int z) {
        for (Entry<Integer, Class> entry : ModRegistry.ModGuis.entrySet()) {
            if (entry.getKey() == id) {
                try {
                    return (GuiScreen) entry.getValue().getConstructor(int.class, int.class, int.class).newInstance(x, y, z);
                } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }

    /**
     * This is where all in-game mod components (Items, Blocks) will be registered.
     */
    public static void RegisterModComponents() {
        /* *********************************** Blocks *********************************** */
        ModRegistry.CompressedStoneBlock = ModRegistry.registerBlock(new BlockCompressedStone(BlockCompressedStone.EnumType.COMPRESSED_STONE));
        ModRegistry.DoubleCompressedStoneBlock = ModRegistry.registerBlock(new BlockCompressedStone(BlockCompressedStone.EnumType.DOUBLE_COMPRESSED_STONE));
        ModRegistry.TripleCompressedStoneBlock = ModRegistry.registerBlock(new BlockCompressedStone(BlockCompressedStone.EnumType.TRIPLE_COMPRESSED_STONE));
        ModRegistry.CompressedGlowstoneBlock = ModRegistry.registerBlock(new BlockCompressedStone(BlockCompressedStone.EnumType.COMPRESSED_GLOWSTONE));
        ModRegistry.DoubleCompressedGlowstoneBlock = ModRegistry.registerBlock(new BlockCompressedStone(BlockCompressedStone.EnumType.DOUBLE_COMPRESSED_GLOWSTONE));
        ModRegistry.CompressedDirtBlock = ModRegistry.registerBlock(new BlockCompressedStone(BlockCompressedStone.EnumType.COMPRESSED_DIRT));
        ModRegistry.DoubleCompressedDirtBlock = ModRegistry.registerBlock(new BlockCompressedStone(BlockCompressedStone.EnumType.DOUBLE_COMPRESSED_DIRT));
        ModRegistry.CompressedObsidianBlock = ModRegistry.registerBlock(new BlockCompressedObsidian(BlockCompressedObsidian.EnumType.COMPRESSED_OBSIDIAN));
        ModRegistry.DoubleCompressedObsidianBlock = ModRegistry.registerBlock(new BlockCompressedObsidian(BlockCompressedObsidian.EnumType.DOUBLE_COMPRESSED_OBSIDIAN));
        ModRegistry.PaperLantern = ModRegistry.registerBlock(new BlockPaperLantern("block_paper_lantern"));
        ModRegistry.PhasingBlock = ModRegistry.registerBlock(new BlockPhasing("block_phasing"));
        ModRegistry.BoundaryBlock = ModRegistry.registerBlock(new BlockBoundary("block_boundary"));
        ModRegistry.GlassStairs = ModRegistry.registerBlock(new BlockGlassStairs("block_glass_stairs"));
        ModRegistry.AndesiteStairs = ModRegistry.registerBlock(new BlockAndesiteStairs("block_andesite_stairs"));
        ModRegistry.DioriteStairs = ModRegistry.registerBlock(new BlockDioriteStairs("block_diorite_stairs"));
        ModRegistry.GraniteStairs = ModRegistry.registerBlock(new BlockGraniteStairs("block_granite_stairs"));
        ////ModRegistry.Drafter = ModRegistry.registerBlock(new BlockDrafter());

        /* *********************************** Item Blocks *********************************** */
        ModRegistry.CompressedStoneItem = ModRegistry.setItemBlockName(ModRegistry.registerItem(new ItemBlock(ModRegistry.CompressedStoneBlock)), BlockCompressedStone.EnumType.COMPRESSED_STONE.getUnlocalizedName());
        ModRegistry.DoubleCompressedStoneItem = ModRegistry.setItemBlockName(ModRegistry.registerItem(new ItemBlock(ModRegistry.DoubleCompressedStoneBlock)), BlockCompressedStone.EnumType.DOUBLE_COMPRESSED_STONE.getUnlocalizedName());
        ModRegistry.TripleCompressedStoneItem = ModRegistry.setItemBlockName(ModRegistry.registerItem(new ItemBlock(ModRegistry.TripleCompressedStoneBlock)), BlockCompressedStone.EnumType.TRIPLE_COMPRESSED_STONE.getUnlocalizedName());
        ModRegistry.CompressedGlowstoneItem = ModRegistry.setItemBlockName(ModRegistry.registerItem(new ItemBlock(ModRegistry.CompressedGlowstoneBlock)), BlockCompressedStone.EnumType.COMPRESSED_GLOWSTONE.getUnlocalizedName());
        ModRegistry.DoubleCompressedGlowstoneItem = ModRegistry.setItemBlockName(ModRegistry.registerItem(new ItemBlock(ModRegistry.DoubleCompressedGlowstoneBlock)), BlockCompressedStone.EnumType.DOUBLE_COMPRESSED_GLOWSTONE.getUnlocalizedName());
        ModRegistry.CompressedDirtItem = ModRegistry.setItemBlockName(ModRegistry.registerItem(new ItemBlock(ModRegistry.CompressedDirtBlock)), BlockCompressedStone.EnumType.COMPRESSED_DIRT.getUnlocalizedName());
        ModRegistry.DoubleCompressedDirtItem = ModRegistry.setItemBlockName(ModRegistry.registerItem(new ItemBlock(ModRegistry.DoubleCompressedDirtBlock)), BlockCompressedStone.EnumType.DOUBLE_COMPRESSED_DIRT.getUnlocalizedName());
        ModRegistry.CompressedObsidianItem = ModRegistry.setItemBlockName(ModRegistry.registerItem(new ItemBlock(ModRegistry.CompressedObsidianBlock)), BlockCompressedObsidian.EnumType.COMPRESSED_OBSIDIAN.getUnlocalizedName());
        ModRegistry.DoubleCompressedObsidianItem = ModRegistry.setItemBlockName(ModRegistry.registerItem(new ItemBlock(ModRegistry.DoubleCompressedObsidianBlock)), BlockCompressedObsidian.EnumType.DOUBLE_COMPRESSED_OBSIDIAN.getUnlocalizedName());
        ModRegistry.PaperLanternItem = ModRegistry.setItemBlockName(ModRegistry.registerItem(new ItemBlock(ModRegistry.PaperLantern)), "block_paper_lantern");
        ModRegistry.PhasicBlockItem = ModRegistry.setItemBlockName(ModRegistry.registerItem(new ItemBlock(ModRegistry.PhasingBlock)), "block_phasing");
        ModRegistry.BoundaryBlockItem = ModRegistry.setItemBlockName(ModRegistry.registerItem(new ItemBlock(ModRegistry.BoundaryBlock)), "block_boundary");
        ModRegistry.GlassStairsItem = ModRegistry.setItemBlockName(ModRegistry.registerItem(new ItemBlock(ModRegistry.GlassStairs)), "block_glass_stairs");
        ModRegistry.AndesiteStairsItem = ModRegistry.setItemBlockName(ModRegistry.registerItem(new ItemBlock(ModRegistry.AndesiteStairs)), "block_andesite_stairs");
        ModRegistry.DioriteStairsItem = ModRegistry.setItemBlockName(ModRegistry.registerItem(new ItemBlock(ModRegistry.DioriteStairs)), "block_diorite_stairs");
        ModRegistry.GraniteStairsItem = ModRegistry.setItemBlockName(ModRegistry.registerItem(new ItemBlock(ModRegistry.GraniteStairs)), "block_granite_stairs");
        ////ModRegistry.DrafterItem = ModRegistry.setItemBlockName(ModRegistry.registerItem(new ItemBlock(ModRegistry.Drafter)), "block_drafter");

        /* *********************************** Items *********************************** */
        ModRegistry.ItemLogo = ModRegistry.setItemName(ModRegistry.registerItem(new Item()), "item_logo");
        ModRegistry.ItemLogo.setCreativeTab(null);

        /* *********************************** Blueprint Items *********************************** */
        ModRegistry.StartHouse = ModRegistry.registerItem(new ItemStartHouse("item_start_house"));
        ModRegistry.CompressedChestItem = ModRegistry.registerItem(new ItemCompressedChest("item_compressed_chest"));
        ModRegistry.PileOfBricks = ModRegistry.registerItem(new ItemPileOfBricks("item_pile_of_bricks"));
        ModRegistry.PalletOfBricks = ModRegistry.registerItem(new ItemPalletOfBricks("item_pallet_of_bricks"));
        ModRegistry.WareHouseUpgrade = ModRegistry.registerItem(new ItemWarehouseUpgrade("item_warehouse_upgrade"));
        ModRegistry.BundleOfTimber = ModRegistry.registerItem(new ItemBundleOfTimber("item_bundle_of_timber"));
        ModRegistry.InstantBridge = ModRegistry.registerItem(new ItemInstantBridge("item_instant_bridge"));
        ModRegistry.StringOfLanterns = ModRegistry.registerItem(new ItemStringOfLanterns("item_string_of_lanterns"));
        ModRegistry.CoilOfLanterns = ModRegistry.registerItem(new ItemCoilOfLanterns("item_coil_of_lanterns"));
        ModRegistry.ModerateHouse = ModRegistry.registerItem(new ItemModerateHouse("item_moderate_house"));
        ModRegistry.Bulldozer = ModRegistry.registerItem(new ItemBulldozer("item_bulldozer", false));
        ModRegistry.Creative_Bulldozer = ModRegistry.registerItem(new ItemBulldozer("item_creative_bulldozer", true));
        ModRegistry.StructurePart = ModRegistry.registerItem(new ItemStructurePart("item_structure_part"));
        ////ModRegistry.ModularHouse = ModRegistry.registerItem(new ItemModularHouse("item_modular_house"));
        ModRegistry.registerItem(new ItemBasicStructure(BasicStructureConfiguration.EnumBasicStructureName.AdvancedCoop.getItemTextureLocation().getPath(), BasicStructureConfiguration.EnumBasicStructureName.AdvancedCoop));
        ModRegistry.registerItem(new ItemBasicStructure(BasicStructureConfiguration.EnumBasicStructureName.AdvancedHorseStable.getItemTextureLocation().getPath(), BasicStructureConfiguration.EnumBasicStructureName.AdvancedHorseStable));
        ModRegistry.registerItem(new ItemBasicStructure(BasicStructureConfiguration.EnumBasicStructureName.Barn.getItemTextureLocation().getPath(), BasicStructureConfiguration.EnumBasicStructureName.Barn));
        ModRegistry.registerItem(new ItemBasicStructure(BasicStructureConfiguration.EnumBasicStructureName.MachineryTower.getItemTextureLocation().getPath(), BasicStructureConfiguration.EnumBasicStructureName.MachineryTower));
        ModRegistry.registerItem(new ItemBasicStructure(BasicStructureConfiguration.EnumBasicStructureName.DefenseBunker.getItemTextureLocation().getPath(), BasicStructureConfiguration.EnumBasicStructureName.DefenseBunker));
        ModRegistry.registerItem(new ItemBasicStructure(BasicStructureConfiguration.EnumBasicStructureName.MineshaftEntrance.getItemTextureLocation().getPath(), BasicStructureConfiguration.EnumBasicStructureName.MineshaftEntrance));
        ModRegistry.registerItem(new ItemBasicStructure(BasicStructureConfiguration.EnumBasicStructureName.EnderGateway.getItemTextureLocation().getPath(), BasicStructureConfiguration.EnumBasicStructureName.EnderGateway));
        ModRegistry.registerItem(new ItemBasicStructure(BasicStructureConfiguration.EnumBasicStructureName.AquaBase.getItemTextureLocation().getPath(), BasicStructureConfiguration.EnumBasicStructureName.AquaBase));
        ModRegistry.registerItem(new ItemBasicStructure(BasicStructureConfiguration.EnumBasicStructureName.GrassyPlain.getItemTextureLocation().getPath(), BasicStructureConfiguration.EnumBasicStructureName.GrassyPlain));
        ModRegistry.registerItem(new ItemBasicStructure(BasicStructureConfiguration.EnumBasicStructureName.MagicTemple.getItemTextureLocation().getPath(), BasicStructureConfiguration.EnumBasicStructureName.MagicTemple));
        ModRegistry.registerItem(new ItemBasicStructure(BasicStructureConfiguration.EnumBasicStructureName.GreenHouse.getItemTextureLocation().getPath(), BasicStructureConfiguration.EnumBasicStructureName.GreenHouse));
        ModRegistry.registerItem(new ItemBasicStructure(BasicStructureConfiguration.EnumBasicStructureName.WatchTower.getItemTextureLocation().getPath(), BasicStructureConfiguration.EnumBasicStructureName.WatchTower));
        ModRegistry.registerItem(new ItemBasicStructure(BasicStructureConfiguration.EnumBasicStructureName.WelcomeCenter.getItemTextureLocation().getPath(), BasicStructureConfiguration.EnumBasicStructureName.WelcomeCenter));
        ModRegistry.registerItem(new ItemBasicStructure(BasicStructureConfiguration.EnumBasicStructureName.Jail.getItemTextureLocation().getPath(), BasicStructureConfiguration.EnumBasicStructureName.Jail));
        ModRegistry.registerItem(new ItemBasicStructure(BasicStructureConfiguration.EnumBasicStructureName.Saloon.getItemTextureLocation().getPath(), BasicStructureConfiguration.EnumBasicStructureName.Saloon));
        ModRegistry.registerItem(new ItemBasicStructure(BasicStructureConfiguration.EnumBasicStructureName.NetherGate.getItemTextureLocation().getPath(), BasicStructureConfiguration.EnumBasicStructureName.NetherGate));

        ModRegistry.registerItem(new ItemBasicStructure(BasicStructureConfiguration.EnumBasicStructureName.Warehouse.getItemTextureLocation().getPath(), BasicStructureConfiguration.EnumBasicStructureName.Warehouse));
        ModRegistry.registerItem(new ItemBasicStructure(BasicStructureConfiguration.EnumBasicStructureName.FishPond.getItemTextureLocation().getPath(), BasicStructureConfiguration.EnumBasicStructureName.FishPond));
        ModRegistry.registerItem(new ItemBasicStructure(BasicStructureConfiguration.EnumBasicStructureName.AdvancedWarehouse.getItemTextureLocation().getPath(), BasicStructureConfiguration.EnumBasicStructureName.AdvancedWarehouse));
        ModRegistry.registerItem(new ItemBasicStructure(BasicStructureConfiguration.EnumBasicStructureName.MonsterMasher.getItemTextureLocation().getPath(), BasicStructureConfiguration.EnumBasicStructureName.MonsterMasher));
        ModRegistry.registerItem(new ItemBasicStructure(BasicStructureConfiguration.EnumBasicStructureName.HorseStable.getItemTextureLocation().getPath(), BasicStructureConfiguration.EnumBasicStructureName.HorseStable));
        ModRegistry.registerItem(new ItemBasicStructure(BasicStructureConfiguration.EnumBasicStructureName.VillagerHouses.getItemTextureLocation().getPath(), BasicStructureConfiguration.EnumBasicStructureName.VillagerHouses));
        ModRegistry.registerItem(new ItemBasicStructure(BasicStructureConfiguration.EnumBasicStructureName.ChickenCoop.getItemTextureLocation().getPath(), BasicStructureConfiguration.EnumBasicStructureName.ChickenCoop));
        ModRegistry.registerItem(new ItemBasicStructure(BasicStructureConfiguration.EnumBasicStructureName.ProduceFarm.getItemTextureLocation().getPath(), BasicStructureConfiguration.EnumBasicStructureName.ProduceFarm));
        ModRegistry.registerItem(new ItemBasicStructure(BasicStructureConfiguration.EnumBasicStructureName.TreeFarm.getItemTextureLocation().getPath(), BasicStructureConfiguration.EnumBasicStructureName.TreeFarm));

        /* *********************************** Slabs *********************************** */

        // Glass Slab.
        ModRegistry.GlassSlab = ModRegistry.registerBlock(new BlockHalfGlassSlab());
        BlockDoubleGlassSlab registeredDoubleGlassSlab = new BlockDoubleGlassSlab();
        ModRegistry.GlassSlabItem = ModRegistry.registerItem(new ItemBlockGlassSlab(ModRegistry.GlassSlab, ModRegistry.GlassSlab, registeredDoubleGlassSlab, true));
        ModRegistry.GlassSlabItem.setRegistryName("block_half_glass_slab");
        ModRegistry.registerBlock(registeredDoubleGlassSlab);

        // Andesite slab.
        ModRegistry.AndesiteSlab = ModRegistry.registerBlock(new BlockHalfAndesiteSlab());
        BlockDoubleAndesiteSlab registeredDoubleAndesiteSlab = new BlockDoubleAndesiteSlab();
        ModRegistry.AndesiteSlabItem = ModRegistry.registerItem(new ItemBlockAndesiteSlab(ModRegistry.AndesiteSlab, ModRegistry.AndesiteSlab, registeredDoubleAndesiteSlab, true));
        ModRegistry.AndesiteSlabItem.setRegistryName("block_half_andesite_slab");
        ModRegistry.registerBlock(registeredDoubleAndesiteSlab);

        // Diorite slab.
        ModRegistry.DioriteSlab = ModRegistry.registerBlock(new BlockHalfDioriteSlab());
        BlockDoubleDioriteSlab registeredDoubleDioriteSlab = new BlockDoubleDioriteSlab();
        ModRegistry.DioriteSlabItem = ModRegistry.registerItem(new ItemBlockDioriteSlab(ModRegistry.DioriteSlab, ModRegistry.DioriteSlab, registeredDoubleDioriteSlab, true));
        ModRegistry.DioriteSlabItem.setRegistryName("block_half_diorite_slab");
        ModRegistry.registerBlock(registeredDoubleDioriteSlab);

        // Granite slab.
        ModRegistry.GraniteSlab = ModRegistry.registerBlock(new BlockHalfGraniteSlab());
        BlockDoubleGraniteSlab registeredDoubleGraniteSlab = new BlockDoubleGraniteSlab();
        ModRegistry.GraniteSlabItem = ModRegistry.registerItem(new ItemBlockGraniteSlab(ModRegistry.GraniteSlab, ModRegistry.GraniteSlab, registeredDoubleGraniteSlab, true));
        ModRegistry.GraniteSlabItem.setRegistryName("block_half_granite_slab");
        ModRegistry.registerBlock(registeredDoubleGraniteSlab);

        Blocks.STRUCTURE_BLOCK.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);

        /* *********************************** Tile Entities *********************************** */
        ////GameRegistry.registerTileEntity(TileEntityDrafter.class, "Drafter");
    }

    /**
     * Registers records into the ore dictionary.
     */
    public static void RegisterOreDictionaryRecords() {
        // Register certain blocks into the ore dictionary.
        OreDictionary.registerOre("compressedDirt1", ModRegistry.CompressedDirtBlock);
        OreDictionary.registerOre("compressedDirt2", ModRegistry.DoubleCompressedDirtBlock);
        OreDictionary.registerOre("compressedStone1", ModRegistry.CompressedStoneBlock);
        OreDictionary.registerOre("compressedStone2", ModRegistry.DoubleCompressedStoneBlock);
        OreDictionary.registerOre("compressedStone3", ModRegistry.TripleCompressedStoneBlock);
        OreDictionary.registerOre("compressedGlowstone1", ModRegistry.CompressedGlowstoneBlock);
        OreDictionary.registerOre("compressedGlowstone2", ModRegistry.DoubleCompressedGlowstoneBlock);
        OreDictionary.registerOre("compressedObsidian1", ModRegistry.CompressedObsidianBlock);
        OreDictionary.registerOre("compressedObsidian2", ModRegistry.DoubleCompressedObsidianBlock);

        OreDictionary.registerOre("compressed_lights", ModRegistry.CompressedGlowstoneItem);
        OreDictionary.registerOre("compressed_lights", ModRegistry.StringOfLanterns);

        OreDictionary.registerOre("double_compressed_lights", ModRegistry.DoubleCompressedGlowstoneItem);
        OreDictionary.registerOre("double_compressed_lights", ModRegistry.CoilOfLanterns);
    }

    /**
     * This is where the mod messages are registered.
     */
    public static void RegisterMessages() {
        Prefab.network.registerMessage(ConfigSyncHandler.class, ConfigSyncMessage.class, 1, Side.CLIENT);

        Prefab.network.registerMessage(StructureHandler.class, StructureTagMessage.class, 2, Side.SERVER);

        Prefab.network.registerMessage(PlayerEntityHandler.class, PlayerEntityTagMessage.class, 3, Side.CLIENT);
    }

    /**
     * This is where mod capabilities are registered.
     */
    public static void RegisterCapabilities() {
    }

    /**
     * Register data fixers for updated blocks.
     */
    public static void RegisterFixers() {
    }

    /**
     * Register an Item
     *
     * @param <T>  The Item type
     * @param item The Item instance
     */
    public static <T extends Item> T registerItem(T item) {
        ModRegistry.ModItems.add(item);

        item.setCreativeTab(ModRegistry.PREFAB_GROUP);

        return item;
    }

    /**
     * Registers a block in the game registry.
     *
     * @param <T>   The type of block to register.
     * @param block The block to register.
     */
    public static <T extends Block> T registerBlock(T block) {
        ModRegistry.ModBlocks.add(block);

        return block;
    }

    /**
     * Set the registry name of {@code item} to {@code itemName} and the un-localised name to the full registry name.
     *
     * @param item     The item
     * @param itemName The item's name
     */
    public static Item setItemName(Item item, String itemName) {
        if (itemName != null) {
            item.setRegistryName(itemName);
            item.setTranslationKey(Objects.requireNonNull(item.getRegistryName()).toString());
        }

        return item;
    }

    /**
     * Set the registry name of {@code item} to {@code itemName} and the un-localised name to the full registry name.
     *
     * @param item     The item
     * @param itemName The item's name
     */
    public static ItemBlock setItemBlockName(ItemBlock item, String itemName) {
        if (itemName != null) {
            item.setRegistryName(itemName);
            item.setTranslationKey(Objects.requireNonNull(item.getRegistryName()).toString());
        }

        return item;
    }

    /**
     * Set the registry name of {@code block} to {@code blockName} and the un-localised name to the full registry name.
     *
     * @param block     The block
     * @param blockName The block's name
     */
    public static void setBlockName(Block block, String blockName) {
        block.setRegistryName(blockName);
        block.setTranslationKey(Objects.requireNonNull(block.getRegistryName()).toString());
    }
}