package com.wuest.prefab.config;

import com.wuest.prefab.Utils;
import net.minecraft.network.chat.Component;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.ArrayList;

public class ConfigOption<T> {
    private String name;
    private ForgeConfigSpec.ConfigValue<?> configValue;
    private T defaultValue;
    private int minRange;
    private int maxRange;
    private ArrayList<String> validValues;
    private ConfigCategory category;
    private String configType;
    private String hoverText;
    private Component hoverTextComponent;

    public ConfigOption() {
        this.validValues = new ArrayList<>();
    }

    public ConfigCategory getCategory() {
        return this.category;
    }

    public ConfigOption<T> setCategory(ConfigCategory value) {
        this.category = value;
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

    public ConfigOption<T> setHoverText(String value) {
        this.hoverText = value;
        this.hoverTextComponent = Utils.createTextComponent(value);
        return this;
    }

    public Component getHoverTextComponent() {
        return this.hoverTextComponent;
    }

    public ConfigOption<T> setHoverTextComponent(Component component) {
        this.hoverTextComponent = component;
        return this;
    }

    public void resetToDefault() {
        switch (this.getConfigType()) {
            case "Boolean": {
                this.getConfigValueAsBoolean().set((Boolean) this.getDefaultValue());
                break;
            }

            case "String": {
                this.getConfigValueAsString().set((String) this.getDefaultValue());
                break;
            }

            case "Integer": {
                this.getConfigValueAsInt().set((Integer) this.getDefaultValue());
                break;
            }
        }
    }
}
