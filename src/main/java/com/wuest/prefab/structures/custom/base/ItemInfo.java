package com.wuest.prefab.structures.custom.base;

import com.google.common.base.Strings;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.JsonAdapter;
import com.wuest.prefab.util.ResourceLocationSerializer;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

/**
 * This class contains the item information.
 */
public class ItemInfo {

    /**
     * The item.
     */
    @Expose
    @JsonAdapter(ResourceLocationSerializer.class)
    public ResourceLocation item;

    /**
     * The required number of items.
     */
    @Expose
    public int count;

    /**
     * The registered item for this resource location.
     * This field is not exposed to the JSON as it is set at server start.
     */
    public Item registeredItem;

    public void writeToTag(CompoundTag tag) {
        tag.putString("resourceLocation", this.item.toString());
        tag.putInt("count", this.count);
    }

    public void readFromTag(CompoundTag tag) {
        String resourceLocation = tag.getString("resourceLocation");

        if (!Strings.isNullOrEmpty(resourceLocation)) {
            this.item = new ResourceLocation(resourceLocation);
            this.registeredItem = Registry.ITEM.get(this.item);
        }

        this.count = tag.getInt("count");
    }
}
