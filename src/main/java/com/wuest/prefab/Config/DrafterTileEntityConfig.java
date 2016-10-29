package com.wuest.prefab.Config;

import com.wuest.prefab.Base.BaseConfig;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

/**
 * This is the configuration class for the drafter tile entity. This is what will be saved to NBTTag data.
 * @author WuestMan
 *
 */
public class DrafterTileEntityConfig extends BaseConfig
{
	public RoomInfo[] BasementFloorRooms; 
	public RoomInfo[] FirstFloorRooms;
	public RoomInfo[] SecondFloorRooms;
	public BlockPos pos;
	
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
		
		if (this.pos != null)
		{
			drafter.setInteger("x", this.pos.getX());
			drafter.setInteger("y", this.pos.getY());
			drafter.setInteger("z", this.pos.getZ());
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
		}
		
		return config;
	}
	
	public void Initialize()
	{
		this.FirstFloorRooms = new RoomInfo[49];
		this.BasementFloorRooms = new RoomInfo[49];
		this.SecondFloorRooms = new RoomInfo[49];
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
	
	/**
	 * This class is used to contain specific information about a room.
	 * @author WuestMan
	 *
	 */
	public static class RoomInfo
	{
		public int ID = -1;
		public String StructureName;
		public EnumFacing StructureFacing;
		
		/**
		 * Initializes a new instance of the RoomInfo class.
		 */
		public RoomInfo(int id)
		{
			this.ID = id;
			this.StructureFacing = EnumFacing.NORTH;
		}
		
		public static RoomInfo CreateFromNBTTag(NBTTagCompound compound)
		{
			if (compound.hasKey("ID"))
			{
				RoomInfo info = new RoomInfo(compound.getInteger("ID"));
				info.StructureName = compound.getString("name");
				info.StructureFacing = EnumFacing.byName(compound.getString("facing"));
				
				return info;
			}
			
			return null;
		}
		
		public void WriteToNBTTagCompound(NBTTagCompound compound)
		{
			NBTTagCompound tag = new NBTTagCompound();
			
			tag.setInteger("ID", this.ID);
			tag.setString("name", this.StructureName);
			tag.setString("facing", this.StructureFacing.getName());
			
			compound.setTag("room_" + this.ID, tag);
		}
	}
}
