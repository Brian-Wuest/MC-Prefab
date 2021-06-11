package com.wuest.prefab.Structures.Gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.wuest.prefab.Events.ClientEventHandler;
import com.wuest.prefab.Gui.GuiLangKeys;
import com.wuest.prefab.Gui.GuiUtils;
import com.wuest.prefab.Structures.Config.FishPondConfiguration;
import com.wuest.prefab.Structures.Messages.StructureTagMessage.EnumStructureConfiguration;
import com.wuest.prefab.Structures.Predefined.StructureFishPond;
import com.wuest.prefab.Structures.Render.StructureRenderHandler;
import com.wuest.prefab.Tuple;
import net.minecraft.client.gui.widget.button.AbstractButton;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;

/**
 * @author WuestMan
 */
public class GuiFishPond extends GuiStructure {
	private static final ResourceLocation structureTopDown = new ResourceLocation("prefab", "textures/gui/fish_pond_top_down.png");
	protected FishPondConfiguration configuration;

	public GuiFishPond() {
		super("Fish Pond");
		this.structureConfiguration = EnumStructureConfiguration.FishPond;
		this.modifiedInitialXAxis = 188;
		this.modifiedInitialYAxis = 83;
	}

	@Override
	protected void preButtonRender(MatrixStack matrixStack, int x, int y, int mouseX, int mouseY, float partialTicks) {
		super.preButtonRender(matrixStack, x, y, mouseX, mouseY, partialTicks);

		GuiUtils.bindTexture(structureTopDown);

		GuiUtils.drawModalRectWithCustomSizedTexture(x + 250, y, 1, 151, 149, 151, 149);
	}

	@Override
	protected void postButtonRender(MatrixStack matrixStack, int x, int y, int mouseX, int mouseY, float partialTicks) {
		this.drawSplitString(GuiLangKeys.translateString(GuiLangKeys.GUI_BLOCK_CLICKED), x + 147, y + 10, 95, this.textColor);
	}

	/**
	 * Called by the controls from the buttonList when activated. (Mouse pressed for buttons)
	 */
	@Override
	public void buttonClicked(AbstractButton button) {
		this.performCancelOrBuildOrHouseFacing(this.configuration, button);

		if (button == this.btnVisualize) {
			StructureFishPond structure = StructureFishPond.CreateInstance(StructureFishPond.ASSETLOCATION, StructureFishPond.class);
			StructureRenderHandler.setStructure(structure, Direction.NORTH, this.configuration);
			this.closeScreen();
		}
	}

	@Override
	protected void Initialize() {
		this.configuration = ClientEventHandler.playerConfig.getClientConfig("Fish Pond", FishPondConfiguration.class);
		this.configuration.pos = this.pos;

		// Get the upper left hand corner of the GUI box.
		Tuple<Integer, Integer> adjustedXYValue = this.getAdjustedXYValue();
		int grayBoxX = adjustedXYValue.getFirst();
		int grayBoxY = adjustedXYValue.getSecond();

		// Create the buttons.
		this.btnVisualize = this.createAndAddButton(grayBoxX + 10, grayBoxY + 90, 90, 20, GuiLangKeys.translateString(GuiLangKeys.GUI_BUTTON_PREVIEW));

		// Create the done and cancel buttons.
		this.btnBuild = this.createAndAddButton(grayBoxX + 10, grayBoxY + 136, 90, 20, GuiLangKeys.translateString(GuiLangKeys.GUI_BUTTON_BUILD));

		this.btnCancel = this.createAndAddButton(grayBoxX + 147, grayBoxY + 136, 90, 20, GuiLangKeys.translateString(GuiLangKeys.GUI_BUTTON_CANCEL));
	}
}
