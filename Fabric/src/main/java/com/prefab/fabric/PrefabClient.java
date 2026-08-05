package com.prefab.fabric;

import com.prefab.PrefabBase;
import com.prefab.config.ModConfiguration;
import com.prefab.config.RecipeMapGuiProvider;
import com.prefab.config.StructureOptionGuiProvider;
import com.prefab.fabric.events.ClientEvents;
import me.shedaniel.autoconfig.AutoConfigClient;
import me.shedaniel.autoconfig.gui.registry.GuiRegistry;
import net.fabricmc.api.ClientModInitializer;

/**
 * This class represents the client-side initialization.
 */
public class PrefabClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		PrefabBase.logger.info("Registering client-side components");
		ClientModRegistry.registerModComponents();

		GuiRegistry registry = AutoConfigClient.getGuiRegistry(ModConfiguration.class);
		RecipeMapGuiProvider providerMap = new RecipeMapGuiProvider();
		StructureOptionGuiProvider structureOptionGuiProvider = new StructureOptionGuiProvider();

		registry.registerPredicateProvider(providerMap, (field) -> field.getDeclaringClass() == ModConfiguration.class && field.getName().equals("recipes"));
		registry.registerPredicateProvider(structureOptionGuiProvider, (field) -> field.getDeclaringClass() == ModConfiguration.class && field.getName().equals("structureOptions"));

		ClientEvents.registerClientEvents();
	}
}
