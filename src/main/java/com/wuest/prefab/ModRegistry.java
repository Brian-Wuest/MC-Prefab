package com.wuest.prefab;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.*;

import com.wuest.prefab.Items.*;
import com.wuest.prefab.Blocks.*;
import com.wuest.prefab.Capabilities.*;
import com.wuest.prefab.Capabilities.Storage.StructureConfigurationStorage;
import com.wuest.prefab.Config.BasicStructureConfiguration.EnumBasicStructureName;
import com.wuest.prefab.Gui.*;
import com.wuest.prefab.Proxy.CommonProxy;
import com.wuest.prefab.Proxy.Messages.*;
import com.wuest.prefab.Proxy.Messages.Handlers.*;
import com.wuest.prefab.Render.PrefabModelMesher;
import com.wuest.prefab.TileEntities.TileEntityDrafter;

import net.minecraft.block.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.init.*;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.oredict.*;

/**
 * This is the mod registry so there is a way to get to all instances of the blocks/items created by this mod.
 * @author WuestMan
 *
 */
public class ModRegistry
{
	public static ArrayList<Item> ModItems = new ArrayList<Item>();
	public static ArrayList<Block> ModBlocks = new ArrayList<Block>();
	public static HashMap<Integer, Class> ModGuis = new HashMap<Integer, Class>();
	public static final int GuiWareHouse = 1;
	public static final int GuiChickenCoop = 2;
	public static final int GuiProduceFarm = 3;
	public static final int GuiTreeFarm = 4;
	public static final int GuiFishPond = 5;
	public static final int GuiStartHouseChooser = 6;
	public static final int GuiAdvancedWareHouse = 7;
	public static final int GuiMonsterMasher = 8;
	public static final int GuiHorseStable = 9;
	public static final int GuiNetherGate = 10;
	public static final int GuiModularHouse = 11;
	public static final int GuiDrafter = 12;
	public static final int GuiBasicStructure = 13;
	
	/**
	 * This capability is used to save the locations where a player spawns when transferring dimensions.
	 */
	@CapabilityInject(IStructureConfigurationCapability.class)
	public static Capability<IStructureConfigurationCapability> StructureConfiguration = null;
	
	public static ItemStartHouse StartHouse()
	{
		return ModRegistry.GetItem(ItemStartHouse.class);
	}
	
	public static ItemWareHouse WareHouse()
	{
		return ModRegistry.GetItem(ItemWareHouse.class);
	}
	
	public static ItemAdvancedWareHouse AdvancedWareHouse()
	{
		return ModRegistry.GetItem(ItemAdvancedWareHouse.class);
	}
	
	public static ItemChickenCoop ChickenCoop()
	{
		return ModRegistry.GetItem(ItemChickenCoop.class);
	}
	
	public static ItemBlockMeta CompressedStoneItem()
	{
		return ModRegistry.GetItem(ItemBlockMeta.class);
	}
	
	public static BlockCompressedStone CompressedStoneBlock()
	{
		return ModRegistry.GetBlock(BlockCompressedStone.class);
	}
	
	public static ItemCompressedChest CompressedChestItem() 
	{
		return ModRegistry.GetItem(ItemCompressedChest.class);
	}
	
	public static ItemProduceFarm ProduceFarm() 
	{
		return ModRegistry.GetItem(ItemProduceFarm.class);
	}
	
	public static ItemTreeFarm TreeFarm() 
	{
		return ModRegistry.GetItem(ItemTreeFarm.class);
	}
	
	public static ItemFishPond FishPond()
	{
		return ModRegistry.GetItem(ItemFishPond.class);
	}
	
	public static ItemPileOfBricks PileOfBricks() 
	{
		return ModRegistry.GetItem(ItemPileOfBricks.class);
	}
	
	public static ItemPalletOfBricks PalletOfBricks()
	{
		return ModRegistry.GetItem(ItemPalletOfBricks.class);
	}
	
	public static ItemMonsterMasher MonsterMasher()
	{
		return ModRegistry.GetItem(ItemMonsterMasher.class);
	}
	
