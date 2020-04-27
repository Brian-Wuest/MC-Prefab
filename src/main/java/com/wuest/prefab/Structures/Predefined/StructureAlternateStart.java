package com.wuest.prefab.Structures.Predefined;

import com.wuest.prefab.Structures.Base.*;
import com.wuest.prefab.Structures.Config.HouseConfiguration;
import com.wuest.prefab.Structures.Config.StructureConfiguration;
import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.SignTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.ArrayList;

/**
 * @author WuestMan
 */
@SuppressWarnings({"unused", "ConstantConditions", "UnusedAssignment"})
public class StructureAlternateStart extends Structure {
	private BlockPos chestPosition = null;
	private BlockPos furnacePosition = null;
	private BlockPos trapDoorPosition = null;
	private BlockPos signPosition = null;
	private BlockPos bedHeadPosition = null;
	private BlockPos bedFootPosition = null;

	public static void ScanBasicHouseStructure(World world, BlockPos originalPos, Direction playerFacing) {
		BuildClear clearedSpace = new BuildClear();
		clearedSpace.getShape().setDirection(Direction.SOUTH);
		clearedSpace.getShape().setHeight(10);
		clearedSpace.getShape().setLength(12);
		clearedSpace.getShape().setWidth(13);
		clearedSpace.getStartingPosition().setSouthOffset(1);
		clearedSpace.getStartingPosition().setEastOffset(5);
		clearedSpace.getStartingPosition().setHeightOffset(-1);

		BlockPos corner = originalPos.east(5).south().down();
		BlockPos corner2 = originalPos.west(8).south(13).up(10);

		Structure.ScanStructure(world, originalPos, corner, corner2,
				"..\\src\\main\\resources\\assets\\prefab\\structures\\basic_house.zip", clearedSpace,
				playerFacing, false, false);
	}

	public static void ScanRanchStructure(World world, BlockPos originalPos, Direction playerFacing) {
		BuildClear clearedSpace = new BuildClear();
		clearedSpace.getShape().setDirection(Direction.SOUTH);
		clearedSpace.getShape().setHeight(7);
		clearedSpace.getShape().setLength(21);
		clearedSpace.getShape().setWidth(11);
		clearedSpace.getStartingPosition().setSouthOffset(1);
		clearedSpace.getStartingPosition().setEastOffset(8);
		clearedSpace.getStartingPosition().setHeightOffset(-1);

		Structure.ScanStructure(world, originalPos, originalPos.east(8).south().down(), originalPos.south(22).west(3).up(8),
				"..\\src\\main\\resources\\assets\\prefab\\structures\\ranch_house.zip", clearedSpace,
				playerFacing, false, false);
	}

	public static void ScanLoftStructure(World world, BlockPos originalPos, Direction playerFacing) {
		BuildClear clearedSpace = new BuildClear();
		clearedSpace.getShape().setDirection(Direction.SOUTH);
		clearedSpace.getShape().setHeight(9);
		clearedSpace.getShape().setLength(13);
		clearedSpace.getShape().setWidth(16);
		clearedSpace.getStartingPosition().setSouthOffset(1);
		clearedSpace.getStartingPosition().setEastOffset(7);

		Structure.ScanStructure(world, originalPos, originalPos.east(7).south(), originalPos.south(14).west(8).up(9),
				"..\\src\\main\\resources\\assets\\prefab\\structures\\loft_house.zip", clearedSpace, playerFacing, false, false);
	}

	public static void ScanHobbitStructure(World world, BlockPos originalPos, Direction playerFacing) {
		BuildClear clearedSpace = new BuildClear();
		clearedSpace.getShape().setDirection(Direction.SOUTH);
		clearedSpace.getShape().setHeight(12);
		clearedSpace.getShape().setLength(16);
		clearedSpace.getShape().setWidth(16);
		clearedSpace.getStartingPosition().setSouthOffset(1);
		clearedSpace.getStartingPosition().setEastOffset(8);
		clearedSpace.getStartingPosition().setHeightOffset(-3);

		Structure.ScanStructure(world, originalPos, originalPos.east(8).south().down(3), originalPos.south(16).west(8).up(12),
				"..\\src\\main\\resources\\assets\\prefab\\structures\\hobbit_house.zip", clearedSpace,
				playerFacing, false, false);
	}

