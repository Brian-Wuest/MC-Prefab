package com.wuest.prefab.Config;

import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.ArrayList;

public class ConfigOption<T> {
    private String name;
    private ForgeConfigSpec.ConfigValue<?> configValue;
    private T defaultValue;
    private int minRange;
    private int maxRange;
    private ArrayList<String> validValues;
    private String groupName;
    private String configType;
    private String hoverText;
    private StringTextComponent hoverTextComponent;

    public ConfigOption() {
        this.validValues = new ArrayList<>();
    }

    public String getGroupName() {
        return this.groupName;
    }

    public ConfigOption<T> setGroupName(String value) {
        this.groupName = value;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public ConfigOption<T> setName(String value) {
        this.name = value;
        return this;
    }

    public ForgeConfigSpec.ConfigValue<?> getConfigValue() {
        return this.configValue;
    }

    public ConfigOption<T> setConfigValue(ForgeConfigSpec.ConfigValue<?> value) {
        this.configValue = value;
        return this;
    }

    public ForgeConfigSpec.BooleanValue getConfigValueAsBoolean() {
        return (ForgeConfigSpec.BooleanValue) this.configValue;
    }

    public ForgeConfigSpec.IntValue getConfigValueAsInt() {
        return (ForgeConfigSpec.IntValue) this.configValue;
    }

    public ForgeConfigSpec.ConfigValue<String> getConfigValueAsString() {
        return (ForgeConfigSpec.ConfigValue<String>) this.configValue;
    }

    public T getDefaultValue() {
        return this.defaultValue;
    }

    public ConfigOption<T> setDefaultValue(T value) {
        this.defaultValue = value;
        return this;
    }

    public int getMinRange() {
        return this.minRange;
    }

    public ConfigOption<T> setMinRange(int value) {
        this.minRange = value;
        return this;
    }

    public int getMaxRange() {
        return this.maxRange;
    }

    public ConfigOption<T> setMaxRange(int value) {
        this.maxRange = value;
        return this;
    }

    public ArrayList<String> getValidValues() {
        return this.validValues;
    }

    public ConfigOption<T> setValidValues(ArrayList<String> value) {
        this.validValues = value;
        return this;
    }

    public String getConfigType() {
        return this.configType;
    }

    public ConfigOption<T> setConfigType(String value) {
        this.configType = value;
        return this;
    }

    public String getHoverText() {
        return hoverText;
    }

    public StringTextComponent getHoverTextComponent() {
        return this.hoverTextComponent;
    }

    public ConfigOption<T> setHoverText(String value) {
        this.hoverText = value;
        this.hoverTextComponent = new StringTextComponent(value);
        return this;
    }
}
