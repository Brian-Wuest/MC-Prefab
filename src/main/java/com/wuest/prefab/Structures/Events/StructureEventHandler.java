package com.wuest.prefab.Structures.Events;

import com.wuest.prefab.Config.EntityPlayerConfiguration;
import com.wuest.prefab.ModRegistry;
import com.wuest.prefab.Prefab;
import com.wuest.prefab.Proxy.CommonProxy;
import com.wuest.prefab.Proxy.Messages.PlayerEntityTagMessage;
import com.wuest.prefab.Structures.Base.*;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.HangingEntity;
import net.minecraft.entity.item.ItemFrameEntity;
import net.minecraft.entity.item.PaintingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.DoubleNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.state.properties.DoubleBlockHalf;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.TickEvent.ServerTickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.network.NetworkDirection;

import java.util.*;
import java.util.Map.Entry;

/**
 * This is the structure event hander.
 *
 * @author WuestMan
 */
@SuppressWarnings({"ConstantConditions", "UnusedAssignment", "unused"})
@EventBusSubscriber(modid = Prefab.MODID)
public final class StructureEventHandler {
	/**
	 * Contains a hashmap for the structures to build and for whom.
	 */
	public static HashMap<PlayerEntity, ArrayList<Structure>> structuresToBuild = new HashMap<PlayerEntity, ArrayList<Structure>>();

	/**
	 * This event is used to determine if the player should be given the starting house item when they log in.
	 *
	 * @param event The event object.
	 */
	@SubscribeEvent
	public static void PlayerLoggedIn(PlayerLoggedInEvent event) {
		if (!event.getPlayer().world.isRemote && event.getPlayer() instanceof ServerPlayerEntity) {
			ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();
			EntityPlayerConfiguration playerConfig = EntityPlayerConfiguration.loadFromEntityData(player);

			String startingItem = CommonProxy.proxyConfiguration.serverConfiguration.startingItem;

			if (!playerConfig.givenHouseBuilder && startingItem != null) {
				ItemStack stack = ItemStack.EMPTY;

				switch (startingItem.toLowerCase()) {
					case "structure part": {
						stack = new ItemStack(ModRegistry.StructurePart.get());
						break;
					}

					case "starting house": {
						stack = new ItemStack(ModRegistry.StartHouse.get());
						break;
					}

					case "moderate house": {
						stack = new ItemStack(ModRegistry.ModerateHouse.get());
						break;
					}
				}

				if (!stack.isEmpty()) {
					System.out.println(player.getDisplayName().getString() + " joined the game for the first time. Giving them starting item.");

					player.inventory.addItemStackToInventory(stack);
					player.openContainer.detectAndSendChanges();

					// Make sure to set the tag for this player so they don't get the item again.
					playerConfig.givenHouseBuilder = true;
					playerConfig.saveToPlayer(player);
				}
			}

			// Send the persist tag to the client.
			Prefab.network.sendTo(
					new PlayerEntityTagMessage(playerConfig.getModIsPlayerNewTag(player)),
					((ServerPlayerEntity) event.getPlayer()).connection.netManager,
					NetworkDirection.PLAY_TO_CLIENT);
		}
	}

