package com.wuest.prefab;

import net.minecraft.util.text.StringTextComponent;

public class GeneralUtils {

    /**
     * This is a wrapper method to make sure that when minecraft changes the name of the StringTextComponent again it's a single place update.
     * @param value The text to create the object from.
     * @return A StringTextComponent object.
     */
    public static StringTextComponent createTextComponent(String value) {
        return new StringTextComponent(value);
    }
}
