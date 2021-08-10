package com.wuest.prefab.structures.config.enums;

import com.wuest.prefab.Prefab;
import com.wuest.prefab.structures.base.BuildShape;
import com.wuest.prefab.structures.base.PositionOffset;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BaseOption {

    private static final HashMap<String, ArrayList<BaseOption>> classOptions = new HashMap<>();
    private final String translationString;
    private final String assetLocation;
    private final ResourceLocation pictureLocation;
    private final BuildShape clearShape;
    private final PositionOffset clearPositionOffset;
    private final boolean hasBedColor;
    private final boolean hasGlassColor;

    protected BaseOption(
            String translationString,
            String assetLocation,
            String pictureLocation,
            Direction direction,
            int height,
            int width,
            int length,
            int offsetParallelToPlayer,
            int offsetToLeftOfPlayer,
            int heightOffset,
            boolean hasBedColor,
            boolean hasGlassColor) {
        this.translationString = translationString;
        this.assetLocation = assetLocation;
        this.pictureLocation = new ResourceLocation(Prefab.MODID, pictureLocation);
        this.clearShape = new BuildShape();
        this.clearPositionOffset = new PositionOffset();
        this.hasBedColor = hasBedColor;
        this.hasGlassColor = hasGlassColor;

        this.clearShape.setDirection(direction);
        this.clearShape.setHeight(height);
        this.clearShape.setWidth(width);
        this.clearShape.setLength(length);
        this.clearPositionOffset.setHorizontalOffset(direction, offsetParallelToPlayer);
        this.clearPositionOffset.setHorizontalOffset(direction.getCounterClockWise(), offsetToLeftOfPlayer);
        this.clearPositionOffset.setHeightOffset(heightOffset);

        BaseOption.addOption(this);
    }

    /**
     * @param translationString The translation string to use to find the option.
     * @return The option found.
     */
    public static BaseOption getOptionByTranslationString(String translationString) {
        for (Map.Entry<String, ArrayList<BaseOption>> mapping : BaseOption.classOptions.entrySet()) {
            for (BaseOption storedOption : mapping.getValue()) {
                if (storedOption.getTranslationString().equals(translationString)) {
                    return storedOption;
                }
            }
        }

        return null;
    }

    public static void addOption(BaseOption option) {
        String className = option.getClass().getName();

        ArrayList<BaseOption> options;

        if (!BaseOption.classOptions.containsKey(className)) {
            options = new ArrayList<>();
            BaseOption.classOptions.put(className, options);
        } else {
            options = BaseOption.classOptions.get(className);
        }

        options.add(option);
    }

    /**
     * @return Get's the translation string for the button when choosing this option.
     */
    public String getTranslationString() {
        return this.translationString;
    }

    /**
     * @return Get's the asset location to use to build the structure.
     */
    public String getAssetLocation() {
        return this.assetLocation;
    }

    /**
     * @return Get's the picture location to show when this option is chosen.
     */
    public ResourceLocation getPictureLocation() {
        return this.pictureLocation;
    }

    /**
     * @return Get the build shape for this option.
     */
    public BuildShape getClearShape() {
        return this.clearShape;
    }

    /**
     * The {@link PositionOffset} for the clear shape.
     *
     * @return A {@link PositionOffset} which describes where the clearing should start.
     */
    public PositionOffset getClearPositionOffset() {
        return this.clearPositionOffset;
    }

    /**
     * @return A value indicating whether the current option has bed color options.
     */
    public boolean getHasBedColor() {
        return this.hasBedColor;
    }

    /**
     * @return A value indicating whether the current option has glass color options.
     */
    public boolean getHasGlassColor() {
        return this.hasGlassColor;
    }

    public ArrayList<BaseOption> getSpecificOptions() {
        String className = this.getClass().getName();

        for (Map.Entry<String, ArrayList<BaseOption>> mapping : BaseOption.classOptions.entrySet()) {
            if (mapping.getKey().equals(className)) {
                return mapping.getValue();
            }
        }

        return null;
    }
}