	public static ItemWarehouseUpgrade WareHouseUpgrade()
	{
		return ModRegistry.GetItem(ItemWarehouseUpgrade.class);
	}
	
	public static ItemBundleOfTimber BundleOfTimber()
	{
		return ModRegistry.GetItem(ItemBundleOfTimber.class);
	}
	
	public static ItemHorseStable HorseStable()
	{
		return ModRegistry.GetItem(ItemHorseStable.class);
	}
	
	public static ItemNetherGate NetherGate()
	{
		return ModRegistry.GetItem(ItemNetherGate.class);
	}
	
	public static ItemModularHouse ModularHouse()
	{
		return ModRegistry.GetItem(ItemModularHouse.class);
	}
	
	public static BlockDrafter Drafter()
	{
		return ModRegistry.GetBlock(BlockDrafter.class);
	}
	
	public static ItemBasicStructure BasicStructure()
	{
		return ModRegistry.GetItem(ItemBasicStructure.class);
	}
	
	public static ItemStack GetCompressedStoneType(BlockCompressedStone.EnumType enumType)
	{
		return ModRegistry.GetCompressedStoneType(enumType, 1);
	}
	
	public static ItemStack GetCompressedStoneType(BlockCompressedStone.EnumType enumType, int count)
	{
		return new ItemStack(Item.getItemFromBlock(ModRegistry.CompressedStoneBlock()), count, enumType.getMetadata());
	}
	
	/**
	 * Gets the item from the ModItems collections.
	 * @param genericClass The class of item to get from the collection.
	 * @return Null if the item could not be found otherwise the item found.
	 */
	public static <T extends Item> T GetItem(Class<T> genericClass)
	{
		for (Item entry : ModRegistry.ModItems)
		{
			if (entry.getClass() == genericClass)
			{
				return (T)entry;
			}
		}
		
		return null;
	}
	
	/**
	 * Gets the block from the ModBlockss collections.
	 * @param genericClass The class of block to get from the collection.
	 * @return Null if the block could not be found otherwise the block found.
	 */
	public static <T extends Block> T GetBlock(Class<T> genericClass)
	{
		for (Block entry : ModRegistry.ModBlocks)
		{
			if (entry.getClass() == genericClass)
			{
				return (T)entry;
			}
		}
		
		return null;
	}
	
