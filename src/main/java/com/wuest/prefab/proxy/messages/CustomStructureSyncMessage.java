package com.wuest.prefab.proxy.messages;

import com.wuest.prefab.structures.custom.base.CustomStructureInfo;
import joptsimple.internal.Strings;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;

import java.util.ArrayList;

public class CustomStructureSyncMessage extends TagMessage {
    public static CustomStructureSyncMessage decode(FriendlyByteBuf buf) {
        return TagMessage.decode(buf, CustomStructureSyncMessage.class);
    }

    public CustomStructureSyncMessage(CompoundTag writeToNBTTagCompound) {
        super(writeToNBTTagCompound);
    }

    public CustomStructureSyncMessage() {
        super();

        this.tagMessage = new CompoundTag();
    }

    public void encodeStructures(ArrayList<CustomStructureInfo> infos) {
        ListTag listTag = new ListTag();

        if (infos != null && infos.size() > 0) {
            for (CustomStructureInfo info : infos) {
                CompoundTag tag = new CompoundTag();
                info.writeToTag(tag);

                listTag.add(tag);
            }
        }

        this.tagMessage.put("structures", listTag);
    }

    public ArrayList<CustomStructureInfo> getDecodedStructures() {
        ArrayList<CustomStructureInfo> returnedStructures = new ArrayList<>();

        ListTag listTag = this.tagMessage.getList("structures", 10);

        if (listTag != null) {
            for (Tag tag : listTag) {
                CompoundTag compoundTag = (CompoundTag) tag;
                CustomStructureInfo structureInfo = new CustomStructureInfo();
                structureInfo.readFromTag(compoundTag);

                if (!Strings.isNullOrEmpty(structureInfo.infoFileName)) {
                    returnedStructures.add(structureInfo);
                }
            }
        }

        return returnedStructures;
    }
}
