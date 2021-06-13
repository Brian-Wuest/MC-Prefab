package com.wuest.prefab.Structures.Events;

import com.wuest.prefab.Config.EntityPlayerConfiguration;
import com.wuest.prefab.ModRegistry;
import com.wuest.prefab.Prefab;
import com.wuest.prefab.Proxy.Messages.PlayerEntityTagMessage;
import com.wuest.prefab.Structures.Base.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityHanging;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.item.EntityPainting;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ServerTickEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.UUID;

/**
 * This is the structure event hander.
 * 
 * @author WuestMan
 */
@EventBusSubscriber(value =
{ Side.SERVER, Side.CLIENT })
public class StructureEventHandler
{
	/**
	 * Contains a hashmap for the structures to build and for whom.
	 */
	public static HashMap<EntityPlayer, ArrayList<Structure>> structuresToBuild = new HashMap<EntityPlayer, ArrayList<Structure>>();

	/**
	 * This event is used to determine if the player should be given the starting house item when they log in.
	 * 
	 * @param event The event object.
	 */
	@SubscribeEvent
	public static void PlayerLoggedIn(PlayerLoggedInEvent event)
	{
		if (!event.player.world.isRemote && event.player instanceof EntityPlayerMP)
		{
			EntityPlayerMP player = (EntityPlayerMP) event.player;
			EntityPlayerConfiguration playerConfig = EntityPlayerConfiguration.loadFromEntityData(player);

			String startingItem = Prefab.proxy.proxyConfiguration.startingItem;

			if (!playerConfig.givenHouseBuilder && startingItem != null)
			{
				ItemStack stack = ItemStack.EMPTY;

				switch (startingItem.toLowerCase())
				{
					case "structure part":
					{
						stack = new ItemStack(ModRegistry.StructurePart);
						break;
					}

					case "starting house":
					{
						stack = new ItemStack(ModRegistry.StartHouse);
						break;
					}

					case "moderate house":
					{
						stack = new ItemStack(ModRegistry.ModerateHouse);
						break;
					}
				}

				if (!stack.isEmpty())
				{
					System.out.println(player.getDisplayNameString() + " joined the game for the first time. Giving them starting item.");
					
					player.inventory.addItemStackToInventory(stack);
					player.inventoryContainer.detectAndSendChanges();

					// Make sure to set the tag for this player so they don't get the item again.
					playerConfig.givenHouseBuilder = true;
					playerConfig.saveToPlayer(player);
				}
			}

			// Send the persist tag to the client.
			Prefab.network.sendTo(new PlayerEntityTagMessage(playerConfig.getModIsPlayerNewTag(player)), player);
		}
	}

	/**
	 * This event is primarily used to build 100 blocks for any queued structures for all players.
	 * 
	 * @param event The event object.
	 */
	@SubscribeEvent
	public static void onServerTick(ServerTickEvent event)
	{
		ArrayList<EntityPlayer> playersToRemove = new ArrayList<EntityPlayer>();

		for (Entry<EntityPlayer, ArrayList<Structure>> entry : StructureEventHandler.structuresToBuild.entrySet())
		{
			ArrayList<Structure> structuresToRemove = new ArrayList<Structure>();
			EntityPlayer player = entry.getKey();

			// Build the first 100 blocks of each structure for this player.
			for (Structure structure : entry.getValue())
			{
				for (int i = 0; i < 100; i++)
				{
					i = StructureEventHandler.setBlock(i, structure, structuresToRemove);
				}
			}

			// Update the list of structures to remove this structure since it's done building.
			StructureEventHandler.removeStructuresFromList(structuresToRemove, entry);

			if (entry.getValue().size() == 0)
			{
				playersToRemove.add(entry.getKey());
			}
		}

		// Remove each player that has their structure's built.
		for (EntityPlayer player : playersToRemove)
		{
			StructureEventHandler.structuresToBuild.remove(player);
		}
	}