	public static GuiScreen GetModGuiByID(int id, int x, int y, int z)
	{
		for (Entry<Integer, Class> entry : ModRegistry.ModGuis.entrySet())
		{
			if (entry.getKey() == id)
			{
				try
				{
					return (GuiScreen)entry.getValue().getConstructor(int.class, int.class, int.class).newInstance(x, y, z);
				}
				catch (InstantiationException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				catch (IllegalAccessException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				catch (IllegalArgumentException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				catch (InvocationTargetException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				catch (NoSuchMethodException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				catch (SecurityException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		return null;
	}
	
	/**
	 * This is where all in-game mod components (Items, Blocks) will be registered.
	 */
	public static void RegisterModComponents()
	{
		ModRegistry.registerItem(new ItemStartHouse("itemStartHouse"));
		ModRegistry.registerItem(new ItemWareHouse("itemWareHouse"));
		ModRegistry.registerItem(new ItemChickenCoop("itemChickenCoop"));
		ModRegistry.registerItem(new ItemProduceFarm("itemProduceFarm"));
		ModRegistry.registerItem(new ItemTreeFarm("itemTreeFarm"));
		ModRegistry.registerItem(new ItemCompressedChest("itemCompressedChest"));
		ModRegistry.registerItem(new ItemPileOfBricks("itemPileOfBricks"));
		ModRegistry.registerItem(new ItemPalletOfBricks("itemPalletOfBricks"));
		ModRegistry.registerItem(new ItemFishPond("itemFishPond"));
		ModRegistry.registerItem(new ItemAdvancedWareHouse("itemAdvancedWareHouse"));
		ModRegistry.registerItem(new ItemMonsterMasher("itemMonsterMasher"));
		ModRegistry.registerItem(new ItemWarehouseUpgrade("itemWareHouseUpgrade"));
		ModRegistry.registerItem(new ItemBundleOfTimber("itemBundleOfTimber"));
		ModRegistry.registerItem(new ItemHorseStable("itemHorseStable"));
		ModRegistry.registerItem(new ItemNetherGate("itemNetherGate"));
		//ModRegistry.registerItem(new ItemModularHouse("itemModularHouse"));
		
		// Register all the basic structures here. The resource location is used for the item models and textures.
		// Only the first one in this list should have the last variable set to true.
		ModRegistry.registerItem(new ItemBasicStructure("item_basic_structure"));
		
		// Create/register the item block with this block as it's needed due to this being a meta data block.
		BlockCompressedStone stone = new BlockCompressedStone();
		ItemBlockMeta meta = new ItemBlockMeta(stone);
		ModRegistry.setItemName(meta, "blockCompressedStone");
		ModRegistry.registerBlock(stone, meta);
		
		//BlockDrafter drafter = new BlockDrafter();
		//ModRegistry.registerBlock(drafter);
		//GameRegistry.registerTileEntity(TileEntityDrafter.class, "Drafter");
	}
	
	/**
	 * This is where the mod recipes are registered.
	 */
	public static void RegisterRecipes()
	{
		// Compressed Stone.
		GameRegistry.addRecipe(ModRegistry.GetCompressedStoneType(BlockCompressedStone.EnumType.COMPRESSED_STONE),
				"xxx",
				"xxx",
				"xxx",
				'x', Item.getItemFromBlock(Blocks.STONE));
		
		GameRegistry.addRecipe(new ItemStack(Item.getItemFromBlock(Blocks.STONE), 9), 
				"x",
				'x', ModRegistry.GetCompressedStoneType(BlockCompressedStone.EnumType.COMPRESSED_STONE));
		
		// Double Compressed Stone.
		GameRegistry.addRecipe(ModRegistry.GetCompressedStoneType(BlockCompressedStone.EnumType.DOUBLE_COMPRESSED_STONE),
				"xxx",
				"xxx",
				"xxx",
				'x', ModRegistry.GetCompressedStoneType(BlockCompressedStone.EnumType.COMPRESSED_STONE));
		
		GameRegistry.addRecipe(ModRegistry.GetCompressedStoneType(BlockCompressedStone.EnumType.COMPRESSED_STONE, 9),
				"x",
				'x',ModRegistry.GetCompressedStoneType(BlockCompressedStone.EnumType.DOUBLE_COMPRESSED_STONE));
		
		// Triple Compressed Stone.
		GameRegistry.addRecipe(ModRegistry.GetCompressedStoneType(BlockCompressedStone.EnumType.TRIPLE_COMPRESSED_STONE),
				"xxx",
				"xxx",
				"xxx",
				'x', ModRegistry.GetCompressedStoneType(BlockCompressedStone.EnumType.DOUBLE_COMPRESSED_STONE));
		
		GameRegistry.addRecipe(ModRegistry.GetCompressedStoneType(BlockCompressedStone.EnumType.DOUBLE_COMPRESSED_STONE, 9),
				"x",
				'x', ModRegistry.GetCompressedStoneType(BlockCompressedStone.EnumType.TRIPLE_COMPRESSED_STONE));
		
		// Compressed Glowstone.
		GameRegistry.addRecipe(ModRegistry.GetCompressedStoneType(BlockCompressedStone.EnumType.COMPRESSED_GLOWSTONE),
				"xxx",
				"xxx",
				"xxx",
				'x', Item.getItemFromBlock(Blocks.GLOWSTONE));
		
		GameRegistry.addRecipe(new ItemStack(Item.getItemFromBlock(Blocks.GLOWSTONE), 9),
				"x",
				'x', ModRegistry.GetCompressedStoneType(BlockCompressedStone.EnumType.COMPRESSED_GLOWSTONE));
		
		// Double Compressed Glowstone.
		GameRegistry.addRecipe(ModRegistry.GetCompressedStoneType(BlockCompressedStone.EnumType.DOUBLE_COMPRESSED_GLOWSTONE),
				"xxx",
				"xxx",
				"xxx",
				'x', ModRegistry.GetCompressedStoneType(BlockCompressedStone.EnumType.COMPRESSED_GLOWSTONE));
		
		GameRegistry.addRecipe(ModRegistry.GetCompressedStoneType(BlockCompressedStone.EnumType.COMPRESSED_GLOWSTONE, 9),
				"x",
				'x', ModRegistry.GetCompressedStoneType(BlockCompressedStone.EnumType.DOUBLE_COMPRESSED_GLOWSTONE));
		
		// Compressed Dirt
		GameRegistry.addRecipe(ModRegistry.GetCompressedStoneType(BlockCompressedStone.EnumType.COMPRESSED_DIRT),
				"xxx",
				"xxx",
				"xxx",
				'x', Item.getItemFromBlock(Blocks.DIRT));
		
		GameRegistry.addRecipe(new ItemStack(Item.getItemFromBlock(Blocks.DIRT), 9),
				"x",
				'x', ModRegistry.GetCompressedStoneType(BlockCompressedStone.EnumType.COMPRESSED_DIRT));
		
		// Double Compressed Dirt
		GameRegistry.addRecipe(ModRegistry.GetCompressedStoneType(BlockCompressedStone.EnumType.DOUBLE_COMPRESSED_DIRT),
				"xxx",
				"xxx",
				"xxx",
				'x', ModRegistry.GetCompressedStoneType(BlockCompressedStone.EnumType.COMPRESSED_DIRT));
		
		GameRegistry.addRecipe(ModRegistry.GetCompressedStoneType(BlockCompressedStone.EnumType.COMPRESSED_DIRT, 9),
				"x",
				'x', ModRegistry.GetCompressedStoneType(BlockCompressedStone.EnumType.DOUBLE_COMPRESSED_DIRT));
		
		// Compressed Chest
		GameRegistry.addRecipe(new ItemStack(ModRegistry.CompressedChestItem()),
				"xxx",
				"xyx",
				"xxx",
				'x', Item.getItemFromBlock(Blocks.CHEST),
				'y', Item.getItemFromBlock(Blocks.ENDER_CHEST));
		
		// Pile of Bricks
		GameRegistry.addRecipe(new ItemStack(ModRegistry.PileOfBricks()),
				"xxx",
				"xxx",
				"xxx",
				'x', Items.BRICK);
		
		GameRegistry.addRecipe(new ItemStack(Items.BRICK, 9),
				"x",
				'x', ModRegistry.PileOfBricks());
		
		// Pallet of Bricks
		GameRegistry.addRecipe(new ItemStack(ModRegistry.PalletOfBricks()),
				"xxx",
				"xxx",
				"xxx",
				'x', ModRegistry.PileOfBricks());
		
		GameRegistry.addRecipe(new ItemStack(ModRegistry.PileOfBricks(), 9),
				"x",
				'x', ModRegistry.PalletOfBricks());
		
		// Warehouse
		GameRegistry.addRecipe(new ItemStack(ModRegistry.WareHouse()),
				"x x",
				"xyx",
				"zaz",
				'x', ModRegistry.GetCompressedStoneType(BlockCompressedStone.EnumType.DOUBLE_COMPRESSED_STONE),
				'y', ModRegistry.CompressedChestItem(),
				'z', ModRegistry.GetCompressedStoneType(BlockCompressedStone.EnumType.DOUBLE_COMPRESSED_GLOWSTONE),
				'a', ModRegistry.GetCompressedStoneType(BlockCompressedStone.EnumType.TRIPLE_COMPRESSED_STONE));
		
		// Produce Farm.
		GameRegistry.addRecipe(new ItemStack(ModRegistry.ProduceFarm()),
				"aba",
				"cdc",
				"aba",
				'a', ModRegistry.PalletOfBricks(),
				'b', ModRegistry.GetCompressedStoneType(BlockCompressedStone.EnumType.DOUBLE_COMPRESSED_DIRT),
				'c', Items.WATER_BUCKET,
				'd', ModRegistry.GetCompressedStoneType(BlockCompressedStone.EnumType.DOUBLE_COMPRESSED_GLOWSTONE));
		
		// Tree Farm/Park
		GameRegistry.addRecipe(new ItemStack(ModRegistry.TreeFarm()),
				"aba",
				"cdc",
				"aba",
				'a', ModRegistry.PalletOfBricks(),
				'b', ModRegistry.GetCompressedStoneType(BlockCompressedStone.EnumType.DOUBLE_COMPRESSED_DIRT),
				'c', Items.FLOWER_POT,
				'd', ModRegistry.GetCompressedStoneType(BlockCompressedStone.EnumType.DOUBLE_COMPRESSED_GLOWSTONE));
		
		// Chicken Coop
		GameRegistry.addRecipe(new ItemStack(ModRegistry.ChickenCoop()),
				"eee",
				"aba",
				"cdc",
				'a', new ItemStack(Item.getItemFromBlock(Blocks.LOG), 1, BlockPlanks.EnumType.SPRUCE.getMetadata()),
				'b', Items.EGG,
				'c', ModRegistry.GetCompressedStoneType(BlockCompressedStone.EnumType.COMPRESSED_DIRT),
				'd', Item.getItemFromBlock(Blocks.HAY_BLOCK),
				'e', Item.getItemFromBlock(Blocks.BRICK_BLOCK));
		
		// Fish farm.
		GameRegistry.addRecipe(new ItemStack(ModRegistry.FishPond()),
				"abc",
				"ded",
				"fgf",
				'a', new ItemStack(Item.getItemFromBlock(Blocks.TALLGRASS), 1, 1),
				'b', new ItemStack(Items.REEDS),
				'c', new ItemStack(Item.getItemFromBlock(Blocks.SAPLING), 1, 0),
				'd', new ItemStack(Items.WATER_BUCKET),
				'e', new ItemStack(Items.FISHING_ROD, 1, 0),
				'f', new ItemStack(Item.getItemFromBlock(Blocks.SAND)),
				'g', new ItemStack(Items.FISH, 1, 0));
		
		// Warehouse Upgrade
		GameRegistry.addRecipe(new ItemStack(ModRegistry.WareHouseUpgrade()),
				"aba",
				"cdc",
				"aba",
				'a', Item.getItemFromBlock(Blocks.BOOKSHELF),
				'b', Items.BREWING_STAND,
				'c', Item.getItemFromBlock(Blocks.ENCHANTING_TABLE),
				'd', Item.getItemFromBlock(Blocks.ANVIL));
		
		// Advanced Warehouse
		GameRegistry.addRecipe(new ItemStack(ModRegistry.AdvancedWareHouse()),
				"xbx",
				"xyx",
				"zaz",
				'x', ModRegistry.GetCompressedStoneType(BlockCompressedStone.EnumType.DOUBLE_COMPRESSED_STONE),
				'y', ModRegistry.CompressedChestItem(),
				'z', ModRegistry.GetCompressedStoneType(BlockCompressedStone.EnumType.DOUBLE_COMPRESSED_GLOWSTONE),
				'a', ModRegistry.GetCompressedStoneType(BlockCompressedStone.EnumType.TRIPLE_COMPRESSED_STONE),
				'b', ModRegistry.WareHouseUpgrade());
		
		// Monster Masher.
		GameRegistry.addRecipe(new ItemStack(ModRegistry.MonsterMasher()),
				"abc",
				"ede",
				"fgh",
				'a', new ItemStack(Items.SKULL, 1, 2),
				'b', Item.getItemFromBlock(Blocks.REDSTONE_BLOCK),
				'c', new ItemStack(Items.SKULL, 1, 0),
				'e', Item.getItemFromBlock(Blocks.IRON_BARS),
				'd', ModRegistry.CompressedChestItem(),
				'f', ModRegistry.GetCompressedStoneType(BlockCompressedStone.EnumType.DOUBLE_COMPRESSED_STONE),
				'g', ModRegistry.GetCompressedStoneType(BlockCompressedStone.EnumType.TRIPLE_COMPRESSED_STONE),
				'h', ModRegistry.GetCompressedStoneType(BlockCompressedStone.EnumType.DOUBLE_COMPRESSED_GLOWSTONE));
		
		// Planks to bundles of timber.
		GameRegistry.addRecipe(new ShapedOreRecipe(ModRegistry.BundleOfTimber(), 
				"aaa", 
				"aaa", 
				"aaa", 
				'a', "logWood"));

		// Bundle of timber to oak planks.
		GameRegistry.addRecipe(new ItemStack(Item.getItemFromBlock(Blocks.LOG), 9), 
				"a",
				'a', ModRegistry.BundleOfTimber());
		
		// Horse Stable.
		GameRegistry.addRecipe(new ItemStack(ModRegistry.HorseStable()),
				"aaa",
				"bcb",
				"ded",
				'a', Item.getItemFromBlock(Blocks.BRICK_BLOCK),
				'b', ModRegistry.BundleOfTimber(),
				'c', Item.getItemFromBlock(Blocks.HAY_BLOCK),
				'd', Item.getItemFromBlock(Blocks.SPRUCE_FENCE),
				'e', Item.getItemFromBlock(Blocks.SPRUCE_FENCE_GATE));
		
		GameRegistry.addRecipe(new ItemStack(ModRegistry.HorseStable()),
				"aaa",
				"bcb",
				"ded",
				'a', Item.getItemFromBlock(Blocks.BRICK_BLOCK),
				'b', ModRegistry.BundleOfTimber(),
				'c', Item.getItemFromBlock(Blocks.HAY_BLOCK),
				'd', Item.getItemFromBlock(Blocks.OAK_FENCE),
				'e', Item.getItemFromBlock(Blocks.OAK_FENCE_GATE));
		
		GameRegistry.addRecipe(new ItemStack(ModRegistry.HorseStable()),
				"aaa",
				"bcb",
				"ded",
				'a', Item.getItemFromBlock(Blocks.BRICK_BLOCK),
				'b', ModRegistry.BundleOfTimber(),
				'c', Item.getItemFromBlock(Blocks.HAY_BLOCK),
				'd', Item.getItemFromBlock(Blocks.BIRCH_FENCE),
				'e', Item.getItemFromBlock(Blocks.BIRCH_FENCE_GATE));
		
		GameRegistry.addRecipe(new ItemStack(ModRegistry.HorseStable()),
				"aaa",
				"bcb",
				"ded",
				'a', Item.getItemFromBlock(Blocks.BRICK_BLOCK),
				'b', ModRegistry.BundleOfTimber(),
				'c', Item.getItemFromBlock(Blocks.HAY_BLOCK),
				'd', Item.getItemFromBlock(Blocks.ACACIA_FENCE),
				'e', Item.getItemFromBlock(Blocks.ACACIA_FENCE_GATE));
		
		GameRegistry.addRecipe(new ItemStack(ModRegistry.HorseStable()),
				"aaa",
				"bcb",
				"ded",
				'a', Item.getItemFromBlock(Blocks.BRICK_BLOCK),
				'b', ModRegistry.BundleOfTimber(),
				'c', Item.getItemFromBlock(Blocks.HAY_BLOCK),
				'd', Item.getItemFromBlock(Blocks.JUNGLE_FENCE),
				'e', Item.getItemFromBlock(Blocks.JUNGLE_FENCE_GATE));
		
		GameRegistry.addRecipe(new ItemStack(ModRegistry.HorseStable()),
				"aaa",
				"bcb",
				"ded",
				'a', Item.getItemFromBlock(Blocks.BRICK_BLOCK),
				'b', ModRegistry.BundleOfTimber(),
				'c', Item.getItemFromBlock(Blocks.HAY_BLOCK),
				'd', Item.getItemFromBlock(Blocks.DARK_OAK_FENCE),
				'e', Item.getItemFromBlock(Blocks.DARK_OAK_FENCE_GATE));
		
		// Nether Gate
		GameRegistry.addRecipe(new ItemStack(ModRegistry.NetherGate()),
				"aba",
				"bcb",
				"aba",
				'a', ModRegistry.GetCompressedStoneType(BlockCompressedStone.EnumType.DOUBLE_COMPRESSED_STONE),
				'b', Item.getItemFromBlock(Blocks.OBSIDIAN),
				'c', Items.FLINT_AND_STEEL);
		
		// Advanced Chicken Coop
		ItemStack result = new ItemStack(ModRegistry.BasicStructure());
		IStructureConfigurationCapability capability = result.getCapability(ModRegistry.StructureConfiguration, EnumFacing.NORTH);
		capability.getConfiguration().basicStructureName = EnumBasicStructureName.AdavancedCoop;
		
		GameRegistry.addShapelessRecipe(result, ModRegistry.ChickenCoop(), ModRegistry.PalletOfBricks());
		
		// Advanced Horse Stable
		result = new ItemStack(ModRegistry.BasicStructure());
		capability = result.getCapability(ModRegistry.StructureConfiguration, EnumFacing.NORTH);
		capability.getConfiguration().basicStructureName = EnumBasicStructureName.AdvancedHorseStable;
		
		GameRegistry.addShapelessRecipe(result, 
				ModRegistry.HorseStable(), ModRegistry.HorseStable(), ModRegistry.HorseStable(), ModRegistry.HorseStable(),
				ModRegistry.GetCompressedStoneType(BlockCompressedStone.EnumType.DOUBLE_COMPRESSED_GLOWSTONE));
		
		// Barn
		result = new ItemStack(ModRegistry.BasicStructure());
		capability = result.getCapability(ModRegistry.StructureConfiguration, EnumFacing.NORTH);
		capability.getConfiguration().basicStructureName = EnumBasicStructureName.Barn;
		
		GameRegistry.addRecipe(result,
				"aba",
				"ccc",
				"cdc",
				'a', ModRegistry.PalletOfBricks(),
				'b', ModRegistry.GetCompressedStoneType(BlockCompressedStone.EnumType.COMPRESSED_GLOWSTONE),
				'c', ModRegistry.BundleOfTimber(),
				'd', ModRegistry.GetCompressedStoneType(BlockCompressedStone.EnumType.DOUBLE_COMPRESSED_DIRT));
	}

	/**
	 * This is where the mod messages are registered.
	 */
	public static void RegisterMessages()
	{
		Prefab.network.registerMessage(HouseHandler.class, HouseTagMessage.class, 1, Side.SERVER);
		Prefab.network.registerMessage(WareHouseHandler.class, WareHouseTagMessage.class, 2, Side.SERVER);
		Prefab.network.registerMessage(ConfigSyncHandler.class, ConfigSyncMessage.class, 3, Side.CLIENT);
		Prefab.network.registerMessage(ChickenCoopHandler.class, ChickenCoopTagMessage.class, 4, Side.SERVER);
		Prefab.network.registerMessage(ProduceFarmHandler.class, ProduceFarmTagMessage.class, 5, Side.SERVER);
		Prefab.network.registerMessage(TreeFarmHandler.class, TreeFarmTagMessage.class, 6, Side.SERVER);
		Prefab.network.registerMessage(FishPondHandler.class, FishPondTagMessage.class, 7, Side.SERVER);
		Prefab.network.registerMessage(MonsterMasherHandler.class, MonsterMasherTagMessage.class, 8, Side.SERVER);
		Prefab.network.registerMessage(HorseStableHandler.class, HorseStableTagMessage.class, 9, Side.SERVER );
		Prefab.network.registerMessage(NetherGateHandler.class, NetherGateTagMessage.class, 10, Side.SERVER);
		Prefab.network.registerMessage(ModularHouseHandler.class, ModularHouseTagMessage.class, 11, Side.SERVER);
		Prefab.network.registerMessage(BasicStructureHandler.class, BasicStructureTagMessage.class, 12, Side.SERVER);
	}
	
	/**
	 * This is where mod capabilities are registered.
	 */
	public static void RegisterCapabilities()
	{
		// Register the dimension home capability.
		CapabilityManager.INSTANCE.register(IStructureConfigurationCapability.class, new StructureConfigurationStorage(), StructureConfigurationCapability.class);
	}
	
	/**
	 * This method is used to register item variants for the blocks for this mod.
	 */
	public static void RegisterItemVariants()
	{
		ModelBakery.registerItemVariants(ModRegistry.CompressedStoneItem(), BlockCompressedStone.EnumType.GetNames());
	}

	
	/**
	 * Register an Item
	 *
	 * @param item The Item instance
	 * @param <T> The Item type
	 * @return The Item instance
	 */
	public static <T extends Item> T registerItem(T item)
	{
		GameRegistry.register(item);
		ModRegistry.ModItems.add(item);

		return item;
	}
	
	/**
	 * Register an Item
	 *
	 * @param item The Item instance
	 * @param <T> The Item type
	 * @return The Item instance
	 */
	public static <T extends Item> T registerItem(T item, ResourceLocation resourceLocation, boolean registerInMod)
	{
		GameRegistry.register(item, resourceLocation);
		
		if (registerInMod)
		{
			ModRegistry.ModItems.add(item);
		}
		
		return item;
	}
	
	public static <T extends Block> T registerBlock(T block)
	{
		return ModRegistry.registerBlock(block, true);
	}
	
	public static <T extends Block> T registerBlock(T block, boolean includeItemBlock)
	{
		GameRegistry.register(block);
		
		if (includeItemBlock)
		{
			GameRegistry.register(new ItemBlock(block).setRegistryName(block.getRegistryName()));
		}
		
		ModRegistry.ModBlocks.add(block);
		
		return block;
	}
	
	public static <T extends Block, I extends ItemBlock> T registerBlock(T block, I itemBlock)
	{
		GameRegistry.register(block);
		ModRegistry.ModBlocks.add(block);
		
		if (itemBlock != null)
		{
			GameRegistry.register(itemBlock);
			ModRegistry.ModItems.add(itemBlock);
		}
		
		return block;
	}
	
	/**
	 * Set the registry name of {@code item} to {@code itemName} and the un-localised name to the full registry name.
	 *
	 * @param item     The item
	 * @param itemName The item's name
	 */
	public static void setItemName(Item item, String itemName) 
	{
		if (itemName != null)
		{
			item.setRegistryName(itemName);
			item.setUnlocalizedName(item.getRegistryName().toString());
		}
	}
	
	/**
	 * Set the registry name of {@code block} to {@code blockName} and the un-localised name to the full registry name.
	 *
	 * @param block     The block
	 * @param blockName The block's name
	 */
	public static void setBlockName(Block block, String blockName) 
	{
		block.setRegistryName(blockName);
		block.setUnlocalizedName(block.getRegistryName().toString());
	}

	public static void AddGuis()
	{
		ModRegistry.ModGuis.put(ModRegistry.GuiWareHouse, GuiWareHouse.class);
		ModRegistry.ModGuis.put(ModRegistry.GuiChickenCoop, GuiChickenCoop.class);
		ModRegistry.ModGuis.put(ModRegistry.GuiProduceFarm, GuiProduceFarm.class);
		ModRegistry.ModGuis.put(ModRegistry.GuiTreeFarm, GuiTreeFarm.class);
		ModRegistry.ModGuis.put(ModRegistry.GuiFishPond, GuiFishPond.class);
		ModRegistry.ModGuis.put(ModRegistry.GuiStartHouseChooser, GuiStartHouseChooser.class);
		ModRegistry.ModGuis.put(ModRegistry.GuiAdvancedWareHouse, GuiAdvancedWareHouse.class);
		ModRegistry.ModGuis.put(ModRegistry.GuiMonsterMasher, GuiMonsterMasher.class);
		ModRegistry.ModGuis.put(ModRegistry.GuiHorseStable, GuiHorseStable.class);
		ModRegistry.ModGuis.put(ModRegistry.GuiNetherGate,  GuiNetherGate.class);
		ModRegistry.ModGuis.put(ModRegistry.GuiModularHouse, GuiModularHouse.class);
		ModRegistry.ModGuis.put(ModRegistry.GuiDrafter, GuiDrafter.class);
		ModRegistry.ModGuis.put(ModRegistry.GuiBasicStructure, GuiBasicStructure.class);
	}
}
