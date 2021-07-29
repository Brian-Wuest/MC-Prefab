package com.wuest.prefab;

import com.wuest.prefab.proxy.messages.TagMessage;
import com.wuest.prefab.structures.messages.StructureTagMessage;
import io.netty.buffer.Unpooled;
import io.netty.util.internal.StringUtil;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Direction;
import net.minecraft.util.text.StringTextComponent;
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

    public static ArrayList<StringTextComponent> WrapStringToLiterals(String value) {
        return Utils.WrapStringToLiterals(value, 50);
    }

    /**
     * This is a wrapper method to make sure that when minecraft changes the name of the StringTextComponent again it's a single place update.
     *
     * @param value The text to create the object from.
     * @return A StringTextComponent object.
     */
    public static StringTextComponent createTextComponent(String value) {
        return new StringTextComponent(value);
    }

    public static ArrayList<StringTextComponent> WrapStringToLiterals(String value, int width) {
        String[] values = Utils.WrapString(value, width);
        ArrayList<StringTextComponent> returnValue = new ArrayList<>();

        for (String stringValue : values) {
            returnValue.add(Utils.createTextComponent(stringValue));
        }

        return returnValue;
    }

    public static TagMessage createMessage(CompoundNBT tag) {
        return new TagMessage(tag);
    }

    public static <T extends TagMessage> T createGenericMessage(CompoundNBT tag, Class<T> tClass) {
        try {
            return tClass.getConstructor(CompoundNBT.class).newInstance(tag);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static StructureTagMessage createStructureMessage(CompoundNBT tag, StructureTagMessage.EnumStructureConfiguration structureConfiguration) {
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