	/**
	 * This occurs when a player dies and is used to make sure that a player does not get a duplicate starting house.
	 * 
	 * @param event
	 */
	@SubscribeEvent
	public static void onClone(PlayerEvent.Clone event)
	{
		if (event.getEntityPlayer() instanceof EntityPlayerMP)
		{
			// Don't add the tag unless the house item was added. This way it can be added if the feature is turned on.
			// When the player is cloned, make sure to copy the tag. If this is not done the item can be given to the
			// player again if they die before the log out and log back in.
			NBTTagCompound originalTag = event.getOriginal().getEntityData();

			// Use the server configuration to determine if the house should be added for this player.
			String startingItem = Prefab.proxy.proxyConfiguration.startingItem;
			if (startingItem != null && !startingItem.equalsIgnoreCase("Nothing"))
			{
				if (originalTag.hasKey(EntityPlayerConfiguration.PLAYER_ENTITY_TAG))
				{
					NBTTagCompound newPlayerTag = event.getEntityPlayer().getEntityData();
					newPlayerTag.setTag(EntityPlayerConfiguration.PLAYER_ENTITY_TAG, originalTag.getTag(EntityPlayerConfiguration.PLAYER_ENTITY_TAG));

					// Send the persist tag to the client.
					Prefab.network.sendTo(new PlayerEntityTagMessage(originalTag.getCompoundTag(EntityPlayerConfiguration.PLAYER_ENTITY_TAG)),
						(EntityPlayerMP) event.getEntityPlayer());
				}
			}
		}
	}

	private static int setBlock(int i, Structure structure, ArrayList<Structure> structuresToRemove)
	{
		// Structure clearing happens before anything else.
		if (structure.clearedBlockPos.size() > 0)
		{
			BlockPos currentPos = structure.clearedBlockPos.get(0);
			structure.clearedBlockPos.remove(0);

			IBlockState clearBlockState = structure.world.getBlockState(currentPos);

			// If this block is not specifically air then set it to air.
			// This will also break other mod's logic blocks but they would probably be broken due to structure
			// generation anyways.
			if (clearBlockState.getBlock() != Blocks.AIR)
			{
				structure.BeforeClearSpaceBlockReplaced(currentPos);
				structure.world.setBlockToAir(currentPos);
			}
			else
			{
				// This is just an air block, move onto the next block don't need to wait for the next tick.
				i--;
			}

			return i;
		}

		BuildBlock currentBlock = null;

		if (structure.priorityOneBlocks.size() > 0)
		{
			currentBlock = structure.priorityOneBlocks.get(0);
			structure.priorityOneBlocks.remove(0);
		}
		else if (structure.priorityTwoBlocks.size() > 0)
		{
			currentBlock = structure.priorityTwoBlocks.get(0);
			structure.priorityTwoBlocks.remove(0);
		}
		else if (structure.priorityThreeBlocks.size() > 0)
		{
			currentBlock = structure.priorityThreeBlocks.get(0);
			structure.priorityThreeBlocks.remove(0);
		}
		else
		{
			// There are no more blocks to set.
			structuresToRemove.add(structure);
			return 999;
		}

		IBlockState state = currentBlock.getBlockState();

		BuildingMethods.ReplaceBlock(structure.world, currentBlock.getStartingPosition().getRelativePosition(structure.originalPos,
			structure.getClearSpace().getShape().getDirection(), structure.configuration.houseFacing), state);

		// After placing the initial block, set the sub-block. This needs to happen as the list isn't always in the
		// correct order.
		if (currentBlock.getSubBlock() != null)
		{
			BuildBlock subBlock = currentBlock.getSubBlock();

			BuildingMethods.ReplaceBlock(structure.world, subBlock.getStartingPosition().getRelativePosition(structure.originalPos,
				structure.getClearSpace().getShape().getDirection(), structure.configuration.houseFacing), subBlock.getBlockState());
		}

		return i;
	}

