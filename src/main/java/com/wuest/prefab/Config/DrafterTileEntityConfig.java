package com.wuest.prefab.Config;

import java.util.AbstractMap.SimpleEntry;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.wuest.prefab.ModRegistry;
import com.wuest.prefab.Base.BaseConfig;
import com.wuest.prefab.Blocks.BlockCompressedStone;
import com.wuest.prefab.Config.ModConfiguration.WallBlockType;

import net.minecraft.init.Items;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;

/**
 * This is the configuration class for the drafter tile entity. This is what will be saved to NBTTag data.
 * @author WuestMan
 *
 */
public class DrafterTileEntityConfig extends BaseConfig
{
	public RoomInfo[] Basement2FloorRooms;
	public RoomInfo[] BasementFloorRooms; 
	public RoomInfo[] FirstFloorRooms;
	public RoomInfo[] SecondFloorRooms;
	public RoomInfo[] ThirdFloorRooms;
	public ArrayList<SimpleEntry<RoomInfo, AvailableRoomType>> PendingChanges;
	
	public BlockPos pos;
	
	static
	{
		DrafterTileEntityConfig.InitializeMaterials();
	}
	
	/**
	 * This method is used to initialize the materials needed for specific room types. 
	 */
	static void InitializeMaterials()
	{
		// Set the materials for the field.
		AvailableRoomType.Field.getRoomMaterials().add(new RoomMaterial(ModRegistry.CompressedStoneBlock().getRegistryName(), BlockCompressedStone.EnumType.COMPRESSED_DIRT.getMetadata(), 4));
		
		// Set the materials for the farmland.
		AvailableRoomType.FarmLand.getRoomMaterials().add(new RoomMaterial(ModRegistry.CompressedStoneBlock().getRegistryName(), BlockCompressedStone.EnumType.COMPRESSED_DIRT.getMetadata(), 4));
		AvailableRoomType.FarmLand.getRoomMaterials().add(new RoomMaterial(Items.WATER_BUCKET.getRegistryName(), -1, 3));
		
		// Set the materials for the Plain stone room.
		AvailableRoomType.PlainStoneRoom.getRoomMaterials().add(new RoomMaterial(ModRegistry.CompressedStoneBlock().getRegistryName(), BlockCompressedStone.EnumType.COMPRESSED_STONE.getMetadata(), 12));
		
		// Set the materials for the tree farm.
		AvailableRoomType.TreeFarm.getRoomMaterials().add(new RoomMaterial(ModRegistry.CompressedStoneBlock().getRegistryName(), BlockCompressedStone.EnumType.COMPRESSED_DIRT.getMetadata(), 4));
		AvailableRoomType.TreeFarm.getRoomMaterials().add(new RoomMaterial(Items.WATER_BUCKET.getRegistryName(), -1, 3));
		AvailableRoomType.TreeFarm.getRoomMaterials().add(new RoomMaterial(ModRegistry.BundleOfTimber().getRegistryName(), -1, 2));
	}
	
	public DrafterTileEntityConfig()
	{
		super();
		this.Initialize();
	}

	@Override
	public void WriteToNBTCompound(NBTTagCompound compound)
	{
		NBTTagCompound drafter = new NBTTagCompound();
		
		NBTTagCompound basementRooms = new NBTTagCompound();
		
		for (RoomInfo info : this.BasementFloorRooms)
		{
			if (info != null)
			{
				info.WriteToNBTTagCompound(basementRooms);
			}
		}
		
		drafter.setTag("basement_rooms", basementRooms);
		
		NBTTagCompound basement2Rooms = new NBTTagCompound();
		
		for (RoomInfo info: this.Basement2FloorRooms)
		{
			if (info != null)
			{
				info.WriteToNBTTagCompound(basement2Rooms);
			}
		}
		
		drafter.setTag("basement2_rooms", basement2Rooms);
		
		NBTTagCompound firstFloorRooms = new NBTTagCompound();
		
		for (RoomInfo info : this.FirstFloorRooms)
		{
			if (info != null)
			{
				info.WriteToNBTTagCompound(firstFloorRooms);
			}
		}
		
		drafter.setTag("first_rooms", firstFloorRooms);
		
		NBTTagCompound secondFloorRooms = new NBTTagCompound();
		
		for (RoomInfo info : this.SecondFloorRooms)
		{
			if (info != null)
			{
				info.WriteToNBTTagCompound(secondFloorRooms);
			}
		}
		
		drafter.setTag("second_rooms", secondFloorRooms);
		
		NBTTagCompound thirdFloorRooms = new NBTTagCompound();
		
		for (RoomInfo info : this.ThirdFloorRooms)
		{
			if (info != null)
			{
				info.WriteToNBTTagCompound(thirdFloorRooms);
			}
		}
		
		drafter.setTag("third_rooms", thirdFloorRooms);
		
		if (this.pos != null)
		{
			drafter.setInteger("x", this.pos.getX());
			drafter.setInteger("y", this.pos.getY());
			drafter.setInteger("z", this.pos.getZ());
		}
		
		
		if (this.PendingChanges.size() > 0)
		{
			NBTTagCompound pendingChanges = new NBTTagCompound();
			
			for (SimpleEntry<RoomInfo, AvailableRoomType> entry : this.PendingChanges)
			{
				NBTTagCompound pendingChange = new NBTTagCompound();
				entry.getKey().WriteToNBTTagCompound(pendingChange);
				pendingChange.setString("availableRoomType", entry.getValue().getName());
				
				pendingChanges.setTag(((Integer)entry.getKey().ID).toString(), pendingChange);
			}
			
			compound.setTag("pendingChanges", pendingChanges);
		}
		
		compound.setTag("drafter", drafter);
	}