	/**
	 * This event is primarily used to build 100 blocks for any queued structures for all players.
	 *
	 * @param event The event object.
	 */
	@SubscribeEvent
	public static void onServerTick(ServerTickEvent event) {
		if (event.phase == TickEvent.Phase.START) {
			ArrayList<PlayerEntity> playersToRemove = new ArrayList<PlayerEntity>();

			for (Entry<PlayerEntity, ArrayList<Structure>> entry : StructureEventHandler.structuresToBuild.entrySet()) {
				ArrayList<Structure> structuresToRemove = new ArrayList<Structure>();

				// Build the first 100 blocks of each structure for this player.
				for (Structure structure : entry.getValue()) {
					if (!structure.entitiesRemoved) {
						// Go through each block and find any entities there. If there are any; kill them if they aren't players.
						// If there is a player there...they will probably die anyways.....
						for (BlockPos clearedPos : structure.clearedBlockPos) {
							AxisAlignedBB axisPos = VoxelShapes.fullCube().getBoundingBox().offset(clearedPos);

							List<Entity> list = structure.world.getEntitiesWithinAABBExcludingEntity((Entity) null, axisPos);

							if (!list.isEmpty()) {
								for (Entity entity : list) {
									// Don't kill living entities.
									if (!(entity instanceof LivingEntity)) {
										if (entity instanceof HangingEntity) {
											structure.BeforeHangingEntityRemoved((HangingEntity) entity);
										}

										structure.world.removeEntity(entity, false);
									}
								}
							}
						}

						structure.entitiesRemoved = true;
					}

					for (int i = 0; i < 100; i++) {
						i = StructureEventHandler.setBlock(i, structure, structuresToRemove);
					}

					// After building the blocks for this tick, find waterlogged blocks and remove them.
					StructureEventHandler.removeWaterLogging(structure);
				}

				// Update the list of structures to remove this structure since it's done building.
				StructureEventHandler.removeStructuresFromList(structuresToRemove, entry);

				if (entry.getValue().size() == 0) {
					playersToRemove.add(entry.getKey());
				}
			}

			// Remove each player that has their structure's built.
			for (PlayerEntity player : playersToRemove) {
				StructureEventHandler.structuresToBuild.remove(player);
			}
		}
	}

	/**
	 * This occurs when a player dies and is used to make sure that a player does not get a duplicate starting house.
	 *
	 * @param event The player clone event.
	 */
	@SubscribeEvent
	public static void onClone(PlayerEvent.Clone event) {
		if (event.getPlayer() instanceof ServerPlayerEntity) {
			// Don't add the tag unless the house item was added. This way it can be added if the feature is turned on.
			// When the player is cloned, make sure to copy the tag. If this is not done the item can be given to the
			// player again if they die before the log out and log back in.
			CompoundNBT originalTag = event.getOriginal().getPersistentData();

			// Use the server configuration to determine if the house should be added for this player.
			String startingItem = CommonProxy.proxyConfiguration.serverConfiguration.startingItem;
			if (startingItem != null && !startingItem.equalsIgnoreCase("Nothing")) {
				if (originalTag.contains(EntityPlayerConfiguration.PLAYER_ENTITY_TAG)) {
					CompoundNBT newPlayerTag = event.getPlayer().getPersistentData();
					newPlayerTag.put(EntityPlayerConfiguration.PLAYER_ENTITY_TAG, originalTag.get(EntityPlayerConfiguration.PLAYER_ENTITY_TAG));

					// Send the persist tag to the client.
					Prefab.network.sendTo(
							new PlayerEntityTagMessage(originalTag.getCompound(EntityPlayerConfiguration.PLAYER_ENTITY_TAG)),
							((ServerPlayerEntity) event.getPlayer()).connection.netManager,
							NetworkDirection.PLAY_TO_CLIENT);
				}
			}
		}
	}

