package com.prefab.registries;

public class ModRegistries {
    private final LightSwitchRegistry lightSwitchRegistry;
    private final StrictBuildingRegistry strictBuildingRegistry;

    public ModRegistries() {
        this.lightSwitchRegistry = new LightSwitchRegistry();
        this.strictBuildingRegistry = new StrictBuildingRegistry();
    }

    public LightSwitchRegistry getLightSwitchRegistry() {
        return this.lightSwitchRegistry;
    }

    public  StrictBuildingRegistry getStrictBuildingRegistry() {
        return this.strictBuildingRegistry;
    }
}
