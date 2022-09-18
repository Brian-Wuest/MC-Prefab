package com.wuest.prefab.registries;

public class ModRegistries {
    private final LightSwitchRegistry lightSwitchRegistry;

    public ModRegistries() {
        this.lightSwitchRegistry = new LightSwitchRegistry();
    }

    public LightSwitchRegistry getLightSwitchRegistry() {
        return lightSwitchRegistry;
    }
}
