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
		ClientProxy.ModGuis.put(ModRegistry.WareHouse(), new GuiWareHouse());
		ClientProxy.ModGuis.put(ModRegistry.ChickenCoop(), new GuiChickenCoop());
		ClientProxy.ModGuis.put(ModRegistry.ProduceFarm(), new GuiProduceFarm());
		ClientProxy.ModGuis.put(ModRegistry.TreeFarm(), new GuiTreeFarm());
		ClientProxy.ModGuis.put(ModRegistry.FishPond(), new GuiFishPond());
		ClientProxy.ModGuis.put(ModRegistry.StartHouse(), new GuiStartHouseChooser());
		ClientProxy.ModGuis.put(ModRegistry.AdvancedWareHouse(), new GuiAdvancedWareHouse());
		ClientProxy.ModGuis.put(ModRegistry.MonsterMasher(), new GuiMonsterMasher());
		ClientProxy.ModGuis.put(ModRegistry.HorseStable(), new GuiHorseStable());
		ClientProxy.ModGuis.put(ModRegistry.NetherGate(), new GuiNetherGate());
		ClientProxy.ModGuis.put(ModRegistry.BasicStructure(), new GuiBasicStructure());
		ClientProxy.ModGuis.put(ModRegistry.VillagerHouses(), new GuiVillagerHouses());
		ClientProxy.ModGuis.put(ModRegistry.ModerateHouse(), new GuiModerateHouse());
		ClientProxy.ModGuis.put(ModRegistry.Bulldozer(), new GuiBulldozer());
		ClientProxy.ModGuis.put(ModRegistry.InstantBridge(), new GuiInstantBridge());
		ClientProxy.ModGuis.put(ModRegistry.StructurePart(), new GuiStructurePart());
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
			if (entry.getKey().getClass() == itemUseContext.getItem().getItem().getClass()) {
				GuiStructure screen = entry.getValue();
				screen.pos = itemUseContext.getPos();

				Minecraft.getInstance().displayGuiScreen(screen);
			}
		}
	}

	@Override
	public void clientSetup(FMLClientSetupEvent clientSetupEvent) {
		RenderTypeLookup.setRenderLayer(ModRegistry.BoundaryBlock(), BlockBoundary::canRenderInLayer);

		// This render type (func_228643_e_) is the "cutout" render type.
		RenderTypeLookup.setRenderLayer(ModRegistry.GlassSlab(), RenderType.func_228643_e_());

		RenderTypeLookup.setRenderLayer(ModRegistry.PaperLantern(), RenderType.func_228643_e_());

		// This is the "translucent" type.
		RenderTypeLookup.setRenderLayer(ModRegistry.PhasingBlock(), RenderType.func_228645_f_());
	}
}