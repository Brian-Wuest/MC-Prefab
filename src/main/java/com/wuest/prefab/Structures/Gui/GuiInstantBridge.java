package com.wuest.prefab.Structures.Gui;

import com.wuest.prefab.Events.ClientEventHandler;
import com.wuest.prefab.Gui.Controls.GuiCheckBox;
import com.wuest.prefab.Gui.GuiLangKeys;
import com.wuest.prefab.Structures.Base.EnumStructureMaterial;
import com.wuest.prefab.Structures.Config.InstantBridgeConfiguration;
import com.wuest.prefab.Structures.Messages.StructureTagMessage.EnumStructureConfiguration;
import com.wuest.prefab.Structures.Predefined.StructureInstantBridge;
import com.wuest.prefab.Structures.Render.StructureRenderHandler;
import com.wuest.prefab.Tuple;
import net.minecraft.client.gui.widget.button.AbstractButton;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.gui.widget.ExtendedButton;
import net.minecraftforge.fml.client.gui.widget.Slider;

@SuppressWarnings("SpellCheckingInspection")
public class GuiInstantBridge extends GuiStructure {
	private static final ResourceLocation structureTopDown = new ResourceLocation("prefab", "textures/gui/instant_bridge_top_down.png");
	protected InstantBridgeConfiguration configuration;
	private ExtendedButton btnMaterialType;
	private Slider sldrBridgeLength;
	private GuiCheckBox chckIncludeRoof;
	private Slider sldrInteriorHeight;

	public GuiInstantBridge() {
		super("Instant Bridge");
		this.structureConfiguration = EnumStructureConfiguration.InstantBridge;
		this.modifiedInitialXAxis = 210;
		this.modifiedInitialYAxis = 83;
	}

	@Override
	protected void Initialize() {
		this.configuration = ClientEventHandler.playerConfig.getClientConfig("InstantBridge", InstantBridgeConfiguration.class);
		this.configuration.pos = this.pos;

		// Get the upper left hand corner of the GUI box.
		int grayBoxX = this.getCenteredXAxis() - 213;
		int grayBoxY = this.getCenteredYAxis() - 83;

		// Create the buttons.
		this.btnMaterialType = this.createAndAddButton(grayBoxX + 10, grayBoxY + 20, 90, 20, this.configuration.bridgeMaterial.getTranslatedName());

		this.sldrBridgeLength = this.createAndAddSlider(grayBoxX + 147, grayBoxY + 20, 90, 20, "", "", 25, 75, this.configuration.bridgeLength, false, true, this::buttonClicked);

		this.chckIncludeRoof = this.createAndAddCheckBox(grayBoxX + 147, grayBoxY + 55, GuiLangKeys.translateString(GuiLangKeys.INCLUDE_ROOF), this.configuration.includeRoof, this::buttonClicked);

		this.sldrInteriorHeight = this.createAndAddSlider(grayBoxX + 147, grayBoxY + 90, 90, 20, "", "", 3, 8, this.configuration.interiorHeight, false, true, this::buttonClicked);

		this.sldrInteriorHeight.visible = this.chckIncludeRoof.isChecked();

		this.btnVisualize = this.createAndAddButton(grayBoxX + 10, grayBoxY + 90, 90, 20, GuiLangKeys.translateString(GuiLangKeys.GUI_BUTTON_PREVIEW));

		// Create the done and cancel buttons.
		this.btnBuild = this.createAndAddButton(grayBoxX + 10, grayBoxY + 136, 90, 20, GuiLangKeys.translateString(GuiLangKeys.GUI_BUTTON_BUILD));

		this.btnCancel = this.createAndAddButton(grayBoxX + 147, grayBoxY + 136, 90, 20, GuiLangKeys.translateString(GuiLangKeys.GUI_BUTTON_CANCEL));
	}

	@Override
	protected void preButtonRender(int x, int y) {
		super.preButtonRender(x, y);

		this.bindTexture(structureTopDown);

		GuiStructure.drawModalRectWithCustomSizedTexture(x + 250, y, 1, 165, 58, 165, 58);
	}

	@Override
	protected void postButtonRender(int x, int y) {
		this.drawString(GuiLangKeys.translateString(GuiLangKeys.BRIDGE_MATERIAL), x + 10, y + 10, this.textColor);

		if (this.chckIncludeRoof.isChecked()) {
			this.drawString(GuiLangKeys.translateString(GuiLangKeys.INTERIOR_HEIGHT), x + 147, y + 80, this.textColor);
		}

		this.drawString(GuiLangKeys.translateString(GuiLangKeys.BRIDGE_LENGTH), x + 147, y + 10, this.textColor);
	}

	/**
	 * Called by the controls from the buttonList when activated. (Mouse pressed for buttons)
	 */
	@Override
	public void buttonClicked(AbstractButton button) {
		int sliderValue = this.sldrBridgeLength.getValueInt();

		if (sliderValue > 75) {
			sliderValue = 75;
		} else if (sliderValue < 25) {
			sliderValue = 25;
		}

		this.configuration.bridgeLength = sliderValue;

		sliderValue = this.sldrInteriorHeight.getValueInt();

		if (sliderValue > 8) {
			sliderValue = 8;
		} else if (sliderValue < 3) {
			sliderValue = 3;
		}

		this.configuration.interiorHeight = sliderValue;
		this.configuration.includeRoof = this.chckIncludeRoof.isChecked();
		this.configuration.houseFacing = player.getHorizontalFacing().getOpposite();
		this.configuration.pos = this.pos;

		this.performCancelOrBuildOrHouseFacing(this.configuration, button);

		if (button == this.chckIncludeRoof) {
			this.configuration.includeRoof = this.chckIncludeRoof.isChecked();

			this.sldrInteriorHeight.visible = this.configuration.includeRoof;
		}
		if (button == this.btnMaterialType) {
			this.configuration.bridgeMaterial = EnumStructureMaterial.getMaterialByNumber(this.configuration.bridgeMaterial.getNumber() + 1);
			this.btnMaterialType.setMessage(this.configuration.bridgeMaterial.getTranslatedName());
		} else if (button == this.btnVisualize) {
			StructureInstantBridge structure = new StructureInstantBridge();
			structure.getClearSpace().getShape().setDirection(Direction.SOUTH);
			structure.setupStructure(this.configuration, this.pos);

			StructureRenderHandler.setStructure(structure, Direction.SOUTH, this.configuration);
			this.closeScreen();
		}
	}
}
