package com.wuest.prefab.config;

public enum ConfigCategory {
    General("General"),
    ChestOptions("Chest Options"),
    RecipeOptions("Recipe Options"),
    HouseOptions("House Options"),

    StructureOptions("Structure Options");

    private String name;

    private ConfigCategory(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static ConfigCategory getNextCategory(ConfigCategory category) {
        int categoryIndex = category.ordinal() + 1;
        ConfigCategory[] categories = ConfigCategory.values();

        if (categoryIndex >= categories.length) {
            categoryIndex = 0;
        }

        return categories[categoryIndex];
    }
}