	private static void removeStructuresFromList(ArrayList<Structure> structuresToRemove, Entry<EntityPlayer, ArrayList<Structure>> entry)
	{
		for (Structure structure : structuresToRemove)
		{
			for (BuildTileEntity buildTileEntity : structure.tileEntities)
			{
				BlockPos tileEntityPos = buildTileEntity.getStartingPosition().getRelativePosition(structure.originalPos,
					structure.getClearSpace().getShape().getDirection(), structure.configuration.houseFacing);
				TileEntity tileEntity = structure.world.getTileEntity(tileEntityPos);

				if (tileEntity == null)
				{
					TileEntity.create(structure.world, buildTileEntity.getEntityDataTag());
				}
				else
				{
					structure.world.removeTileEntity(tileEntityPos);
					tileEntity = TileEntity.create(structure.world, buildTileEntity.getEntityDataTag());
					structure.world.setTileEntity(tileEntityPos, tileEntity);
					structure.world.getChunk(tileEntityPos).markDirty();
					tileEntity.markDirty();
					SPacketUpdateTileEntity packet = tileEntity.getUpdatePacket();

					if (packet != null)
					{
						structure.world.getMinecraftServer().getPlayerList().sendPacketToAllPlayers(tileEntity.getUpdatePacket());
					}
				}
			}

			for (BuildEntity buildEntity : structure.entities)
			{
				Entity entity = EntityList.createEntityByIDFromName(buildEntity.getEntityResource(), structure.world);
				NBTTagCompound tagCompound = buildEntity.getEntityDataTag();
				BlockPos entityPos = buildEntity.getStartingPosition().getRelativePosition(structure.originalPos,
					structure.getClearSpace().getShape().getDirection(), structure.configuration.houseFacing);

				if (tagCompound != null)
				{
					if (tagCompound.hasUniqueId("UUID"))
					{
						tagCompound.setUniqueId("UUID", UUID.randomUUID());
					}
					
	                NBTTagList nbttaglist = new NBTTagList();
	                nbttaglist.appendTag(new NBTTagDouble(entityPos.getX()));
	                nbttaglist.appendTag(new NBTTagDouble(entityPos.getY()));
	                nbttaglist.appendTag(new NBTTagDouble(entityPos.getZ()));
	                tagCompound.setTag("Pos", nbttaglist);

					entity.readFromNBT(tagCompound);
				}

				entity.forceSpawn = true;
				
				// Set item frame facing and rotation here.
				if (entity instanceof EntityItemFrame)
				{
					entity = StructureEventHandler.setItemFrameFacingAndRotation((EntityItemFrame)entity, buildEntity, entityPos, structure);
				}
				else if (entity instanceof EntityPainting)
				{
					entity = StructureEventHandler.setPaintingFacingAndRotation((EntityPainting)entity, buildEntity, entityPos, structure);
				}
				else
				{
					// All other entities
					entity = StructureEventHandler.setEntityFacingAndRotation(entity, buildEntity, entityPos, structure);
				}
					
				structure.world.spawnEntity(entity);
			}

			// This structure is done building. Do any post-building operations.
			structure.AfterBuilding(structure.configuration, structure.world, structure.originalPos, structure.assumedNorth, entry.getKey());
			entry.getValue().remove(structure);
		}
	}

