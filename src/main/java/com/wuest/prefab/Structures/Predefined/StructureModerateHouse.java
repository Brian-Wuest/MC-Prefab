package com.wuest.prefab.Structures.Predefined;

import java.util.ArrayList;

import com.wuest.prefab.Prefab;
import com.wuest.prefab.Config.EntityPlayerConfiguration;
import com.wuest.prefab.Structures.Base.BuildBlock;
import com.wuest.prefab.Structures.Base.BuildClear;
import com.wuest.prefab.Structures.Base.Structure;
import com.wuest.prefab.Structures.Config.ModerateHouseConfiguration;
import com.wuest.prefab.Structures.Config.StructureConfiguration;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.FurnaceBlock;
import net.minecraft.block.TrapDoorBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.tileentity.FurnaceTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * 
 * @author WuestMan
 *
 */
public class StructureModerateHouse extends Structure
{
	private BlockPos chestPosition = null;
	private ArrayList<BlockPos> furnacePosition = null;
	private BlockPos trapDoorPosition = null;
	private static ArrayList<BlockPos> torchPositions = null;

	public static void ScanStructure(World world, BlockPos originalPos, Direction playerFacing, ModerateHouseConfiguration.HouseStyle houseStyle)
	{
		BuildClear clearedSpace = new BuildClear();
		clearedSpace.getShape().setDirection(Direction.SOUTH);
		clearedSpace.getShape().setHeight(houseStyle.getHeight());
		clearedSpace.getShape().setLength(houseStyle.getLength());
		clearedSpace.getShape().setWidth(houseStyle.getWidth());
		clearedSpace.getStartingPosition().setSouthOffset(1);
		clearedSpace.getStartingPosition().setEastOffset(houseStyle.getEastOffSet());
		clearedSpace.getStartingPosition().setHeightOffset(houseStyle.getDownOffSet() * -1);

		BlockPos cornerPos = originalPos.east(houseStyle.getEastOffSet()).south().down(houseStyle.getDownOffSet());

		Structure.ScanStructure(
			world,
			originalPos,
			cornerPos,
			cornerPos.south(houseStyle.getLength()).west(houseStyle.getWidth()).up(houseStyle.getHeight()),
			"../src/main/resources/" + houseStyle.getStructureLocation(),
			clearedSpace,
			playerFacing, false, false);
	}

	@Override
	protected Boolean CustomBlockProcessingHandled(StructureConfiguration configuration, BuildBlock block, World world, BlockPos originalPos,
		Direction assumedNorth, Block foundBlock, BlockState blockState, PlayerEntity player)
	{
		if (foundBlock instanceof FurnaceBlock)
		{
			if (this.furnacePosition == null)
			{
				this.furnacePosition = new ArrayList<BlockPos>();
			}

			this.furnacePosition.add(block.getStartingPosition().getRelativePosition(
				originalPos,
				this.getClearSpace().getShape().getDirection(),
				configuration.houseFacing));
		}
		else if (foundBlock instanceof ChestBlock && !((ModerateHouseConfiguration) configuration).addChests)
		{
			return true;
		}
		else if (foundBlock instanceof ChestBlock && this.chestPosition == null)
		{
			this.chestPosition = block.getStartingPosition().getRelativePosition(
				originalPos,
				this.getClearSpace().getShape().getDirection(),
				configuration.houseFacing);
		}
		else if (foundBlock instanceof TrapDoorBlock)
		{
			// The trap door will still be added, but the mine shaft may not be
			// built.
			this.trapDoorPosition = block.getStartingPosition().getRelativePosition(
				originalPos,
				this.getClearSpace().getShape().getDirection(),
				configuration.houseFacing);
		}

		return false;
	}

	/**
	 * This method is used after the main building is build for any additional structures or modifications.
	 * 
	 * @param configuration The structure configuration.
	 * @param world         The current world.
	 * @param originalPos   The original position clicked on.
	 * @param assumedNorth  The assumed northern direction.
	 * @param player        The player which initiated the construction.
	 */
	@Override
	public void AfterBuilding(StructureConfiguration configuration, ServerWorld world, BlockPos originalPos, Direction assumedNorth, PlayerEntity player)
	{
		ModerateHouseConfiguration houseConfig = (ModerateHouseConfiguration) configuration;
		EntityPlayerConfiguration playerConfig = EntityPlayerConfiguration.loadFromEntityData((ServerPlayerEntity) player);

		if (this.furnacePosition != null)
		{
			for (BlockPos furnacePos : this.furnacePosition)
			{
				// Fill the furnace.
				TileEntity tileEntity = world.getTileEntity(furnacePos);

				if (tileEntity instanceof FurnaceTileEntity)
				{
					FurnaceTileEntity furnaceTile = (FurnaceTileEntity) tileEntity;
					furnaceTile.setInventorySlotContents(1, new ItemStack(Items.COAL, 20));
				}
			}
		}

		if (this.chestPosition != null && !playerConfig.builtStarterHouse && houseConfig.addChestContents)
		{
			// Fill the chest if the player hasn't generated the starting house yet.
			StructureModerateHouse.FillChest(world, this.chestPosition, houseConfig, player);
		}

		if (this.trapDoorPosition != null && this.trapDoorPosition.getY() > 15 && houseConfig.addMineshaft)
		{
			// Build the mineshaft.
			StructureAlternateStart.PlaceMineShaft(world, this.trapDoorPosition.down(), houseConfig.houseFacing, false);
		}

		// Make sure to set this value so the player cannot fill the chest a second time.
		playerConfig.builtStarterHouse = true;
		playerConfig.saveToPlayer(player);
	}

