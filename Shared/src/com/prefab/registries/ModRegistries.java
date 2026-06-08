package com.prefab.registries;

public class ModRegistries {
    private final LightSwitchRegistry lightSwitchRegistry;
    private final StrictBuildingRegistry strictBuildingRegistry;

    private final PhasicBlockRegistry phasicBlockRegistry;

    public ModRegistries() {
        this.lightSwitchRegistry = new LightSwitchRegistry();
        this.strictBuildingRegistry = new StrictBuildingRegistry();
        this.phasicBlockRegistry = new PhasicBlockRegistry();
    }

    public LightSwitchRegistry getLightSwitchRegistry() {
        return this.lightSwitchRegistry;
    }

    public StrictBuildingRegistry getStrictBuildingRegistry() {
        return this.strictBuildingRegistry;
    }

    public PhasicBlockRegistry getPhasicBlockRegistry() {
        return this.phasicBlockRegistry;
    }
}