	public static void ScanDesert2Structure(World world, BlockPos originalPos, Direction playerFacing) {
		BuildClear clearedSpace = new BuildClear();
		clearedSpace.getShape().setDirection(Direction.SOUTH);
		clearedSpace.getShape().setHeight(10);
		clearedSpace.getShape().setLength(14);
		clearedSpace.getShape().setWidth(11);
		clearedSpace.getStartingPosition().setSouthOffset(1);
		clearedSpace.getStartingPosition().setEastOffset(8);
		clearedSpace.getStartingPosition().setHeightOffset(-1);

		BlockPos corner = originalPos.east(8).south().down();
		BlockPos corner2 = originalPos.west(6).south(16).up(10);

		Structure.ScanStructure(world, originalPos, corner, corner2,
				"..\\src\\main\\resources\\assets\\prefab\\structures\\desert_house2.zip", clearedSpace,
				playerFacing, false, false);
	}

	public static void ScanSubAquaticStructure(World world, BlockPos originalPos, Direction playerFacing) {
		BuildClear clearedSpace = new BuildClear();
		clearedSpace.getShape().setDirection(Direction.SOUTH);
		clearedSpace.getShape().setHeight(13);
		clearedSpace.getShape().setLength(9);
		clearedSpace.getShape().setWidth(12);
		clearedSpace.getStartingPosition().setSouthOffset(1);
		clearedSpace.getStartingPosition().setEastOffset(8);
		clearedSpace.getStartingPosition().setHeightOffset(-1);

		BlockPos corner = originalPos.east(8).south().down();
		BlockPos corner2 = originalPos.west(4).south(10).up(12);

		Structure.ScanStructure(world, originalPos, corner, corner2,
				"..\\src\\main\\resources\\assets\\prefab\\structures\\subaquatic_house.zip", clearedSpace,
				playerFacing, true, true);
	}

	public static void ScanStructure(World world, BlockPos originalPos, Direction playerFacing, String structureFileName, boolean includeAir, boolean excludeWater) {
		BuildClear clearedSpace = new BuildClear();
		clearedSpace.getShape().setDirection(Direction.SOUTH);
		clearedSpace.getShape().setHeight(8);
		clearedSpace.getShape().setLength(16);
		clearedSpace.getShape().setWidth(16);
		clearedSpace.getStartingPosition().setSouthOffset(1);
		clearedSpace.getStartingPosition().setEastOffset(8);
		clearedSpace.getStartingPosition().setHeightOffset(-1);

		BuildShape buildShape = clearedSpace.getShape();
		PositionOffset offset = clearedSpace.getStartingPosition();

		int downOffset = offset.getHeightOffset() < 0 ? Math.abs(offset.getHeightOffset()) : 0;
		BlockPos cornerPos = originalPos.east(offset.getEastOffset()).south(offset.getSouthOffset()).down(downOffset);

		Structure.ScanStructure(
				world,
				originalPos,
				cornerPos,
				cornerPos.south(buildShape.getLength()).west(buildShape.getWidth()).up(buildShape.getHeight()),
				"..\\src\\main\\resources\\assets\\prefab\\structures\\" + structureFileName + ".zip",
				clearedSpace,
				playerFacing,
				includeAir,
				excludeWater);
	}

