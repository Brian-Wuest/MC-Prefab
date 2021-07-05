package com.wuest.prefab.structures.messages;

import com.wuest.prefab.proxy.messages.TagMessage;
import com.wuest.prefab.structures.config.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;

/**
 * @author WuestMan
 */
public class StructureTagMessage extends TagMessage {
    private EnumStructureConfiguration structureConfig;

    /**
     * Initializes a new instance of the StructureTagMessage class.
     */
    public StructureTagMessage() {
    }

    /**
     * Initializes a new instance of the StructureTagMessage class.
     *
     * @param tagMessage The message to send.
     */
    public StructureTagMessage(CompoundNBT tagMessage, EnumStructureConfiguration structureConfig) {
        super(tagMessage);

        this.structureConfig = structureConfig;
    }

    public static StructureTagMessage decode(PacketBuffer buf) {
        // This class is very useful in general for writing more complex objects.
        CompoundNBT tag = buf.readNbt();
        StructureTagMessage returnValue = new StructureTagMessage();

        returnValue.structureConfig = EnumStructureConfiguration.getFromIdentifier(tag.getInt("config"));

        returnValue.tagMessage = tag.getCompound("dataTag");

        return returnValue;
    }

    public static void encode(StructureTagMessage message, PacketBuffer buf) {
        CompoundNBT tag = new CompoundNBT();
        tag.putInt("config", message.structureConfig.identifier);
        tag.put("dataTag", message.tagMessage);

        buf.writeNbt(tag);
    }

    EnumStructureConfiguration getStructureConfig() {
        return this.structureConfig;
    }

    /**
     * This enum is used to contain the structures which will be used in message handling.
     *
     * @author WuestMan
     */
    public enum EnumStructureConfiguration {
        Basic(0, new BasicStructureConfiguration()),
        AdvancedWareHouse(2, new WareHouseConfiguration()),
        MonsterMasher(6, new MonsterMasherConfiguration()),
        StartHouse(9, new HouseConfiguration()),
        WareHouse(12, new WareHouseConfiguration()),
        ModerateHouse(13, new ModerateHouseConfiguration()),
        Bulldozer(14, new BulldozerConfiguration()),
        InstantBridge(15, new InstantBridgeConfiguration()),
        Parts(16, new StructurePartConfiguration());

        public int identifier;
        public StructureConfiguration structureConfig;

        <T extends StructureConfiguration> EnumStructureConfiguration(int identifier, T structureConfig) {
            this.identifier = identifier;
            this.structureConfig = structureConfig;
        }

        public static EnumStructureConfiguration getFromIdentifier(int identifier) {
            for (EnumStructureConfiguration config : EnumStructureConfiguration.values()) {
                if (config.identifier == identifier) {
                    return config;
                }
            }

            return EnumStructureConfiguration.Basic;
        }

        public static EnumStructureConfiguration getByConfigurationInstance(StructureConfiguration structureConfig) {
            for (EnumStructureConfiguration configuration : EnumStructureConfiguration.values()) {
                if (configuration.structureConfig.getClass().equals(structureConfig.getClass())) {
                    return configuration;
                }
            }

            return null;
        }
    }
}