	private static Entity setPaintingFacingAndRotation(EntityPainting entity, BuildEntity buildEntity, BlockPos entityPos, Structure structure)
	{
		float yaw = entity.rotationYaw;
		Rotation rotation = Rotation.NONE;
		double x_axis_offset = buildEntity.entityXAxisOffset;
		double z_axis_offset = buildEntity.entityZAxisOffset;
		EnumFacing facing =  entity.facingDirection;
		double y_axis_offset = buildEntity.entityYAxisOffset * -1;

		if (structure.configuration.houseFacing == structure.assumedNorth.getOpposite())
		{
			rotation = Rotation.CLOCKWISE_180;
			x_axis_offset = x_axis_offset * -1;
			z_axis_offset = z_axis_offset * -1;
			facing = facing.getOpposite();
		}
		else if (structure.configuration.houseFacing == structure.assumedNorth.rotateY())
		{
			rotation = rotation.CLOCKWISE_90;
			x_axis_offset = x_axis_offset * -1;
			z_axis_offset = z_axis_offset * -1;
			
			if (structure.getClearSpace().getShape().getDirection() == EnumFacing.NORTH)
			{
				facing = facing.rotateYCCW();	
			}
			else if (structure.getClearSpace().getShape().getDirection() == EnumFacing.SOUTH)
			{
				facing = facing.rotateY();
			}
		}
		else if (structure.configuration.houseFacing == structure.assumedNorth.rotateYCCW())
		{
			rotation = rotation.COUNTERCLOCKWISE_90;
			x_axis_offset = x_axis_offset * -1;
			z_axis_offset = z_axis_offset * -1;
			
			if (structure.getClearSpace().getShape().getDirection() == EnumFacing.NORTH)
			{
				facing = facing.rotateY();
			}
			else if (structure.getClearSpace().getShape().getDirection() == EnumFacing.SOUTH)
			{
				facing = facing.rotateYCCW();	
			}
		}
		else
		{
			x_axis_offset = 0;
			z_axis_offset = 0;
		}
		
		if (entity.art.sizeY > entity.art.sizeX
			|| entity.art.sizeY > 16)
		{
			y_axis_offset--;
		}

		yaw = entity.getRotatedYaw(rotation);

		((EntityHanging) entity).facingDirection = facing;
		StructureEventHandler.updateEntityHangingBoundingBox((EntityHanging) entity);

		entity.setLocationAndAngles(entityPos.getX() + x_axis_offset, entityPos.getY() + y_axis_offset, entityPos.getZ() + z_axis_offset, yaw,
			entity.rotationPitch);

		StructureEventHandler.updateEntityHangingBoundingBox((EntityHanging) entity);
		Chunk chunk = structure.world.getChunk(entityPos);

		chunk.markDirty();
		
		return entity;
	}
	
	private static Entity setItemFrameFacingAndRotation(EntityItemFrame frame, BuildEntity buildEntity, BlockPos entityPos, Structure structure)
	{
		float yaw = frame.rotationYaw;
		Rotation rotation = Rotation.NONE;
		double x_axis_offset = buildEntity.entityXAxisOffset;
		double z_axis_offset = buildEntity.entityZAxisOffset;
		EnumFacing facing =  frame.facingDirection;
		double y_axis_offset = buildEntity.entityYAxisOffset;
		x_axis_offset = x_axis_offset * -1;
		z_axis_offset = z_axis_offset * -1;
		
		if (structure.configuration.houseFacing == structure.assumedNorth.getOpposite())
		{
			rotation = Rotation.CLOCKWISE_180;
			facing = facing.getOpposite();
		}
		else if (structure.configuration.houseFacing == structure.assumedNorth.rotateY())
		{	
			if (structure.getClearSpace().getShape().getDirection() == EnumFacing.NORTH)
			{
				rotation = rotation.CLOCKWISE_90;
				facing = facing.rotateYCCW();	
			}
			else if (structure.getClearSpace().getShape().getDirection() == EnumFacing.SOUTH)
			{
				facing = facing.rotateY();
				rotation = rotation.COUNTERCLOCKWISE_90;
			}
		}
		else if (structure.configuration.houseFacing == structure.assumedNorth.rotateYCCW())
		{			
			if (structure.getClearSpace().getShape().getDirection() == EnumFacing.NORTH)
			{
				rotation = rotation.COUNTERCLOCKWISE_90;
				facing = facing.rotateY();
			}
			else if (structure.getClearSpace().getShape().getDirection() == EnumFacing.SOUTH)
			{
				facing = facing.rotateYCCW();
				rotation = rotation.CLOCKWISE_90;
			}
		}
		else
		{
			x_axis_offset = 0;
			z_axis_offset = 0;
		}

		yaw = frame.getRotatedYaw(rotation);

		((EntityHanging) frame).facingDirection = facing;
		StructureEventHandler.updateEntityHangingBoundingBox((EntityHanging) frame);

		frame.setLocationAndAngles(entityPos.getX() + x_axis_offset, entityPos.getY() + y_axis_offset, entityPos.getZ() + z_axis_offset, yaw,
			frame.rotationPitch);

		StructureEventHandler.updateEntityHangingBoundingBox((EntityHanging) frame);
		Chunk chunk = structure.world.getChunk(entityPos);

		chunk.markDirty();
		
		return frame;
	}

