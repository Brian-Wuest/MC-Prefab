package com.wuest.prefab.structures.custom.base;

import com.google.gson.annotations.Expose;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;

import java.nio.file.Path;
import java.util.ArrayList;

/**
 * Contains the details of a custom structure.
 */
public class CustomStructureInfo {

    /**
     * The display name to show in the GUI.
     */
    @Expose
    public String displayName;

    /**
     * The structure file name containing all the blocks used to generate the building.
     */
    @Expose
    public String structureFileName;

    /**
     * The required items for this custom structure.
     */
    @Expose
    public ArrayList<ItemInfo> requiredItems;

    /**
     * Contains the structure file path determined during registration.
     */
    public Path structureFilePath;

    /**
     * Contains the file name for this class.
     */
    public String infoFileName;

    public void writeToTag(CompoundTag tag) {
        tag.putString("displayName", this.displayName);
        tag.putString("structureFileName", this.structureFileName);
        tag.putString("infoFileName", this.infoFileName);

        if (this.requiredItems != null) {
            ListTag listTag = new ListTag();
            for (ItemInfo itemInfo : this.requiredItems) {
                CompoundTag itemTag = new CompoundTag();
                itemInfo.writeToTag(itemTag);

                listTag.add(itemTag);
            }

            tag.put("requiredItems", listTag);
        }
    }

    public void readFromTag(CompoundTag tag) {
        this.displayName = tag.getString("displayName");
        this.structureFileName = tag.getString("structureFileName");
        this.infoFileName = tag.getString("infoFileName");

        this.requiredItems = new ArrayList<>();
        ListTag listTag = tag.getList("requiredItems", 0);

        if (listTag != null) {
            for (Tag itemTag : listTag) {
                CompoundTag compoundTag = (CompoundTag) itemTag;
                ItemInfo itemInfo = new ItemInfo();
                itemInfo.readFromTag(compoundTag);

                if (itemInfo.count != 0) {
                    this.requiredItems.add(itemInfo);
                }
            }
        }
    }
}