	private static int setBlock(int i, Structure structure, ArrayList<Structure> structuresToRemove) {
		// Structure clearing happens before anything else.
		if (structure.clearedBlockPos.size() > 0) {
			BlockPos currentPos = structure.clearedBlockPos.get(0);
			structure.clearedBlockPos.remove(0);

			BlockState clearBlockState = structure.world.getBlockState(currentPos);

			// If this block is not specifically air then set it to air.
			// This will also break other mod's logic blocks but they would probably be broken due to structure
			// generation anyways.
			if (!clearBlockState.isAir(structure.world, currentPos)) {
				structure.BeforeClearSpaceBlockReplaced(currentPos);

				for (Direction adjacentBlock : Direction.values()) {
					BlockPos tempPos = currentPos.offset(adjacentBlock);
					BlockState foundState = structure.world.getBlockState(tempPos);
					Block foundBlock = foundState.getBlock();

					// Check if this block is one that is attached to a facing, if it is, remove it first.
					if (foundBlock instanceof TorchBlock
							|| foundBlock instanceof AbstractSignBlock
							|| foundBlock instanceof LeverBlock
							|| foundBlock instanceof AbstractButtonBlock
							|| foundBlock instanceof BedBlock
							|| foundBlock instanceof CarpetBlock
							|| foundBlock instanceof FlowerPotBlock
							|| foundBlock instanceof SugarCaneBlock
							|| foundBlock instanceof AbstractPressurePlateBlock
							|| foundBlock instanceof DoorBlock
							|| foundBlock instanceof LadderBlock
							|| foundBlock instanceof VineBlock
							|| foundBlock instanceof RedstoneWireBlock
							|| foundBlock instanceof RedstoneDiodeBlock
							|| foundBlock instanceof AbstractBannerBlock
							|| foundBlock instanceof LanternBlock) {
						structure.BeforeClearSpaceBlockReplaced(currentPos);

						if (!(foundBlock instanceof BedBlock)) {
							structure.world.removeBlock(tempPos, false);
						} else if (foundBlock instanceof DoorBlock) {
							// Make sure to remove both parts before going on.
							DoubleBlockHalf currentHalf = foundState.get(BlockStateProperties.DOUBLE_BLOCK_HALF);

							BlockPos otherHalfPos = currentHalf == DoubleBlockHalf.LOWER
									? tempPos.up() : tempPos.down();

							structure.world.setBlockState(tempPos, Blocks.AIR.getDefaultState(), 35);
							structure.world.setBlockState(otherHalfPos, Blocks.AIR.getDefaultState(), 35);

						} else {
							// Found a bed, try to find the other part of the bed and remove it.
							for (Direction currentDirection : Direction.values()) {
								BlockPos bedPos = tempPos.offset(currentDirection);
								BlockState bedState = structure.world.getBlockState(bedPos);

								if (bedState.getBlock() instanceof BedBlock) {
									// found the other part of the bed. Remove the current block and this one.
									structure.world.setBlockState(tempPos, Blocks.AIR.getDefaultState(), 35);
									structure.world.setBlockState(bedPos, Blocks.AIR.getDefaultState(), 35);
									break;
								}
							}
						}
					}
				}

				structure.world.removeBlock(currentPos, false);
			} else {
				// This is just an air block, move onto the next block don't need to wait for the next tick.
				i--;
			}

			return i;
		}

		BuildBlock currentBlock = null;

		if (structure.priorityOneBlocks.size() > 0) {
			currentBlock = structure.priorityOneBlocks.get(0);
			structure.priorityOneBlocks.remove(0);
		} else if (structure.priorityTwoBlocks.size() > 0) {
			currentBlock = structure.priorityTwoBlocks.get(0);
			structure.priorityTwoBlocks.remove(0);
		} else if (structure.airBlocks.size() > 0) {
			currentBlock = structure.airBlocks.get(0);
			structure.airBlocks.remove(0);
			structure.hasAirBlocks = true;
		} else if (structure.priorityThreeBlocks.size() > 0) {
			currentBlock = structure.priorityThreeBlocks.get(0);
			structure.priorityThreeBlocks.remove(0);
		} else {
			// There are no more blocks to set.
			structuresToRemove.add(structure);
			return 999;
		}

		BlockState state = currentBlock.getBlockState();

		BuildingMethods.ReplaceBlock(structure.world, currentBlock.getStartingPosition().getRelativePosition(structure.originalPos,
				structure.getClearSpace().getShape().getDirection(), structure.configuration.houseFacing), state);

		// After placing the initial block, set the sub-block. This needs to happen as the list isn't always in the
		// correct order.
		if (currentBlock.getSubBlock() != null) {
			BuildBlock subBlock = currentBlock.getSubBlock();

			BuildingMethods.ReplaceBlock(structure.world, subBlock.getStartingPosition().getRelativePosition(structure.originalPos,
					structure.getClearSpace().getShape().getDirection(), structure.configuration.houseFacing), subBlock.getBlockState());
		}

		return i;
	}

