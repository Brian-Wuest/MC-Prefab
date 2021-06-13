package com.wuest.prefab.structures.base;

import com.google.gson.annotations.Expose;

/**
 * @author WuestMan
 */
public class BuildProperty {
    @Expose
    private String name;

    @Expose
    private String value;

    public BuildProperty() {
        this.Initialize();
    }

    public String getName() {
        return this.name;
    }

    public void setName(String value) {
        this.name = value;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String newValue) {
        this.value = newValue;
    }

    public void Initialize() {
        this.name = "";
        this.value = "";
    }
}