	private static Entity setEntityFacingAndRotation(Entity entity, BuildEntity buildEntity, BlockPos entityPos, Structure structure)
	{
		float yaw = entity.rotationYaw;
		Rotation rotation = Rotation.NONE;
		double x_axis_offset = buildEntity.entityXAxisOffset;
		double z_axis_offset = buildEntity.entityZAxisOffset;
		EnumFacing facing = structure.assumedNorth;
		double y_axis_offset = buildEntity.entityYAxisOffset;

		if (structure.configuration.houseFacing == structure.assumedNorth.getOpposite())
		{
			rotation = Rotation.CLOCKWISE_180;
			x_axis_offset = x_axis_offset * -1;
			z_axis_offset = z_axis_offset * -1;
			facing = facing.getOpposite();
		}
		else if (structure.configuration.houseFacing == structure.assumedNorth.rotateY())
		{
			rotation = rotation.CLOCKWISE_90;
			x_axis_offset = x_axis_offset * -1;
			z_axis_offset = z_axis_offset * -1;
			facing = facing.rotateY();
		}
		else if (structure.configuration.houseFacing == structure.assumedNorth.rotateYCCW())
		{
			rotation = rotation.COUNTERCLOCKWISE_90;
			x_axis_offset = x_axis_offset * -1;
			z_axis_offset = z_axis_offset * -1;
			facing = facing.rotateYCCW();
		}
		else
		{
			x_axis_offset = 0;
			z_axis_offset = 0;
		}

		yaw = entity.getRotatedYaw(rotation);

		entity.setPositionAndRotation(entityPos.getX() + x_axis_offset, entityPos.getY() + y_axis_offset, entityPos.getZ() + z_axis_offset, yaw,
			entity.rotationPitch);
		
		return entity;
	}

	private static void updateEntityHangingBoundingBox(EntityHanging entity)
	{
		double d0 = (double) entity.getHangingPosition().getX() + 0.5D;
		double d1 = (double) entity.getHangingPosition().getY() + 0.5D;
		double d2 = (double) entity.getHangingPosition().getZ() + 0.5D;
		double d3 = 0.46875D;
		double d4 = entity.getWidthPixels() % 32 == 0 ? 0.5D : 0.0D;
		;
		double d5 = entity.getHeightPixels() % 32 == 0 ? 0.5D : 0.0D;
		;
		d0 = d0 - (double) entity.facingDirection.getXOffset() * 0.46875D;
		d2 = d2 - (double) entity.facingDirection.getZOffset() * 0.46875D;
		d1 = d1 + d5;
		EnumFacing enumfacing = entity.facingDirection.rotateYCCW();
		d0 = d0 + d4 * (double) enumfacing.getXOffset();
		d2 = d2 + d4 * (double) enumfacing.getZOffset();
		entity.posX = d0;
		entity.posY = d1;
		entity.posZ = d2;
		double d6 = (double) entity.getWidthPixels();
		double d7 = (double) entity.getHeightPixels();
		double d8 = (double) entity.getWidthPixels();

		if (entity.facingDirection.getAxis() == EnumFacing.Axis.Z)
		{
			d8 = 1.0D;
		}
		else
		{
			d6 = 1.0D;
		}

		d6 = d6 / 32.0D;
		d7 = d7 / 32.0D;
		d8 = d8 / 32.0D;
		entity.setEntityBoundingBox(new AxisAlignedBB(d0 - d6, d1 - d7, d2 - d8, d0 + d6, d1 + d7, d2 + d8));
	}
}