	private static void removeStructuresFromList(ArrayList<Structure> structuresToRemove, Entry<PlayerEntity, ArrayList<Structure>> entry) {
		for (Structure structure : structuresToRemove) {
			for (BuildTileEntity buildTileEntity : structure.tileEntities) {
				BlockPos tileEntityPos = buildTileEntity.getStartingPosition().getRelativePosition(structure.originalPos,
						structure.getClearSpace().getShape().getDirection(), structure.configuration.houseFacing);
				TileEntity tileEntity = structure.world.getTileEntity(tileEntityPos);
				BlockState tileBlock = structure.world.getBlockState(tileEntityPos);

				if (tileEntity == null) {
					TileEntity.readTileEntity(tileBlock, buildTileEntity.getEntityDataTag());
				} else {
					structure.world.removeTileEntity(tileEntityPos);
					tileEntity = TileEntity.readTileEntity(tileBlock, buildTileEntity.getEntityDataTag());
					structure.world.setTileEntity(tileEntityPos, tileEntity);
					structure.world.getChunkAt(tileEntityPos).markDirty();
					tileEntity.markDirty();
					SUpdateTileEntityPacket packet = tileEntity.getUpdatePacket();

					if (packet != null) {
						structure.world.getServer().getPlayerList().sendPacketToAllPlayers(tileEntity.getUpdatePacket());
					}
				}
			}

			StructureEventHandler.removeWaterLogging(structure);

			for (BuildEntity buildEntity : structure.entities) {
				Optional<EntityType<?>> entityType = EntityType.byKey(buildEntity.getEntityResourceString());

				if (entityType.isPresent()) {
					Entity entity = entityType.get().create(structure.world);

					if (entity != null) {
						CompoundNBT tagCompound = buildEntity.getEntityDataTag();
						BlockPos entityPos = buildEntity.getStartingPosition().getRelativePosition(structure.originalPos,
								structure.getClearSpace().getShape().getDirection(), structure.configuration.houseFacing);

						if (tagCompound != null) {
							if (tagCompound.hasUniqueId("UUID")) {
								tagCompound.putUniqueId("UUID", UUID.randomUUID());
							}

							ListNBT nbttaglist = new ListNBT();
							nbttaglist.add(DoubleNBT.valueOf(entityPos.getX()));
							nbttaglist.add(DoubleNBT.valueOf(entityPos.getY()));
							nbttaglist.add(DoubleNBT.valueOf(entityPos.getZ()));
							tagCompound.put("Pos", nbttaglist);

							entity.read(tagCompound);
						}

						entity.forceSpawn = true;

						// Set item frame facing and rotation here.
						if (entity instanceof ItemFrameEntity) {
							entity = StructureEventHandler.setItemFrameFacingAndRotation((ItemFrameEntity) entity, buildEntity, entityPos, structure);
						} else if (entity instanceof PaintingEntity) {
							entity = StructureEventHandler.setPaintingFacingAndRotation((PaintingEntity) entity, buildEntity, entityPos, structure);
						} else {
							// All other entities
							entity = StructureEventHandler.setEntityFacingAndRotation(entity, buildEntity, entityPos, structure);
						}

						structure.world.addEntity(entity);
					}
				}
			}

			// This structure is done building. Do any post-building operations.
			structure.AfterBuilding(structure.configuration, structure.world, structure.originalPos, structure.assumedNorth, entry.getKey());
			entry.getValue().remove(structure);
		}
	}

	private static void removeWaterLogging(Structure structure) {
		if (structure.hasAirBlocks) {
			for (BlockPos currentPos : structure.allBlockPositions) {
				BlockState currentState = structure.world.getBlockState(currentPos);

				if (currentState.hasProperty(BlockStateProperties.WATERLOGGED)) {
					// This is a water loggable block and there were air blocks, make sure that it's no longer water logged.
					currentState = currentState.with((BlockStateProperties.WATERLOGGED), false);
					structure.world.setBlockState(currentPos, currentState);
				} else if (currentState.getMaterial() == Material.WATER) {
					structure.world.setBlockState(currentPos, Blocks.AIR.getDefaultState());

				}
			}
		}
	}