	@Override
	protected Boolean CustomBlockProcessingHandled(StructureConfiguration configuration, BuildBlock block, World world, BlockPos originalPos,
												   Direction assumedNorth, Block foundBlock, BlockState blockState, PlayerEntity player) {
		HouseConfiguration houseConfig = (HouseConfiguration) configuration;

		if ((!houseConfig.addBed && foundBlock instanceof BedBlock) || (!houseConfig.addChest && foundBlock instanceof ChestBlock)
				|| (!houseConfig.addTorches && foundBlock instanceof TorchBlock)
				|| (!houseConfig.addCraftingTable && foundBlock instanceof CraftingTableBlock)
				|| (!houseConfig.addFurnace && foundBlock instanceof FurnaceBlock)
				|| (!houseConfig.addChest && foundBlock instanceof BarrelBlock)
				|| (foundBlock instanceof SeaGrassBlock)
				|| (foundBlock instanceof TallSeaGrassBlock)) {
			// Don't place the block, returning true means that this has been
			// "handled"
			return true;
		}

		if (foundBlock instanceof FurnaceBlock) {
			this.furnacePosition = block.getStartingPosition().getRelativePosition(
					originalPos,
					this.getClearSpace().getShape().getDirection(),
					configuration.houseFacing);
		} else if (foundBlock instanceof TrapDoorBlock && houseConfig.addMineShaft) {
			// The trap door will still be added, but the mine shaft may not be
			// built.
			this.trapDoorPosition = block.getStartingPosition().getRelativePosition(
					originalPos,
					this.getClearSpace().getShape().getDirection(),
					configuration.houseFacing);
		} else if ((foundBlock instanceof ChestBlock && this.chestPosition == null)
				|| (foundBlock instanceof BarrelBlock && this.chestPosition == null)) {
			this.chestPosition = block.getStartingPosition().getRelativePosition(
					originalPos,
					this.getClearSpace().getShape().getDirection(),
					configuration.houseFacing);
		} else if (foundBlock instanceof StandingSignBlock) {
			this.signPosition = block.getStartingPosition().getRelativePosition(
					originalPos,
					this.getClearSpace().getShape().getDirection(),
					configuration.houseFacing);
		} else if (foundBlock instanceof BedBlock && houseConfig.addBed) {
			this.bedHeadPosition = block.getStartingPosition().getRelativePosition(originalPos, this.getClearSpace().getShape().getDirection(), configuration.houseFacing);
			this.bedFootPosition = block.getSubBlock().getStartingPosition().getRelativePosition(
					originalPos,
					this.getClearSpace().getShape().getDirection(),
					configuration.houseFacing);
		}

		if (foundBlock.getRegistryName().getNamespace().equals(Blocks.WHITE_STAINED_GLASS.getRegistryName().getNamespace())
				&& foundBlock.getRegistryName().getPath().endsWith("stained_glass")) {
			blockState = this.getStainedGlassBlock(houseConfig.glassColor);

			block.setBlockState(blockState);
			this.priorityOneBlocks.add(block);

			return true;
		} else if (foundBlock.getRegistryName().getNamespace().equals(Blocks.WHITE_STAINED_GLASS_PANE.getRegistryName().getNamespace())
				&& foundBlock.getRegistryName().getPath().endsWith("stained_glass_pane")) {
			blockState = this.getStainedGlassPaneBlock(houseConfig.glassColor);
			block.setBlockState(blockState);

			this.priorityOneBlocks.add(block);
			return true;
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
	public void AfterBuilding(StructureConfiguration configuration, ServerWorld world, BlockPos originalPos, Direction assumedNorth, PlayerEntity player) {
		HouseConfiguration houseConfig = (HouseConfiguration) configuration;

		ArrayList<BlockPos> furnacePositions = new ArrayList<>();

		if (this.furnacePosition != null) {
			furnacePositions.add(this.furnacePosition);
		}

		BuildingMethods.FillFurnaces(world, furnacePositions);

		if (this.chestPosition != null && houseConfig.addChestContents) {
			// Fill the chest.
			BuildingMethods.FillChest(world, this.chestPosition);
		}

		if (this.trapDoorPosition != null && this.trapDoorPosition.getY() > 15 && houseConfig.addMineShaft) {
			// Build the mineshaft.
			BuildingMethods.PlaceMineShaft(world, this.trapDoorPosition.down(), houseConfig.houseFacing, false);
		}

		if (this.signPosition != null) {
			TileEntity tileEntity = world.getTileEntity(this.signPosition);

			if (tileEntity instanceof SignTileEntity) {
				SignTileEntity signTile = (SignTileEntity) tileEntity;
				signTile.signText[0] = new StringTextComponent("This is");

				if (player.getDisplayName().getString().length() >= 15) {
					signTile.signText[1] = new StringTextComponent(player.getDisplayName().getString());
				} else {
					signTile.signText[1] = new StringTextComponent(player.getDisplayName().getString() + "'s");
				}

				signTile.signText[2] = new StringTextComponent("house!");
			}
		}

		if (this.bedHeadPosition != null && houseConfig.addBed) {
			BuildingMethods.PlaceColoredBed(world, this.bedHeadPosition, this.bedFootPosition, houseConfig.bedColor);
		}
	}
}