	@Override
	public DrafterTileEntityConfig ReadFromNBTTagCompound(NBTTagCompound compound)
	{
		DrafterTileEntityConfig config = new DrafterTileEntityConfig();
		
		if (compound.hasKey("drafter"))
		{
			NBTTagCompound drafter = compound.getCompoundTag("drafter");
		
			if (drafter.hasKey("x"))
			{
				this.pos = new BlockPos(drafter.getInteger("x"), drafter.getInteger("y"), drafter.getInteger("z"));
			}
			
			if (drafter.hasKey("basement_rooms"))
			{
				NBTTagCompound basement_rooms = (NBTTagCompound)drafter.getTag("basement_rooms");
				this.createRoomsFromTag(basement_rooms, this.BasementFloorRooms);
			}
			
			if (drafter.hasKey("basement2_rooms"))
			{
				NBTTagCompound basement2_rooms = (NBTTagCompound)drafter.getTag("basement2_rooms");
				this.createRoomsFromTag(basement2_rooms, this.Basement2FloorRooms);
			}
			
			if (drafter.hasKey("first_rooms"))
			{
				NBTTagCompound first_rooms = (NBTTagCompound)drafter.getTag("first_rooms");
				this.createRoomsFromTag(first_rooms, this.FirstFloorRooms);
			}
			
			if (drafter.hasKey("second_rooms"))
			{
				NBTTagCompound second_rooms = (NBTTagCompound)drafter.getTag("second_rooms");
				this.createRoomsFromTag(second_rooms, this.SecondFloorRooms);
			}
			
			if (drafter.hasKey("third_rooms"))
			{
				NBTTagCompound third_rooms = (NBTTagCompound)drafter.getTag("third_rooms");
				this.createRoomsFromTag(third_rooms, this.ThirdFloorRooms);
			}
			
			if (drafter.hasKey("pendingChanges"))
			{
				NBTTagCompound pendingChanges = drafter.getCompoundTag("pendingChanges");
				
				for (String key : pendingChanges.getKeySet())
				{
					NBTTagCompound pendingChange = compound.getCompoundTag(key);
					AvailableRoomType roomType = AvailableRoomType.Empty;
					RoomInfo roomInfo = null;
					
					for (String pendingChangeKey : pendingChange.getKeySet())
					{
						// This is the pairing.
						if (pendingChangeKey.equals("availableRoomType"))
						{
							roomType = AvailableRoomType.ValueOf(pendingChange.getString(pendingChangeKey));
						}
						else
						{
							roomInfo = RoomInfo.CreateFromNBTTag(pendingChange.getCompoundTag(pendingChangeKey));
						}
					}
					
					if (roomInfo != null && roomType != AvailableRoomType.Empty)
					{
						this.AddUpdatePendingChange(roomInfo, roomType);
					}
				}
			}
		}
		
		return config;
	}
	