	private static Entity setPaintingFacingAndRotation(PaintingEntity entity, BuildEntity buildEntity, BlockPos entityPos, Structure structure) {
		float yaw = entity.rotationYaw;
		Rotation rotation = Rotation.NONE;
		double x_axis_offset = buildEntity.entityXAxisOffset;
		double z_axis_offset = buildEntity.entityZAxisOffset;
		Direction facing = entity.getHorizontalFacing();
		double y_axis_offset = buildEntity.entityYAxisOffset * -1;

		if (structure.configuration.houseFacing == structure.assumedNorth.getOpposite()) {
			rotation = Rotation.CLOCKWISE_180;
			x_axis_offset = x_axis_offset * -1;
			z_axis_offset = z_axis_offset * -1;
			facing = facing.getOpposite();
		} else if (structure.configuration.houseFacing == structure.assumedNorth.rotateY()) {
			rotation = Rotation.CLOCKWISE_90;
			x_axis_offset = x_axis_offset * -1;
			z_axis_offset = z_axis_offset * -1;

			if (structure.getClearSpace().getShape().getDirection() == Direction.NORTH) {
				facing = facing.rotateYCCW();
			} else if (structure.getClearSpace().getShape().getDirection() == Direction.SOUTH) {
				facing = facing.rotateY();
			}
		} else if (structure.configuration.houseFacing == structure.assumedNorth.rotateYCCW()) {
			rotation = Rotation.COUNTERCLOCKWISE_90;
			x_axis_offset = x_axis_offset * -1;
			z_axis_offset = z_axis_offset * -1;

			if (structure.getClearSpace().getShape().getDirection() == Direction.NORTH) {
				facing = facing.rotateY();
			} else if (structure.getClearSpace().getShape().getDirection() == Direction.SOUTH) {
				facing = facing.rotateYCCW();
			}
		} else {
			x_axis_offset = 0;
			z_axis_offset = 0;
		}

		if (entity.art.getHeight() > entity.art.getWidth()
				|| entity.art.getHeight() > 16) {
			y_axis_offset--;
		}

		yaw = entity.getRotatedYaw(rotation);

		HangingEntity hangingEntity = entity;
		CompoundNBT compound = new CompoundNBT();
		hangingEntity.writeAdditional(compound);
		compound.putByte("Facing", (byte) facing.getHorizontalIndex());
		hangingEntity.readAdditional(compound);
		StructureEventHandler.updateEntityHangingBoundingBox(hangingEntity);

		entity.setLocationAndAngles(entityPos.getX() + x_axis_offset, entityPos.getY() + y_axis_offset, entityPos.getZ() + z_axis_offset, yaw,
				entity.rotationPitch);

		StructureEventHandler.updateEntityHangingBoundingBox(entity);
		Chunk chunk = structure.world.getChunkAt(entityPos);

		chunk.markDirty();

		return entity;
	}

	private static Entity setItemFrameFacingAndRotation(ItemFrameEntity frame, BuildEntity buildEntity, BlockPos entityPos, Structure structure) {
		float yaw = frame.rotationYaw;
		Rotation rotation = Rotation.NONE;
		double x_axis_offset = buildEntity.entityXAxisOffset;
		double z_axis_offset = buildEntity.entityZAxisOffset;
		Direction facing = frame.getHorizontalFacing();
		double y_axis_offset = buildEntity.entityYAxisOffset;
		x_axis_offset = x_axis_offset * -1;
		z_axis_offset = z_axis_offset * -1;

		if (structure.configuration.houseFacing == structure.assumedNorth.getOpposite()) {
			rotation = Rotation.CLOCKWISE_180;
			facing = facing.getOpposite();
		} else if (structure.configuration.houseFacing == structure.assumedNorth.rotateY()) {
			if (structure.getClearSpace().getShape().getDirection() == Direction.NORTH) {
				rotation = Rotation.CLOCKWISE_90;
				facing = facing.rotateYCCW();
			} else if (structure.getClearSpace().getShape().getDirection() == Direction.SOUTH) {
				facing = facing.rotateY();
				rotation = Rotation.COUNTERCLOCKWISE_90;
			}
		} else if (structure.configuration.houseFacing == structure.assumedNorth.rotateYCCW()) {
			if (structure.getClearSpace().getShape().getDirection() == Direction.NORTH) {
				rotation = Rotation.COUNTERCLOCKWISE_90;
				facing = facing.rotateY();
			} else if (structure.getClearSpace().getShape().getDirection() == Direction.SOUTH) {
				facing = facing.rotateYCCW();
				rotation = Rotation.CLOCKWISE_90;
			}
		} else {
			x_axis_offset = 0;
			z_axis_offset = 0;
		}

		yaw = frame.getRotatedYaw(rotation);

		HangingEntity hangingEntity = frame;
		CompoundNBT compound = new CompoundNBT();
		hangingEntity.writeAdditional(compound);
		compound.putByte("Facing", (byte) facing.getIndex());
		hangingEntity.readAdditional(compound);
		StructureEventHandler.updateEntityHangingBoundingBox(hangingEntity);

		frame.setLocationAndAngles(entityPos.getX() + x_axis_offset, entityPos.getY() + y_axis_offset, entityPos.getZ() + z_axis_offset, yaw,
				frame.rotationPitch);

		StructureEventHandler.updateEntityHangingBoundingBox(frame);
		Chunk chunk = structure.world.getChunkAt(entityPos);

		chunk.markDirty();

		return frame;
	}

