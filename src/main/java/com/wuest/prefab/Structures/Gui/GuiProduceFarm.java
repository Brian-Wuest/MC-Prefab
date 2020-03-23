package com.wuest.prefab.Structures.Gui;

import com.wuest.prefab.Events.ClientEventHandler;
import com.wuest.prefab.Gui.GuiLangKeys;
import com.wuest.prefab.Structures.Config.ProduceFarmConfiguration;
import com.wuest.prefab.Structures.Messages.StructureTagMessage.EnumStructureConfiguration;
import com.wuest.prefab.Structures.Predefined.StructureProduceFarm;
import com.wuest.prefab.Structures.Render.StructureRenderHandler;
import com.wuest.prefab.Tuple;
import net.minecraft.client.gui.widget.button.AbstractButton;
import net.minecraft.item.DyeColor;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.gui.widget.ExtendedButton;

/**
 * @author WuestMan
 */
public class GuiProduceFarm extends GuiStructure {
	private static final ResourceLocation houseTopDown = new ResourceLocation("prefab", "textures/gui/produce_farm_top_down.png");
	protected ProduceFarmConfiguration configuration;
	private ExtendedButton btnGlassColor;

	public GuiProduceFarm() {
		super("Produce Farm");
		this.structureConfiguration = EnumStructureConfiguration.ProduceFarm;
	}

	@Override
	public void Initialize() {
		this.configuration = ClientEventHandler.playerConfig.getClientConfig("Produce Farm", ProduceFarmConfiguration.class);
		this.configuration.pos = this.pos;

		// Get the upper left hand corner of the GUI box.
		int grayBoxX = this.getCenteredXAxis() - 210;
		int grayBoxY = this.getCenteredYAxis() - 83;

		// Create the buttons.
		this.btnGlassColor = this.createAndAddButton(grayBoxX + 10, grayBoxY + 20, 90, 20, GuiLangKeys.translateDye(this.configuration.dyeColor));

		this.btnVisualize = this.createAndAddButton(grayBoxX + 10, grayBoxY + 90, 90, 20, GuiLangKeys.translateString(GuiLangKeys.GUI_BUTTON_PREVIEW));

		// Create the done and cancel buttons.
		this.btnBuild = this.createAndAddButton(grayBoxX + 10, grayBoxY + 136, 90, 20, GuiLangKeys.translateString(GuiLangKeys.GUI_BUTTON_BUILD));

		this.btnCancel = this.createAndAddButton(grayBoxX + 147, grayBoxY + 136, 90, 20, GuiLangKeys.translateString(GuiLangKeys.GUI_BUTTON_CANCEL));
	}

	@Override
	protected Tuple<Integer, Integer> getAdjustedXYValue() {
		return new Tuple<>(this.getCenteredXAxis() - 210, this.getCenteredYAxis() - 83);
	}

	@Override
	protected void preButtonRender(int x, int y) {
		super.preButtonRender(x, y);

		this.minecraft.getTextureManager().bindTexture(houseTopDown);
		GuiProduceFarm.drawModalRectWithCustomSizedTexture(x + 250, y, 1, 170, 171, 170, 171);
	}

	@Override
	protected void postButtonRender(int x, int y) {
		this.minecraft.fontRenderer.drawString(GuiLangKeys.translateString(GuiLangKeys.GUI_STRUCTURE_GLASS), x + 10, y + 10, this.textColor);

		// Draw the text here.
		this.minecraft.fontRenderer.drawSplitString(GuiLangKeys.translateString(GuiLangKeys.GUI_BLOCK_CLICKED), x + 147, y + 10, 100, this.textColor);
		this.minecraft.fontRenderer.drawSplitString(GuiLangKeys.translateString(GuiLangKeys.PRODUCE_FARM_SIZE), x + 147, y + 50, 100, this.textColor);
	}

	/**
	 * Called by the controls from the buttonList when activated. (Mouse pressed for buttons)
	 */
	@Override
	public void buttonClicked(AbstractButton button) {
		this.performCancelOrBuildOrHouseFacing(this.configuration, button);

		if (button == this.btnGlassColor) {
			this.configuration.dyeColor = DyeColor.byId(this.configuration.dyeColor.getId() + 1);
			this.btnGlassColor.setMessage(GuiLangKeys.translateDye(this.configuration.dyeColor));
		} else if (button == this.btnVisualize) {
			StructureProduceFarm structure = StructureProduceFarm.CreateInstance(StructureProduceFarm.ASSETLOCATION, StructureProduceFarm.class);
			StructureRenderHandler.setStructure(structure, Direction.NORTH, this.configuration);
			assert this.minecraft != null;
			this.minecraft.displayGuiScreen(null);
		}
	}
}