	public void Initialize()
	{
		this.FirstFloorRooms = new RoomInfo[49];
		this.InitializeArray(this.FirstFloorRooms);
		
		this.BasementFloorRooms = new RoomInfo[49];
		this.InitializeArray(this.BasementFloorRooms);
		
		this.SecondFloorRooms = new RoomInfo[49];
		this.InitializeArray(this.SecondFloorRooms);
		
		this.Basement2FloorRooms = new RoomInfo[49];
		this.InitializeArray(this.Basement2FloorRooms);
		
		this.ThirdFloorRooms = new RoomInfo[49];
		this.InitializeArray(this.ThirdFloorRooms);
		this.PendingChanges = new ArrayList<SimpleEntry<RoomInfo, AvailableRoomType>>();
	}
	
	/**
	 * This method is used to add/update a pending change record.
	 * @param roomInfo The room information for the pending change.
	 * @param roomType The room type of this pending change.
	 */
	public void AddUpdatePendingChange(RoomInfo roomInfo, AvailableRoomType roomType)
	{
		boolean updatedEntry = false;
		
		for (SimpleEntry<RoomInfo, AvailableRoomType> entry : this.PendingChanges)
		{
			if (roomInfo.ID == entry.getKey().ID)
			{
				// Same key, update the value.
				entry.setValue(roomType);
				updatedEntry = true;
				break;
			}
		}
		
		if (!updatedEntry)
		{
			this.PendingChanges.add(new SimpleEntry<RoomInfo, AvailableRoomType>(roomInfo, roomType));
		}
	}
	
	/**
	 * Removes a pending change from the list.
	 * @param roomInfo The room information to remove from the pending change list.
	 */
	public void RemovePendingChange(RoomInfo roomInfo)
	{
		SimpleEntry<RoomInfo, AvailableRoomType> entryToRemove = null;
		
		for (SimpleEntry<RoomInfo, AvailableRoomType> entry : this.PendingChanges)
		{
			if (roomInfo.ID == entry.getKey().ID)
			{
				entryToRemove = entry;
				break;
			}
		}
		
		if (entryToRemove != null)
		{
			this.PendingChanges.remove(entryToRemove);
		}
	}
	
	protected void createRoomsFromTag(NBTTagCompound compound, RoomInfo[] rooms)
	{
		for (String key : compound.getKeySet())
		{
			if (key.startsWith("room"))
			{
				NBTTagCompound roomTag = compound.getCompoundTag(key);
				RoomInfo newRoom = RoomInfo.CreateFromNBTTag(roomTag);
				
				if (newRoom.ID != -1)
				{
					rooms[newRoom.ID] = newRoom;
				}
			}
		}
	}
	
	protected void InitializeArray(RoomInfo[] roomInfo)
	{
		for (int i = 0; i < roomInfo.length; i++)
		{
			RoomInfo info = new RoomInfo(i);
			roomInfo[i] = info;
		}
	}
	
	/**
	 * This class is used to contain specific information about a room.
	 * @author WuestMan
	 *
	 */
	public static class RoomInfo
	{
		public int ID = -1;
		public AvailableRoomType StructureName;
		public EnumFacing StructureFacing;
		public BlockPos RoomCoordinates;
		public boolean PendingBuilding; 
		public int FloorNumber;
		
		/**
		 * Initializes a new instance of the RoomInfo class.
		 */
		public RoomInfo(int id)
		{
			this.ID = id;
			this.StructureFacing = EnumFacing.NORTH;
			this.RoomCoordinates = new BlockPos(0,0,0);
			this.StructureName = AvailableRoomType.Empty;
			this.PendingBuilding = false;
			this.FloorNumber = 0;
		}
		
		public static RoomInfo CreateFromNBTTag(NBTTagCompound compound)
		{
			if (compound.hasKey("ID"))
			{
				RoomInfo info = new RoomInfo(compound.getInteger("ID"));
				
				String roomName = compound.getString("name");
				info.StructureName = AvailableRoomType.ValueOf(compound.getString("name"));
				info.StructureFacing = EnumFacing.byName(compound.getString("facing"));
				info.RoomCoordinates = new BlockPos(compound.getInteger("x"), compound.getInteger("y"), compound.getInteger("z"));
				info.PendingBuilding = compound.getBoolean("pendingBuilding");
				info.FloorNumber = compound.getInteger("floor_number");
				
				return info;
			}
			
			return null;
		}
		