	private static Entity setEntityFacingAndRotation(Entity entity, BuildEntity buildEntity, BlockPos entityPos, Structure structure) {
		float yaw = entity.rotationYaw;
		Rotation rotation = Rotation.NONE;
		double x_axis_offset = buildEntity.entityXAxisOffset;
		double z_axis_offset = buildEntity.entityZAxisOffset;
		Direction facing = structure.assumedNorth;
		double y_axis_offset = buildEntity.entityYAxisOffset;

		if (structure.configuration.houseFacing == structure.assumedNorth.getOpposite()) {
			rotation = Rotation.CLOCKWISE_180;
			x_axis_offset = x_axis_offset * -1;
			z_axis_offset = z_axis_offset * -1;
			facing = facing.getOpposite();
		} else if (structure.configuration.houseFacing == structure.assumedNorth.rotateY()) {
			rotation = Rotation.CLOCKWISE_90;
			x_axis_offset = x_axis_offset * -1;
			z_axis_offset = z_axis_offset * -1;
			facing = facing.rotateY();
		} else if (structure.configuration.houseFacing == structure.assumedNorth.rotateYCCW()) {
			rotation = Rotation.COUNTERCLOCKWISE_90;
			x_axis_offset = x_axis_offset * -1;
			z_axis_offset = z_axis_offset * -1;
			facing = facing.rotateYCCW();
		} else {
			x_axis_offset = 0;
			z_axis_offset = 0;
		}

		yaw = entity.getRotatedYaw(rotation);

		entity.setPositionAndRotation(entityPos.getX() + x_axis_offset, entityPos.getY() + y_axis_offset, entityPos.getZ() + z_axis_offset, yaw,
				entity.rotationPitch);

		return entity;
	}

	private static void updateEntityHangingBoundingBox(HangingEntity entity) {
		double d0 = (double) entity.getHangingPosition().getX() + 0.5D;
		double d1 = (double) entity.getHangingPosition().getY() + 0.5D;
		double d2 = (double) entity.getHangingPosition().getZ() + 0.5D;
		double d3 = 0.46875D;
		double d4 = entity.getWidthPixels() % 32 == 0 ? 0.5D : 0.0D;
		double d5 = entity.getHeightPixels() % 32 == 0 ? 0.5D : 0.0D;
		Direction horizontal = entity.getHorizontalFacing();
		d0 = d0 - (double) horizontal.getXOffset() * 0.46875D;
		d2 = d2 - (double) horizontal.getZOffset() * 0.46875D;
		d1 = d1 + d5;
		Direction direction = horizontal == Direction.DOWN || horizontal == Direction.UP ? horizontal.getOpposite() : horizontal.rotateYCCW();
		d0 = d0 + d4 * (double) direction.getXOffset();
		d2 = d2 + d4 * (double) direction.getZOffset();

		// The function call below set the following fields from the "entity" class. posX, posY, posZ.
		// This will probably have to change when the mappings get updated.
		entity.setRawPosition(d0, d1, d2);
		double d6 = entity.getWidthPixels();
		double d7 = entity.getHeightPixels();
		double d8 = entity.getWidthPixels();

		if (horizontal.getAxis() == Direction.Axis.Z) {
			d8 = 1.0D;
		} else {
			d6 = 1.0D;
		}

		d6 = d6 / 32.0D;
		d7 = d7 / 32.0D;
		d8 = d8 / 32.0D;
		entity.setBoundingBox(new AxisAlignedBB(d0 - d6, d1 - d7, d2 - d8, d0 + d6, d1 + d7, d2 + d8));
	}
}
