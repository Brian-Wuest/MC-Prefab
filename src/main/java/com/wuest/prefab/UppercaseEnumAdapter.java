package com.wuest.prefab;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

public class UppercaseEnumAdapter implements JsonDeserializer<Enum> {
    @Override
    public Enum deserialize(JsonElement json, java.lang.reflect.Type type, JsonDeserializationContext context)
            throws JsonParseException {
        try {
            if(type instanceof Class && ((Class<?>) type).isEnum()) {
                return Enum.valueOf((Class<Enum>) type, json.getAsString().toUpperCase());
            }

            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