	public static void FillChest(World world, BlockPos itemPosition, ModerateHouseConfiguration configuration, PlayerEntity player)
	{
		// Add each stone tool to the chest and leather armor.
		TileEntity tileEntity = world.getTileEntity(itemPosition);

		if (tileEntity instanceof ChestTileEntity)
		{
			ChestTileEntity chestTile = (ChestTileEntity) tileEntity;

			int itemSlot = 0;

			// Add the tools.
			if (Prefab.proxy.proxyConfiguration.serverConfiguration.addAxe)
			{
				chestTile.setInventorySlotContents(itemSlot++, new ItemStack(Items.STONE_AXE));
			}

			if (Prefab.proxy.proxyConfiguration.serverConfiguration.addHoe)
			{
				chestTile.setInventorySlotContents(itemSlot++, new ItemStack(Items.STONE_HOE));
			}

			if (Prefab.proxy.proxyConfiguration.serverConfiguration.addPickAxe)
			{
				// Trigger the "Time to Mine" achievement and the better
				// pick axe achievement.
				chestTile.setInventorySlotContents(itemSlot++, new ItemStack(Items.STONE_PICKAXE));
			}

			if (Prefab.proxy.proxyConfiguration.serverConfiguration.addShovel)
			{
				chestTile.setInventorySlotContents(itemSlot++, new ItemStack(Items.STONE_SHOVEL));
			}

			if (Prefab.proxy.proxyConfiguration.serverConfiguration.addSword)
			{
				// Include the swift blade if WuestUtilities has registered the
				// swift blades.
				ResourceLocation name = new ResourceLocation("repurpose", "itemSwiftBladeStone");

				Item sword = Items.STONE_SWORD;

				if (ForgeRegistries.ITEMS.containsKey(name))
				{
					sword = ForgeRegistries.ITEMS.getValue(name);
				}

				chestTile.setInventorySlotContents(itemSlot++, new ItemStack(sword));
			}

			if (Prefab.proxy.proxyConfiguration.serverConfiguration.addArmor)
			{
				// Add the armor.
				chestTile.setInventorySlotContents(itemSlot++, new ItemStack(Items.LEATHER_BOOTS));
				chestTile.setInventorySlotContents(itemSlot++, new ItemStack(Items.LEATHER_CHESTPLATE));
				chestTile.setInventorySlotContents(itemSlot++, new ItemStack(Items.LEATHER_HELMET));
				chestTile.setInventorySlotContents(itemSlot++, new ItemStack(Items.LEATHER_LEGGINGS));
			}

			if (Prefab.proxy.proxyConfiguration.serverConfiguration.addFood)
			{
				// Add some bread.
				chestTile.setInventorySlotContents(itemSlot++, new ItemStack(Items.BREAD, 20));
			}

			if (Prefab.proxy.proxyConfiguration.serverConfiguration.addCrops)
			{
				// Add potatoes.
				chestTile.setInventorySlotContents(itemSlot++, new ItemStack(Items.POTATO, 3));

				// Add carrots.
				chestTile.setInventorySlotContents(itemSlot++, new ItemStack(Items.CARROT, 3));

				// Add seeds.
				chestTile.setInventorySlotContents(itemSlot++, new ItemStack(Items.WHEAT_SEEDS, 3));
			}

			if (Prefab.proxy.proxyConfiguration.serverConfiguration.addCobble)
			{
				// Add Cobblestone.
				chestTile.setInventorySlotContents(itemSlot++, new ItemStack(Item.getItemFromBlock(Blocks.COBBLESTONE), 64));
			}

			if (Prefab.proxy.proxyConfiguration.serverConfiguration.addDirt)
			{
				// Add Dirt.
				chestTile.setInventorySlotContents(itemSlot++, new ItemStack(Item.getItemFromBlock(Blocks.DIRT), 64));
			}

			if (Prefab.proxy.proxyConfiguration.serverConfiguration.addSaplings)
			{
				// Add oak saplings.
				chestTile.setInventorySlotContents(itemSlot++, new ItemStack(Item.getItemFromBlock(Blocks.OAK_SAPLING), 3));
			}

			if (Prefab.proxy.proxyConfiguration.serverConfiguration.addTorches)
			{
				// Add a set of 20 torches.
				chestTile.setInventorySlotContents(itemSlot++, new ItemStack(Item.getItemFromBlock(Blocks.TORCH), 20));
			}
		}
	}

}