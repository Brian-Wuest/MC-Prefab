package com.wuest.prefab.Proxy;

import com.wuest.prefab.Blocks.BlockBoundary;
import com.wuest.prefab.Config.ServerModConfiguration;
import com.wuest.prefab.Gui.GuiPrefab;
import com.wuest.prefab.ModRegistry;
import com.wuest.prefab.Prefab;
import com.wuest.prefab.Structures.Gui.*;
import com.wuest.prefab.Structures.Items.StructureItem;
import com.wuest.prefab.Structures.Render.ShaderHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.item.ItemUseContext;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Supplier;

/**
 * @author WuestMan
 */
@SuppressWarnings({"WeakerAccess", "SpellCheckingInspection"})
public class ClientProxy extends CommonProxy {
	/**
	 * The hashmap of mod guis.
	 */
	public static HashMap<StructureItem, GuiStructure> ModGuis = new HashMap<>();

	public ServerModConfiguration serverConfiguration = null;

	public ClientProxy() {
		super();
	}

	/**
	 * Adds all of the Mod Guis to the HasMap.
	 */
	public static void AddGuis() {
		ClientProxy.ModGuis.put(ModRegistry.WareHouse.get(), new GuiWareHouse());
		ClientProxy.ModGuis.put(ModRegistry.ChickenCoop.get(), new GuiChickenCoop());
		ClientProxy.ModGuis.put(ModRegistry.ProduceFarm.get(), new GuiProduceFarm());
		ClientProxy.ModGuis.put(ModRegistry.TreeFarm.get(), new GuiTreeFarm());
		ClientProxy.ModGuis.put(ModRegistry.FishPond.get(), new GuiFishPond());
		ClientProxy.ModGuis.put(ModRegistry.StartHouse.get(), new GuiStartHouseChooser());
		ClientProxy.ModGuis.put(ModRegistry.AdvancedWareHouse.get(), new GuiAdvancedWareHouse());
		ClientProxy.ModGuis.put(ModRegistry.MonsterMasher.get(), new GuiMonsterMasher());
		ClientProxy.ModGuis.put(ModRegistry.HorseStable.get(), new GuiHorseStable());
		ClientProxy.ModGuis.put(ModRegistry.NetherGate.get(), new GuiNetherGate());
		ClientProxy.ModGuis.put(ModRegistry.VillagerHouses.get(), new GuiVillagerHouses());
		ClientProxy.ModGuis.put(ModRegistry.ModerateHouse.get(), new GuiModerateHouse());
		ClientProxy.ModGuis.put(ModRegistry.Bulldozer.get(), new GuiBulldozer());
		ClientProxy.ModGuis.put(ModRegistry.InstantBridge.get(), new GuiInstantBridge());
		ClientProxy.ModGuis.put(ModRegistry.StructurePart.get(), new GuiStructurePart());

		ClientProxy.ModGuis.put(ModRegistry.Barn.get(), new GuiBasicStructure());
		ClientProxy.ModGuis.put(ModRegistry.AdvancedCoop.get(), new GuiBasicStructure());
		ClientProxy.ModGuis.put(ModRegistry.AdvancedHorseStable.get(), new GuiBasicStructure());
		ClientProxy.ModGuis.put(ModRegistry.MachineryTower.get(), new GuiBasicStructure());
		ClientProxy.ModGuis.put(ModRegistry.DefenseBunker.get(), new GuiBasicStructure());
		ClientProxy.ModGuis.put(ModRegistry.MineshaftEntrance.get(), new GuiBasicStructure());
		ClientProxy.ModGuis.put(ModRegistry.EnderGateway.get(), new GuiBasicStructure());
		ClientProxy.ModGuis.put(ModRegistry.AquaBase.get(), new GuiBasicStructure());
		ClientProxy.ModGuis.put(ModRegistry.GrassyPlain.get(), new GuiBasicStructure());
		ClientProxy.ModGuis.put(ModRegistry.MagicTemple.get(), new GuiBasicStructure());
		ClientProxy.ModGuis.put(ModRegistry.GreenHouse.get(), new GuiBasicStructure());
		ClientProxy.ModGuis.put(ModRegistry.WatchTower.get(), new GuiBasicStructure());
		ClientProxy.ModGuis.put(ModRegistry.WelcomeCenter.get(), new GuiBasicStructure());
		ClientProxy.ModGuis.put(ModRegistry.Jail.get(), new GuiBasicStructure());
		ClientProxy.ModGuis.put(ModRegistry.Saloon.get(), new GuiBasicStructure());
		ClientProxy.ModGuis.put(ModRegistry.SkiLodge.get(), new GuiBasicStructure());
		ClientProxy.ModGuis.put(ModRegistry.WindMill.get(), new GuiBasicStructure());
		ClientProxy.ModGuis.put(ModRegistry.TownHall.get(), new GuiBasicStructure());
	}

	@Override
	public void preInit(FMLCommonSetupEvent event) {
		super.preInit(event);

		ClientProxy.AddGuis();

		// After all items have been registered and all recipes loaded, register any necessary renderer.
		Prefab.proxy.registerRenderers();
	}

	@Override
	public void init(FMLCommonSetupEvent event) {
		super.init(event);
	}

	@Override
	public void postinit(FMLCommonSetupEvent event) {
		super.postinit(event);
	}

	@Override
	public void RegisterEventHandler() {
		Optional<? extends ModContainer> modContainer = ModList.get().getModContainerById(Prefab.MODID);

		if (modContainer != null && modContainer.isPresent()) {
			Supplier<BiFunction<Minecraft, Screen, Screen>> prefabGui = () -> (minecraft, screen) -> new GuiPrefab(minecraft, screen);
			// TODO: The below line allows this mod to define a custom configuration GUI.
			//modContainer.get().registerExtensionPoint(ExtensionPoint.CONFIGGUIFACTORY, prefabGui);
		}
	}

	@Override
	public void registerRenderers() {
		ShaderHelper.Initialize();
	}

	@Override
	public ServerModConfiguration getServerConfiguration() {
		if (this.serverConfiguration == null) {
			// Get the server configuration.
			return CommonProxy.proxyConfiguration.serverConfiguration;
		} else {
			return this.serverConfiguration;
		}
	}

	@Override
	public void openGuiForItem(ItemUseContext itemUseContext) {
		for (Map.Entry<StructureItem, GuiStructure> entry : ClientProxy.ModGuis.entrySet()) {
			if (entry.getKey() == itemUseContext.getItem().getItem()) {
				GuiStructure screen = entry.getValue();
				screen.pos = itemUseContext.getPos();

				Minecraft.getInstance().displayGuiScreen(screen);
			}
		}
	}

	@Override
	public void clientSetup(FMLClientSetupEvent clientSetupEvent) {
		RenderTypeLookup.setRenderLayer(ModRegistry.BlockBoundary.get(), BlockBoundary::canRenderInLayer);

		// This render type (func_228643_e_) is the "cutout" render type.
		RenderTypeLookup.setRenderLayer(ModRegistry.GlassSlab.get(), RenderType.getCutout());

		RenderTypeLookup.setRenderLayer(ModRegistry.GlassStairs.get(), RenderType.getCutout());

		RenderTypeLookup.setRenderLayer(ModRegistry.PaperLantern.get(), RenderType.getCutout());

		// This is the "translucent" type.
		RenderTypeLookup.setRenderLayer(ModRegistry.BlockPhasing.get(), RenderType.getTranslucent());
	}
}