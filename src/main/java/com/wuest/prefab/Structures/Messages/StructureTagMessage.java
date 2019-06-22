package com.wuest.prefab.Structures.Messages;

import com.wuest.prefab.Proxy.Messages.TagMessage;
import com.wuest.prefab.Structures.Config.BasicStructureConfiguration;
import com.wuest.prefab.Structures.Config.BulldozerConfiguration;
import com.wuest.prefab.Structures.Config.ChickenCoopConfiguration;
import com.wuest.prefab.Structures.Config.FishPondConfiguration;
import com.wuest.prefab.Structures.Config.HorseStableConfiguration;
import com.wuest.prefab.Structures.Config.HouseConfiguration;
import com.wuest.prefab.Structures.Config.InstantBridgeConfiguration;
import com.wuest.prefab.Structures.Config.ModerateHouseConfiguration;
import com.wuest.prefab.Structures.Config.ModularHouseConfiguration;
import com.wuest.prefab.Structures.Config.MonsterMasherConfiguration;
import com.wuest.prefab.Structures.Config.NetherGateConfiguration;
import com.wuest.prefab.Structures.Config.ProduceFarmConfiguration;
import com.wuest.prefab.Structures.Config.StructureConfiguration;
import com.wuest.prefab.Structures.Config.StructurePartConfiguration;
import com.wuest.prefab.Structures.Config.TreeFarmConfiguration;
import com.wuest.prefab.Structures.Config.VillagerHouseConfiguration;
import com.wuest.prefab.Structures.Config.WareHouseConfiguration;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.network.ByteBufUtils;

/**
 * 
 * @author WuestMan
 *
 */
public class StructureTagMessage extends TagMessage
{
	protected EnumStructureConfiguration structureConfig;
	
	/**
	 * Initializes a new instance of the StructureTagMessage class.
	 */
	public StructureTagMessage()
	{
	}

	/**
	 * Initializes a new instance of the StructureTagMessage class.
	 * @param tagMessage The message to send.
	 */
	public StructureTagMessage(CompoundNBT tagMessage, EnumStructureConfiguration structureConfig)
	{
		super(tagMessage);
		
		this.structureConfig = structureConfig;
	}
	
	public EnumStructureConfiguration getStructureConfig()
	{
		return this.structureConfig;
	}
	
	public void setStructureConfig(EnumStructureConfiguration value)
	{
		this.structureConfig = value;
	}
	
	public static StructureTagMessage decode(PacketBuffer buf)
	{
		// This class is very useful in general for writing more complex objects.
		CompoundNBT tag = buf.readCompoundTag();
		StructureTagMessage returnValue = new StructureTagMessage();
		
		returnValue.structureConfig = EnumStructureConfiguration.getFromIdentifier(tag.getInt("config"));
		
		returnValue.tagMessage = tag.getCompound("dataTag");
		
		return returnValue;
	}

	public static void encode (StructureTagMessage message, PacketBuffer buf)
	{
		CompoundNBT tag = new CompoundNBT();
		tag.putInt("config", message.structureConfig.identifier);
		tag.put("dataTag", message.tagMessage);
		
		buf.writeCompoundTag(tag);
	}
	
	/**
	 * This enum is used to contain the structures which will be used in message handling.
	 * @author WuestMan
	 *
	 */
	public enum EnumStructureConfiguration
	{
		Basic(0, new BasicStructureConfiguration()),
		ChickenCoop(1, new ChickenCoopConfiguration()),
		AdvancedWareHouse(2, new WareHouseConfiguration()),
		FishPond(3, new FishPondConfiguration()),
		HorseStable(4, new HorseStableConfiguration()),
		ModularHouse(5, new ModularHouseConfiguration()),
		MonsterMasher(6, new MonsterMasherConfiguration()),
		NetherGate(7, new NetherGateConfiguration()),
		ProduceFarm(8, new ProduceFarmConfiguration()),
		StartHouse(9, new HouseConfiguration()),
		TreeFarm(10, new TreeFarmConfiguration()),
		VillagerHouses(11, new VillagerHouseConfiguration()),
		WareHouse(12, new WareHouseConfiguration()),
		ModerateHouse(13, new ModerateHouseConfiguration()),
		Bulldozer(14, new BulldozerConfiguration()),
		InstantBridge(15, new InstantBridgeConfiguration()),
		Parts(16, new StructurePartConfiguration());
		
		private <T extends StructureConfiguration> EnumStructureConfiguration(int identifier, T structureConfig)
		{
			this.identifier = identifier;
			this.structureConfig = structureConfig;
		}
		
		public int identifier;
		public StructureConfiguration structureConfig;
		
		public static EnumStructureConfiguration getFromIdentifier(int identifier)
		{
			for (EnumStructureConfiguration config : EnumStructureConfiguration.values())
			{
				if (config.identifier == identifier)
				{
					return config;
				}
			}
			
			return EnumStructureConfiguration.Basic;
		}
	}
}