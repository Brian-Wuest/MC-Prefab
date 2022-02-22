package com.wuest.prefab.util;

import com.google.gson.*;
import net.minecraft.resources.ResourceLocation;

import java.lang.reflect.Type;

/**
 * This class is used to serialize and de-serialize resource location objects.
 */
public class ResourceLocationSerializer implements JsonSerializer<ResourceLocation>, JsonDeserializer<ResourceLocation> {

    @Override
    public JsonElement serialize(ResourceLocation sourceValue, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(sourceValue.toString());
    }

    @Override
    public ResourceLocation deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return new ResourceLocation(json.getAsString());
    }
}