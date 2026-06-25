package com.prefab.structures.messages;

import com.prefab.structures.config.*;
import com.prefab.network.message.TagMessage;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;

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
    public StructureTagMessage(CompoundTag tagMessage, EnumStructureConfiguration structureConfig) {
        super(tagMessage);

        this.structureConfig = structureConfig;
    }

    public StructureTagMessage(FriendlyByteBuf friendlyByteBuf) {
        StructureTagMessage.decode(this, friendlyByteBuf);
    }

    public static StructureTagMessage decode(StructureTagMessage messageToUpdate, FriendlyByteBuf buf) {
        // This class is very useful in general for writing more complex objects.
        CompoundTag tag = buf.readNbt();

        if (tag == null) {
            // Should never _really_ happen, but just in-case.
            return new  StructureTagMessage(new CompoundTag(), EnumStructureConfiguration.Basic);
        }

        messageToUpdate.structureConfig = EnumStructureConfiguration.getFromIdentifier(tag.getInt("config").orElse(0));

        messageToUpdate.tagMessage = tag.getCompound("dataTag").orElse(new CompoundTag());

        return messageToUpdate;
    }

    public static void encode(StructureTagMessage message, FriendlyByteBuf buf) {
        CompoundTag tag = new CompoundTag();
        tag.putInt("config", message.structureConfig.identifier);
        tag.put("dataTag", message.tagMessage);

        buf.writeNbt(tag);
    }

    public EnumStructureConfiguration getStructureConfig() {
        return this.structureConfig;
    }

    @Override
    public void write(FriendlyByteBuf friendlyByteBuf) {
        StructureTagMessage.encode(this, friendlyByteBuf);
    }

    /**
     * This enum is used to contain the structures which will be used in message handling.
     *
     * @author WuestMan
     */
    public enum EnumStructureConfiguration {
        Basic(0, new BasicStructureConfiguration()),
        StartHouse(1, new HouseConfiguration()),
        ModerateHouse(2, new HouseImprovedConfiguration()),
        Bulldozer(3, new BulldozerConfiguration()),
        InstantBridge(4, new InstantBridgeConfiguration()),
        AdvancedHouse(5, new HouseAdvancedConfiguration());

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