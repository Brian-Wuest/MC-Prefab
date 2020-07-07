package com.wuest.prefab.Structures.Predefined;

import com.wuest.prefab.Proxy.CommonProxy;
import com.wuest.prefab.Structures.Base.BuildBlock;
import com.wuest.prefab.Structures.Base.BuildClear;
import com.wuest.prefab.Structures.Base.Structure;
import com.wuest.prefab.Structures.Config.MonsterMasherConfiguration;
import com.wuest.prefab.Structures.Config.StructureConfiguration;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.WallSignBlock;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.MobSpawnerTileEntity;
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
@SuppressWarnings({"ConstantConditions", "SpellCheckingInspection"})
public class StructureMonsterMasher extends Structure {
	public static final String ASSETLOCATION = "assets/prefab/structures/monster_masher.zip";

	private ArrayList<BlockPos> mobSpawnerPos = new ArrayList<BlockPos>();
	private BlockPos signPosition = null;

	public static void ScanStructure(World world, BlockPos originalPos, Direction playerFacing) {
		BuildClear clearedSpace = new BuildClear();
		clearedSpace.getShape().setDirection(Direction.SOUTH);
		clearedSpace.getShape().setHeight(18);
		clearedSpace.getShape().setLength(15);
		clearedSpace.getShape().setWidth(13);
		clearedSpace.getStartingPosition().setSouthOffset(1);
		clearedSpace.getStartingPosition().setEastOffset(6);

		Structure.ScanStructure(
				world,
				originalPos,
				originalPos.east(6).south(),
				originalPos.south(15).west(6).up(18),
				"..\\src\\main\\resources\\assets\\prefab\\structures\\monster_masher.zip",
				clearedSpace,
				playerFacing, false, false);
	}

	@Override
	protected Boolean CustomBlockProcessingHandled(StructureConfiguration configuration, BuildBlock block, World world, BlockPos originalPos, Direction assumedNorth,
												   Block foundBlock, BlockState blockState, PlayerEntity player) {
		if (foundBlock.getRegistryName().getNamespace().equals(Blocks.WHITE_STAINED_GLASS.getRegistryName().getNamespace())
				&& foundBlock.getRegistryName().getPath().endsWith("stained_glass")) {
			MonsterMasherConfiguration wareHouseConfiguration = (MonsterMasherConfiguration) configuration;

			blockState = this.getStainedGlassBlock(wareHouseConfiguration.dyeColor);
			block.setBlockState(blockState);
			this.priorityOneBlocks.add(block);

			return true;
		} else if (foundBlock.getRegistryName().getNamespace().equals(Blocks.SPAWNER.getRegistryName().getNamespace())
				&& foundBlock.getRegistryName().getPath().equals(Blocks.SPAWNER.getRegistryName().getPath())) {
			if (CommonProxy.proxyConfiguration.serverConfiguration.includeSpawnersInMasher) {
				this.mobSpawnerPos.add(block.getStartingPosition().getRelativePosition(
						originalPos,
						this.getClearSpace().getShape().getDirection(),
						configuration.houseFacing));
			} else {
				return true;
			}
		} else if (foundBlock instanceof WallSignBlock) {
			this.signPosition = block.getStartingPosition().getRelativePosition(
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
	public void AfterBuilding(StructureConfiguration configuration, ServerWorld world, BlockPos originalPos, Direction assumedNorth, PlayerEntity player) {
		int monstersPlaced = 0;

		// Set the spawner.
		for (BlockPos pos : this.mobSpawnerPos) {
			TileEntity tileEntity = world.getTileEntity(pos);

			if (tileEntity instanceof MobSpawnerTileEntity) {
				MobSpawnerTileEntity spawner = (MobSpawnerTileEntity) tileEntity;

				switch (monstersPlaced) {
					case 0: {
						// Zombie.
						spawner.getSpawnerBaseLogic().setEntityType(EntityType.ZOMBIE);
						break;
					}

					case 1: {
						// Skeleton.
						spawner.getSpawnerBaseLogic().setEntityType(EntityType.SKELETON);
						break;
					}

					case 2: {
						// Spider.
						spawner.getSpawnerBaseLogic().setEntityType(EntityType.SPIDER);
						break;
					}

					default: {
						// Creeper.
						spawner.getSpawnerBaseLogic().setEntityType(EntityType.CREEPER);
						break;
					}
				}

				monstersPlaced++;
			}
		}

		if (this.signPosition != null) {
			TileEntity tileEntity = world.getTileEntity(this.signPosition);

			if (tileEntity instanceof SignTileEntity) {
				SignTileEntity signTile = (SignTileEntity) tileEntity;
				signTile.setText(0, new StringTextComponent("Lamp On=Mobs"));

				signTile.setText(2, new StringTextComponent("Lamp Off=No Mobs"));
			}
		}
	}
}
