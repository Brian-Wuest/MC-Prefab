package com.wuest.prefab;

import io.netty.util.internal.StringUtil;
import net.minecraft.util.EnumFacing;
import org.apache.commons.lang3.text.WordUtils;

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

    public static EnumFacing getDirectionByName(String name) {
        if (!StringUtil.isNullOrEmpty(name)) {
            for (EnumFacing direction : EnumFacing.values()) {
                if (direction.getName().equalsIgnoreCase(name)) {
                    return direction;
                }
            }
        }

        return EnumFacing.NORTH;
    }
}
