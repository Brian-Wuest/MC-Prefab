package com.wuest.prefab.Gui;

import com.electronwill.nightconfig.core.UnmodifiableConfig;
import com.wuest.prefab.Proxy.CommonProxy;
import javafx.util.Pair;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.Map;

public class GuiPrefab extends GuiBase {

	private Screen parentScreen;
	private ForgeConfigSpec spec;

	public GuiPrefab(Minecraft minecraft, Screen parent) {
		super("Prefab Configuration");
		this.parentScreen = parent;
		super.minecraft = minecraft;
		this.spec = CommonProxy.COMMON_SPEC;
	}

	@Override
	protected void Initialize() {
		// Get the upper left hand corner of the GUI box.
		Pair<Integer, Integer> adjustedXYValue = this.getAdjustedXYValue();
		int x = adjustedXYValue.getKey();
		int y = adjustedXYValue.getValue();

		this.createAndAddButton(x + 10, y + 90, 120, 20, "This is a cool button!");

		UnmodifiableConfig config = this.spec.getValues();

		for (Map.Entry<String, Object> entry : config.valueMap().entrySet()) {
			/*
			 * TODO: Create an entry for each object in the save. If it's a configuration (secondary layer), show a new screen for that layer.
			 *  Later during the save. Load the configuration file and make any staged updated.
			 * */
		}
	}

	@Override
	public void buttonClicked(Button button) {
		this.minecraft.displayGuiScreen(this.parentScreen);
	}

	@Override
	protected Pair<Integer, Integer> getAdjustedXYValue() {
		return new Pair<>(0, 0);
	}

	@Override
	protected void preButtonRender(int x, int y) {
		this.renderDirtBackground(0);

	}

	@Override
	protected void postButtonRender(int x, int y) {

	}
}