		public void WriteToNBTTagCompound(NBTTagCompound compound)
		{
			NBTTagCompound tag = new NBTTagCompound();
			
			tag.setInteger("ID", this.ID);
			tag.setString("name", this.StructureName.getName());
			tag.setString("facing", this.StructureFacing.getName());
			tag.setInteger("x", this.RoomCoordinates.getX());
			tag.setInteger("y", this.RoomCoordinates.getY());
			tag.setInteger("z", this.RoomCoordinates.getZ());
			tag.setBoolean("pendingBuilding", this.PendingBuilding);
			tag.setInteger("floor_number", this.FloorNumber);
			
			compound.setTag("room_" + this.ID, tag);
		}
	
		public boolean checkNeighbors(RoomInfo[] roomInfoArray)
		{
			String emptyRoomName = AvailableRoomType.Empty.getName();
			
			if (this.StructureName.getName().equals(emptyRoomName))
			{
				RoomInfo roomToTheLeft = this.getRoomInfoForID(roomInfoArray, this.ID - 1);
				RoomInfo roomToTheRight = this.getRoomInfoForID(roomInfoArray, this.ID + 1);
				RoomInfo roomAbove = this.getRoomInfoForID(roomInfoArray, this.ID - 7);
				RoomInfo roomBelow = this.getRoomInfoForID(roomInfoArray, this.ID + 7);
				
				// Make sure that at least 1 neighbor is not empty.
				if ((roomToTheLeft != null && !roomToTheLeft.StructureName.getName().equals(emptyRoomName))
						|| (roomToTheRight != null && !roomToTheRight.StructureName.getName().equals(emptyRoomName))
						|| (roomAbove != null && !roomAbove.StructureName.getName().equals(emptyRoomName))
						|| (roomBelow != null && !roomBelow.StructureName.getName().equals(emptyRoomName)))
				{
					return true;
				}
			}
			else
			{
				return true;
			}
			
			return false;
		}
		
		protected RoomInfo getRoomInfoForID(RoomInfo[] roomInfoArray, int id)
		{
			if (id < 0 || id >=49)
			{
				return null;
			}
			else
			{
				return roomInfoArray[id];
			}
		}
	}
	
	public static class RoomMaterial
	{
		public ResourceLocation resourceLocation;
		public int metaData;
		public int numberRequired;
		
		public RoomMaterial(ResourceLocation resourceLocation, int metaData, int numberRequried)
		{
			this.resourceLocation = resourceLocation;
			this.metaData = metaData;
			this.numberRequired = numberRequried;
		}
	}

	public enum AvailableRoomType
	{
		Empty(-2, "Empty"),
		
		Foyer(-1, "Foyer"),
	
		/**
		 * This is a regular flat field.
		 */
		Field(0, "Field"),
		
		FarmLand(1, "Farm Land"),
		
		PlainStoneRoom(2, "Plain Stone Room"),
		
		TreeFarm(3, "Tree Farm");
		
		private final int key;
		private final String name;
		private ArrayList<RoomMaterial> roomMaterials;
		
		AvailableRoomType(int key, String name)
		{
			this.key = key;
			this.name = name;
			this.roomMaterials = new ArrayList<RoomMaterial>();
		}
		
		/**
		 * Gets the key associated with this available room.
		 * @return The integer representing the key for this room.
		 */
		public int getKey()
		{
			return this.key;
		}
		
		/**
		 * Returns the name of this room.
		 * @return The name of this room.
		 */
		public String getName()
		{
			return this.name;
		}
		
		public static ArrayList<AvailableRoomType> getValues()
		{
			ArrayList<AvailableRoomType> roomTypes = new ArrayList<AvailableRoomType>();
			
			for (AvailableRoomType roomType : AvailableRoomType.values())
			{
				if (roomType == AvailableRoomType.Empty
						|| roomType == Foyer)
				{
					continue;
				}
				
				roomTypes.add(roomType);
			}
			
			return roomTypes;
		}
		
		/**
		 * Get the materials for this room.
		 * @return The required materials for this room.
		 */
		public ArrayList<RoomMaterial> getRoomMaterials()
		{
			return this.roomMaterials;
		}
		
		public static AvailableRoomType ValueOf(int value)
		{
			for (AvailableRoomType room : AvailableRoomType.values())
			{
				if (value == room.getKey())
				{
					return room;
				}
			}
			
			return AvailableRoomType.Empty;
		}
		
		public static AvailableRoomType ValueOf(String roomName)
		{
			for (AvailableRoomType room : AvailableRoomType.values())
			{
				if (roomName.equals(room.name))
				{
					return room;
				}
			}
			
			return AvailableRoomType.Empty;
		}
	}

}