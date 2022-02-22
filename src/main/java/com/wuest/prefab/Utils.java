package com.wuest.prefab;

import com.wuest.prefab.proxy.messages.TagMessage;
import com.wuest.prefab.structures.messages.StructureTagMessage;
import io.netty.buffer.Unpooled;
import io.netty.util.internal.StringUtil;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.TextComponent;
import org.apache.commons.lang3.text.WordUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

public class Utils {
    public static String[] WrapString(String value) {
        return Utils.WrapString(value, 50);
    }

    public static String[] WrapString(String value, int width) {
        String result = WordUtils.wrap(value, width);
        String[] results = result.split("\n");

        String[] returnValue = new String[results.length];

        for (int i = 0; i < results.length; i++) {
            returnValue[i] = results[i].trim();
        }

        return returnValue;
    }

    public static ArrayList<TextComponent> WrapStringToLiterals(String value) {
        return Utils.WrapStringToLiterals(value, 50);
    }

    /**
     * This is a wrapper method to make sure that when minecraft changes the name of the StringTextComponent again it's a single place update.
     *
     * @param value The text to create the object from.
     * @return A StringTextComponent object.
     */
    public static TextComponent createTextComponent(String value) {
        return new TextComponent(value);
    }

    public static ArrayList<TextComponent> WrapStringToLiterals(String value, int width) {
        String[] values = Utils.WrapString(value, width);
        ArrayList<TextComponent> returnValue = new ArrayList<>();

        for (String stringValue : values) {
            returnValue.add(Utils.createTextComponent(stringValue));
        }

        return returnValue;
    }

    public static FriendlyByteBuf createMessageBuffer(CompoundTag tag) {
        TagMessage message = new TagMessage(tag);

        return Utils.createMessageBuffer(message);
    }

    public static FriendlyByteBuf createMessageBuffer(TagMessage tagMessage) {
        FriendlyByteBuf byteBuf = new FriendlyByteBuf(Unpooled.buffer());
        TagMessage.encode(tagMessage, byteBuf);

        return byteBuf;
    }

    public static <T extends TagMessage> T createGenericMessage(CompoundTag tag, Class<T> tClass) {
        try {
            return tClass.getConstructor(CompoundTag.class).newInstance(tag);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static StructureTagMessage createStructureMessage(CompoundTag tag, StructureTagMessage.EnumStructureConfiguration structureConfiguration) {
        return new StructureTagMessage(tag, structureConfiguration);
    }

    public static Direction getDirectionByName(String name) {
        if (!StringUtil.isNullOrEmpty(name)) {
            for (Direction direction : Direction.values()) {
                if (direction.getSerializedName().equalsIgnoreCase(name)) {
                    return direction;
                }
            }
        }

        return Direction.NORTH;
    }
}
