package com.wuest.prefab.Structures.Gui;

import com.wuest.prefab.Events.ClientEventHandler;
import com.wuest.prefab.Gui.GuiLangKeys;
import com.wuest.prefab.Gui.GuiTabScreen;
import com.wuest.prefab.Structures.Config.HorseStableConfiguration;
import com.wuest.prefab.Structures.Messages.StructureTagMessage.EnumStructureConfiguration;
import com.wuest.prefab.Structures.Predefined.StructureHorseStable;
import com.wuest.prefab.Structures.Render.StructureRenderHandler;
import com.wuest.prefab.Tuple;
import javafx.util.Pair;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;

/**
 * @author WuestMan
 */
public class GuiHorseStable extends GuiStructure {
	private static final ResourceLocation structureTopDown = new ResourceLocation("prefab", "textures/gui/horse_stable_top_down.png");
	protected HorseStableConfiguration configuration;

	public GuiHorseStable() {
		super("Horse Stable");
		this.structureConfiguration = EnumStructureConfiguration.HorseStable;
	}

	@Override
	protected Tuple<Integer, Integer> getAdjustedXYValue() {
		return new Tuple<>(this.getCenteredXAxis() - 213, this.getCenteredYAxis() - 83);
	}

	@Override
	protected void preButtonRender(int x, int y) {
		super.preButtonRender(x, y);

		this.minecraft.getTextureManager().bindTexture(structureTopDown);
		GuiTabScreen.drawModalRectWithCustomSizedTexture(x + 250, y, 1, 104, 166, 104, 166);
	}

	@Override
	protected void postButtonRender(int x, int y) {
		// Draw the text here.
		this.minecraft.fontRenderer.drawString(GuiLangKeys.translateString(GuiLangKeys.GUI_STRUCTURE_FACING), x + 10, y + 10, this.textColor);

		// Draw the text here.
		this.minecraft.fontRenderer.drawSplitString(GuiLangKeys.translateString(GuiLangKeys.GUI_BLOCK_CLICKED), x + 147, y + 10, 95, this.textColor);
	}

	/**
	 * Called by the controls from the buttonList when activated. (Mouse pressed for buttons)
	 */
	@Override
	public void buttonClicked(Button button) {
		this.performCancelOrBuildOrHouseFacing(this.configuration, button);

		if (button == this.btnVisualize) {
			StructureHorseStable structure = StructureHorseStable.CreateInstance(StructureHorseStable.ASSETLOCATION, StructureHorseStable.class);
			StructureRenderHandler.setStructure(structure, Direction.NORTH, this.configuration);
			assert this.minecraft != null;
			this.minecraft.displayGuiScreen(null);
		}
	}

	@Override
	protected void Initialize() {
		this.configuration = ClientEventHandler.playerConfig.getClientConfig("Horse Stable", HorseStableConfiguration.class);
		this.configuration.pos = this.pos;

		// Get the upper left hand corner of the GUI box.
		int grayBoxX = (this.width / 2) - 213;
		int grayBoxY = (this.height / 2) - 83;

		// Create the buttons.
		this.btnVisualize = this.createAndAddButton(grayBoxX + 10, grayBoxY + 20, 90, 20, GuiLangKeys.translateString(GuiLangKeys.GUI_BUTTON_PREVIEW));

		// Create the done and cancel buttons.
		this.btnBuild = this.createAndAddButton(grayBoxX + 10, grayBoxY + 136, 90, 20, GuiLangKeys.translateString(GuiLangKeys.GUI_BUTTON_BUILD));

		this.btnCancel = this.createAndAddButton(grayBoxX + 147, grayBoxY + 136, 90, 20, GuiLangKeys.translateString(GuiLangKeys.GUI_BUTTON_CANCEL));
	}
}